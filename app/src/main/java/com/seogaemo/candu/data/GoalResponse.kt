package com.seogaemo.candu.data

data class GoalResponse(
    val chapters: List<Chapter>,
    val goal: String,
    val icon: String,
    val story: Story
)