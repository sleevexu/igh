package service;

import common.CONSTANT;
import gnu.io.SerialPort;
import util.ByteUtil;
import util.SerialUtil;

import java.util.ArrayList;

/**
 * Created by Jiajie on 2017/10/30.
 */
public class CommSender {
    private static SerialUtil serialUtil = new SerialUtil();
    private static SerialPort port;
    private static byte[] GET_DATA_ORDER = new byte[2];
    private static byte[] REFRESH_NODE_ORDER = {(byte) 0xff};

    public CommSender(SerialPort port) {
        this.port = port;
    }

    public static void getData(ArrayList<Byte> address) {
        for (int i = 0; i < address.size(); i++) {
            GET_DATA_ORDER[0] = CONSTANT.GET_DATA_CMD;
            GET_DATA_ORDER[1] = address.get(i);
            serialUtil.sendToPort(port, GET_DATA_ORDER);
            System.out.println("Send Data Command " + ByteUtil.bytesToHexString(GET_DATA_ORDER));
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void getNodeAddress() {
        serialUtil.sendToPort(port, REFRESH_NODE_ORDER);
        System.out.println("Refresh Node Address:" + ByteUtil.bytesToHexString(REFRESH_NODE_ORDER));
    }

}
