package com.example.quiz

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.quiz.adapter.QuestionAdapter
import com.example.quiz.databinding.ActivityCreateBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CreateActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreateBinding
    private lateinit var questionAdapter: QuestionAdapter
    private val questionList = mutableListOf<Question>()
    private val MAX_QUESTION = 5

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 데이터베이스 클래스 객체 생성
        val dao = QuestionDao()
        var id = 1

        // 질문 리스트 초기화
        for (i in 1..MAX_QUESTION) {
            questionList.add(Question(id++, "질문 ${i}. ", "", "", "", 0))
        }

        // RecyclerView 설정
        questionAdapter = QuestionAdapter(questionList)
        binding.recyclerViewQuestions.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewQuestions.adapter = questionAdapter

        // 생성 버튼 설정
        binding.submitBtn.setOnClickListener {
            val creatorName = binding.creatorName.text.toString()

            if (creatorName.isBlank()) {
                Toast.makeText(this, "출제자의 이름을 작성해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // 코루틴을 사용해서 비동기적으로 db write
            CoroutineScope(Dispatchers.Main).launch {
                dao.getQuestionCountByCreator(creatorName) { count ->
                    if (count >= MAX_QUESTION) {
                        Toast.makeText(this@CreateActivity, "이미 등록된 질문이 있습니다.", Toast.LENGTH_SHORT).show()
                    } else {
                        // 모든 질문과 답안이 입력되었는지 확인
                        val allQuestionsFilled = questionList.all { question ->
                            question.question.isNotBlank() && question.option_one.isNotBlank() &&
                                    question.option_two.isNotBlank() && question.option_three.isNotBlank()
                        }

                        if (!allQuestionsFilled) {
                            Toast.makeText(this@CreateActivity, "모든 질문과 답안을 입력하세요.", Toast.LENGTH_SHORT).show()
                        } else {
                            questionList.forEach { question ->
                                CoroutineScope(Dispatchers.IO).launch {
                                    try {
                                        dao.addQuestion(creatorName, question)
                                        launch(Dispatchers.Main) {
                                            Toast.makeText(
                                                this@CreateActivity,
                                                "질문 ${question.id} 등록 성공",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            // 성공적으로 등록되면 MainActivity로 이동
                                            val intent = Intent(this@CreateActivity, MainActivity::class.java)
                                            startActivity(intent)
                                            finish()
                                        }
                                    } catch (e: Exception) {
                                        launch(Dispatchers.Main) {
                                            Toast.makeText(
                                                this@CreateActivity,
                                                "질문 ${question.id} 등록 실패: ${e.message}",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // mainBtn 누르면 MainActivity로 이동
        binding.mainBtn.setOnClickListener {
            val intent = Intent(this@CreateActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        // resetBtn 누르면 모든 질문 및 답안, 출제자 이름 초기화
        binding.resetBtn.setOnClickListener {
            binding.creatorName.setText("")

            var i = 1;
            questionList.forEach { question ->
                question.question = "질문 ${i++}. "
                question.option_one = ""
                question.option_two = ""
                question.option_three = ""
                question.answer = 0
            }

            questionAdapter.notifyDataSetChanged()
            Toast.makeText(this, "초기화되었습니다.", Toast.LENGTH_SHORT).show()
        }

        // 출제자 이름이 입력되었는지 확인하여 생성 버튼 활성화/비활성화
        binding.creatorName.addTextChangedListener {
            binding.submitBtn.isEnabled = it.toString().isNotBlank()
        }
    }
}