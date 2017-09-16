package Mapper;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BatchUpdate {

	private List<Object> valueList = new ArrayList<>();
	private List<Map<String, Object>> list = new ArrayList<>();
	private AutoUpdate automatedSqlExecuter;
	private int batchCnt;

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
				valueList.addAll(map.values());
			}
			for (int i = 0; i < cols.length; i++) {
				valueList.add(map.get(cols[i]));
			}
		}
	}

	public int execute() throws SQLException {
		PreparedStatement ps = automatedSqlExecuter.connection.prepareStatement(automatedSqlExecuter.sql);

		makeBatch(ps);

		int[] updateCounts = ps.executeBatch();

		return sum(updateCounts);
	}

	private void makeBatch(PreparedStatement ps) throws SQLException {
		for (int i = 0; i < batchCnt; i++) {
			for (int j = 0; j < ps.getParameterMetaData().getParameterCount(); j++) {
				BindValue.setParam(j + 1, valueList.get(j + i * ps.getParameterMetaData().getParameterCount()),
						valueList.get(j).getClass(), ps);
			}
			ps.addBatch();
		}
	}

	private int sum(int[] updateCounts) {
		int sum = 0;
		for (int i = 0; i < updateCounts.length; i++) {
			sum += updateCounts[i];
		}
		return sum;
	}

}
