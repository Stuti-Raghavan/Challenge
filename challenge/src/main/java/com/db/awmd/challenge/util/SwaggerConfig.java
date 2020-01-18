package com.db.awmd.challenge.util;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Swagger Configuration
 *
 */
@Profile({"default","test"}) //Swagger should  be available in non-prod environments only
@Configuration 
@EnableSwagger2
public class SwaggerConfig extends WebMvcConfigurerAdapter  {

	 @Bean
	    public Docket apiDocket() {
	        return new Docket(DocumentationType.SWAGGER_2)
	        		  .select()                                  
	        	      .apis(RequestHandlerSelectors.basePackage("com.db.awmd.challenge"))
	        	      .paths(PathSelectors.ant("/v1/accounts/**"))                          
	        	      .build();
	    }
	 
	    @Override
	    public void addResourceHandlers(ResourceHandlerRegistry registry) 
	    {
	        registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
	        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
	    }
	}