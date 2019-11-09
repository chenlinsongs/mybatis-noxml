package self.mybatis.noxml;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import self.mybatis.noxml.config.*;

@Configuration
@EnableConfigurationProperties({MybatisProperties.class, DataSourceProperties.class})
@Import({MybatisSqlSessionFactoryConfigure.class,
        MyBatisStarterConfigure.class,
        DataSourceConfigure.class})
public class MybatisNoXmlAutoConfigure {
}
