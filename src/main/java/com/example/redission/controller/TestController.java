package com.example.redission.controller;

import org.redisson.api.RLock;
import org.redisson.api.RSemaphore;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 * @author wangjianhua
 * @date 2021/6/15/015 10:41
 */
@RestController
public class TestController {

    @Autowired
    private RedissonClient redissonClient;
    @GetMapping("test")
    public String testLock(){
        /*
        reddison的可重入锁会阻塞线程 需要等待其他锁释放
         */
        //获取锁
        RLock lock = redissonClient.getLock("test-lock");
        //加锁
        lock.lock();
        //执行业务的时间一定要小于锁的自动过期时间 否则手动释放锁会报错
        //lock.lock(8, TimeUnit.SECONDS);
        try{

            System.out.println("加锁成功,线程id:"+Thread.currentThread().getId());
            Thread.sleep(10000);
        }catch (InterruptedException e){
            e.printStackTrace();
        }finally {
            //释放锁
            lock.unlock();
            System.out.println("释放锁成功,线程id:"+Thread.currentThread().getId());
        }

        return "test lock ok";

    }

    @GetMapping("park")
    public String park() throws InterruptedException {
        //获取停车场信号量
        RSemaphore park = redissonClient.getSemaphore("park");
        //获取一个车位
        park.acquire();
        return "获取车位成功";
    }

    @GetMapping("leave")
    public String leave(){
        RSemaphore park = redissonClient.getSemaphore("park");
        //释放车位
        park.release();
        /*
        多次释放信号量 会一直增加计数 而不是原来的数字
         */

        return "释放车位成功";
    }

    @GetMapping("tryPark")
    public String tryPark(){
        RSemaphore park = redissonClient.getSemaphore("park");
        boolean b;
        try{
             b= park.tryAcquire(3, TimeUnit.SECONDS);
        //   park.tryAcquireAsync()  该方法同样不会阻塞
        }catch (InterruptedException e){
            return e.getMessage();
        }
        if(b){
            return "停车成功";
        }
        else {
            return "停车失败";
        }

    }
}
