package org.java.sql.nations;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Main {

	public static void main(String[] args) {
		
		String url = "jdbc:mysql://localhost:3306/nations";
		String user = "root";
		String password = "root";
		
		try(Connection con = DriverManager.getConnection(url, user, password)) {
			System.out.println("Connected");
			
		}catch (SQLException ex){
			System.out.println("Connection error");
		}

	}

}
