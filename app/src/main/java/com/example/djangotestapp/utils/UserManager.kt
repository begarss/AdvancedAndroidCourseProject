package com.example.djangotestapp.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.*
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.clear
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.preferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserManager(context: Context) {
    private val applicationContext = context.applicationContext
    private val dataStore: DataStore<Preferences>

    companion object {
        private val KEY_AUTH = preferencesKey<String>("key_auth")
        private val USER_ID = preferencesKey<Int>("userid")
        private val USER_NAME = preferencesKey<String>("name")
        private val USER_AVA = preferencesKey<String>("ava")
        private val USER_SUPER = preferencesKey<Boolean>("is_superuser")
        private val USER_POST_COUNT = preferencesKey<Int>("postsCount")
    }

    init {
        dataStore = applicationContext.createDataStore("user_prefs")
    }

    val authToken: Flow<String?>
        get() = dataStore.data.map { preferences ->
            preferences[KEY_AUTH]
        }
    val userName: Flow<String?>
        get() = dataStore.data.map { preferences ->
            preferences[USER_NAME]
        }
    val userID: Flow<Int?>
        get() = dataStore.data.map { preferences ->
            preferences[USER_ID]
        }
    val userAva: Flow<String?>
        get() = dataStore.data.map { preferences ->
            preferences[USER_AVA]
        }
    val isSuper: Flow<Boolean?>
        get() = dataStore.data.map { preferences ->
            preferences[USER_SUPER]
        }
    val postCount: Flow<Int?>
        get() = dataStore.data.map { preferences ->
            preferences[USER_POST_COUNT]
        }

    suspend fun saveAuthToken(authToken: String) {
        dataStore.edit { preferences ->
            preferences[KEY_AUTH] = authToken
        }
    }

    suspend fun saveUserName(name: String) {
        dataStore.edit { preferences ->
            preferences[USER_NAME] = name
        }
    }
    suspend fun saveUserId(id: Int) {
        dataStore.edit { preferences ->
            preferences[USER_ID] = id
        }
    }
    suspend fun saveUserAva(ava: String) {
        dataStore.edit { preferences ->
            preferences[USER_AVA] = ava
        }
    }
    suspend fun saveUserSuperState(issuper: Boolean) {
        dataStore.edit { preferences ->
            preferences[USER_SUPER] = issuper
        }
    }

    suspend fun saveUserPostsCount(count: Int) {
        dataStore.edit { preferences ->
            preferences[USER_POST_COUNT] = count
        }
    }



    suspend fun clear() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}