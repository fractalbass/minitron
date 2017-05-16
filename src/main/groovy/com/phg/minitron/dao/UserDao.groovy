package com.phg.minitron.dao

import com.phg.minitron.model.User
import groovy.util.logging.Slf4j
import org.springframework.stereotype.Component

import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet

/**
 * Created by milesporter on 2/26/17.
 */
@Slf4j
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
            log.error("Error saving user: " + exp.toString())
        }
        return result
    }

    User getUserByUserId(String userId) {
        User user = null;
        try {
            Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement("Select email from mtuser where userId=?")
            ps.setString(1,userId)
            ResultSet rs = ps.executeQuery()
            while (rs.next()) {
                user = new User()
                user.userId = userId
                user.email = rs.getString(1)
            }
        } catch (Exception exp) {
            log.error("Error getting user by ID: " + exp.toString());
        }
        return user;
    }

    boolean checkIfEmailExists(String email) {
        int count=0
        try {
            Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement("Select count(email) from mtuser where email=?");
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery()
            while (rs.next()) {
                count = rs.getInt(1)
            }
        }
        catch (Exception exp) {
            log.error("Error checking if user email exists: " + exp.toString())
        }
        return (count>0)
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
           log.error("Error updating user: " + exp.toString());
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
            log.error("Error cleaning (deleting) all users: " + exp.toString());
        }
        return result
    }

    boolean deleteUserById(String userId) {
        boolean result = false
        try {
            Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement("delete from mtuser where userId=?");
            ps.setString(1, userId)
            ps.execute();
            result = true
        } catch (Exception exp) {
            log.error("Error deleting user by userId: " + exp.toString());
        }
        return result
    }

    User getUserByEmail(User user) {
        try {
            Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement("Select userId from mtuser where email=?")
            ps.setString(1,user.email)
            //ps.setString(2,user.getPasswordHash())

            ResultSet rs = ps.executeQuery()
            while (rs.next()) {
                String uid = rs.getString(1)
                user.userId=uid
            }
        } catch (Exception exp) {
            log.error("Error getting user by email: " + exp.toString());
        }
        return user;
    }

    User getUserByEmailAndPassword(User user) {
        User authUser = null
        try {
            Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement("Select userId from mtuser where email=? and password=?")
            ps.setString(1,user.email)
            ps.setString(2,user.getPasswordHash())

            ResultSet rs = ps.executeQuery()
            while (rs.next()) {
                String uid = rs.getString(1)
                authUser = user
                authUser.userId = uid
            }
        } catch (Exception exp) {
            log.error("Error getting user by email and password: " + exp.toString());
        }
        return authUser;
    }

    List<User> getAllUsers() {
        ArrayList<User> users = new ArrayList<>()
        try {
            Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement("Select userId, email from mtuser order by userId")
            ResultSet rs = ps.executeQuery()
            while (rs.next()) {
                User u = new User()
                u.userId=rs.getString(1)
                u.email=rs.getString(2)
                users.add(u)
            }
        } catch (Exception exp) {
            log.error("Error getting all users: " + exp.toString());
        }
        return users;
    }

}
