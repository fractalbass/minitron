package com.phg.minitron.dao

import com.phg.minitron.model.Device
import groovy.util.logging.Slf4j
import org.springframework.stereotype.Component

import java.sql.PreparedStatement
import java.sql.ResultSet

/**
 * Created by milesporter on 2/26/17.
 */
@Slf4j
@Component
class DeviceDao extends BaseDao {

    ArrayList<Device> getDevicesByUserId(UUID userId) {
        ArrayList<Device> devices = new ArrayList<>()
        try {
            PreparedStatement preparedStatement = getPreparedStatement("select deviceCode, deviceId, deviceName from device where userId=?")
            preparedStatement.setString(1, userId.toString())
            ResultSet rs = preparedStatement.executeQuery()
            while (rs.next()) {
                Device device =  new Device()
                device.setDeviceCode(rs.getString(1))
                device.setDeviceId(rs.getString(2))
                device.setDeviceName((rs.getString(3)))
                devices.add(device)
            }
        } catch (Exception exp) {
            log.error("Error getting device: " + exp.toString())
        }
        return devices
    }

    boolean save(Device device) {
        boolean result = false;
        try {
            PreparedStatement preparedStatement = getPreparedStatement("insert into device (deviceCode, deviceId, deviceName, userId) values (?,?,?,?)")
            preparedStatement.setString(1, device.deviceCode)
            preparedStatement.setString(2, device.deviceId)
            preparedStatement.setString(3, device.deviceName)
            preparedStatement.setString(4, device.userId)
            preparedStatement.execute()
            result=true
        } catch (Exception exp) {
            log.error("Error saving device: " + exp.toString())
        }
        return result
    }

    boolean update(Device device) {
        boolean result = false;
        try {
            PreparedStatement preparedStatement = getPreparedStatement("update device set deviceCode=?, userId=?, deviceName=? where deviceId=?")
            preparedStatement.setString(1, device.deviceCode)
            preparedStatement.setString(2, device.userId)
            preparedStatement.setString(3, device.deviceName)
            preparedStatement.setString(4, device.deviceId)
            preparedStatement.execute()
            result=true
        } catch (Exception exp) {
            log.error("Error updating device: " + exp.toString())
        }
        return result
    }

    ArrayList<Device> getAllDevices() {

        ArrayList<Device> devices = new ArrayList<>()
        try {
            PreparedStatement ps = getPreparedStatement("select deviceId, deviceCode, deviceName, userId from device")
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Device d = new Device(deviceId: rs.getInt(1),
                                      deviceCode: rs.getInt(2),
                                      deviceName: rs.getString(3),
                                      userId: rs.getString(4))
                devices.add(d)
            }

        } catch (Exception exp) {
            log.error("Error getting all devices: " + exp.toString())
        }
        return devices
    }

    ArrayList<Device> getAllNonAssociatedDevices() {
        ArrayList<Device> devices = new ArrayList<>()
        try {
            PreparedStatement ps = getPreparedStatement("select deviceId, deviceCode, deviceName from device where userId is null")
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Device d = new Device(deviceId: rs.getInt(1),
                                      deviceCode: rs.getInt(2),
                                      deviceName: rs.getString(3))
                devices.add(d)
            }
        } catch (Exception exp) {
            log.error("Error: " + exp.toString())
        }
    return devices
    }

    Device getDeviceById(String deviceId) {
        Device device = null
        try {
            PreparedStatement ps = getPreparedStatement("select userId, deviceCode, deviceName from device where deviceId = ?")
            ps.setString(1, deviceId)
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                device = new Device(userId: rs.getString(1),
                        deviceCode: rs.getString(2),
                        deviceName: rs.getString(3),
                        deviceId: deviceId)
                }
        } catch (Exception exp) {
            log.error("Error getting device by ID: " + exp.toString())
        }
        return device
    }

    Device getDeviceByCode(String code) {
        Device device = null
        try {
            PreparedStatement ps = getPreparedStatement("select userId, deviceId, deviceName from device where deviceCode = ?")
            ps.setString(1, code)
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                device = new Device(userId: rs.getString(1),
                        deviceId: rs.getString(2),
                        deviceName: rs.getString(3),
                        deviceCode: code)
            }
        } catch (Exception exp) {
            log.error("Error getting device by ID: " + exp.toString())
        }
        return device
    }

}
