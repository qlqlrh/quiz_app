package com.example.quiz

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.quiz.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    // binding을 사용하면  findById 없이 바로 id에 접근 가능
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // createBtn 누르면 CreateActivity로 넘어감
        binding.createBtn.setOnClickListener {
            val intent = Intent(this@MainActivity, CreateActivity::class.java)
            startActivity(intent)
            finish()
        }

        // testBtn 누르면 TestActivity로 넘어감
        binding.testBtn.setOnClickListener {
            val intent = Intent(this, TestActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    // intent를 사용해서 받아온 출제자의 이름을 Main에서 띄우기
    override fun onResume() {
        super.onResume()
        // Intent로부터 데이터 수신
        val creatorName = intent.getStringExtra("creator_name")
        println("MainActivity : $creatorName")
        if (creatorName != null) {
            binding.creatorNameTextView.text = "출제자: $creatorName"
        }
    }
}
