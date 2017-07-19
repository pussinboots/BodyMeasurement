package org.frank.resources;

import org.frank.persistence.PersistenceProvider;
import org.frank.persistence.database.BodyMeasurementDB;
import org.frank.providers.StorageFactory;
import org.glassfish.hk2.api.TypeLiteral;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.process.internal.RequestScoped;
import org.glassfish.jersey.server.ResourceConfig;

public class BodyMeasurementApplication extends ResourceConfig {

    public BodyMeasurementApplication() {
        register(new MyBinder());
        packages("org.frank");
    }

    public static class MyBinder extends AbstractBinder {
        @Override
        protected void configure() {
            bindFactory(StorageFactory.class).to(new TypeLiteral<PersistenceProvider.Storage<BodyMeasurementDB, Long>>(){})
                    .proxy(true).proxyForSameScope(false).in(RequestScoped.class);
        }
    }

}
