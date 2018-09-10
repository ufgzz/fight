package com.oofgz.fight.async;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import java.util.Random;
import java.util.concurrent.Future;

@Slf4j
@Component
public class AsyncTaskFuture {

    public static Random random = new Random();

    @Async("taskExecutor")
    public Future<String> run() throws Exception {
        long sleep = random.nextInt(10000);
        log.info("Future开始任务，需耗时：" + sleep + "毫秒");
        Thread.sleep(sleep);
        log.info("Future完成任务");
        return new AsyncResult<>(">>>Future完成任务<<<");
    }


}
