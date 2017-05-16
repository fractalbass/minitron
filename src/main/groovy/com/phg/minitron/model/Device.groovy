package com.phg.minitron.model

import org.apache.commons.lang.RandomStringUtils

/**
 * Created by milesporter on 2/27/17.
 */
class Device {

    String deviceName
    String deviceCode
    String deviceId
    String userId

    static generateCode() {
        String charset = (('A'..'Z') + ('0'..'9')).join()
        Integer length = 9
        String randomString = RandomStringUtils.random(length, charset.toCharArray())
        randomString
    }

}
