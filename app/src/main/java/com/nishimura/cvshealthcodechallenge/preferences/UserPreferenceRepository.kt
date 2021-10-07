package com.nishimura.cvshealthcodechallenge.preferences

import android.app.Activity
import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


object UserPreferenceRepository {
    private const val sharedPrefKey = "USER_PREFERENCE_MAIN_KEY"
    private const val savedSearchKey = "SAVED_SEARCH_LIST_KEY"
    private val arrayListStringType = object : TypeToken<ArrayList<String>>() {}.type

    //saving list in Shared Preference
    suspend fun addSearchToHistory(newSearch: String, context: Context): Boolean {
        return withContext(Dispatchers.IO) {
            val preferences = context.getSharedPreferences(sharedPrefKey, Activity.MODE_PRIVATE)
            val gson = Gson()
            val oldList = (preferences.getString(savedSearchKey, null)?.let {
                //Remove any old copy of same search
                gson.fromJson<ArrayList<String>>(it, arrayListStringType)
                    .apply { removeAll { keywords -> keywords == newSearch } }
            } ?: ArrayList()).let {
                if (it.size > 4) it.subList(0, 4) else it
            }
            //Insert search at top
            oldList.add(0, newSearch)
            val json = gson.toJson(oldList)
            return@withContext with(preferences.edit()) {
                putString(savedSearchKey, json)
                commit()
            }
        }
    }

    //getting the list from shared preference
    suspend fun getSearchHistory(context: Context): ArrayList<String> {
        return withContext(Dispatchers.IO) {
            val preferences = context.getSharedPreferences(sharedPrefKey, Activity.MODE_PRIVATE)
            return@withContext (preferences.getString(savedSearchKey, null)?.let {
                Gson().fromJson(it, arrayListStringType)
            } ?: ArrayList())

        }
    }
}
