package self.mybatis.noxml.interceptor;

import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.scripting.xmltags.DynamicSqlSource;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.util.*;

/**
 * @author linsong.chen
 * @date 2020-09-22 19:00
 * 防sql注入，只拦截带${}的sql
 */
@Intercepts({@Signature(type = Executor.class, method = "query",
        args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}),
        @Signature(type = Executor.class, method = "query",
                args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class,CacheKey.class, BoundSql.class}),
        @Signature(type = Executor.class, method = "update",
                args = {MappedStatement.class, Object.class})})
public class MyBatisSqlInjectionInterceptor implements Interceptor {

    /**
     * 免疫字符，这些字符集不受各种格式的编码影响
     * */
    private final static char[] IMMUNE_SQL = { ' ' };

    /**
     * 初始化数组以标记要编码的字符。 存储该字符的十六进制字符串以节省时间。 如果不应该对字符进行编码，则存储null
     */
    private static final String[] hex = new String[256];

    static {
        for ( char c = 0; c < 0xFF; c++ ) {
            if ( c >= 0x30 && c <= 0x39 || c >= 0x41 && c <= 0x5A || c >= 0x61 && c <= 0x7A ) {
                hex[c] = null;
            } else {
                hex[c] = toHex(c).intern();
            }
        }
    }

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        final MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
        SqlSource sqlSource =mappedStatement.getSqlSource();
        //DynamicSqlSource类型表示sql带${}
        if (sqlSource instanceof DynamicSqlSource){
            Object parameter = invocation.getArgs()[1];
            //带${}的sql的参数不可能为空
            encodeParameter(parameter);
            //parameter为string类型时，编码后重新赋值
            if (parameter instanceof String){
                invocation.getArgs()[1] = parameter;
            }
        }
        return invocation.proceed();
    }

    /**
     * 只处理string,model,map的第一层值，对model和map中嵌套的对象不再递归处理
     * @return parameter
     * */
    public Object encodeParameter(Object parameter) throws IllegalAccessException {
        if (parameter == null){
            return null;
        } else if (parameter instanceof String){
            //对string类型参数编码
            parameter = encode(IMMUNE_SQL, (String) parameter);
            return parameter;
        } else if (parameter instanceof Map){
            //对map编码,map有个bug,dao层参数使用@Param注解后，参数类型就是Map,而且同一个参数会存在两个引用
            Map<Object,Object> parameterMap = (Map) parameter;
            for (Map.Entry entry : parameterMap.entrySet()){
                Object value = entry.getValue();
                if (value == null){
                    continue;
                }
                if (value instanceof String){
                    //对string类型参数编码
                    value = encode(IMMUNE_SQL, (String) value);
                    entry.setValue(value);
                }else if (value instanceof Collection){
                    //对集合编码 集合有个问题，同一个集合，mybatis会在map中存在两次，所以会被处理两次
                    encodeCollection((Collection) value);
                }else {
                    //基本数据类型、数组不处理,嵌套的Map不再递归处理
                    if (value.getClass().isPrimitive() || value.getClass().isArray() || value instanceof Map){
                        continue;
                    }
                    //参数是实体类（包含基本数据类型的包装类型）
                    encodeEntity(value);
                }
            }
            return parameter;
        } else if (parameter instanceof Collection){
            //对集合编码 集合有个问题，同一个集合，mybatis会在map中存在两次，所以会被处理两次
            encodeCollection((Collection) parameter);
            return parameter;
        } else {
            //参数是实体类（包含基本数据类型的包装类型）
            if (!(parameter.getClass().isPrimitive() || parameter.getClass().isArray())){
                encodeEntity(parameter);
            }
            return parameter;
        }
    }

    public void encodeCollection(Collection collection){
        if (collection.size() > 0 && (collection.iterator().next() instanceof String)){
            List<String> encodeList = new ArrayList<>(collection.size());
            for (Object el : collection){
                //对string类型参数编码
                el = encode(IMMUNE_SQL, (String)el);
                encodeList.add((String) el);
            }
            collection.clear();
            collection.addAll(encodeList);
        }
    }



    /**
     * 只对当前实体类的string类型字段编码，不进行递归处理
     * */
    public void encodeEntity(Object entity) throws IllegalAccessException {
        //实体对象
        Field[] fields = getAccessibleField(entity);
        if (fields == null){
            return;
        }
        for (Field field : fields){
            //如果是string类型的字段，对参数编码
            if (String.class.isAssignableFrom(field.getType())){
                makeAccessible(field);
                String val = (String) field.get(entity);
                if (val == null){
                    continue;
                }
                val = encode(IMMUNE_SQL, val);
                field.set(entity,val);
            }
        }
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {

    }

    public static Field[] getAccessibleField(final Object obj) {
        List<Field> fieldList = new ArrayList<>();
        for (Class<?> superClass = obj.getClass(); superClass != Object.class; superClass = superClass.getSuperclass()) {
            Field[] fields = superClass.getDeclaredFields();
            for (Field field:fields){
                fieldList.add(field);
            }
        }
        return fieldList.toArray(new Field[fieldList.size()]);
    }

    /**
     * 改变private/protected的成员变量为public，尽量不调用实际改动的语句，避免JDK的SecurityManager抱怨。
     */
    public static void makeAccessible(Field field) {
        if ((!Modifier.isPublic(field.getModifiers()) || !Modifier.isPublic(field.getDeclaringClass().getModifiers()) || Modifier
                .isFinal(field.getModifiers())) && !field.isAccessible()) {
            field.setAccessible(true);
        }
    }

    /**
     * 转码单引号和双引号
     * */
    public String encode(char[] immune, String input) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            sb.append(encodeCharacter(immune, c));
        }
        return sb.toString();
    }

    public String encodeCharacter( char[] immune, Character c ) {
        char ch = c.charValue();

        // check for immune characters
        if ( containsCharacter( ch, immune ) ) {
            return ""+ch;
        }

        // check for alphanumeric characters
        String hex = getHexForNonAlphanumeric( ch );
        if ( hex == null ) {
            return ""+ch;
        }

        return encodeCharacterANSI( c );
    }

    /**
     * encodeCharacterANSI用于ANSI SQL编码。
     *
     * 单引号双引号会被编码
     *
     * BUG###：在ANSI模式下，也可以使用引号来传递字符串。 在ANSI_QUOTES模式下，引号被认为是标识符，因此根本不能在值中使用引号，而将其完全删除
     *
     * @param c
     * 		    被编码的字符
     * @return
     *          字符串编码为以ANSI模式运行的MySQL标准
     */
    private String encodeCharacterANSI( Character c ) {
        if ( c == '\'' )
            return "\'\'";
        if ( c == '\"' )
            return "";
        return ""+c;
    }

    /**
     * 在char []中搜索特定字符的工具方法.
     *
     * @param c
     * @param array
     * @return
     */
    public static boolean containsCharacter( char c, char[] array ) {
        for (char ch : array) {
            if (c == ch) return true;
        }
        return false;
    }

    /**
     * 查找非字母数字字符的十六进制值.
     * @param c 要查找的字符.
     * @return, 如果为字母数字或字符代码，则返回null,其他返回十六进制
     */
    public static String getHexForNonAlphanumeric(char c) {
        if(c<0xFF)
            return hex[c];
        return toHex(c);
    }

    public static String toHex(char c) {
        return Integer.toHexString(c);
    }
}

