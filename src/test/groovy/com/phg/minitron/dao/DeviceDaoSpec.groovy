package com.phg.minitron.dao

import com.phg.minitron.model.Device
import spock.lang.Specification

import java.sql.Connection
import java.sql.PreparedStatement
/**
 * Created by milesporter on 2/26/17.
 */
class DeviceDaoSpec extends Specification{

    DeviceDao deviceDao
    Connection connection

    def setup() {
        deviceDao = new DeviceDao()
        connection = Mock(Connection)
        deviceDao.conn = connection
    }

    def 'I should be able to save a device'() {
        given:
        UUID deviceId = UUID.randomUUID()
        UUID userId = UUID.randomUUID()
        Device device = new Device(userId: userId, deviceCode: "xyz123", deviceId: deviceId)
        PreparedStatement preparedStatement = Mock(PreparedStatement)

        when:
        deviceDao.save(device)

        then:
        1 * connection.prepareStatement("insert into device (deviceCode, deviceId, userId) values (?,?,?)") >> preparedStatement
        1 * preparedStatement.setString(1,"xyz123")
        1 * preparedStatement.setString(2, deviceId.toString())
        1 * preparedStatement.setString(3, userId.toString())
        1 * preparedStatement.execute()
        0 * _
    }

    def 'I should be able to update a device'() {
        given:
        UUID deviceId = UUID.randomUUID()
        UUID userId = UUID.randomUUID()
        Device device = new Device(userId: userId, deviceCode: "xyz123", deviceId: deviceId)
        PreparedStatement preparedStatement = Mock(PreparedStatement)

        when:
        deviceDao.update(device)

        then:
        1 * connection.prepareStatement("update device set deviceCode=?, userId=? where deviceId=?") >> preparedStatement
        1 * preparedStatement.setString(1,"xyz123")
        1 * preparedStatement.setString(3, deviceId.toString())
        1 * preparedStatement.setString(2, userId.toString())
        1 * preparedStatement.execute()
        0 * _
    }

    def 'I should be able to get all devices for a user'() {
        expect:
        false
    }

    def 'I should be able to get all devices'() {
        expect:
        false
    }

    def 'I should be able to get all non-associated devices'() {
        expect:
        false
    }


}
