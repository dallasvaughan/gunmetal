package scope;

import com.github.overengineer.gunmetal.key.Generic;
import com.github.overengineer.gunmetal.module.BaseModule;
import scope.monitor.*;

import java.util.Collections;
import java.util.Set;

/**
 *
 */
public class CommonModule extends BaseModule {

    @Override
    public void configure() {

        use(EmptyActionProvider.class).forType(ActionProvider.class);

        use(new Generic<ScheduledExecutorTimeoutMonitor<FakeTimeoutable>>(){})
                .forType(TimeoutMonitor.class)
                .forType(new Generic<TimeoutMonitor<FakeTimeoutable>>() {});

        use(DefaultSchedulerProvider.class).forType(SchedulerProvider.class);

        use(CommonConstants.Defaults.MONITORING_FREQUENCY).withQualifier(CommonConstants.Properties.MONITORING_FREQUENCY);

        use(CommonConstants.Defaults.MONITORING_THREAD_POOL_SIZE).withQualifier(CommonConstants.Properties.MONITORING_THREAD_POOL_SIZE);
    }

    public static class FakeTimeoutable implements Timeoutable<FakeTimeoutable> {

        @Override
        public void setMaxIdleTime(long maxIdleTime) {
        }

        @Override
        public String getId() {
            return null;
        }

        @Override
        public long getRemainingTime() {
            return 0;
        }

        @Override
        public void timeout() {
        }

        @Override
        public void reset() {
        }

        @Override
        public void addTimeoutListener(TimeoutListener timeoutListener) {
        }
    }

    public static class EmptyActionProvider implements ActionProvider {

        @Override
        public Set<Class<?>> getActionClasses() {
            return Collections.emptySet();
        }

    }
    
}
