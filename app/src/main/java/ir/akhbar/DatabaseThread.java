package ir.akhbar;

import java.util.LinkedList;
import java.util.Queue;

public class DatabaseThread extends Thread {

    private boolean isStarted = false;

    private Queue<Runnable> queue = new LinkedList<>();

    @Override
    public void run() {
        isStarted = true;
        while (!queue.isEmpty()) {
            Runnable work = queue.poll();
            if (work != null) {
                work.run();
            }
        }
        isStarted = false;
    }

    public void addRunnable(Runnable runnable) {
        queue.offer(runnable);
        if (!isStarted) {
            start();
        }
    }
}
