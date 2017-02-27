package com.phg.minitron.service

import com.phg.minitron.dao.DeviceDao
import com.phg.minitron.model.Device
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
 * Created by milesporter on 2/27/17.
 */
@Component
class DeviceService {

    @Autowired
    DeviceDao deviceDao

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
}
