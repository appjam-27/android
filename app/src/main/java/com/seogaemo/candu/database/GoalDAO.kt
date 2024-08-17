package com.seogaemo.candu.database

import androidx.room.*
import com.seogaemo.candu.data.Goal

@Dao
interface GoalDAO {
    @Insert
    fun insertGoal(goal: Goal)

    @Update
    fun updateGoal(goal: Goal)

    @Query("SELECT * FROM Goal")
    fun getGoalList(): MutableList<Goal>

    @Delete
    fun deleteGoal(goal: Goal)
}