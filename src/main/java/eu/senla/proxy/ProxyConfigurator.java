package eu.senla.proxy;

public interface ProxyConfigurator {
    Object configureProxy(Object implObject, Class<?> implClass);
}
