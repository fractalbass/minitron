package com.phg.minitron.controller

import com.phg.minitron.model.Device
import com.phg.minitron.model.Message
import com.phg.minitron.model.User
import com.phg.minitron.service.DeviceService
import com.phg.minitron.service.MessageService
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

    @Autowired
    DeviceService deviceService

    @Autowired
    MessageService messageService

    @Value('${admin.passwd}')
    String admin_passwd

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String getMessage(@RequestParam String email, @RequestParam String passwd, Model model) {
        log.info("Login attempt for ${email}")
        if (email.equals("superuser") && passwd.equals(admin_passwd)) {
            ArrayList<User> allUsers = userService.getAllUsers()
            model.addAttribute("userList", allUsers)
            return "admin"
        } else {
            User user = new User(email: email, password: passwd)
            //user.setEmail(email)
            //emailuser.setPassword(passwd)
            User authUser = userService.authenticateUser(user)
            model.addAttribute("user",authUser)
            if (authUser!=null) {
                ArrayList<Device> userDeviceList = deviceService.getDevicesForUser(UUID.fromString(authUser.userId))
                model.addAttribute("deviceList", userDeviceList)
                return 'main'
            } else {
                return 'relogin'
            }
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

    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public String signup(@RequestParam String email, @RequestParam String passwd, Model model) {
        log.info("Attempting to add user ${email}, ${passwd}")
        User  newUser = new User();
        newUser.setEmail(email)
        newUser.setPassword(passwd)
        userService.registerUser(newUser)
        model.addAttribute("user",newUser)
        return "main"
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

    @RequestMapping(value = "/addDevice", method = RequestMethod.POST)
    public String deleteUser(@RequestParam String deviceName, @RequestParam UUID userId, Model model) {
        log.info("Attempting to create a device with name ${deviceName}")
        deviceService.createDeviceForUser(deviceName, userId)
        ArrayList<Device> userDeviceList = deviceService.getDevicesForUser(userId)
        model.addAttribute("deviceList", userDeviceList)
        model.addAttribute("user", userService.getUserByUserId(userId.toString()))
        return "main"
    }

    @RequestMapping(value = "/deviceMessages", method = RequestMethod.POST)
    public String getMessages(@RequestParam UUID deviceId, @RequestParam UUID userId, Model model) {
        log.info("Attempting to get message for device ${deviceId}")
        User  user = userService.getUserByUserId(userId.toString());
        model.addAttribute("user",user)
        Device device = deviceService.getDeviceById(deviceId)
        def messages = messageService.getAllByDeviceId(deviceId)
        model.addAttribute("messageList", messages)
        model.addAttribute("device", device)
        return "messages"
    }

    @RequestMapping(value = "/updateMessage", method = RequestMethod.POST)
    public String updateMessage(@RequestParam UUID messageId, @RequestParam String messageText, Model model) {
        log.info("Attempting to update message text ${messageText} for message ${messageId}")
        Message message = messageService.getByMessageId(messageId)
        Device device = deviceService.getDeviceById(UUID.fromString(message.deviceId))
        User  user = userService.getUserByUserId(device.userId);
        model.addAttribute("user",user)
        model.addAttribute("device", device)
        message.messageText=messageText
        messageService.update(message)
        def messages = messageService.getAllByDeviceId(UUID.fromString(device.deviceId))
        model.addAttribute("messageList", messages)
        model.addAttribute("device", device)
        return "messages"
    }

    @RequestMapping(value = "/deleteDevice", method = RequestMethod.POST)
    public String deleteDevice(@RequestParam UUID deviceId, @RequestParam UUID userId, Model model) {
        log.info("Attempting to delete a device with ID ${deviceId}")
        deviceService.deleteDevice(deviceId)



        ArrayList<Device> userDeviceList = deviceService.getDevicesForUser(userId)
        model.addAttribute("deviceList", userDeviceList)
        model.addAttribute("user", userService.getUserByUserId(userId.toString()))
        return "main"
    }


}