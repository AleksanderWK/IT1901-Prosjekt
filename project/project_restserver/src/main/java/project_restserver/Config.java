package project_restserver;

import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;

import project_restapi.AccountObjectMapperProvider;
import project_restapi.AccountService;

public class Config extends ResourceConfig {
  
	public Config() {
    register(AccountService.class);
    register(AccountObjectMapperProvider.class);
	register(JacksonFeature.class);
	
	/*
    register(new AbstractBinder() {
      @Override
      protected void configure() {
        bind(latLongs);
      }
    });
    */
  }
}
