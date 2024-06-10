package com.example.quiz.adapter

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.quiz.databinding.ItemQuestionBinding
import com.example.quiz.Question


class QuestionAdapter(private val questionList: MutableList<Question>) : RecyclerView.Adapter<QuestionAdapter.QuestionViewHolder>() {

    // 화면 연결해주는 함수
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionViewHolder {
        val binding = ItemQuestionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return QuestionViewHolder(binding)
    }

    // 데이터 연결해주는 함수
    override fun onBindViewHolder(holder: QuestionViewHolder, position: Int) {
        val currentQuestion = questionList[position]
        holder.bind(currentQuestion)
    }

    // 사용자 목록 사이즈 리턴해주는 함수
    override fun getItemCount() = questionList.size

    // 화면 뷰 객체 만들어주는 클래스
    class QuestionViewHolder(private val binding: ItemQuestionBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(question: Question) {
            binding.questionText.setText(question.question)
            binding.option1Text.setText(question.option_one)
            binding.option2Text.setText(question.option_two)
            binding.option3Text.setText(question.option_three)
            binding.answerText.setText(question.answer.toString())
        }
    }
}
