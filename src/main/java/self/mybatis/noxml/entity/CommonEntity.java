package self.mybatis.noxml.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.xml.bind.annotation.XmlTransient;
import java.lang.reflect.ParameterizedType;

public abstract class CommonEntity<P> implements Entity{

    private static final long serialVersionUID = 1L;

    /**
     * 是否是新记录（默认：false），调用setNewRecord()设置新记录，使用自定义ID。
     * 设置为true，ID不会为snowflake的id产生方案，需从手动传入。
     */
    @JsonIgnore
    @XmlTransient
    protected boolean isNewRecord = false;

    /**
     * 有些子线程中无法获取用户id,可以将mainThread设置我false,然后手动设置用户id
     * */
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


    public boolean isMainThread() {
        return mainThread;
    }

    public void setMainThread(boolean mainThread) {
        this.mainThread = mainThread;
    }


//    /*
//     * CommonEntity不做成泛型了，因为mybatis没法解析实体泛型的实际值，所以这个方法没有意义
//     * */
//    /**
//     * 主键类型
//     * */
//    @JsonIgnore
//    @XmlTransient
//    public P getPrimaryKeyType(){
//        ParameterizedType parameterizedType = (ParameterizedType) this.getClass().getGenericSuperclass();
//        if (parameterizedType.getActualTypeArguments().length > 0){
//            Class clazz = (Class) parameterizedType.getActualTypeArguments()[0];
//            return (P) clazz;
//        }else return null;
//    }
}
