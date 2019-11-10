package self.mybatis.noxml.entity;

import javax.persistence.Column;

public class UUIDUserDateNameEntity extends UUIDUserDateEnityt{

    @Column(name = "name")
    protected String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static String getFieldName() {
        return "name";
    }
}
