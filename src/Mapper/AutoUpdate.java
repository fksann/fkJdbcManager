package Mapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AutoUpdate {

	protected String sql;
	protected Connection connection;
	private List<Object> valueList = new ArrayList<>();

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

	public void setColsVals(Map<String, Object> map) {
		int i = 0;
		if (sql.contains("insert")) {
			String colStr = "", valueStr = "";
			for (String col : map.keySet()) {
				i++;
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
				if (i == map.size()) {
					sql += col + "=?";
				} else {
					sql += col + "=?,";
				}
			}
		}
	}

	public AutoUpdate where(Where condtion) {
		sql += condtion.whereStr;
		valueList.addAll(condtion.valueList);
		return this;
	}

	public int execute() throws SQLException {
		PreparedStatement ps = connection.prepareStatement(sql);
		for (int i = 0; i < valueList.size(); i++) {
			BindValue.setParam(i + 1, valueList.get(i), valueList.get(i).getClass(), ps);
		}
		try {
			return ps.executeUpdate();
		} catch (com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException e) {
			throw new javax.persistence.EntityExistsException("DBの一意制約違反");
		}
	}



	public AutoUpdate params(List<Map<String, Object>> list) {

		for(Map<String,Object> map:list){

		}
		return null;
	}
}
