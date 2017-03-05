package com.phg.minitron.dao

import java.sql.Connection
import java.sql.DriverManager
import java.sql.PreparedStatement
import java.sql.SQLException
/**
 * Created by milesporter on 2/26/17.
 */
class BaseDao {

    static Connection conn = null;

    static Connection getConnection() throws URISyntaxException, SQLException {
        if (conn==null) {
            String dbUrl = System.getenv("JDBC_DATABASE_URL")
            Properties props = new Properties();
            if (dbUrl==null) {
                dbUrl="jdbc:postgresql://localhost/postgres"
                props.setProperty("user","postgres");
                props.setProperty("password","secret");
                conn = DriverManager.getConnection(dbUrl, props)
            } else {
                conn = DriverManager.getConnection(dbUrl)
            }

        }
        return conn
    }


    def getPreparedStatement(String sql) {
        conn = getConnection()
        PreparedStatement preparedStatement = conn.prepareStatement(sql)
        return preparedStatement
    }
}
