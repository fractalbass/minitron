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

    @Ignore
    def 'I can get a user by email and password'() {
        expect:
        false
    }

    @Ignore
    def 'I can update a user'() {
        expect:
        false
    }


}
