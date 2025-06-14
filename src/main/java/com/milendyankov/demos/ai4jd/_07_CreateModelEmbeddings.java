package com.milendyankov.demos.ai4jd;

import com.pgvector.PGvector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class _07_CreateModelEmbeddings {

    public static void main(String[] args) throws SQLException {

        Connection conn = PostgressDB.getConnection();

        PreparedStatement selectStmt = conn.prepareStatement("SELECT title, description FROM books_embeddings");
        PreparedStatement updateStmt = conn.prepareStatement("UPDATE books_embeddings SET embeddings = ? WHERE title = ?");
        ResultSet resultSet = selectStmt.executeQuery();

        while (resultSet.next()) {
            String title = resultSet.getString("title");
            String description = resultSet.getString("description");
            if (description != null) {
                float[] vector = ModelEmbeddings.embed(description);
                // Update the database
                updateStmt.setObject(1, new PGvector(vector));
                updateStmt.setString(2, title);
                updateStmt.executeUpdate();
            }
        }
        System.out.println("Embeddings update complete.");
    }

}
