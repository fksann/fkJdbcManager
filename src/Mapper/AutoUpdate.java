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
	private Map<String, Object> map;
	private List<Object> valueList = new ArrayList<>();
	protected SqlLog sqlLog;

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
		for (String col : map.keySet()) {
			if (col.equals("version")) {
				continue;
			} else {
				valueList.add(map.get(col));
			}
		}
		return this;
	}

	public void setColsVals(Map<String, Object> map) {
		int i = 0;
		this.map = map;
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
		} else if (sql.contains("update")) {
			for (String col : map.keySet()) {
				i++;
				if (col.equals("version")) {
					sql += "version=version+1";
					continue;
				}
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

	public AutoUpdate where(String... cols) {
		makeConditions(cols);
		setVals(cols);
		return this;
	}

	private void makeConditions(String[] cols) {
		sql += " where " + cols[0] + "=?";
		for (int i = 1; i < cols.length; i++) {
			sql += " and " + cols[i] + "=?";
		}
	}

	private void setVals(String[] cols) {
		for (String col : cols) {
			valueList.add(map.get(col));
		}

	}

	public AutoUpdate version(String version) {
		sql += " and version=?";
		valueList.add(map.get(version));
		return this;
	}

	public int execute() throws SQLException, ClassNotFoundException {
		PreparedStatement ps = connection.prepareStatement(sql);
		for (int i = 0; i < valueList.size(); i++) {
			BindValue.setParam(i + 1, valueList.get(i), valueList.get(i).getClass(), ps);
		}

		saveLog(ps);

		try {
			int i = ps.executeUpdate();
			if (i == 0) {
				throw new javax.persistence.OptimisticLockException("他のユーザーに先に更新処理されている");
			} else {
				return i;
			}
		} catch (com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException e) {
			throw new javax.persistence.EntityExistsException("DBの一意制約違反");
		}
	}

	private void saveLog(PreparedStatement ps) throws ClassNotFoundException, SQLException {
		sqlLog = new SqlLog(sql, makeCompleteSql(ps), valueList.toArray(), getClassArray(ps));
	}

	private String makeCompleteSql(PreparedStatement ps) throws SQLException {
		String completeSql = sql;

		for(int i =0;i<ps.getParameterMetaData().getParameterCount();i++){
			completeSql=completeSql.replaceFirst("\\?", valueList.get(i).toString());
		}

		return completeSql;
	}

	private Class<?>[] getClassArray(PreparedStatement ps) throws SQLException, ClassNotFoundException {
		List<Class<?>> classList = new ArrayList<>();

		for (int i = 1; i <= ps.getParameterMetaData().getParameterCount(); i++) {
			String classNm = ps.getParameterMetaData().getParameterClassName(i);
			classList.add(Class.forName(classNm));
		}

		return classList.toArray(new Class[classList.size()]);
	}
}
