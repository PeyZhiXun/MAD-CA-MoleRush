package np.ict.mad.peyzhixun.ca.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ScoreDatabaseHelper {

    //Insert one score record when a game ends
    @Insert
    suspend fun addScore(score: ScoreEntity)

    //Best score for one user
    @Query("SELECT MAX(score) FROM scores WHERE userId = :userId")
    suspend fun getBestForUser(userId: Int): Int?

    //Best score overall
    @Query("SELECT MAX(score) FROM scores")
    suspend fun getGlobalBest(): Int?

    //Top 10 scores (with user display name)
    @Query("""
        SELECT users.displayName AS displayName, MAX(scores.score) AS bestScore
        FROM scores
        INNER JOIN users ON users.id = scores.userId
        GROUP BY scores.userId
        ORDER BY bestScore DESC
        LIMIT 10
    """)
    suspend fun getTop5BestScores(): List<ScoreRow>
}

data class ScoreRow(
    val displayName: String,
    val bestScore: Int
)
