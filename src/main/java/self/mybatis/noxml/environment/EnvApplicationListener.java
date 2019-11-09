package self.mybatis.noxml.environment;


import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import self.mybatis.noxml.utils.PropertiesUtil;

public class EnvApplicationListener implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    public EnvApplicationListener(){
        super();
    }

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        initGlobal(applicationContext.getEnvironment());
    }

    private void initGlobal(Environment environment){
        PropertiesUtil.setEnvironment(environment);
    }
}
