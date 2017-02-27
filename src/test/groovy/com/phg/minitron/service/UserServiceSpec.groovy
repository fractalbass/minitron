package com.phg.minitron.service

import com.phg.minitron.dao.UserDao
import com.phg.minitron.model.User
import spock.lang.Ignore
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
        1 * userDao.save(user) >> true
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
        1 * userDao.getUser(user) >> authenticatedUser
        0 * _

    }

    //I reset a user password password
    def 'I can reset my password'() {
        given:
        def uuid = UUID.randomUUID()
        User unauthUser = new User(email: "some@Email.com", password: "somePassword")
        User user =       new User(email: "some@Email.com", password: "somePassword", userId: uuid)
        User updatedUser = new User(email: "some@Email.com", password: "somePassword", userId: uuid)
        userService.metaClass.getUuid = {
            uuid
        }

        when:
        def registeredUser = userService.resetPassword(user, "newPassword")

        then:
        registeredUser.userId == uuid
        registeredUser.password == "newPassword"
        1 * userDao.getUser(_) >> user
        1 * userDao.update(user) >> updatedUser
        0 * _
    }

}
