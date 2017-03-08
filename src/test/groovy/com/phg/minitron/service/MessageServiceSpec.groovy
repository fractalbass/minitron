package com.phg.minitron.service

import com.phg.minitron.dao.MessageDao
import com.phg.minitron.model.Message
import spock.lang.Specification

/**
 * Created by milesporter on 2/24/17.
 */
class MessageServiceSpec extends Specification{

    MessageService messageService
    MessageDao messageDao

    def setup() {
        messageDao = Mock(MessageDao)
        messageService = new MessageService()
        messageService.messageDao = messageDao
    }

    def 'I should be able to run a test'() {
        expect:
        true
    }

    def 'I should be able to create a message'() {
        given:
        Message message = new Message(deviceId: '1', channel: 1, messageText: "Blahblahblah")
        when:
        messageService.create(message)

        then:
        1 * messageDao.save(message)
    }

    def 'I should be able to update a message'() {
        given:
        Message message = new Message(deviceId: 1, channel: 1, messageText: "Blahblahblah")
        when:
        messageService.update(message)

        then:
        1 * messageDao.update(message)
    }

    def 'I should be able to create or update a message when the message does not exist'() {
        given:
        Message message = new Message(deviceId: 1, channel: 1, messageText: "Blahblahblah")
        when:
        messageService.createOrUpdate(message)

        then:
        1 * messageDao.save(message) >> true
        0 * _
    }

    def 'I should be able to create a new message.'() {
        given:
        Message message = new Message(deviceId: 1, channel: 1, messageText: "Blahblahblah")
        when:
        def newMessage = messageService.create(message)

        then:
        newMessage.getMessageId()!=null
        1 * messageDao.save(message) >> true
        0 * _
    }

    def 'I should be able to update a new message.'() {
        given:
        String messageId = UUID.randomUUID().toString()
        Message message = new Message(messageId: messageId, deviceId: 1, channel: 1, messageText: "Blahblahblah")
        when:
        def newMessage = messageService.update(message)

        then:
        1 * messageDao.update(message) >> true
        0 * _
    }


    def 'I should be able to get a message'() {
        given:
        Message message = new Message(deviceId: 1, channel: 1, messageText: "Blahblahblah")
        Message lookup = new Message(deviceId: 1, channel: 1)
        when:
        Message message1 = messageService.getByDeviceAndChannel(lookup)

        then:
        1 * messageDao.getByDeviceAndChannel(lookup) >> message
        message1.equals(message)
    }

    def 'I should be able to delete a message'() {
        given:
        Message lookup = new Message(deviceId: 1, channel: 1)

        when:
        boolean result = messageService.delete(lookup)

        then:
        1 * messageDao.delete(lookup) >> true
        result
    }
}
