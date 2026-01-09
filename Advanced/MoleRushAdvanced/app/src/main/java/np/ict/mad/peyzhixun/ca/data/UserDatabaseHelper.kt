package np.ict.mad.peyzhixun.ca.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserDatabaseHelper {

    // Save a new user into the database
    @Insert
    suspend fun addUser(user: UserEntity)

    // Get user by email (used during registration check)
    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun getUserByEmail(email: String): UserEntity?

    // Get user by email + password (used during login)
    @Query(
        "SELECT * FROM users WHERE email = :email AND password = :password LIMIT 1"
    )
    suspend fun loginUser(email: String, password: String): UserEntity?
}
