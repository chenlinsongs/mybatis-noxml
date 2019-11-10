package self.mybatis.noxml.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import self.mybatis.noxml.example.CommonExample;
import self.mybatis.noxml.dao.CommonDao;
import self.mybatis.noxml.entity.CommonEntity;
import self.mybatis.noxml.page.Page;

import java.util.List;

/**
 * P 主键类型
 * T entity实体
 * D dao接口
 * */
public class CommonService<P,T extends CommonEntity<P>,D extends CommonDao<P,T>> {

    protected Logger logger = LoggerFactory.getLogger(getClass());
    /**
     * 持久层对象
     */
    @Autowired
    protected D dao;

    public long countByExample(CommonExample example){
        try {
            Page page = example.getPage();
            long count;
            if(page != null){
                example.setPage(null);
                count = dao.countByExample(example);
                example.setPage(page);
            }else {
                count = dao.countByExample(example);
            }
            return count;
        }catch (Exception e){
            logger.error("countByExample:"+e.getMessage(),e);
            throw e;
        }
    }

    public long deleteByExample(CommonExample example){
        try {
            return dao.deleteByExample(example);
        }catch (Exception e){
            logger.error("deleteByExample:"+e.getMessage(),e);
            throw e;
        }
    }

    public int deleteByPrimaryKey(P id){
        try {
            return dao.deleteByPrimaryKey(id);
        }catch (Exception e){
            logger.error("deleteByPrimaryKey:"+e.getMessage(),e);
            throw e;
        }
    }

    public int insert(T record){
        try {
            record.preInsert();
            return dao.insert(record);
        }catch (Exception e){
            logger.error("insert:"+e.getMessage(),e);
            throw e;
        }
    }

    public int insertSelective(T record){
        try {
            record.preInsert();
            return dao.insertSelective(record);
        }catch (Exception e){
            logger.error("insertSelective:"+e.getMessage(),e);
            throw e;
        }
    }

    public List<T> selectByExample(CommonExample example){
        try {
            return dao.selectByExample(example);
        }catch (Exception e){
            logger.error("selectByExample:"+e.getMessage(),e);
            throw e;
        }
    }

    public T selectByPrimaryKey(P id){
        try {
            return dao.selectByPrimaryKey(id);
        }catch (Exception e){
            logger.error("selectByPrimaryKey:"+e.getMessage(),e);
            throw e;
        }
    }

    public int updateByExampleSelective(T record, CommonExample example){
        try {
            record.preUpdate();
            return dao.updateByExampleSelective(record,example);
        }catch (Exception e){
            logger.error("updateByExampleSelective:"+e.getMessage(),e);
            throw e;
        }
    }

    public int updateByExample(T record, CommonExample example){
        try {
            record.preUpdate();
            return dao.updateByExample(record,example);
        }catch (Exception e){
            logger.error("updateByExample:"+e.getMessage(),e);
            throw e;
        }
    }

    public int updateByPrimaryKeySelective(T record){
        try {
            record.preUpdate();
            return dao.updateByPrimaryKeySelective(record);
        }catch (Exception e){
            logger.error("updateByPrimaryKeySelective:"+e.getMessage(),e);
            throw e;
        }
    }

    public int updateByPrimaryKey(T record){
        try {
            record.preUpdate();
            return dao.updateByPrimaryKey(record);
        }catch (Exception e){
            logger.error("updateByPrimaryKey:"+e.getMessage(),e);
            throw e;
        }
    }

}
