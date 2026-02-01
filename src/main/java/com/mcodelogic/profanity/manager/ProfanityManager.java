package com.mcodelogic.profanity.manager;

import com.hypixel.hytale.server.core.util.Config;
import com.mcodelogic.profanity.config.KConfig;
import lombok.Getter;
import org.ahocorasick.trie.Trie;

import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

@Getter
public class ProfanityManager {

    private Config<KConfig> config;

    private Set<String> bannedWords;

    private Trie trie;

    public ProfanityManager(Config<KConfig> config, Path baseDir) {
        this.config = config;
        this.bannedWords = new HashSet<>();
        loadWords(ProfanityWordLoader.load(baseDir));
    }

    public void loadWords(Set<String> words) {
        bannedWords.clear();
        bannedWords.addAll(words);
        trie = Trie.builder()
                .ignoreCase()
                .addKeywords(bannedWords)
                .build();
    }

    public boolean checkMessage(String message) {
        FilterMode mode = getFilterMode(config.get().getFilterMode());
        return ProfanityFilter.containsProfanity(message, mode, bannedWords, trie);
    }

    public FilterMode getFilterMode(String mode) {
        try {
            return FilterMode.valueOf(mode);
        } catch (IllegalArgumentException e) {
            return FilterMode.MODERATE;
        }
    }


}
