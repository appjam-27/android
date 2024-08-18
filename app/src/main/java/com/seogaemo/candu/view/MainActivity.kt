package com.seogaemo.candu.view

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.ViewTreeObserver
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.firebase.messaging.FirebaseMessaging
import com.seogaemo.candu.R
import com.seogaemo.candu.adapter.AchievementAdapter
import com.seogaemo.candu.adapter.CommitAdapter
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
    private val colorList = listOf(Color.parseColor("#BB3435"), Color.parseColor("#2F9147"), Color.parseColor("#E95132"), Color.parseColor("#7C2DE0"), Color.parseColor("#3359DE"), Color.parseColor("#FFBF0F"))

    companion object {
        const val PERMISSION_REQUEST_CODE = 200
    }

    private fun permissionCheck() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val permissionCheck = ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.POST_NOTIFICATIONS
            )
            if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                    PERMISSION_REQUEST_CODE
                )
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupSystemBarsInsets()
        setBottomSheet()
        setAdapter()
        setInit()
        setCommit()
        permissionCheck()
        FirebaseMessaging.getInstance().subscribeToTopic("all")
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(applicationContext, "Permission is denied", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    Toast.makeText(applicationContext, "Permission is granted", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    private fun setCommit() {
        val defaultAdapter = CommitAdapter(listOf(1, 1, 1, 1, 1))
        binding.apply {
            commitList2.adapter = defaultAdapter
            commitList2.layoutManager = LinearLayoutManager(this@MainActivity)

            commitList2.adapter = defaultAdapter
            commitList2.layoutManager = LinearLayoutManager(this@MainActivity)

            commitList3.adapter = defaultAdapter
            commitList3.layoutManager = LinearLayoutManager(this@MainActivity)

            commitList4.adapter = defaultAdapter
            commitList4.layoutManager = LinearLayoutManager(this@MainActivity)

            commitList5.adapter = defaultAdapter
            commitList5.layoutManager = LinearLayoutManager(this@MainActivity)

            commitList6.adapter = defaultAdapter
            commitList6.layoutManager = LinearLayoutManager(this@MainActivity)

            commitList7.adapter = defaultAdapter
            commitList7.layoutManager = LinearLayoutManager(this@MainActivity)

            commitList8.adapter = defaultAdapter
            commitList8.layoutManager = LinearLayoutManager(this@MainActivity)

            commitList9.adapter = defaultAdapter
            commitList9.layoutManager = LinearLayoutManager(this@MainActivity)

            commitList11.adapter = defaultAdapter
            commitList11.layoutManager = LinearLayoutManager(this@MainActivity)

            commitList12.adapter = defaultAdapter
            commitList12.layoutManager = LinearLayoutManager(this@MainActivity)

            commitList13.adapter = defaultAdapter
            commitList13.layoutManager = LinearLayoutManager(this@MainActivity)

            commitList14.adapter = defaultAdapter
            commitList14.layoutManager = LinearLayoutManager(this@MainActivity)

            commitList15.adapter = defaultAdapter
            commitList15.layoutManager = LinearLayoutManager(this@MainActivity)

            commitList16.adapter = defaultAdapter
            commitList16.layoutManager = LinearLayoutManager(this@MainActivity)

            commitList17.adapter = defaultAdapter
            commitList17.layoutManager = LinearLayoutManager(this@MainActivity)

            commitList18.adapter = defaultAdapter
            commitList18.layoutManager = LinearLayoutManager(this@MainActivity)

        }
    }

    private fun setInit() {
        binding.root.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            private var previousKeyboardHeight = 0

            override fun onGlobalLayout() {
                val rect = Rect()
                binding.root.getWindowVisibleDisplayFrame(rect)
                val screenHeight = binding.root.rootView.height
                val keyboardHeight = screenHeight - rect.bottom

                if (keyboardHeight > screenHeight * 0.15) {
                    if (keyboardHeight != previousKeyboardHeight) {
                        binding.sheet.updatePadding(bottom = keyboardHeight)
                    }
                } else {
                    if (previousKeyboardHeight > 0) {
                        binding.sheet.updatePadding(bottom = 0)
                    }
                }

                previousKeyboardHeight = keyboardHeight
            }
        })

        CoroutineScope(Dispatchers.IO).launch {
            val goalDao = AppDatabase.getDatabase(this@MainActivity)?.goalDao()
            goalDao?.getGoalList()?.let {
                var num = 0
                it.forEach { count -> num+=count.level }
                if (num != 0) {
                    val value = ((num.toDouble() / (it.size * 4).toDouble()) * 100).toInt()

                    if (value.toString().length > 1) {
                        val item = value.toString()[0].code
                        binding.commitList1.adapter = CommitAdapter(listOf(item, 1, 1, 1, 1))
                        binding.commitList1.layoutManager = LinearLayoutManager(this@MainActivity)
                    } else {
                        binding.commitList1.adapter = CommitAdapter(listOf(1, 1, 1, 1, 1))
                        binding.commitList1.layoutManager = LinearLayoutManager(this@MainActivity)
                    }
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
                    CoroutineScope(Dispatchers.Main).launch {
                        val item = goalNew(inputText)
                        withContext(Dispatchers.IO) {
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
            val statusBars = insets.getInsets(WindowInsetsCompat.Type.statusBars())

            val statusBarHeight = statusBars.top
            val navBarHeight = systemBars.bottom

            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            binding.textView.setPadding(0, statusBarHeight, 0, navBarHeight)
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