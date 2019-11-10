package self.mybatis.noxml;

import org.apache.ibatis.builder.BuilderException;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import self.mybatis.noxml.annotation.MyBatisDao;
import self.mybatis.noxml.dao.CommonDao;
import self.mybatis.noxml.utils.OperateXMLByDOM;

import javax.persistence.Table;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;


public class DynamicMapperSqlSessionBean extends SqlSessionFactoryBean {

    Logger logger = LoggerFactory.getLogger(DynamicMapperSqlSessionBean.class);

    private String searchLocation;

    private String commonMapperXmlPath;

    public DynamicMapperSqlSessionBean(String searchLocation,String commonMapperXmlPath) {
        this.searchLocation = searchLocation;
        this.commonMapperXmlPath = commonMapperXmlPath;
    }

    @Override
    public void setMapperLocations(Resource[] mapperLocations) {
        List<Resource> resource = getEntityMapperResource();
        if (mapperLocations == null)
            mapperLocations = new Resource[0];
        Resource[] resources = new Resource[mapperLocations.length +resource.size()];
        if (resource != null && resource.size() > 0){
            for(int i= 0;i < resource.size();i++){
                resources[i]= resource.get(i);
            }
        }
        int base = 0;
        if (resource != null)
            base = resource.size();

        for(int i=0;i < mapperLocations.length;i++){
            resources[i+base]= mapperLocations[i];
        }
        super.setMapperLocations(resources);
    }

    private List<Resource> getEntityMapperResource(){
        Reflections reflections = new Reflections(searchLocation);
        Set<Class<?>> annotated = reflections.getTypesAnnotatedWith(MyBatisDao.class);
        List<Resource> resources = new ArrayList<>();
        if (annotated !=null && annotated.size() > 0){
            Iterator iterator = annotated.iterator();
            if (logger.isInfoEnabled())
                logger.info("Dao total:"+annotated.size());
            int i = 0;
            while (iterator.hasNext()){
                Class clazz = (Class) iterator.next();
                if (CommonDao.class.isAssignableFrom(clazz)){
                    if (logger.isInfoEnabled())
                        logger.info("init dao:"+clazz.getName()+" "+(i++));
                    Resource resource = getMapper4Class(clazz);
                    resources.add(resource);
                }
            }
        }
        return resources;
    }

    private Resource getMapper4Class(Class dao){
        Resource resource = new ClassPathResource(commonMapperXmlPath);
        try {
            Type[] daoTypes = dao.getGenericInterfaces();
            Class entity = null;
            if (daoTypes != null && daoTypes.length > 0){
                for (Type type:daoTypes) {
                    if (type instanceof ParameterizedType) {
                        ParameterizedType parameterizedType = (ParameterizedType) type;
                        //返回表示此类型实际类型参数的 Type 对象的数组
                        Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
                        if (actualTypeArguments !=null && actualTypeArguments.length == 2){
                            entity = (Class) actualTypeArguments[1];
                        }else {
                            throw new RuntimeException("设置的泛型不对");
                        }
                    }
                }
            }else {
                throw new RuntimeException("没有找到抽象接口");
            }
            Document document = createDocument(new InputSource(resource.getInputStream()));
            updateNamespace(document,dao);
            updateSelect(document,entity);
            Table table = (Table) entity.getAnnotation(Table.class);
            String tableName = table.name();
            String xmlDoc = OperateXMLByDOM.doc2FormatString(document);
            String tableReplace = xmlDoc.replaceAll("\\$\\{table}",tableName);
            resource = new InputStreamResource(new ByteArrayInputStream(tableReplace.getBytes("UTF-8")),dao.getName());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resource;
    }

    private Document createDocument(InputSource inputSource) {
        // important: this must only be called AFTER common constructor
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setValidating(false);
            factory.setNamespaceAware(false);
            factory.setIgnoringComments(true);
            factory.setIgnoringElementContentWhitespace(false);
            factory.setCoalescing(false);
            factory.setExpandEntityReferences(true);
            factory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);

            DocumentBuilder builder = factory.newDocumentBuilder();
            builder.setErrorHandler(new ErrorHandler() {
                @Override
                public void error(SAXParseException exception) throws SAXException {
                    throw exception;
                }

                @Override
                public void fatalError(SAXParseException exception) throws SAXException {
                    throw exception;
                }

                @Override
                public void warning(SAXParseException exception) throws SAXException {
                }
            });
            return builder.parse(inputSource);
        } catch (Exception e) {
            throw new BuilderException("Error creating document instance.  Cause: " + e, e);
        }
    }

    private void updateNamespace(Document doc,Class clazz){
        NodeList mapper = doc.getElementsByTagName("mapper");
        Element namespace = (Element) mapper.item(0);
        namespace.setAttribute("namespace",clazz.getName());
    }

    private void updateSelect(Document doc,Class clazz) {
        NodeList employees = doc.getElementsByTagName("select");
        Element emp;
        //loop for each employee
        for(int i=0; i<employees.getLength();i++){
            emp = (Element) employees.item(i);
            String resultType =emp.getAttribute("resultType");
            if ("entity".equals(resultType)){
                emp.setAttribute("resultType",clazz.getName());
            }
        }
    }

}
