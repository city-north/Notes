package vip.ericchen.study.orm.ecbatis.v2.mapping;

/**
 * <p>
 * description
 * </p>
 *
 * @author EricChen 2020/02/25 21:30
 */
public final class MappedStatement {
    private String id;
    private String sql;
    private Class<?> resultType;

    public MappedStatement(String id, String sql, Class<?> resultType) {
        this.id = id;
        this.sql = sql;
        this.resultType = resultType;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public Class<?> getResultType() {
        return resultType;
    }

    public void setResultType(Class<?> resultType) {
        this.resultType = resultType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
