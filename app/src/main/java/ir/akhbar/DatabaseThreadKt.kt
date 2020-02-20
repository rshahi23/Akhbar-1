package ir.akhbar

import java.util.*
import java.util.concurrent.atomic.AtomicBoolean

class DatabaseThreadKt : Thread() {

    private val isStarted = AtomicBoolean(false)

    private val queue: Queue<Runnable> = LinkedList()

    override fun run() {
        super.run()
        isStarted.set(true)
        while (!queue.isEmpty()) {
            queue.poll()?.run()
        }
        isStarted.set(false)
    }

    fun addRunnable(runnable: Runnable) {
        queue.offer(runnable)
        if (!isStarted.get()) {
            start()
        }
    }

}