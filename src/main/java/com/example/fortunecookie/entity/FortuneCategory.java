package com.example.fortunecookie.entity;

public enum FortuneCategory {
    LUCK("행운"),
    SUCCESS("성공"),
    RELATIONSHIP("인간관계"),
    LOVE("사랑"),
    HEALTH("건강"),
    CHALLENGE("도전"),
    POSITIVITY("긍정"),
    WEALTH("재물"),
    CREATIVITY("창의"),
    TRAVEL("여행"),
    WISDOM("지혜"),
    GRATITUDE("감사");

    private final String label;

    FortuneCategory(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
