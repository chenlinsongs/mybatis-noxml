package self.mybatis.noxml;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MybatisNoXmlApplication {
    public static void main(String[] args) {
        new SpringApplication(MybatisNoXmlApplication.class).run(args);
    }
}
