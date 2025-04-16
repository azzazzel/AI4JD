package com.milendyankov.demos.ai4jd;

import com.pgvector.PGvector;

import java.sql.*;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class _05_VectorSeachOverNaiveEmbeddings {

    public static void main(String[] args) throws SQLException {
        Connection conn = PostgressDB.getConnection();

        String query = "I want to learn to play a song on a piano";

        // tokenize and convert query to vector
        PreparedStatement toVectorStmt = conn.prepareStatement("SELECT tsvector_to_array(to_tsvector('english', ?)) AS tokens ");
        toVectorStmt.setString(1, query);
        ResultSet resultSet = toVectorStmt.executeQuery();
        resultSet.next();
        Array searchTokensArray = resultSet.getArray("tokens");
        Set<String> searchTokens = Arrays.stream((String[]) searchTokensArray.getArray()).collect(Collectors.toSet());
        resultSet.close();

        // find nearest vectors
        PreparedStatement neighborStmt = conn.prepareStatement("SELECT * FROM books_vector ORDER BY embedding <-> ? LIMIT 5");
        neighborStmt.setObject(1, new PGvector(NaiveEmbeddings.generateEmbedding(searchTokens)));
        resultSet = neighborStmt.executeQuery();
        while (resultSet.next()) {
            System.out.println("---");
            System.out.println(resultSet.getString("title"));
            System.out.println("---");
            System.out.println("\t" + resultSet.getString("description"));
        }
        resultSet.close();
    }

}
