package self.mybatis.noxml.test;

import self.mybatis.noxml.annotation.MyBatisDao;
import self.mybatis.noxml.dao.CommonDao;

@MyBatisDao
public interface PropertiesDao extends CommonDao<Integer,Properties> {
}
