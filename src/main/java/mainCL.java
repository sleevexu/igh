import gnu.io.SerialPort;
import listener.CommListener;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import service.CommSender;
import task.GetDataTask;
import util.SerialUtil;

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
            serialUtil.setPortName(portList.get(0));
            SerialPort port = serialUtil.openPort(serialUtil.getPortName(), 115200);
            System.out.println("Port is open");
//            CommListener listener = new CommListener(port);
//            SerialUtil.addListener(port, listener);
            CommSender sender = new CommSender(port);

            JobDetail job = JobBuilder.newJob(GetDataTask.class).withIdentity("job1", "group1").build();

            Trigger trigger = TriggerBuilder.newTrigger().withIdentity("trigger1", "group1")
                    .withSchedule(CronScheduleBuilder.cronSchedule("0/5 * * * * ? * ")).build();

            Scheduler scheduler = new StdSchedulerFactory().getScheduler();
            scheduler.start();
            scheduler.scheduleJob(job, trigger);
        }
    }

}

