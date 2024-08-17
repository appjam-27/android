package com.seogaemo.candu

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.seogaemo.candu.data.Goal
import com.seogaemo.candu.databinding.AchievementItemBinding

class AchievementAdapter(private val achievementList: List<Goal>): RecyclerView.Adapter<AchievementAdapter.AchievementViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AchievementViewHolder {
        val binding = AchievementItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AchievementViewHolder(binding).also { handler ->

        }
    }

    override fun getItemCount(): Int {
        return achievementList.size
    }

    override fun onBindViewHolder(holder: AchievementViewHolder, position: Int) {
        holder.bind(achievementList[position])
    }

    inner class AchievementViewHolder(private val binding: AchievementItemBinding):
        RecyclerView.ViewHolder(binding.root) {

        fun bind(goal: Goal) {
            val icon = when (goal.item.icon) {
                "design" -> R.drawable.figma
                "activities" -> R.drawable.directions
                "movies" -> R.drawable.theaters
                "social" -> R.drawable.handshake
                "dev" -> R.drawable.dev
                "business" -> R.drawable.business
                else -> R.drawable.figma
            }
            binding.icon.setImageResource(icon)
            binding.main.setCardBackgroundColor(goal.color)
            binding.title.text = goal.item.goal
            binding.sub.text = goal.item.story.title
        }
    }

}