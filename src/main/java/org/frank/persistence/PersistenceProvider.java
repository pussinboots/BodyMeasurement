package org.frank.persistence;

public class PersistenceProvider {
    public interface Storage<T> {
        void save(T entity);
    }

    public static <T> Storage<T> storage() {
        return new Storage<T>() {
            @Override
            public void save(T entity) {
                System.out.println("Save entity " + entity.toString());
            }
        };
    }
}