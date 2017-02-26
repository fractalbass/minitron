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
        Message newMessage = new Message(device: 1, channel: 1, messageText: 'I do not care.')
        PreparedStatement preparedStatement = Mock(PreparedStatement)

        when:
        messageDao.save(newMessage)

        then:
        1 * connection.prepareStatement("insert into message (device, channel, message) values (?,?,?)") >> preparedStatement
        1 * preparedStatement.setInt(1,1)
        1 * preparedStatement.setInt(2,1)
        1 * preparedStatement.setString(3, 'I do not care.')
        1 * preparedStatement.execute()
        0 * _
    }

    def 'I should be able to update an existing message'() {
            given:
            Message newMessage = new Message(device: 4, channel: 5, messageText: 'Update me.')
            PreparedStatement preparedStatement = Mock(PreparedStatement)

            when:
            messageDao.update(newMessage)

            then:
            1 * connection.prepareStatement("update message set message=? where device=? and channel=?") >> preparedStatement
            1 * preparedStatement.setString(1,'Update me.')
            1 * preparedStatement.setInt(2,4)
            1 * preparedStatement.setInt(3,5)
            1 * preparedStatement.execute()
            0 * _
    }

    def 'I should be able to get a message'() {
        given:
        Message newMessage = new Message(device: 123, channel: 4, messageText: 'get me.')
        PreparedStatement preparedStatement = Mock(PreparedStatement)
        ResultSet rs = Mock(ResultSet)
        def cnt=0
        when:
        messageDao.get(newMessage)

        then:

        1 * connection.prepareStatement("Select message from message where device=? and channel=?") >> preparedStatement
        1 * preparedStatement.setInt(1,123)
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
        0 * _
    }

    def 'I should be able to delete a message'() {
        given:
        Message newMessage = new Message(device: 123, channel: 4, messageText: 'Delete me.')
        PreparedStatement preparedStatement = Mock(PreparedStatement)

        when:
        messageDao.delete(newMessage)

        then:
        1 * connection.prepareStatement("Delete from message where device=? and channel=?") >> preparedStatement
        1 * preparedStatement.setInt(1,123)
        1 * preparedStatement.setInt(2,4)
        1 * preparedStatement.execute()
        0 * _
    }

}
