package com.example.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.example.data.model.FocusChoice
import com.example.data.model.FocusRecord
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.json.JSONArray
import org.json.JSONObject

data class FocusStats(
    val totalInterventions: Int,
    val studyChoices: Int,
    val phoneChoices: Int,
    val focusRatePercent: Int,
    val estimatedMinutesSaved: Int,
    val recentRecords: List<FocusRecord>
)

class StatsRepository(private val context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    private val _statsFlow = MutableStateFlow(loadStats())
    val statsFlow: StateFlow<FocusStats> = _statsFlow.asStateFlow()

    companion object {
        private const val PREFS_NAME = "study_guard_stats"
        private const val KEY_TOTAL_STUDIED = "total_studied"
        private const val KEY_TOTAL_PHONE = "total_phone"
        private const val KEY_HISTORY_JSON = "history_json"

        @Volatile
        private var INSTANCE: StatsRepository? = null

        fun getInstance(context: Context): StatsRepository {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: StatsRepository(context.applicationContext).also { INSTANCE = it }
            }
        }
    }

    fun loadStats(): FocusStats {
        val studied = prefs.getInt(KEY_TOTAL_STUDIED, 0)
        val phone = prefs.getInt(KEY_TOTAL_PHONE, 0)
        val total = studied + phone
        val rate = if (total > 0) (studied * 100) / total else 0
        val minutesSaved = studied * 20

        val historyJsonStr = prefs.getString(KEY_HISTORY_JSON, "[]") ?: "[]"
        val records = mutableListOf<FocusRecord>()
        try {
            val jsonArray = JSONArray(historyJsonStr)
            for (i in 0 until jsonArray.length()) {
                val obj = jsonArray.getJSONObject(i)
                records.add(
                    FocusRecord(
                        id = obj.optString("id", i.toString()),
                        timestamp = obj.optLong("timestamp", System.currentTimeMillis()),
                        choice = if (obj.optString("choice") == FocusChoice.STUDIED.name)
                            FocusChoice.STUDIED else FocusChoice.PHONE_USED,
                        note = obj.optString("note", "")
                    )
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return FocusStats(
            totalInterventions = total,
            studyChoices = studied,
            phoneChoices = phone,
            focusRatePercent = rate,
            estimatedMinutesSaved = minutesSaved,
            recentRecords = records.sortedByDescending { it.timestamp }
        )
    }

    fun recordChoice(choice: FocusChoice, note: String = "") {
        val currentStudied = prefs.getInt(KEY_TOTAL_STUDIED, 0)
        val currentPhone = prefs.getInt(KEY_TOTAL_PHONE, 0)

        val newStudied = if (choice == FocusChoice.STUDIED) currentStudied + 1 else currentStudied
        val newPhone = if (choice == FocusChoice.PHONE_USED) currentPhone + 1 else currentPhone

        val historyJsonStr = prefs.getString(KEY_HISTORY_JSON, "[]") ?: "[]"
        val jsonArray = try {
            JSONArray(historyJsonStr)
        } catch (_: Exception) {
            JSONArray()
        }

        val newRecordObj = JSONObject().apply {
            put("id", System.currentTimeMillis().toString())
            put("timestamp", System.currentTimeMillis())
            put("choice", choice.name)
            put("note", note)
        }

        val updatedArray = JSONArray()
        updatedArray.put(newRecordObj)
        val limit = minOf(jsonArray.length(), 49)
        for (i in 0 until limit) {
            updatedArray.put(jsonArray.get(i))
        }

        prefs.edit().apply {
            putInt(KEY_TOTAL_STUDIED, newStudied)
            putInt(KEY_TOTAL_PHONE, newPhone)
            putString(KEY_HISTORY_JSON, updatedArray.toString())
            apply()
        }

        _statsFlow.value = loadStats()
    }

    fun resetStats() {
        prefs.edit().clear().apply()
        _statsFlow.value = loadStats()
    }
}
