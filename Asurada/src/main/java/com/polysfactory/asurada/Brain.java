package com.polysfactory.asurada;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.util.List;
import java.util.Locale;

/**
 * Created by poly on 7/26/13.
 */
public class Brain {
    private BrainListener mBrainListener;
    private final GooglePlacesApiWrapper mGooglePlaceApi;
    private volatile Handler main;

    public Brain(Context context) {
        mGooglePlaceApi = new GooglePlacesApiWrapper(context);
        main = new Handler();
    }

    public void setBrainListener(BrainListener brainListener) {
        this.mBrainListener = brainListener;
    }

    // This is rule based for now...
    public void ask(final String input) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final String ans = _ask(input);
                main.post(new Runnable() {
                    @Override
                    public void run() {
                        if (mBrainListener != null) {
                            mBrainListener.onAnswer(ans);
                        }
                    }
                });
            }
        }).start();
    }

    private String _ask(String input) {
        String answer = "";
        input = input.toLowerCase(Locale.US);
        if (input.contains("hello")) {
            answer = "hey how are you doing?";
        } else if (input.contains("thank")) {
            answer = "you're welcome";
        } else if (input.contains("how are you")) {
            answer = "good. how are you?";
        } else if (input.contains("awesome")) {
            answer = "awesome";
        } else if (input.contains("good day")) {
            answer = "you too!";
        } else if (input.contains("restaurant")) {
            List<String> foodPlaces = mGooglePlaceApi.getFoodPlaces();
            answer = "How about ";
            int len = foodPlaces.size();
            for (int i = 0; i < len; i++) {
                answer += foodPlaces.get(i);
                if (i < len - 2) {
                    answer += ",";
                } else if (i == len - 2) {
                    answer += " or ";
                }
            }
            answer += "?";
            // answer = "foooood....";
        } else {
            answer = "I'm sorry?";
        }
        return answer;
    }

    public interface BrainListener {
        public void onAnswer(String answer);
    }
}
