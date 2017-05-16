package com.phg.minitron.dao

import com.phg.minitron.model.Message
import spock.lang.Specification

import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet

/**
 * Created by milesporter on 2/25/17.
 */
class MessageDaoSpec extends Specification{

    MessageDao messageDao
    Connection connection

    def setup() {
        messageDao = new MessageDao()
        connection = Mock(Connection)
        messageDao.conn = connection

    }

    def 'I should be able to create a message if no message exists'() {
        given:
        UUID messageId = UUID.randomUUID()
        Message newMessage = new Message(messageId: messageId.toString(), deviceId: '1', channel: 1, messageText: 'I do not care.')
        PreparedStatement preparedStatement = Mock(PreparedStatement)

        when:
        messageDao.save(newMessage)

        then:
        1 * connection.prepareStatement("insert into message (deviceId, channel, message, messageId) values (?,?,?,?)") >> preparedStatement
        1 * preparedStatement.setString(1,'1')
        1 * preparedStatement.setInt(2,1)
        1 * preparedStatement.setString(3, 'I do not care.')
        1 * preparedStatement.setString(4, messageId.toString())
        1 * preparedStatement.execute()
        0 * _
    }

    def 'I should be able to update an existing message'() {
            given:
            UUID messageId = UUID.randomUUID()
            Message newMessage = new Message(messageId: messageId.toString(), deviceId: '4', channel: 5, messageText: 'Update me.')
            PreparedStatement preparedStatement = Mock(PreparedStatement)

            when:
            messageDao.update(newMessage)

            then:
            1 * connection.prepareStatement("update message set message=?, channel=?, deviceId=? where messageId=?") >> preparedStatement
            1 * preparedStatement.setString(1,'Update me.')
            1 * preparedStatement.setInt(2,5)
            1 * preparedStatement.setString(3,'4')
            1 * preparedStatement.setString(4, messageId.toString())
            1 * preparedStatement.execute()
            0 * _
    }

    def 'I should be able to get a message by device and channel'() {
        given:
        Message newMessage = new Message(messageId: '789', deviceId: '123', channel: 4, messageText: 'get me.')
        PreparedStatement preparedStatement = Mock(PreparedStatement)
        ResultSet rs = Mock(ResultSet)
        def cnt=0
        when:
        messageDao.getByDeviceAndChannel(newMessage)

        then:

        1 * connection.prepareStatement("Select message, messageId from message where deviceId=? and channel=?") >> preparedStatement
        1 * preparedStatement.setString(1,'123')
        1 * preparedStatement.setInt(2,4)
        1 * preparedStatement.executeQuery() >> rs
        2 * rs.next() >> {
                          if (cnt == 0) {
                              cnt++
                             return true
                             } else {
                               return false
                             }
                          }

        1 * rs.getString(1)
        1 * rs.getString(2)
        0 * _
    }

    def 'I should be able to delete a message'() {
        given:
        Message newMessage = new Message(messageId: 123)
        PreparedStatement preparedStatement = Mock(PreparedStatement)

        when:
        messageDao.delete(newMessage)

        then:
        1 * connection.prepareStatement("Delete from message where messageId=?") >> preparedStatement
        1 * preparedStatement.setString(1,'123')
        1 * preparedStatement.execute()
        0 * _
    }

    def 'I should be able to get a list of messages by deviceId'() {
        given:
        Message m1 = new Message(messageId: '789', deviceId: '123', channel: 4, messageText: 'get me.')
        Message m2 = new Message(messageId: '789', deviceId: '123', channel: 4, messageText: 'get me.')
        Message m3 = new Message(messageId: '789', deviceId: '123', channel: 4, messageText: 'get me.')

        PreparedStatement preparedStatement = Mock(PreparedStatement)
        ResultSet rs = Mock(ResultSet)
        def cnt=0
        when:
        messageDao.getByDevice('123')

        then:
        1 * connection.prepareStatement("Select message, messageId, channel from message where deviceId=? order by channel") >> preparedStatement
        1 * preparedStatement.setString(1,'123')
        1 * preparedStatement.executeQuery()
        0 * _
    }

}
