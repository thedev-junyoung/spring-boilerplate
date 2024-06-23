package com.example.springboilerplate.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import javax.sql.DataSource;

@Configuration // 설정 파일임을 명시하는 어노테이션
@EnableTransactionManagement // 트랜잭션 관리를 위한 어노테이션
public class AppConfig {
    @Bean
    public PlatformTransactionManager transactionManager(DataSource dataSource) {
        // DataSourceTransactionManager 객체를 생성하여 반환
        // DataSourceTransactionManager: DataSource 를 사용하는 트랜잭션 매니저
        return new DataSourceTransactionManager(dataSource);
    }

    // DataSource Bean 정의
}
