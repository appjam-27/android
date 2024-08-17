package com.seogaemo.candu

import android.content.Intent
import android.graphics.BlurMaskFilter
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.mukesh.MarkDown
import com.seogaemo.candu.data.ContentRequest
import com.seogaemo.candu.data.ContentResponse
import com.seogaemo.candu.data.Goal
import com.seogaemo.candu.data.GoalRequest
import com.seogaemo.candu.data.GoalResponse
import com.seogaemo.candu.database.AppDatabase
import com.seogaemo.candu.databinding.ActivityLearningBinding
import com.seogaemo.candu.databinding.ActivityMainBinding
import com.seogaemo.candu.network.RetrofitAPI
import com.seogaemo.candu.network.RetrofitClient
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
                backButton.setOnClickListener { finish() }
                subTitle.text = it.item.goal

                CoroutineScope(Dispatchers.IO).launch {
                    finishButton.setOnClickListener { view ->
                        val goalDao = AppDatabase.getDatabase(this@LearningActivity)?.goalDao()
                        val level = ++it.level

                        it.level = level
                        goalDao?.updateGoal(it)

                        startActivity(Intent(this@LearningActivity, MainActivity::class.java))
                        finishAffinity()
                    }
                    val item = getContents(ContentRequest(it.item.goal, it.item.chapters[it.level]))
                    withContext(Dispatchers.Main) {
                        markdown.apply {
                            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
                            setContent {
                                MaterialTheme {
                                    MarkDown(
                                        text = item.toString(),
                                        modifier = Modifier.fillMaxSize()
                                    )
                                }
                            }
                        }
                    }
                }

            }


        }
    }

    private suspend fun getContents(content: ContentRequest): ContentResponse? {
        return try {
            withContext(Dispatchers.IO) {
                val retrofitAPI = RetrofitClient.getInstance().create(RetrofitAPI::class.java)
                val response = retrofitAPI.getContent(content)
                if (response.isSuccessful) {
                    response.body()
                } else {
                    withContext(Dispatchers.Main) {
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