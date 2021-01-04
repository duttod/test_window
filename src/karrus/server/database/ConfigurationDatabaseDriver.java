package karrus.server.database;

import java.util.ArrayList;
import java.util.List;

import karrus.shared.ApplicationException;
import karrus.shared.hibernate.CtLane;
import karrus.shared.hibernate.TtItinerary;
import karrus.shared.language.Language;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;

public class ConfigurationDatabaseDriver {

	private static Logger logger = Logger.getLogger(ConfigurationDatabaseDriver.class);

	
/*
 *#######################################################################################################
 *                                            LANES                                                    ##
 *#######################################################################################################	
 */

	public static void removeLane(CtLane lane) throws Exception {
		try {
			HibernateDatabaseDriver.delete(lane);
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw e;
		}
	}

	public static CtLane updateLane(CtLane oldLane, CtLane lane) throws Exception {
		// If primary key has not changed, update the lane in the database
		if (oldLane.getId().equals(lane.getId())) {
			HibernateDatabaseDriver.update(lane);
		}
		// Else save new lane and delete previous one
		else {
			try {
				HibernateDatabaseDriver.save(lane);
				HibernateDatabaseDriver.delete(oldLane);
			}
			catch (ConstraintViolationException e) {
				throw new ApplicationException(Language.laneConstraintViolationError(lane.getId().getStation(), lane.getId().getLane()));
			}
		}
		return getLane(lane.getId().getStation(), lane.getId().getLane());
	}

	public static CtLane addNewLane(CtLane lane) throws Exception {
		try {
			HibernateDatabaseDriver.save(lane);
			return getLane(lane.getId().getStation(), lane.getId().getLane());
		}
		catch (ConstraintViolationException e) {
			throw new ApplicationException(Language.laneConstraintViolationError(lane.getId().getStation(), lane.getId().getLane()));
		}
	}
	
	@SuppressWarnings("unchecked")
	private static CtLane getLane(String station, String lane) throws Exception {
		List<CtLane> ctLanes = new ArrayList<CtLane>();
		try {
			String query = "SELECT * FROM \"ct_lane\" WHERE \"station\" = '" + station + "' AND \"lane\" = '" + lane + "';";
			logger.info(query);
			ctLanes = (List<CtLane>)HibernateDatabaseDriver.get(query, CtLane.class);
			if (ctLanes.size() != 1) {
				throw new Exception("Error in getLane: there is no exactly one element...");
			}
		} catch (Exception e){
			logger.error(e.getMessage());
			throw e;
		}
		return ctLanes.get(0);
	}
	
/*
 *#######################################################################################################
 *                                          ITINERARIES                                                ##
 *#######################################################################################################	
 */
	
	public static void removeItinerary(String name, String origin, String destination) throws Exception {
		Session session = karrus.server.database.HibernateUtil.getSessionFactory().openSession();
		Transaction transaction = null;
		try{
			// delete itinerary
			transaction = session.beginTransaction();
			String query="DELETE FROM \"tt_itinerary\" WHERE name='"+name+"' AND origin='"+origin+"' AND destination='"+destination+"'; ";
			// logger.debug(query);
			SQLQuery sqlQuery = session.createSQLQuery(query);
			sqlQuery.executeUpdate();
			transaction.commit();
		} catch (HibernateException e) {
			transaction.rollback();
			logger.error(e.getMessage()+"\n"+e.getCause());
			throw new Exception(e.getMessage()+"\n"+e.getCause());
		}
		finally {
			session.close();
		}	
	}

	public static TtItinerary updateItinerary(String formerName, String formerOrigin, String formerDestination, String newName, String newOrigin, String newDestination, Integer nominalTravelTime, Integer maxTravelTime, Integer levelOfServiceThreshold1, Integer levelOfServiceThreshold2, Integer scale) throws Exception {
		try {
			Session session = karrus.server.database.HibernateUtil.getSessionFactory().openSession();
			Transaction transaction = null;
			try {
				transaction = session.beginTransaction();

				String query="UPDATE \"tt_itinerary\" SET \"name\"='"+newName+"', \"origin\"='"+newOrigin+"', destination='"+newDestination+"', ";

				if (nominalTravelTime==null)
					query += "\"nominal_travel_time\"=null, ";
				else
					query += "\"nominal_travel_time\"="+nominalTravelTime+", ";

				if (maxTravelTime==null)
					query += "\"max_travel_time\"=null, ";
				else
					query += "\"max_travel_time\"="+maxTravelTime+", ";

				if (levelOfServiceThreshold1==null)
					query += "\"level_of_service_threshold_1\"=null, ";
				else
					query += "\"level_of_service_threshold_1\"="+levelOfServiceThreshold1+", ";

				if (levelOfServiceThreshold2==null)
					query += "\"level_of_service_threshold_2\"=null ";
				else
					query += "\"level_of_service_threshold_2\"="+levelOfServiceThreshold2+", ";
				if (scale==null)
					query += "\"scale\"=null ";
				else
					query += "\"scale\"="+scale+" ";
				query +="WHERE \"name\"='"+formerName+"' AND \"origin\"='"+formerOrigin+"' AND \"destination\"='"+formerDestination+"'";

				logger.debug(query);
				SQLQuery sqlQuery = session.createSQLQuery(query);
				sqlQuery.executeUpdate();
				transaction.commit();
			} catch (ConstraintViolationException e) {
				transaction.rollback();
				throw new ApplicationException(Language.itineraryConstraintViolationError(newOrigin, newDestination));
			} catch (HibernateException e) {
				transaction.rollback();
				logger.error(e.getMessage()+"\n"+e.getCause());
				throw new Exception(e.getMessage()+"\n"+e.getCause());
			}
			finally {
				session.close();
			}
			return getItinerary(newName, newOrigin, newDestination);
		} catch (Exception e){
			logger.error(e.getMessage());
			throw e;
		}
	}

	public static TtItinerary addNewItinerary(String name, String origin, String destination, Integer nominalTravelTime, Integer maxTravelTime, Integer levelOfServiceThreshold1, Integer levelOfServiceThreshold2, Integer scale) throws Exception {
		try {
			Session session = karrus.server.database.HibernateUtil.getSessionFactory().openSession();
			Transaction transaction = null;
			try {
				transaction = session.beginTransaction();

				String query="INSERT INTO \"tt_itinerary\" (name, origin, destination, nominal_travel_time, max_travel_time, level_of_service_threshold_1, level_of_service_threshold_2, scale) VALUES ('"+name+"', '"+origin+"', '"+destination+"', ";

				if (nominalTravelTime==null)
					query += "null, ";
				else
					query += nominalTravelTime+", ";

				if (maxTravelTime==null)
					query += "null, ";
				else
					query += maxTravelTime+", ";

				if (levelOfServiceThreshold1==null)
					query += "null, ";
				else
					query += levelOfServiceThreshold1+", ";

				if (levelOfServiceThreshold2==null)
					query += "null, ";
				else
					query += levelOfServiceThreshold2+", ";
				
				if (scale==null)
					query += "null) ";
				else
					query += scale+") ";
				logger.debug(query);
				SQLQuery sqlQuery = session.createSQLQuery(query);
				sqlQuery.executeUpdate();
				transaction.commit();
			} catch (ConstraintViolationException e) {
				transaction.rollback();
				throw new ApplicationException(Language.itineraryConstraintViolationError(origin, destination));	
			} catch (HibernateException e) {
				transaction.rollback();
				logger.error(e.getMessage()+"\n"+e.getCause());
				throw new Exception(e.getMessage()+"\n"+e.getCause());
			}
			finally {
				session.close();
			}
			return getItinerary(name, origin, destination);
		} catch (Exception e){
			logger.error(e.getMessage());
			throw e;
		}
	}

	@SuppressWarnings("unchecked")
	private static TtItinerary getItinerary(String name, String origin, String destination) throws Exception {
		List<TtItinerary> out = new ArrayList<TtItinerary>();
		Session session = karrus.server.database.HibernateUtil.getSessionFactory().openSession();
		Transaction transaction = null;
		try{
			transaction = session.beginTransaction();
			String query = "SELECT * FROM \"tt_itinerary\" WHERE \"name\"='"+name+"' AND origin='"+origin+"' AND destination='"+destination+"';";
			logger.debug(query);
			out = session.createSQLQuery(query).addEntity(TtItinerary.class).list();
			if (out.size()!=1)
				throw new Exception("Error in getItinerary: there is no exactly one element...");
			transaction.commit();
		} catch (HibernateException e) {
			transaction.rollback();
			logger.error(e.getMessage()+"\n"+e.getCause());
			throw new Exception(e.getMessage()+"\n"+e.getCause());
		}
		finally {
			session.close();
		}
		return out.get(0);
	}
}
