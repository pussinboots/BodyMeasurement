package org.frank.providers;

import org.frank.persistence.PersistenceProvider;
import org.frank.persistence.database.BodyMeasurementDB;
import org.glassfish.hk2.api.Factory;

import java.io.IOException;
import java.sql.SQLException;

public class StorageFactory implements Factory<PersistenceProvider.Storage<?, ?>> {

    @Override
    public PersistenceProvider.Storage<?, ?> provide() {
        try {
            return PersistenceProvider.storage(BodyMeasurementDB.class);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void dispose(PersistenceProvider.Storage<?, ?> storage) {
        try {
            storage.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
