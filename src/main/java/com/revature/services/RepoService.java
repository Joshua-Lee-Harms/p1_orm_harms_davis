package com.revature.services;

public interface RepoService<T> {
	
	public void initializeTable(Object obj);
	public Object addItem(Object obj);
	public Object getItem(int id, Object obj);
	public Object getAll(T o);
	public void deleteItem(int id, Object obj);
	public void update(int id, T obj);
}
