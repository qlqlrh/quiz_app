package com.example.quiz

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.quiz.adapter.QuestionAdapter
import com.example.quiz.databinding.ActivityCreateBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


class CreateActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreateBinding
    private lateinit var questionAdapter: QuestionAdapter
    private val questionList = mutableListOf<Question>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 데이터베이스 클래스 객체 생성
        val dao = QuestionDao()
        var id = 1

        // 질문 리스트 초기화
        questionList.add(Question(id++, "질문1. ", "", "", "", 0))
        questionList.add(Question(id++, "질문2. ", "", "", "", 0))
        questionList.add(Question(id++, "질문3. ", "", "", "", 0))
        questionList.add(Question(id++, "질문4. ", "", "", "", 0))
        questionList.add(Question(id++, "질문5. ", "", "", "", 0))

        // RecyclerView 설정
        questionAdapter = QuestionAdapter(questionList)
        binding.recyclerViewQuestions.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewQuestions.adapter = questionAdapter

        // 각 질문을 데이터베이스에 추가
        binding.submitBtn.setOnClickListener {
            // CoroutineScope(Dispatchers.Main).launch를 사용해서 코루틴을 시작
            // Dispatchers.Main을 사용해서 UI 작업을 처리하기 위해 메인 스레드에서 코루틴 실행
            CoroutineScope(Dispatchers.Main).launch {
                questionList.forEach { question ->
                    try {
                        // dao.add(question)?.await()를 사용해서 Firebase 작업을 중단 가능한 suspend 함수로 변환 (코루틴 사용)
                        // task가 완료될 때까지 중단하고, 결과를 반환
                        dao.add(question)?.await()
                        Toast.makeText(this@CreateActivity, "질문 ${question.id} 등록 성공", Toast.LENGTH_SHORT).show()
                    } catch (e: Exception) {
                        Toast.makeText(this@CreateActivity, "질문 ${question.id} 등록 실패: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        // mainBtn 누르면 MainActivity로 이동
        binding.mainBtn.setOnClickListener {
            val intent = Intent(this@CreateActivity, MainActivity::class.java)

            // CreateActivity에서 출제자 이름을 가져와서 MainActivity로 전달
            val creatorName = binding.inputName.text.toString()
            println("CreateActivity : " + creatorName)
            intent.putExtra("creator_name", creatorName)

            startActivity(intent)
            finish()
        }

    }
}