package com.phg.minitron.controller

import com.phg.minitron.MinitronApplication
import com.phg.minitron.dao.UserDao
import groovyx.net.http.ContentType
import groovyx.net.http.RESTClient
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Ignore
import spock.lang.Shared
import spock.lang.Specification
/**
 * Created by milesporter on 3/2/17.
 */
//@DockerDependent
@SpringBootTest(classes = MinitronApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerSpec extends Specification {

    @Shared
    def RESTClient restClient

    @Value('${local.server.port}')
    int port //random port chosen by spring test

    def setup() {
        UserDao userDao = new UserDao()
        userDao.clean()

        restClient = new RESTClient("http://localhost:${port}/")
        restClient.handler.failure = { resp -> resp.status }
    }

    def 'I can hit the device controller health endpoint'() {
        given:
        restClient != null

        when:
        def resp = restClient.get(path: 'user/health')

        then:
        resp.status == 200
        resp.responseData.str == 'OK'
    }

    def 'I can save a user.'() {
        given:
        restClient != null

        when:
        def resp = restClient.post(path: 'user/mporter@paintedharmony.com',
                                   body: 'thisIsAPassword',
                                   requestContentType: ContentType.JSON)

        then:
        resp.status == 201

    }

    @Ignore
    def 'I can update a user.'() {
        expect:
        false

    }
}