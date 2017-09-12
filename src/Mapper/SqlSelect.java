package Mapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class SqlSelect {

	private Connection connection;
	private String sql;

	public SqlSelect(Connection Connection, String sql) {
		this.connection = Connection;
		this.sql = sql;
	}

	public List<Map<String, Object>> getResultList() throws SQLException {
		PreparedStatement ps = connection.prepareStatement(sql);
		return Select.executeMultiple(ps);
	}

	public Map<String, Object> getSingleResult() throws SQLException {
		PreparedStatement ps = connection.prepareStatement(sql);
		return Select.executeSingle(ps);
	}

	public long getCount() throws SQLException {
		PreparedStatement ps = connection.prepareStatement(sql);
		return Select.executeCount(ps);
	}
}
