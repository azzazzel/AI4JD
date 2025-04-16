package com.milendyankov.demos.ai4jd;

import com.pgvector.PGvector;

import java.sql.*;

public class _08_SemanticSearch {
    public static void main(String[] args) throws SQLException {
        Connection conn = PostgressDB.getConnection();

        String query = "I want to learn to play a song on a piano";

        PreparedStatement neighborStmt = conn.prepareStatement("SELECT * FROM books_embeddings ORDER BY embeddings <-> ? LIMIT 5");
        neighborStmt.setObject(1, new PGvector(ModelEmbeddings.generateEmbedding(query)));
        ResultSet resultSet = neighborStmt.executeQuery();
        while (resultSet.next()) {
            System.out.println("---");
            System.out.println(resultSet.getString("title"));
            System.out.println("---");
            System.out.println("\t" + resultSet.getString("description"));
        }
        resultSet.close();
    }
}
