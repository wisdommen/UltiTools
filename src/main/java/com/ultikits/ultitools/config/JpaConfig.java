package com.ultikits.ultitools.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.persistence.EntityManagerFactory;
import java.util.Properties;

@Configuration
@ComponentScan(basePackages = {"dao"})
// 指定Repository所在的包
@EnableJpaRepositories(basePackages = {"dao"})
public class JpaConfig {

    // 名字必须是entityManagerFactory,或者把@bean中name属性设置为entityManagerFactory
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        // 设置数据库(如果在hibernate中配置了连接池,则不需要设置)
//        em.setDataSource(dataSource());
        // 指定Entity所在的包
        em.setPackagesToScan("dao");
        JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        // 配置属性
        Properties properties = new Properties();
        properties.setProperty("hibernate.connection.driver_class", "oracle.jdbc.OracleDriver");
        properties.setProperty("hibernate.connection.url", "jdbc:oracle:thin:@192.168.0.21:1521:brcw");
        properties.setProperty("hibernate.connection.username", "BRCW");
        properties.setProperty("hibernate.connection.password", "123");
        properties.setProperty("hibernate.connection.provider_class", "com.zaxxer.hikari.hibernate.HikariConnectionProvider");
        properties.setProperty("hibernate.show_sql", "true");
        properties.setProperty("format_sql", "true");
        em.setJpaProperties(properties);
        return em;
    }

    // 名字必须是transactionManager,或者把@bean中name属性设置为transactionManager
    @Bean
    public PlatformTransactionManager transactionManager(
            EntityManagerFactory emf) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(emf);
        return transactionManager;
    }

}
