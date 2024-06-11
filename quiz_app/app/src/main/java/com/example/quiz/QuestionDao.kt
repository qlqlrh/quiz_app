package com.example.quiz

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.tasks.await

class QuestionDao {
    private var databaseReference: DatabaseReference? = null

    init {
        val db = FirebaseDatabase.getInstance()
        databaseReference = db.getReference("questions")
    }

    // 특정 출제자 아래에 질문 추가 (비동기 처리)
    suspend fun addQuestion(creatorName: String, question: Question?) {
        databaseReference!!.child(creatorName).push().setValue(question).await()
    }

    // 특정 출제자의 모든 질문 가져오기
    fun getQuestionsByCreator(creatorName: String, callback: (List<Question>) -> Unit) {
        databaseReference!!.child(creatorName).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val questions = mutableListOf<Question>()
                for (data in snapshot.children) {
                    val question = data.getValue(Question::class.java)
                    if (question != null) {
                        questions.add(question)
                    }
                }
                // id를 기준으로 정렬
                questions.sortBy { it.id }
                callback(questions)
            }

            override fun onCancelled(error: DatabaseError) {
                callback(emptyList())
            }
        })
    }

    // 특정 출제자의 질문 개수 가져오기
    fun getQuestionCountByCreator(creatorName: String, callback: (Int) -> Unit) {
        databaseReference!!.child(creatorName).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                callback(snapshot.childrenCount.toInt())
            }

            override fun onCancelled(error: DatabaseError) {
                callback(0)
            }
        })
    }

    // 모든 출제자 이름 가져오기
    fun getAllCreators(callback: (List<String>) -> Unit) {
        databaseReference!!.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val creators = mutableListOf<String>()
                for (data in snapshot.children) {
                    creators.add(data.key ?: "")
                }
                callback(creators)
            }

            override fun onCancelled(error: DatabaseError) {
                callback(emptyList())
            }
        })
    }

    // 모든 질문 가져오기
    fun getQuestions(callback: (List<Question>) -> Unit) {
        databaseReference!!.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val questions = mutableListOf<Question>()
                for (data in snapshot.children) {
                    for (questionSnapshot in data.children) {
                        val question = questionSnapshot.getValue(Question::class.java)
                        if (question != null) {
                            questions.add(question)
                        }
                    }
                }
                callback(questions)
            }

            override fun onCancelled(error: DatabaseError) {
                callback(emptyList())
            }
        })
    }
}
