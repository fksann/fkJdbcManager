import static Mapper.Operations.*;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import Mapper.AquaJdbcManager;

public class Work1 {

	public static void main(String[] arg) throws SQLException {
		AquaJdbcManager ajm = new AquaJdbcManager("jdbc:mysql://localhost:3306/data?characterEncoding=UTF-8", "root",
				"3927");

		List<Map<String, Object>> resultMap13 = ajm.from("emp_mst")
				.where(or(eq("div_id", "1"), eq("div_id", "2")), or(eq("div_id", "2"), eq("div_id", "3")))
				.getResultList();
//
//		List<Map<String, Object>> resultMap14 = ajm.from("emp_mst")
//				.where(new Where().and(new Where().or(new Where().eq("div_id", "1"), new Where().eq("div_id", "2")),
//						new Where().or(new Where().eq("div_id", "2"), new Where().eq("div_id", "3"))))
//				.getResultList();

		System.out.println("hello.");
	}
}

// カラムやvalueはStringで渡すようになっている。
// valueをリストやセットで渡すことは現状できない。
// SimpleWhere().excludesWhitespace()も実装していない。
// ajmを使いまわせなくなっています。