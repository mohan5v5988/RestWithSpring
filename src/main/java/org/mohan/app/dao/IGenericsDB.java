package org.mohan.app.dao;

import java.util.List;

import org.moham.app.exception.GException;

public interface IGenericsDB<T> {
	public List<T> getAll(T obj) throws GException;
	public T getByPK(T obj) throws GException;
	public Boolean add(T obj) throws GException;
	public Boolean update(T obj) throws GException;
	public Boolean delete(T obj) throws GException;
	public List<Object> findByExample(T obj) throws GException;
}
