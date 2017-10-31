import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import util.SerialUtil;

/**
 * Created by Jiajie on 2017/10/30.
 */
public class HelloJob implements Job{

    private SerialUtil serialUtil;

    private CommSender sender;

    @Override
    public void execute(JobExecutionContext context)
            throws JobExecutionException{
        System.out.println("Hello Quartz");
        sender.getData();
        System.out.println("Get Data");

    }

}
