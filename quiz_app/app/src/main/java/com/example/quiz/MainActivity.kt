package com.example.quiz

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.quiz.databinding.ActivityMainBinding
import com.google.firebase.FirebaseApp

class MainActivity : AppCompatActivity() {

    // binding을 사용하면 findById 없이 바로 id에 접근 가능
    private lateinit var binding: ActivityMainBinding
    private val dao = QuestionDao()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)


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
            checkForAvailableQuizzes()
        }
    }

    private fun checkForAvailableQuizzes() {
        dao.getQuestions { questions ->
            if (questions.isNotEmpty()) {
                val intent = Intent(this, TestActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "등록된 퀴즈가 없습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
