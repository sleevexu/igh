package thread;

import service.CommSender;

import java.text.SimpleDateFormat;
import java.util.Date;

public class RefreshNodeThread implements Runnable {

    private static CommSender sender;

    public RefreshNodeThread(CommSender sender) {
        this.sender = sender;
    }

    @Override
    public void run() {
        Date now = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println("Task 2 started" + simpleDateFormat.format(now));
        sender.getNodeAddress();

    }
}
