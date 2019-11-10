package self.mybatis.noxml.entity;

import javax.persistence.Column;

public class UUIDUserDateDelFlagEnityt extends UUIDUserDateEnityt{

    @Column(name = "del_flag")
    protected String delFlag; 	// 删除标记（0：正常；1：删除；2：审核）

    /**
     * 删除标记（0：正常；1：删除；2：审核；）
     */
    public static final String DEL_FLAG_NORMAL = "0";
    public static final String DEL_FLAG_DELETE = "1";
    public static final String DEL_FLAG_AUDIT = "2";

    public UUIDUserDateDelFlagEnityt(){
        this.delFlag = DEL_FLAG_NORMAL;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }
}
