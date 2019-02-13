package callCenter;

import java.util.concurrent.Semaphore;

public abstract class CustomerService {
    private int rank;
    private String name;
    private CustomerService superior;
    private Semaphore available;

    CustomerService(String name, int rank, int size) {
        this.name = name;
        this.rank = rank;
        available = new Semaphore(size, true);
    }

    void setSuperior(CustomerService superior) {
        this.superior = superior;
    }

    String getName() {
        return name;
    }

    int getRank() {
        return rank;
    }

    void service(Call call) {
        boolean isSolved = false;
        if (call.getLevel() < rank) {
            if (available.tryAcquire()) {
                try {
                    isSolved = resolve(call);
                } finally {
                    available.release();
                }
            }
        }

        if (isSolved) {
            done(call);
        } else if (null == superior && call.getLevel() == rank) {
            fail(call);
        } else if (null != superior) {
            escalate(call);
        } else {
            reQueue(call);
        }
    }

    protected abstract boolean resolve(Call call);


    private void done(Call call) {
        System.out.printf("%s is resolved by %s.%n", call.toString(), name);
        call.setReQueue(false);

    }

    private void fail(Call call) {
        System.out.printf("%s can't be resolved by %s.%n", call.toString(), name);
        call.setReQueue(false);
        record(call);
    }

    /*
     * Problem can not be solved, record it.
     * @param call
     */
    private void record(Call call) {
    }

    private void escalate(Call call) {
        System.out.printf("%s is escalated to %s.%n", call.toString(), superior.getName());
        superior.service(call);
    }

    private void reQueue(Call call) {
        System.out.printf("%s: The line is busy, please wait a moment.%n", call.toString());
        call.setReQueue(true);
    }
}
