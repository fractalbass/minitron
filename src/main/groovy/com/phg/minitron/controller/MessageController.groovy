package com.phg.minitron.controller

import com.phg.minitron.service.MessageService
import com.phg.minitron.model.Message
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

/**
 * Created by milesporter on 2/24/17.
 */
@RestController
class MessageController {

    @Autowired
    MessageService messageService

    @RequestMapping(value = "/message/{device}/{channel}", method = RequestMethod.GET)
    public String getMessage(@PathVariable int device, @PathVariable int channel) {
        Message msg = new Message(device: device, channel: channel)
        return messageService.get(msg)
    }

    @RequestMapping(value = "/message/{device}/{channel}", method = RequestMethod.POST)
    public ResponseEntity<String> getMessage(@PathVariable int device, @PathVariable int channel, @RequestBody String message) {
        boolean result = messageService.createOrUpdate(new Message(device: device, channel: channel, message: message))
        if (result) {
            return "OK"
        } else {
            return "FAILED"

        }
    }

    @RequestMapping(value = "/message/health", method = RequestMethod.GET)
    public ResponseEntity<String> getMessage() {
        return new ResponseEntity<String>("OK", HttpStatus.OK);
    }

}