package vip.ericchen.study.orm.ecbatis.v2.executor;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * <p>
 * description
 * </p>
 *
 * @author EricChen 2020/02/25 22:39
 */
public class ParameterHandler {
    private PreparedStatement psmt;

    public ParameterHandler(PreparedStatement psmt) {
        this.psmt = psmt;
    }

    public void setParameters(Object[] parameters) {
        try {
            // PreparedStatement的序号是从1开始的
            for (int i = 0; i < parameters.length; i++) {
                int k = i + 1;
                if (parameters[i] instanceof Integer) {
                    psmt.setInt(k, (Integer) parameters[i]);
                } else if (parameters[i] instanceof Long) {
                    psmt.setLong(k, (Long) parameters[i]);
                } else if (parameters[i] instanceof String) {
                    psmt.setString(k, String.valueOf(parameters[i]));
                } else if (parameters[i] instanceof Boolean) {
                    psmt.setBoolean(k, (Boolean) parameters[i]);
                } else {
                    psmt.setString(k, String.valueOf(parameters[i]));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
