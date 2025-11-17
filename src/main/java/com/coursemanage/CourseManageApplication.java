package com.coursemanage;

import com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication(
        exclude = {
                MybatisPlusAutoConfiguration.class,  // 排除 MyBatis-Plus 自动配置，阻止生成 ddlApplicationRunner
        }
)

@MapperScan("com.coursemanage.module.*.mapper")
public class CourseManageApplication {

    public static void main(String[] args) {
        SpringApplication.run(CourseManageApplication.class, args);
    }

}
