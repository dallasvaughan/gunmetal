package com.github.overengineer.gunmetal;



/**
 * Created by dallasvaughan on 4/3/14.
 */

public abstract class TestService<T,S> implements Handler<T,S> {

    @Override
    public abstract T handle(S value);

    public String doSomething(T value){
        return value.toString() + " aha";
    }
}
