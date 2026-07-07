/*
 * Investment Calculator
 * Copyright (C) 2026  r3kurs
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 3 as
 * published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the LICENSE file in the project directory for details.
 */

package de.investitionskalkulator

import android.app.AlertDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.InputMethodManager
import android.widget.ScrollView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import de.investitionskalkulator.databinding.ActivityMainBinding
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale
import kotlin.math.abs

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()

    private val euroFormat: NumberFormat = NumberFormat.getCurrencyInstance(Locale.GERMANY)
    private val decimalFormat: NumberFormat = NumberFormat.getInstance(Locale.GERMANY).apply {
        minimumFractionDigits = 2
        maximumFractionDigits = 2
    }
    private val integerFormat: NumberFormat = NumberFormat.getIntegerInstance(Locale.GERMANY)

    private var vorgabenInitialized = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Dezimalfelder: Komma-Normalisierung + max. 2 Nachkommastellen
        addDezimalWatcher(binding.etGebuehren, maxNachkomma = 2)
        addDezimalWatcher(binding.etKaufkurs,  maxNachkomma = 2)
        addDezimalWatcher(binding.etStoploss,  maxNachkomma = 2)

        // Risiko: spezieller Watcher mit Schrittkorrektur
        setupRisikoWatcher()

        // Vermögen: Tausenderpunkt-Formatierung
        setupVermoegenWatcher()

        // Live-Risikokapital
        setupRisikokapitalWatcher()

        setupListeners()
        observeUiState()
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Dezimalfelder: Punkt→Komma, max. ein Komma, max. maxNachkomma Stellen
    // ─────────────────────────────────────────────────────────────────────────
    private fun addDezimalWatcher(et: TextInputEditText, maxNachkomma: Int) {
        et.addTextChangedListener(object : TextWatcher {
            private var isEditing = false
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                if (isEditing || s == null) return
                val raw = s.toString()
                val hasPunkt   = raw.contains('.')
                val kommaCount = raw.count { it == ',' }
                val nachkomma  = if (raw.contains(',')) raw.substringAfter(',').length else 0
                if (!hasPunkt && kommaCount <= 1 && nachkomma <= maxNachkomma) return

                isEditing = true
                var result = raw.replace('.', ',')
                val parts = result.split(',')
                result = if (parts.size > 2) parts[0] + "," + parts.drop(1).joinToString("") else result
                if (result.contains(',')) {
                    result = result.substringBefore(',') + "," + result.substringAfter(',').take(maxNachkomma)
                }
                et.setText(result)
                et.setSelection(result.length)
                isEditing = false
            }
        })
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Risiko-TextWatcher mit Sofort-Korrektur
    //
    // Regeln:
    //   • Punkt → Komma normalisieren
    //   • Maximal ein Komma
    //   • Maximal 1 Nachkommastelle
    //   • Vorkommateil darf nie > 2 Stellen haben (max. 99)
    //   • Nach Komma: nur "0" oder "5" gültig; alle anderen werden auf
    //     die nächste gültige 0,5-Stufe gerundet und ersetzt
    //   • Abschlussvalidierung (beim Verlassen / Speichern / Berechnen)
    //     prüft dann zusätzlich den gültigen Bereich (0,5–9,5 / 10–99)
    //
    // Warum keine harte Blockierung während der Eingabe:
    //   „10" ist gültig, aber der Nutzer tippt erst „1", dann „0" – das
    //   wäre mit blockierendem Filter nicht möglich. Deshalb: korrigieren
    //   statt blockieren, validieren beim Abschluss.
    // ─────────────────────────────────────────────────────────────────────────
    private fun setupRisikoWatcher() {
        binding.etRisiko.addTextChangedListener(object : TextWatcher {
            private var isEditing = false
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                if (isEditing || s == null) return
                val raw = s.toString()
                if (raw.isEmpty()) return

                var result = raw

                // 1. Punkt → Komma
                result = result.replace('.', ',')

                // 2. Mehrfache Kommas: erstes behalten
                val parts = result.split(',')
                result = if (parts.size > 2) parts[0] + "," + parts.drop(1).joinToString("") else result

                // 3. Max. 1 Nachkommastelle: kürzen
                if (result.contains(',')) {
                    val vor   = result.substringBefore(',')
                    val nach  = result.substringAfter(',').take(1)
                    result = "$vor,$nach"
                }

                // 4. Vorkommateil: max. 2 Stellen (Zahlen > 99 abschneiden)
                val vorkomma = result.substringBefore(',')
                if (!result.contains(',') && vorkomma.length > 2) {
                    result = vorkomma.take(2)
                }

                // 5. Nachkommastelle muss 0 oder 5 sein; wenn komplett eingegeben
                //    (genau 1 Zeichen nach Komma) und weder 0 noch 5 → auf 0 oder 5 runden
                if (result.contains(',')) {
                    val nachStr = result.substringAfter(',')
                    if (nachStr.length == 1 && nachStr != "0" && nachStr != "5") {
                        val nachZiffer = nachStr.toIntOrNull() ?: 0
                        // Runde zur nächsten 0 oder 5: 1,2,3→0 sieht komisch aus,
                        // daher: < 3 → 0, >= 3 und < 8 → 5, >= 8 → 0 (und Vorkomma +1)
                        val vor = result.substringBefore(',')
                        result = when {
                            nachZiffer in 1..2 -> "$vor,0"
                            nachZiffer in 3..7 -> "$vor,5"
                            else -> {
                                // ≥8 → aufrunden: Vorkomma +1, kein Nachkomma
                                val vorInt = vor.toIntOrNull() ?: 0
                                (vorInt + 1).toString()
                            }
                        }
                    }
                }

                // 6. Wenn ab ≥10 und ein Komma vorhanden → Komma + Nachkomma entfernen
                //    (aber nur wenn Vorkommateil bereits ≥ 2 Stellen, also Eingabe abgeschlossen)
                if (result.contains(',')) {
                    val vor = result.substringBefore(',').toIntOrNull()
                    if (vor != null && vor >= 10) {
                        result = vor.toString()
                    }
                }

                if (result == raw) return  // nichts geändert → kein Loop

                isEditing = true
                binding.etRisiko.setText(result)
                binding.etRisiko.setSelection(result.length)
                isEditing = false
            }
        })
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Vermögen: Live-Tausenderpunkt
    // ─────────────────────────────────────────────────────────────────────────
    private fun setupVermoegenWatcher() {
        binding.etVermoegen.addTextChangedListener(object : TextWatcher {
            private var isEditing = false
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                if (isEditing || s == null) return
                val raw    = s.toString()
                val digits = raw.replace(".", "")
                if (digits.isEmpty()) return
                val zahl      = digits.toLongOrNull()
                val formatted = if (zahl != null) integerFormat.format(zahl) else digits
                if (formatted == raw) return
                isEditing = true
                binding.etVermoegen.setText(formatted)
                binding.etVermoegen.setSelection(formatted.length)
                isEditing = false
            }
        })
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Live-Risikokapital
    // ─────────────────────────────────────────────────────────────────────────
    private fun setupRisikokapitalWatcher() {
        val watcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) { updateRisikokapitalAnzeige() }
        }
        binding.etVermoegen.addTextChangedListener(watcher)
        binding.etRisiko.addTextChangedListener(watcher)
    }

    private fun updateRisikokapitalAnzeige() {
        val vermoegen = binding.etVermoegen.text?.toString()?.trim()?.replace(".", "")?.toIntOrNull()
        val risiko    = parseDecimal(binding.etRisiko.text?.toString())
        if (vermoegen != null && risiko != null && vermoegen > 0 && risiko > 0.0) {
            binding.tvRisikokapital.text = euroFormat.format(vermoegen.toDouble() * risiko / 100.0)
        } else {
            binding.tvRisikokapital.text = getString(R.string.result_placeholder)
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Buttons
    // ─────────────────────────────────────────────────────────────────────────
    private fun setupListeners() {

        binding.btnSpeichern.setOnClickListener {
            hideSoftKeyboard()
            val vorgaben = parseVorgaben() ?: return@setOnClickListener
            viewModel.speichereVorgaben(vorgaben)
        }

        binding.btnBerechnen.setOnClickListener {
            hideSoftKeyboard()
            binding.tvVorgabenFehler.isVisible = false

            val vermoegen = binding.etVermoegen.text?.toString()?.trim()
                ?.replace(".", "")?.toIntOrNull()
            val risiko    = parseDecimal(binding.etRisiko.text?.toString())
            val gebuehren = parseDecimal(binding.etGebuehren.text?.toString())
            val kaufkurs  = parseDecimal(binding.etKaufkurs.text?.toString())
            val stoploss  = parseDecimal(binding.etStoploss.text?.toString())

            if (vermoegen == null || risiko == null || gebuehren == null
                || kaufkurs == null || stoploss == null) {
                showBerechnungsFehler(getString(R.string.err_ungueltige_eingabe))
                return@setOnClickListener
            }
            if (!isRisikoGueltig(risiko)) {
                showBerechnungsFehler(getString(R.string.err_risiko_ungueltig))
                return@setOnClickListener
            }

            viewModel.berechne(
                vermoegen = vermoegen,
                risiko    = risiko,
                gebuehren = gebuehren,
                kaufkurs  = kaufkurs,
                stoploss  = stoploss
            )
        }

        binding.btnHilfe.setOnClickListener { zeigHilfeDialog() }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Risiko-Validierung (Abschluss): wird beim Speichern + Berechnen genutzt
    // ─────────────────────────────────────────────────────────────────────────
    private fun isRisikoGueltig(risiko: Double): Boolean {
        if (risiko <= 0.0) return false
        return if (risiko < 10.0) {
            risiko >= 0.5 && (abs(risiko * 10.0 % 5.0) < 0.001)
        } else {
            risiko <= 99.0 && (abs(risiko - risiko.toLong().toDouble()) < 0.001)
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Hilfe-Dialog mit Versionsangabe am Ende
    // ─────────────────────────────────────────────────────────────────────────
    private fun zeigHilfeDialog() {
        val scrollView = ScrollView(this)
        val tv = TextView(this).apply {
            val version = BuildConfig.VERSION_NAME
            text = getString(R.string.hilfe_text) + "\n\nVersion $version"
            textSize = 14f
            setPadding(56, 40, 56, 32)
            setTextColor(resources.getColor(R.color.on_surface, theme))
            setLineSpacing(4f, 1.1f)
        }
        scrollView.addView(tv)

        AlertDialog.Builder(
            this,
            com.google.android.material.R.style.ThemeOverlay_Material3_Dialog_Alert
        )
            .setTitle(getString(R.string.hilfe_titel))
            .setView(scrollView)
            .setPositiveButton("✕ Schließen") { dialog, _ -> dialog.dismiss() }
            .show()
    }

    // ─────────────────────────────────────────────────────────────────────────
    // State Observer
    // ─────────────────────────────────────────────────────────────────────────
    private fun observeUiState() {
        lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                if (state.geladen && !vorgabenInitialized) {
                    populateVorgabenFelder(state.vorgaben)
                }
                if (state.gespeichert) {
                    Snackbar.make(binding.root, getString(R.string.msg_gespeichert),
                        Snackbar.LENGTH_SHORT).show()
                    viewModel.bestaetigeGespeichert()
                }
                renderErgebnis(state.ergebnis)
            }
        }
    }

    private fun populateVorgabenFelder(vorgaben: Vorgaben) {
        binding.etVermoegen.setText(integerFormat.format(vorgaben.vermoegen))
        val risikoFormatiert = if (vorgaben.risiko % 1.0 == 0.0) {
            vorgaben.risiko.toInt().toString()
        } else {
            String.format(Locale.GERMANY, "%.1f", vorgaben.risiko)
        }
        binding.etRisiko.setText(risikoFormatiert)
        binding.etGebuehren.setText(decimalFormat.format(vorgaben.gebuehren))
        vorgabenInitialized = true
        updateRisikokapitalAnzeige()
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Vorgaben parsen + validieren
    // ─────────────────────────────────────────────────────────────────────────
    private fun parseVorgaben(): Vorgaben? {
        val vermoegenText = binding.etVermoegen.text?.toString()?.trim() ?: ""
        val risiko        = parseDecimal(binding.etRisiko.text?.toString())
        val gebuehren     = parseDecimal(binding.etGebuehren.text?.toString())
        val vermoegen     = vermoegenText.replace(".", "").toIntOrNull()

        if (vermoegen == null || risiko == null || gebuehren == null) {
            showVorgabenFehler(getString(R.string.err_ungueltige_eingabe)); return null
        }
        if (vermoegen <= 0) {
            showVorgabenFehler(getString(R.string.err_positive_werte)); return null
        }
        if (!isRisikoGueltig(risiko)) {
            showVorgabenFehler(getString(R.string.err_risiko_ungueltig)); return null
        }
        if (gebuehren < 0.0) {
            showVorgabenFehler(getString(R.string.err_positive_werte)); return null
        }
        if (gebuehren > 99.99) {
            showVorgabenFehler(getString(R.string.err_gebuehren_zu_gross)); return null
        }
        val risikokapital = vermoegen.toDouble() * risiko / 100.0
        if (gebuehren >= risikokapital) {
            showVorgabenFehler(getString(R.string.err_gebuehren_zu_hoch)); return null
        }
        hideVorgabenFehler()
        return Vorgaben(vermoegen, risiko, gebuehren)
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Dezimal-Parser
    // ─────────────────────────────────────────────────────────────────────────
    private fun parseDecimal(raw: String?): Double? {
        if (raw.isNullOrBlank()) return null
        val text = raw.trim()
        return if (text.contains(','))
            text.replace(".", "").replace(',', '.').toDoubleOrNull()
        else
            text.toDoubleOrNull()
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Ergebnis rendern
    // ─────────────────────────────────────────────────────────────────────────
    private fun renderErgebnis(ergebnis: BerechnungsErgebnis?) {
        val placeholder = getString(R.string.result_placeholder)
        when (ergebnis) {
            null -> {
                hideBerechnungsFehler()
                binding.tvWarnKaufwert.isVisible   = false
                binding.tvWarnStueckeNull.isVisible = false
                binding.tvStuecke.text    = placeholder
                binding.tvKaufwert.text   = placeholder
                binding.tvMaxVerlust.text = placeholder
            }
            is BerechnungsErgebnis.Erfolg -> {
                hideBerechnungsFehler()
                binding.tvStuecke.text    = integerFormat.format(ergebnis.stuecke)
                binding.tvKaufwert.text   = euroFormat.format(ergebnis.kaufwert)
                binding.tvMaxVerlust.text = euroFormat.format(ergebnis.maxVerlust)
                binding.tvWarnKaufwert.isVisible   = ergebnis.kaufwertUeberstehtVermoegen
                // Hinweis bei 0 Stücken: Berechnung bleibt sichtbar
                binding.tvWarnStueckeNull.isVisible = (ergebnis.stuecke == 0)
            }
            is BerechnungsErgebnis.Fehler -> {
                val msg = when (ergebnis.meldung) {
                    FehlerTyp.UNGUELTIGE_EINGABE                   -> getString(R.string.err_ungueltige_eingabe)
                    FehlerTyp.KAUFKURS_NICHT_GROESSER_ALS_STOPLOSS -> getString(R.string.err_kaufkurs_kleiner)
                    FehlerTyp.RISIKOKAPITAL_NULL_ODER_NEGATIV      -> getString(R.string.err_risiko_zu_gering)
                    FehlerTyp.NEGATIVE_WERTE                       -> getString(R.string.err_positive_werte)
                }
                showBerechnungsFehler(msg)
                binding.tvWarnKaufwert.isVisible   = false
                binding.tvWarnStueckeNull.isVisible = false
                binding.tvStuecke.text    = placeholder
                binding.tvKaufwert.text   = placeholder
                binding.tvMaxVerlust.text = placeholder
            }
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Helfer
    // ─────────────────────────────────────────────────────────────────────────
    private fun showVorgabenFehler(msg: String) {
        binding.tvVorgabenFehler.text = "⚠ $msg"
        binding.tvVorgabenFehler.isVisible = true
    }
    private fun hideVorgabenFehler() { binding.tvVorgabenFehler.isVisible = false }
    private fun showBerechnungsFehler(msg: String) {
        binding.tvFehler.text = "⚠ $msg"
        binding.tvFehler.isVisible = true
    }
    private fun hideBerechnungsFehler() { binding.tvFehler.isVisible = false }
    private fun hideSoftKeyboard() {
        currentFocus?.let {
            (getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager)
                .hideSoftInputFromWindow(it.windowToken, 0)
        }
    }
}
