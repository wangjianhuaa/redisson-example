package com.example.redission;

import com.example.redission.config.CustomRedissonConfig;
import org.junit.jupiter.api.Test;
import org.redisson.api.RLock;
import org.redisson.api.RReadWriteLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.concurrent.TimeUnit;

@SpringBootTest
class RedissionApplicationTests {
    AnnotationConfigApplicationContext applicationContext;

    @Test
    void contextLoads() {
        applicationContext = new AnnotationConfigApplicationContext(CustomRedissonConfig.class);
        RedissonClient redisson = (RedissonClient)applicationContext.getBean("redisson");
        System.out.println(redisson.getConfig());
    }

    @Autowired
    RedissonClient redissonClient;
    @Test
    void testAutowired(){
        System.out.println(redissonClient);
    }

    @Test
    void rLock(){
        RLock lock = redissonClient.getLock("anyLock");
        // RedissonLock 锁操作类
        System.out.println(lock);
    }

    @Test
    void readWriteLock(){
        //读写锁操作类
        RReadWriteLock readWriteLock = redissonClient.getReadWriteLock("anyReadWriteLock");
        readWriteLock.readLock().lock();
        readWriteLock.writeLock().lock();

        //10s 后自动释放 无需手动释放锁
        readWriteLock.readLock().lock(10, TimeUnit.SECONDS);
        readWriteLock.writeLock().lock(10,TimeUnit.SECONDS);

        try{
            //尝试加锁  最多等待100s 上锁后10s自动解锁
            boolean res1 = readWriteLock.readLock().tryLock(100, 10, TimeUnit.SECONDS);
            boolean res2 = readWriteLock.writeLock().tryLock(100,10,TimeUnit.SECONDS);
        }catch (InterruptedException e){
            e.printStackTrace();
        }

    }

}
