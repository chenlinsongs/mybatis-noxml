package self.mybatis.noxml.entity;


import javax.persistence.Column;
import java.util.Date;

public class UserDateDelFlagEntity extends CommonEntity {

    @Column(name = "create_by")
    protected String createBy;    // 创建者

    @Column(name = "create_date")
    protected Date createDate;    // 创建日期

    @Column(name = "update_by")
    protected String updateBy;    // 更新者

    @Column(name = "update_date")
    protected Date updateDate;    // 更新日期

    @Column(name = "del_flag")
    protected String delFlag; 	// 删除标记（0：正常；1：删除；2：审核）

    /**
     * 删除标记（0：正常；1：删除；2：审核；）
     */
    public static final String DEL_FLAG_NORMAL = "0";
    public static final String DEL_FLAG_DELETE = "1";
    public static final String DEL_FLAG_AUDIT = "2";

    public UserDateDelFlagEntity(){
        this.delFlag = DEL_FLAG_NORMAL;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }


    public static String getFieldCreateBy() {
        return "createBy";
    }

    public static String getFieldCreateDate() {
        return "createDate";
    }

    public static String getFieldUpdateBy() {
        return "updateBy";
    }

    public static String getFieldUpdateDate() {
        return "updateDate";
    }

    public static String getFieldDelFlag() {
        return "delFlag";
    }


    /**
     * 插入之前执行方法，需要手动调用
     */
    @Override
    public void preInsert(){
        this.updateDate = new Date();
        this.createDate = this.updateDate;
    }

    /**
     * 更新之前执行方法，需要手动调用
     */
    @Override
    public void preUpdate(){
        this.updateDate = new Date();
    }
}
