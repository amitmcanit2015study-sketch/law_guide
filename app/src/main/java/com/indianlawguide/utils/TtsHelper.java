package com.indianlawguide.utils;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.util.Log;

import java.util.Locale;

public class TtsHelper {

    private static final String TAG = "TtsHelper";
    private TextToSpeech textToSpeech;
    private boolean isInitialized = false;
    private TtsStateListener stateListener;

    public interface TtsStateListener {
        void onSpeechStart();
        void onSpeechDone();
        void onSpeechError();
    }

    public TtsHelper(Context context, TtsStateListener listener) {
        this.stateListener = listener;
        textToSpeech = new TextToSpeech(context.getApplicationContext(), status -> {
            if (status == TextToSpeech.SUCCESS) {
                int result = textToSpeech.setLanguage(new Locale("en", "IN"));
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    textToSpeech.setLanguage(Locale.US);
                }
                isInitialized = true;
                setupUtteranceListener();
            } else {
                Log.e(TAG, "TTS Initialization failed");
                if (stateListener != null) stateListener.onSpeechError();
            }
        });
    }

    private void setupUtteranceListener() {
        textToSpeech.setOnUtteranceProgressListener(new UtteranceProgressListener() {
            @Override
            public void onStart(String utteranceId) {
                if (stateListener != null) stateListener.onSpeechStart();
            }

            @Override
            public void onDone(String utteranceId) {
                if (stateListener != null) stateListener.onSpeechDone();
            }

            @Override
            public void onError(String utteranceId) {
                if (stateListener != null) stateListener.onSpeechError();
            }
        });
    }

    public void speak(String text) {
        if (isInitialized && textToSpeech != null && text != null) {
            textToSpeech.stop();
            textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, "LawGuideUtterance");
        }
    }

    public void stop() {
        if (textToSpeech != null) {
            textToSpeech.stop();
            if (stateListener != null) stateListener.onSpeechDone();
        }
    }

    public boolean isSpeaking() {
        return textToSpeech != null && textToSpeech.isSpeaking();
    }

    public void shutdown() {
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
    }
}
