package de.investitionskalkulator

import kotlin.math.floor

data class BerechnungsEingabe(
    val vermoegen: Int,
    val risiko: Double,       // z.B. 2.5 für 2,5 %
    val gebuehren: Double,
    val kaufkurs: Double,
    val stoploss: Double
)

sealed class BerechnungsErgebnis {
    data class Erfolg(
        val stuecke: Int,
        val kaufwert: Double,
        val maxVerlust: Double,
        val kaufwertUeberstehtVermoegen: Boolean = false
    ) : BerechnungsErgebnis()

    data class Fehler(val meldung: FehlerTyp) : BerechnungsErgebnis()
}

enum class FehlerTyp {
    UNGUELTIGE_EINGABE,
    KAUFKURS_NICHT_GROESSER_ALS_STOPLOSS,
    RISIKOKAPITAL_NULL_ODER_NEGATIV,
    NEGATIVE_WERTE
}

object Kalkulator {

    fun berechne(eingabe: BerechnungsEingabe): BerechnungsErgebnis {
        // Validierung: positive Werte
        if (eingabe.vermoegen <= 0 || eingabe.risiko <= 0.0 ||
            eingabe.gebuehren < 0 || eingabe.kaufkurs <= 0 || eingabe.stoploss <= 0
        ) {
            return BerechnungsErgebnis.Fehler(FehlerTyp.NEGATIVE_WERTE)
        }

        // Validierung: Kaufkurs > Stoploss
        if (eingabe.kaufkurs <= eingabe.stoploss) {
            return BerechnungsErgebnis.Fehler(FehlerTyp.KAUFKURS_NICHT_GROESSER_ALS_STOPLOSS)
        }

        // Risikokapital = Vermögen * Risiko / 100 - Gebühren
        val risikokapital = eingabe.vermoegen.toDouble() *
                eingabe.risiko / 100.0 - eingabe.gebuehren

        if (risikokapital <= 0) {
            return BerechnungsErgebnis.Fehler(FehlerTyp.RISIKOKAPITAL_NULL_ODER_NEGATIV)
        }

        // Wertpapier-Stücke (abgerundet)
        val kursSpanne = eingabe.kaufkurs - eingabe.stoploss
        val stuecke = floor(risikokapital / kursSpanne).toInt()

        // Kaufwert = Stücke × Kaufkurs
        val kaufwert = stuecke.toDouble() * eingabe.kaufkurs

        // Maximaler Verlust = (Kaufkurs - Stoploss) × Stücke
        val maxVerlust = kursSpanne * stuecke.toDouble()

        // Warnung: Kaufwert überschreitet investierbares Vermögen
        val kaufwertUeberstehtVermoegen = kaufwert > eingabe.vermoegen.toDouble()

        return BerechnungsErgebnis.Erfolg(
            stuecke                    = stuecke,
            kaufwert                   = kaufwert,
            maxVerlust                 = maxVerlust,
            kaufwertUeberstehtVermoegen = kaufwertUeberstehtVermoegen
        )
    }
}
