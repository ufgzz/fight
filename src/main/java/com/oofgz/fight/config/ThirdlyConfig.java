package com.oofgz.fight.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateSettings;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManager;
import javax.sql.DataSource;
import java.util.Map;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef = "entityManagerFactoryThirdly",
        transactionManagerRef = "transactionManagerThirdly",
        basePackages = {"com.oofgz.fight.repository.thirdly"}
)
public class ThirdlyConfig extends DataSourceConfig {

    @Autowired
    @Qualifier("thirdlyDataSource")
    private DataSource thirdlyDataSource;

    @Bean(name = "entityManagerThirdly")
    public EntityManager entityManager(EntityManagerFactoryBuilder builder) {
        return entityManagerFactoryThirdly(builder).getObject().createEntityManager();
    }

    @Bean(name = "entityManagerFactoryThirdly")
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryThirdly (EntityManagerFactoryBuilder builder) {
        return builder
                .dataSource(thirdlyDataSource)
                .properties(getVendorProperties())
                .packages("com.oofgz.fight.domain.thirdly") //设置实体类所在位置
                .persistenceUnit("thirdlyPersistenceUnit")
                .build();
    }

    @Autowired
    private JpaProperties jpaProperties;
    private Map<String, Object> getVendorProperties() {
        return jpaProperties.getHibernateProperties(new HibernateSettings());
    }

    @Bean(name = "transactionManagerThirdly")
    PlatformTransactionManager transactionManagerThirdly(EntityManagerFactoryBuilder builder) {
        return new JpaTransactionManager(entityManagerFactoryThirdly(builder).getObject());
    }


}
