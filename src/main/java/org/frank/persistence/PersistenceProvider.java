package org.frank.persistence;

import org.frank.persistence.database.PostgreSQLStorage;

import java.sql.SQLException;

public class PersistenceProvider {
    public interface Storage<T, ID> {
        void save(T entity) throws SQLException;

        T get(ID id) throws SQLException;
    }

    public static <T, ID> Storage<T, ID> storage(Class<T> clazz) throws SQLException {
        /*return new Storage<T>() {
            @Override
            public void save(T entity) {
                System.out.println("Save entity " + entity.toString());
            }
        };*/
        return new PostgreSQLStorage<>(clazz);
    }
}