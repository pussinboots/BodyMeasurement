package org.frank.persistence.database;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import org.frank.persistence.PersistenceProvider;

import java.sql.SQLException;

public class PostgreSQLStorage<T, ID> implements PersistenceProvider.Storage<T, ID> {

    private Dao<T, ID> dao;

    public PostgreSQLStorage(Class<T> clazz) throws SQLException {
        ConnectionSource connectionSource = new JdbcConnectionSource(JDBCUrlResolver.jdbcUrl());
        this.dao = DaoManager.createDao( connectionSource, clazz );
    }

    @Override
    public void save(T entity) throws SQLException {
        dao.create(entity);
    }

    @Override
    public T get(ID id) throws SQLException {
        return dao.queryForId(id);
    }
}
