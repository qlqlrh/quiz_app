package com.example.quiz

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.quiz.databinding.ActivityTestBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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

        // 완료 버튼 클릭 시 점수 계산 후 ScoreActivity로 이동
        binding.finishBtn.setOnClickListener {
            val participantName = binding.participantNameEditText.text.toString()
            if (participantName.isNotEmpty()) {
                calculateAndShowScore(creatorName, participantName)
            } else {
                Toast.makeText(this, "이름을 입력하세요", Toast.LENGTH_SHORT).show()
            }
        }

        // Load creators and set up buttons
        loadCreators()
    }

    private fun loadCreators() {
        CoroutineScope(Dispatchers.Main).launch {
            dao.getAllCreators { creators ->
                creators.forEach { creatorName ->
                    val button = Button(this@TestActivity, null, 0, R.style.MainButtonStyle).apply {
                        text = creatorName
                        layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        ).apply {
                            setMargins(8, 8, 8, 8)
                        }
                    }
                    button.setOnClickListener {
                        // 출제자의 퀴즈를 가져와 RecyclerView에 표시
                        loadQuestionsForCreator(creatorName)
                    }
                    binding.creatorButtonsLayout.addView(button)
                }
            }
        }
    }

    private fun loadQuestionsForCreator(creatorName: String) {
        CoroutineScope(Dispatchers.Main).launch {
            dao.getQuestionsByCreator(creatorName) { questions ->
                if (questions.isEmpty()) {
                    Toast.makeText(this@TestActivity, "질문이 없습니다.", Toast.LENGTH_SHORT).show()
                } else {
//                    questionAdapter = QuestionTestAdapter(questions)
//                    binding.recyclerViewQuestions.layoutManager = LinearLayoutManager(this@TestActivity)
//                    binding.recyclerViewQuestions.adapter = questionAdapter
                    questionList.clear()
                    questionList.addAll(questions) // Already sorted by id in DAO
                    questionAdapter.notifyDataSetChanged()
                }
            }
        }
    }

    private fun calculateAndShowScore(creatorName: String?, participantName: String) {
        var score = 0
        val selectedAnswers = questionAdapter.getSelectedAnswers()
        for ((index, question) in questionList.withIndex()) {
            if (selectedAnswers[index] == question.answer) {
                score++
            }
        }
        // Start ScoreActivity and pass the score, creator name, and participant name
        val intent = Intent(this, ScoreActivity::class.java)
        intent.putExtra("score", score)
        intent.putExtra("creator_name", creatorName)
        intent.putExtra("participant_name", participantName)
        startActivity(intent)
    }
}
