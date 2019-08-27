package com.sap.cloud.s4hana.mlretrainservice.webconfig;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web application context configuration using Java annotations.
 */
@Configuration
@EnableWebMvc
@ComponentScan(basePackages = { "com.sap.cloud.s4hana.mlretrainservice" })
public class WebAppConfigurer implements WebMvcConfigurer {

	/**
	 * Enable default view ("index.html") mapped under "/".
	 */
	@Override
	public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
		configurer.enable();
	}

	/**
	 * Set up the cached resource handling for OpenUI5 runtime served from the
	 * webjar in {@code /WEB-INF/lib} directory and local JavaScript files in
	 * {@code /resources} directory.
	 */
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/resources/**").addResourceLocations("classpath:/resources/", "/resources/**")
				.setCachePeriod(31556926);
	}

	/*
	 * @Override public void addCorsMappings(CorsRegistry registry) {
	 * registry.addMapping("/**") .allowedOrigins("*") .allowedMethods("GET",
	 * "POST", "PUT", "DELETE", "PATCH") .allowCredentials(false).maxAge(3600); }
	 */
}
