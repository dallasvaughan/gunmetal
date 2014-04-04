package com.github.overengineer.gunmetal;

import java.util.function.Function;

/**
 * Created by dallasvaughan on 4/3/14.
 */
public class Handlers {
    public String handle(Integer integer) {
        return integer.toString();
    }

    public Integer handle(String string){
        return Integer.parseInt(string);
    }


    Function<String, Integer> handle = new Function<String, Integer>() {
        @Override
        public Integer apply(String s) {
            return Integer.parseInt(s) + 100;
        }
    };
}
