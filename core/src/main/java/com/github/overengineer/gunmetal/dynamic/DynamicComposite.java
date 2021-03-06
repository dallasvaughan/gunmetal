package com.github.overengineer.gunmetal.dynamic;

import com.github.overengineer.gunmetal.ComponentStrategy;
import com.github.overengineer.gunmetal.Provider;
import com.github.overengineer.gunmetal.SelectionAdvisor;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Iterator;
import java.util.List;

/**
 * @author rees.byars
 */
public class DynamicComposite<T> implements InvocationHandler, Serializable {

    private static final long serialVersionUID = 4463243954034425681L;
    private final Class<T> componentInterface;
    private final Provider provider;
    private List<T> components;
    T proxy;

    DynamicComposite(Class<T> componentInterface, final Provider provider) {
        this.componentInterface = componentInterface;
        this.provider = provider;
    }

    @Override
    public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
        String methodName = method.getName();
        if ("equals".equals(methodName)) {
            return proxy == objects[0];
        } else if ("hashCode".equals(methodName)) {
            return System.identityHashCode(proxy);
        } else if ("toString".equals(methodName)) {
            return proxy.getClass().getName() + '@' + Integer.toHexString(System.identityHashCode(this)) + "$DynamicComposite$[" + componentInterface.getName() + "]";
        }

        //TODO add double-check locking
        if (components == null) {
            components = provider.getAll(componentInterface, new SelectionAdvisor() {
                @Override
                public boolean validSelection(ComponentStrategy<?> candidateStrategy) {
                    Class<?> candidateClass = candidateStrategy.getComponentType();
                    return candidateClass != proxy.getClass() || Proxy.isProxyClass(candidateClass);
                }
            });
            for (Iterator<T> i = components.iterator(); i.hasNext();) {
                if (i.next().toString().equals(proxy.toString())) {
                    i.remove();
                }
            }
        }
        for (T component : components) {
            method.invoke(component, objects);
        }
        return null;
    }

}
