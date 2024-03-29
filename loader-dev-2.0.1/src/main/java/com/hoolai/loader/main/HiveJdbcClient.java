package com.hoolai.loader.main;

import java.sql.SQLException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.DriverManager;
 
/**
 * @author WL
 *连接旧的hive "jdbc:hive2://103.235.226.132:10001/default", "", "");
 * 仅仅是测试，没有用处
 */
public class HiveJdbcClient {
  private static String driverName = "org.apache.hive.jdbc.HiveDriver";
 
  public static void main(String[] args) throws SQLException {
    try {
      Class.forName(driverName);
    } catch (ClassNotFoundException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      System.exit(1);
    }
    Connection con = DriverManager.getConnection("jdbc:hive2://103.235.226.132:10001/default", "", "");
    Statement stmt = con.createStatement();
    String tableName = "testHiveDriverTable";
    stmt.execute("drop table " + tableName);
    stmt.execute("create table " + tableName + " (key int, value string)");
    // show tables
    String sql = "show tables '" + tableName + "'";
    System.out.println("Running: " + sql);
    ResultSet res = stmt.executeQuery(sql);
    if (res.next()) {
      System.out.println(res.getString(1));
    }
    // describe table
    sql = "describe " + tableName;
    System.out.println("Running: " + sql);
    res = stmt.executeQuery(sql);
    while (res.next()) {
      System.out.println(res.getString(1) + "\t" + res.getString(2));
    }
 
//    // load data into table
//    // NOTE: filepath has to be local to the hive server
//    // NOTE: /tmp/a.txt is a ctrl-A separated file with two fields per line
//    String filepath = "/tmp/a.txt";
//    sql = "load data local inpath '" + filepath + "' into table " + tableName;
//    System.out.println("Running: " + sql);
//    res = stmt.executeQuery(sql);
// 
//    // select * query
//    sql = "select * from " + tableName;
//    System.out.println("Running: " + sql);
//    res = stmt.executeQuery(sql);
//    while (res.next()) {
//      System.out.println(String.valueOf(res.getInt(1)) + "\t" + res.getString(2));
//    }
// 
//    // regular hive query
//    sql = "select count(1) from " + tableName;
//    System.out.println("Running: " + sql);
//    res = stmt.executeQuery(sql);
//    while (res.next()) {
//      System.out.println(res.getString(1));
//    }
  }
}