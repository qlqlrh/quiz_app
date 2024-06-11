package com.example.quiz

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.quiz.databinding.ItemQuestionTestBinding

class QuestionTestAdapter(private val questionList: List<Question>) :
    RecyclerView.Adapter<QuestionTestAdapter.QuestionViewHolder>() {

    private val selectedAnswers = mutableMapOf<Int, Int>()

    inner class QuestionViewHolder(private val binding: ItemQuestionTestBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(question: Question) {
            binding.questionTextView.text = question.question
            binding.optionOne.text = question.option_one
            binding.optionTwo.text = question.option_two
            binding.optionThree.text = question.option_three

            val options = listOf(binding.optionOne, binding.optionTwo, binding.optionThree)
            options.forEachIndexed { index, option ->
                option.setOnClickListener {
                    selectedAnswers[adapterPosition] = index + 1
                    updateOptionColors(index + 1, options)
                }
            }
        }

        private fun updateOptionColors(selectedOption: Int, options: List<View>) {
            options.forEachIndexed { index, option ->
                if (index + 1 == selectedOption) {
                    option.setBackgroundColor(itemView.context.getColor(R.color.selectedOptionColor))
                } else {
                    option.setBackgroundColor(itemView.context.getColor(R.color.defaultOptionColor))
                }
            }
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

    fun getSelectedAnswers(): Map<Int, Int> {
        return selectedAnswers
    }
}
