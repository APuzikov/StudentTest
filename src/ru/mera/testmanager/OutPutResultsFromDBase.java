package ru.mera.testmanager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class OutPutResultsFromDBase {

    private Connection connection;

    public OutPutResultsFromDBase(Connection connection) {
        this.connection = connection;
    }

    public void outPutStudentTest(String name) throws SQLException {
        try(PreparedStatement preparedStatement = connection.prepareStatement("SELECT id FROM student WHERE name = ?;")){
            preparedStatement.setString(1, name);
            ResultSet resultSet =preparedStatement.executeQuery();


        }

    }


}
