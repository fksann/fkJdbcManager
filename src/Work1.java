
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Mapper.AquaJdbcManager;

public class Work1 {

	public static void main(String[] arg) throws SQLException {
		AquaJdbcManager ajm = new AquaJdbcManager("jdbc:mysql://localhost:3306/data?characterEncoding=UTF-8", "root",
				"3927");

		// なお、Listに格納されるMapは、すべて同じColを持つ前提で構いません。

		 List<Map<String, Object>> list = new ArrayList<>();
		 Map<String, Object> map1 = new HashMap<>();
		 map1.put("emp_id", 100);
		 map1.put("emp_nm", "松井稼頭央");
		 map1.put("join_date", new Date());
		 list.add(map1);
		 Map<String, Object> map2 = new HashMap<>();
		 map2.put("emp_id", 101);
		 map2.put("emp_nm", "野茂英雄");
		 map2.put("join_date", new Date());
		 list.add(map2);


		 //バッチInsert
		 int insertCount = ajm.insertBatch("emp_mst").params(list).execute();

//		 //バッチ更新処理
//		 int updateCount =
//		 ajm.updateBatch("table_name").params(list).where("col1").execute();//"col1"の値はListのmapから取得される（ここのwhereの引数はカラム名の可変長文字列）
//
//		 //バッチ削除処理
//		 int deleteCount =
//		 ajm.deleteBatch("table_name").params(list).where("col1").execute();//"col1"の値はListのmapから取得される（ここのwhereの引数はカラム名の可変長文字列）

		System.out.println("hello.");
	}
}

// カラムやvalueはStringで渡すようになっている。
// valueをリストやセットで渡すことは現状できない。
// SimpleWhere().excludesWhitespace()も実装していない。
// ajmを使いまわせなくなっています。