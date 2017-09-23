package Mapper;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BatchUpdate {

	private List<Object> valueList = new ArrayList<>();
	private List<List<Object>> valueLists = new ArrayList<>();
	private List<Map<String, Object>> list = new ArrayList<>();
	private AutoUpdate automatedSqlExecuter;
	private int batchCnt;
	protected SqlLog sqlLog;

	public BatchUpdate(AutoUpdate automatedSqlExecuter) {
		this.automatedSqlExecuter = automatedSqlExecuter;
	}

	public BatchUpdate params(List<Map<String, Object>> list) {
		automatedSqlExecuter.setColsVals(list.get(0));
		if (automatedSqlExecuter.sql.contains("insert")) {
			for (Map<String, Object> map : list) {
				valueList.addAll(map.values());
			}
		}
		batchCnt = list.size();
		this.list = list;
		return this;
	}

	public BatchUpdate where(String... cols) {
		makeConditions(cols);
		setVals(cols);
		return this;
	}

	private void makeConditions(String[] cols) {
		automatedSqlExecuter.sql += " where " + cols[0] + "=?";
		for (int i = 1; i < cols.length; i++) {
			automatedSqlExecuter.sql += " and " + cols[i] + "=?";
		}
	}

	private void setVals(String[] cols) {
		for (Map<String, Object> map : list) {
			if (automatedSqlExecuter.sql.contains("update")) {
				List<Object> valueList = new ArrayList<>();
				for (String col : map.keySet()) {
					if (col.equals("version")) {
						continue;
					} else {
						valueList.add(map.get(col));
					}
				}
				for (int i = 0; i < cols.length; i++) {
					valueList.add(map.get(cols[i]));
				}
				valueLists.add(valueList);
			} else {
				for (int i = 0; i < cols.length; i++) {
					valueList.add(map.get(cols[i]));
				}
			}
		}
	}

	public BatchUpdate version(String string) {
		automatedSqlExecuter.sql += " and version=?";
		for (int i = 0; i < list.size(); i++) {
			valueLists.get(i).add(list.get(i).get("version"));
		}
		return this;
	}

	public int execute() throws SQLException, ClassNotFoundException {
		PreparedStatement ps = automatedSqlExecuter.connection.prepareStatement(automatedSqlExecuter.sql);

		makeBatch(ps);
		saveLog(ps);

		try {
			int[] updateCounts = ps.executeBatch();

			List<Integer> l = new ArrayList<>();
			for (int i = 0; i < updateCounts.length; i++) {
				l.add(updateCounts[i]);
			}
			if (l.contains(0)) {
				String errIndex = "";
				for (int i = 0; i < updateCounts.length; i++) {
					if (updateCounts[i] == 0) {
						if (errIndex.contains("目")) {
							errIndex += "、";
						}
						errIndex += i + 1 + "つ目";
					}
				}
				throw new javax.persistence.OptimisticLockException(errIndex + "の処理は、他のユーザーに先に更新処理されている");

			} else {
				return sum(updateCounts);
			}
		} catch (com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException e) {
			throw new javax.persistence.EntityExistsException("DBの一意制約違反");
		}

	}

	private void makeBatch(PreparedStatement ps) throws SQLException {
		if (automatedSqlExecuter.sql.contains("update")) {
			for (List<Object> l : valueLists) {
				valueList.addAll(l);
			}
		}
		for (int i = 0; i < batchCnt; i++) {
			for (int j = 0; j < ps.getParameterMetaData().getParameterCount(); j++) {
				BindValue.setParam(j + 1, valueList.get(j + i * ps.getParameterMetaData().getParameterCount()),
						valueList.get(j).getClass(), ps);
			}
			ps.addBatch();
		}
	}

	private void saveLog(PreparedStatement ps) throws ClassNotFoundException, SQLException {
		sqlLog = new SqlLog(automatedSqlExecuter.sql, makeCompleteSql(ps), valueList.toArray(), getClassArray(ps));
	}

	private String makeCompleteSql(PreparedStatement ps) throws SQLException {
		String completeSql = automatedSqlExecuter.sql;

		for (int i = 0; i < valueLists.size() - 1; i++) {
			completeSql += "\n" + automatedSqlExecuter.sql;
		}

		for (int i = 0; i < ps.getParameterMetaData().getParameterCount() * valueLists.size(); i++) {
			completeSql = completeSql.replaceFirst("\\?", valueList.get(i).toString());
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

	private int sum(int[] updateCounts) {
		int sum = 0;
		for (int i = 0; i < updateCounts.length; i++) {
			sum += updateCounts[i];
		}
		return sum;
	}

}
