package com.phg.minitron.service

import com.phg.minitron.dao.UserDao
import com.phg.minitron.exception.UserAlreadyExistsException
import com.phg.minitron.model.User
import spock.lang.Specification
/**
 * Created by milesporter on 2/26/17.
 */


class UserServiceSpec extends Specification{

    def UserService userService = new UserService()
    def UserDao userDao

    def setup() {
        userDao = Mock(UserDao)
        userService.userDao = userDao
    }

    //I can register
    def 'I can register a user'() {
        given:
        def uuid = UUID.randomUUID()
        User user = new User(email: "some@Email.com", password: "somePassword")
        userService.metaClass.getUuid = {
            uuid
        }

        when:
        def registeredUser = userService.registerUser(user)

        then:
        registeredUser.userId == uuid
        1 * userDao.checkIfEmailExists(_) >> false
        1 * userDao.save(user) >> true
        0 * _
    }

    //I can register
    def 'I cannot register a user if they already exist'() {
        given:
        def uuid = UUID.randomUUID()
        User user = new User(email: "some@Email.com", password: "somePassword")
        userService.metaClass.getUuid = {
            uuid
        }

        when:
        def registeredUser = userService.registerUser(user)

        then:
        1 * userDao.checkIfEmailExists(_) >> true
        final UserAlreadyExistsException exception = thrown()
        0 * _
    }

    //I can log in
    def 'I can authenticate a user'() {
        given:
        def uuid = UUID.randomUUID()
        User user = new User(email: "some@Email.com", password: "somePassword")
        User authenticatedUser = new User(email: "some@Email.com", password: "somePassword", userId: uuid)

        when:
        def authUser = userService.authenticateUser(user)

        then:
        authUser!=null
        1 * userDao.getUserByEmailAndPassword(_) >> authenticatedUser
        0 * _

    }

    //I reset a user password password
    def 'I can reset my password'() {
        given:
        def uuid = UUID.randomUUID()
        User unauthUser = new User(email: "some@Email.com")
        unauthUser.setPassword("somePassword")
        User user =       new User(email: "some@Email.com", userId: uuid)
        user.setPassword("somePassword")
        User updatedUser = new User(email: "some@Email.com", userId: uuid)
        updatedUser.setPassword("somePassword")
        userService.metaClass.getUuid = {
            uuid
        }

        when:
        def registeredUser = userService.resetPassword(user, "newPassword")

        then:
        registeredUser.userId == uuid
        registeredUser.getPasswordHash() == User.getPasswordHash("newPassword")
        1 * userDao.getUserByEmail(_) >> user
        1 * userDao.update(user) >> updatedUser
        0 * _
    }

}
