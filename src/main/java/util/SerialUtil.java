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

    public static void sendToPort(SerialPort serialPort, byte[] order) {
        try {
            OutputStream out = serialPort.getOutputStream();
            out.write(order);
            out.flush();
            out.close();
        } catch (IOException e) {
            System.out.println("Send to SerialPort failure");
        }
    }

    public byte[] readFromPort(SerialPort serialPort) {
        synchronized (this){
        byte[] bytes = new byte[16];
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
        return bytes;}
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
}
