package de.hauschil.dbprojekt.controller;

public class Index {
	private Class<?> indexClass;
	private String indexField;
	private boolean indexed;
	
	public Index(Class<?> indexClass, String indexField, boolean indexed) {
		super();
		this.indexClass = indexClass;
		this.indexField = indexField;
		this.indexed = indexed;
	}
	
	public Class<?> getIndexClass() {
		return indexClass;
	}
	public void setIndexClass(Class<?> indexClass) {
		this.indexClass = indexClass;
	}
	public String getIndexField() {
		return indexField;
	}
	public void setIndexField(String indexField) {
		this.indexField = indexField;
	}
	public boolean isIndexed() {
		return indexed;
	}
	public void setIndexed(boolean indexed) {
		this.indexed = indexed;
	}
}
