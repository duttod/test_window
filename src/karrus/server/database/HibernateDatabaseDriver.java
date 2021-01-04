package karrus.server.database;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import karrus.shared.ApplicationException;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;

/**
 * Class used to do query on the database.
 */
public class HibernateDatabaseDriver {
	
	private static DateFormat timestampFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static Logger logger = Logger.getLogger(HibernateDatabaseDriver.class);

	/**
	 * Time stamp format used by the database engine.
	 * @return Time stamp format used by the database engine.
	 */
	public static DateFormat getTimestampFormat() {
		return timestampFormat;
	}
	
	public static void executeQuery(String query) throws Exception{
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			SQLQuery sqlQuery = session.createSQLQuery(query);
			sqlQuery.executeUpdate();
			transaction.commit();
		} catch (ConstraintViolationException e) {
			transaction.rollback();
			logger.error(e.getMessage()+"\n"+e.getCause());
			throw new ApplicationException(e.getMessage());
		} catch (HibernateException e) {
			transaction.rollback();
			logger.error(e.getMessage()+"\n"+e.getCause());
			throw new Exception(e.getMessage()+"\n"+e.getCause());
		}
		finally {
			session.close();
		}
	}
	
	/**
	 * Generic object saver.
	 * @param object Object to be saved in the database.
	 * @throws Exception Exception throws if something goes wrong while saving the object in the database.
	 */
	static public void save(Object object) throws Exception {
		Session session = karrus.server.database.HibernateUtil.getSessionFactory().openSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			session.save(object);
			transaction.commit();
		} catch (HibernateException hibernateException) {
			transaction.rollback();
			throw hibernateException;
		}
		finally {
			session.close();
		}
	}
	
	/**
	 * Generic object deleter.
	 * @param object Object to be deleted in the database.
	 * @throws Exception Exception throwned if something goes wrong while deleting the object in the database.
	 */
	static public void delete(Object object) throws Exception {
		Session session = karrus.server.database.HibernateUtil.getSessionFactory().openSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			session.delete(object);
			transaction.commit();
		} catch (HibernateException hibernateException) {
			transaction.rollback();
			throw hibernateException;
		}
		finally {
			session.close();
		}
	}

	/**
	 * Generic object getter.
	 * @param query Select sql query to be executed.
	 * @param ojectClass Class of the objects that should be returned.
	 * @return List of object constructed from the database rows.
	 * @throws Exception Exception throws if something goes wrong while saving the object in the database. 
	 */
	static public List<?> get(String query, Class<?> ojectClass) throws Exception {
		Session session = karrus.server.database.HibernateUtil.getSessionFactory().openSession();
		Transaction transaction = null;
		List<?> result = null;
		try {
			transaction = session.beginTransaction();
			result = session.createSQLQuery(query).addEntity(ojectClass).list();
			transaction.commit();
		} catch (HibernateException hibernateException) {
			transaction.rollback();
			throw hibernateException;
		}
		finally {
			session.close();
		}
		return result;
	}
	
	static public void update(Object object) throws Exception {
		Session session = karrus.server.database.HibernateUtil.getSessionFactory().openSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			session.update(object);
			transaction.commit();
		} catch (HibernateException e) {
			transaction.rollback();
			throw new Exception(e.getMessage()+"\n"+e.getCause());
		}
		finally {
			session.close();
		}
	}
	
	static public void clearAllTable(String tableName) throws Exception{
		String query = "DELETE FROM \""+tableName+"\"";
		logger.debug(query);
		Session session = karrus.server.database.HibernateUtil.getSessionFactory().openSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			SQLQuery sqlQuery = session.createSQLQuery(query);
			sqlQuery.executeUpdate();
			transaction.commit();
		} catch (HibernateException e) {
			transaction.rollback();
			throw new Exception(e.getMessage()+"\n"+e.getCause());
		}
		finally {
			session.close();
		}
	}	
}
