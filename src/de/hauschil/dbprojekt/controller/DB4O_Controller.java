package de.hauschil.dbprojekt.controller;

import static de.hauschil.dbprojekt.anwendungsfaelle.Fallmanager.DB_PATH;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.config.EmbeddedConfiguration;
import com.db4o.query.Query;

public class DB4O_Controller implements DB_Controller {
	private static ObjectContainer db;
	
	@Override
	public void initDBConnection(EmbeddedConfiguration conf) {
		if (conf == null) {
			db = Db4oEmbedded.openFile(Db4oEmbedded.newConfiguration(), DB_PATH);
		} else {
			db = Db4oEmbedded.openFile(conf, DB_PATH);
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
}
