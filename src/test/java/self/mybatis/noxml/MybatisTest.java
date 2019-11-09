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
        logger.info("----"+mybatisTestDao.get().get(0).toString());
    }

    @Test
    public void propertiesDaoTest(){
        CommonExample example = new CommonExample(Properties.class);
        List list = propertiesDao.selectByExample(example);
        Assert.assertNotNull(list);
        Assert.assertTrue(list.size() > 0);
        logger.info("----"+list.get(0).toString());
    }
}
