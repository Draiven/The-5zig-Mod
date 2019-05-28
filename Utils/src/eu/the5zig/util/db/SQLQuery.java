package eu.the5zig.util.db;

import eu.the5zig.util.Callback;
import eu.the5zig.util.db.exceptions.NoConnectionException;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.UUID;

/**
 * Created by 5zig.
 * All rights reserved © 2015
 */
public class SQLQuery<T> {

	protected Database database;
	protected Class<T> entityClass;

	public SQLQuery(Database database, Class<T> entity) {
		this.database = database;
		this.entityClass = entity;
	}

	/**
	 * Executes a MySQL Query and returns a List of all affected Entities.
	 *
	 * @param query  The Prepared Statement Query. Use {@code ?} for fields.
	 * @param fields The Fields that should be inserted into the Statement.
	 */
	public void query(final Callback<SQLResult<T>> callback, final String query, final Object... fields) {
		database.EXECUTOR.execute(new Runnable() {
			@Override
			public void run() {
				callback.call(query(query, fields));
			}
		});
	}

	/**
	 * Executes a MySQL Query and returns a List of all affected Entities.
	 *
	 * @param query  The Prepared Statement Query. Use {@code ?} for fields.
	 * @param fields The Fields that should be inserted into the Statement.
	 * @return A List of {@code EntityBase} classes that contain the Result Set of the Query.
	 * The Classes need to be registered using {@code register(Class entity, String... tables)}.
	 */
	public SQLResult<T> query(String query, Object... fields) {
		Connection connection;
		try {
			connection = database.getConnection();
		} catch (NoConnectionException e) {
			database.getLogger().debug(e);
			return new SQLResult<T>();
		}
		if (connection == null)
			throw new RuntimeException("Connection is Null!");

		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = connection.prepareStatement(query);
			for (int i = 0; i < fields.length; i++) {
				st.setObject(i + 1, fields[i]);
			}
			rs = st.executeQuery();
			SQLResult<T> result = new SQLResult<T>();

			while (rs.next()) {
				ResultSetMetaData metaData = rs.getMetaData();

				try {
					T entity = entityClass.newInstance();
					int columns = metaData.getColumnCount();
					for (int i = 1; i <= columns; i++) {
						Field field;
						try {
							field = entity.getClass().getDeclaredField(metaData.getColumnName(i));
						} catch (NoSuchFieldException e) {
							continue;
						}
						Object value = rs.getObject(i);
						if (field == null || value == null)
							continue;

						field.setAccessible(true);
						if (UUID.class.isAssignableFrom(field.getType()))
							field.set(entity, UUID.fromString(rs.getString(i)));
						else if (boolean.class.isAssignableFrom(field.getType()))
							field.set(entity, rs.getBoolean(i));
						else
							field.set(entity, value);
						field.setAccessible(false);
					}
					result.add(entity);
				} catch (InstantiationException e) {
					database.getLogger().warn("Failed to Instantiate a Field via Reflection", e);
				} catch (IllegalAccessException e) {
					database.getLogger().warn("Failed to Access a Field via Reflection", e);
				}
			}
			return result;
		} catch (SQLException e) {
			database.getLogger().warn("Could not Execute MySQL Update " + query, e);
			return new SQLResult<T>();
		} finally {
			database.closeResources(rs, st);
		}
	}

}
