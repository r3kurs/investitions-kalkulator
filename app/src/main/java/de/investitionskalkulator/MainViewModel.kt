package de.investitionskalkulator

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class UiState(
    val vorgaben: Vorgaben = Vorgaben(),
    val ergebnis: BerechnungsErgebnis? = null,
    val gespeichert: Boolean = false,
    // geladen=false: DataStore noch nicht gelesen – UI-Felder noch NICHT befüllen.
    // geladen=true:  DataStore hat geantwortet – jetzt die echten Werte (oder
    //                Defaults, falls noch nie gespeichert) in die Felder schreiben.
    val geladen: Boolean = false
)

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repo = PreferencesRepository(application)

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repo.vorgabenFlow.collect { vorgaben ->
                _uiState.value = _uiState.value.copy(
                    vorgaben = vorgaben,
                    geladen  = true
                )
            }
        }
    }

    fun speichereVorgaben(vorgaben: Vorgaben) {
        viewModelScope.launch {
            repo.speichern(vorgaben)
            _uiState.value = _uiState.value.copy(
                vorgaben    = vorgaben,
                gespeichert = true
            )
        }
    }

    fun bestaetigeGespeichert() {
        _uiState.value = _uiState.value.copy(gespeichert = false)
    }

    /**
     * Berechnung mit ALLEN Werten direkt aus der UI.
     * Kein Zugriff auf _uiState.vorgaben – damit werden immer die sichtbaren
     * Feldwerte verwendet, unabhängig davon ob der Nutzer bereits gespeichert hat.
     */
    fun berechne(
        vermoegen: Int,
        risiko: Double,
        gebuehren: Double,
        kaufkurs: Double,
        stoploss: Double
    ) {
        val eingabe = BerechnungsEingabe(
            vermoegen = vermoegen,
            risiko    = risiko,
            gebuehren = gebuehren,
            kaufkurs  = kaufkurs,
            stoploss  = stoploss
        )
        val ergebnis = Kalkulator.berechne(eingabe)
        _uiState.value = _uiState.value.copy(ergebnis = ergebnis)
    }

    fun resetErgebnis() {
        _uiState.value = _uiState.value.copy(ergebnis = null)
    }
}
