package com.phg.minitron.controller

import com.phg.minitron.model.User
import com.phg.minitron.service.UserService
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
/**
 * Created by milesporter on 5/11/17.
 */
@Slf4j
@Controller
public class WebController {

    @Autowired
    UserService userService;

    @Value('${admin.passwd}')
    String admin_passwd

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String getMessage(@RequestParam String username, @RequestParam String passwd, Model model) {
        log.info("Login attempt with ${username}, ${passwd}")
        if (username.equals("superuser") && passwd.equals(admin_passwd)) {
            ArrayList<User> allUsers = userService.getAllUsers()
            model.addAttribute("userList", allUsers)
            return "admin"
        } else {
            return "main"
        }
    }

    @RequestMapping(value = "/addUser", method = RequestMethod.POST)
    public String addUser(@RequestParam String email, @RequestParam String passwd, Model model) {
        log.info("Attempting to add user ${email}, ${passwd}")
        User  newUser = new User();
        newUser.setEmail(email)
        newUser.setPassword(passwd)
        userService.registerUser(newUser)
        ArrayList<User> allUsers = userService.getAllUsers()
        model.addAttribute("userList", allUsers)
        return "admin"
    }

    @RequestMapping(value = "/passwdReset", method = RequestMethod.POST)
    public String passwdReset(@RequestParam String userId, @RequestParam String passwd, Model model) {
        log.info("Attempting to reset password for user ${userId}")
        User  user = userService.getUserByUserId(userId);
        userService.resetPassword(user, passwd)
        ArrayList<User> allUsers = userService.getAllUsers()
        model.addAttribute("userList", allUsers)
        return "admin"
    }

    @RequestMapping(value = "/deleteUser", method = RequestMethod.POST)
    public String deleteUser(@RequestParam String userId, Model model) {
        log.info("Attempting to delete user ${userId}")
        userService.deleteUserByUserId(userId)
        ArrayList<User> allUsers = userService.getAllUsers()
        model.addAttribute("userList", allUsers)
        return "admin"
    }



}