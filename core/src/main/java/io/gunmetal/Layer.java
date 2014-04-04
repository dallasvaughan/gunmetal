package io.gunmetal;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * if any layers are specified in the ApplicationModule, then each system module is required
 * to declare itself as a member of one and only one of the layers in the application module
 *
 *  <pre>
 *  {@code
 *
 *  {@literal @}ApplicationModule(
 *      layers = {
 *          UI.class, BL.class
 *      }
 *  )
 *
 *  {@literal @}Module(layer = UI.class)
 *
 *  {@literal @}BL //qualifier
 *  {@literal @}Layer.Uses(DAL.class)
 *  {@literal @}Layer.UsedBy(UI.class)
 *  public class BL implements Layer { }
 *
 *
 *  }
 *  </pre>
 *
 * @author rees.byars
 *
 */
public interface Layer {

    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    public @interface Uses {
        Class<? extends Layer>[] value();
    }

    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    public @interface UsedBy {
        Class<? extends Layer>[] value();
    }

}
