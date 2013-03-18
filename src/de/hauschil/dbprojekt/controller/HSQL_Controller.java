package de.hauschil.dbprojekt.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

import static de.hauschil.dbprojekt.anwendungsfaelle.Fallmanager.*;

import com.db4o.config.EmbeddedConfiguration;
import com.db4o.query.Query;

import de.hauschil.dbprojekt.model.Anruf;
import de.hauschil.dbprojekt.model.Kunde;
import de.hauschil.dbprojekt.model.Telefon;

public class HSQL_Controller implements DB_Controller {
	Connection c = null;
	PreparedStatement psAnrufe = null;
	
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
		
//		try (
//			Statement stmt = c.createStatement();
//			ResultSet rs = stmt.executeQuery("SELECT * FROM Customer")	
//		) {
//			while (rs.next()) {
//				String id = rs.getString(1);
//				String firstName = rs.getString(2);
//				String lastName = rs.getString(3);
//				System.out.println(id + ", " + firstName + " " + lastName);
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
	}
	
	@Override
	public void createTables() {
		try (Statement stmt = c.createStatement()) {
			/* Erzeuge Tabelle Kunde */
			stmt.execute(
				"CREATE TABLE IF NOT EXISTS Kunde (" +
				"id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY," +
				"vorname varchar(100) NOT NULL," +
				"nachname varchar(100) NOT NULL," +
				")"
			);
			/* erzeuge Tabelle Telefon */
			stmt.execute(
				"CREATE TABLE IF NOT EXISTS Telefon (" +
				"id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY," +
				"nummer varchar(15) NOT NULL," + 
				"id_kunde INTEGER NOT NULL," +
				"FOREIGN KEY (id_kunde) REFERENCES Kunde(id)" +
				")"
			);
			/* erzeuge Tabelle Anruf */
			stmt.execute(
				"CREATE TABLE IF NOT EXISTS Anruf (" +
				"id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY," +
				"dauer INTEGER NOT NULL," +
				"datum timestamp NOT NULL," +
				"id_anrufer INTEGER NOT NULL," +
				"id_angerufener INTEGER NOT NULL," +
				"FOREIGN KEY (id_anrufer) REFERENCES Telefon(id)," +
				"FOREIGN KEY (id_angerufener) REFERENCES Telefon(id)" +
				")"
			);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	@Override
	public void dropTables() {
		try (Statement stmt = c.createStatement()) {
			stmt.execute("DROP TABLE IF EXISTS Anruf");
			stmt.execute("DROP TABLE IF EXISTS Telefon");
			stmt.execute("DROP TABLE IF EXISTS Kunde");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void storeKunden(Kunde[] kunden) {
		try (PreparedStatement ps = c.prepareStatement(
			"INSERT INTO Kunde (vorname, nachname) VALUES (?, ?)"
		)) {
			for (Kunde k : kunden) {
				ps.setString(1, k.getVorname());
				ps.setString(2, k.getNachname());
				ps.execute();
				
				try (PreparedStatement ps2 = c.prepareStatement(
					"INSERT INTO Telefon (nummer, id_kunde)" +
					"VALUES (?, (SELECT TOP 1 id FROM Kunde ORDER BY id DESC))"
				)) {
					for (Telefon t : k.getTelefone()) {
						ps2.setString(1, t.toString());
						ps2.addBatch();
					}
					ps2.executeBatch();
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void storeAnrufe(Anruf[] anrufe) {
		if (psAnrufe == null) {
			try {
				psAnrufe = c.prepareStatement(
					"INSERT INTO Anruf (dauer, datum, id_anrufer, id_angerufener)" +
					"VALUES (?, ?, ?, ?)"
				);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		try {
			for (Anruf a : anrufe) {
				psAnrufe.setInt(1, a.getDauer());
				psAnrufe.setTimestamp(2, new Timestamp(a.getDatum()));
				Statement stmt = c.createStatement();
				ResultSet rs = stmt.executeQuery("SELECT id FROM Telefon WHERE nummer = '" + a.getAnrufer().toString() + "'");
				rs.next();
				psAnrufe.setInt(3, rs.getInt(1));
				
				rs = stmt.executeQuery("SELECT id FROM Telefon WHERE nummer = '" + a.getAngerufener().toString() + "'");
				rs.next();
				psAnrufe.setInt(4, rs.getInt(1));
				psAnrufe.addBatch();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void commit() {
		if (psAnrufe != null) {
			try {
				psAnrufe.executeBatch();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public void closePStatement() {
		if (psAnrufe != null) {
			try {
				psAnrufe.close();
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				psAnrufe = null;
			}
		}
	}
	
	@Override
	public void storeObject(Object o) {
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
