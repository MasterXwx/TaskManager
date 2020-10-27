package com.weex.taskmanager

import com.weex.manager.Task
import com.weex.manager.TaskManager

/**
 * Created by Weex on 2020-10-27.
 *
 */
class Test {

    companion object {
        fun main(args: Array<String>) {
            val groupA = TaskA()
            val childTask = TaskA()
            val childTaskB = TaskA()
            groupA.addTask(childTask)
            groupA.addTask(childTaskB)

            val groupB = TaskB()
            val childGB = TaskB()
            groupB.addTask(childGB)
            groupA.before(groupB)

            TaskManager.launchTask(groupA)
        }
    }

    class TaskA : Task("init-sdk") {

        override fun task() {
            // init umeng
            println("init-umeng")
            // init sentry
            println("init-senrey")
            // init others
            println("init-other")
        }

    }


    class TaskB : Task("prepare-res") {
        override fun task() {
            // prepare res1
            println("prepare res1")
            println("prepare res2")
        }
    }

}
