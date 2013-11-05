package io.gunmetal.builder;

import io.gunmetal.Component;

import java.lang.reflect.Method;

/**
 * @author rees.byars
 */
class DefaultComponentAdapterFactory implements ComponentAdapterFactory {

    private final Injector.Factory injectorFactory;

    DefaultComponentAdapterFactory(Injector.Factory injectorFactory) {
        this.injectorFactory = injectorFactory;
    }

    @Override
    public <T> ComponentAdapter<T> create(Component component, InternalProvider internalProvider) {
        return null;
    }

    @Override
    public <T> ComponentAdapter<T> create(Method providerMethod, InternalProvider internalProvider) {
        Injector.StaticMethod injector = injectorFactory.create(providerMethod, internalProvider);
        return null;
    }

}
