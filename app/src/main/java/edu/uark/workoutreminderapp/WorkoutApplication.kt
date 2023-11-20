package edu.uark.workoutreminderapp

import android.app.Application
import edu.uark.workoutreminderapp.model.WorkoutRepository
import edu.uark.workoutreminderapp.model.WorkoutDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob


class WorkoutApplication : Application() {
    val applicationScope = CoroutineScope(SupervisorJob())
    val database by lazy { WorkoutDatabase.getDatabase(this, applicationScope) }
    val repository by lazy { WorkoutRepository(database.workoutDao()) }
}