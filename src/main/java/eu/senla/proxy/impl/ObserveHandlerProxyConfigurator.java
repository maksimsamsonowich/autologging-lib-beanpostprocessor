package eu.senla.proxy.impl;

import eu.senla.annotation.Observe;
import eu.senla.proxy.ProxyConfigurator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;

import java.util.Arrays;
import java.util.stream.Collectors;

@Slf4j
public class ObserveHandlerProxyConfigurator implements ProxyConfigurator {

    @Override
    public Object configureProxy(Object implObject, Class<?> implClass) {

        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(implClass);
        enhancer.setCallback((MethodInterceptor) (obj, method, args, proxy) -> {
            if (!method.isAnnotationPresent(Observe.class)) {
                return method.invoke(implObject, args);
            }

            long startTime = System.currentTimeMillis();
            final Object proceed = method.invoke(implObject, args);

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
        });

        return enhancer.create();
    }
}
