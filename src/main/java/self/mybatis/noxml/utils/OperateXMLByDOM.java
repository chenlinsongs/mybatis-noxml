package self.mybatis.noxml.utils;

import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.net.URI;

/**
 * DOM方式操作XML
 *
 * @author Watson Xu
 * @date 2011-5-3 上午09:49:27
 */
public class OperateXMLByDOM {
    /**
     * 将给定文件的内容或者给定 URI 的内容解析为一个 XML 文档，并且返回一个新的 DOM Document 对象
     *
     * @param filePath 文件所在路径
     * @return DOM的Document对象
     * @throws Exception
     */
    public static Document xml2Doc(String filePath) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        FileInputStream inputStream = null;
        Document doc = null;
        try {
            builder = factory.newDocumentBuilder();

            /* 通过文件方式读取,注意文件保存的编码和文件的声明编码要一致(默认文件声明是UTF-8) */
            File file = new File(filePath);
            doc = builder.parse(file);

            /* 通过一个URL方式读取 */
            URI uri = new URI(filePath);//filePath="http://java.sun.com/index.html"
            doc = builder.parse(uri.toString());

            /* 通过java IO 流的读取 */
            inputStream = new FileInputStream(filePath);
            doc = builder.parse(inputStream);
            return doc;
        } catch (Exception e) {
            return null;
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    return null;
                }
            }
        }
    }

    /**
     * Document 转换为 String 并且进行了格式化缩进
     *
     * @param doc XML的Document对象
     * @return String
     */
    public static String doc2FormatString(Document doc) {
        StringWriter stringWriter = null;
        try {
            stringWriter = new StringWriter();
            if(doc != null){
                OutputFormat format = new OutputFormat(doc,"UTF-8",true);
                //format.setIndenting(true);//设置是否缩进，默认为true
                //format.setIndent(4);//设置缩进字符数
                //format.setPreserveSpace(false);//设置是否保持原来的格式,默认为 false
                //format.setLineWidth(500);//设置行宽度
                XMLSerializer serializer = new XMLSerializer(stringWriter,format);
                serializer.asDOMSerializer();
                serializer.serialize(doc);
                return stringWriter.toString();
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        } finally {
            if(stringWriter != null){
                try {
                    stringWriter.close();
                } catch (IOException e) {
                    return null;
                }
            }
        }
    }

    /**
     * Document 转换为 String
     *
     * @param doc XML的Document对象
     * @return String
     */
    public static String doc2String(Document doc){
        try {
            Source source = new DOMSource(doc);
            StringWriter stringWriter = new StringWriter();
            Result result = new StreamResult(stringWriter);
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.transform(source, result);
            return stringWriter.getBuffer().toString();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * String 转换为 Document 对象
     *
     * @param xml 字符串
     * @return Document 对象
     */
    public static Document string2Doc(String xml) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        Document doc = null;
        InputSource source = null;
        StringReader reader = null;
        try {
            builder = factory.newDocumentBuilder();
            reader = new StringReader(xml);
            source = new InputSource(reader);//使用字符流创建新的输入源
            doc = builder.parse(source);
            return doc;
        } catch (Exception e) {
            return null;
        } finally {
            if(reader != null){
                reader.close();
            }
        }
    }

    /**
     * Document 保存为 XML 文件
     *
     * @param doc Document对象
     * @param path 文件路径
     */
    public static void doc2XML(Document doc, String path) {
        try {
            Source source = new DOMSource(doc);
            Result result = new StreamResult(new File(path));
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.transform(source, result);
        } catch (Exception e) {
            return;
        }
    }

//    public static void main(String[] args) {
//        Document doc = xml2Doc("test.xml");
//        System.out.println(doc);
//        System.out.println(doc2String(doc));
//        System.out.println(doc2FormatString(doc));
//        doc = string2Doc(doc2FormatString(doc));
//        doc2XML(doc, "1.xml");
//    }
}