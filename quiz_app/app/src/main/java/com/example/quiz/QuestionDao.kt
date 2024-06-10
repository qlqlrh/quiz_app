package com.example.quiz

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.tasks.await

class QuestionDao { // 데이터베이스와 연결할 클래스
    private var databaseReference: DatabaseReference? = null

    init { // QuestionDao 객체 생성 시 호출되는 함수
        val db = FirebaseDatabase.getInstance()
        databaseReference = db.getReference("question")
    }

    // write
    suspend fun add(question: Question?) {
        databaseReference!!.push().setValue(question).await()
    }

    // read
    fun getQuestions(): Query {
        return databaseReference!!.orderByKey()
    }

    fun getAllQuestions(callback: (List<Question>) -> Unit) {
        databaseReference!!.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val questions = mutableListOf<Question>()
                for (data in snapshot.children) {
                    val question = data.getValue(Question::class.java)
                    if (question != null) {
                        questions.add(question)
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
