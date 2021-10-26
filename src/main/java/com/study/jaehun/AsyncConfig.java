package com.study.jaehun;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurerSupport;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

//Configuration 선언 필수
@Configuration
// EnableAsync 선언 필수. 여기서 해당 플젝의 AsyncConfigurer를 공통선언/사용
@EnableAsync
public class AsyncConfig extends AsyncConfigurerSupport {

    @Override
    public Executor getAsyncExecutor(){
        ThreadPoolTaskExecutor asyncExecutor = new ThreadPoolTaskExecutor();
        asyncExecutor.setCorePoolSize(10); // thread의 코어 수를 설정
        asyncExecutor.setMaxPoolSize(15);  //최대 동작하는 Async Thread 수
        asyncExecutor.setQueueCapacity(10); // 최대 동작 Thread를 초과할 시, 보관되는 Queue의 수
        asyncExecutor.setThreadNamePrefix("TESTAsync-");  // Async Thread가 동작할 때 표시되는 Thread Name
        asyncExecutor.initialize();  //위 설정의
        return asyncExecutor;
    }
}

