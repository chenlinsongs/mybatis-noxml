package self.mybatis.noxml.test;


import self.mybatis.noxml.entity.Entity;

public class MybatisTest implements Entity {

    private int id;

    private String name;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "MybatisTest{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
