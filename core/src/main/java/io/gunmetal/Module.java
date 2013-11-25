package io.gunmetal;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author rees.byars
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Module {
    Component[] components() default { };
    Class[] dependsOn() default { };
    Class<? extends BlackList> notAccessibleFrom() default BlackList.class;
    Class<? extends WhiteList> onlyAccessibleFrom() default WhiteList.class;
    AccessLevel access() default AccessLevel.UNDEFINED;
}
