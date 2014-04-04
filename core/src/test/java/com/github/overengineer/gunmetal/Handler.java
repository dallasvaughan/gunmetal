package com.github.overengineer.gunmetal;

/**
 * Created by dallasvaughan on 4/3/14.
 */
interface Handler<T,S> {
    T handle(S object);
}
