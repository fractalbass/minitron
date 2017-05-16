package com.phg.minitron.exception

/**
 * Created by milesporter on 5/16/17.
 */
class UserAlreadyExistsException extends Exception {
    UserAlreadyExistsException(String message) {
        super(message)
    }
}
