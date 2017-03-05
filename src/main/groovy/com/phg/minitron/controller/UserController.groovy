package com.phg.minitron.controller

import com.phg.minitron.model.User
import com.phg.minitron.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
/**
 * Created by milesporter on 3/2/17.
 */
@RestController
class UserController {

    @Autowired
    UserService userService

    @RequestMapping(value = "/user/authenticate/{userId}", method = RequestMethod.POST)
    public ResponseEntity<Boolean> getMessage(@PathVariable String userId, @RequestBody String password) {
        def result = new ResponseEntity<Boolean>(Boolean.FALSE, HttpStatus.NOT_FOUND)
        User user = new User(userId: userId, password: password)

        if (userService.authenticateUser(user)) {
            result =new  ResponseEntity<Boolean>(Boolean.TRUE, HttpStatus.OK)
        }
        result
    }

    @RequestMapping(value = "/user/{email}", method = RequestMethod.POST)
    public ResponseEntity<User> createUser(@PathVariable String email, @RequestBody String password) {
        def result = new ResponseEntity<User>(null, HttpStatus.INTERNAL_SERVER_ERROR)
        User user = new User(email: email, password: password)
        user = userService.registerUser(user)
        if (user) {
            result = new  ResponseEntity<User>(user, HttpStatus.CREATED)
        }
        result
    }

    @RequestMapping(value = "/user/health", method = RequestMethod.GET)
    public ResponseEntity<String> getHealth() {
        return new ResponseEntity<String>("OK", HttpStatus.OK)
    }

}
