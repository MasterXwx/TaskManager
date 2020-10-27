package com.weex.taskmanager

import android.util.Log

/**
 * Created by Weex on 2020-10-26.
 *
 */
abstract class NamedTask(var name: String = "NamedTask") : Runnable {

    var next: NamedTask? = null

    override fun run() {
//        Log.d(TAG, "task $name run")
        doTask()
//        Log.d(TAG, "task $name finished")
        next?.run {
            doTask()
        }
    }

    fun before(task: NamedTask) {
        next = task
    }

    fun after(task: NamedTask) {
        task.next = this
    }

    abstract fun doTask()

}
