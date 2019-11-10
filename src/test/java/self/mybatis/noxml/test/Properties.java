package self.mybatis.noxml.test;

import self.mybatis.noxml.annotation.Primary;
import self.mybatis.noxml.entity.CommonEntity;

import javax.persistence.Column;
import javax.persistence.Table;

@Table(name = "properties")
public class Properties extends CommonEntity<Integer> {

    @Primary(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "sec")
    private String sec;

    @Column(name = "value")
    private String value;

    @Column(name = "age")
    private Long age;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSec() {
        return sec;
    }

    public void setSec(String sec) {
        this.sec = sec;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Long getAge() {
        return age;
    }

    public void setAge(Long age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "Properties{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", sec='" + sec + '\'' +
                ", value='" + value + '\'' +
                ", age=" + age +
                '}';
    }

    @Override
    public void preInsert() {

    }

    @Override
    public void preUpdate() {

    }

    public static String getFieldName(){
        return "name";
    }

    public static String getFieldSec(){
        return "sec";
    }

    public static String getFieldAge(){
        return "age";
    }
}
