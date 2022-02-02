package eu.senla.beanpostprocessor;

import eu.senla.annotation.Observe;
import eu.senla.proxy.ProxyConfigurator;
import eu.senla.proxy.impl.ObserveHandlerProxyConfigurator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Component
public class ObserveAnnotationBeanPostProcessor implements BeanPostProcessor {

    private final Map<String, Class<?>> wardedBeans = new HashMap<>();
    private final ProxyConfigurator proxyConfigurator = new ObserveHandlerProxyConfigurator();

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {

        if (Arrays.stream(bean.getClass().getMethods())
                .anyMatch(method -> method.isAnnotationPresent(Observe.class))) {
            wardedBeans.put(beanName, bean.getClass());
        }

        return BeanPostProcessor.super.postProcessBeforeInitialization(bean, beanName);
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {

        Class<?> wardedBean = wardedBeans.get(beanName);

        if (Objects.isNull(wardedBean))
            return BeanPostProcessor.super.postProcessBeforeInitialization(bean, beanName);

        return proxyConfigurator.configureProxy(bean, wardedBean);
    }
}
