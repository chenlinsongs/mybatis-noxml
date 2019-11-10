package self.mybatis.noxml.entity;


import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import self.mybatis.noxml.annotation.Primary;
import self.mybatis.noxml.idgen.IdGenerate;

public abstract class IdEntity extends CommonEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 实体编号（唯一标识）
	 */
	@Primary(name = "id")
	protected Long id;

	public IdEntity() {
	}

	public IdEntity(Long id) {
		this();
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public static String getFieldId() {
		return "id";
	}


    @Override
    public boolean equals(Object obj) {
        if (null == obj) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (!getClass().equals(obj.getClass())) {
            return false;
        }
        IdEntity that = (IdEntity) obj;
        return null == this.getId() ? false : this.getId().equals(that.getId());
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }

	/**
	 * 插入之前执行方法
	 */
	@Override
	public void preInsert(){
		// 不限制ID为snowflake的id产生方案，调用setIsNewRecord()使用自定义ID
		if (!this.isNewRecord){
			setId(IdGenerate.nextId());
		}
	}

	/**
	 * 更新之前执行方法，不需要做什么
	 */
	@Override
	public void preUpdate(){
	}

	@Override
	public boolean isNewRecord() {
//		Class clazz = (Class) this.getPrimaryKeyType();
//		if (String.class.isAssignableFrom(clazz)){
//			return isNewRecord || StringUtils.isBlank((String) getId());
//		}else if (Integer.class.isAssignableFrom(clazz)){
		return isNewRecord || getId() != null || getId() != 0;
//		} else throw new RuntimeException("不支持的主键类型");
	}
}
