package self.mybatis.noxml.entity;

import javax.persistence.Column;

public class IdUserDateDelFlagEntity<P> extends IdUserDateEntity<P> {
    private static final long serialVersionUID = 1L;

    @Column(name = "del_flag")
    protected String delFlag; 	// 删除标记（0：正常；1：删除；2：审核）

    /**
     * 删除标记（0：正常；1：删除；2：审核；）
     */
    public static final String DEL_FLAG_NORMAL = "0";
    public static final String DEL_FLAG_DELETE = "1";
    public static final String DEL_FLAG_AUDIT = "2";

    public IdUserDateDelFlagEntity(){
        super();
        this.delFlag = DEL_FLAG_NORMAL;
    }
    public IdUserDateDelFlagEntity(P id){
        super(id);
        this.delFlag = DEL_FLAG_NORMAL;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    public static String getFieldDelFlag() {
        return "delFlag";
    }

}
