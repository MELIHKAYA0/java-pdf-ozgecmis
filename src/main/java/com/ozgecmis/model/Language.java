package com.ozgecmis.model;

import java.util.Objects;

/** Bir yabancı dil ve seviyesi. Puan PDF'te 5 noktalı gösterge olarak çizilir. */
public final class Language {

    /** Sınıfa ait sabit: tüm Language nesneleri için ortak en yüksek puan. */
    public static final int MAX_SCORE = 5;

    private final String name;
    private final String level;
    private final int score;

    public Language(String name, String level, int score) {
        if (score < 1 || score > MAX_SCORE) {
            throw new IllegalArgumentException("score 1 ile " + MAX_SCORE + " arasında olmalı: " + score);
        }
        this.name = Objects.requireNonNull(name, "name");
        this.level = Objects.requireNonNull(level, "level");
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public String getLevel() {
        return level;
    }

    public int getScore() {
        return score;
    }
}
