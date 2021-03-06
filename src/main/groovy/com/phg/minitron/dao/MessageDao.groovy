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
            PreparedStatement preparedStatement = getPreparedStatement("insert into message (deviceId, channel, message, messageId) values (?,?,?,?)")
            preparedStatement.setString(1, message.getDeviceId())
            preparedStatement.setInt(2, message.getChannel())
            preparedStatement.setString(3, message.getMessageText())
            preparedStatement.setString(4, message.getMessageId())
            preparedStatement.execute()
            result=true
        } catch (Exception exp) {
            System.out.println("Error: " + exp.toString())
        }
        return result
    }

    def update(Message message) {
        def result = null;
        try {
            PreparedStatement preparedStatement = getPreparedStatement("update message set message=?, channel=?, deviceId=? where messageId=?")
            preparedStatement.setString(1, message.getMessageText())
            preparedStatement.setInt(2, message.getChannel())
            preparedStatement.setString(3, message.getDeviceId())
            preparedStatement.setString(4, message.getMessageId())
            preparedStatement.execute()
            result=message
        } catch (Exception exp) {
            System.out.println("Error: " + exp.toString())
        }
        return result
    }

    public Message getByDeviceAndChannel(Message message) {
        try {
            Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement("Select message, messageId from message where deviceId=? and channel=?");
            ps.setString(1,message.getDeviceId());
            ps.setInt(2,message.getChannel());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                message.setMessageText(rs.getString(1));
                message.setMessageId(rs.getString(2))
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
            PreparedStatement ps = conn.prepareStatement("Delete from message where messageId=?");
            ps.setString(1, message.getMessageId());
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

    public ArrayList<Message> getByDevice(String deviceId) {
        ArrayList<Message> messages = new ArrayList<>()
        try {
            Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement("Select message, messageId, channel from message where deviceId=? order by channel");
            ps.setString(1,deviceId.toString());

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Message message = new Message()
                message.messageText= rs.getString(1)
                message.messageId = rs.getString(2)
                message.channel = rs.getInt(3)
                messages.add(message)
            }
        } catch (Exception exp) {
            System.out.println("oops." + exp.toString());
        }
        return messages;
    }

    public Message getByMessageId(String messageId) {
        Message message = null
        try {
            Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement("Select message, channel, deviceId from message where messageId=?");
            ps.setString(1,messageId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                message = new Message()
                message.setMessageText(rs.getString(1))
                message.setChannel(rs.getInt(2))
                message.setDeviceId(rs.getString(3))
                message.messageId = UUID.fromString(messageId)
            }
        } catch (Exception exp) {
            System.out.println("Error getting message by message ID." + exp.toString());
        }
        return message;
    }


}
