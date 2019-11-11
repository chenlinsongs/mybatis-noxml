package self.mybatis.noxml.interceptor;


import com.mchange.v2.c3p0.impl.NewProxyPreparedStatement;
import com.mysql.jdbc.JDBC42PreparedStatement;
import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.plugin.*;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Properties;

@Component
@Intercepts({@Signature(type = ResultSetHandler.class, method = "handleResultSets", args = {Statement.class})})
public class ResultHandlerInterceptor implements Interceptor {

    public Object intercept(Invocation invocation) throws Throwable {
        NewProxyPreparedStatement newProxyPreparedStatement = (NewProxyPreparedStatement)(invocation.getArgs()[0]);
        Field field = getField(newProxyPreparedStatement,"inner");
        PreparedStatement preparedStatement;
        if (field != null){
            field.setAccessible(true);
            preparedStatement = field.get(newProxyPreparedStatement) != null? (PreparedStatement) field.get(newProxyPreparedStatement) :null;
            if (preparedStatement !=null){
                System.out.println("sql:"+((JDBC42PreparedStatement) preparedStatement).asSql());
            }

        }
        return invocation.proceed();
    }

    private Field getField(Object object, String fieldName) {
        Field field = null;
        Class clazz = object.getClass();
        while (clazz != Object.class) {
            try {
                field = clazz.getDeclaredField(fieldName);
                if (field != null){
                    return field;
                }
            } catch (NoSuchFieldException e) {
            }
            clazz = clazz.getSuperclass();
        }
        return field;
    }

    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    public void setProperties(Properties properties) {

    }
}
