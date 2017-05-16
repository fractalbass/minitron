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
        Device device = new Device(userId: userId, deviceCode: "xyz123", deviceName: "somename", deviceId: deviceId)
        PreparedStatement preparedStatement = Mock(PreparedStatement)

        when:
        deviceDao.save(device)

        then:
        1 * connection.prepareStatement("insert into device (deviceCode, deviceId, deviceName, userId) values (?,?,?,?)") >> preparedStatement
        1 * preparedStatement.setString(1,"xyz123")
        1 * preparedStatement.setString(2, deviceId.toString())
        1 * preparedStatement.setString(3, "somename")
        1 * preparedStatement.setString(4, userId.toString())
        1 * preparedStatement.execute()
        0 * _
    }

    def 'I should be able to update a device'() {
        given:
        UUID deviceId = UUID.randomUUID()
        UUID userId = UUID.randomUUID()
        Device device = new Device(userId: userId, deviceCode: "xyz123", deviceId: deviceId, deviceName: "somename")
        PreparedStatement preparedStatement = Mock(PreparedStatement)

        when:
        deviceDao.update(device)

        then:
        1 * connection.prepareStatement("update device set deviceCode=?, userId=?, deviceName=? where deviceId=?") >> preparedStatement
        1 * preparedStatement.setString(1,"xyz123")
        1 * preparedStatement.setString(2, userId.toString())
        1 * preparedStatement.setString(3, "somename")
        1 * preparedStatement.setString(4, deviceId.toString())
        1 * preparedStatement.execute()
        0 * _
    }

    def 'I should be able to get all devices for a user'() {
        given:
        UUID deviceId1 = UUID.randomUUID()
        UUID deviceId2 = UUID.randomUUID()
        UUID deviceId3 = UUID.randomUUID()
        UUID userId = UUID.randomUUID()
        Device device1 = new Device(userId: userId, deviceCode: "xyz121", deviceName: "name1", deviceId: deviceId1)
        Device device2 = new Device(userId: userId, deviceCode: "xyz122", deviceName: "name2", deviceId: deviceId2)
        Device device3 = new Device(userId: userId, deviceCode: "xyz123", deviceName: "name3", deviceId: deviceId3)

        PreparedStatement preparedStatement1 = Mock(PreparedStatement)
        PreparedStatement preparedStatement2 = Mock(PreparedStatement)
        when:
        deviceDao.save(device1)
        deviceDao.save(device2)
        deviceDao.save(device3)
        deviceDao.getDevicesByUserId(userId)


        then:
        3 * connection.prepareStatement("insert into device (deviceCode, deviceId, deviceName, userId) values (?,?,?,?)") >> preparedStatement1
        1 * connection.prepareStatement("select deviceCode, deviceId, deviceName from device where userId=?") >> preparedStatement2
        1 * preparedStatement1.setString(1,device1.deviceCode)
        1 * preparedStatement1.setString(1,device2.deviceCode)
        1 * preparedStatement1.setString(1,device3.deviceCode)
        1 * preparedStatement1.setString(2,device1.deviceId)
        1 * preparedStatement1.setString(2,device2.deviceId)
        1 * preparedStatement1.setString(2,device3.deviceId)
        1 * preparedStatement1.setString(3,device1.deviceName)
        1 * preparedStatement1.setString(3,device2.deviceName)
        1 * preparedStatement1.setString(3,device3.deviceName)
        3 * preparedStatement1.setString(4,userId.toString())
        3 * preparedStatement1.execute()
        1 * preparedStatement2.setString(1, userId.toString())
        1 * preparedStatement2.executeQuery()
        0 * _
    }

    def 'I should be able to get all devices'() {
        given:
        UUID deviceId1 = UUID.randomUUID()
        UUID deviceId2 = UUID.randomUUID()
        UUID deviceId3 = UUID.randomUUID()
        UUID userId = UUID.randomUUID()
        Device device1 = new Device(userId: userId, deviceCode: "xyz121", deviceName: "name1", deviceId: deviceId1)
        Device device2 = new Device(userId: userId, deviceCode: "xyz122", deviceName: "name2", deviceId: deviceId2)
        Device device3 = new Device(userId: userId, deviceCode: "xyz123", deviceName: "name3", deviceId: deviceId3)

        PreparedStatement preparedStatement1 = Mock(PreparedStatement)
        PreparedStatement preparedStatement2 = Mock(PreparedStatement)
        when:
        deviceDao.save(device1)
        deviceDao.save(device2)
        deviceDao.save(device3)
        deviceDao.getAllDevices()


        then:
        3 * connection.prepareStatement("insert into device (deviceCode, deviceId, deviceName, userId) values (?,?,?,?)") >> preparedStatement1
        1 * connection.prepareStatement("select deviceId, deviceCode, deviceName, userId from device") >> preparedStatement2
        1 * preparedStatement1.setString(1,device1.deviceCode)
        1 * preparedStatement1.setString(1,device2.deviceCode)
        1 * preparedStatement1.setString(1,device3.deviceCode)
        1 * preparedStatement1.setString(2,device1.deviceId)
        1 * preparedStatement1.setString(2,device2.deviceId)
        1 * preparedStatement1.setString(2,device3.deviceId)
        1 * preparedStatement1.setString(3,device1.deviceName)
        1 * preparedStatement1.setString(3,device2.deviceName)
        1 * preparedStatement1.setString(3,device3.deviceName)
        3 * preparedStatement1.setString(4,userId.toString())
        3 * preparedStatement1.execute()
        1 * preparedStatement2.executeQuery()
        0 * _
    }


    def 'I should be able to get all non-associated devices'() {
        given:
        UUID deviceId1 = UUID.randomUUID()
        UUID deviceId2 = UUID.randomUUID()
        UUID deviceId3 = UUID.randomUUID()
        UUID userId = UUID.randomUUID()
        Device device1 = new Device(userId: userId, deviceCode: "xyz121", deviceId: deviceId1, deviceName: 'name1')
        Device device2 = new Device(userId: userId, deviceCode: "xyz122", deviceId: deviceId2, deviceName: 'name2')
        Device device3 = new Device(userId: userId, deviceCode: "xyz123", deviceId: deviceId3, deviceName: 'name3')

        PreparedStatement preparedStatement1 = Mock(PreparedStatement)
        PreparedStatement preparedStatement2 = Mock(PreparedStatement)
        when:
        deviceDao.save(device1)
        deviceDao.save(device2)
        deviceDao.save(device3)
        deviceDao.getAllNonAssociatedDevices()


        then:
        3 * connection.prepareStatement("insert into device (deviceCode, deviceId, deviceName, userId) values (?,?,?,?)") >> preparedStatement1
        1 * connection.prepareStatement("select deviceId, deviceCode, deviceName from device where userId is null") >> preparedStatement2
        1 * preparedStatement1.setString(1,device2.deviceCode)
        1 * preparedStatement1.setString(1,device3.deviceCode)
        1 * preparedStatement1.setString(1,device1.deviceCode)
        1 * preparedStatement1.setString(2,device1.deviceId)
        1 * preparedStatement1.setString(2,device2.deviceId)
        1 * preparedStatement1.setString(2,device3.deviceId)
        1 * preparedStatement1.setString(3,device1.deviceName)
        1 * preparedStatement1.setString(3,device2.deviceName)
        1 * preparedStatement1.setString(3,device3.deviceName)
        3 * preparedStatement1.setString(4,userId.toString())
        3 * preparedStatement1.execute()
        1 * preparedStatement2.executeQuery()
        0 * _
    }


}
