package com.bsi.utils;

import com.bsi.framework.core.utils.ExceptionUtils;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 通过JDBC操作Oracle数据库
 */
@Slf4j
public class OracleJdbcClient {

    private final Logger info_log = LoggerFactory.getLogger("TASK_INFO_LOG");
    private final String url;
    private final String user;
    private final String password;

    static {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Failed to load Oracle JDBC driver", e);
        }
    }

    public OracleJdbcClient(String url,String user,String password){
        this.password = password;
        this.url = url;
        this.user = user;
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(this.url, this.user, this.password);
    }

    /**
     * 执行SQL查询，并返回查询结果
     * 该方法用于执行数据库的查询操作，将查询结果封装为List<List<Object>>的形式
     *
     * @param sql 查询的SQL语句
     * @param params 查询语句中可能用到的参数数组
     * @return 查询结果，为一个二维列表，外层列表的每个元素对应查询结果中的一行，内层列表的每个元素对应一行中的一列
     */
    public List<List<Object>> query(String sql, Object[] params) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            // 获取数据库连接
            conn = getConnection();
            if(conn == null){
                return null;
            }
            // 创建预编译的SQL语句对象
            pstmt = conn.prepareStatement(sql);
            // 设置SQL语句的参数
            setParameters(pstmt, params);

            // 执行查询语句并获取结果集
            rs = pstmt.executeQuery();
            // 初始化列表以存储查询结果
            List<List<Object>> results = new ArrayList<>();
            // 遍历结果集中的每一行
            while (rs.next()) {
                // 创建一个列表以存储当前行的数据
                List<Object> row = new ArrayList<>();
                // 获取当前行中的每一列数据，并添加到行列表中
                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                    row.add(rs.getObject(i));
                }
                // 将当前行的数据列表添加到结果列表中
                results.add(row);
            }
            // 返回查询结果
            return results;
        } catch (Exception e) {
            // 记录异常信息
            info_log.error("执行数据库查询报错，错误信息:{}", ExceptionUtils.getFullStackTrace(e));
        } finally {
            // 安静关闭结果集、PreparedStatement和数据库连接
            closeQuietly(rs);
            closeQuietly(pstmt);
            closeQuietly(conn);
        }
        // 如果发生异常，则返回null
        return null;
    }

    /**
     * 执行SQL批处理操作
     * 此方法用于执行一系列SQL操作，如批量插入、更新等
     * 它通过预编译的语句执行批处理，提高效率和安全性
     *
     * @param sql 批处理SQL语句，可以是INSERT、UPDATE等DML语句
     * @param batchParams 二维对象数组，每一项对应于一个SQL语句的一组参数
     * @return 执行结果数组，每个元素对应于一条SQL语句的执行结果如果所有语句都成功执行，则返回结果数组；否则，返回null
     */
    public int[] executeBatch(String sql, List<Object[]> batchParams){
        Connection conn = null; // 数据库连接
        PreparedStatement pstmt = null; // 预编译的SQL语句对象
        try {
            // 获取数据库连接
            conn = getConnection();
            if(conn == null){
                return null;
            }
            // 根据SQL语句创建预编译的SQL语句对象
            pstmt = conn.prepareStatement(sql);

            // 遍历参数数组，为每一条语句设置参数并添加到批处理中
            for (Object[] param : batchParams) {
                // 设置语句的参数
                setParameters(pstmt, param);
                // 将当前语句添加到批处理中
                pstmt.addBatch();
            }

            // 执行批处理并返回执行结果数组
            return pstmt.executeBatch();
        }catch (Exception e){
            // 记录批处理执行错误日志，包括完整的堆栈信息
            info_log.error("批量执行sql报错，错误信息:{}", ExceptionUtils.getFullStackTrace(e));
        }finally {
            // 确保关闭所有打开的资源
            closeQuietly(pstmt);
            closeQuietly(conn);
        }
        return null; // 如果发生异常或错误，则返回null
    }

    private void setParameters(PreparedStatement pstmt, Object[] params) throws SQLException {
        if (params != null) {
            for (int i = 0; i < params.length; i++) {
                pstmt.setObject(i + 1, params[i]);
            }
        }
    }

    private void closeQuietly(AutoCloseable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Exception e) {
                // Ignore exceptions on close
            }
        }
    }
}