package eu.senla.annotation.impl;

import eu.senla.annotation.impl.proxy.WardedClassProxyInvocationHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import eu.senla.annotation.Observe;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class ObserveAnnotationBeanPostProcessor implements BeanPostProcessor {

    private final Map<String, Class<?>> wardedBeans = new HashMap<>();

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

        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(wardedBean);
        enhancer.setCallback((MethodInterceptor) (obj, method, args, proxy) -> {
            Optional<Method> originalMethod = Arrays.stream(wardedBean.getMethods())
                    .filter(method::equals)
                    .findFirst();

            if (originalMethod.isPresent()) {
                Observe annotation = originalMethod.get().getAnnotation(Observe.class);
                if (annotation != null) {
                    if (!method.isAnnotationPresent(Observe.class)) {
                        return method.invoke(args);
                    }

                    long startTime = System.currentTimeMillis();
                    final Object proceed = method.invoke(args);

                    log.info("total execution time: " + (System.currentTimeMillis() - startTime) + "ms");

                    String argsToDisplay = Arrays.stream(args)
                            .map(Object::toString)
                            .collect(Collectors.joining(","));

                    log.info(method.getName() + " args [" + argsToDisplay + "]");

                    if (proceed == null)
                        log.info("returning value = void");
                    else
                        log.info("returning value = " + proceed);

                    log.info("method " + method.getName() + " completed its execution");

                    return proceed;
                }
            }
            return method.invoke(bean, args);
        });
        return enhancer.create();
    }
}
