package eu.senla.annotation.impl;

import eu.senla.annotation.impl.proxy.WardedClassProxy;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import eu.senla.annotation.Observe;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Component
public class ObserveAnnotationBeanPostProcessor implements BeanPostProcessor {

    private final Map<String, Class> wardedBeans = new HashMap<>();

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

        Class wardedBean = wardedBeans.get(beanName);

        if (Objects.isNull(wardedBean))
            return BeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);

        Object proxyBean = Proxy.newProxyInstance(bean.getClass().getClassLoader(),
                bean.getClass().getInterfaces(), new WardedClassProxy());


        return BeanPostProcessor.super.postProcessAfterInitialization(proxyBean, beanName);
    }
}
