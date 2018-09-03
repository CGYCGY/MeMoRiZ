package cgy.memoriz

object URLEndpoint {
    private val urlRoot = "http://192.168.0.166/linkdbAPI/v1/?op="
    val urlRegister = urlRoot + "register"
    val urlLogin = urlRoot + "login"
    val urlInsertQuestion = urlRoot + "insert_question"
    val urlGetQuestionnHelper = urlRoot + "get_question_helper"
    val urlUpdateQuestion = urlRoot + "update_question"
    val urlGetQuestionSolver = urlRoot + "get_question_solver"
    val urlGetAnswer = urlRoot + "get_answer"
    val urlInsertAnswer = urlRoot + "insert_answer"
    val urlInsertReport = urlRoot + "insert_report"
    val urlGetReport = urlRoot + "get_report"
    val urlInsertSet = urlRoot + "insert_set"
    val urlGetSet = urlRoot + "get_set"
    val urlInsertQuiz = urlRoot + "insert_quiz"
    val urlGetQuiz = urlRoot + "get_quiz"
    val urlGetUserList = urlRoot + "get_user_list"
    val urlGetClassList = urlRoot + "get_class_list"
    val urlInsertClass = urlRoot + "insert_class"
}