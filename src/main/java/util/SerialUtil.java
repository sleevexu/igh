package util;

import gnu.io.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.TooManyListenersException;

/**
 * Created by Jiajie on 2017/10/7.
 */
public class SerialUtil {

    private String portName;

    public String getPortName() {
        return portName;
    }

    public void setPortName(String portName) {
        this.portName = portName;
    }

    @SuppressWarnings("unchecked")
    public static final ArrayList<String> findPort() {
        Enumeration<CommPortIdentifier> portList = CommPortIdentifier.getPortIdentifiers();
        ArrayList<String> portNameList = new ArrayList<String>();
        while (portList.hasMoreElements()) {
            String portName = portList.nextElement().getName();
            portNameList.add(portName);
        }
        return portNameList;

    }

    public static final SerialPort openPort(String portName, int baudRate) {
        try {
            CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(portName);
            CommPort commPort = portIdentifier.open(portName, 2000);
            if (commPort instanceof SerialPort) {
                SerialPort serialPort = (SerialPort) commPort;
                try {
                    serialPort.setSerialPortParams(baudRate, SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
                            SerialPort.PARITY_NONE);
                } catch (UnsupportedCommOperationException e) {
                    System.out.println("Set Serial Port Parameters failure.{}");
                }
                System.out.println("Open" + portName + " successfully !");
                return serialPort;
            } else {
                System.out.println("This port is not a serial port");
                return null;
            }
        } catch (NoSuchPortException | PortInUseException e) {
            return null;
        }
    }

    public static final void closePort(SerialPort serialPort) {
        String portName = serialPort.getName();
        serialPort.close();
        System.out.println("Close " + portName + " sucessfully !");
    }

    public void sendToPort(SerialPort serialPort, byte[] order) {
        try {
            OutputStream out = serialPort.getOutputStream();
            out.write(order);
            out.flush();
            out.close();
        } catch (IOException e) {
            System.out.println("Send to SerialPort failure");
        }
    }

    public static byte[] readFromPort(SerialPort serialPort) {
        byte[] bytes = new byte[10];
        InputStream readBuffer = null;
        try {
            readBuffer = serialPort.getInputStream();
        } catch (IOException e) {
            closePort(serialPort);
            openPort(serialPort.getName(), serialPort.getBaudRate());
            return null;
        }
        try {
            while (readBuffer.available() > 0) {
                readBuffer.read(bytes);
            }
        } catch (IOException e) {
            System.out.println("IOException");
        }
        String HexBytes = ByteUtil.bytesToHexString(bytes);
        System.out.println(HexBytes);
        return bytes;
    }

    public static void addListener(SerialPort port, SerialPortEventListener listener) throws
            TooManyListenersException {
        try {
            port.addEventListener(listener);
            port.notifyOnDataAvailable(true);
            port.notifyOnBreakInterrupt(true);
        } catch (TooManyListenersException e) {
            System.out.println("Too many listeners");
        }
    }

//    private class CommListener implements SerialPortEventListener {
//
//        private InputStream inputStream;
//
//        @Override
//        public void serialEvent(SerialPortEvent serialPortEvent) {
//            switch (serialPortEvent.getEventType()) {
//                case SerialPortEvent.BI: // 10通讯中断
//                case SerialPortEvent.FE: // 9帧错误
//                case SerialPortEvent.PE: // 8奇偶校验
//                case SerialPortEvent.OE: // 7溢位错误
//                case SerialPortEvent.CD: // 6载波检测
//                case SerialPortEvent.RI: // 5振铃指示
//                case SerialPortEvent.DSR: // 4数据设备准备好
//                case SerialPortEvent.CTS: // 3清除发送
//                case SerialPortEvent.OUTPUT_BUFFER_EMPTY: // 2输出缓冲区已清空
//                    break;
//                case SerialPortEvent.DATA_AVAILABLE: // 1 读到可用数据时激活
//                {
//                    System.out.println("SerialPortEvent.DATA_AVAILABLE occurred");
//                    byte[] readBuffer = new byte[1024];
//                    try {
//                        Thread.sleep(20000);//硬件发送是分段的，加一个延时就行了
//                        int numBytes = inputStream.read(readBuffer);
//                        System.out.println(numBytes);
//                    }catch (IOException | InterruptedException e){
//                        System.out.println("IOException");}
//                }
//            }
}
