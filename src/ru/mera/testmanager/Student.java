package ru.mera.testmanager;

public class Student {

    private final String name;
    private String password;

    public Student (String name, String password) {
        this.name = name;
        this.password = password;
    }

    String getName() {
        return name;
    }

    String getPassword(){
        return password;
    }

}
