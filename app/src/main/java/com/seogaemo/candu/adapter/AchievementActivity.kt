package com.seogaemo.candu.adapter

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.seogaemo.candu.R
import com.seogaemo.candu.data.Goal
import com.seogaemo.candu.data.GoalRequest
import com.seogaemo.candu.data.GoalResponse
import com.seogaemo.candu.database.AppDatabase
import com.seogaemo.candu.databinding.ActivityAchievementBinding
import com.seogaemo.candu.network.RetrofitAPI
import com.seogaemo.candu.network.RetrofitClient
import com.seogaemo.candu.util.Dialog.createLoadingDialog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AchievementActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAchievementBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAchievementBinding.inflate(layoutInflater)
        setContentView(binding.root)

        intent.getParcelableExtra<Goal>("item")?.let {
            binding.apply {
                oButton.setOnClickListener { view ->
                    val intent = Intent(this@AchievementActivity, LearningActivity::class.java)
                    intent.putExtra("item", it)
                    startActivity(intent)
                    overridePendingTransition(
                        R.anim.anim_slide_in_from_right_fade_in,
                        R.anim.anim_fade_out
                    )
                }
                xButton.setOnClickListener { view ->
                    CoroutineScope(Dispatchers.IO).launch {
                        val update = goalNew(it.item.goal)
                        val item = it
                        if (update != null) {
                            item.item = update
                            val goalDao = AppDatabase.getDatabase(this@AchievementActivity)?.goalDao()
                            goalDao?.updateGoal(item)

                            withContext(Dispatchers.Main) {
                                initView(item)
                            }
                        }
                    }
                }

                backButton.setOnClickListener {
                    finish()
                    overridePendingTransition(
                        R.anim.anim_slide_in_from_left_fade_in,
                        R.anim.anim_fade_out
                    )
                }
                shareButton.setOnClickListener {

                    Intent.createChooser(
                        Intent(Intent.ACTION_SEND_MULTIPLE).apply {
                            type = "text/plain"
                            val content = "목표를 공유했어요!\n하단 설명을 통해 어떤 목표인지 확인하세요."
                            putExtra(Intent.EXTRA_TEXT,"$content")
                        },
                        "친구에게 목표 공유하기"
                    )
                }

                initView(it)
            }

        }
    }
    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.anim_slide_in_from_left_fade_in, R.anim.anim_fade_out)
    }

    private fun initView(it: Goal) {
        binding.apply {
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
            root.setBackgroundColor(it.color)

            viewPager.apply {
                val list = it.item.story.content.subList(0, it.level+1)
                adapter = ViewPagerAdapter(list, it.color, it.item.story.title)
                binding.indicator.attachTo(this)
            }
        }
    }

    private suspend fun goalNew(goal: String): GoalResponse? {
        val dialog = this@AchievementActivity.createLoadingDialog()
        dialog.show()

        return try {
            withContext(Dispatchers.IO) {
                val retrofitAPI = RetrofitClient.getInstance().create(RetrofitAPI::class.java)
                val response = retrofitAPI.goalNew(GoalRequest(goal))
                if (response.isSuccessful) {
                    dialog.dismiss()
                    response.body()
                } else {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@AchievementActivity, "실패하였습니다", Toast.LENGTH_SHORT).show()
                    }
                    null
                }
            }
        } catch (e: Exception) {
            Log.d("확인", e.toString())
            withContext(Dispatchers.Main) {
                Toast.makeText(this@AchievementActivity, "실패하였습니다", Toast.LENGTH_SHORT).show()
            }
            null
        }
    }

}