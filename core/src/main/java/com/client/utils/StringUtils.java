package com.client.utils;

public class StringUtils {
    public static String convertToCamelCase(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }

        String[] words = input.split("[^a-zA-Z0-9]");
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < words.length; i++) {
            String word = words[i];
            if (word.isEmpty()) {
                continue;
            }

            if (i == 0) {
                result.append(word.substring(0, 1).toLowerCase());
            } else {
                result.append(word.substring(0, 1).toUpperCase());
            }

            if (word.length() > 1) {
                result.append(word.substring(1).toLowerCase());
            }
        }

        return result.toString();
    }
}
