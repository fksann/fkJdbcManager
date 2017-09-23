package Mapper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class AquaJdbcManager {

	protected Connection connection;
	protected AutoSelect autoSelect;
	private AutoUpdate autoUpdate;
	private BatchUpdate batchUpdate;

	public AquaJdbcManager(String db_url, String db_user_id, String db_user_password) throws SQLException {
		connection = DriverManager.getConnection(db_url, db_user_id, db_user_password);
	}

	// selectを実行する。
	public SqlSelect selectBySql(String sql) {
		SqlSelect directSqlExecuter = new SqlSelect(connection, sql);
		return directSqlExecuter;
	}

	public AquaJdbcManager select(String... columns) {
		autoSelect = new AutoSelect(connection).makeSelectSql(columns);
		return this;
	}

	public AutoSelect from(String tableNm) {
		if (autoSelect == null) {
			autoSelect = new AutoSelect(this.connection).makeFromSql(tableNm);
		} else {
			autoSelect = autoSelect.makeFromSql(tableNm);
		}

		return autoSelect;
	}

	// update,insert,deleteを実行する。
	public SqlUpdate updateBySql(String sql) {
		SqlUpdate directSqlExecuter = new SqlUpdate(connection, sql);
		return directSqlExecuter;
	}

	public SqlUpdate updateBySql(String sql, Class<?>... paramClasses) {
		SqlUpdate directSqlExecuter = new SqlUpdate(connection, sql, paramClasses);
		return directSqlExecuter;
	}

	public AutoUpdate insert(String tableNm) {
		autoUpdate = new AutoUpdate(connection);
		return autoUpdate.makeInsretSql(tableNm);
	}

	public AutoUpdate update(String tableNm) {
		autoUpdate = new AutoUpdate(connection);
		return autoUpdate.makeUpdateSql(tableNm);
	}

	public AutoUpdate delete(String tableNm) {
		autoUpdate = new AutoUpdate(connection);
		return autoUpdate.makeDeleteSql(tableNm);
	}

	public BatchUpdate insertBatch(String tableNm) {
		AutoUpdate automatedSqlExecuter = new AutoUpdate(connection);
		automatedSqlExecuter = automatedSqlExecuter.makeInsretSql(tableNm);
		batchUpdate = new BatchUpdate(automatedSqlExecuter);
		return batchUpdate;
	}

	public BatchUpdate updateBatch(String tableNm) {
		AutoUpdate automatedSqlExecuter = new AutoUpdate(connection);
		automatedSqlExecuter = automatedSqlExecuter.makeUpdateSql(tableNm);
		batchUpdate = new BatchUpdate(automatedSqlExecuter);
		return batchUpdate;
	}

	public BatchUpdate deleteBatch(String tableNm) {
		AutoUpdate automatedSqlExecuter = new AutoUpdate(connection);
		automatedSqlExecuter = automatedSqlExecuter.makeDeleteSql(tableNm);
		batchUpdate = new BatchUpdate(automatedSqlExecuter);
		return batchUpdate;
	}

	// 実行したsqlの内容を表示
	public SqlLog getLastSqlLog() {
		if (autoSelect != null) {
			return autoSelect.sqlLog;
		} else if (batchUpdate != null) {
			return batchUpdate.sqlLog;
		} else if (autoUpdate != null) {
			return autoUpdate.sqlLog;

		} else {
			return null;
		}
	}

}