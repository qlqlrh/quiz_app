package com.example.quiz.adapter

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.quiz.databinding.ItemQuestionBinding
import com.example.quiz.Question

class QuestionAdapter(private val questionList: List<Question>) : RecyclerView.Adapter<QuestionAdapter.QuestionViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionViewHolder {
        val binding = ItemQuestionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return QuestionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: QuestionViewHolder, position: Int) {
        val question = questionList[position]
        holder.bind(question)
    }

    override fun getItemCount(): Int {
        return questionList.size
    }

    inner class QuestionViewHolder(private val binding: ItemQuestionBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(question: Question) {
            binding.questionText.setText(question.question)
            binding.option1Text.setText(question.option_one)
            binding.option2Text.setText(question.option_two)
            binding.option3Text.setText(question.option_three)
            binding.answerText.setText(question.answer.toString())

            // 질문 텍스트 변경 리스너
            binding.questionText.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    question.question = s.toString()
                }
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            })

            // 선택지 1 텍스트 변경 리스너
            binding.option1Text.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    question.option_one = s.toString()
                }
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            })

            // 선택지 2 텍스트 변경 리스너
            binding.option2Text.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    question.option_two = s.toString()
                }
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            })

            // 선택지 3 텍스트 변경 리스너
            binding.option3Text.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    question.option_three = s.toString()
                }
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            })

            // 답변 텍스트 변경 리스너
            binding.answerText.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    val answerText = s.toString()
                    question.answer = if (answerText.isEmpty()) 0 else answerText.toInt()
                }
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            })
        }
    }
}