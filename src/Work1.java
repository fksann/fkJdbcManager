import java.sql.SQLException;

import Mapper.AquaJdbcManager;

public class Work1 {

	public static void main(String[] arg) throws SQLException {
		AquaJdbcManager ajm = new AquaJdbcManager("jdbc:mysql://localhost:3306/data?characterEncoding=UTF-8", "root",
				"3927");

		//SQLでのアップデート処理（insert,update,delete）戻り値:更新した行数。
				int updateCount = ajm.updateBySql("insert table_name ('col1','col2') values ('val1', 'val2')").execute();
				//パラメータ渡しの場合
				int updateCount = ajm.updateBySql("insert table_name ('col1','col2') values (?, ?)",String.class,String.class).params("val1","val2").execute();

		System.out.println("hello.");
	}
}

// カラムやvalueはStringで渡すようになっている。
// valueをリストやセットで渡すことは現状できない。
// SimpleWhere().excludesWhitespace()も実装していない。
// ajmを使いまわせなくなっています。