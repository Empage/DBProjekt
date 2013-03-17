package de.hauschil.dbprojekt.controller;

import com.db4o.config.EmbeddedConfiguration;
import com.db4o.query.Query;

public interface DB_Controller {
	public void initDBConnection(EmbeddedConfiguration conf);
	public void createTables();
	public void dropTables();
	public void storeObject(Object o);
	public void commit();
	public Query query();
	public void delete(Object o);
	public void closeDBConncetion();
}
