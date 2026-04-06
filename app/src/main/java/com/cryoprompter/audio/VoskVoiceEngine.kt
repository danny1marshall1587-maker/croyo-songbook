package com.cryoprompter.audio

import android.content.Context
import org.vosk.Model
import org.vosk.Recognizer
import org.vosk.android.SpeechService
import org.vosk.android.StorageService
import java.io.IOException

class VoskVoiceEngine(private val context: Context) {
    private var model: Model? = null
    private var speechService: SpeechService? = null
    private var listener: RecognitionListener? = null

    interface RecognitionListener {
        fun onResult(text: String)
        fun onPartialResult(text: String)
        fun onCommand(command: FuzzyMatcher.Command)
        fun onError(e: Exception)
    }

    fun initModel(onComplete: (Boolean) -> Unit) {
        StorageService.unpack(context, "model-en-us", "model",
            { model ->
                this.model = model
                onComplete(true)
            },
            { e ->
                onComplete(false)
            }
        )
    }

    fun startListening(grammar: List<String>, listener: RecognitionListener) {
        this.listener = listener
        model?.let {
            try {
                // Initialize Vosk Recognizer (16k sample rate)
                val rec = Recognizer(it, 16000.0f)

                speechService = SpeechService(rec, 16000.0f)
                speechService?.startListening(object : org.vosk.android.RecognitionListener {
                    override fun onResult(hypothesis: String) {
                        val text = parseHypothesis(hypothesis)
                        if (text.isEmpty()) return

                        // I. Check for "Computer" Commands First
                        val command = FuzzyMatcher.detectCommand(text)
                        if (command != null) {
                            listener.onCommand(command)
                        } else {
                            // II. Process as Lyric Track
                            listener.onResult(text)
                        }
                    }

                    override fun onPartialResult(hypothesis: String) {
                        listener.onPartialResult(parseHypothesis(hypothesis))
                    }

                    override fun onFinalResult(hypothesis: String) {}

                    override fun onError(e: Exception) {
                        listener.onError(e)
                    }

                    override fun onTimeout() {
                        // Stage-Safe: Auto-restart if silence is too long (solos)
                        stopListening()
                        startListening(grammar, listener)
                    }
                })
            } catch (e: Exception) {
                listener.onError(e)
            }
        }
    }

    fun stopListening() {
        speechService?.stop()
        speechService = null
    }

    private fun parseHypothesis(hypothesis: String): String {
        val regex = """"text"\s*:\s*"(.*?)"""".toRegex()
        val partialRegex = """"partial"\s*:\s*"(.*?)"""".toRegex()
        return regex.find(hypothesis)?.groupValues?.get(1) 
            ?: partialRegex.find(hypothesis)?.groupValues?.get(1) 
            ?: ""
    }
}
