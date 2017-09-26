package Mapper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class AquaJdbcManager {

	protected Connection connection;
	protected AutoSelect autoSelect;
	private AutoUpdate autoUpdate;
	private BatchUpdate batchUpdate;

	//コンストラクタ
	public AquaJdbcManager(String dbUrl, String dbUserId, String dbUserPassword) throws SQLException {
		connection = DriverManager.getConnection(dbUrl, dbUserId, dbUserPassword);
		connection.setAutoCommit(true);
	}

	public AquaJdbcManager(String dbUrl, String dbUserId, String dbUserPassword, boolean autoCommit) throws SQLException {
		connection = DriverManager.getConnection(dbUrl, dbUserId, dbUserPassword);
		connection.setAutoCommit(autoCommit);
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

	//トランザクション処理
	public void setAutoCommit(boolean autoCommit) throws SQLException{
		connection.commit();
		connection.setAutoCommit(autoCommit);
	}

	public boolean getAutoCommit() throws SQLException{
		return connection.getAutoCommit();
	}

	public void rollback() throws SQLException {
		connection.rollback();
	}

	public void commit() throws SQLException {
		connection.commit();
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