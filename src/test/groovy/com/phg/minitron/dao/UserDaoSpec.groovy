package com.phg.minitron.dao

import com.phg.minitron.model.Message
import com.phg.minitron.model.User
import spock.lang.Ignore
import spock.lang.Specification

import java.sql.Connection
import java.sql.PreparedStatement

/**
 * Created by milesporter on 2/26/17.
 */
class UserDaoSpec extends Specification{

    UserDao userDao
    Connection connection

    def setup() {
        userDao = new UserDao()
        connection = Mock(Connection)
        userDao.conn = connection

    }

    def 'I can save a user'() {
        given:
        UUID userId = UUID.randomUUID()
        User user = new User(userId: userId, email: "some@email.com", password: "password")
        PreparedStatement preparedStatement = Mock(PreparedStatement)

        when:
        userDao.save(user)

        then:
        1 * connection.prepareStatement("insert into user (userId, email, password) values (?,?,?)") >> preparedStatement
        1 * preparedStatement.setString(1,userId.toString())
        1 * preparedStatement.setString(2,"some@email.com")
        1 * preparedStatement.setString(3, "password")
        1 * preparedStatement.execute()
        0 * _
    }


    def 'I can get a user by email and password'() {
        given:
        User user = new User(email: "some@email.com", password: "somepassword")
        PreparedStatement preparedStatement = Mock(PreparedStatement)


        when:
        userDao.getUser(user)
        then:
        1 * connection.prepareStatement("Select userId from user where email=? and password=?") >> preparedStatement
        1 * preparedStatement.setString(1, "some@email.com")
        1 * preparedStatement.setString(2, "somepassword")
        1 * preparedStatement.executeQuery()
        0 * _

    }


    def 'I can update a user'() {
        given:
        User user = new User(userId: "1", email: "some@email.com", password: "somepassword")
        PreparedStatement preparedStatement = Mock(PreparedStatement)


        when:
        userDao.update(user)
        then:
        1 * connection.prepareStatement("Update user set email=?, password=? where userId=?") >> preparedStatement
        1 * preparedStatement.setString(3, "1")
        1 * preparedStatement.setString(1, "some@email.com")
        1 * preparedStatement.setString(2, "somepassword")
        1 * preparedStatement.execute()
        0 * _

    }


}
