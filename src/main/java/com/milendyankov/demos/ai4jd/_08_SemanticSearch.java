package com.milendyankov.demos.ai4jd;

import com.pgvector.PGvector;

import java.sql.*;

public class _08_SemanticSearch {
    public static void main(String[] args) throws SQLException {
        Connection conn = PostgressDB.getConnection();

        String query = "I want to learn to play a song on a piano";

        PreparedStatement neighborStmt = conn.prepareStatement(
            """
                    SELECT
                        title,
                        description,
                        embeddings <-> ? AS distance
                    FROM books_embeddings
                    ORDER BY distance
                    LIMIT 5
                """);
        var queryVector = new PGvector(ModelEmbeddings.embed(query));
        neighborStmt.setObject(1, queryVector);
        ResultSet resultSet = neighborStmt.executeQuery();

        System.out.println("❓" + query);
        while (resultSet.next()) {
            System.out.println("------ (" + resultSet.getString("distance") +")");
            System.out.println("\t\uD83D\uDCD7" + resultSet.getString("title"));
            System.out.println("\t" + resultSet.getString("description"));
        }
        resultSet.close();
    }
}
