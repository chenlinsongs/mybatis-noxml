package self.mybatis.noxml.test.model;

import self.mybatis.noxml.test.MybatisTest;

import java.util.Map;

/**
 * @author linsong.chen
 * @date 2020-09-23 15:44
 */
public class TestModel {

    public String name;

    private String region;

    private int age;

    private Integer money;

    private boolean success;

    private Boolean build;

    private Map map;

    private MybatisTest mybatisTest;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Integer getMoney() {
        return money;
    }

    public void setMoney(Integer money) {
        this.money = money;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Boolean getBuild() {
        return build;
    }

    public void setBuild(Boolean build) {
        this.build = build;
    }

    public Map getMap() {
        return map;
    }

    public void setMap(Map map) {
        this.map = map;
    }

    public MybatisTest getMybatisTest() {
        return mybatisTest;
    }

    public void setMybatisTest(MybatisTest mybatisTest) {
        this.mybatisTest = mybatisTest;
    }

    @Override
    public String toString() {
        return "TestModel{" +
                "name='" + name + '\'' +
                ", region='" + region + '\'' +
                ", age=" + age +
                ", money=" + money +
                ", success=" + success +
                ", build=" + build +
                ", map=" + map +
                ", mybatisTest=" + mybatisTest +
                '}';
    }
}
