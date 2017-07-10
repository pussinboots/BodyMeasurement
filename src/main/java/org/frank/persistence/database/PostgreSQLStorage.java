package org.frank.persistence.database;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import org.frank.persistence.PersistenceProvider;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class PostgreSQLStorage<T, ID> extends PersistenceProvider.AbstractStorage<T, ID> {

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

    @Override
    public List<T> list(Map<String, Object> fields) throws SQLException {
        return (fields == null) ? dao.queryForAll() :
                                  dao.queryForFieldValues(fields);
    }

    @Override
    public void close() throws IOException {
        dao.getConnectionSource().close();
    }
}
