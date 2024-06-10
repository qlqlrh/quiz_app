package com.example.quiz

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.quiz.databinding.ActivityTestBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class TestActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTestBinding
    private lateinit var questionAdapter: QuestionTestAdapter
    private val questionList = mutableListOf<Question>()
    private val dao = QuestionDao()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTestBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 출제자의 이름을 받아와서 표시
        val creatorName = intent.getStringExtra("creator_name")
        if (creatorName != null) {
            binding.creatorNameTextView.text = "출제자: $creatorName"
        }

        // RecyclerView 설정
        questionAdapter = QuestionTestAdapter(questionList)
        binding.recyclerViewQuestions.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewQuestions.adapter = questionAdapter

        // 사용자 정보 가져오기
        getQuestions()

        // 완료 버튼 클릭 시 MainActivity로 이동
        binding.finishBtn.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun getQuestions() {
        dao.getQuestions()?.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                questionList.clear()  // 기존 데이터 초기화
                for (dataSnapshot in snapshot.children) {
                    val question = dataSnapshot.getValue(Question::class.java)

                    if (question != null) {
                        questionList.add(question)
                        Log.d("TestActivity", "Question Added: ${question.question}")
                    } else {
                        Log.e("TestActivity", "Failed to convert dataSnapshot to Question")
                    }
                }

                // 데이터 적용
                questionAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@TestActivity, "데이터를 불러오는데 실패했습니다: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
