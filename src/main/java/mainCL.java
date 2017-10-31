import gnu.io.SerialPort;
import listener.CommListener;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import util.ByteUtil;
import util.SerialUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.TooManyListenersException;
/**
 * Created by Jiajie on 2017/10/30.
 */
public class mainCL {

    public static void main(String[] args) throws TooManyListenersException, SchedulerException {

        List<String> portList = SerialUtil.findPort();
        if (portList.size() < 1) {
            System.out.println("no device");
        } else {
            System.out.println(portList);
            SerialUtil serialUtil = new SerialUtil();
            serialUtil.setPortName(portList.get(1));
            SerialPort port = serialUtil.openPort(serialUtil.getPortName(), 9600);
            System.out.println("Port is open");
            CommSender sender = new CommSender(port);

            CommListener listener = new CommListener(port);
            SerialUtil.addListener(port,listener);
            JobDetail job = JobBuilder.newJob(HelloJob.class).withIdentity("job1","group1").build();

            Trigger trigger = TriggerBuilder.newTrigger().withIdentity("trigger1","group1")
                    .withSchedule(CronScheduleBuilder.cronSchedule("0/2 * * * * ? ")).build();

            Scheduler scheduler = new StdSchedulerFactory().getScheduler();
            scheduler.start();
            scheduler.scheduleJob(job,trigger);
//            byte[] data ={(byte)0x01,(byte)0x02,(byte)0x03,(byte)0xFF};
//            System.out.println("Write"+ByteUtil.bytesToHexString(data));
//            serialUtil.sendToPort(port, data);
//            SerialUtil.closePort(port);

        }
    }

    public static ArrayList<Byte> getNodeList(Byte[] bytes){


    }
}

