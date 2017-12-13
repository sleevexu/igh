package service;

import helper.DataBaseHelper;
import model.Device;
import model.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.ByteUtil;
import util.CRC16;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ParseDataService {

    private final static Logger logger = LoggerFactory.getLogger(ParseDataService.class);

    public static void parseData(ArrayList<Byte> address, Map<String, Object> dataPackage) {
        byte[] data = (byte[]) dataPackage.get("data");
        Timestamp sampleTime = (Timestamp) dataPackage.get("sampleTime");
        byte nodeAddress = data[0];
        byte dataType = data[1];
        if (dataType == (byte) 0x01) {
            if (CRC16.checkCRC16(data)) {
                if (!address.contains(nodeAddress)) {
                    System.out.println("Invalid Node Address!");
                    return;
                }
                System.out.println("Parse Data start:" + (nodeAddress & 0xff));
                int deviceId = address2deviceId(nodeAddress & 0xff);
                System.out.println("device ID is:" + deviceId);
                HashMap<String, Object> dataMap = new HashMap<>();
                dataMap.put("deviceid", deviceId);
                dataMap.put("sampletime", sampleTime);

                System.out.println("Parse Data of Ask Data");
                if (data.length < 19) return;
                if ((data[16] & 0x01) == 0) {
                    float soilTemp = 0.0f;
                    if ((data[3] & 0x80) == 128) {
                        data[3] = (byte) (data[3] & 0b01111111);
                        soilTemp = (float) (((data[3] << 8) | data[4] & 0xff) / 10.0);
                        System.out.println("soil Temperature:" + soilTemp);
                    } else {
                        soilTemp = (float) (((data[3] << 8) | data[4] & 0xff) / 10.0);
                        System.out.println("soil Temperature:" + soilTemp);
                    }
                    dataMap.put("value", soilTemp);
                    DataBaseHelper.insertEntity("t_soiltemp", dataMap);
                } else System.out.println("Soil Temp is offline.");
                if ((data[16] & 0x08) >> 3 == 0) {
                    float soilHumidity = (float) (((data[5] << 8) | data[6] & 0xff) / 10.0);
                    System.out.println("soil Humidity:" + soilHumidity);
                    dataMap.put("value", soilHumidity);
                    DataBaseHelper.insertEntity("t_soilhumidity", dataMap);
                } else System.out.println("Soil Humidity is offline");
                if ((data[16] & 0x04) >> 2 == 0) {
                    float airTemp = 0.0f;
                    if ((data[7] & 0x80) == 128) {
                        data[7] = (byte) (data[7] & 0b01111111);
                        airTemp = (float) (((data[7] << 8) | data[8] & 0xff) / 10.0);
                        System.out.println("air Temperature:" + airTemp);
                    } else {
                        airTemp = (float) (((data[7] << 8) | data[8] & 0xff) / 10.0);
                        System.out.println("air Temperature:" + airTemp);
                    }
                    dataMap.put("value", airTemp);
                    DataBaseHelper.insertEntity("t_airtemp", dataMap);
                    float airHumidity = (float) (((data[9] << 8) | data[10] & 0xff) / 10.0);
                    System.out.println("air Humidity:" + airHumidity);
                    dataMap.put("value", airHumidity);
                    DataBaseHelper.insertEntity("t_airhumidity", dataMap);
                } else System.out.println("air Sensor is offline");
                if ((data[16] & 0x02) >> 1 == 0) {
                    float light = (data[11] << 8) & 0xff00 | data[12] & 0xff;
                    System.out.println("light:" + light);
                    dataMap.put("value", light);
                    DataBaseHelper.insertEntity("t_light", dataMap);
                } else System.out.println("light sensor is offline");
                float voltage = (float) (((data[13] << 8) | data[14] & 0xff) / 10.0);
                System.out.println("voltage:" + voltage);
                dataMap.put("value", voltage);
                DataBaseHelper.insertEntity("t_voltage", dataMap);
            }else {
                StringBuilder str = new StringBuilder();
                str.append("CRC16 CHECK FAILED!");
                if ((data[16] & 0x01) == 0) {
                    float soilTemp = 0.0f;
                    if ((data[3] & 0x80) == 128) {
                        data[3] = (byte) (data[3] & 0b01111111);
                        soilTemp = (float) (((data[3] << 8) | data[4] & 0xff) / 10.0);
                        str.append("SoilTemp:");
                        str.append(String.valueOf(soilTemp));
                    } else {
                        soilTemp = (float) (((data[3] << 8) | data[4] & 0xff) / 10.0);
                        str.append("SoilTemp:");
                        str.append(String.valueOf(soilTemp));
                    }
                } else System.out.println("Soil Temp is offline.");
                if ((data[16] & 0x08) >> 3 == 0) {
                    float soilHumidity = (float) (((data[5] << 8) | data[6] & 0xff) / 10.0);
                    str.append("SoilHumidity:");
                    str.append(String.valueOf(soilHumidity));
                } else System.out.println("Soil Humidity is offline");
                if ((data[16] & 0x04) >> 2 == 0) {
                    float airTemp = 0.0f;
                    if ((data[7] & 0x80) == 128) {
                        data[7] = (byte) (data[7] & 0b01111111);
                        airTemp = (float) (((data[7] << 8) | data[8] & 0xff) / 10.0);
                        str.append("AirTemp:");
                        str.append(String.valueOf(airTemp));
                    } else {
                        airTemp = (float) (((data[7] << 8) | data[8] & 0xff) / 10.0);
                        str.append("AirTemp:");
                        str.append(String.valueOf(airTemp));
                    }
                    float airHumidity = (float) (((data[9] << 8) | data[10] & 0xff) / 10.0);
                    str.append("AirHumidity:");
                    str.append(String.valueOf(airHumidity));
                } else System.out.println("air Sensor is offline");
                if ((data[16] & 0x02) >> 1 == 0) {
                    float light = (data[11] << 8) & 0xff00 | data[12] & 0xff;
                    str.append("Light:");
                    str.append(String.valueOf(light));
                } else System.out.println("light sensor is offline");
                float voltage = (float) (((data[13] << 8) | data[14] & 0xff) / 10.0);
                str.append("Voltage:");
                str.append(String.valueOf(voltage));
                logger.error(str.toString());
            }
        }
        if (dataType == (byte) 0xfe) {
            System.out.println("Parse Data of Add Node");
            RefreshNodeService.saveNodeAddress(address, data[0]);
            System.out.println(ByteUtil.bytesToHexString(data));
        }
    }

    private static int address2deviceId(int address) {
        String sqlForSerial = "SELECT * FROM node WHERE address=" + address;
        Node nodeMap = DataBaseHelper.queryEntity(Node.class, sqlForSerial);
        String sqlForDevice = "SELECT * FROM device WHERE serialid=" + nodeMap.getSerialId();
        Device deviceMap = DataBaseHelper.queryEntity(Device.class, sqlForDevice);
        return deviceMap.getId();
    }
}
