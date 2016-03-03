package com.cqu.edu.DB;
import java.sql.Connection;  
import java.sql.PreparedStatement;  
import java.sql.ResultSet;  
import java.sql.SQLException;  
import javax.sql.DataSource;  
import com.mchange.v2.c3p0.ComboPooledDataSource;  
public final class JdbcUtil {  
    private static ComboPooledDataSource dataSource;  
    static {  
        dataSource = new ComboPooledDataSource();  
    }  
    // 取得链接  
    public static Connection getMySqlConnection() throws SQLException {  
        return dataSource.getConnection();  
    }  
    //  
    public static DataSource getDataSource(){  
        return dataSource;  
    }  
  
    // 关闭链接  
    public static void close(Connection conn) throws SQLException {  
        if (conn != null) {  
            try {  
                conn.close();  
            } catch (SQLException e) {  
                e.printStackTrace();  
                throw e;  
            }  
        }  
    }  
  
    public static void close(PreparedStatement pstate) throws SQLException {  
        if(pstate!=null){  
            pstate.close();  
        }  
    }  
    public static void close(ResultSet rs) throws SQLException {  
        if(rs!=null){  
            rs.close();  
        }  
    }  
      
}  