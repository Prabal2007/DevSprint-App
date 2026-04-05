package com.example.myapplication

import android.util.Log
import androidx.lifecycle.*
import kotlinx.coroutines.launch

data class JudgeResult(
    val insult: String,
    val advice: String,
    val quote: String,
    val verdict: String,
    val gif: String
)

class MainViewModel : ViewModel() {

    private val _result = MutableLiveData<JudgeResult?>()
    val result: LiveData<JudgeResult?> = _result

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    fun judgeLife() {
        _loading.value = true
        // Optional: clear previous result so UI can show a loading state
        // _result.value = null 

        viewModelScope.launch {
            try {
                val timestamp = System.currentTimeMillis()
                
                val insult = RetrofitInstance.insultApi.getInsult(timestamp)
                val advice = RetrofitInstance.adviceApi.getAdvice(timestamp)
                val kanye = RetrofitInstance.kanyeApi.getKanye(timestamp)
                val yesno = RetrofitInstance.yesNoApi.getYesNo(timestamp)

                _result.value = JudgeResult(
                    insult.insult,
                    advice.slip.advice,
                    kanye.quote,
                    yesno.answer.uppercase(),
                    yesno.image
                )

            } catch (e: Exception) {
                Log.e("MainViewModel", "Error fetching data: ${e.message}", e)
                _result.value = JudgeResult(
                    "Even APIs gave up on you 💀",
                    "Try turning your life off and on again. (${e.javaClass.simpleName})",
                    "I am the glitch.",
                    "NO",
                    "https://media.giphy.com/media/26BkNPTXGoS71wthu/giphy.gif"
                )
            }
            _loading.value = false
        }
    }
}