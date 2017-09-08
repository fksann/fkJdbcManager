package Mapper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class AquaJdbcManager {

	Connection connection;
	AutoSelect automatedSqlExecuter;

	public AquaJdbcManager(String db_url, String db_user_id, String db_user_password) throws SQLException {
		this.connection = DriverManager.getConnection(db_url, db_user_id, db_user_password);
	}

	public SqlSelect selectBySql(String sql) {
		SqlSelect directSqlExecuter = new SqlSelect(this, sql);
		return directSqlExecuter;
	}

	public AquaJdbcManager select(String... columns) {
		this.automatedSqlExecuter = new AutoSelect(this.connection).makeSelectSql(columns);
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

}