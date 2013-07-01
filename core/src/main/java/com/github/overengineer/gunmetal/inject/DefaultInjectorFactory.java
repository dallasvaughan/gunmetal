package com.github.overengineer.gunmetal.inject;

import com.github.overengineer.gunmetal.Provider;
import com.github.overengineer.gunmetal.key.Smithy;
import com.github.overengineer.gunmetal.metadata.MetadataAdapter;
import com.github.overengineer.gunmetal.parameter.ParameterBuilderFactory;
import com.github.overengineer.gunmetal.util.FieldRef;
import com.github.overengineer.gunmetal.util.FieldRefImpl;
import com.github.overengineer.gunmetal.util.MethodRefImpl;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @author rees.byars
 */
public class DefaultInjectorFactory implements InjectorFactory {

    private final MetadataAdapter metadataAdapter;
    private final ParameterBuilderFactory parameterBuilderFactory;

    public DefaultInjectorFactory(MetadataAdapter metadataAdapter, ParameterBuilderFactory parameterBuilderFactory) {
        this.metadataAdapter = metadataAdapter;
        this.parameterBuilderFactory = parameterBuilderFactory;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> ComponentInjector<T> create(Class<T> implementationType) {
        List<FieldInjector<T>> fieldInjectors = new ArrayList<FieldInjector<T>>();
        for (Field field : implementationType.getDeclaredFields()) {
            if (metadataAdapter.shouldInject(field)) {
                FieldRef fieldRef = new FieldRefImpl(field);
                FieldInjector<T> fieldInjector = new DefaultFieldInjector<T>(
                        fieldRef,
                        Smithy.forge(fieldRef, metadataAdapter.getQualifier(field.getGenericType(), field.getAnnotations())));
                fieldInjectors.add(fieldInjector);
            }
        }
        List<MethodInjector<T>> methodInjectors = new ArrayList<MethodInjector<T>>();
        for (Method method : implementationType.getMethods()) {
            if (metadataAdapter.isSetter(method)) {
                MethodInjector<T> setterInjector = create(implementationType, method);
                methodInjectors.add(setterInjector);
            }
        }
        Method initMethod = metadataAdapter.getInitMethod(implementationType);
        if (initMethod != null) {
            MethodInjector<T> initInjector = create(implementationType, initMethod);
            methodInjectors.add(initInjector);
        }
        return new DefaultComponentInjector<T>(fieldInjectors, methodInjectors);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> MethodInjector<T> create(Class<T> injectionTarget, Method method, Class ... providedArgs) {
        return new DefaultMethodInjector(new MethodRefImpl(method), parameterBuilderFactory.create(injectionTarget, method, providedArgs));
    }

    static class EmptyInjector<T> implements ComponentInjector<T> {
        @Override
        public void inject(T component, Provider provider) { }
    }
}