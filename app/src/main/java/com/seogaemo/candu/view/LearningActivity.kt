package com.seogaemo.candu.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.seogaemo.candu.R
import com.seogaemo.candu.data.ContentRequest
import com.seogaemo.candu.data.ContentResponse
import com.seogaemo.candu.data.Goal
import com.seogaemo.candu.database.AppDatabase
import com.seogaemo.candu.databinding.ActivityLearningBinding
import com.seogaemo.candu.network.RetrofitAPI
import com.seogaemo.candu.network.RetrofitClient
import com.seogaemo.candu.util.Dialog.createLoadingDialog
import io.noties.markwon.Markwon
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LearningActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLearningBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLearningBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupWindowInsets()

        binding.apply {
            main.run {
                header = binding.topBar
            }

            intent.getParcelableExtra<Goal>("item")?.let {
                title.text = it.item.goal
                backButton.setOnClickListener {
                    finish()
                    overridePendingTransition(
                        R.anim.anim_slide_in_from_left_fade_in,
                        R.anim.anim_fade_out
                    )
                }
                subTitle.text = it.item.goal

                CoroutineScope(Dispatchers.IO).launch {
                    val item = getContents(ContentRequest(it.item.goal, it.item.chapters[it.level]))

                    withContext(Dispatchers.Main) {
                        finishButton.setOnClickListener { view ->
                            CoroutineScope(Dispatchers.IO).launch {
                                val goalDao = AppDatabase.getDatabase(this@LearningActivity)?.goalDao()
                                val level = ++it.level

                                it.level = level
                                goalDao?.updateGoal(it)
                                withContext(Dispatchers.Main) {
                                    val intent = Intent(this@LearningActivity, EndActivity::class.java)
                                    intent.putExtra("md", item?.content)
                                    intent.putExtra("goal", it.item.goal)
                                    startActivity(intent)
                                    overridePendingTransition(
                                        R.anim.anim_slide_in_from_right_fade_in,
                                        R.anim.anim_fade_out
                                    )
                                }
                            }
                        }
                        val markwon = Markwon.create(this@LearningActivity)
                        markwon.setMarkdown(markdown, item!!.content)

                    }
                }

            }


        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.anim_slide_in_from_left_fade_in, R.anim.anim_fade_out)
    }

    private suspend fun getContents(content: ContentRequest): ContentResponse? {
        val dialog = this@LearningActivity.createLoadingDialog()
        dialog.show()
        return try {
            withContext(Dispatchers.IO) {
                val retrofitAPI = RetrofitClient.getInstance().create(RetrofitAPI::class.java)
                val response = retrofitAPI.getContent(content)
                if (response.isSuccessful) {
                    withContext(Dispatchers.Main) {
                        dialog.dismiss()
                    }
                    dialog.dismiss()
                    response.body()

                } else {
                    withContext(Dispatchers.Main) {
                        dialog.dismiss()
                        Toast.makeText(this@LearningActivity, "실패하였습니다", Toast.LENGTH_SHORT).show()
                    }
                    null
                }
            }
        } catch (e: Exception) {
            Log.d("확인", e.toString())
            withContext(Dispatchers.Main) {
                Toast.makeText(this@LearningActivity, "실패하였습니다", Toast.LENGTH_SHORT).show()
            }
            null
        }
    }


    private fun setupWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { _, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            val statusBars = insets.getInsets(WindowInsetsCompat.Type.statusBars())

            val statusBarHeight = statusBars.top
            val navBarHeight = systemBars.bottom

            binding.topBar.setPadding(0, statusBarHeight, 0, navBarHeight)
            insets
        }
    }


}