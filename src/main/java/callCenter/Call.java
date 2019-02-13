package callCenter;

import java.util.concurrent.atomic.AtomicInteger;

public class Call {
    private String id;
    private int level = 0;
    private boolean reQueue = false;
    private static AtomicInteger idGenerator = new AtomicInteger();

    public Call() {
        this.id = String.valueOf(idGenerator.incrementAndGet());
    }

    public String getId() {
        return id;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public boolean isReQueue() {
        return reQueue;
    }

    public void setReQueue(boolean reQueue) {
        this.reQueue = reQueue;
    }

    public String toString() {
        return "Call-" + id;
    }
}
