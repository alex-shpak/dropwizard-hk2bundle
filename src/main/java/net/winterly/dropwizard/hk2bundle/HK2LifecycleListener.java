package net.winterly.dropwizard.hk2bundle;

import net.winterly.dropwizard.hk2bundle.validation.HK2ValidationBundle;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.jersey.server.monitoring.ApplicationEvent;
import org.glassfish.jersey.server.monitoring.ApplicationEventListener;
import org.glassfish.jersey.server.monitoring.RequestEvent;
import org.glassfish.jersey.server.monitoring.RequestEventListener;

import javax.inject.Inject;

public class HK2LifecycleListener implements ApplicationEventListener {

    @Inject
    private ServiceLocator serviceLocator;

    @Inject
    private HK2ValidationBundle hk2ValidationBundle;

    @Override
    public void onEvent(ApplicationEvent event) {
        if (event.getType() == ApplicationEvent.Type.INITIALIZATION_START) {
            serviceLocator.inject(hk2ValidationBundle);
        }
    }

    @Override
    public RequestEventListener onRequest(RequestEvent requestEvent) {
        return null;
    }
}
