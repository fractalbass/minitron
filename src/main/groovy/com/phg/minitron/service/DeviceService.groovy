package com.phg.minitron.service

import com.phg.minitron.dao.DeviceDao
import com.phg.minitron.dao.MessageDao
import com.phg.minitron.model.Device
import com.phg.minitron.model.Message
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
 * Created by milesporter on 2/27/17.
 */
@Component
class DeviceService {

    @Autowired
    DeviceDao deviceDao

    @Autowired
    MessageDao messageDao

    ArrayList<Device> getDevicesForUser(UUID userId) {
        return deviceDao.getDevicesByUserId(userId)
    }

    def associateDeviceToUser(Device device, UUID userId) {
        device.userId = userId
        deviceDao.update(device)
        device
    }

    def deAssociateDeviceFromUser(Device device) {
        device.userId = null
        deviceDao.update(device)
        device
    }

    def getAllDevices() {
        deviceDao.getAllDevices()
    }

    def getAllNonAssociatedDevices() {
        deviceDao.getAllNonAssociatedDevices()
    }

    def createDeviceForUser(String deviceName, UUID userId) {
        Device device = new Device()
        device.deviceId = UUID.randomUUID()
        device.userId=userId
        device.deviceName=deviceName
        device.deviceCode=Device.generateCode()
        deviceDao.save(device)
        (0..11).each() { channel ->
            Message message = new Message()
            message.channel = channel
            message.deviceId = device.deviceId
            message.messageText = "Coming soon."
            message.messageId = UUID.randomUUID()
            messageDao.save(message)
        }
        device
    }

    def getDeviceById(UUID deviceId) {
        deviceDao.getDeviceById(deviceId.toString())
    }
}
