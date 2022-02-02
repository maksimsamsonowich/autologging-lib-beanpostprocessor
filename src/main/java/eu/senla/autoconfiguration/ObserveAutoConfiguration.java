package eu.senla.autoconfiguration;

import eu.senla.proxy.impl.ObserveHandlerProxyConfigurator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;

@ConditionalOnClass( { ObserveHandlerProxyConfigurator.class } )
public class ObserveAutoConfiguration {

    @Bean
    public ObserveHandlerProxyConfigurator observeHandlerProxyConfigurator() {
        return new ObserveHandlerProxyConfigurator();
    }

}
