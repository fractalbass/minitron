package com.phg.minitron.dao

import com.phg.minitron.model.User
import org.springframework.stereotype.Component

/**
 * Created by milesporter on 2/26/17.
 */
@Component
class UserDao {

    boolean save(User user) {
        return false
    }

    User getUserByEmailAndPassword(String email, String password) {
       null
    }

    User update(User user) {
        null
    }
}
