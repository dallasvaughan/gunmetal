package com.github.overengineer.gunmetal.inject;

import com.github.overengineer.gunmetal.Provider;
import com.github.overengineer.gunmetal.parameter.ParameterBuilder;
import com.github.overengineer.gunmetal.util.MethodRef;

/**
 * @author rees.byars
 */
public class DefaultMethodInjector<T> implements MethodInjector<T> {

    private final MethodRef methodRef;
    private final ParameterBuilder parameterBuilder;

    DefaultMethodInjector(MethodRef methodRef, ParameterBuilder<T> parameterBuilder) {
        this.methodRef = methodRef;
        this.parameterBuilder = parameterBuilder;
    }

    @Override
    public Object inject(T component, Provider provider, Object ... providedArgs) {
        try {
            return methodRef.getMethod().invoke(component, parameterBuilder.buildParameters(provider, providedArgs));
        } catch (Exception e) {
            throw new InjectionException("Could not inject method [" + methodRef.getMethod().getName() + "] on component of type [" + component.getClass().getName() + "].", e);
        }
    }

}