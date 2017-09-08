package Mapper;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class SqlSelect {

	AquaJdbcManager ajm;
	String sql;

	public SqlSelect(AquaJdbcManager ajm, String sql) {
		this.ajm = ajm;
		this.sql = sql;
	}

	public List<Map<String, Object>> getResultList() throws SQLException{
		Select sl =new Select(this.ajm,this.sql);
		return sl.executeMultiple();
	}
}
