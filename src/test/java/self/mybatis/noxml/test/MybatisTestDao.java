package self.mybatis.noxml.test;



import org.apache.ibatis.annotations.Param;
import self.mybatis.noxml.annotation.MyBatisDao;
import self.mybatis.noxml.test.model.TestModel;

import java.util.Collection;
import java.util.List;

@MyBatisDao
public interface MybatisTestDao  {
    List<MybatisTest> get();
    List<MybatisTest>  getByIdInt(@Param(value = "id")int id);

    List<MybatisTest> getById(@Param(value = "id") String id);

    List<MybatisTest> getByName(@Param(value = "name") String name);

    List<MybatisTest> getByNamePrepare(@Param(value = "name") String name);



    List<MybatisTest> testModel(@Param(value = "testModel") TestModel name);

    List<MybatisTest> testMap(@Param(value = "name")String testName,TestModel testModel,int age);

    List<MybatisTest>  testCollection(@Param(value = "idList") Collection idList);


}
