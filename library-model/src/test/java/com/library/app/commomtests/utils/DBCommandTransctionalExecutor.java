package com.library.app.commomtests.utils;

import javax.persistence.EntityManager;

import org.junit.Ignore;

@Ignore
public class DBCommandTransctionalExecutor {
	private EntityManager em;

	public DBCommandTransctionalExecutor(EntityManager em) {
		this.em = em;
	}
	
	public <T> T executeCommand(DBCommand<T> dbCommand){
		try{
			em.getTransaction().begin();
			T toReturn = dbCommand.execute();
			em.getTransaction().commit();
			em.clear();
			return toReturn;
		}catch(Exception e){
			e.printStackTrace();
			em.getTransaction().rollback();
			throw new IllegalStateException(e);
		}
	}
}
