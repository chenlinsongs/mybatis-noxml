package self.mybatis.noxml.entity;

import self.mybatis.noxml.annotation.Primary;
import self.mybatis.noxml.idgen.IdGenerate;

public class UUIDEntity extends CommonEntity{

    @Primary(name = "id")
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public static String getFieldId() {
        return "id";
    }

    /**
     * 生成uuid
     * */
    @Override
    public void preInsert() {
        // 不限制ID为UUID，调用setIsNewRecord()使用自定义ID
        if (!this.isNewRecord){
            setId(IdGenerate.uuid());
        }
    }

    /**
     * 不需要做什么
     * */
    @Override
    public void preUpdate() {

    }
}
