package com.seogaemo.candu.adapter

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.seogaemo.candu.R
import com.seogaemo.candu.data.CompleteRequest
import com.seogaemo.candu.data.CompleteResponse
import com.seogaemo.candu.databinding.ActivityEndBinding
import com.seogaemo.candu.network.RetrofitAPI
import com.seogaemo.candu.network.RetrofitClient
import com.seogaemo.candu.util.Dialog.createLoadingDialog
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
                        overridePendingTransition(
                            R.anim.anim_slide_in_from_right_fade_in,
                            R.anim.anim_fade_out
                        )
                    }
                }
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.anim_slide_in_from_left_fade_in, R.anim.anim_fade_out)
    }

    private suspend fun getImage(data: CompleteRequest): CompleteResponse? {
        val dialog = this@EndActivity.createLoadingDialog()
        dialog.show()
        return try {
            withContext(Dispatchers.IO) {
                val retrofitAPI = RetrofitClient.getInstance().create(RetrofitAPI::class.java)
                val response = retrofitAPI.getImage(data)
                if (response.isSuccessful) {
                    dialog.dismiss()
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