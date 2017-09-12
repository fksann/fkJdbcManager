import java.sql.SQLException;

import Mapper.AquaJdbcManager;

public class Work1 {

	public static void main(String[] arg) throws SQLException {
		AquaJdbcManager ajm = new AquaJdbcManager("jdbc:mysql://localhost:3306/data?characterEncoding=UTF-8", "root",
				"3927");

		//SQLでのアップデート処理（insert,update,delete）戻り値:更新した行数。
//				int updateCount1 = ajm.updateBySql("insert into emp_mst (emp_id,emp_nm,div_id) values ('76', '加納義彦','4')").execute();
				//パラメータ渡しの場合
				int updateCount2= ajm.updateBySql("insert into emp_mst (emp_id,emp_nm,div_id) values  (?, ?, ?)",Integer.class,String.class,Integer.class).params(78,"加納義彦",4).execute();

		System.out.println("hello.");
	}
}

// カラムやvalueはStringで渡すようになっている。
// valueをリストやセットで渡すことは現状できない。
// SimpleWhere().excludesWhitespace()も実装していない。
// ajmを使いまわせなくなっています。