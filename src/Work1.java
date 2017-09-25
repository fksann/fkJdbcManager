
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Mapper.AquaJdbcManager;
import Mapper.SqlLog;

public class Work1 {

	public static void main(String[] arg) throws SQLException, ClassNotFoundException {
		AquaJdbcManager ajm = new AquaJdbcManager("jdbc:mysql://localhost:3306/data?characterEncoding=UTF-8", "root",
				"3927");

		List<Map<String, Object>> list = new ArrayList<>();
		Map<String, Object> map1 = new HashMap<String, Object>();
		map1.put("emp_id",65 );//現在のid
		map1.put("emp_nm", "鶏肉");
		map1.put("version", 6);//現在のversion
		list.add(map1);
		Map<String, Object> map2 = new HashMap<String, Object>();
		map2.put("emp_id",76 );//現在のid
		map2.put("emp_nm", "チョコクッキー");
		map2.put("version", 4);//現在のversion
		list.add(map2);

		ajm.updateBatch("emp_mst").params(list).where("emp_id").version("version").execute();

		SqlLog lastSqlLog = ajm.getLastSqlLog();
		System.out.println(lastSqlLog.getRawSql());// "insert into table (id , nm) values (? ,?);"
		System.out.println(lastSqlLog.getCompleteSql());// "insert into table (id , nm) values (1 ,'名前');"
		Object[] args = lastSqlLog.getBindArgs();//[1 ,"名前"]
		Class<?>[] argTypes = lastSqlLog.getBindArgTypes();//[Integer.class ,String.class ]

		System.out.println("hello.");
	}
}

// カラムやvalueはStringで渡すようになっている。
// valueをリストやセットで渡すことは現状できない。
// SimpleWhere().excludesWhitespace()も実装していない。
// ajmを使いまわせなくなっています。