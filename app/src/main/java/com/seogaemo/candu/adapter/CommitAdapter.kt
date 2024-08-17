package com.seogaemo.candu.adapter

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.seogaemo.candu.R
import com.seogaemo.candu.data.Goal
import com.seogaemo.candu.databinding.AchievementItemBinding
import com.seogaemo.candu.databinding.CommitItemBinding
import com.seogaemo.candu.view.AchievementActivity

class CommitAdapter(private val commitList: List<Int>): RecyclerView.Adapter<CommitAdapter.CommitViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommitViewHolder {
        val binding = CommitItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CommitViewHolder(binding).also { handler ->

        }
    }

    override fun getItemCount(): Int {
        return commitList.size
    }

    override fun onBindViewHolder(holder: CommitViewHolder, position: Int) {
        holder.bind(commitList[position])
    }

    inner class CommitViewHolder(private val binding: CommitItemBinding):
        RecyclerView.ViewHolder(binding.root) {

        fun bind(count: Int) {
            val color = when(count) {
                1 -> Color.parseColor("#1affffff")
                2 -> Color.parseColor("#4dffffff")
                3 -> Color.parseColor("#80ffffff")
                4 -> Color.parseColor("#b2ffffff")
                5 -> Color.parseColor("#e5ffffff")
                6 -> Color.parseColor("#ffffffff")
                else -> Color.parseColor("#ffffffff")
            }
            binding.main.setCardBackgroundColor(color)
        }
    }

}