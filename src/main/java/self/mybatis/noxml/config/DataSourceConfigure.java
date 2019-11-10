package self.mybatis.noxml.config;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;

@ConditionalOnProperty(prefix = "jdbc", value = "c3p0", havingValue = "true")
@EnableConfigurationProperties(DataSourceProperties.class)
public class DataSourceConfigure {

    private final DataSourceProperties dataSourceProperties;

    public DataSourceConfigure(DataSourceProperties dataSourceProperties1){

        this.dataSourceProperties = dataSourceProperties1;
    }

    @Bean(name="dataSource")
    public DataSource c3poDataSource(){
        ComboPooledDataSource cpds = new ComboPooledDataSource();
        try {
            cpds.setDriverClass( dataSourceProperties.getDriver() ); //loads the jdbc driver
        } catch (PropertyVetoException e) {
            e.printStackTrace();
        }
        cpds.setJdbcUrl( dataSourceProperties.getUrl() );
        cpds.setUser(dataSourceProperties.getUsername());
        cpds.setPassword(dataSourceProperties.getPassword());
        return cpds;
    }
}
