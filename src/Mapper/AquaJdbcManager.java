package Mapper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class AquaJdbcManager {

	Connection connection;
	AutoSelect automatedSqlExecuter;

	public AquaJdbcManager(String db_url, String db_user_id, String db_user_password) throws SQLException {
		connection = DriverManager.getConnection(db_url, db_user_id, db_user_password);
	}

	//select
	public SqlSelect selectBySql(String sql) {
		SqlSelect directSqlExecuter = new SqlSelect(this, sql);
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

	//update,insert,delete
	public SqlUpdate updateBySql(String sql){
		return null;
	}

}