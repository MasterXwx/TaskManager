package com.weex.manager

/**
 * Created by Weex on 2020-10-26.
 *
 */
interface ITask {

    fun getCurState(): Int

    fun singleTask(): Boolean
}
