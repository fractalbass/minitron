package com.phg.minitron.service

import com.phg.minitron.dao.DeviceDao
import com.phg.minitron.dao.MessageDao
import com.phg.minitron.model.Device
import com.phg.minitron.model.Message
import spock.lang.Specification
/**
 * Created by milesporter on 2/26/17.
 */
class DeviceServiceSpec extends Specification{

    def deviceService = new DeviceService()
    def deviceDao

    def setup() {
        deviceDao = Mock(DeviceDao)
        deviceService.deviceDao = deviceDao
    }

    //I can get a list of devices for a user
    def 'I can get a list of devices for a given user ID'() {
        given:
        def userId = UUID.randomUUID()
        def deviceId = UUID.randomUUID()
        ArrayList<Device> deviceList = new ArrayList<>()
        deviceList.add(new Device(deviceId: deviceId, userId: userId, deviceCode: "code"))

        when:
        ArrayList<Device> devices = deviceService.getDevicesForUser(userId)

        then:
        1 * deviceDao.getDevicesByUserId(_) >> deviceList
        0 * _

    }
    //I can associate a device to a user
    def 'I can associate a device to a user'() {
        given:
        def userId = UUID.randomUUID()
        def deviceId = UUID.randomUUID()
        Device device = new Device(deviceId: deviceId, userId: null, deviceCode: "code")

        when:
        def result = deviceService.associateDeviceToUser(device, userId)

        then:
        result.userId==userId.toString()
        result.deviceId==deviceId.toString()
        result.deviceCode=="code"
        1 * deviceDao.update(_) >> true
        0 * _

    }
    //I can de-associate a device from a user
    def 'I can de-associate a device from a user'() {
        given:
        def userId = UUID.randomUUID()
        def deviceId = UUID.randomUUID()
        Device device = new Device(deviceId: deviceId, userId: userId, deviceCode: "code")

        when:
        def result = deviceService.deAssociateDeviceFromUser(device)

        then:
        result.userId==null
        result.deviceId==deviceId.toString()
        result.deviceCode=="code"
        1 * deviceDao.update(_) >> true
        0 * _

    }
    //I can get a list of all devices
    def 'I can get a list of all devices'() {
        given:
        ArrayList<Device> deviceList = new ArrayList<>()
        (1..5).each() {
            def userId = UUID.randomUUID()
            def deviceId = UUID.randomUUID()
            deviceList.add(new Device(deviceId: deviceId, userId: userId, deviceCode: "code"))
        }

        when:
        ArrayList<Device> devices = deviceService.getAllDevices()

        then:
        devices.size()==5
        1 * deviceDao.getAllDevices() >> deviceList
        0 * _
    }
    def 'I can get a list of all non-associated devices'() {
        given:
        ArrayList<Device> deviceList = new ArrayList<>()
        (1..5).each() {
            def deviceId = UUID.randomUUID()
            deviceList.add(new Device(deviceId: deviceId, userId: null, deviceCode: "code"))
        }

        when:
        ArrayList<Device> devices = deviceService.getAllNonAssociatedDevices()

        then:
        devices.size()==5
        1 * deviceDao.getAllNonAssociatedDevices() >> deviceList
        0 * _
    }

    def 'When I create a device I also create 12 messages'() {
        given:
        MessageDao messageDao = Mock(MessageDao)
        deviceService.messageDao = messageDao

        when:
        deviceService.createDeviceForUser('dontcare',UUID.randomUUID())

        then:
        12 * messageDao.save(_)
        1 * deviceDao.save(_)
        0 * _
    }

    def 'I can delete a devices and its associated messages'() {
        given:
        MessageDao messageDao = Mock(MessageDao)
        deviceService.messageDao = messageDao
        UUID deviceId = UUID.randomUUID()
        Message m1 = new Message(deviceId: deviceId)
        Message m2 = new Message(deviceId: deviceId)
        Message m3 = new Message(deviceId: deviceId)
        ArrayList<Message> messages = [m1,m2,m3]

        when:
        deviceService.deleteDevice(deviceId)

        then:
        1 * messageDao.delete(m1)
        1 * messageDao.delete(m2)
        1 * messageDao.delete(m3)

        1 * messageDao.getByDevice( deviceId.toString()) >> messages
        1 * deviceDao.deleteDevice( deviceId.toString())
        0 * _
    }



}
