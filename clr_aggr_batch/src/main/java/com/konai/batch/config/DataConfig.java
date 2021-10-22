package com.konai.batch.config;

import net.sf.log4jdbc.Log4jdbcProxyDataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.batch.core.configuration.BatchConfigurationException;
import org.springframework.batch.core.configuration.annotation.BatchConfigurer;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.explore.support.MapJobExplorerFactoryBean;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.MapJobRepositoryFactoryBean;
import org.springframework.batch.support.transaction.ResourcelessTransactionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.konai.batch.common.KonaEncryptUtil;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

/**
 *
 * @author eslee
 *
 */
@Configuration
@PropertySource(value = "file:${app.home}/application.properties", ignoreResourceNotFound = true) // java -jar -Dapp.home="/home/jbhan/income" sample.jar
@PropertySource(value = "classpath:application.properties", ignoreResourceNotFound = true)
@EnableTransactionManagement
public class DataConfig {

    DataSource dataSource;

	@Value("${spring.datasource.url}")
	private String url;

	@Value("${spring.datasource.driverClassName}")
	private String className;

	@Value("${spring.datasource.username}")
	private String userName;

	@Value("${spring.datasource.password}")
	private String password;

	@Autowired
	KonaEncryptUtil encDbUtil;

	// DB Connection
	@Bean
	//@ConfigurationProperties(prefix="spring.datasource")
	public DataSource realDataSource(){

		String pwd = encDbUtil == null?
				password : encDbUtil.decryptCustomerInfo(password);

        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName(className);
        dataSource.setUrl(url);
        dataSource.setUsername(userName);
        dataSource.setPassword(pwd);

        this.dataSource = dataSource;

        return this.dataSource;
	}

	@Bean
	@Primary
	DataSource dataSource() {
		return new Log4jdbcProxyDataSource(dataSource);
	}

    @Bean
    public PlatformTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dataSource);
    }


    @Bean
    public BatchConfigurer batchConfigurer() {

        BatchConfigurer configurer = new BatchConfigurer() {

            private PlatformTransactionManager tranactionManager;
            private JobRepository jobRepository;
            private JobLauncher jobLauncher;
            private JobExplorer jobExplorer;

            @Override
            public JobRepository getJobRepository() throws Exception {
                return jobRepository;
            }

            @Override
            public PlatformTransactionManager getTransactionManager() throws Exception {
                return tranactionManager;
            }

            @Override
            public JobLauncher getJobLauncher() throws Exception {
                return jobLauncher;
            }

            @Override
            public JobExplorer getJobExplorer() throws Exception {
                return jobExplorer;
            }

            @PostConstruct
            public void initialize(){
                if(this.tranactionManager == null){
                    this.tranactionManager = new ResourcelessTransactionManager();
                }
                try {
                    MapJobRepositoryFactoryBean jrf = new MapJobRepositoryFactoryBean(this.tranactionManager);
                    jrf.setIsolationLevelForCreate("ISOLATION_REPEATABLE_READ");
                    jrf.afterPropertiesSet();
                    this.jobRepository = jrf.getObject();

                    MapJobExplorerFactoryBean jef = new MapJobExplorerFactoryBean(jrf);
                    jef.afterPropertiesSet();
                    this.jobExplorer = jef.getObject();

                    SimpleJobLauncher jobLauncher = new SimpleJobLauncher();
                    jobLauncher.setJobRepository(jobRepository);
                    jobLauncher.afterPropertiesSet();

                    this.jobLauncher = jobLauncher;

                } catch ( Exception e ){
                    throw new BatchConfigurationException(e);
                }

            }
        };

        return configurer;
    }


}