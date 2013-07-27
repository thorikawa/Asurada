package com.polysfactory.asurada;

/**
 * Created by poly on 7/26/13.
 */
public class Brain {
    // This is rule based for now...
    public String answer (String input) {
        if (input.contains("hello")) {
            return "hey how are you doing?";
        }
        if (input.contains("thank")) {
            return "you're welcome";
        }
        if (input.contains("how are you")) {
            return "good. how are you?";
        }
        if (input.contains("awesome")) {
            return "awesome";
        }
        if (input.contains("good day")) {
            return "you too!";
        }
        return "I'm sorry?";
    }
}
