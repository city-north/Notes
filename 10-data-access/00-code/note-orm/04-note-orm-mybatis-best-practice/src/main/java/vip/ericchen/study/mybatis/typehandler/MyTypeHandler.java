package vip.ericchen.study.mybatis.typehandler;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;
import vip.ericchen.study.mybatis.entity.Blog;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * <p>
 * A custom {@link TypeHandler}
 * </p>
 *
 * @author EricChen 2020/02/21 13:13
 */
public class MyTypeHandler implements TypeHandler<String> {
    @Override
    public void setParameter(PreparedStatement ps, int i, String parameter, JdbcType jdbcType) throws SQLException {
        // 设置 String 类型的参数的时候调用，Java类型到JDBC类型
        // 注意只有在字段上添加typeHandler属性才会生效
        // insertBlog name字段
        System.out.println("---------------setNonNullParameter1："+parameter);
        ps.setString(i, parameter);
    }

    @Override
    public String getResult(ResultSet rs, String columnName) throws SQLException {
        // 根据列名获取 String 类型的参数的时候调用，JDBC类型到java类型
        // 注意只有在字段上添加typeHandler属性才会生效
        System.out.println("---------------getNullableResult1："+columnName);
        return rs.getString(columnName);
    }

    @Override
    public String getResult(ResultSet rs, int columnIndex) throws SQLException {
        // 根据下标获取 String 类型的参数的时候调用
        System.out.println("---------------getNullableResult2："+columnIndex);
        return rs.getString(columnIndex);
    }

    @Override
    public String getResult(CallableStatement cs, int columnIndex) throws SQLException {
        System.out.println("---------------getNullableResult3：");
        return cs.getString(columnIndex);
    }
}
