package np.ict.mad.peyzhixun.ca.data

import android.content.Context

class LoginPreferences(context: Context) {

    private val prefs = context.getSharedPreferences("login_prefs", Context.MODE_PRIVATE)

    fun saveLogin(userId: Int, email: String, displayName: String) {
        prefs.edit()
            .putInt("user_id", userId)
            .putString("email", email)
            .putString("display_name", displayName)
            .putBoolean("remember", true)
            .apply()
    }

    fun clear() {
        prefs.edit().clear().apply()
    }

    fun isRemembered(): Boolean = prefs.getBoolean("remember", false)
    fun getUserId(): Int = prefs.getInt("user_id", -1)
    fun getDisplayName(): String = prefs.getString("display_name", "") ?: ""
    fun getEmail(): String = prefs.getString("email", "") ?: ""
}
