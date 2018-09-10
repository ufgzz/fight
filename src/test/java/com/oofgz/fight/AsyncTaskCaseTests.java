package com.oofgz.fight;

import com.oofgz.fight.async.AsyncTask;
import com.oofgz.fight.async.AsyncTaskPool;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.Future;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class AsyncTaskCaseTests {

    @Autowired
    private AsyncTask asyncTask;

    @Autowired
    private AsyncTaskPool asyncTaskPool;

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


}
