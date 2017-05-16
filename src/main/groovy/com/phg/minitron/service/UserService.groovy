package com.phg.minitron.service

import com.phg.minitron.dao.UserDao
import com.phg.minitron.exception.UserAlreadyExistsException
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

    def registerUser(User user) throws UserAlreadyExistsException {

        if (userDao.checkIfEmailExists(user.email)) {
            throw new UserAlreadyExistsException("User ${user.email} already exists.")
        }

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
        User resetUser = userDao.getUserByEmail(user)
        resetUser.setPassword(newPassword)
        userDao.update(resetUser)
        resetUser
    }

    def getAllUsers() {
        ArrayList<User> allUsers = userDao.getAllUsers()
        allUsers
    }

    def getUserByUserId(String userId) {
        User user = userDao.getUserByUserId(userId)
        user
    }

    def deleteUserByUserId(String userId) {
        boolean result = userDao.deleteUserById(userId)
        return result
    }


}
