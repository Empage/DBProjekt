package de.hauschil.dbprojekt.controller;

import static de.hauschil.dbprojekt.anwendungsfaelle.Fallmanager.DB4O_PATH;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.config.EmbeddedConfiguration;
import com.db4o.query.Query;

public class DB4O_Controller implements DB_Controller {
	private static ObjectContainer db;
	
	@Override
	public void initDBConnection(EmbeddedConfiguration conf) {
		if (conf == null) {
			db = Db4oEmbedded.openFile(Db4oEmbedded.newConfiguration(), DB4O_PATH);
		} else {
			db = Db4oEmbedded.openFile(conf, DB4O_PATH);
		}
	}

	@Override
	public void storeObject(Object o) {
		db.store(o);
	}

	@Override
	public void commit() {
		db.commit();
	}

	@Override
	public void closeDBConncetion() {
		db.close();
	}

	@Override
	public Query query() {
		return db.query();
	}

	@Override
	public void delete(Object o) {
		db.delete(o);
		
	}

	@Override
	public void createTables() {
		/* wird nicht benötigt für db4o */
	}

	@Override
	public void dropTables() {
		/* wird nicht benötigt für db4o */
	}
}
