package de.hauschil.dbprojekt.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;

import static de.hauschil.dbprojekt.anwendungsfaelle.Fallmanager.*;

import com.db4o.config.EmbeddedConfiguration;
import com.db4o.query.Constraint;
import com.db4o.query.Query;

import de.hauschil.dbprojekt.model.Anruf;
import de.hauschil.dbprojekt.model.Kunde;
import de.hauschil.dbprojekt.model.Telefon;

public class HSQL_Controller implements DB_Controller {
	private Connection c = null;
	private PreparedStatement psAnrufe = null;
	private final String CON_STR ="jdbc:hsqldb:file:" + HSQL_PATH + "; shutdown=true";
	
	@Override
	public void initDBConnection(Index... indizes) {
		try {
			c = DriverManager.getConnection(
				CON_STR,
				"root", "cocacola"
			);
			try (Statement st = c.createStatement()) {
				/* Spezielle Variante für diese Testcases */
				if (indizes.length == 2) {
					/* Fall1 */
					if (indizes[0].getIndexClass().equals(Kunde.class)) {
						if (indizes[0].getIndexField().equals("vorname") && indizes[1].getIndexField().equals("nachname")) {
							createSingleIndex(st, indizes);
							if (indizes[0].isIndexed()) {
								st.execute("CREATE INDEX idx_name ON Kunde(vorname, nachname)");
							} else {
								st.execute("DROP INDEX idx_name IF EXISTS");
							}
						} else {
							throw new RuntimeException("TODO");
						}
					} else if (indizes[0].getIndexClass().equals(Anruf.class)) {
						/* Fall2 */
						if (indizes[0].getIndexField().equals("anrufer") && indizes[1].getIndexField().equals("datum")) {
							createSingleIndex(st, indizes);
							if (indizes[0].isIndexed()) {
								st.execute("CREATE INDEX idx_andat ON Anruf(id_anrufer, datum)");
							} else {
								st.execute("DROP INDEX idx_andat IF EXISTS");
							}
						/* Fall4 */
						} else if (indizes[0].getIndexField().equals("anrufer") && indizes[1].getIndexField().equals("angerufener")) {
							createSingleIndex(st, indizes);
							if (indizes[0].isIndexed()) {
								st.execute("CREATE INDEX idx_anan ON Anruf(id_anrufer, id_angerufener)");
							} else {
								st.execute("DROP INDEX idx_anan IF EXISTS");
							}
						} else {
							throw new RuntimeException("TODO");
						}
					} else {
						throw new RuntimeException("TODO");
					}
				} else {
					/* Fall3 */
					createSingleIndex(st, indizes);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/* Allgemeine Methode für Einzelspalten-Index */
	private void createSingleIndex(Statement st, Index... indizes) throws SQLException {
		for (Index i : indizes) {
			String sql_start = "";
			String sql_table = "";
			String sql_end = "";
			
			/* index == false? Dann lösche vorhandenen Index */
			if (!i.isIndexed()) {
				sql_start = "DROP INDEX idx_" + i.getIndexField() + " IF EXISTS";
				st.execute(sql_start);
			/* Ansonsten erstelle einen, abhängig von der Klasse und den Feldern */
			} else {
				sql_start = "CREATE INDEX idx_" + i.getIndexField() + " ON ";
				sql_end = "(" + i.getIndexField() + ")";

				if (i.getIndexClass().equals(Kunde.class)) {
					sql_table = "Kunde";
				} else if (i.getIndexClass().equals(Anruf.class)) {
					sql_table = "Anruf";
					if (i.getIndexField().equals("anrufer") || i.getIndexField().equals("angerufener")) {
						sql_end = "(id_" + i.getIndexField() + ")";
					}
				} else {
					throw new RuntimeException("TODO");
				}
				
				st.execute(sql_start + sql_table + sql_end);
			}
		}
	}
	
	@Override
	public void createTables() {
		try (Statement stmt = c.createStatement()) {
			/* setzte Größe für Tabellen im Ram auf 1 GiB */
			stmt.execute("SET FILES CACHE SIZE 1000000"); //kiB
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
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
			/* Foreign keys auskommentiert, weil sonst automatisch Indizes angelegt werden (eig ja gut, aber nicht für diese Aufgabe ;) ) */
			stmt.execute(
				"CREATE TABLE IF NOT EXISTS Telefon (" +
				"nummer varchar(15) PRIMARY KEY," + 
				"id_kunde INTEGER NOT NULL," +
//				"FOREIGN KEY (id_kunde) REFERENCES Kunde(id)" +
				")"
			);
			/* erzeuge Tabelle Anruf */
			stmt.execute(
				"CREATE CACHED TABLE IF NOT EXISTS Anruf (" +
				"id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY," +
				"dauer INTEGER NOT NULL," +
				"datum BIGINT NOT NULL," +
				"id_anrufer varchar(15) NOT NULL," +
				"id_angerufener varchar(15) NOT NULL," +
//				"FOREIGN KEY (id_anrufer) REFERENCES Telefon(id)," +
//				"FOREIGN KEY (id_angerufener) REFERENCES Telefon(id)" +
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
				c.setAutoCommit(false);
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
				psAnrufe.setString(3, a.getAnrufer().toString());
				psAnrufe.setString(4, a.getAngerufener().toString());
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
				c.commit();
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
				c.setAutoCommit(true);
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				psAnrufe = null;
			}
		}
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
			rs.close();
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
			rs.close();
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
					"SELECT a.id_anrufer, a.id_angerufener, dauer, datum " +
					"FROM Anruf a " +
					"WHERE a.id_anrufer = '" + anrufer.toString() + "' " +
					"AND a.datum > " + d1.longValue() +" " +
					"AND a.datum < " + d2.longValue()
				);
			/* Fall4 */
//			} else if (anrufer != null && angerufener != null && d1 == null && d2 == null) {
//				rs = stmt.executeQuery(
//					"SELECT t1.nummer as Anrufer, t2.nummer as Angerufener, dauer, datum " +
//					"FROM Anruf a, Telefon t1, Telefon t2 " +
//					"WHERE ((a.id_anrufer = t1.nummer AND a.id_angerufener = t2.nummer) " +
//					"OR (a.id_anrufer = t2.nummer AND a.id_angerufener = t1.nummer))" +
//					"AND t1.nummer = '" + anrufer.toString() + "'"
//				);
			} else if (anrufer != null && angerufener == null && d1 == null && d2 == null) {
				rs = stmt.executeQuery(
						"SELECT a.id_anrufer as Anrufer,  a.id_angerufener as Angerufener, dauer, datum " +
						"FROM Anruf a " +
						"WHERE a.id_anrufer = '" + anrufer.toString() + "'"
					);
			} else if (anrufer == null && angerufener != null && d1 == null && d2 == null) {
				rs = stmt.executeQuery(
						"SELECT a.id_anrufer as Anrufer, a.id_angerufener as Angerufener, dauer, datum " +
						"FROM Anruf a " +
						"WHERE a.id_angerufener = '" + angerufener.toString() + "'"
					);
			} else {
				throw new RuntimeException("TODO");
			}
			while (rs.next()) {
				if (anrufer != null) {
					list.add(new Anruf(anrufer, new Telefon(rs.getString(2)), rs.getLong(4), rs.getInt(3)));
				} else {
					list.add(new Anruf(new Telefon(rs.getString(1)), angerufener, rs.getLong(4), rs.getInt(3)));
				}
			}
			rs.close();
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

	@Override
	public Kunde getKundeByNumber(String number) {
		Kunde k = null;
		try (Statement st = c.createStatement()) {
			ResultSet rs = st.executeQuery(
				"SELECT vorname, nachname FROM Kunde k, Telefon t " +
				"WHERE k.id = t.id_kunde AND t.nummer = '" + number + "'"
			);
			rs.next();
			k = new Kunde(rs.getString(1), rs.getString(2), null);
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return k;
	}
	
	@Override
	public String toString() {
		return "HSQL-Controller!";
	}
}
