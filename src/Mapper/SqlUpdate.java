package Mapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class SqlUpdate {

	private Connection connection;
	private String sql;
	private List<Object> valueList;
	/**
	 * パラメータのクラスの配列です。
	 */
	protected Class<?>[] paramClasses;

	/**
	 * パラメータの配列です。
	 */
	protected Object[] params = new Object[0];

	public SqlUpdate(Connection Connection, String sql, Class<?>... paramClasses) {
		this.connection = Connection;
		this.sql = sql;
		this.paramClasses = paramClasses;
	}

	public SqlUpdate(Connection connection, String sql) {
		this(connection, sql, null);
	}

	public SqlUpdate params(Object... params) {
		this.params = params;
		return this;
	}

	public int execute() throws SQLException {
		PreparedStatement ps = connection.prepareStatement(sql);
		if (paramClasses == null) {
			return ps.executeUpdate();
		} else {
			for (int i = 0; i < params.length; i++) {
				BindValue.setParam(i+1,params[i], paramClasses[i],ps);
			}
			return ps.executeUpdate();
		}
	}

}
