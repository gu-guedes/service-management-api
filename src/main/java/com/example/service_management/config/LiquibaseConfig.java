package com.example.service_management.config;

import liquibase.integration.spring.SpringLiquibase;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class LiquibaseConfig {

    @Bean
    public SpringLiquibase liquibase(DataSource dataSource) {
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setDataSource(dataSource);
        liquibase.setChangeLog("classpath:db/changelog/db.changelog-master.sql");
        liquibase.setContexts("development,production");
        liquibase.setShouldRun(true);
        return liquibase;
    }

    /**
     * No Spring Boot 4.0 a auto-configuração automática do Liquibase (que
     * antes garantia, sozinha, que o Hibernate esperasse o Liquibase rodar
     * as migrations antes de validar o schema) não está presente na
     * dependência deste projeto - por isso o Hibernate estava tentando
     * validar a tabela "medical_records" ANTES do Liquibase ter chance de
     * criá-la, e a aplicação falhava com "SchemaManagementException: missing
     * table". Este BeanFactoryPostProcessor recria manualmente essa garantia
     * de ordem: força o bean "entityManagerFactory" a esperar o bean
     * "liquibase" terminar antes de ser criado.
     *
     * Usa o NOME do bean ("entityManagerFactory", nome padrão usado pelo
     * Spring Boot na auto-configuração de JPA) em vez do TIPO, porque a
     * primeira tentativa (buscar por getBeanNamesForType(EntityManagerFactory.class))
     * não encontrou nada - o Spring não consegue prever o tipo produzido
     * pelo FactoryBean sem instanciá-lo, então a busca por tipo retornava
     * vazio e nenhuma dependência era adicionada.
     *
     * Precisa ser "static" para não disparar a criação antecipada de outros
     * beans durante o processamento das @Configuration classes.
     */
    @Bean
    static BeanFactoryPostProcessor entityManagerFactoryDependsOnLiquibase() {
        return beanFactory -> {
            if (beanFactory.containsBeanDefinition("entityManagerFactory")) {
                beanFactory.getBeanDefinition("entityManagerFactory").setDependsOn("liquibase");
            }
        };
    }
}
