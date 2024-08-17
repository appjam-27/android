package com.seogaemo.candu

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.seogaemo.candu.data.Goal
import com.seogaemo.candu.databinding.ActivityAchievementBinding

class AchievementActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAchievementBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAchievementBinding.inflate(layoutInflater)
        setContentView(binding.root)

        intent.getParcelableExtra<Goal>("item")?.let {
            binding.apply {
                backButton.setOnClickListener { finish() }
                shareButton.setOnClickListener {

                }
                val image = when (it.item.icon) {
                    "design" -> R.drawable.figma
                    "activities" -> R.drawable.directions
                    "movies" -> R.drawable.theaters
                    "social" -> R.drawable.handshake
                    "dev" -> R.drawable.dev
                    "business" -> R.drawable.business
                    else -> R.drawable.figma
                }
                icon.setImageResource(image)
                title.text = it.item.goal
                rank.text = "${it.level}단계"
                nextTitle.text = it.item.goal
                studyTitle.text = it.item.chapters[it.level].title
                studySub.text = it.item.chapters[it.level].desc
                time.text = it.item.chapters[it.level].duration

                viewPager.apply {
                    adapter = ViewPagerAdapter(it.item.story.content, it.color, it.item.story.title)
                    binding.indicator.attachTo(this)
                }
            }

        }
    }
}