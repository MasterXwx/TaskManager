package com.weex.manager

/**
 * Created by Weex on 2020-10-27.
 *
 */
interface TaskListener {

    fun onStart(task: Task)

    fun onFinished(task: Task)

    fun onGroupFinished(group: Task)
}
