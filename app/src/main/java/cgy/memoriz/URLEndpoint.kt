package cgy.memoriz

object URLEndpoint {
    private val urlRoot = "http://192.168.0.166/linkdbAPI/v1/?op="
    val urlRegister = urlRoot + "register"
    val urlLogin = urlRoot + "login"
    val urlInsertQuestion = urlRoot + "insert_question"
    val urlGetQuestionnHelper = urlRoot + "get_question_helper"
}