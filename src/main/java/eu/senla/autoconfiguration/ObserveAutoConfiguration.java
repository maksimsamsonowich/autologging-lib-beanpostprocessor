package eu.senla.autoconfiguration;

import eu.senla.beanpostprocessor.ObserveAnnotationBeanPostProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

@ConditionalOnClass( { ObserveAnnotationBeanPostProcessor.class } )
public class ObserveAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public ObserveAnnotationBeanPostProcessor beanPostProcessor() {
        return new ObserveAnnotationBeanPostProcessor();
    }
}
