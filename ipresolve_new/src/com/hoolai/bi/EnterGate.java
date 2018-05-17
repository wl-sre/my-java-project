package com.hoolai.bi;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.hadoop.hive.ql.exec.UDF;

public class EnterGate extends  UDF{

	
	
	static Connection crunchifyConn = null;
	static PreparedStatement crunchifyPrepareStat = null;
 
	public static void main(String[] argv) {
 
		try {
			log("-------- Simple Crunchify Tutorial on how to make JDBC connection to MySQL DB locally on macOS ------------");
			makeJDBCConnection();
 
//			log("\n---------- Adding company 'Crunchify LLC' to DB ----------");
//			addDataToDB("Crunchify, LLC.", "NYC, US", 5, "https://crunchify.com");
//			addDataToDB("Google Inc.", "Mountain View, CA, US", 50000, "https://google.com");
//			addDataToDB("Apple Inc.", "Cupertino, CA, US", 30000, "http://apple.com");
 
			log("\n---------- Let's get Data from DB ----------");
			getDataFromDB();
 
			crunchifyPrepareStat.close();
			crunchifyConn.close(); // connection close
 
		} catch (SQLException e) {
 
			e.printStackTrace();
		}
	}
 
 
	/**
	 * 连接mysql，并返回 connections 
	 */
	private static void makeJDBCConnection() {
 
		try {
			Class.forName("com.mysql.jdbc.Driver");
			log("Congrats - Seems your MySQL JDBC Driver Registered!----------开始注册");
		} catch (ClassNotFoundException e) {
			log("Sorry, couldn't found JDBC driver. Make sure you have added JDBC Maven Dependency Correctly----找不到JDBC 的连接包");
			e.printStackTrace();
			return;
		}
 
		try {
			// DriverManager: The basic service for managing a set of JDBC drivers.
			crunchifyConn = DriverManager.getConnection("jdbc:mysql://hlbi_db01:5599/ipresolve", "root", "hoolaibi");
			if (crunchifyConn != null) {
				log("Connection Successful! Enjoy. Now it's time to push data------已经陈功连接");
			} else {
				log("Failed to make connection!");
			}
		} catch (SQLException e) {
			log("MySQL Connection Failed!");
			e.printStackTrace();
			return;
		}
 
	}
 
	
	/**
	 * 添加 数据到 mysql
	 * @param companyName
	 * @param address
	 * @param totalEmployee
	 * @param webSite
	 */
	private static void addDataToDB(String companyName, String address, int totalEmployee, String webSite) {
 
		try {
			String insertQueryStatement = "INSERT  INTO  Employee  VALUES  (?,?,?,?)";
 
			crunchifyPrepareStat = crunchifyConn.prepareStatement(insertQueryStatement);
			crunchifyPrepareStat.setString(1, companyName);
			crunchifyPrepareStat.setString(2, address);
			crunchifyPrepareStat.setInt(3, totalEmployee);
			crunchifyPrepareStat.setString(4, webSite);
 
			// execute insert SQL statement
			crunchifyPrepareStat.executeUpdate();
			log(companyName + " added successfully");
		} catch (
 
		SQLException e) {
			e.printStackTrace();
		}
	}
 
	/**
	 * 从数据库中查询数据
	 */
	private static void getDataFromDB() {
 
		try {
			// MySQL Select Query Tutorial
			String getQueryStatement = "SELECT * FROM dbip_inter_2 limit 100;";
 
			crunchifyPrepareStat = crunchifyConn.prepareStatement(getQueryStatement);
 
			// Execute the Query, and get a java ResultSet
			ResultSet rs = crunchifyPrepareStat.executeQuery();
 
			// Let's iterate through the java ResultSet
			while (rs.next()) {
				Integer start_ip = rs.getInt("start_ip");
				Integer end_ip = rs.getInt("end_ip");
				String country = rs.getString("country");
				String province = rs.getString("province");
				String city = rs.getString("city");
 
				// Simply Print the results
				System.out.format("%d, %d, %s, %s, %s\n", start_ip, end_ip, country, province,city);
			}
 
		} catch (
 
		SQLException e) {
			e.printStackTrace();
		}
 
	}
 
	
	/**
	 * 自定义方法，实现 控制台输出
	 */
	private static void log(String string) {
//		System.out.println(string);
 
	}
	
	
}
