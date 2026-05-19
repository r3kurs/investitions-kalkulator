package de.investitionskalkulator

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "investitions_vorgaben")

data class Vorgaben(
    val vermoegen: Int = 10_000,
    val risiko: Double = 2.0,      // z.B. 2.0 für 2 %, 1.5 für 1,5 %
    val gebuehren: Double = 9.90
)

class PreferencesRepository(private val context: Context) {

    companion object {
        private val KEY_VERMOEGEN = intPreferencesKey("vermoegen")
        private val KEY_RISIKO    = doublePreferencesKey("risiko_double")  // neuer Key (Double)
        private val KEY_GEBUEHREN = doublePreferencesKey("gebuehren")
    }

    val vorgabenFlow: Flow<Vorgaben> = context.dataStore.data.map { prefs ->
        Vorgaben(
            vermoegen = prefs[KEY_VERMOEGEN] ?: 10_000,
            risiko    = prefs[KEY_RISIKO]    ?: 2.0,
            gebuehren = prefs[KEY_GEBUEHREN] ?: 9.90
        )
    }

    suspend fun speichern(vorgaben: Vorgaben) {
        context.dataStore.edit { prefs ->
            prefs[KEY_VERMOEGEN] = vorgaben.vermoegen
            prefs[KEY_RISIKO]    = vorgaben.risiko
            prefs[KEY_GEBUEHREN] = vorgaben.gebuehren
        }
    }
}
