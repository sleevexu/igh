import gnu.io.SerialPort;
import util.ByteUtil;
import util.SerialUtil;

import java.util.List;

/**
 * Created by Jiajie on 2017/10/30.
 */
public class CommThread implements Runnable {
    CommThread readThread;
    SerialPort serialPort;

    public void start() {
        List<String> portList = SerialUtil.findPort();
        if (portList.size() < 1) {
            System.out.println("no device");
        } else {
            System.out.println(portList);
            SerialUtil serialUtil = new SerialUtil();
            serialUtil.setPortName(portList.get(1));
            SerialPort port = serialUtil.openPort(serialUtil.getPortName(), 9600);
            System.out.println("Port is open");
            try {
                readThread = new CommThread();
                readThread.run();
            } catch (Exception e) {
            }
        }
    }

    @Override
    public void run() {
        System.out.println("Start Read");
        byte[] bytes = SerialUtil.readFromPort(serialPort);
        String ex = ByteUtil.bytesToHexString(bytes);
        System.out.println(ex);
        try {
            java.lang.Thread.sleep(10000);
        } catch (InterruptedException e) {
        }
        start();
    }

}
