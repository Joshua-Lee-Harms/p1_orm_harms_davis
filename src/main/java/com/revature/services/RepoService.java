package com.revature.services;

public interface RepoService<T> {
	
	public void initializeTable(T obj);
	public Object addItem(T obj);
	public Object getItem(int id, T obj);
	public Object getAll(T o);
	public void deleteItem(int id, T obj);
	public void update(T obj, int id, String updateField, T updateValue);
}
