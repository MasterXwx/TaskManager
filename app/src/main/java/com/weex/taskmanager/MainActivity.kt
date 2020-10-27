package com.weex.taskmanager


import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger

const val TAG = "TAG"

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun test() {
        executor().submit(section1())
    }


    fun section1(): NamedTask {
        return object : NamedTask() {
            override fun doTask() {
                Log.d(TAG, "init Umeng")
                Log.d(TAG, "init Sentry")
                try {
                    Log.d(TAG, "do delay")
                    TimeUnit.SECONDS.sleep(2)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }
        }
    }

    fun executor(): ExecutorService {
        return Executors.newFixedThreadPool(
            Runtime.getRuntime().availableProcessors()
        ) { r ->
            val threadNo = AtomicInteger(1)

            Thread(r, "custom thread ${threadNo.getAndIncrement()}")
        }
    }

}
