package com.example.myapplication

import android.os.*
import android.speech.tts.TextToSpeech
import android.view.View
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.bumptech.glide.Glide
import java.util.*

class MainActivity : AppCompatActivity(), TextToSpeech.OnInitListener {

    private val viewModel: MainViewModel by viewModels()
    private lateinit var tts: TextToSpeech

    private lateinit var cardSelectionLayout: View
    private lateinit var roastMainLayout: View
    private lateinit var judgeBtn: Button
    private lateinit var resultText: TextView
    private lateinit var loadingText: TextView
    private lateinit var gifView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize Views
        cardSelectionLayout = findViewById(R.id.cardSelectionLayout)
        roastMainLayout = findViewById(R.id.roastMainLayout)
        judgeBtn = findViewById(R.id.judgeBtn)
        resultText = findViewById(R.id.resultText)
        loadingText = findViewById(R.id.loadingText)
        gifView = findViewById(R.id.gifView)

        val card1 = findViewById<CardView>(R.id.card1)
        val card2 = findViewById<CardView>(R.id.card2)
        val card3 = findViewById<CardView>(R.id.card3)

        tts = TextToSpeech(this, this)

        // 1. Initial Button Click: Show Cards
        judgeBtn.setOnClickListener {
            showCardSelection()
        }

        // 2. Card Selection: Trigger Roast
        val cardClickListener = View.OnClickListener {
            hideCardSelection()
            viewModel.judgeLife()
        }

        card1.setOnClickListener(cardClickListener)
        card2.setOnClickListener(cardClickListener)
        card3.setOnClickListener(cardClickListener)

        viewModel.loading.observe(this) { isLoading ->
            loadingText.visibility = if (isLoading) View.VISIBLE else View.GONE
            // Keep main roast layout visible during loading to show progress
            if (isLoading) {
                resultText.text = "The Judge is calculating your doom..."
            }
        }

        viewModel.result.observe(this) { res ->
            res?.let {
                val fullText = """
🤬 The Judge says:
${it.insult}

🧠 Your only hope:
${it.advice}

🗣 Kanye intervenes:
${it.quote}

❓ FINAL VERDICT: ${it.verdict}
                """.trimIndent()

                resultText.text = fullText

                if (it.gif.isNotEmpty()) {
                    Glide.with(this)
                        .load(it.gif)
                        .placeholder(android.R.drawable.progress_horizontal)
                        .error(android.R.drawable.stat_notify_error)
                        .into(gifView)
                }

                // Vibration
                vibrateDevice()

                // Speak insult
                tts.speak(it.insult, TextToSpeech.QUEUE_FLUSH, null, null)
            }
        }
    }

    private fun showCardSelection() {
        roastMainLayout.visibility = View.GONE
        cardSelectionLayout.visibility = View.VISIBLE
    }

    private fun hideCardSelection() {
        cardSelectionLayout.visibility = View.GONE
        roastMainLayout.visibility = View.VISIBLE
    }

    private fun vibrateDevice() {
        val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager = getSystemService(VIBRATOR_MANAGER_SERVICE) as VibratorManager
            vibratorManager.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            getSystemService(VIBRATOR_SERVICE) as Vibrator
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(300, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            @Suppress("DEPRECATION")
            vibrator.vibrate(300)
        }
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            tts.language = Locale.US
        }
    }

    override fun onDestroy() {
        if (::tts.isInitialized) {
            tts.stop()
            tts.shutdown()
        }
        super.onDestroy()
    }
}