package de.hauschil.dbprojekt.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static de.hauschil.dbprojekt.anwendungsfaelle.Fallmanager.*;

import com.db4o.config.EmbeddedConfiguration;
import com.db4o.query.Query;

public class HSQL_Controller implements DB_Controller {
	Connection c = null;
	
	@Override
	public void initDBConnection(EmbeddedConfiguration conf) {
		try {
			c = DriverManager.getConnection(
				"jdbc:hsqldb:file:" + HSQL_PATH + "; shutdown=true",
				"root", "cocacola"
			);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		try (
			Statement stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM Customer")	
		) {
			while (rs.next()) {
				String id = rs.getString(1);
				String firstName = rs.getString(2);
				String lastName = rs.getString(3);
				System.out.println(id + ", " + firstName + " " + lastName);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void storeObject(Object o) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void commit() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Query query() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(Object o) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void closeDBConncetion() {
		if (c == null) {
			return;
		}
		
		try {
			c.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
