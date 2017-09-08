package Mapper;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Select {

	public static List<Map<String, Object>> executeMultiple(PreparedStatement ps) throws SQLException {

		ResultSet rs = ps.executeQuery();
		ResultSetMetaData rsmd = rs.getMetaData();

		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();

		while (rs.next()) {
			Map<String, Object> map = new HashMap<String, Object>();
			for (int i = 1; i <= rsmd.getColumnCount(); i++) {
				map.put(rsmd.getColumnName(i), rs.getObject(i));
			}
			resultList.add(map);
		}
		rs.close();

		return resultList;
	}

	public static Map<String, Object> executeSingle(PreparedStatement ps) throws SQLException {

		ResultSet rs = ps.executeQuery();
		ResultSetMetaData rsmd = rs.getMetaData();

		Map<String, Object> resultMap = new HashMap<String, Object>();

		while (rs.next()) {
			for (int i = 1; i <= rsmd.getColumnCount(); i++) {
				resultMap.put(rsmd.getColumnName(i), rs.getObject(i));
			}
		}
		rs.close();

		return resultMap;
	}

	public static long executeCount(PreparedStatement ps) throws SQLException {

		ResultSet rs = ps.executeQuery();

		Long count = 0l;

		while (rs.next()) {
			count = rs.getLong(1);
		}
		rs.close();

		return count;
	}
}
