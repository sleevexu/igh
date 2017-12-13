package listener;

import com.sun.activation.registries.MailcapParseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import service.ParseDataService;
import util.ByteUtil;
import util.SerialUtil;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jiajie on 2017/10/30.
 */
public class CommListener implements SerialPortEventListener {
    private SerialUtil serialUtil;

    private InputStream inputStream;

    private SerialPort port;

    private ArrayList<Byte> address;

    public CommListener(SerialPort port, ArrayList<Byte> address) {
        this.port = port;
        this.address = address;
        try {
            this.inputStream = new BufferedInputStream(port.getInputStream());
        } catch (IOException e) {
            System.out.println("IOException!");
        }
    }

    @Override
    public void serialEvent(SerialPortEvent serialPortEvent) {
        switch (serialPortEvent.getEventType()) {
            case SerialPortEvent.BI: // 10通讯中断
            case SerialPortEvent.FE: // 9帧错误
            case SerialPortEvent.PE: // 8奇偶校验
            case SerialPortEvent.OE: // 7溢位错误
            case SerialPortEvent.CD: // 6载波检测
            case SerialPortEvent.RI: // 5振铃指示
            case SerialPortEvent.DSR: // 4数据设备准备好
            case SerialPortEvent.CTS: // 3清除发送
            case SerialPortEvent.OUTPUT_BUFFER_EMPTY: // 2输出缓冲区已清空
                break;
            case SerialPortEvent.DATA_AVAILABLE: // 1 读到可用数据时激活
            {
                System.out.println("SerialPortEvent.DATA_AVAILABLE occurred");
                try {
                    byte[] data = new byte[19];
                    try {
                        int i = 0;
                        while (inputStream.available() > 0 && data[i] != -1) {
                            try {
                                Thread.sleep(15);
                            }catch (InterruptedException e){e.printStackTrace();}
                            data[i] = (byte) inputStream.read();
                            i++;
                            if (inputStream.available() == 0) {
                                Timestamp sampleTime = new Timestamp(System.currentTimeMillis());
                                Map<String, Object> dataPackage = new HashMap<>();
                                dataPackage.put("sampleTime", sampleTime);
                                dataPackage.put("data", data);
                                System.out.println(ByteUtil.bytesToHexString(data));
                                ParseDataService.parseData(address, dataPackage);
                                break;
                            }
                        }
                    } catch (IOException e) {
                        System.out.println("IOException");
                        e.printStackTrace();
                    }
                } catch (UnsupportedOperationException e) {
                    System.out.println("Cant do this");
                }
            }
        }
    }
}