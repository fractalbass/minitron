package com.phg.minitron.service

import com.phg.minitron.dao.MessageDao
import com.phg.minitron.model.Message
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
 * Created by milesporter on 2/24/17.
 */
@Component
class MessageService {

    @Autowired
    MessageDao messageDao

    def createOrUpdate(Message message) {
        def result = create(message)
        if (!result) {
            result = update(message)
        }
        result
    }

    def update(Message message){
        messageDao.update(message)
    }

    def create(Message message){
        String messageId = UUID.randomUUID().toString()
        message.setMessageId(messageId)
        messageDao.save(message)
        message
    }

    def getByDeviceAndChannel(Message message) {
        messageDao.getByDeviceAndChannel(message)
    }

    def delete(Message message) {
        messageDao.delete(message)
    }

    def getAllByDeviceId(UUID deviceId) {
        messageDao.getByDevice(deviceId.toString())
    }


    def getByMessageId(UUID messageId) {
        messageDao.getByMessageId(messageId.toString())
    }
}

