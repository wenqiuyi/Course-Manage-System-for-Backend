package com.coursemanage.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
@MapperScan({
        "com.coursemanage.module.announcement.mapper",
        "com.coursemanage.module.log.mapper",
        "com.coursemanage.module.login.mapper",
        "com.coursemanage.module.comment.mapper",
        "com.coursemanage.module.student.mapper",
        "com.coursemanage.module.teacher.mapper",
        "com.coursemanage.module.courseclass.mapper",
        "com.coursemanage.module.course.mapper",
        "com.coursemanage.module.accountmanage.mapper",
        "com.coursemanage.module.checkin.mapper",
        "com.coursemanage.module.notice.mapper",
        "com.coursemanage.module.project.mapper",
        "com.coursemanage.pojo.mapper"
})
public class MybatisConfig {
    @Bean
    public MybatisSqlSessionFactoryBean sqlSessionFactory(DataSource dataSource) throws Exception {
        MybatisSqlSessionFactoryBean sessionFactory = new MybatisSqlSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);

        // 配置驼峰命名转换
        MybatisConfiguration configuration = new MybatisConfiguration();
        configuration.setMapUnderscoreToCamelCase(true);
        configuration.setCacheEnabled(true);
        sessionFactory.setConfiguration(configuration);

        return sessionFactory;
    }
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        // 添加分页插件（适配MySQL）
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }
}
