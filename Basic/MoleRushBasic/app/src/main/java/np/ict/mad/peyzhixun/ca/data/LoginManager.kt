package np.ict.mad.peyzhixun.ca.data

class LoginManager(private val userDb: UserDatabaseHelper) {

    suspend fun registerUser(email: String, password: String, displayName: String): String? {
        val existing = userDb.getUserByEmail(email.trim())
        if (existing != null) return "Email already registered."

        userDb.addUser(
            UserEntity(
                email = email.trim(),
                password = password,
                displayName = displayName.trim()
            )
        )
        return null // success
    }

    suspend fun loginUser(email: String, password: String): UserEntity? {
        return userDb.loginUser(email.trim(), password)
    }
}
