package com.phg.minitron.model

import java.security.MessageDigest

/**
 * Created by milesporter on 2/26/17.
 */

class User {

    def email
    def passwordHash
    def userId

    def setPassword(String s) {
        passwordHash = getPasswordHash(s)
    }

    static getPasswordHash(String s) {
        MessageDigest.getInstance("MD5").digest(s.bytes).encodeHex().toString()
    }
}

