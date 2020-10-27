package com.weex.taskmanager;

import com.weex.manager.Task;
import com.weex.manager.TaskManager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by Weex on 2020-10-26.
 */
public class TaskTest {

    public static void main(String[] args) {
        InitSdkTask initSdkTask = new InitSdkTask("group-init");
        List<InitSdkTask> tasks = new ArrayList<>();
        for (int i = 0, size = 3; i < size; i++) {
            tasks.add(new InitSdkTask("init-" + i + "-task"));
        }
        initSdkTask.addTasks(tasks);
        PrepareResTask prepareResTask = new PrepareResTask("group-prepare");
        List<PrepareResTask> prepareTasks = new ArrayList<>();
        for (int i = 0, size = 3; i < size; i++) {
            prepareTasks.add(new PrepareResTask("prepare-" + i + "-task"));
        }
        prepareResTask.addTasks(prepareTasks);
        initSdkTask.before(prepareResTask);
        TaskManager.INSTANCE.launchTask(initSdkTask);
    }

    static class InitSdkTask extends Task {

        InitSdkTask(String name) {
            super(name);
        }

        @Override
        public void task() {
            try {
                System.out.println("thread # " + Thread.currentThread().getName() +
                        " in task " + getName());
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    static class PrepareResTask extends Task {

        PrepareResTask(String name) {
            super(name);
        }

        @Override
        public void task() {
            try {
                System.out.println("thread # " + Thread.currentThread().getName() +
                        " in task " + getName());
                TimeUnit.MILLISECONDS.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
