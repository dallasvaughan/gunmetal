package com.github.overengineer.gunmetal;

import com.github.overengineer.gunmetal.inject.ComponentInjector;
import com.github.overengineer.gunmetal.instantiate.Instantiator;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.List;

/**
 * @author rees.byars
 */
public class CircularPrototypeComponentStrategy<T> implements ComponentStrategy<T> {

    private final ComponentInjector<T> injector;
    private final Instantiator<T> instantiator;
    private final Object qualifier;
    private final List<ComponentInitializationListener> initializationListeners;
    private transient ThreadLocal<CircularDependencyGuard> circularDependencyGuardThreadLocal = CircularDependencyGuard.local();

    CircularPrototypeComponentStrategy(ComponentInjector<T> injector, Instantiator<T> instantiator, Object qualifier, List<ComponentInitializationListener> initializationListeners) {
        this.injector = injector;
        this.instantiator = instantiator;
        this.qualifier = qualifier;
        this.initializationListeners = initializationListeners;
    }

    @SuppressWarnings("unchecked")
    @Override
    public T get(Provider provider) {
        CircularDependencyGuard circularDependencyGuard = circularDependencyGuardThreadLocal.get();
        if (circularDependencyGuard.holder != null) {
             if (circularDependencyGuard.holder != this) {
                 return (T) circularDependencyGuard.holder;
             }
            throw new CircularReferenceException(getComponentType(), getQualifier());
        } else {
            circularDependencyGuard.holder = this;
        }
        try {
            T component = instantiator.getInstance(provider);
            circularDependencyGuard.holder = component;
            injector.inject(component, provider);
            for (ComponentInitializationListener listener : initializationListeners) {
                component = listener.onInitialization(component);
            }
            return component;
        } catch (CircularReferenceException e) {
            if (e.getComponentType() == getComponentType() && e.getQualifier() == getQualifier()) {
                circularDependencyGuardThreadLocal.remove();
                ComponentStrategy<?> reverseStrategy = e.getReverseStrategy();
                Object reverseComponent = reverseStrategy.get(provider);
                return (T) e.getFieldProxy().get(reverseComponent);
            } else if (e.getComponentType() != getComponentType() || e.getQualifier() != getQualifier() && e.getReverseStrategy() == null) {
                e.setReverseStrategy(this);
            }
            throw e;
        } finally {
            circularDependencyGuardThreadLocal.remove();
        }
    }

    @Override
    public Class getComponentType() {
        return instantiator.getProducedType();
    }

    @Override
    public boolean isDecorator() {
        return instantiator.isDecorator();
    }

    @Override
    public Object getQualifier() {
        return qualifier;
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        circularDependencyGuardThreadLocal = CircularDependencyGuard.local();
    }

    private static class CircularDependencyGuard {

        Object holder;

        private static ThreadLocal<CircularDependencyGuard> local() {
            return new ThreadLocal<CircularDependencyGuard>() {
                protected CircularDependencyGuard initialValue() {
                    return new CircularDependencyGuard();
                }
            };
        }

    }

}
