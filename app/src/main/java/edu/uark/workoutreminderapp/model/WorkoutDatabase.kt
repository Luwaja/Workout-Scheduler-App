package edu.uark.workoutreminderapp.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

// This is the Room Database that will store the workouts for our application.
@Database(entities = arrayOf(Workout::class), version = 1, exportSchema = false)
abstract class WorkoutDatabase : RoomDatabase() {
    abstract fun workoutDao(): WorkoutDao

    private class WorkoutDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {
                    val workoutDao = database.workoutDao()

                    // Delete all of the workouts from the database to start.
                    workoutDao.deleteAllWorkouts()

                    // Add sample workouts to start in the database for testing.
                    // TODO: Add these later
                }
            }
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: WorkoutDatabase? = null

        fun getDatabase(
            context: Context,
            scope: CoroutineScope
        ): WorkoutDatabase {
            return INSTANCE?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    WorkoutDatabase::class.java,
                    "workout_database"
                )
                    .addCallback(WorkoutDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                // return the instance
                instance
            }
        }
    }
}