package ru.mera.testmanager;

public enum DifficultyLevel {

    EASY("Лёгкий", 1), NORMAL("Средний", 2), HARD("Сложный", 3);

    private String name;
    private int level;

    DifficultyLevel(String name, int level){
        this.name = name;
        this.level = level;
    }

    String getName(){
        return name;
    }
    int getLevel() {
        return level;
    }
}
