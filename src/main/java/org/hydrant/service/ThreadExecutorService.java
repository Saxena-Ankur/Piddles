package org.hydrant.service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by asaxena on 2/25/17.
 */
public abstract class ThreadExecutorService {

    private static ExecutorService executorService;

    static{
        executorService  = Executors.newCachedThreadPool();
    }

    public static void executeThread(Runnable runnable){
        executorService.submit(runnable);
    }

    public static void shutDown(){
        executorService.shutdown();
    }

}
