package com.phg.minitron.dao

import com.phg.minitron.model.User
import spock.lang.Specification

import java.sql.Connection
import java.sql.PreparedStatement
/**
 * Created by milesporter on 2/26/17.
 */
class UserDaoSpec extends Specification {

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
        User user = new User(userId: userId, email: "some@email.com");
        user.setPassword("password")
        def hash = user.getPasswordHash()
        PreparedStatement preparedStatement = Mock(PreparedStatement)

        when:
        userDao.save(user)

        then:
        1 * connection.prepareStatement("insert into mtuser (userId, email, password) values (?,?,?)") >> preparedStatement
        1 * preparedStatement.setString(1, userId.toString())
        1 * preparedStatement.setString(2, "some@email.com")
        1 * preparedStatement.setString(3, hash)
        1 * preparedStatement.execute()
        0 * _
    }

    def 'I can see if a user email already exists'() {
        given:
        PreparedStatement preparedStatement = Mock(PreparedStatement)

        when:
        userDao.checkIfEmailExists("email@test.com")

        then:
        1 * connection.prepareStatement("Select count(email) from mtuser where email=?") >> preparedStatement
        1 * preparedStatement.setString(1, "email@test.com");
        1 * preparedStatement.executeQuery()
        0 * _
    }


    def 'I can get a user by email and password'() {
        given:
        User user = new User(email: "some@email.com")
        user.setPassword("somePassword")
        PreparedStatement preparedStatement = Mock(PreparedStatement)


        when:
        userDao.getUserByEmailAndPassword(user)
        then:
        1 * connection.prepareStatement("Select userId from mtuser where email=? and password=?") >> preparedStatement
        1 * preparedStatement.setString(1, "some@email.com")
        1 * preparedStatement.setString(2, user.getPasswordHash())
        1 * preparedStatement.executeQuery()
        0 * _

    }


    def 'I can update a user'() {
        given:
        User user = new User(userId: "1", email: "some@email.com")
        user.setPassword("someOtherPassword")
        PreparedStatement preparedStatement = Mock(PreparedStatement)


        when:
        userDao.update(user)
        then:
        1 * connection.prepareStatement("Update mtuser set email=?, password=? where userId=?") >> preparedStatement
        1 * preparedStatement.setString(3, "1")
        1 * preparedStatement.setString(1, "some@email.com")
        1 * preparedStatement.setString(2, user.getPasswordHash())
        1 * preparedStatement.execute()
        0 * _

    }

    def 'I can get a list of all users'() {
        given:
        PreparedStatement preparedStatement = Mock(PreparedStatement)


        when:
        userDao.getAllUsers()
        then:
        1 * connection.prepareStatement("Select userId, email from mtuser order by userId") >> preparedStatement
        1 * preparedStatement.executeQuery()
        0 * _


    }
}