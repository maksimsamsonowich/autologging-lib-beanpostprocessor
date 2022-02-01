package eu.senla.annotation.impl.proxy;

import eu.senla.annotation.Observe;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.stream.Collectors;

@Slf4j
@Component
public class WardedClassProxyInvocationHandler implements InvocationHandler {

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

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
