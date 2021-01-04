package karrus.server.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import karrus.client.service.ExportService;
import karrus.server.core.ErrorStreamReader;
import karrus.server.database.AlarmDatabaseDriver;
import karrus.server.database.GenericDatabaseDriver;
import karrus.server.os.Encoding;
import karrus.server.os.Environment;
import karrus.shared.CountDataAndLanesMap;
import karrus.shared.hibernate.AlAlarmClosed;
import karrus.shared.hibernate.AlAlarmOpened;
import karrus.shared.hibernate.CtCountData;
import karrus.shared.hibernate.CtLane;
import karrus.shared.hibernate.TtItinerary;
import karrus.shared.hibernate.TtBtItt;
import karrus.shared.hibernate.RsuStation;
import karrus.shared.hibernate.WthMeteoData;
import karrus.shared.language.Language;
import net.sf.gilead.core.PersistentBeanManager;
import net.sf.gilead.core.hibernate.HibernateUtil;
import net.sf.gilead.core.serialization.GwtProxySerialization;
import net.sf.gilead.core.store.stateless.StatelessProxyStore;
import net.sf.gilead.gwt.PersistentRemoteService;

import org.apache.log4j.Logger;

public class ExportServiceImpl extends PersistentRemoteService implements ExportService {

	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(ExportServiceImpl.class);
    private SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat dateTimeFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	/**
	 * Constructor.
	 * @throws Exception 
	 */
	public ExportServiceImpl() throws Exception {
		HibernateUtil gileadHibernateUtil = new HibernateUtil();
		gileadHibernateUtil.setSessionFactory(karrus.server.database.HibernateUtil.getSessionFactory());
		PersistentBeanManager persistentBeanManager = new PersistentBeanManager();
		persistentBeanManager.setPersistenceUtil(gileadHibernateUtil);
		StatelessProxyStore proxyStore = new StatelessProxyStore(); 
		proxyStore.setProxySerializer(new GwtProxySerialization());
		persistentBeanManager.setProxyStore(proxyStore);
		setBeanManager(persistentBeanManager);
	}
	
	/**
	 * Action on the server-side when clicking on the export data button (count data export).
	 */
	public String getCountDataCsvFilePath(String stationName, String way, Date startDate, Date endDate) throws Exception {
		String fileName= "";
		fileName = Environment.getTempDirectory() + "rdt_" + stationName + "_" + way.replace(" > ", "_").replace(" ", "_") + "_" + dateFormatter.format(startDate) + "_" + dateFormatter.format(endDate) + ".csv";
		fileName = Normalizer.normalize(fileName, Normalizer.Form.NFD);
		fileName = fileName.replaceAll("[^\\p{ASCII}]", "");
		fileName = fileName.replace(">", "_").replace(" ", "_");
		try {
			FileOutputStream fileOutputStream = new FileOutputStream(fileName, false);
			BufferedWriter output = new BufferedWriter(new OutputStreamWriter(fileOutputStream, Encoding.fileFormat));
			CountDataAndLanesMap countDataAndLanesMap = GenericDatabaseDriver.getCountDataForExport(stationName, way, startDate, endDate);
			List<CtCountData> countDataList = new ArrayList<CtCountData>();
			countDataList = countDataAndLanesMap.getCountData();
			int numberOfLanes = countDataAndLanesMap.getLanesMap().size();
			if (numberOfLanes > 0) {
				for (int k = 0; k < numberOfLanes; k++) {
					output.write(Language.exportServiceTimestamp
							+ Language.csvSeparator
							+ Language.exportServiceStation
							+ Language.csvSeparator
							+ Language.exportServiceLane
							+ Language.csvSeparator
							+ Language.exportServiceSpeed
							+ Language.csvSeparator
							+ Language.exportServiceCount
							+ Language.csvSeparator
							+ Language.exportServiceCountLong
							+ Language.csvSeparator
							+ Language.exportServiceOccupancy
							);
				 }
				output.write("\n");
				int index = 0;
				while (index + numberOfLanes - 1 < countDataList.size()) {
					for (int k = 0; k < numberOfLanes; k++) {
						String horodate = getDateAsString(countDataList.get(index + k).getId().getTimestamp());
						String station = countDataList.get(index + k).getId().getStation();
						String lane = countDataList.get(index + k).getId().getLane();
						String occupancy = Double.toString(countDataList.get(index + k).getOccupancy()).replace(".", ",");
						int count = countDataList.get(index + k).getCount();
						int countLong = countDataList.get(index + k).getCountLong();
						long speed = Math.round(countDataList.get(index + k).getSpeed());
						output.write(horodate + Language.csvSeparator + station
								+ Language.csvSeparator + lane
								+ Language.csvSeparator + speed
								+ Language.csvSeparator + count
								+ Language.csvSeparator + countLong
								+ Language.csvSeparator + occupancy
								);
					}
					output.write("\n");
					index += numberOfLanes;
				}
			}
			output.flush();
			output.close();
		} catch (IOException e) {
			logger.error(e.getMessage()+"\n"+e.getCause());
			throw new Exception(e.getMessage()+"\n"+e.getCause());
		}
		return fileName;
	}
	
	/**
	 * Action on the server-side when clicking on the export individual data button.
	 */
	public String getIndividualTravelTimeCsvFilePath(String itinerary, Date startDate, Date endDate) throws Exception {
		String startDateString = getDateAsString(startDate).replace(":", "-").replace(" ", "-");
		startDateString = startDateString.substring(0, startDateString.length()-3);
		String endDateString = getDateAsString(endDate).replace(":", "-").replace(" ", "-");
		endDateString = endDateString.substring(0, endDateString.length()-3);
		String fileName = Environment.getTempDirectory()+"data_indiv_"+itinerary+"_"+startDateString+"_"+endDateString+".csv";
		fileName = Normalizer.normalize(fileName, Normalizer.Form.NFD);
		fileName = fileName.replaceAll("[^\\p{ASCII}]", "");
		fileName = fileName.replace(">", "_").replace(" ", "_");
		try {
			FileOutputStream fw = new FileOutputStream(fileName, false);
			BufferedWriter output = new BufferedWriter(new OutputStreamWriter(fw, Encoding.fileFormat));
			List<TtBtItt> btIttList = GenericDatabaseDriver.getBtItt(itinerary, startDate, endDate);
			output.write(Language.originColumn 
					+ Language.csvSeparator 
					+ Language.destinationColumn 
					+ Language.csvSeparator 
					+ Language.timestampColumn 
					+ Language.csvSeparator 
					+ Language.macColumn 
					+ Language.csvSeparator 
					+ Language.classColumn 
					+ Language.csvSeparator 
					+ Language.travelTimesSecColumn 
					+ Language.csvSeparator 
					+ Language.validColumn 
					+ "\n");
			for (TtBtItt btItt : btIttList){
				String origin = btItt.getOrigin();
				String destination = btItt.getDestination();
				String horodate = getDateAsString(btItt.getId().getTimestamp());
				String mac = btItt.getId().getMacSecret();
				String classBt = btItt.getClass_();				
				int travelTime = btItt.getId().getTravelTime();
				boolean isAttValid = btItt.isAttValid();
				String isValidString = Language.noString;
				if (isAttValid) {
					isValidString = Language.yesString;
				}
				output.write(origin 
						+ Language.csvSeparator
						+ destination
						+ Language.csvSeparator
						+ horodate
						+ Language.csvSeparator
						+ mac+Language.csvSeparator
						+ classBt+Language.csvSeparator
						+ travelTime
						+ Language.csvSeparator
						+ isValidString
						+ "\n");
			}
			output.flush();
			output.close();
		} catch (IOException e) {
			logger.error(e.getMessage()+"\n"+e.getCause());
			throw new Exception(e.getMessage()+"\n"+e.getCause());
		}
		return fileName;
	}
	
	/**
	 * Action on the server-side when clicking on the export statistical data button.
	 */
	public String getStatisticalTravelTimeCsvFilePath(String itinerary, Date startDate, Date endDate, int period, int horizon, String typeOfTimestamp) throws Exception {
		String fileName = Environment.getTempDirectory() + "data_stat_" + itinerary + "_" + dateFormatter.format(startDate) + "_" + dateFormatter.format(endDate) + ".csv";
		fileName = Normalizer.normalize(fileName, Normalizer.Form.NFD);
		fileName = fileName.replaceAll("[^\\p{ASCII}]", "");
		fileName = fileName.replace(" ", "_");
		String startDateString = getDateAsString(startDate);
		String endDateString = getDateAsString(endDate);
		String periodString = Integer.toString(period*60);
		String horizonString = Integer.toString(horizon*60);
		String isAvergaedCentred = "False";
		if (typeOfTimestamp.equals("centré")) {
			isAvergaedCentred = "True";
		}
		Process createCsvFile;
		String[] parameters = GenericDatabaseDriver.getEnvironmentVariable(Language.envGwtVar).getContent().split(",");
		if (parameters.length != 2) {
			logger.error("The sys env variable 'gwt' has not the good format");
			throw new Exception("The sys env variable 'gwt' has not the good format");
		}
		String scriptFilePath = parameters[0];
		String propertiesFilePath = parameters[1];
		String [] command = {
			"python",
			scriptFilePath,
			propertiesFilePath,
			itinerary,
			fileName,
			startDateString,
			endDateString,
			horizonString,
			periodString,
			isAvergaedCentred
		};
		try {
			String line;
			createCsvFile = Runtime.getRuntime().exec(command);
			ErrorStreamReader errorStreamReader = new ErrorStreamReader(createCsvFile);
			errorStreamReader.start();
			BufferedReader in = new BufferedReader(new InputStreamReader(createCsvFile.getInputStream()));
			while ((line = in.readLine()) != null) {
				logger.debug(line);
			}
			in.close();
			errorStreamReader.interrupt();
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw e;
		}
		return fileName;
	}
	
	/**
	 * Action on the server-side when clicking on the export weather data button
	 */
	public String getWeatherDataCsvFilePath(String stationName, Date startDate, Date endDate) throws Exception {
		String startDateString = getDateAsString(startDate).replace(":", "-").replace(" ", "-");
		startDateString = startDateString.substring(0, startDateString.length()-3);
		String endDateString = getDateAsString(endDate).replace(":", "-").replace(" ", "-");
		endDateString = endDateString.substring(0, endDateString.length()-3);
		String fileName = Environment.getTempDirectory()+"data_weather_"+stationName+"_"+startDateString+"_"+endDateString+".csv";
		fileName = fileName.replace(">", "_").replace(" ", "_");
		fileName = Normalizer.normalize(fileName, Normalizer.Form.NFD);
		fileName = fileName.replaceAll("[^\\p{ASCII}]", "");
		try {
			FileOutputStream fw = new FileOutputStream(fileName, false);
			BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(fw, Encoding.fileFormat));
			List<WthMeteoData> weatherDataList = GenericDatabaseDriver.getWeatherDataForStation(stationName, startDate, endDate);
			bufferedWriter.write(Language.weatherExportTimestampColumn 
					+ Language.csvSeparator 
					+ Language.weatherExportStationColumn 
					+ Language.csvSeparator 
					+ Language.weatherExportAirTemperatureColumn 
					+ Language.csvSeparator 
					+ Language.weatherExportDewTemperatureColumn 
					+ Language.csvSeparator 
					+ Language.weatherExportHumidityColumn 
					+ Language.csvSeparator 
					+ Language.weatherExportRoadTemperatureColumn 
					+ Language.csvSeparator 
					+ Language.weatherExportBelowSurfTemperatureColumn 
					+ Language.csvSeparator 
					+ Language.weatherExportSurfaceStateColumn
					+ Language.csvSeparator 
					+ Language.weatherExportSalinityColumn
					+ Language.csvSeparator 
					+ Language.weatherExportFreezingTemperatureColumn
					+ Language.csvSeparator 
					+ Language.weatherExportPrecipitationTypeColumn
					+ Language.csvSeparator 
					+ Language.weatherExportPrecipitationIntensityColumn
					+ Language.csvSeparator 
					+ Language.weatherExportFreezingRiskColumn
					+ Language.csvSeparator 
					+ Language.weatherExportLatencyColumn
					+ "\n");
			DecimalFormatSymbols franceSymbols = new DecimalFormatSymbols(Locale.FRANCE);
			franceSymbols.setDecimalSeparator(',');
			DecimalFormat decimalFormatTwoDecimal = new DecimalFormat();
			decimalFormatTwoDecimal.setMaximumFractionDigits(2);
			decimalFormatTwoDecimal.setDecimalFormatSymbols(franceSymbols);
			for (WthMeteoData weatherData : weatherDataList){
				String timestamp = getDateAsString(weatherData.getId().getTimestamp());
				String station = weatherData.getId().getStation();
				String airTemperature = weatherData.getTemperatureAir() == null ? "" : decimalFormatTwoDecimal.format(weatherData.getTemperatureAir());
				String dewTemperature = weatherData.getTemperatureDew() == null ? "" : decimalFormatTwoDecimal.format(weatherData.getTemperatureDew());
				String humidity = weatherData.getHumidity() == null ? "" : decimalFormatTwoDecimal.format(weatherData.getHumidity());
				String roadTemperature = weatherData.getTemperatureRoad() == null ? "" : decimalFormatTwoDecimal.format(weatherData.getTemperatureRoad());
				String belowSurfTemperature = weatherData.getTemperatureBelowSurf() == null ? "" : decimalFormatTwoDecimal.format(weatherData.getTemperatureBelowSurf());
				String surfaceState = weatherData.getSurfaceState() == null ? "" : Integer.toString(weatherData.getSurfaceState());
				String salinity = weatherData.getSalinity() == null ? "" : decimalFormatTwoDecimal.format(weatherData.getSalinity());
				String freezingTemperature = weatherData.getTemperatureFreezing() == null ? "" : decimalFormatTwoDecimal.format(weatherData.getTemperatureFreezing());
				String precipitationType = weatherData.getPrecipitationType() == null ? "" : Integer.toString(weatherData.getPrecipitationType());
				String precipitationIntensity = weatherData.getPrecipitationIntensity() == null ? "" : Integer.toString(weatherData.getPrecipitationIntensity());
				String freezingRisk = weatherData.getFreezingRisk() == null ? "" : Integer.toString(weatherData.getFreezingRisk());
				String latency = weatherData.getLatency() == null ? "" : Integer.toString(weatherData.getLatency());
				bufferedWriter.write(timestamp 
						+ Language.csvSeparator
						+ station
						+ Language.csvSeparator
						+ airTemperature
						+ Language.csvSeparator
						+ dewTemperature
						+ Language.csvSeparator
						+ humidity
						+ Language.csvSeparator
						+ roadTemperature
						+ Language.csvSeparator
						+ belowSurfTemperature
						+ Language.csvSeparator
						+ surfaceState
						+ Language.csvSeparator
						+ salinity
						+ Language.csvSeparator
						+ freezingTemperature
						+ Language.csvSeparator
						+ precipitationType
						+ Language.csvSeparator
						+ precipitationIntensity
						+ Language.csvSeparator
						+ freezingRisk
						+ Language.csvSeparator
						+ latency
						+ "\n");
			}
			bufferedWriter.flush();
			bufferedWriter.close();
		} catch (IOException e) {
			logger.error(e.getMessage()+"\n"+e.getCause());
			throw new Exception(e.getMessage()+"\n"+e.getCause());
		} catch (Exception e) {
			logger.error(e.getMessage()+"\n"+e.getCause());
			throw new Exception(e.getMessage()+"\n"+e.getCause());
		}
		return fileName;
	}
	
	/**
	 * Service to export data from the stations table
	 * @param stations
	 * @throws Exception
	 */
	@Override
	public String exportStationsTable(List<RsuStation> stations) throws Exception {
		
		String fileName = Environment.getTempDirectory()+"stations.csv"; 
		try {
			FileOutputStream fileOutputStream = new FileOutputStream(fileName, false);
			BufferedWriter output = new BufferedWriter(new OutputStreamWriter(fileOutputStream, Encoding.fileFormat));
			output.write(Language.stationId+Language.csvSeparator+
					Language.stationSerial+Language.csvSeparator+
					Language.stationHost+Language.csvSeparator+
					Language.stationLatitude+Language.csvSeparator+
					Language.stationLongitude+Language.csvSeparator+
					Language.stationRoad+Language.csvSeparator+
					Language.stationPosition+Language.csvSeparator+
					Language.stationCycleCountingSec+Language.csvSeparator+
					Language.stationCycleTravelTimeSec+Language.csvSeparator+
					Language.stationCycleWeatherSec+Language.csvSeparator+
					Language.stationCycleV2XSec+
					"\n");
			for (RsuStation station : stations){
				output.write(station.getId()+Language.csvSeparator+
						station.getSerial()+Language.csvSeparator+
						station.getHost()+Language.csvSeparator+
						(station.getLatitude() == null ? "" : station.getLatitude()) +Language.csvSeparator+
						(station.getLongitude() == null ? "" : station.getLongitude()) +Language.csvSeparator+
						station.getRoad()+Language.csvSeparator+
						station.getPosition()+Language.csvSeparator+
						(station.getCycleCountingSec() == null ? "" : station.getCycleCountingSec()) +Language.csvSeparator+
						(station.getCycleTravelTimeSec() == null ? "" : station.getCycleTravelTimeSec()) +Language.csvSeparator+
						(station.getCycleWeatherSec() == null ? "" : station.getCycleWeatherSec()) +Language.csvSeparator+
						(station.getCycleV2xSec() == null ? "" : station.getCycleV2xSec()) +
						"\n");
			}
			output.flush();
			output.close();
			return fileName;
		} catch (IOException e) {
			logger.error(e.getMessage()+"\n"+e.getCause());
			throw new Exception(e.getMessage()+"\n"+e.getCause());
		}
	}
	
	
	
	
	/**
	 * Exports data from the lanes table
	 * @param lanes
	 * @throws Exception
	 */
	@Override
	public String exportLanesTable() throws Exception {
		String fileName = Environment.getTempDirectory() + "voies.csv";
		List<CtLane> lanes = GenericDatabaseDriver.getAllLanes();
		try {
			FileOutputStream fileOutputStream = new FileOutputStream(fileName, false);
			BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(fileOutputStream, Encoding.fileFormat));
			bufferedWriter.write(Language.lanesExportStationColumn + Language.csvSeparator +
					Language.lanesExportLaneColumn + Language.csvSeparator +
					Language.lanesExportRoadColumn + Language.csvSeparator +
					Language.lanesExportWayColumn + Language.csvSeparator +
					Language.lanesExportPositionColumn +
					"\n");
			for (CtLane lane : lanes) {
				bufferedWriter.write(lane.getId().getStation() + Language.csvSeparator +
						lane.getId().getLane() + Language.csvSeparator +
						lane.getRoad() + Language.csvSeparator +
						lane.getWay() + Language.csvSeparator +
						lane.getPosition() +
						"\n");
			}
			bufferedWriter.flush();
			bufferedWriter.close();
			return fileName;
		} catch (IOException e) {
			logger.error(e.getMessage()+"\n"+e.getCause());
			throw new Exception(e.getMessage()+"\n"+e.getCause());
		}
	}
	
	
	
	
	
	
	
	public String exportClosedAlarmsTable(Date startDate, Date stopDate, String Source, String Type, String State) throws Exception {
		List<AlAlarmClosed> Alarms = AlarmDatabaseDriver.getAlarmsHistoryFiltered(startDate, stopDate, Source, Type, State);
		String startDateString = dateFormatter.format(startDate);
		String stopDateString = dateFormatter.format(stopDate);
		String fileName = Environment.getTempDirectory()+"alarmes_"+startDateString+"_"+stopDateString+".csv"; 
		try {
			FileOutputStream fileOutputStream = new FileOutputStream(fileName, false);
			BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(fileOutputStream, Encoding.fileFormat));
			bufferedWriter.write(
					Language.typeString+
					Language.csvSeparator+
					Language.equipmentString+
					Language.csvSeparator+
					Language.alarmsStatusHeader+
					Language.csvSeparator+
					Language.openingTimestampString+
					Language.csvSeparator+
					Language.acknowledgedTimestampString+
					Language.csvSeparator+
					Language.closingTimestampString+
					"\n");
			for (AlAlarmClosed alarm : Alarms){
				String type = alarm.getId().getType() == null ? "" : alarm.getId().getType();
				String equipment = alarm.getId().getSource() == null ? "" : alarm.getId().getSource();
				String status = alarm.getStatus() == null ? "" : alarm.getStatus();
				String openingTimestamp = alarm.getId().getOpeningTimestamp() == null ? "" : dateTimeFormatter.format(alarm.getId().getOpeningTimestamp());
				String acknowledgmentTimestamp = alarm.getAcknowledgementTimestamp() == null ? "" : dateTimeFormatter.format(alarm.getAcknowledgementTimestamp());
				String closingTimestamp = alarm.getClosingTimestamp() == null ? "" : dateTimeFormatter.format(alarm.getClosingTimestamp());
				bufferedWriter.write(
						type+Language.csvSeparator+
						equipment+Language.csvSeparator+
						status+Language.csvSeparator+
						openingTimestamp+Language.csvSeparator+
						acknowledgmentTimestamp+Language.csvSeparator+
						closingTimestamp+
						"\n");
			}
			bufferedWriter.flush();
			bufferedWriter.close();
		} catch (IOException e) {
			logger.error(e.getMessage()+"\n" + e.getCause());
			throw new Exception(e.getMessage()+"\n" + e.getCause());
		}
		return fileName;
	}
	
	public String exportOpenedAlarmsTable() throws Exception {
		List<AlAlarmOpened> Alarms = AlarmDatabaseDriver.getOpenedAlarms();
		String fileName = Environment.getTempDirectory()+"alarmes_ouvertes.csv"; 
		try {
			FileOutputStream fileOutputStream = new FileOutputStream(fileName, false);
			BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(fileOutputStream, Encoding.fileFormat));
			bufferedWriter.write(
					Language.typeString+
					Language.csvSeparator+
					Language.equipmentString+
					Language.csvSeparator+
					Language.alarmsStatusHeader+
					Language.csvSeparator+
					Language.openingTimestampString +
					Language.csvSeparator+
					Language.acknowledgedTimestampString +
					"\n");
			for (AlAlarmOpened alarm : Alarms){
				String type = alarm.getId().getType();
				String equipment = alarm.getId().getSource();
				String status = alarm.getStatus();
				String openingTimestamp = alarm.getId().getOpeningTimestamp() == null ? "" : dateTimeFormatter.format(alarm.getId().getOpeningTimestamp());
				String acknowledgmentTimestamp = alarm.getAcknowledgementTimestamp() == null ? "" : dateTimeFormatter.format(alarm.getAcknowledgementTimestamp());
				bufferedWriter.write(
						type+Language.csvSeparator+
						equipment+Language.csvSeparator+
						status+Language.csvSeparator+
						openingTimestamp+Language.csvSeparator+
						acknowledgmentTimestamp+
						"\n");
			}
			bufferedWriter.flush();
			bufferedWriter.close();
		} catch (IOException e) {
			logger.error(e.getMessage()+"\n" + e.getCause());
			throw new Exception(e.getMessage()+"\n" + e.getCause());
		}
		return fileName;
	}
	
	@Override
	public String exportItinerariesTable(List<TtItinerary> itineraries) throws Exception {
		String fileName = Environment.getTempDirectory()+"itinerariesTable.csv"; 
		try {
			FileOutputStream fw = new FileOutputStream(fileName, false);
			BufferedWriter output = new BufferedWriter(new OutputStreamWriter(fw, Encoding.fileFormat));
			output.write(
					Language.originColumn +
					Language.csvSeparator +
					Language.destinationColumn +
					Language.csvSeparator +
//					Language.nameColumn +
//					Language.csvSeparator +
					Language.nominalTravelTimeColumn +
					Language.csvSeparator +
					Language.maxTravelTimeColumn +
					Language.csvSeparator +
					Language.levelOfServiceThreshold1Column +
					Language.csvSeparator +
					Language.levelOfServiceThreshold2Column +
					Language.csvSeparator +
					Language.scaleColumn +
					"\n");
			for (TtItinerary itnerary : itineraries){
				output.write(itnerary.getId().getOrigin() +
						Language.csvSeparator +
						itnerary.getId().getDestination() +
						Language.csvSeparator +
//						itnerary.getName() +
//						Language.csvSeparator +
						itnerary.getNominalTravelTime() +
						Language.csvSeparator +
						itnerary.getMaxTravelTime() +
						Language.csvSeparator +
						itnerary.getLevelOfServiceThreshold1() +
						Language.csvSeparator +
						itnerary.getLevelOfServiceThreshold2() +
						Language.csvSeparator +
						itnerary.getScale() +
						"\n");
			}
			output.flush();
			output.close();
		} catch (IOException e) {
			logger.error(e.getMessage()+"\n"+e.getCause());
			throw new Exception(e.getMessage()+"\n"+e.getCause());
		}
		return fileName;
	}
	
	/**
	 * Turn a date into String using following date format yyyy-MM-dd HH:mm:ss
	 * @param date Date to return as a String
	 * @return Empty String if date is null
	 */
	public static String getDateAsString(Date date){
		if (date==null) {
			return Language.emptyString;
		}	
		return (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(date);
	}
}
