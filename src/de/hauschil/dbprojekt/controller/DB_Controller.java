package de.hauschil.dbprojekt.controller;

import com.db4o.config.EmbeddedConfiguration;
import com.db4o.query.Query;

import de.hauschil.dbprojekt.model.Anruf;
import de.hauschil.dbprojekt.model.Kunde;

public interface DB_Controller {
	public void initDBConnection(EmbeddedConfiguration conf);
	public void createTables();
	public void dropTables();
	public void storeObject(Object o);
	public void commit();
	public Query query();
	public void delete(Object o);
	public void closeDBConncetion();
	public void storeKunden(Kunde[] k);
	public void storeAnrufe(Anruf[] a);
	public void closePStatement();
}
