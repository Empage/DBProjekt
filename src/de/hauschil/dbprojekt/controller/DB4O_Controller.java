package de.hauschil.dbprojekt.controller;

import java.util.List;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.query.Predicate;

import static de.hauschil.dbprojekt.controller.Main.*;

public class DB4O_Controller implements DB_Controller {
	private static ObjectContainer db;
	
	@Override
	public void initDBConnection() {
		db = Db4oEmbedded.openFile(Db4oEmbedded.newConfiguration(), DB_PATH);
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
	public List query(Object o) {
		return db.query((Predicate) o);
	}
	

}
