package com.bhtcnpm.website;

import com.bhtcnpm.website.miscellenous.PrettySqlFormat;
import com.fasterxml.jackson.databind.InjectableValues;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.p6spy.engine.spy.P6SpyOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.config.BootstrapMode;
import org.springframework.data.repository.init.Jackson2RepositoryPopulatorFactoryBean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@SpringBootApplication
//Deferred mode is for Hibernate Search 6 startup issue's workaround.
//https://docs.jboss.org/hibernate/stable/search/reference/en-US/html_single/#_spring_boot
@EnableJpaRepositories(bootstrapMode = BootstrapMode.DEFERRED)
@EnableAsync
public class WebsiteApplication {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private ObjectMapper objectMapper;

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(WebsiteApplication.class);
        P6SpyOptions.getActiveInstance().setLogMessageFormat(PrettySqlFormat.class.getName());
        //This is for initialize Hibernate Search indexing when the application is ready.
        springApplication.addListeners(new HibernateSearchEventListener());
        SpringApplication.run(WebsiteApplication.class, args);
    }

    @Bean
    public RestTemplate restTemplate (RestTemplateBuilder builder) {
        return builder.build();
    }

    //Populate data for repository.
    @Bean
    @Transactional
    public Jackson2RepositoryPopulatorFactoryBean getRepositoryPopulator() {
        Jackson2RepositoryPopulatorFactoryBean factoryBean = new Jackson2RepositoryPopulatorFactoryBean();
        InjectableValues injects = new InjectableValues.Std().addValue(EntityManager.class, entityManager);
        objectMapper.setInjectableValues(injects);
        factoryBean.setResources(new Resource[]{
                new ClassPathResource("data/UserWebsite/user-website-role.json"),
                new ClassPathResource("data/UserWebsite/user-website.json"),
                new ClassPathResource("data/tag-data.json"),
                new ClassPathResource("data/Post/post-category.json"),
                new ClassPathResource("data/Post/post.json"),
                new ClassPathResource("data/Post/highlight-post.json"),
                new ClassPathResource("data/Post/post-comment.json"),
                new ClassPathResource("data/Doc/doc-subject.json"),
                new ClassPathResource("data/Doc/doc-category.json"),
                new ClassPathResource("data/Doc/doc-file-upload.json"),
                new ClassPathResource("data/Doc/doc.json"),
                new ClassPathResource("data/Doc/user-doc-reaction.json"),
                new ClassPathResource("data/report-reason.json"),
                new ClassPathResource("data/Exercise/exercise-subject-group.json"),
                new ClassPathResource("data/Exercise/exercise-subject-faculty.json"),
                new ClassPathResource("data/Exercise/exercise-subject.json"),
                new ClassPathResource("data/Exercise/exercise-topic.json"),
                new ClassPathResource("data/Exercise/exercise-category.json"),
                new ClassPathResource("data/Exercise/exercise.json")
        });
        factoryBean.setMapper(objectMapper);

        return factoryBean;
    }
}