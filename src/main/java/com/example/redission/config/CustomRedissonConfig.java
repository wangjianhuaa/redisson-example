package com.example.redission.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

/**
 * 单节点redis配置
 * @author wangjianhua
 * @date 2021/6/15/015 9:56
 */
@Configuration
public class CustomRedissonConfig {

    /**
     * 规定关闭方法 注入操作类ReddisonClient 其实用的是它的实现类
     * @return RedissonClient
     * @throws IOException io异常
     */
    @Bean(destroyMethod = "shutdown")
    public RedissonClient redisson () throws IOException {
        //创建配置
        Config config = new Config();
        //集群模式 加入redis集群地址 可多写
        //config.useClusterServers().addNodeAddress("","");
        //根据config创建出 RedissonClient实例
        config.useSingleServer().setAddress("redis://127.0.0.1:6379");
        return Redisson.create(config);
    }
}
