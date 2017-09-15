package Mapper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class AquaJdbcManager {

	protected Connection connection;
	protected AutoSelect automatedSqlExecuter;

	public AquaJdbcManager(String db_url, String db_user_id, String db_user_password) throws SQLException {
		connection = DriverManager.getConnection(db_url, db_user_id, db_user_password);
	}

	// select
	public SqlSelect selectBySql(String sql) {
		SqlSelect directSqlExecuter = new SqlSelect(connection, sql);
		return directSqlExecuter;
	}

	public AquaJdbcManager select(String... columns) {
		automatedSqlExecuter = new AutoSelect(connection).makeSelectSql(columns);
		return this;
	}

	public AutoSelect from(String tableNm) {
		if (this.automatedSqlExecuter == null) {
			this.automatedSqlExecuter = new AutoSelect(this.connection).makeFromSql(tableNm);
		} else {
			this.automatedSqlExecuter = this.automatedSqlExecuter.makeFromSql(tableNm);
		}

		return automatedSqlExecuter;
	}

	// update,insert,delete
	public SqlUpdate updateBySql(String sql) {
		SqlUpdate directSqlExecuter = new SqlUpdate(connection, sql);
		return directSqlExecuter;
	}

	public SqlUpdate updateBySql(String sql, Class<?>... paramClasses) {
		SqlUpdate directSqlExecuter = new SqlUpdate(connection, sql, paramClasses);
		return directSqlExecuter;
	}

	public AutoUpdate insert(String tableNm) {
		AutoUpdate automatedSqlExecuter = new AutoUpdate(connection);
		return automatedSqlExecuter.makeInsretSql(tableNm);
	}

	public AutoUpdate update(String tableNm) {
		AutoUpdate automatedSqlExecuter = new AutoUpdate(connection);
		return automatedSqlExecuter.makeUpdateSql(tableNm);
	}

	public AutoUpdate delete(String tableNm) {
		AutoUpdate automatedSqlExecuter = new AutoUpdate(connection);
		return automatedSqlExecuter.makeDeleteSql(tableNm);
	}

	public BatchUpdate insertBatch(String tableNm) {
		AutoUpdate automatedSqlExecuter = new AutoUpdate(connection);
		automatedSqlExecuter=automatedSqlExecuter.makeInsretSql(tableNm);
		return new BatchUpdate(automatedSqlExecuter);
	}

}