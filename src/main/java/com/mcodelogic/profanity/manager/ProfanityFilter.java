package com.mcodelogic.profanity.manager;

import org.ahocorasick.trie.Trie;

import java.util.Set;

public final class ProfanityFilter {

    public static boolean containsProfanity(String message, FilterMode mode, Set<String> bannedWords, Trie trie) {
        if (message == null || message.isBlank()) return false;

        return switch (mode) {
            case SIMPLE -> simpleCheck(message, bannedWords);
            case MODERATE -> moderateCheck(message, bannedWords);
            case ADVANCED -> advancedCheck(message, trie);
        };
    }

    private static boolean simpleCheck(String message, Set<String> bannedWords) {
        for (String word : message.toLowerCase().split("\\s+")) {
            if (bannedWords.contains(word)) {
                return true;
            }
        }
        return false;
    }

    private static boolean moderateCheck(String message, Set<String> bannedWords) {
        String normalized = normalize(message);

        // Word check
        for (String word : normalized.split(" ")) {
            if (bannedWords.contains(word)) {
                return true;
            }
        }

        // Space exploit check (f u c k)
        String noSpaces = normalized.replace(" ", "");
        for (String banned : bannedWords) {
            if (noSpaces.contains(banned)) {
                return true;
            }
        }
        return false;
    }

    private static String normalize(String input) {
        String s = input.toLowerCase();

        s = java.text.Normalizer.normalize(s, java.text.Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "");

        s = s.replaceAll("[^a-z]", " ");
        return s.replaceAll("\\s+", " ").trim();
    }

    private static boolean advancedCheck(String message, Trie trie) {
        String normalized = normalize(message).replace(" ", "");
        return trie.parseText(normalized).iterator().hasNext();
    }
}

