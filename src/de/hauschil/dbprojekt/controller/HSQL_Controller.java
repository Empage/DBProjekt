package de.hauschil.dbprojekt.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;

import static de.hauschil.dbprojekt.anwendungsfaelle.Fallmanager.*;

import com.db4o.config.EmbeddedConfiguration;
import com.db4o.query.Constraint;
import com.db4o.query.Query;

import de.hauschil.dbprojekt.model.Anruf;
import de.hauschil.dbprojekt.model.Kunde;
import de.hauschil.dbprojekt.model.Telefon;

public class HSQL_Controller implements DB_Controller {
	Connection c = null;
	PreparedStatement psAnrufe = null;
	
	@Override
	public void initDBConnection(Index... indizes) {
		//TODO indizes
		try {
			c = DriverManager.getConnection(
				"jdbc:hsqldb:file:" + HSQL_PATH + "; shutdown=true",
				"root", "cocacola"
			);
		} catch (SQLException e) {
			e.printStackTrace();
		}
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
				"datum BIGINT NOT NULL," +
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
				psAnrufe.setLong(2, a.getDatum());
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

	@Override
	public ArrayList<Kunde> getKunden(String vorname, String nachname) {
		ArrayList<Kunde> k = new ArrayList<Kunde>();
		try (Statement st = c.createStatement()) {
			ResultSet rs;
			if (vorname != null && nachname != null) {
				rs = st.executeQuery(
					"SELECT id, vorname, nachname FROM Kunde " +
					"WHERE vorname = '" + vorname + "' AND nachname = '" + nachname + "' " +
					"ORDER BY nachname ASC, vorname ASC"
				);
			} else if (vorname != null) {
				rs = st.executeQuery(
					"SELECT id, vorname, nachname FROM Kunde " +
					"WHERE vorname = '" + vorname + "'" +
					"ORDER BY nachname ASC, vorname ASC"
				);
			} else if (nachname != null) {
				rs = st.executeQuery(
					"SELECT id, vorname, nachname FROM Kunde " +
					"WHERE nachname = '" + nachname + "' " +
					"ORDER BY nachname ASC, vorname ASC"
				);
			/* beides null */
			} else {
				rs = st.executeQuery(
					"SELECT id, vorname, nachname FROM Kunde " +
					"ORDER BY nachname ASC, vorname ASC"
				);
			}
			
			while (rs.next()) {
				k.add(new Kunde(rs.getString(2), rs.getString(3), getTelefoneWithKID(rs.getInt(1))));
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return k;
	}
	
	private ArrayList<Telefon> getTelefoneWithKID(int k_id) {
		ArrayList<Telefon> t = new ArrayList<>();
		
		try (Statement st = c.createStatement()) {
			ResultSet rs = st.executeQuery(
				"SELECT nummer FROM Telefon WHERE id_kunde = '" + k_id + "'"
			);
			while (rs.next()) {
				t.add(new Telefon(rs.getString(1)));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return t;
	}
	
	/* d1 untere Grenze, d2 obere Grenze */
	@Override
	public ArrayList<Anruf> getAnrufe(Telefon anrufer, Telefon angerufener, Long d1, Long d2) {
		ArrayList<Anruf> list = new ArrayList<>();
		
		/* Fall2 */
		try (Statement stmt = c.createStatement()) {
			ResultSet rs = null;
			if (anrufer != null && angerufener == null && d1 != null && d2 != null) {
				rs = stmt.executeQuery(
					"SELECT t1.nummer as Anrufer, t2.nummer as Angerufener, dauer, datum " +
					"FROM Anruf a, Telefon t1, Telefon t2 " +
					"WHERE a.id_anrufer = t1.id AND a.id_angerufener = t2.id " +
					"AND t1.nummer = '" + anrufer.toString() + "' " +
					"AND a.datum > " + d1.longValue() +" " +
					"AND a.datum < " + d2.longValue()
				);
			} else {
				throw new RuntimeException("TODO");
			}
			while (rs.next()) {
				list.add(new Anruf(anrufer, new Telefon(rs.getString(2)), rs.getLong(4), rs.getInt(3)));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			
		}
		
		return list;
	}

	@Override
	public void deleteAnrufe(long datum1, long datum2) {
		try (Statement stmt = c.createStatement()) {
			stmt.execute(
				"DELETE FROM Anruf WHERE datum > " + datum1 + " AND datum < " + datum2
			);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
