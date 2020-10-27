package com.weex.manager

import java.util.concurrent.atomic.AtomicInteger

/**
 * Created by Weex on 2020-10-26.
 *
 */
abstract class Task(var name: String = "") : ITask, Runnable, TaskListener {

    companion object {
        const val TAG = "TASK"
        const val STATE_READY = 0
        const val STATE_RUNNING = 1
        const val STATE_FINISHED = 2
    }

    val remainTask = AtomicInteger()

    var nextTask: Task? = null

    var preTask: Task? = null

    var childTasks: MutableList<Task> = mutableListOf()

    var taskState: Int = STATE_READY

    var taskListeners: MutableList<TaskListener> = mutableListOf()

    fun before(task: Task): Task {
        val pre = task.preTask
        pre?.let {
            it.nextTask = this
            this@Task.preTask = it
        }
        nextTask = task
        task.preTask = this
        return this
    }

    fun after(task: Task): Task {
        val after = task.nextTask
        after?.let {
            nextTask = it
            it.preTask = this
        }
        preTask = task
        task.nextTask = this
        return this
    }

    fun addTask(task: Task) {
        childTasks.add(task)
        task.addListener(this)
        remainTask.set(childTasks.size)
    }

    fun addTasks(tasks: List<Task>) {
        tasks.forEach {
            it.addListener(this)
        }
        childTasks.addAll(tasks)
        remainTask.set(childTasks.size)
    }

    fun addTasks(tasks: Array<Task>) {
        tasks.forEach {
            it.addListener(this)
        }
        childTasks.addAll(tasks)
        remainTask.set(childTasks.size)
    }

    fun addListener(listener: TaskListener) {
        taskListeners.add(listener)
    }

    fun clearListener() {
        taskListeners.clear()
    }

    abstract fun task()

    override fun run() {
        switchState(STATE_RUNNING)
        notifyStarted()
        task()
        notifyFinished()
        switchState(STATE_FINISHED)
    }

    fun notifyStarted() {
        taskListeners.forEach {
            it.onStart(this)
        }
    }

    fun notifyFinished() {
        taskListeners.forEach {
            it.onFinished(this)
        }
    }

    override fun getCurState(): Int {
        return taskState
    }

    private fun switchState(state: Int) {
        taskState = state
    }

    override fun onFinished(task: Task) {
        if (childTasks.isEmpty()) {
            return
        }

        synchronized(remainTask) {
            val remain = remainTask.decrementAndGet()
            println("task ${task.name} finished | group $name remain $remain tasks ")
            if (remain == 0) {
                taskListeners.forEach {
                    it.onGroupFinished(this)
                }
            }
        }
    }

    override fun onStart(task: Task) {

    }

    override fun onGroupFinished(group: Task) {

    }

    override fun isGroupTask(): Boolean {
        return remainTask.get() > 0
    }

}
