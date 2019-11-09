package self.mybatis.noxml.config;

import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import self.mybatis.noxml.annotation.MyBatisDao;
import self.mybatis.noxml.utils.PropertiesUtil;

/**
 * MyBatis扫描接口
 */
@Configuration
@AutoConfigureAfter(MybatisSqlSessionFactoryConfigure.class)//TODO 注意，由于MapperScannerConfigurer执行的比较早，所以必须有下面的注解
public class MyBatisStarterConfigure {


    @Bean(name="mapperScannerConfigurer")
    public MapperScannerConfigurer mapperScannerConfigurer() {
        MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
        mapperScannerConfigurer.setSqlSessionFactoryBeanName("sqlSessionFactory");
        mapperScannerConfigurer.setBasePackage(PropertiesUtil.getProperty("mybatis.basePackage"));
        mapperScannerConfigurer.setAnnotationClass(MyBatisDao.class);
        return mapperScannerConfigurer;
    }
}
