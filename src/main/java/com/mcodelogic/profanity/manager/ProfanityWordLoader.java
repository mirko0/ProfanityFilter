package com.mcodelogic.profanity.manager;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

public final class ProfanityWordLoader {

    private static final String RESOURCE_DEFAULT = "profanity/default.txt";

    private ProfanityWordLoader() {}

    private static boolean hasTxtFiles(Path dir) throws IOException {
        try (Stream<Path> files = Files.list(dir)) {
            return files.anyMatch(p -> p.toString().endsWith(".txt"));
        }
    }

    private static void copyDefaultFromResources(Path wordsDir)
            throws IOException {

        Path target = wordsDir.resolve("default.txt");

        try (InputStream in = ProfanityWordLoader.class
                .getClassLoader()
                .getResourceAsStream(RESOURCE_DEFAULT)) {

            if (in == null) {
                throw new FileNotFoundException(
                        "Resource " + RESOURCE_DEFAULT + " not found"
                );
            }

            Files.copy(in, target, StandardCopyOption.REPLACE_EXISTING);
        }
    }

    public static Set<String> load(Path baseDir) {
        Path wordsDir = baseDir.resolve("words");

        try {
            Files.createDirectories(wordsDir);

            if (!hasTxtFiles(wordsDir)) {
                copyDefaultFromResources(wordsDir);
            }

        } catch (IOException e) {
            throw new RuntimeException("Failed to initialize profanity words", e);
        }

        Set<String> words = new HashSet<>();

        try (DirectoryStream<Path> stream =
                     Files.newDirectoryStream(wordsDir, "*.txt")) {

            for (Path file : stream) {
                loadFile(file, words);
            }

        } catch (IOException e) {
            throw new RuntimeException("Failed to load profanity word files", e);
        }

        return words;
    }

    private static void loadFile(Path file, Set<String> words)
            throws IOException {

        for (String line : Files.readAllLines(file)) {
            String word = line
                    .trim()
                    .toLowerCase();

            if (word.isEmpty()) continue;
            if (word.startsWith("#")) continue; // comment support

            words.add(word);
        }
    }
}
