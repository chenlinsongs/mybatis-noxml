package self.mybatis.noxml.aop;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.ibatis.binding.MapperProxy;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.aop.framework.ReflectiveMethodInvocation;
import org.springframework.stereotype.Component;
import self.mybatis.noxml.annotation.Primary;
import javax.persistence.Column;
import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Aspect
public class CommonDaoAspect {

    @Pointcut("execution(* self.mybatis.noxml.dao.CommonDao.insert(..))")
    public void insert(){}

    @Pointcut("execution(* self.mybatis.noxml.dao.CommonDao.countByExample(..))")
    public void countByExample(){}

    @Pointcut("execution(* self.mybatis.noxml.dao.CommonDao.deleteByExample(..))")
    public void deleteByExample(){}

    @Pointcut("execution(* self.mybatis.noxml.dao.CommonDao.deleteByPrimaryKey(..))")
    public void deleteByPrimaryKey(){}

    @Pointcut("execution(* self.mybatis.noxml.dao.CommonDao.insertSelective(..))")
    public void insertSelective(){}

    @Pointcut("execution(* self.mybatis.noxml.dao.CommonDao.selectByExample(..))")
    public void selectByExample(){}

    @Pointcut("execution(* self.mybatis.noxml.dao.CommonDao.selectByPrimaryKey(..))")
    public void selectByPrimaryKey(){}

    @Pointcut("execution(* self.mybatis.noxml.dao.CommonDao.updateByExampleSelective(..))")
    public void updateByExampleSelective(){}

    @Pointcut("execution(* self.mybatis.noxml.dao.CommonDao.updateByExample(..))")
    public void updateByExample(){}

    @Pointcut("execution(* self.mybatis.noxml.dao.CommonDao.updateByPrimaryKeySelective(..))")
    public void updateByPrimaryKeySelective(){}

    @Pointcut("execution(* self.mybatis.noxml.dao.CommonDao.updateByPrimaryKey(..))")
    public void updateByPrimaryKey(){}



    /**
     * 插入全部属性，无论属性值是否为空
     * */
    @Around("insert()")
    public Object insert(final ProceedingJoinPoint pjp) throws Throwable {
        Object[] args = pjp.getArgs();
        if (args == null){
            return null;
        }
        Object object = args[0];
        Map map = new HashMap();
        List<Map<String,Object>> columnList = getColumnNameAndValue(object);
        Map primaryMap = getPrimaryFieldNameAndValue(object);
        //没有需要插入的数据
        if (columnList == null && primaryMap == null){
            return null;
        }
        //只插入主键
        if (columnList == null){
            columnList = new ArrayList<>(1);
        }
        //primaryMap==null不需要插入主键（自增主键）
        if (primaryMap != null){
            columnList.add(primaryMap);
        }

        map.put("columns",columnList);
        Object[] args2 = {map};
        return pjp.proceed(args2);
    }

    /**
     * 插入属性值不为空的属性
     * */
    @Around("insertSelective()")
    public int insertSelective(final ProceedingJoinPoint pjp) throws Throwable {
        Object[] args = pjp.getArgs();
        if (args == null)
            return 0;
        Object record = args[0];
        Map map = new HashMap();
        List<Map<String,Object>> selectiveColumnList = getSelectiveColumnNameAndValue(record);
        Map<String,Object> primaryMap = getPrimaryFieldNameAndValue(record);
        //没有需要插入的数据
        if (selectiveColumnList == null && primaryMap == null)
            return 0;
        //只插入主键
        if (selectiveColumnList == null)
            selectiveColumnList = new ArrayList<>(1);
        //primaryMap==null不需要插入主键（自增主键）
        if (primaryMap != null){
            selectiveColumnList.add(primaryMap);
        }
        map.put("columns",selectiveColumnList);
        Object[] args2 = {map};
        return (int) pjp.proceed(args2);

    }

    @Around("selectByExample()")
    public Object selectByExample(final ProceedingJoinPoint pjp) throws Throwable {
        Object result;
        Object[] args = pjp.getArgs();
        result = pjp.proceed(args);
        return result;
    }

    /**
     * 更新部分属性，只更新值不为空成员变量
     * */
    @Around("updateByExampleSelective()")
    public int updateByExampleSelective(final ProceedingJoinPoint pjp) throws Throwable {
        Object[] args = pjp.getArgs();
        if (args == null)
            return 0;
        Object record = args[0];
        Object example = args[1];
        Map map = new HashMap();
        List<Map<String,Object>> selectiveColumnList = getSelectiveColumnNameAndValue(record);
        //可能会更新主键
        Map<String,Object> primaryMap = getPrimaryFieldNameAndValue(record);

        if (selectiveColumnList == null && primaryMap == null){
            throw new RuntimeException("selectiveColumnList and primaryMap are both null");
        }
        if (selectiveColumnList == null){
            selectiveColumnList = new ArrayList<>(1);
        }
        selectiveColumnList.add(primaryMap);
        map.put("columns",selectiveColumnList);
        Object[] args2 = {map,example};
        return (int) pjp.proceed(args2);

    }

    /**
     * 更新全部属性，无论成员变量值是否为空
     * */
    @Around("updateByExample()")
    public int updateByExample(final ProceedingJoinPoint pjp) throws Throwable {
        Object[] args = pjp.getArgs();
        if (args == null)
            return 0;
        Object record = args[0];
        Object example = args[1];
        Map map = new HashMap();
        List<Map<String,Object>> columnList = getColumnNameAndValue(record);
        //更新时不一定有主键
        Map primaryMap = getPrimaryFieldNameAndValue(record);
        if (columnList == null && primaryMap == null){
            throw new RuntimeException("columnList and primaryMap are both null");
        }
        if (columnList == null){
            columnList = new ArrayList<>(1);
        }
        columnList.add(primaryMap);
        map.put("columns",columnList);
        Object[] args2 = {map,example};
        return (int) pjp.proceed(args2);

    }

    /**
     * 根据主键更新（更新不为空的成员变量），主键不能为空，被更新的对象不能为空
     * */
    @Around("updateByPrimaryKeySelective()")
    public int updateByPrimaryKeySelective(final ProceedingJoinPoint pjp) throws Throwable {
        Object[] args = pjp.getArgs();
        if (args == null)
            return 0;
        Object record = args[0];
        Map map = new HashMap();
        //必须有主键
        Map<String,Object> primaryMap = getPrimaryFieldNameAndValue(record);
        if (primaryMap == null){
            throw new RuntimeException("primaryMap is null");
        }
        //必须有被更新的对象
        List<Map<String,Object>> selectiveColumnList = getSelectiveColumnNameAndValue(record);
        if (selectiveColumnList == null){
            return 0;
        }
        map.put("columns",selectiveColumnList);
        String primaryKey = (String)(primaryMap.keySet().toArray()[0]);
        Object primaryValue = primaryMap.values().toArray()[0];
        map.put("primaryKey",primaryKey);
        map.put("primaryValue",primaryValue);
        Object[] args2 = {map};
        return (int) pjp.proceed(args2);

    }

    /**
     * 根据主键更新（成员变量可能会空），主键不能为空，被更新的对象不能为空，
     * */
    @Around("updateByPrimaryKey()")
    public int updateByPrimaryKey(final ProceedingJoinPoint pjp) throws Throwable {
        Object[] args = pjp.getArgs();
        if (args == null)
        return 0;
        Object record = args[0];
        Map map = new HashMap();
        //必须有主键
        Map<String,Object> primaryMap = getPrimaryFieldNameAndValue(record);
        if (primaryMap == null){
            throw new RuntimeException("primaryMap is null");
        }
        //必须有被更新的对象
        List<Map<String,Object>> columnList = getColumnNameAndValue(record);
        if (columnList == null){
            return 0;
        }
        map.put("columns",columnList);
        String primaryKey = (String)(primaryMap.keySet().toArray()[0]);
        Object primaryValue = primaryMap.values().toArray()[0];
        map.put("primaryKey",primaryKey);
        map.put("primaryValue",primaryValue);
        Object[] args2 = {map};
        return (int) pjp.proceed(args2);
    }

    @Around("selectByPrimaryKey()")
    public Object selectByPrimaryKey(final ProceedingJoinPoint pjp) throws Throwable {
        Object entityObject = getActualEntityObject(pjp);
        Map<String,Object> primaryMap = getPrimaryFieldNameAndValue(entityObject);
        String primaryKey = primaryMap.keySet().toArray()[0].toString();
        Map map = new HashMap();
        map.put("primaryKey",primaryKey);
        Object value = pjp.getArgs()[0];
        map.put("primaryValue",value);
        Object[] args2 = {map};
        return pjp.proceed(args2);
    }

    @Around("deleteByPrimaryKey()")
    public int deleteByPrimaryKey(final ProceedingJoinPoint pjp) throws Throwable {
        int result;
        Object entityObject = getActualEntityObject(pjp);
        Map<String,Object> primaryMap = getPrimaryFieldNameAndValue(entityObject);
        String primaryKey = primaryMap.keySet().toArray()[0].toString();
        Map map = new HashMap();
        map.put("primaryKey",primaryKey);
        Object value = pjp.getArgs()[0];
        map.put("primaryValue",value);
        Object[] args2 = {map};
        result = (int) pjp.proceed(args2);
        return result;
    }


    /**
     *无论变量值是否为空，都返回
     * */
    private List<Map<String,Object>> getColumnNameAndValue(Object object) throws IllegalAccessException {
        List<Field> fields =  FieldUtils.getFieldsListWithAnnotation(object.getClass(),Column.class);
        if (fields != null && fields.size() > 0){
            List list = new ArrayList();
            for (Field field:fields){
                field.setAccessible(true) ;
                Column column = field.getAnnotation(Column.class);
                String name = column.name();
                Object value = field.get(object);
                Map columnMap = new HashMap();
                columnMap.put(name,value);
                list.add(columnMap);
            }
            return list;
        }
        return null;
    }

    /**
     * 返回变量值不为空的成员变量
     * */
    private List<Map<String,Object>> getSelectiveColumnNameAndValue(Object object) throws IllegalAccessException {
        List<Field> fields =  FieldUtils.getFieldsListWithAnnotation(object.getClass(),Column.class);
        if (fields != null && fields.size() > 0){
            List list = new ArrayList();
            for (Field field:fields){
                field.setAccessible(true);
                Column column = field.getAnnotation(Column.class);
                Object value = field.get(object);
                if (value != null){
                    String name = column.name();
                    Map columnMap = new HashMap();
                    columnMap.put(name,value);
                    list.add(columnMap);
                }
            }
            //等于0，相当于没有需要更新的字段
            if (list.size() > 0)
                return list;
        }
        return null;
    }

    /**
     * 如果主键是自增长，primaryMap可以为null
     * */
    private Map<String,Object> getPrimaryFieldNameAndValue(Object object) throws IllegalAccessException {
        List<Field> IdFields =  FieldUtils.getFieldsListWithAnnotation(object.getClass(),Primary.class);
        if (IdFields != null && IdFields.size() == 1){
            Field field = IdFields.get(0);
            field.setAccessible(true);
            Primary column = field.getAnnotation(Primary.class);
            Object value = field.get(object);
            if (value != null ){
                String name = column.name();
                Map primaryMap = new HashMap();
                primaryMap.put(name,value);
                return primaryMap;
            }
        }
        return null;
    }

    private Object getActualEntityObject(ProceedingJoinPoint pjp) throws IllegalAccessException, ClassNotFoundException, InstantiationException {
        Field proxyMethodInvocation =  FieldUtils.getAllFields(pjp.getClass())[1];
        proxyMethodInvocation.setAccessible(true);
        ReflectiveMethodInvocation invocation = (ReflectiveMethodInvocation) proxyMethodInvocation.get(pjp);
        InvocationHandler invocationHandler = Proxy.getInvocationHandler(((Proxy) invocation.getThis()));
        Field field = FieldUtils.getAllFields(((MapperProxy) invocationHandler).getClass())[2];
        field.setAccessible(true);
        FieldUtils.removeFinalModifier(field,true);
        Class actualDao = (Class) field.get(invocationHandler);
        Type[] daoTypes = actualDao.getGenericInterfaces();
        Type type = daoTypes[0];
        Class primaryClass = null;
        Class entityClass = null;
        if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            //返回表示此类型实际类型参数的 Type 对象的数组
            Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
            if (actualTypeArguments !=null && actualTypeArguments.length == 2){
                primaryClass = (Class) actualTypeArguments[0];
                entityClass = (Class) actualTypeArguments[1];
            }else {
                throw new RuntimeException("设置的泛型不对");
            }
        }
        String name = entityClass.getName();
        Object object =  Class.forName(name).newInstance();
        return object;
    }


}
