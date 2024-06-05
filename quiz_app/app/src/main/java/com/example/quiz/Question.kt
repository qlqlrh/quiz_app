package com.example.quiz

data class Question( // 문항 정보를 담을 클래스
    var id : Int,
    var question : String,
    var option_one : String,
    var option_two : String,
    var option_three : String,
    var answer : Int
) {
    constructor() : this(0, "", "", "", "", 0)
}
