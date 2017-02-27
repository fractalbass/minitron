package com.phg.minitron.dao

import com.phg.minitron.model.Device
import org.springframework.stereotype.Component

/**
 * Created by milesporter on 2/26/17.
 */
@Component
class DeviceDao {

    ArrayList<Device> getDevicesByUserId(UUID userId) {
        return null
    }

    boolean update(Device device) {
        return false
    }

    ArrayList<Device> getAllDevices() {
        return null
    }

    ArrayList<Device> getAllNonAssociatedDevices() {
        return null
    }

}
