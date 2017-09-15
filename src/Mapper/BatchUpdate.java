package Mapper;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BatchUpdate {

	private List<Object> valueList = new ArrayList<>();
	private AutoUpdate automatedSqlExecuter;
	private int batchCnt;

	public BatchUpdate(AutoUpdate automatedSqlExecuter) {
		this.automatedSqlExecuter = automatedSqlExecuter;
	}

	public BatchUpdate params(List<Map<String, Object>> list) {
		automatedSqlExecuter.setColsVals(list.get(0));
		for (Map<String, Object> map : list) {
			valueList.addAll(map.values());
		}
		batchCnt = list.size();
		return this;
	}

	public int execute() throws SQLException {
		PreparedStatement ps = automatedSqlExecuter.connection.prepareStatement(automatedSqlExecuter.sql);
		for (int i = 0; i < batchCnt; i++) {
			for (int j = 0; j < ps.getParameterMetaData().getParameterCount(); j++) {
				BindValue.setParam(j+1, valueList.get(j + i * ps.getParameterMetaData().getParameterCount()),
						valueList.get(j).getClass(), ps);
			}
			ps.addBatch();
		}
		int[] updateCounts = ps.executeBatch();
		int sum = 0;
		for (int i = 0; i < updateCounts.length; i++) {
			sum += updateCounts[i];
		}
		return sum;
	}

}
