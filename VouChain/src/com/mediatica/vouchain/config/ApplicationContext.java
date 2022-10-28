package com.mediatica.vouchain.config;

import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.mediatica.vouchain.utilities.EncrptingUtils;

/**
 * 
 * @author Pietro Napolitano
 *
 */

@Configuration
@PropertySource("file:${vouchain_home}/configurations/database.properties")
@EnableTransactionManagement
@EnableScheduling
public class ApplicationContext {
	@Autowired
    private Environment environment;
	
	@Autowired
    private EncrptingUtils encrptingUtils;

    @Bean
    public LocalSessionFactoryBean sessionFactory() throws  IllegalStateException {
    	System.out.println("sessionFactory STARTING");
    	LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSource());
        sessionFactory.setPackagesToScan(new String[] {
            Constants.ENTITY_BASE_PACKAGE
        });
        sessionFactory.setHibernateProperties(hibernateProperties());
        return sessionFactory;
    }

    @Bean
    public DataSource dataSource() {
    	DriverManagerDataSource dataSource = new DriverManagerDataSource();
    	try {
    		System.out.println("dataSource STARTING");

    		dataSource.setDriverClassName(environment.getRequiredProperty("jdbc.driverClassName"));
    		dataSource.setUrl(environment.getRequiredProperty("jdbc.url"));
    		dataSource.setUsername(environment.getRequiredProperty("jdbc.username"));
    		dataSource.setPassword(encrptingUtils.decrypt(environment.getRequiredProperty("jdbc.password").trim()));   		
    		
    	} catch (IllegalStateException e) {
    		e.printStackTrace();
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	return dataSource;
    }

    private Properties hibernateProperties() {
    	
        Properties properties = new Properties();
        properties.put("hibernate.dialect", environment.getRequiredProperty("hibernate.dialect"));
        properties.put("hibernate.show_sql", environment.getRequiredProperty("hibernate.show_sql"));
        properties.put("hibernate.format_sql", environment.getRequiredProperty("hibernate.format_sql"));
        properties.put("hibernate.hbm2ddl.auto", environment.getRequiredProperty("hibernate.hbm2ddl.auto"));
        properties.put("hibernate.dbcp.initialSize", environment.getRequiredProperty("hibernate.dbcp.initialSize"));
        properties.put("hibernate.dbcp.maxTotal", environment.getRequiredProperty("hibernate.dbcp.maxTotal"));
        properties.put("hibernate.dbcp.maxIdle", environment.getRequiredProperty("hibernate.dbcp.maxIdle"));
        properties.put("hibernate.dbcp.minIdle", environment.getRequiredProperty("hibernate.dbcp.minIdle"));
        properties.put("hibernate.dbcp.maxWaitMillis", environment.getRequiredProperty("hibernate.dbcp.maxWaitMillis"));
        return properties;
    }

    @Bean
    public HibernateTransactionManager getTransactionManager() {
    	System.out.println("transactionManager STARTING");
        HibernateTransactionManager transactionManager = new HibernateTransactionManager();
        transactionManager.setSessionFactory(sessionFactory().getObject());
        return transactionManager;
    }
    
    @Bean(name = "multipartResolver")
    public CommonsMultipartResolver multipartResolver() {
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
        multipartResolver.setMaxUploadSize(Constants.maxUploadsSize);
        return multipartResolver;
    }
}
