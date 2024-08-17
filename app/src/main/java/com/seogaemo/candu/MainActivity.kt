package com.seogaemo.candu

import android.content.Context
import android.os.Bundle
import android.util.TypedValue
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.seogaemo.candu.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupSystemBarsInsets()
        setBottomSheet()
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
                    Toast.makeText(this@MainActivity, inputText, Toast.LENGTH_SHORT).show()
                    // 입력
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