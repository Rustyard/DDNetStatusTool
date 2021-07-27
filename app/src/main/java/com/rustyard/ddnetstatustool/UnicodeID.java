package com.rustyard.ddnetstatustool;

import androidx.annotation.NonNull;

public class UnicodeID {
    public static String parse(@NonNull String playerID) {
        if (playerID.isEmpty()) throw new IllegalArgumentException("playerID is empty");
        StringBuilder sbPlayerID = new StringBuilder();
        int codePointCount = playerID.codePointCount(0, playerID.length());
        for (int i = 0; i < codePointCount; i++) {
            // get the actual code point for every UTF characters one by one
            int index = playerID.offsetByCodePoints(0, i);
            int codePoint = playerID.codePointAt(index);
            // check if this character needs to be replaced
            if (!Character.isSupplementaryCodePoint(codePoint)) {
                if (Character.isLetterOrDigit(codePoint) && codePoint < 256)
                    sbPlayerID.append((char) codePoint);
                else
                    sbPlayerID.append("-").append(codePoint).append("-");
            } else {
                sbPlayerID.append("-").append(codePoint).append("-");
            }
        }
        return sbPlayerID.toString();
    }
}
