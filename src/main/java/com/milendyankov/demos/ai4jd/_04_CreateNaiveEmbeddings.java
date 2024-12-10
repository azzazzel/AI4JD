package com.milendyankov.demos.ai4jd;

import com.pgvector.PGvector;

import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

public class _04_CreateNaiveEmbeddings {

    public static void main(String[] args) throws SQLException {

        Connection conn = PostgressDB.getConnection();

        PreparedStatement selectStmt = conn.prepareStatement("SELECT title, tsvector_to_array(tokens) as tokens FROM books_vector");
        PreparedStatement updateStmt = conn.prepareStatement("UPDATE books_vector SET embedding = ? WHERE title = ?");
        ResultSet resultSet = selectStmt.executeQuery();

        while (resultSet.next()) {
            String title = resultSet.getString("title");
            Array tokensArray = resultSet.getArray("tokens");
            if (tokensArray != null) {
                Set<String> tokens = Arrays.stream((String[]) tokensArray.getArray()).collect(Collectors.toSet());
                float[] vector = NaiveEmbeddings.generateEmbedding(tokens);
                updateStmt.setObject(1, new PGvector(vector));
                updateStmt.setString(2, title);
                updateStmt.executeUpdate();
            }
        }
        System.out.println("Vector update complete.");
    }


}
