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

    public static String removeFirstCamelCaseWord(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        for (int i = 1; i < input.length(); i++) {
            if (Character.isUpperCase(input.charAt(i))) {
                return Character.toLowerCase(input.charAt(i)) + input.substring(i + 1);
            }
        }
        return "";
    }

    public static String convertToolNameToAssetName(String name) {
        if (name == null) {
            return null;
        }
        if (name.equals("Hoe")) {
            return "hoe";
        }
        if (name.equals("Pickaxe")) {
            return "pickaxe";
        }
        if (name.equals("Axe")) {
            return "axe";
        }
        if (name.equals("Watering Can Default") || name.equals("Watering Can Copper") || name.equals("Watering Can Iron")
            || name.equals("Watering Can Gold") || name.equals("Watering Can Iridium")) {
            return "can";
        }
        if (name.equals("Fishing Rod")) {
            return "rod";
        }
        if (name.equals("Scythe")) {
            return "scythe";
        }
        if (name.equals("Shear")) {
            return "shear";
        }
        return null;
    }
}
