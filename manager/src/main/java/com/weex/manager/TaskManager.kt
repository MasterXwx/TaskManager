package com.weex.manager

import java.util.concurrent.*
import java.util.concurrent.atomic.AtomicLong

/**
 * Created by Weex on 2020-10-26.
 *
 */

val MAX_THREADS = Runtime.getRuntime().availableProcessors()

object TaskManager : Runnable, TaskListener {

    val taskQueue = LinkedBlockingQueue<Task>()

    val lock = Object()

    private val taskManagerThreadFactory = object : ThreadFactory {

        val threadNo = AtomicLong(1)

        override fun newThread(r: Runnable): Thread {
            return Thread(r, "task-manager-thread#${threadNo.getAndIncrement()}")
        }
    }

    private val taskExecutorService = ThreadPoolExecutor(
        MAX_THREADS, MAX_THREADS, 60, TimeUnit.SECONDS,
        LinkedBlockingQueue<Runnable>(), taskManagerThreadFactory
    )

    private val fetchExecutorService = Executors.newSingleThreadExecutor(
        taskManagerThreadFactory
    )

    init {
        fetchExecutorService.submit(this)
    }

    fun launchTask(task: Task) {
        parseTask(task)
    }

    private fun parseTask(task: Task) {
        taskQueue.add(task)
        if (task.nextTask != null) {
            parseTask(task.nextTask!!)
        }
    }

    override fun run() {
        while (!Thread.interrupted()) {
            val task = taskQueue.take()
            println("take | ${task.name}")
            submit(task)
            synchronized(lock) {
                lock.wait()
            }
        }
    }

    private fun submit(group: Task) {
        group.addListener(this)
        if (group.childTasks.isNotEmpty()) {
            group.childTasks.forEach {
                taskExecutorService.execute(it)
                println("submit # ${it.name}")
            }
        } else {
            taskExecutorService.execute(group)
        }
    }

    override fun onStart(task: Task) {
        println("start # ${task.name}")
    }

    override fun onFinished(task: Task) {
        println("finished # ${task.name}")
        if (task.singleTask()) {
            synchronized(lock) {
                lock.notifyAll()
            }
        }
    }

    override fun onGroupFinished(group: Task) {
        println("group ${group.name} finish")
        synchronized(lock) {
            lock.notifyAll()
        }
    }

}
