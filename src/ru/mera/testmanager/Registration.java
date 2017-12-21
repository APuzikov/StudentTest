package ru.mera.testmanager;

import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Registration {

    private String name;
    private String email;
    private String password;
    private Connection connection;

    public Registration(Connection connection) {
        this.connection = connection;
    }

    BufferedReader getReader() {

        return new BufferedReader(new InputStreamReader(System.in));
    }

    private void readName(BufferedReader reader) throws IOException{
        System.out.println("Введите ваше имя:");
            while (true) {
                name = reader.readLine();
                if (checkName(name)) break;
                System.out.println("Не очень-то похоже на имя, попробуй еще раз.");
            }

        //return name;
    }

    private boolean checkName(String name){
        Pattern regex = Pattern.compile("[a-zA-Z]+");
        Matcher matcher = regex.matcher(name);
        return matcher.matches();
    }

    private void readEmail(BufferedReader reader) throws IOException{
        System.out.println("Введите адрес электронной почты:");
         while(true) {
                email = reader.readLine();
                if (checkEmail(email)) break;
                System.out.println("Не очень-то похоже на email, попробуй еще раз.");
            }

        //return email;
    }

    private boolean checkEmail(String email){
        Pattern regex = Pattern.compile("\\w+([.-]?\\w+)*@\\w+([.-]?\\w+)*\\.\\w{2,4}");
        Matcher matcher = regex.matcher(email);
        return matcher.matches();
    }

    private void readPassword(BufferedReader reader) throws IOException{
        System.out.println("Введите ваш пароль(не менее 5 символов):");
        while (true){
            password = reader.readLine();
            if(checkPassword(password)) break;
            System.out.println("Плохой пароль");
        }
    }

    private boolean checkPassword(String password){
        Pattern regex = Pattern.compile("\\w+\\W+|\\W+\\w+");
        Matcher matcher = regex.matcher(password);
        return matcher.matches() && password.length() >= 5;
    }

    private void saveToDBase(String name, String email, String password) throws SQLException {

        try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO student (name, email, password) VALUES(?, ?, ?);")){

            preparedStatement.setString(1, name);
            preparedStatement.setString(2, email);
            preparedStatement.setString(3, password);
            preparedStatement.executeUpdate();
        }

    }

    public static void main(String[] args) {

        try {

            TestManager testManager = new TestManager(args[0]);
            try(Connection connection = testManager.getConnection()) {
                Registration registration = new Registration(connection);
                try (BufferedReader reader = registration.getReader()) {

                    registration.readName(reader);
                    registration.readEmail(reader);
                    registration.readPassword(reader);

                    registration.saveToDBase(registration.name, registration.email, registration.password);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();

        }
    }
}
