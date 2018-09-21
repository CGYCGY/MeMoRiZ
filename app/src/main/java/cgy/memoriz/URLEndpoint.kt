package cgy.memoriz

object URLEndpoint {
    private val urlRoot = "http://192.168.0.166/linkdbAPI/v1/?op="
//    private val urlRoot = "http://192.168.43.138/linkdbAPI/v1/?op="
    private val urlBase = "http://192.168.0.166/linkdbAPI/"
//    private val urlBase = "http://192.168.43.138/linkdbAPI/"

    val urlRegister = urlRoot + "register"
    val urlLogin = urlRoot + "login"
    val urlInsertQuestion = urlRoot + "insert_question"
    val urlGetQuestionHelper = urlRoot + "get_question_helper"
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
    val urlUpdateQuiz = urlRoot + "update_quiz"
    val urlDeleteQuiz = urlRoot + "delete_quiz"
    val urlGetUserList = urlRoot + "get_user_list"
    val urlGetClassList = urlRoot + "get_class_list"
    val urlInsertClass = urlRoot + "insert_class"
    val urlJoinedClassList = urlRoot + "joined_class_list"
    val urlNotJoinedClassList = urlRoot + "not_joined_class_list"
    val urlInsertClassmember = urlRoot + "insert_classmember"
    val urlInsertQuizResult = urlRoot + "insert_quiz_result"
    val urlInsertQuizResultRecord = urlRoot + "insert_quiz_result_record"
    val urlUploadImage =  urlRoot + "upload_profile_photo"
    val urlGetUserProfile =  urlRoot + "get_user_profile"
    val urlUpdateUserProfile =  urlRoot + "update_user_profile"
    val urlInsertFCSet = urlRoot + "insert_flashcardset"
    val urlGetFCSet = urlRoot + "get_flashcardset"
    val urlInsertFlashcard = urlRoot + "insert_flashcard"
    val urlUpdateFlashcard = urlRoot + "update_flashcard"
    val urlDeleteFlashcard = urlRoot + "delete_flashcard"
    val urlGetFlashcard = urlRoot + "get_flashcard"

    val urlLoadImage = urlBase + "image/"
}