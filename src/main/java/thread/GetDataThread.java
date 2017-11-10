package thread;

import service.CommSender;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class GetDataThread implements Runnable {

    private static ArrayList<Byte> address;
    private static CommSender sender;

    public GetDataThread(ArrayList<Byte> address, CommSender sender) {
        this.address = address;
        this.sender = sender;
    }

    @Override
    public void run() {
        try {
            Date now = new Date();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            System.out.println("Task 1 started" + simpleDateFormat.format(now));
            sender.getData(address);
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

