package self.mybatis.noxml.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "mybatis")
public class MybatisProperties {

    private String basePackage;

    private String commonMapperXmlPath = "/mappings/common/Common.xml";

    private String customMapperXmlPath = "classpath*:/mappings/modules/*/*.xml";

    private String configFilePath = "classpath:/mybatis-config.xml";


    public String getBasePackage() {
        return basePackage;
    }

    public void setBasePackage(String basePackage) {
        this.basePackage = basePackage;
    }

    public String getCommonMapperXmlPath() {
        return commonMapperXmlPath;
    }

    public void setCommonMapperXmlPath(String commonMapperXmlPath) {
        this.commonMapperXmlPath = commonMapperXmlPath;
    }

    public String getCustomMapperXmlPath() {
        return customMapperXmlPath;
    }

    public void setCustomMapperXmlPath(String customMapperXmlPath) {
        this.customMapperXmlPath = customMapperXmlPath;
    }

    public String getConfigFilePath() {
        return configFilePath;
    }

    public void setConfigFilePath(String configFilePath) {
        this.configFilePath = configFilePath;
    }
}
