package com.example.quiz

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.quiz.Question
import com.example.quiz.databinding.ItemQuestionTestBinding

class QuestionTestAdapter(private val questionList: List<Question>) :
    RecyclerView.Adapter<QuestionTestAdapter.QuestionViewHolder>() {

    inner class QuestionViewHolder(private val binding: ItemQuestionTestBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(question: Question) {
            binding.questionTextView.text = question.question
            binding.optionOne.text = question.option_one
            binding.optionTwo.text = question.option_two
            binding.optionThree.text = question.option_three
//
//            // 각 선택지에 대해 클릭 리스너 설정
//            binding.optionOne.setOnClickListener {
//
//            }
//            binding.optionTwo.setOnClickListener(this)
//            binding.optionThree.setOnClickListener(this)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionViewHolder {
        val binding = ItemQuestionTestBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return QuestionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: QuestionViewHolder, position: Int) {
        holder.bind(questionList[position])
    }

    override fun getItemCount(): Int {
        return questionList.size
    }
}