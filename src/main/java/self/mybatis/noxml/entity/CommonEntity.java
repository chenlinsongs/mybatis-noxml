package self.mybatis.noxml.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.xml.bind.annotation.XmlTransient;
import java.lang.reflect.ParameterizedType;

public abstract class CommonEntity<P> implements Entity{

    private static final long serialVersionUID = 1L;
    @JsonIgnore
    @XmlTransient
    protected boolean isNewRecord = false;

    @JsonIgnore
    @XmlTransient
    protected boolean mainThread = true;

    /**
     * 插入之前执行方法，子类实现
     */
    public abstract void preInsert();

    /**
     * 更新之前执行方法，子类实现
     */
    public abstract void preUpdate();

    /**
    * 是否是新数据插入
    * */
    public  boolean isNewRecord(){
        return isNewRecord;
    }

    public void setNewRecord(boolean newRecord){
        this.isNewRecord = newRecord;
    }

    /**
     * 主键类型
     * */
    @JsonIgnore
    @XmlTransient
    public P getPrimaryKeyType(){
        ParameterizedType parameterizedType = (ParameterizedType) this.getClass().getGenericSuperclass();
        if (parameterizedType.getActualTypeArguments().length > 0){
            Class clazz = (Class) parameterizedType.getActualTypeArguments()[0];
            return (P) clazz;
        }else return null;
    }

    public boolean isMainThread() {
        return mainThread;
    }

    public void setMainThread(boolean mainThread) {
        this.mainThread = mainThread;
    }
}
