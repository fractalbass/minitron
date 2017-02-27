package com.phg.minitron.dao

import com.phg.minitron.model.User
import org.springframework.stereotype.Component

import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet

/**
 * Created by milesporter on 2/26/17.
 */
@Component
class UserDao extends BaseDao{

    boolean save(User user) {
        boolean result = false;
        try {
            PreparedStatement preparedStatement = getPreparedStatement("insert into user (userId, email, password) values (?,?,?)")
            preparedStatement.setString(1, user.getUserId().toString())
            preparedStatement.setString(2, user.getEmail())
            preparedStatement.setString(3, user.getPassword())
            preparedStatement.execute()
            result=true
        } catch (Exception exp) {
            System.out.println("Error: " + exp.toString())
        }
        return result
    }

    User getUser(User user) {
        try {
            Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement("Select userId from user where email=? and password=?");
            ps.setString(1,user.email);
            ps.setInt(2,user.password);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                user.setUserId(rs.getString(1));
            }
        } catch (Exception exp) {
            System.out.println("oops." + exp.toString());
        }
        return user;
    }

    User update(User user) {
        null
    }
}
