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
            PreparedStatement preparedStatement = getPreparedStatement("insert into mtuser (userId, email, password) values (?,?,?)")
            preparedStatement.setString(1, user.getUserId().toString())
            preparedStatement.setString(2, user.getEmail())
            preparedStatement.setString(3, user.getPasswordHash())
            preparedStatement.execute()
            result=true
        } catch (Exception exp) {
            System.out.println("Error: " + exp.toString())
        }
        return result
    }

    User getUserByEmailAndPassword(User user) {
        try {
            Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement("Select userId from mtuser where email=? and password=?")
            ps.setString(1,user.email)
            ps.setString(2,user.getPasswordHash())

            ResultSet rs = ps.executeQuery()
            while (rs.next()) {
                String uid = rs.getString(1)
                user.userId=uid
            }
        } catch (Exception exp) {
            System.out.println("oops." + exp.toString());
        }
        return user;
    }

    User update(User user) {
        try {
            Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement("Update mtuser set email=?, password=? where userId=?");
            ps.setString(3, user.userId)
            ps.setString(1, user.email);
            ps.setString(2, user.getPasswordHash());
            ps.execute();
        } catch (Exception exp) {
            System.out.println("oops." + exp.toString());
        }
        return user;
    }

    boolean clean() {
        boolean result = false
        try {
            Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement("delete from mtuser");
            ps.execute();
            result = true
        } catch (Exception exp) {
            System.out.println("oops." + exp.toString());
        }
        return result
    }

    User getUserByEmail(User user) {
        try {
            Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement("Select userId from mtuser where email=?")
            ps.setString(1,user.email)
            ps.setString(2,user.getPasswordHash())

            ResultSet rs = ps.executeQuery()
            while (rs.next()) {
                String uid = rs.getString(1)
                user.userId=uid
            }
        } catch (Exception exp) {
            System.out.println("oops." + exp.toString());
        }
        return user;
    }



}
