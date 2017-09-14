
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import Mapper.AquaJdbcManager;
import Mapper.Where;

public class Work1 {

	public static void main(String[] arg) throws SQLException {
		AquaJdbcManager ajm = new AquaJdbcManager("jdbc:mysql://localhost:3306/data?characterEncoding=UTF-8", "root",
				"3927");

		Map<String, Object> map = new HashMap<>();
		map.put("emp_id", 65);
		map.put("emp_nm", "ドラえもん");
		map.put("join_date", new Date());

		//挿入処理
		//DBの一意制約違反により挿入ができなかった場合は、 javax.persistence.EntityExistsException を発生させて下さい。
		int insertCount = ajm.insert("emp_mst").params(map).execute();

		//更新処理
		int updateCount = ajm.update("emp_mst").params(map).where(new Where().eq("emp_id","75")).execute();
		//または
//		int updateCount = ajm.update("table_name").params(map).where("col1").execute();//"col1"の値はmapから取得される（ここのwhereの引数はカラム名の可変長文字列）

		//削除処理
		int deleteCount = ajm.delete("emp_mst").where(new Where().eq("emp_id","39")).execute();

		System.out.println("hello.");
	}
}

// カラムやvalueはStringで渡すようになっている。
// valueをリストやセットで渡すことは現状できない。
// SimpleWhere().excludesWhitespace()も実装していない。
// ajmを使いまわせなくなっています。