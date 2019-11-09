package self.mybatis.noxml.test;



import self.mybatis.noxml.annotation.MyBatisDao;

import java.util.List;

@MyBatisDao
public interface MybatisTestDao  {
    List<MybatisTest> get();
}
