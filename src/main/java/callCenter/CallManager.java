package callCenter;

import java.util.concurrent.*;

public class CallManager {
    private static CallManager instance;
    private ExecutorService phonePool;
    private CustomerService customerService;

    public static CallManager getInstance(int fresher, int tl, int pm) {
        if (instance == null){
            synchronized(CallManager.class){
                if(instance == null) {
                    instance = new CallManager(fresher, tl, pm);
                }
            }
        }
        return instance;
    }

    private CallManager(int fresher, int tl, int pm) {
        int employees = fresher + tl + pm;
        initCustomerService(fresher, tl, pm);
        initPhonePool(employees);
        System.out.printf("Call center is open. There are %d fresher(s), %d TL and %d PM.%n", fresher, tl, pm);
    }

    private void initCustomerService(int fresher, int tl, int pm) {
        customerService = new Employe("fresher", 1, fresher);
        CustomerService technicalLead = new Employe("TL", 2, tl);
        CustomerService productManager = new Employe("PM", 3, pm);
        customerService.setSuperior(technicalLead);
        technicalLead.setSuperior(productManager);
    }

    private void initPhonePool(int employees) {
        phonePool = Executors.newFixedThreadPool(employees);
    }

    public void shutdown() {
        phonePool.shutdown();
    }

    public void call() {
        call(null);
    }

    private void call(Call call) {
        Future<Call> future = phonePool.submit(new Process(call));
        try {
            call = future.get();
            if (false == call.isReQueue()) {
                return;
            }
            //為避免頻繁請求，五秒後再重新發送。
            TimeUnit.SECONDS.sleep(5);
            call(call);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    class Process implements Callable {
        private Call call;

        public Process(Call call) {
            this.call = call;
        }

        @Override
        public Call call() {
            if (null == call) {
                call = new Call();
            }
            customerService.service(call);
            return call;
        }
    }
}
