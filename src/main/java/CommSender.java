import gnu.io.SerialPort;
import util.ByteUtil;
import util.SerialUtil;

/**
 * Created by Jiajie on 2017/10/30.
 */
public class CommSender {
    private static SerialUtil serialUtil = new SerialUtil();
    private static SerialPort port;
    private static byte[] DATA_ORDER = {(byte)0x01};

    public CommSender(SerialPort serialPort){this.port = serialPort;}

    public static void getData(){
        serialUtil.sendToPort(port, DATA_ORDER);
        System.out.println("Send Data Command"+ ByteUtil.bytesToHexString(DATA_ORDER));
    }

}
