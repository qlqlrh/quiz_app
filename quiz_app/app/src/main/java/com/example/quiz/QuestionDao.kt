package com.example.quiz

import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class QuestionDao { // 데이터베이스와 연결할 클래스
    private var databaseReference : DatabaseReference? = null

    init { // QuestionDao 객체 생성 시 호출되는 함수
        val db = FirebaseDatabase.getInstance()
        databaseReference = db.getReference("question")
    }

    // 등록
    fun add(question: Question?): Task<Void> {
        return databaseReference!!.push().setValue(question)
    }
}