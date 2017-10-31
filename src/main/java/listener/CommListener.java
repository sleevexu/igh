package listener;

import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import util.ByteUtil;
import util.SerialUtil;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Jiajie on 2017/10/30.
 */
public class CommListener implements SerialPortEventListener {

    private InputStream inputStream;

    public CommListener(SerialPort serialPort) {
        try {
        this.inputStream = new BufferedInputStream(serialPort.getInputStream());// 获取输入流
        } catch (IOException e) {
        System.out.println("IOException");
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
                byte[] readBuffer = new byte[10];
                try {
                    System.out.println("READ");
                    int i = 0;
                    while (inputStream.available()>0 && readBuffer[i] != -1){

//                    while ((i=inputStream.read(readBuffer))!=-1){
//                        String str = ByteUtil.bytesToHexString(readBuffer);
//                        System.out.println(str);
//                    }
//                    int numBytes = inputStream.read(readBuffer);
                        readBuffer[i] = (byte)inputStream.read();
                        i++;
                        System.out.println("Number:"+i);
                        System.out.println(ByteUtil.bytesToHexString(readBuffer));
                        Thread.sleep(100);
                    }

            }catch (IOException e1){
                System.out.println("IOException");} catch (InterruptedException e) {
                System.out.println("Interrupted");
                    e.printStackTrace();
                }
            }
        }
    }
}
