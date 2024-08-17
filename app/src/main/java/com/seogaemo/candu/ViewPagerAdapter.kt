package com.seogaemo.candu

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.seogaemo.candu.databinding.StoryItemBinding

class ViewPagerAdapter(private val list: List<String>, private val color: Int, private val title: String) : RecyclerView.Adapter<ViewPagerAdapter.ViewPagerHolder>()  {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPagerHolder {
        val binding = StoryItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )
        return ViewPagerHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewPagerHolder, position: Int) {
        holder.bind(list[position], title, color)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewPagerHolder(private val binding: StoryItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: String, title: String, color: Int) {
            binding.main.setBackgroundColor(color)
            binding.content.text = item
            binding.title.text = title
        }
    }

}
