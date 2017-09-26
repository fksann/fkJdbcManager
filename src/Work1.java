
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Mapper.AquaJdbcManager;

public class Work1 {

	public static void main(String[] arg) throws SQLException, ClassNotFoundException {
		AquaJdbcManager ajm = new AquaJdbcManager("jdbc:mysql://localhost:3306/data?characterEncoding=UTF-8", "root",
				"3927",false);

		List<Map<String, Object>> list1 = new ArrayList<>();
		Map<String, Object> map1 = new HashMap<>();
		map1.put("emp_id", 100);
		map1.put("emp_nm", "松井稼頭央");
		map1.put("join_date", new Date());
		list1.add(map1);
		Map<String, Object> map2 = new HashMap<>();
		map2.put("emp_id", 101);
		map2.put("emp_nm", "野茂英雄");
		map2.put("join_date", new Date());
		list1.add(map2);

		List<Map<String, Object>> list2 = new ArrayList<>();
		Map<String, Object> map3 = new HashMap<>();
		map3.put("emp_id", 100);
		map3.put("emp_nm", "小林一茶");
		map3.put("join_date", new Date());
		list2.add(map3);
		Map<String, Object> map4 = new HashMap<>();
		map4.put("emp_id", 101);
		map4.put("emp_nm", "松尾芭蕉");
		map4.put("join_date", new Date());
		list2.add(map4);

		try{
			int insertCount = ajm.insertBatch("emp_mst").params(list1).execute();
			int updateCount = ajm.updateBatch("emp_mst").params(list2).where("emp_id").execute();//②
//			int deleteBatch = ajm.deleteBatch("emp_mst").params(list1).where("emp_id").execute();
		}catch(SQLException e){
			ajm.rollback();//①②のSQLの実行がキャンセルされる
			throw(e);
		}
		ajm.commit();//ここで①②のSQL実行がコミットされる

		System.out.println("hello.");
	}
}

// カラムやvalueはStringで渡すようになっている。
// valueをリストやセットで渡すことは現状できない。
// SimpleWhere().excludesWhitespace()も実装していない。
// ajmを使いまわせなくなっています。