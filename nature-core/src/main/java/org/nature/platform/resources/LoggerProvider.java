package org.nature.platform.resources;

import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * 
 * @author hutianlong
 *
 */
@Dependent
public class LoggerProvider {
	
	 @Produces
	    public Logger getLogger(InjectionPoint injectionPoint){
	        return LoggerFactory.getLogger(injectionPoint.getMember().getDeclaringClass().getName());
	    }

}
