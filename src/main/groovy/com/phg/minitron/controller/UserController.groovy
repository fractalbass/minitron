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

    @RequestMapping(value = "/user/authenticate/{email:.+}", method = RequestMethod.POST)
    public ResponseEntity<User> getMessage(@PathVariable String email, @RequestBody String password) {
        User user = new User(email: email)
        user.setPassword(password)

        def authUser = userService.authenticateUser(user)
        if (authUser) {
            return new ResponseEntity < User > (authUser, HttpStatus.OK )
        } else {
            return new ResponseEntity < User > (authUser, HttpStatus.NOT_FOUND )
        }
    }

    @RequestMapping(value = "/user/{email:.+}", method = RequestMethod.POST)
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
