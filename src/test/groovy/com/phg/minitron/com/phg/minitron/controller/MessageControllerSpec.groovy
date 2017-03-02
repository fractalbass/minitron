package com.phg.minitron.com.phg.minitron.controller

import com.phg.minitron.MinitronApplication
import com.phg.minitron.dao.MessageDao
import com.phg.minitron.model.Message
import groovyx.net.http.ContentType
import groovyx.net.http.RESTClient
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Ignore
import spock.lang.Shared
import spock.lang.Specification
/**
 * Created by milesporter on 2/26/17.
 */

@SpringBootTest(classes = MinitronApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MessageControllerSpec extends Specification{

    @Shared
    def RESTClient restClient

    @Value('${local.server.port}')
    int port //random port chosen by spring test

    def setup() {
        MessageDao messageDao = new MessageDao()
        messageDao.clean()

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
        Message message = new Message(device: "1", channel: "1", messageText: "Test Message.")

        when:
        def resp = restClient.post(path: "/message",
                                   body: message,
                                   requestContentType: ContentType.JSON)

        then:
        resp.status == 201
        resp.responseData.str=='OK'
    }

    @Ignore
    def 'I can get a message'() {
        expect:
        false
    }
}
