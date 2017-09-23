package Mapper;

public class SqlLog {

    private String rawSql;

    private String completeSql;

    private Object[] bindArgs;

    private Class[] bindArgTypes;

    public SqlLog(String rawSql, String completeSql, Object[] bindArgs, Class[] bindArgTypes) {
        this.rawSql = rawSql;
        this.completeSql = completeSql;
        this.bindArgs = bindArgs;
        this.bindArgTypes = bindArgTypes;
    }

    public Object[] getBindArgs() {
        return bindArgs;
    }

    public Class[] getBindArgTypes() {
        return bindArgTypes;
    }

    public String getCompleteSql() {
        return completeSql;
    }

    public String getRawSql() {
        return rawSql;
    }

    public String toString() {
        return rawSql;
    }
}
