import common.Configuration;
import gnu.io.SerialPort;
import listener.CommListener;
import service.CommSender;
import service.RefreshNodeService;
import thread.GetDataThread;
import thread.GetWeatherThread;
import thread.RefreshNodeThread;
import thread.SyncThread;
import util.SerialUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.TooManyListenersException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by Jiajie on 2017/10/30.
 */
public class mainCL {

    public static void main(String[] args) throws TooManyListenersException {
        SerialPort port = init();
        if (port != null) {
            ArrayList<Byte> address = new ArrayList<>();
            System.out.println("Address length: " + address.size());
            CommListener listener = new CommListener(port, address);
            SerialUtil.addListener(port, listener);
            CommSender sender = new CommSender(port);
            ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
            service.scheduleAtFixedRate(new GetDataThread(address, sender), Configuration.IndoorAcqDelay, Configuration.IndoorAcqCycle, TimeUnit.SECONDS);
            service.scheduleAtFixedRate(new RefreshNodeThread(sender), Configuration.NodeAddressAcqDelay, Configuration.NodeAddressAcqCycle, TimeUnit.SECONDS);
            ScheduledThreadPoolExecutor taskExecutor = new ScheduledThreadPoolExecutor(2);
            taskExecutor.scheduleAtFixedRate(new SyncThread(), Configuration.UploadDelay, Configuration.UploadCycle, TimeUnit.SECONDS);
            taskExecutor.scheduleAtFixedRate(new GetWeatherThread(), Configuration.WeatherReportAcqDelay, Configuration.WeatherReportAcqCycle, TimeUnit.SECONDS);
        } else {
            System.out.println("There is no device connected!");
        }
    }

    private static SerialPort init() {
        List<String> portList = SerialUtil.findPort();
        if (portList.size() < 1) {
            System.out.println("There is no device connected!");
            return null;
        } else {
            System.out.println("Port list is " + portList);
            SerialUtil serialUtil = new SerialUtil();
            serialUtil.setPortName(portList.get(0));
            SerialPort port = SerialUtil.openPort(serialUtil.getPortName(), 2400);
            return port;
        }
    }
}