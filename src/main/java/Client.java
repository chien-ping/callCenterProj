import callCenter.CallManager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Client {
    static CallManager callManager;

    public static void main(String[] args) {

        int fresher = getParam("fresher");
        int tl = getParam("TL");
        int pm = getParam("PM");
        callManager = CallManager.getInstance(fresher, tl, pm);

        int call = getParam("call");
        Client client = new Client();

        System.out.println("================= start calling ==================");
        client.complain(call);

        callManager.shutdown();
    }

    static int getParam(String param) {
        System.out.printf("Enter number of %s :", param);
        String value = System.console().readLine();
        try{
            return Integer.valueOf(value);
        }catch(Exception e) {
           return 1;
        }
    }

    private void complain(int num) {
        ExecutorService clients = Executors.newCachedThreadPool();

        List<Future> result = new ArrayList<Future>();

        for (int i = 0; i < num; i++) {
            result.add(clients.submit(new Runnable() {
                @Override
                public void run() {
                    callManager.call();
                }
            }));
        }

        for (Future future : result) {
            try {
                future.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

        clients.shutdown();
    }
}
