package np.ict.mad.peyzhixun.ca.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [UserEntity::class, ScoreEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDb(): UserDatabaseHelper
    abstract fun scoreDb(): ScoreDatabaseHelper

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val db = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_db"
                ).build()
                INSTANCE = db
                db
            }
        }
    }
}
