package com.github.overengineer.gunmetal.proxy;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;

/**
 * @author rees.byars
 */
public interface JdkProxyFactory extends Serializable {
    Object newProxyInstance(InvocationHandler invocationHandler);
}
