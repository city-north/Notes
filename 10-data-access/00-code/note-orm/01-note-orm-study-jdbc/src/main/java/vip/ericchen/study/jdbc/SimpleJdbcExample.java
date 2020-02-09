package vip.ericchen.study.jdbc;

import java.sql.*;

/**
 * <p>
 * 使用 JDBC API 的普通方式
 * The Common Way to Use JDBC Api
 * </p>
 *
 * @author qiang.chen04@hand-china.com 2020/02/08 23:16
 */
public class SimpleJdbcExample {
    public static void main(String[] args) {

        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("MySQL JDBC Driver not found !!");
            return;
        }
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/JDBCDemo", "root", "root");
            String sql = "SELECT * FROM sys_user where user_id = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1,1);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int user_id = resultSet.getInt("user_id");
                String username = resultSet.getString("username");
                System.out.println("user_id = " + user_id + ",username = " + username);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                resultSet.close();
                preparedStatement.close();
                connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
