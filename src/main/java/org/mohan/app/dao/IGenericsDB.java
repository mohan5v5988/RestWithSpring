package org.mohan.app.dao;

import java.util.List;

public interface IGenericsDB<T> {
	public List<T> getAll();
	public T getByPK(T obj);
	public int add(T obj);
	public int update(T obj);
	public int delete(T obj);
}
