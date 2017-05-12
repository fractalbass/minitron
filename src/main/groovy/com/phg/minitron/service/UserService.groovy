package com.phg.minitron.service

import com.phg.minitron.dao.UserDao
import com.phg.minitron.model.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
/**
 * Created by milesporter on 2/26/17.
 */
@Component
class UserService {

    @Autowired
    UserDao userDao

    def registerUser(User user) {

        user.userId=getUuid()
        if (userDao.save(user)) {
            return user
        } else {
            return null
        }
    }

    def getUuid() {
        def uuid = UUID.randomUUID()
        uuid
    }

    def authenticateUser(User user) {
        User authUser = userDao.getUserByEmailAndPassword(user)
        authUser
    }

    def resetPassword(User user, String newPassword) {
        User u = userDao.getUserByEmail(user)
        u.setPassword(newPassword)
        userDao.update(u)
        u
    }

    def getAllUsers() {
        ArrayList<User> allUsers = userDao.getAllUsers()
        return allUsers
    }


}
