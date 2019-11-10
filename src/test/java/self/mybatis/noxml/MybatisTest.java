package self.mybatis.noxml;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import self.mybatis.noxml.example.CommonExample;
import self.mybatis.noxml.page.Page;
import self.mybatis.noxml.test.MybatisTestDao;
import self.mybatis.noxml.test.Properties;
import self.mybatis.noxml.test.PropertiesDao;
import self.mybatis.noxml.test.PropertiesService;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MybatisTest {

    Logger logger = LoggerFactory.getLogger(MybatisTest.class);

    @Autowired
    MybatisTestDao mybatisTestDao;

    @Autowired
    PropertiesDao propertiesDao;

    @Autowired
    PropertiesService propertiesService;

    @Test
    public void mybatisTestDaoTest(){
        Assert.assertNotNull(mybatisTestDao.get());
        Assert.assertTrue(mybatisTestDao.get().size() > 0);
        logger.info("----++++:"+mybatisTestDao.get().get(0).toString());
    }

    @Test
    public void propertiesDaoTest(){
        //insert
        Properties properties = new Properties();
        properties.setName("2343"+Math.random());
        int row = propertiesDao.insert(properties);
        Assert.assertTrue( row == 1);

        properties.setAge(10L);
        properties.setSec("test");
        properties.setName("4353");
        row = propertiesDao.insertSelective(properties);
        Assert.assertTrue( row == 1);

        CommonExample example = new CommonExample(Properties.class);
        example.createCriteria().andEqualTo(Properties.getFieldAge(),"100");
        List list = propertiesDao.selectByExample(example);
        Assert.assertNotNull(list);
        Assert.assertTrue(list.size() == 1);
        logger.info("----++++:"+list.get(0).toString());

        long count = propertiesDao.countByExample(example);

        Assert.assertTrue( count == 1);

        example = new CommonExample(Properties.class);
        example.setPage(new Page(1,1));
        List list1 = propertiesDao.selectByExample(example);
        Assert.assertTrue( list1.size() == 1);
    }

//    @Test
//    public void propertiesDaoConditionTest(){
//        CommonExample example = new CommonExample(Properties.class);
//        example.createCriteria().andLike(Properties.getFieldName(),"ch%");
//        List list = propertiesDao.selectByExample(example);
//        Assert.assertNotNull(list);
//        Assert.assertTrue(list.size() > 0);
//        logger.info("----++++:"+list.get(0).toString());
//        long count = propertiesDao.countByExample(example);
//        logger.info("count----++++:"+count);
//    }

//    @Test
//    public void propertiesServiceTest(){
//        CommonExample example = new CommonExample(Properties.class);
//        List list = propertiesService.selectByExample(example);
//        Assert.assertNotNull(list);
//        Assert.assertTrue(list.size() > 0);
//        logger.info("service----++++:"+list.get(0).toString());
//    }
}
