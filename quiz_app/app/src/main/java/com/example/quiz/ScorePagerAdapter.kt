package com.example.quiz

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.quiz.databinding.PageInitialBinding
import com.example.quiz.databinding.PageScoreBinding

class ScorePagerAdapter(
    private val context: Context,
    private val score: Int,
    private val creatorName: String?,
    private val participantName: String
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val PAGE_INITIAL = 0
        private const val PAGE_SCORE = 1
        private const val PAGE_COUNT = 2
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            PAGE_INITIAL -> {
                val binding = PageInitialBinding.inflate(LayoutInflater.from(context), parent, false)
                InitialViewHolder(binding)
            }
            PAGE_SCORE -> {
                val binding = PageScoreBinding.inflate(LayoutInflater.from(context), parent, false)
                ScoreViewHolder(binding)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            PAGE_INITIAL -> (holder as InitialViewHolder).bind(participantName)
            PAGE_SCORE -> (holder as ScoreViewHolder).bind(score)
        }
    }

    override fun getItemCount(): Int = PAGE_COUNT

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> PAGE_INITIAL
            1 -> PAGE_SCORE
            else -> throw IllegalArgumentException("Invalid position")
        }
    }

    inner class InitialViewHolder(private val binding: PageInitialBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(participantName: String) {
            binding.initialTextView.text = "두근두근!\n ${participantName}님의 결과 확인하기\n (옆으로 넘기세요)"
        }
    }

    inner class ScoreViewHolder(private val binding: PageScoreBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(score: Int) {
            val scoreMessage = when (score) {
                0 -> "당신의 점수는 0점 !\n 너.. 내 친구 맞아 ㅠ?"
                1 -> "당신의 점수는 20점 !\n 약간은 어색한 사이 ^^!"
                2 -> "당신의 점수는 40점 !\n  좀 더 친해지자 우리 ^~^"
                3 -> "당신의 점수는 60점 !\n 발전할 수 있는 관계네요"
                4 -> "당신의 점수는 80점 !\n 조금만 더 노력해보세요"
                5 -> "당신의 점수는 100점 !\n 찾았다 나의 베스트프렌드 !!"
                else -> "Invalid score"
            }
            binding.scoreTextView.text = scoreMessage
        }
    }
}
