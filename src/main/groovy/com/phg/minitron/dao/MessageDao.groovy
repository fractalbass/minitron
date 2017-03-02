package com.phg.minitron.dao;

import com.phg.minitron.model.Message;
import org.springframework.stereotype.Component

import java.sql.*;

/**
 * Created by milesporter on 2/20/17.
 */
@Component
public class MessageDao extends BaseDao{

    def save(Message message) {
        boolean result = false;
        try {
            PreparedStatement preparedStatement = getPreparedStatement("insert into message (device, channel, message) values (?,?,?)")
            preparedStatement.setInt(1, message.getDevice())
            preparedStatement.setInt(2, message.getChannel())
            preparedStatement.setString(3, message.getMessageText())
            preparedStatement.execute()
            result=true
        } catch (Exception exp) {
            System.out.println("Error: " + exp.toString())
        }
        return result
    }

    def update(Message message) {
        boolean result = false;
        try {
            PreparedStatement preparedStatement = getPreparedStatement("update message set message=? where device=? and channel=?")
            preparedStatement.setString(1, message.getMessageText())
            preparedStatement.setInt(2, message.getDevice())
            preparedStatement.setInt(3, message.getChannel())
            preparedStatement.execute()
            result=true
        } catch (Exception exp) {
            System.out.println("Error: " + exp.toString())
        }
        return result
    }

    public Message get(Message message) {
        try {
            Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement("Select message from message where device=? and channel=?");
            ps.setInt(1,message.getDevice());
            ps.setInt(2,message.getChannel());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                message.setMessageText(rs.getString(1));
            }
        } catch (Exception exp) {
            System.out.println("oops." + exp.toString());
        }
        return message;
    }

    def delete(Message message) {
        def result = false
        try {
            Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement("Delete from message where device=? and channel=?");
            ps.setInt(1,message.getDevice());
            ps.setInt(2,message.getChannel());
            ps.execute();
            result=true
        } catch (Exception exp) {
            System.out.println("oops." + exp.toString());
        }
        result
    }

    def clean() {
        def result = false
        try {
            Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement("Delete from message");
            ps.execute();
            result=true
        } catch (Exception exp) {
            System.out.println("oops." + exp.toString());
        }
        result
    }
}
