package Mapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class AutoUpdate {

	private String sql;
	private Connection connection;
	private List<Object> valueList;

	public AutoUpdate(Connection connection) {
		this.connection = connection;
	}

	public AutoUpdate makeInsretSql(String tableNm) {
		sql = "insert into " + tableNm;
		return this;
	}

	public AutoUpdate makeUpdateSql(String tableNm) {
		sql = "update " + tableNm + " set ";
		return this;
	}

	public AutoUpdate makeDeleteSql(String tableNm) {
		sql = "delete from " + tableNm;
		return this;
	}

	public AutoUpdate params(Map<String, Object> map) {
		setColsVals(map);
		valueList.addAll(map.values());
		return this;
	}

	private void setColsVals(Map<String, Object> map) {
		int i = 0;
		if (sql.contains("insert")) {
			String colStr = "", valueStr = "";
			for (String col : map.keySet()) {
				i++;
				valueList.add(map.get(col));
				if (i == map.size()) {
					colStr += col;
					valueStr += "?";
				} else {
					colStr += col + ",";
					valueStr += "?,";
				}
			}
			sql += " (" + colStr + ") values(" + valueStr + ");";
		} else {
			for (String col : map.keySet()) {
				i++;
				valueList.add(map.get(col));
				if (i == map.size()) {
					sql += col + "=?";
				} else {
					sql += col + "=?,";
				}
			}
		}
	}

	public AutoUpdate where(Where condtion) {
		return this;
	}

	public int execute() throws SQLException {
		PreparedStatement ps = connection.prepareStatement(sql);

		return ps.executeUpdate();
	}

}
