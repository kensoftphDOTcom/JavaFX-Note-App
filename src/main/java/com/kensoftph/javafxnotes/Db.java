package com.kensoftph.javafxnotes;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class Db {
    private Connection connection;
    private Logger logger = Logger.getLogger(this.getClass().getName());

    public void getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection("jdbc:sqlite:notes.db");
                logger.info("Connected to database");
                createTable();
            }
        } catch (SQLException e) {
            logger.info(e.toString());
        }
    }

    private void createTable() {
        getConnection();
        String query = "create table if not exists note (id integer not null primary key autoincrement, title text not null, content text not null)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.executeUpdate();
            logger.info("Table created");
        } catch (SQLException e) {
            logger.info(e.toString());
        }
    }

    private void closeConnection() throws SQLException {
        if (connection != null || !connection.isClosed()) {
            connection.close();
        }
    }

    public void insertNote(String title, String content) {
        getConnection();
        String query = "insert into note (title, content) values(?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, title);
            statement.setString(2, content);
            statement.executeUpdate();
            logger.info("Note inserted");

            closeConnection();
        } catch (SQLException e) {
            logger.info(e.toString());
        }
    }

    public void deleteNote(int id) {
        getConnection();
        String query = "delete from note where id = ?";
        try(PreparedStatement statement = connection.prepareStatement(query)){
            statement.setInt(1, id);
            statement.executeUpdate();
            logger.info("Note deleted");

            closeConnection();
        }catch (SQLException e){
            logger.info(e.toString());
        }
    }

    public void updateNote(int id, String title, String content) {
        getConnection();
        String query = "update note set title = ?, content = ? where id = ?";
        try(PreparedStatement statement = connection.prepareStatement(query)){
            statement.setString(1, title);
            statement.setString(2, content);
            statement.setInt(3, id);
            statement.executeUpdate();
            logger.info("Note updated");

            closeConnection();
        }catch (SQLException e){
            logger.info(e.toString());
        }
    }

    public List<Note> readNotes() {
        getConnection();
        String query = "select * from note";
        List<Note> notes = new ArrayList<>();
        try(PreparedStatement statement = connection.prepareStatement(query)){
            ResultSet rs = statement.executeQuery();

            while(rs.next()){
                int id = rs.getInt("id");
                String title = rs.getString("title");
                String content = rs.getString("content");
                notes.add(new Note(id, title, content));
            }

            closeConnection();
        }catch(SQLException e) {
            logger.info(e.toString());
        }

        return notes;
    }
}
