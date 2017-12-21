package ru.mera.testmanager;

import java.sql.*;

public class StudentLoaderFromDBase {
    private Connection connection;

    public StudentLoaderFromDBase(Connection connection) {
        this.connection = connection;
    }

    public Student loadByEmail(String email) throws LoadException {
        Student student = null;
        try(PreparedStatement statement = connection.prepareStatement("SELECT * FROM student WHERE email = ?")) {
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()) {
                student = new Student(resultSet.getString("name"), resultSet.getString("password"));
            }
        } catch (SQLException e){
            throw new LoadException("Ошибка при поиске студента", e);
        }
        return student;
    }
}
