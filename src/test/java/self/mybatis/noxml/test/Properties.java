package self.mybatis.noxml.test;

import self.mybatis.noxml.annotation.Primary;
import self.mybatis.noxml.entity.Entity;

import javax.persistence.Column;
import javax.persistence.Table;

@Table(name = "properties")
public class Properties implements Entity {

    @Primary(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;

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

    @Override
    public String toString() {
        return "Properties{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
