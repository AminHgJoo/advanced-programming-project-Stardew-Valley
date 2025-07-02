package com.server.utilities;

public class RNG {
    public static boolean performRandomTrial(int successChance) {
        int randomInt = (int) (Math.random() * 100);
        return randomInt < successChance;
    }

    /// returns a random int in the range \[min,max\] (inclusive)
    public static int randomInt(int min, int max) {
        return (int) (Math.random() * (max - min + 1) + min);
    }
}
