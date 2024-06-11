package com.example.quiz

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.quiz.databinding.ActivityScoreBinding

class ScoreActivity : AppCompatActivity() {

    private lateinit var binding: ActivityScoreBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScoreBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val score = intent.getIntExtra("score", 0)
        val creatorName = intent.getStringExtra("creator_name")
        val participantName = intent.getStringExtra("participant_name") ?: ""

        val viewPager: ViewPager2 = findViewById(R.id.viewPager)
        viewPager.adapter = ScorePagerAdapter(this, score, creatorName, participantName)

        binding.buttonGoHome.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }
    }
}
