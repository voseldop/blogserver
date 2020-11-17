package info.podlesov;

import javax.servlet.MultipartConfigElement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.util.unit.DataSize;

import info.podlesov.model.User;

/**
 * Home redirection to swagger api documentation
 */

@SpringBootApplication
@EnableTransactionManagement
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
@ComponentScan(basePackages = { "info.podlesov", "info.podlesov.api" , "info.podlesov.configuration"})
public class Swagger2SpringBoot {
    @Bean
    MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setMaxFileSize(DataSize.ofKilobytes(300));
        return factory.createMultipartConfig();
    }

    public static void main(String[] args) {
        new SpringApplication(Swagger2SpringBoot.class).run(args);
    }

    @Bean
    public AuditorAware<User> auditorProvider() {
        return new AuditorAwareImpl();
    }
}
