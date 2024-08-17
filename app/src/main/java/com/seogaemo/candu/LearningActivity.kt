package com.seogaemo.candu

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.seogaemo.candu.databinding.ActivityLearningBinding
import com.seogaemo.candu.databinding.ActivityMainBinding

class LearningActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLearningBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLearningBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}