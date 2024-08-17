package com.seogaemo.candu.adapter

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.seogaemo.candu.R
import com.seogaemo.candu.data.Goal
import com.seogaemo.candu.data.GoalRequest
import com.seogaemo.candu.data.GoalResponse
import com.seogaemo.candu.database.AppDatabase
import com.seogaemo.candu.databinding.ActivityMainBinding
import com.seogaemo.candu.network.RetrofitAPI
import com.seogaemo.candu.network.RetrofitClient
import com.seogaemo.candu.util.Dialog.createLoadingDialog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.random.Random


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    val colorList = listOf(Color.parseColor("#BB3435"), Color.parseColor("#2F9147"), Color.parseColor("#E95132"), Color.parseColor("#7C2DE0"), Color.parseColor("#3359DE"), Color.parseColor("#FFBF0F"))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupSystemBarsInsets()
        setBottomSheet()
        setAdapter()
        setInit()
    }

    private fun setInit() {
        CoroutineScope(Dispatchers.IO).launch {
            val goalDao = AppDatabase.getDatabase(this@MainActivity)?.goalDao()
            goalDao?.getGoalList()?.let {
                var num = 0
                it.forEach { count -> num+=count.level }
                if (num != 0) {
                    val value = (num.toDouble() / (it.size * 4).toDouble()) * 100
                    Log.d("확인", value.toString())

                    withContext(Dispatchers.Main) {
                        binding.achievementText.text = "$value% 달성"
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        binding.achievementText.text = "0% 달성"
                    }
                }
            }

        }
    }

    private fun setAdapter() {
        CoroutineScope(Dispatchers.IO).launch {
            val goalDao = AppDatabase.getDatabase(this@MainActivity)?.goalDao()
            val item = goalDao?.getGoalList()

            withContext(Dispatchers.Main) {
                binding.achievementList.apply {
                    adapter = item?.let { AchievementAdapter(it) }
                    layoutManager = LinearLayoutManager(this@MainActivity)
                }
            }
        }
    }

    private suspend fun goalNew(goal: String): GoalResponse? {
        val dialog = this@MainActivity.createLoadingDialog()
        dialog.show()
        return try {
            withContext(Dispatchers.IO) {
                val retrofitAPI = RetrofitClient.getInstance().create(RetrofitAPI::class.java)
                val response = retrofitAPI.goalNew(GoalRequest(goal))
                if (response.isSuccessful) {
                    withContext(Dispatchers.Main) {
                        dialog.dismiss()
                    }
                    response.body()
                } else {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@MainActivity, "실패하였습니다", Toast.LENGTH_SHORT).show()
                    }
                    null
                }
            }
        } catch (e: Exception) {
            Log.d("확인", e.toString())
            withContext(Dispatchers.Main) {
                Toast.makeText(this@MainActivity, "실패하였습니다", Toast.LENGTH_SHORT).show()
            }
            null
        }
    }

    private fun setBottomSheet() {
        val sheet = BottomSheetBehavior.from(binding.sheet).apply {
            peekHeight = dpToPx(20)
            this.state = BottomSheetBehavior.STATE_COLLAPSED
        }
        binding.main.setOnClickListener {
            keyBordHide()
            binding.main.requestFocus()
            sheet.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        binding.achievementInput.apply {
            this.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    binding.drawIcon.setColorFilter(resources.getColor(R.color.blackFull))
                    binding.sheetBackground.setBackgroundResource(R.drawable.focus_dotted_rounded_background)
                } else {
                    binding.drawIcon.clearColorFilter()
                    binding.sheetBackground.setBackgroundResource(R.drawable.dotted_rounded_background)
                }
            }
            this.setOnEditorActionListener { v, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    val inputText = v.text.toString()
                    CoroutineScope(Dispatchers.IO).launch {
                        val item = goalNew(inputText)
                        val goalDao = AppDatabase.getDatabase(this@MainActivity)?.goalDao()
                        item?.let { Goal(item= it, color = colorList[Random.nextInt(0, colorList.size - 1)], level = 0) }?.let {
                            goalDao?.insertGoal(it)
                            setAdapter()

                            withContext(Dispatchers.Main) {
                                keyBordHide()
                                binding.main.requestFocus()
                                sheet.state = BottomSheetBehavior.STATE_COLLAPSED
                            }
                        }
                    }
                    true
                } else {
                    false
                }
            }
        }
    }

    private fun keyBordHide() {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.achievementInput.windowToken, 0)
    }

    private fun setupSystemBarsInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    fun Context.dpToPx(dp: Int): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp.toFloat(),
            resources.displayMetrics
        ).toInt()
    }
}