package self.mybatis.noxml;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import self.mybatis.noxml.test.MybatisTestDao;
import self.mybatis.noxml.test.Properties;
import self.mybatis.noxml.test.PropertiesDao;
import self.mybatis.noxml.test.model.TestModel;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MybatisTest {

    Logger logger = LoggerFactory.getLogger(MybatisTest.class);

    @Autowired
    MybatisTestDao mybatisTestDao;

    @Autowired
    PropertiesDao propertiesDao;

    @Test
    public void mybatisTestDaoTest(){
        Assert.assertNotNull(mybatisTestDao.get());
        Assert.assertTrue(mybatisTestDao.get().size() > 0);
        logger.error(mybatisTestDao.get().get(0).toString());
    }

    /**
     * sql注入测试1 单引号
     * 测试时，mybatis-config.xml文件不配置防sql注入拦截器则会产生sql注入
     * MyBatisSqlInjectionInterceptor
     *
     * 配置以后则不会造成sql注入
     * */
    @Test
    public void mybatisTestDaoGetById1(){
        Assert.assertNotNull(mybatisTestDao.getById("'" +" or '1'='1"));
    }

    /**
     * sql注入测试2 双引号
     * 测试时，mybatis-config.xml文件不配置防sql注入拦截器则会产生sql注入
     * MyBatisSqlInjectionInterceptor
     *
     * 配置以后则不会造成sql注入
     * */
    @Test
    public void mybatisTestDaoGetById2(){
        Assert.assertNotNull(mybatisTestDao.getByName("\"" +" or \"1\"=\"1"));
    }

    /**
     * sql注入测试2 单引号,使用mybatis预编译语句，不会产生sql注入
     * 测试之前先去掉MyBatisSqlInjectionInterceptor拦截器
     * */
    @Test
    public void mybatisTestDaoGetByNamePrepare1(){
        Assert.assertNotNull(mybatisTestDao.getByNamePrepare("'" +" or '1'='1"));
    }

    /**
     * sql注入测试2 双引号,使用mybatis预编译语句，不会产生sql注入
     * 测试之前先去掉MyBatisSqlInjectionInterceptor拦截器
     * */
    @Test
    public void mybatisTestDaoGetByNamePrepare2(){
        Assert.assertNotNull(mybatisTestDao.getByNamePrepare("\"" +" or \"1\"=\"1"));
    }

    /**
     * sql注入测试1，sql注入将失败， 参数是实体类
     * 测试之前确保添加MyBatisSqlInjectionInterceptor拦截器
     * */
    @Test
    public void mybatisTestDaoGetByNameModel(){
        TestModel testModel = new TestModel();
        testModel.setName("'" +" or '1'='1");
        testModel.setAge(1);
        testModel.setBuild(false);
        Assert.assertNotNull(mybatisTestDao.testModel(testModel));
    }

    /**
     * sql注入测试1，测试参数有string,int,和实体类的情况
     * */
    @Test
    public void mybatisTestDaoGetByNameMap(){
        TestModel testModel = new TestModel();
        testModel.setName("'" +" or '1'='1");
        testModel.setAge(1);
        testModel.setBuild(false);
        Assert.assertNotNull(mybatisTestDao.testMap("'" +" or '1'='1",testModel,1));
    }

    /**
     * 测试参数为int时，拦截器是否正常
     * */
    @Test
    public void mybatisTestDaoGetByIdInt(){
        Assert.assertNotNull(mybatisTestDao.getByIdInt(1));
    }

    /**
     * 测试参数是集合时，拦截器是否正常
     * */
    @Test
    public void mybatisTestDaoGetByCollection(){
        List<Integer> integerList = new ArrayList<>();
        integerList.add(1);
        Assert.assertNotNull(mybatisTestDao.testCollection(integerList));
    }

    /**
     * 测试参数是集合时，拦截器是否正常
     * */
    @Test
    public void mybatisTestDaoGetByCollection2(){
        List<String> stringList = new ArrayList<>();
        stringList.add("'" +" or '1'='1");
        Assert.assertNotNull(mybatisTestDao.testCollection(stringList));
    }



    @Test
    public void propertiesDaoTest(){
        CommonExample example = new CommonExample(Properties.class);
        List list = propertiesDao.selectByExample(example);
        Assert.assertNotNull(list);
        Assert.assertTrue(list.size() > 0);
        logger.error(list.get(0).toString());
    }
}
