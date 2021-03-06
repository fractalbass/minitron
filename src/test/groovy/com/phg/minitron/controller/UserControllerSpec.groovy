package com.phg.minitron.controller

import com.phg.minitron.MinitronApplication
import com.phg.minitron.dao.UserDao
import com.phg.minitron.integration.DatabaseUtil
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
@Ignore
@SpringBootTest(classes = MinitronApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerSpec extends Specification {

    @Shared
    def RESTClient restClient

    @Value('${local.server.port}')
    int port //random port chosen by spring test

    def setupSpec() {
        DatabaseUtil dbUtil = new DatabaseUtil()
        dbUtil.createDatabase()
    }

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

    def 'I can authenticate a user.'() {
        given:
        restClient != null

        when:
        //  Create the user
        def resp = restClient.post(path: "user/someuser@email.com",
                                    body: "somePassword",
                                    requestContentType: ContentType.JSON)
        def user = resp.responseData

        //Attempt to authenticate
        def authResp = restClient.post(path: "/user/authenticate/someuser@email.com",
                                        body: "somePassword",
                                        requestContentType: ContentType.JSON)

        then:
        resp.responseData.userId == authResp.responseData.userId
    }
}