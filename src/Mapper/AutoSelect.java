package Mapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class AutoSelect {

	private Connection connection;
	private String sql;
	private List<String> valueList;
	private int maxRows;

	public AutoSelect(Connection connection) {
		this.connection = connection;
	}

	public AutoSelect makeSelectSql(String... columns) {
		for (int i = 0; i < columns.length; i++) {
			if (i == 0) {
				this.sql = "select " + columns[i];
			} else {
				this.sql = this.sql + "," + columns[i];
			}
		}
		return this;
	}

	public AutoSelect makeFromSql(String tableNm) {
		if (this.sql == null) {
			this.sql = "select * from " + tableNm;
		} else {
			this.sql = this.sql + " from " + tableNm;
		}
		return this;
	}

	public AutoSelect where(Where whereConditions) {
		this.sql = this.sql + whereConditions.whereStr;
		this.valueList = whereConditions.valueList;
		return this;
	}

	public AutoSelect where(String... conditions) {
		for (int i = 0; i < conditions.length; i++) {
			if (i == 0) {
				this.sql += " where (" + conditions[i] + ")";
			} else {
				this.sql += " and " + "(" + conditions[i] + ")";
			}
		}
		return this;
	}

	public AutoSelect orderBy(String... columns) {
		for (int i = 0; i < columns.length; i++) {
			if (i == 0) {
				this.sql = this.sql + " order by " + columns[i];
			} else {
				this.sql = this.sql + ", " + columns[i];
			}
		}
		return this;
	}

	public AutoSelect limit(int limit) {
		this.sql = this.sql + " limit " + limit;
		return this;
	}

	public AutoSelect offset(int offset) {
		this.sql = this.sql + " offset " + offset;
		return this;
	}

	public AutoSelect maxRows(int maxRows) {
		this.maxRows = maxRows;
		return this;
	}

	public AutoSelect innerJoin(String tableNm, String joinColumn) {
		this.sql += " inner join " + tableNm + " on " + joinColumn;
		return this;
	}

	public AutoSelect leftOuterJoin(String tableNm, String joinColumn) {
		this.sql += " left outer join " + tableNm + " on " + joinColumn;
		return this;
	}

	public List<Map<String, Object>> getResultList() throws SQLException {
		PreparedStatement ps = makePreparedStatement();
		return Select.executeMultiple(ps);
	}

	public Map<String, Object> getSingleResult() throws SQLException {
		PreparedStatement ps = makePreparedStatement();
		return Select.executeSingle(ps);
	}

	public long getCount() throws SQLException {
		this.sql = this.sql.replace(
				this.sql.substring(this.sql.lastIndexOf("select") + 6, this.sql.lastIndexOf("from")), " count(*) ");
		PreparedStatement ps = makePreparedStatement();
		return Select.executeCount(ps);
	}

	public PreparedStatement makePreparedStatement() throws SQLException {
		PreparedStatement ps = connection.prepareStatement(sql);
		if (valueList != null) {
			for (int i = 0; i < valueList.size(); i++) {
				ps.setString(i + 1, valueList.get(i));
			}
		}
		statementUtils(ps);
		return ps;
	}

	public void statementUtils(PreparedStatement ps) throws SQLException {
		if (this.maxRows > 0) {
			ps.setMaxRows(this.maxRows);
		}
	}

}