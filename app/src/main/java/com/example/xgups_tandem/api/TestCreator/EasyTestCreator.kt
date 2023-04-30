package com.example.xgups_tandem.api.TestCreator

import com.example.xgups_tandem.api.convertClassToJson
import com.example.xgups_tandem.api.convertJsonToClass
import com.squareup.moshi.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

class EasyTestCreator {
    open class Script() {
        var startQuestion : Question? = null
        var nextQuestion : Question? = null

        var getAnswer : ((String) -> String) = {
            ""
        }

        lateinit var nowQuestion : Question
        fun start() {
            if(startQuestion == null) return

            nowQuestion = startQuestion!!
            nowQuestion.invokeOnCreate()

            val answerString = getAnswer.invoke(nowQuestion.getQuestionText())

            val rightAnswer = nowQuestion.responseTextAndGetAnswer(answerString)
            nowQuestion.invokeOnAnswer(rightAnswer)
        }
    }
    abstract class Question(val answers : List<Answer>){
        var textQuestion: String = "Вопрос"

        var setOnCreateListner: ((Question) -> Unit) = {}
        fun invokeOnCreate() { setOnCreateListner.invoke(this) }

        var setOnAnswerListener: ((Question, Answer?) -> Unit) = { _, _ -> }
        var setOnNotCorrectAnswerListner: ((Question) -> Unit) = {}
        fun invokeOnAnswer(answer: Answer?){
            setOnAnswerListener.invoke(this,answer)
            if(answer != null) answer.invokeOnAnswer()
            else setOnNotCorrectAnswerListner.invoke(this)
        }

        abstract fun getQuestionText() : String
        abstract fun responseTextAndGetAnswer(text : String) : Answer?
    }
    open class Answer() {
        var text : String = "Ответ"

        var nextQuestion : Question? = null

        var setOnAnswerListener: ((Answer) -> Unit) = { }
        fun invokeOnAnswer() { setOnAnswerListener?.invoke(this) }

        constructor(Text: String) : this(){
            text = Text
        }
        constructor(Text: String, Answer : (Answer) -> Unit) : this(){
            text = Text
            setOnAnswerListener = Answer
        }
    }
    class OpenAI{

        companion object{
            val Bearer : String = "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"
            fun generateText(mes : List<Message>) : String {
                val values = mapOf(
                    "model" to "gpt-3.5-turbo",
                    "messages" to mes,
                )
                val requestBody: String = values.convertClassToJson()

                val JSON = "application/json; charset=utf-8".toMediaType()

                val client = OkHttpClient()
                val body: RequestBody = requestBody.toRequestBody(JSON)
                val request = Request.Builder()
                    .url("https://api.openai.com/v1/chat/completions")
                    .addHeader("Authorization","Bearer $Bearer")
                    .post(body)
                    .build()

                try {
                    client.newCall(request).execute().use { response ->
                        if (!response.isSuccessful) {
                            throw IOException(
                                "Запрос к серверу не был успешен:" +
                                        " ${response.code} ${response.message}"
                            )
                        }
                        return response.body!!.string()
                    }
                } catch (e: IOException) {
                    println("Ошибка подключения: $e")
                    return "er"
                }
            }
        }

        class OpenAIScript() : Script(){
            val messageContext = mutableListOf<Message>()

            init {
                getAnswer = {
                    messageContext.add(Message("user", it))

                    val chat = generateText(listOf(Message("user",it))).convertJsonToClass<ChatCompletion>()
                    val chatGPT = chat.choices[0].message.content

                    messageContext.add(Message("assistant", chatGPT))
                    chatGPT
                }
            }
        }

        class TestQuestion(answers: List<Answer>) : Question(answers) {

            override fun getQuestionText() : String {
                val answers = getAnswersWithNumeric()
                return "Есть запрос:'$textQuestion'. Что сказано в запросе: $answers";
            }

            override fun responseTextAndGetAnswer(text: String) : Answer? {
                text
                val answerNumber = convertLetterToDigit(
                    text.split(')')[0][0]
                ) - 1

                return if (answerNumber == 0 ||
                    answerNumber < 0 ||
                    answerNumber > answers.size)  null
                else answers[answerNumber - 1]
            }

            private fun getAnswersWithNumeric(): String {
                var answerText = "${convertDigitToLetter(1)}) нет правильного ответа."
                for (i in this.answers.indices)
                    answerText = "$answerText ${convertDigitToLetter(i+2)})${this.answers[i].text} "
                return answerText
            }

            private fun convertDigitToLetter(digit: Int): Char {
                if (digit < 1 || digit > 26) {
                    throw IllegalArgumentException("Digit must be between 1 and 26.")
                }
                return 'A' + digit - 1
            }

            private fun convertLetterToDigit(letter: Char): Int {
                if (!letter.isLetter() || !letter.isUpperCase()) {
                    throw IllegalArgumentException("Letter must be a capital letter of the English alphabet.")
                }
                return letter.toInt() - 'A'.toInt() + 1
            }
        }

        data class ChatCompletion(
            val id: String,
            @Json(name = "object") val obj: String,
            val created: Long,
            val model: String,
            val usage: Usage,
            val choices: List<Choice>
        )
        data class Usage(
            val prompt_tokens: Int,
            val completion_tokens: Int,
            val total_tokens: Int
        )
        data class Choice(
            val message: Message,
            val finish_reason: String,
            val index: Int
        )
        data class Message(val role: String, val content: String)
    }
}