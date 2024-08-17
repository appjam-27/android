package com.seogaemo.candu

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.seogaemo.candu.data.CompleteRequest
import com.seogaemo.candu.data.CompleteResponse
import com.seogaemo.candu.data.GoalRequest
import com.seogaemo.candu.data.GoalResponse
import com.seogaemo.candu.databinding.ActivityEndBinding
import com.seogaemo.candu.network.RetrofitAPI
import com.seogaemo.candu.network.RetrofitClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EndActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEndBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEndBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val md = intent.getStringExtra("md").toString()
        val goal = intent.getStringExtra("goal").toString()

        CoroutineScope(Dispatchers.IO).launch {
            val getImage = getImage(CompleteRequest(md, goal))
            withContext(Dispatchers.Main) {
                binding.apply {
                    subTitle.text = goal
                    Glide.with(this@EndActivity)
                        .load(getImage?.content?.url)
                        .into(binding.image)
                    subText.text = getImage?.content?.content
                    finishButton.setOnClickListener {
                        startActivity(Intent(this@EndActivity, MainActivity::class.java))
                        finishAffinity()
                    }
                }
            }
        }
    }

    private suspend fun getImage(data: CompleteRequest): CompleteResponse? {
        return try {
            withContext(Dispatchers.IO) {
                val retrofitAPI = RetrofitClient.getInstance().create(RetrofitAPI::class.java)
                val response = retrofitAPI.getImage(data)
                if (response.isSuccessful) {
                    response.body()
                } else {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@EndActivity, "실패하였습니다", Toast.LENGTH_SHORT).show()
                    }
                    null
                }
            }
        } catch (e: Exception) {
            Log.d("확인", e.toString())
            withContext(Dispatchers.Main) {
                Toast.makeText(this@EndActivity, "실패하였습니다", Toast.LENGTH_SHORT).show()
            }
            null
        }
    }
}