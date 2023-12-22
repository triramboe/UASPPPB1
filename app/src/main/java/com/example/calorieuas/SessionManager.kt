package com.example.calorieuas
import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = sharedPreferences.edit()

    companion object {
        private const val PREF_NAME = "YourAppPrefs"
        private const val KEY_IS_LOGGED_IN = "isLoggedIn"

        private const val USER_ROLE = "role"

    }
    var isLoggedIn: Boolean
        get() = sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false)
        set(value) {
            editor.putBoolean(KEY_IS_LOGGED_IN, value)
            editor.apply()
        }
    var userRole: String?
        get() = sharedPreferences.getString(USER_ROLE, "user")
        set(value) {
            editor.putString(USER_ROLE, value)
            editor.apply()
        }
    fun clearSession() {
        editor.clear().apply()
    }
}