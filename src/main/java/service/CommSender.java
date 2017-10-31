package service;

import gnu.io.SerialPort;
import util.ByteUtil;
import util.SerialUtil;

/**
 * Created by Jiajie on 2017/10/30.
 */
public class CommSender {
    private static SerialUtil serialUtil = new SerialUtil();
    private static SerialPort port;
    private static byte[] GET_DATA_ORDER = {(byte) 0x01,(byte)0x02};
    private static byte[] REFRESH_NODE_ORDER = {(byte) 0xff};

    public CommSender(SerialPort serialPort) {
        this.port = serialPort;
    }

    public static void getData() {
        serialUtil.sendToPort(port, GET_DATA_ORDER);
        System.out.println("Send Data Command" + ByteUtil.bytesToHexString(GET_DATA_ORDER));
        byte[] data = serialUtil.readFromPort(port);
        System.out.println("Read Data:"+ByteUtil.bytesToHexString(data));
    }

    public static void getNodeAddress() {
        serialUtil.sendToPort(port, REFRESH_NODE_ORDER);
        System.out.println("Refresh Node Address:" + ByteUtil.bytesToHexString(REFRESH_NODE_ORDER));
    }

}
