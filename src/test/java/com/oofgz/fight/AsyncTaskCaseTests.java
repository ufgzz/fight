package com.oofgz.fight;

import com.oofgz.fight.async.AsyncTask;
import com.oofgz.fight.async.AsyncTaskFuture;
import com.oofgz.fight.async.AsyncTaskPool;
import com.oofgz.fight.async.AsyncTaskRedisPool;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class AsyncTaskCaseTests {

    @Autowired
    private AsyncTask asyncTask;

    @Autowired
    private AsyncTaskPool asyncTaskPool;

    @Autowired
    private AsyncTaskRedisPool asyncTaskRedisPool;

    @Autowired
    private AsyncTaskFuture asyncTaskFuture;

    /**
     * 同步调用：几个方法按顺序执行
     * 异步调用：在顺序执行时，不等待异步调用的语句返回结果就执行后面的程序
     * 同步调用虽然顺利的执行完了三个任务，但是可以看到执行时间比较长，
     * 若这三个任务本身之间不存在依赖关系，可以并发执行的话，
     * 同步调用在执行效率方面就比较差，可以考虑通过异步调用的方式来并发执行
     * @throws Exception
     */
    @Test
    public void asyncTaskTest() throws Exception {

        long start = System.currentTimeMillis();

        Future<String> task1 = asyncTask.doTaskOne();
        Future<String> task2 = asyncTask.doTaskTwo();
        Future<String> task3 = asyncTask.doTaskThree();

        while(true) {
            if(task1.isDone() && task2.isDone() && task3.isDone()) {
                // 三个任务都调用完成，退出循环等待
                break;
            }
            Thread.sleep(1000);
        }

        long end = System.currentTimeMillis();

        System.out.println("任务全部完成，总耗时：" + (end - start) + "毫秒");

    }


    @Test
    public void asyncTaskPoolTest() throws Exception {

        asyncTaskPool.doTaskOne();
        asyncTaskPool.doTaskTwo();
        asyncTaskPool.doTaskThree();
        //阻塞当前调用线程，直到某线程完全执行才调用线程才继续执行
        Thread.currentThread().join();

    }


    /**
     * .@SneakyThrows 在代码中，使用 try，catch来捕捉一些异常，而你不想对他处理，只想抛出去
     * JedisConnectionException: Could not get a resource from the pool
     * 模拟高并发情况下ShutDown的情况
     * 通过for循环往上面定义的线程池中提交任务，由于是异步执行，在执行过程中，利用System.exit(0)来关闭程序，
     * 此时由于有任务在执行，就可以观察这些异步任务的销毁与Spring容器中其他资源的顺序是否安全
     * @throws Exception
     */
    @Test
    @SneakyThrows
    public void asyncTaskRedisPoolTest() throws Exception {

        for (int i = 0; i < 10000; i++) {
            asyncTaskRedisPool.doTaskOne();
            asyncTaskRedisPool.doTaskTwo();
            asyncTaskRedisPool.doTaskThree();

            if (i == 9999) {
                System.exit(0);
            }
        }


    }


    /**
     *
     * 判断任务是否完成；
     * 能够中断任务；
     * 能够获取任务执行结果。
     *
     * @throws Exception
     */
    @Test
    public void asyncTaskFutureTest() throws Exception {

        Future<String> futureResult = asyncTaskFuture.run();
        //我们在get方法中还定义了该线程执行的超时时间，
        // 通过执行这个测试我们可以观察到执行时间超过5秒的时候，
        // 这里会抛出超时异常，该执行线程就能够因执行超时而释放回线程池，不至于一直阻塞而占用资源
        String result = futureResult.get(5, TimeUnit.SECONDS);
        log.info(result);

    }


}
