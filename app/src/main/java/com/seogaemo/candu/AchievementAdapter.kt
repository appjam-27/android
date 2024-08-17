package com.seogaemo.candu

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.seogaemo.candu.data.Goal
import com.seogaemo.candu.databinding.AchievementItemBinding

class AchievementAdapter(private val achievementList: List<Goal>): RecyclerView.Adapter<AchievementAdapter.AchievementViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AchievementViewHolder {
        val binding = AchievementItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AchievementViewHolder(binding).also { handler ->
            binding.root.setOnClickListener {
                val intent = Intent(binding.root.context, AchievementActivity::class.java)
                intent.putExtra("item", achievementList[handler.adapterPosition])
                binding.root.context.startActivity(intent)

                (binding.root.context as Activity).overridePendingTransition(R.anim.anim_slide_in_from_right_fade_in, R.anim.anim_fade_out)
            }
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