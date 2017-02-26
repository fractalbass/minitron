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
            update(message)
        }
    }

    def update(Message message){
        messageDao.update(message)
    }

    def create(Message message){
        messageDao.save(message)
    }

    def get(Message message) {
        messageDao.get(message)
    }

    def delete(Message message) {
        messageDao.delete(message)
    }
}
