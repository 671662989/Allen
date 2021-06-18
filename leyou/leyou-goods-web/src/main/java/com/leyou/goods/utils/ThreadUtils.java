package com.leyou.goods.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * create by: cdy
 * description:$
 * create time: $ $
 * <p>
 * $
 *
 * @return $
 */
public class ThreadUtils {

    private final static ExecutorService EXECUTOR_SERVICE= Executors.newFixedThreadPool(10);

    public static void execute(Runnable runnable){
        EXECUTOR_SERVICE.submit(runnable);
    }
}
