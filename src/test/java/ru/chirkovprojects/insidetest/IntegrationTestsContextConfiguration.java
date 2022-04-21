package ru.chirkovprojects.insidetest;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import ru.chirkovprojects.insidetest.service.UserService;
import ru.chirkovprojects.insidetest.service.UserServiceImpl;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Properties;
import static org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType.H2;

@Configuration
@ComponentScan(basePackages = "ru.chirkovprojects.insidetest")
@EnableAutoConfiguration
public class IntegrationTestsContextConfiguration {

    ApplicationContext applicationContext;

    public IntegrationTestsContextConfiguration(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Bean
    public DataSource dataSource() {
        return new EmbeddedDatabaseBuilder().setType(H2).build();
    }

    @Bean
    public EntityManagerFactory entityManagerFactory(DataSource dataSource){
        LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();

        Properties props = new Properties();
        props.setProperty("hibernate.format_sql", String.valueOf(true));
        props.setProperty("hibernate.connection.autocommit", String.valueOf(true));

        factoryBean.setDataSource(dataSource);
        factoryBean.setPackagesToScan("ru.chirkovprojects.insidetest");
        factoryBean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        factoryBean.setJpaProperties(props);
        factoryBean.afterPropertiesSet();

        return factoryBean.getNativeEntityManagerFactory();
    }

    @Bean
    public UserService userService() {
        return applicationContext.getBean(UserServiceImpl.class);
    }

}
