package com.sap.cloud.s4hana.mlretrainservice.webconfig;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.slf4j.Logger;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import com.sap.cloud.sdk.cloudplatform.logging.CloudLoggerFactory;

/**
 * Web application initializer without {@code web.xml}, for Servlet 3.0.
 **/
public class WebAppInitializer implements WebApplicationInitializer {
	private static final Logger logger = CloudLoggerFactory.getLogger(WebAppInitializer.class);

	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {
		logger.info("Initializing web application with context configuration class {}",
				WebAppConfigurer.class.getCanonicalName());

		// create annotation based web application context
		AnnotationConfigWebApplicationContext webAppContext = new AnnotationConfigWebApplicationContext();
		webAppContext.register(WebAppConfigurer.class);

		// create and register Spring MVC dispatcher servlet
		ServletRegistration.Dynamic dispatcher = servletContext.addServlet("dispatcher",
				new DispatcherServlet(webAppContext));
		dispatcher.setLoadOnStartup(1);
		dispatcher.addMapping("/");

	}

}
