/*
 * Copyright (c) 2013.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.gunmetal.internal;

import java.lang.reflect.Method;

/**
 * @author rees.byars
 */
interface Injectors {

    interface Factory {
        StaticInjector staticInjector(Method method, ComponentMetadata componentMetadata);
        <T> Injector<T> composite(ComponentMetadata componentMetadata);
        <T> Injector<T> lazy(ComponentMetadata componentMetadata);
        <T> Instantiator<T> constructorInstantiator(ComponentMetadata<Class> componentMetadata);
        <T> Instantiator<T> methodInstantiator(ComponentMetadata<Method> componentMetadata);
    }

    interface StaticInjector extends Dependent {
        Object inject(InternalProvider internalProvider, ResolutionContext resolutionContext);
    }

    interface Injector<T> extends Dependent {
        Object inject(T target, InternalProvider internalProvider, ResolutionContext resolutionContext);
    }

    interface Instantiator<T> extends Dependent {

        T getInstance(InternalProvider provider, ResolutionContext resolutionContext);

        T getInstance(InternalProvider provider, ResolutionContext resolutionContext, Object... providedArgs);

    }

}
