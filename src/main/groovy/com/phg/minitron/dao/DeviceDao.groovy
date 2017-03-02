package com.phg.minitron.dao

import com.phg.minitron.model.Device
import org.springframework.stereotype.Component

import java.sql.PreparedStatement
import java.sql.ResultSet

/**
 * Created by milesporter on 2/26/17.
 */
@Component
class DeviceDao extends BaseDao {

    ArrayList<Device> getDevicesByUserId(UUID userId) {
        Device device =  new Device();
        try {
            PreparedStatement preparedStatement = getPreparedStatement("select deviceCode, deviceId, from device where userId=?")
            preparedStatement.setInt(1, userId)
            preparedStatement.executeQuery()
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                device.setDeviceCode(rs.getInt(1));
                device.setDeviceId(rs.getInt(2))
            }
        } catch (Exception exp) {
            System.out.println("Error: " + exp.toString())
        }
        return device
    }

    boolean save(Device device) {
        boolean result = false;
        try {
            PreparedStatement preparedStatement = getPreparedStatement("insert into device (deviceCode, deviceId, userId) values (?,?,?)")
            preparedStatement.setString(1, device.deviceCode)
            preparedStatement.setString(2, device.deviceId)
            preparedStatement.setString(3, device.userId)
            preparedStatement.execute()
            result=true
        } catch (Exception exp) {
            System.out.println("Error: " + exp.toString())
        }
        return result
    }

    boolean update(Device device) {
        boolean result = false;
        try {
            PreparedStatement preparedStatement = getPreparedStatement("update device set deviceCode=?, userId=? where deviceId=?")
            preparedStatement.setString(1, device.deviceCode)
            preparedStatement.setString(3, device.deviceId)
            preparedStatement.setString(2, device.userId)
            preparedStatement.execute()
            result=true
        } catch (Exception exp) {
            System.out.println("Error: " + exp.toString())
        }
        return result
    }

        ArrayList<Device> getAllDevices() {
        boolean result = false;
        ArrayList<Device> devices = new ArrayList<>()
        try {
            PreparedStatement preparedStatement = getPreparedStatement("select deviceId, deviceCode, userId from device)")
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Device d = new Device(deviceId: rs.getInt(1),
                                      deviceCode: rs.getInt(2),
                                      userId: rs.getString(3))
                devices.add(d)
            }
            result=true
        } catch (Exception exp) {
            System.out.println("Error: " + exp.toString())
        }
        return devices
    }

    ArrayList<Device> getAllNonAssociatedDevices() {
        ArrayList<Device> devices = new ArrayList<>()
        try {
            PreparedStatement preparedStatement = getPreparedStatement("select deviceId, deviceCode from device where userId is null)")
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Device d = new Device(deviceId: rs.getInt(1),
                                      deviceCode: rs.getInt(2))
                devices.add(d)
            }
        } catch (Exception exp) {
            System.out.println("Error: " + exp.toString())
        }
    return devices
    }

}
