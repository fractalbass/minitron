package com.phg.minitron.controller

import com.phg.minitron.MinitronApplication
import com.phg.minitron.dao.MessageDao
import com.phg.minitron.integration.DatabaseUtil
import com.phg.minitron.integration.DockerDependent
import com.phg.minitron.model.Message
import groovyx.net.http.ContentType
import groovyx.net.http.RESTClient
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Shared
import spock.lang.Specification
/**
 * Created by milesporter on 2/26/17.
 */
@DockerDependent
@SpringBootTest(classes = MinitronApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MessageControllerSpec extends Specification{

    @Shared
    def RESTClient restClient

    @Value('${local.server.port}')
    int port //random port chosen by spring test

    def setupSpec() {
        DatabaseUtil dbUtil = new DatabaseUtil()
        dbUtil.createDatabase()
    }

    def setup() {

        restClient = new RESTClient("http://localhost:${port}/message/")
        restClient.handler.failure = { resp -> resp.status }
    }

    def 'I can hit the message controller health endpoint'() {
        given:
        restClient!=null

        when:
        def resp = restClient.get(path: 'health')

        then:
        resp.status == 200
        resp.responseData.str=='OK'
    }

    def 'I can save a message.'() {
        given:
        restClient!=null
        Message message = new Message(deviceId: '2', channel: 1, messageText: "Test Message.")

        when:
        def resp = restClient.post(path: "/message",
                                   body: message,
                                   requestContentType: ContentType.JSON)

        then:
        resp.status == 201
        resp.responseData.deviceId == '2'
        resp.responseData.channel == 1
        resp.responseData.messageText=="Test Message."
        resp.responseData.messageId!=null
    }

    def 'I can update a message.'() {
        given:
        restClient!=null
        Message message = new Message(deviceId: '2', channel: 1, messageText: "New Test Message.")
        def respOriginal = restClient.post(path: "/message",
                body: message,
                requestContentType: ContentType.JSON)

        //  Need the messageId coming back from the commit.
        message = respOriginal.responseData

        when:
        message.messageText = "Updated message text."
        def resp = restClient.put(path: "/message",
                body: message,
                requestContentType: ContentType.JSON)

        then:
        resp.status == 201
        and:
        MessageDao messageDao = new MessageDao()
        Message newMessage = new Message(deviceId: '2', channel: 1)
        def updatedMessage = messageDao.getByDeviceAndChannel(newMessage)
        updatedMessage.messageText == "Updated message text."

    }



    def 'I can get a message'() {
        given:
        restClient!=null
        MessageDao messageDao = new MessageDao()
        Message message = new Message(deviceId: '2', channel: 3, messageText: "Saved message test.")
        restClient.post(path: "/message",
                body: message,
                requestContentType: ContentType.JSON)


        when:
        def resp = restClient.get(path: "/message/2/3",
                requestContentType: ContentType.JSON)

        then:
        resp.status == 200
        resp.responseData.str=='Saved message test.'
    }
}
