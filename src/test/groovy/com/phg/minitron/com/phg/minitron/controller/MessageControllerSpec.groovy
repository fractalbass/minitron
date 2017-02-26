package com.phg.minitron.com.phg.minitron.controller

import com.phg.minitron.MinitronApplication
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestPropertySource
import spock.lang.Ignore
import spock.lang.Shared
import spock.lang.Specification
import groovyx.net.http.RESTClient

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
        restClient = new RESTClient("http://localhost:${port}/message/")
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
}
