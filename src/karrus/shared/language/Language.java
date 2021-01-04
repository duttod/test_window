package karrus.shared.language;

import karrus.shared.hibernate.CtLane;
import karrus.shared.hibernate.UsrUser;


public class Language {
	public static String dateTimeFieldMissingError = "Un champ est manquant dans la date ou le temps.";//Data is missing in time parameter.");
	public static String hourNotValidError = "L'heure n'est pas valide.";//"The hour paramter is not valid.");
	public static String minuteNotValidError = "Les minutes ne sont pas valides.";//The minute paramter is not valid.");
	public static String secondNotValidError = "Les secondes ne sont pas valides";//.The second paramter is not valid.");
	public static String dateTimeFormatIncorrectError = "Le format de la date est incorrect.";//The time format is incorrect");
	public static String maxRequestSize(int maxHour){return "Taille de requête maximale : "+maxHour+" h.";};
	public static String closableTabPanelNotExistString(String closableTabPanelTitle){return "L'onglet '"+closableTabPanelTitle+"' n'existe pas.";};
	public static String maxOpenedTabsError = "Il y a trop d'onglets ouverts. Il faut en fermer.";
	public static String tableString = "table ";
	public static String plotString = "graphe ";
	public static String alreadyTitleError = "Un onglet a déjà été nommé ainsi. Entrez un nouveau nom.";
	public static String noOpenedTabError(String tabTitle){return "Aucun onglet '"+tabTitle+"' n'est ouvert.";};
	public static String userString = "Utilisateur ";
	public static String passwordString = "Mot de passe";
	public static String okString = "Ok";
	public static String emptyString = "";
	public static String alarmType = "alarmes";
	public static String alertType = "alertes";
	public static String allString = "tout";
	public static String errorFieldEmpty(String text) {return "Aucun élément n'est entré dans le champ '"+text+"'.";}
	public static String paramaterFormatError(String param){return " Le paramètre '"+param+"' n'a pas le bon format.";};
	public static String initialDateString = "Date de début";
	public static String finalDateString   = "Date de fin";
	public static String periodString = "période";
	public static String fullDayString   = "journée";
	public static String morningString   = "matin";
	public static String afternoonString   = "après-midi";
	public static String plageHoraireString = "Plage horaire";
	public static String initialAfterFinalDateError = "La date de début ne peut pas être après la date de fin.";
	public static String finalBeforeInitialDateError = "La date de fin ne peut pas être avant la date de fin.";
	public static String displayString = "Afficher";
	public static String extractionString = "Extraction";
	public static String countingMenu = "RDT";
	public static String weatherDataMenu = "Météo";
	public static String rsuDiagnosticMenu = "UBR";
	public static String v2xAlarmsMenu = "V2X";
	public static String noDataForThisTimePeriod = "Pas de données pour cette période.";
	public static String blankString = "_blank";
	public static String CSVexportString = "Exporter";
	public static String wayString = "Sens";
	public static String accessPointString = "Point d'accès";
	public static String wayOfTrafficString = "Sens de circulation";
	public static String noElementChosenError(String listName){return "Aucun élément n'est choisi dans la liste '"+listName+"'.";};
	public static String doubleDot = " :";
	public static String countYLabelString = "Débit horaire (veh/h)";
	public static String occupancyYLabelString = "Taux d'occupation (%)";
	public static String averageSpeedYLabelString = "Vitesse (km/h)";
	public static String countingMenuString = "Comptage";
	public static String administrationLabelString = "Administration";
	public static String usersLabelString = "Utilisateurs";
	public static String currentPasswordString = "Saisissez le mot de passe actuel";
	public static String enterNewPasswordString = "Entrez un nouveau mot de passe";	
	public static String reEnterNewPasswordString = "Entrez à nouveau le nouveau mot de passe";
	public static String saveString = "sauvegarder";
	public static String cancelString = "annuler";
	public static String incorrectPasswordForUserError(String user){return "Le mot de passe courant pour l'utilisateur '"+user+"' est incorrect.";};
	public static String incorrectNewPasswordsError = "Les nouveaux mots de passe entrés ne sont pas les mêmes.";
	public static String userField = "Utilisateur";
	public static String userEmailField = "Email";
	public static String newPasswordField = "Nouveau mot de passe";
	public static String newPassword2Field = "Nouveau mot de passe (2)";
	public static String groupField = "Groupe";
	public static String userNotSaved(String user){return "L'utilisateur '"+user+"' n'a pas été sauvegardé.";};
	public static String userConstraintViolationError(String userLogin) {return "Le login '" + userLogin + "' est déjà utilisé, vous devez en choisir un autre.";}
	public static String userSaved(String user){return "L'utilisateur '"+user+"' a été sauvegardé.";};
	public static String loginEmptyError = "Le login ne doit pas être vide.";
	public static String userLoginAlreadyUsedError(String login){ return "Un utilisateur est déjà identifié avec le login '"+login+"'. Entrez une nouveau login.";};
	public static String passwordEmptyError = "Le mot de passe ne doit pas être vide.";
	public static String groupEmptyError = "Le groupe ne doit pas être vide.";
	public static String changePasswordString = "changer le mot de passe";
	public static String editUserString = "Editer l'utilisateur";
	public static String addNewUserString = "Ajouter un nouvel utilisateur";
	public static String removeUserString = "Supprimer un utilisateur";
	public static String userListString = "Liste des utilisateurs :";
	public static String loginColumn = "Utilisateur";
	public static String emailColumn = "Email";
	public static String passwordColumn = "Mot de passe";
	public static String groupColumn = "Groupe";
	public static String noSelectedUserError = "Un utilisateur doit être sélectionné dans le tableau pour l'éditer ou le supprimer.";
	public static String impossibleToRemoveMainUserString = "Vous ne pouvez pas supprimer cet utilisateur car c'est l'utilisateur connecté.";
	public static String confirmRemovingUser(String login){return "Confirmer la suppression de l'utilisateur '"+login+"' ?";}
	public static String userNotRemoved(String login){return "L'utilisateur '"+login+"' n'a pas été supprimé.";};
	public static String userRemoved(String login){return "L'utilisateur '"+login+"' a été supprimé.";};
	public static String yesString = "oui";
	public static String noString = "non";
	public static String csvSeparator = ";";
	public static String loginError1 = "Erreur d'authentification. Nom d'utilisateur incorrect.";
	public static String loginError2 = "Erreur d'authentification. Utilisateur défini plusieurs fois dans la base de données. Contacter l'administateur.";
	public static String loginError3 = "Erreur d'authentification. Mot de passe incorrect.";
	public static String loginError4 = "Erreur d'authentification. Erreur dans l'accès à la base de données. Contacter l'administateur.";
	public static String userProfileOperator = "opérateur";
	public static String userProfileAdmin = "administrateur";
	public static String userProfileGuest = "invité";
	public static String nameColumn = "nom";
	public static String alarmsHistoryString = "Historique des alarmes";
	public static String alarmsLowerCaseString = "alarmes";
	public static String tableNameString = "nom de la table";
	public static String displayTableString = "afficher la (les) table(s)";
	public static String alarmsDisplayButton = "Afficher";
	public static String alarmsStatusHeader = "Statut";
	public static String openingTimestampString = "Horodate de création";
	public static String acknowledgedTimestampString = "Horodate d'acquittement";
	public static String disappearanceTimestampString = "Horodate de disparition";
	public static String lastChangedTimestampString = "Horodate de dernière modification";
	public static String closingTimestampString = "Horodate de clotûre";
	public static String openedAlarmString = "Ouvert";
	public static String allAlarmString = "Toutes";
	public static String middleware = "Middleware";
	public static String closedAlarmString = "Clôturé";
	public static String filterLabel = "Filtre";
	public static String noDataForThisSetOfParameters = "Aucune alarme ne correspond à vos critères de recherche";
	public static String equipmentString = "Equipement";
	public static String typeString = "Type";
	public static String statusString = "Statut";
	public static String exportTableString = "Exporter";
	public static String reopenAlarmAction = "Réouvrir";
	public static String clotureAlarmAction = "Clôturer";
	public static String acknowledgeAlarmAction = "Acquitter";
	public static String openAlarmAction = "Ouvrir";
	public static String selectAll = "Sélectionner tout ";
	public static String descritptionString = "Descritption";
	public static String noAlarmSelected = "Aucune alarme n'a été sélectionnée";
	public static String closingAlarmsConfirmation = "Les alarmes sélectionnées ont bien été clôturées";
	public static String alarmsAcknowledgementsConfirmation = "Les alarmes sélectionnées ont bien été acquittées";
	public static String customizedString = "personnalisé";
	public static String doubleError = " doit être un double.";
	public static String integerError = " doit être un entier.";
	public static String parameterError(String parameter){return "Le paramètre '"+parameter+"' ";};
	public static String stationIdAlreadyUsedError(String id){ return "Une station est déjà identifiée avec l'id '"+id+"'. Entrez un nouvel id.";};
	public static String stationNameAlreadyUsedError(String name){ return "Une station est déjà identifiée avec le nom '"+name+"'. Entrez un nouveau nom.";};
	public static String nameEmptyError = "Le nom ne doit pas être vide.";
	public static String tcpPort1emptyError = "Le port tcp 1 ne doit pas être vide.";
	public static String stationNotSaved(String stationName) {return "La station '"+stationName+"' n'a pas été sauvegardée.";};
	public static String stationSaved(String stationName){return "La station '"+stationName+"' a été sauvegardée.";};
	public static String stationsTitle = "Stations :";
	public static String editStationString = "Editer la station";
	public static String addNewStationString = "Ajouter une nouvelle station";
	public static String removeStationString = "Supprimer une station";
	public static String noSelectedStationError = "Une station doit être sélectionnée dans le tableau pour l'éditer ou la supprimer.";
	public static String confirmRemovingStation(String stationName){return "Confirmer la suppression de la station '"+stationName+"' ?";}
	public static String stationNotRemoved(String stationName){return "La station '"+stationName+"' n'a pas été supprimée.";};
	public static String stationRemoved(String stationName){return "La station '"+stationName+"' a été supprimée.";};
	public static String stationsConfigString = "UBR";
	// Stations
	public static String stationId = "Identifiant";
	public static String stationSerial = "Numéro de série";
	public static String stationHost = "Hôte";
	public static String stationLatitude = "Latitude";
	public static String stationLongitude = "Longitude";
	public static String stationRoad = "Route";
	public static String stationPosition = "Position";
	public static String stationEnabled = "Activé";
	public static String stationCycleCountingSec = "Période TR";
	public static String stationCycleTravelTimeSec = "Période TP";
	public static String stationCycleWeatherSec = "Période MET";
	public static String stationCycleV2XSec = "Période V2X";
	public static String stationLastLatitude = "Dernière latitude";
	public static String stationLastLongitude = "Dernière longitude";
	public static String stationLastHdop = "Dernièr Hdop";
	public static String stationLastTimestamp = "Dernier timestamp";
	public static String serialString = "Numéro de série";
	public static String stationIdEmptyError = "L'id ne doit pas être vide.";
	public static String stationSerialEmptyError = "Le numéro de série ne doit pas être vide.";
	public static String stationHostEmptyError = "L'hôte ne doit pas être vide.";
	public static String stationConstraintViolationError(String id) {return "L'id '" + id + "' est déjà utilisé, vous devez en choisir un autre.";}
	// user
	public static String user_currentUser = "Actuellement, vous êtes logué en : ";
	public static String user_currentGroupe = "Vous avez les privilèges du groupe : ";
	// Email
	public static String emailHeaderAddress = "Adresse";
    public static String emailAlarms = "alarms"; 
	public static String alertString = "alertes";
	public static String dailyReportString = "rapports-J";
	public static String weeklyReportString = "rapports-H";
	public static String monthlyReportString = "rapports-M";
	public static String emailNotRemoved(String email){return "Le mail '"+email+"' n'a pas été supprimé.";};
	public static String emailRemoved(String address){return "Le mail '"+address+"' a été supprimé.";};
	public static String emailAlreadyUsedError(String address){ return "Un email est déjà identifié avec l'adresse '"+address+"'. Entrez une nouvelle adresse.";};
	public static String emailNotSaved(){return "Les paramètres pour la diffusion e-mail n'ont pas été sauvegardés.";};
	public static String emailSaved(){return "Les paramètres pour la diffusion e-mail ont été sauvegardés.";};
	public static String emailNotSaved(String address){return "Le mail '"+address+"' n'a pas été sauvegardé.";};
	public static String emailSaved(String address){return "Le mail '"+address+"' a été sauvegardé.";};
	public static String addressEmptyError = "L'adresse ne doit pas être vide.";
	public static String emailTitle = "E-mail :";
	public static String editMailString = "éditer le mail";
	public static String addNewEmailString = "ajouter un nouveau mail";
	public static String removeEmailString = "supprimer un mail";
	public static String noSelectedMailError = "Un mail doit être sélectionné dans le tableau pour l'éditer ou le supprimer.";
	public static String confirmRemovingEmail(String email){return "Confirmer la suppression du mail '"+email+"' ?";}
	// Export service
	public static String exportServiceTimestamp = "Horodate";
	public static String exportServiceStation = "Station";
	public static String exportServiceLane = "Voie";
	public static String exportServiceSpeed = "Vitesse";
	public static String exportServiceCount = "Débit";
	public static String exportServiceCountLong = "Débit PL";
	public static String exportServiceOccupancy = "Taux";
	public static String exportServiceOpcuaTagId = "Id";
	public static String exportServiceOpcuaTagType = "Type";
	public static String exportServiceOpcuaTagValue = "Valeur";
	public static String exportServiceOpcuaTagEnabled = "Activé";
	public static String exportServiceTrafficAlarmLabel = "Label";
	public static String exportServiceTrafficAlarmSource = "Source";
	public static String exportServiceTrafficAlarmData = "Donnée";
	public static String exportServiceTrafficAlarmHorizon = "Horizon";
	public static String exportServiceTrafficAlarmPeriodicity = "Périodicité";
	public static String exportServiceTrafficAlarmThreshold = "Seuil";
	// Menu
	public static String synopticMenu = "Synoptiques";
	public static String referentialMenu = "Référentiel";
	// SynopticMenu
	public static String countSynoptic ="RDT";
	public static String travelTimeSynoptic ="TP";
	public static String weatherSynoptic ="Météo";
	public static String alarmsSynoptic ="Alarmes";
	public static String countDisabledSynoptic ="Comptage";
	public static String travelTimeDisabledSynoptic ="Temps de parcours";
	public static String weatherDisabledSynoptic ="Données météo";
	public static String equipmentsDisabledSynoptic ="Equipements";
	// Alarms
	public static String closedStatus = "Clôturé";
	public static String acknowledgedStatus = "Acquitté";
	// Counting sub menu/SelectDataSourcesPanel
	public static String station = "Station";
	public static String way = "Sens";
	public static String wayUp = "Montant";
	public static String wayDown = "Descendant";
	// TimeHorizonSelector
	public static String min5 = "5min";
	public static String min10 = "10min";
	public static String min20 = "20min";
	public static String min30 = "30min";
	public static String min45 = "45min";
	public static String h1 = "1h";
	public static String h1min30 = "1h30";
	public static String h2 = "2h";
	public static String h3 = "3h";
	public static String h4 = "4h";
	public static String h5 = "5h";
	public static String h6 = "6h";
	// CountDataDashboardTimer
	public static String countColumn = "count";
	public static String speedColumn = "speed";
	public static String travelTimeColumn = "traveltime";
	public static String individualValidTravelTimeColumn = "traveltime_indiv_valid";
	public static String individualNotValidTravelTimeColumn = "traveltime_indiv_no_valid";
	public static String occupancyColumn = "occupancy";
	public static String getFrenchForColumn(String column){
		if (column.equals(Language.speedColumn))
			return speedDashboardString;
		if (column.equals(Language.countColumn))
			return countDashboardString;
		if (column.equals(Language.occupancyColumn))
			return occupancyDashboardString;
		if (column.equals(Language.travelTimeColumn))
			return travelTimeDashboardString;
		if (column.equals(Language.individualValidTravelTimeColumn))
			return individualValidTravelTimeDashboardString;
		if (column.equals(Language.individualNotValidTravelTimeColumn))
			return individualNotValidTravelTimeDashboardString;
		if (column.equals(Language.airTemperature)) {
			return Language.airTemperatureLegend;
		}
		if (column.equals(Language.dewTemperature)) {
			return Language.dewTemperatureLegend;
		}
		if (column.equals(Language.roadTemperature)) {
			return Language.roadTemperatureLegend;
		}
		if (column.equals(Language.belowSurfTemperature)) {
			return Language.belowSurfTemperatureLegend;
		}
		if (column.equals(Language.freezingTemperature)) {
			return Language.freezingTemperatureLegend;
		}
		return "";
	}
	public static String getUnitsForColumn(String column){
		if (column.equals(Language.speedColumn))
			return speedAndUnitsString;
		if (column.equals(Language.countColumn))
			return countAndUnitsString;
		if (column.equals(Language.occupancyColumn))
			return occupancyAndUnitsString;
		if (column.equals(Language.travelTimeColumn))
			return travelTimeAndUnitsString;
		if (column.equals(Language.individualValidTravelTimeColumn))
			return travelTimeAndUnitsString;
		if (column.equals(Language.individualNotValidTravelTimeColumn))
			return travelTimeAndUnitsString;
		if (column.equals(Language.airTemperature)) {
			return temperatureYLabel;
		}
		if (column.equals(Language.dewTemperature)) {
			return temperatureYLabel;
		}
		if (column.equals(Language.roadTemperature)) {
			return temperatureYLabel;
		}
		if (column.equals(Language.belowSurfTemperature)) {
			return temperatureYLabel;
		}
		if (column.equals(Language.freezingTemperature)) {
			return temperatureYLabel;
		}
		return "";
	}
	public static String getDataUnitForColumn(String column){
		if (column.equals(Language.speedColumn))
			return speedDataUnitString;
		if (column.equals(Language.countColumn))
			return countDataUnitString;
		if (column.equals(Language.occupancyColumn))
			return occupancyDataUnitString;
		if (column.equals(Language.travelTimeColumn))
			return travelTimeDataUnitsString;
		if (column.equals(Language.individualValidTravelTimeColumn))
			return travelTimeDataUnitsString;
		if (column.equals(Language.individualNotValidTravelTimeColumn))
			return travelTimeDataUnitsString;
		if (column.equals(Language.airTemperature)) {
			return temperatureUnit;
		}
		if (column.equals(Language.dewTemperature)) {
			return temperatureUnit;
		}
		if (column.equals(Language.roadTemperature)) {
			return temperatureUnit;
		}
		if (column.equals(Language.belowSurfTemperature)) {
			return temperatureUnit;
		}
		if (column.equals(Language.freezingTemperature)) {
			return temperatureUnit;
		}	
		return "";
	}
	private static String speedDashboardString = "Vitesse";
	private static String occupancyDashboardString = "Taux d'occupation";
	private static String travelTimeDashboardString = "Temps de parcours";
	private static String individualValidTravelTimeDashboardString = "Temps de parcours individuels valides";
	private static String individualNotValidTravelTimeDashboardString = "Temps de parcours individuels non valides";
	private static String countDashboardString = "Débit";
	private static String speedAndUnitsString = "Vitesse (km/h)";
	private static String speedDataUnitString = "km/h";
	private static String countAndUnitsString = "Débit (véh/h)";
	private static String countDataUnitString = "véh/h";
	private static String occupancyAndUnitsString = "Taux d'occupation (%)";
	private static String occupancyDataUnitString = "%";
	private static String travelTimeAndUnitsString = "Temps de parcours (min)";
	private static String travelTimeDataUnitsString = "min";
	// PlotMinMax
	public static String columnUnknownError(String column){return "La colonne '"+column+"' n'existe pas.";};
	// utils/Colors
	public static String lengthColumn = "length";
	public static String gapColumn = "gap";
	public static String genericErrorString = "Erreur, contactez votre administrateur";
	// SVG Element
	public static String nullSvgElementError(String svgId) {
		return "Il n'y a pas d'objet svg correspondant pour l'id '" + svgId + "'";
	}
	// CountDataHistoryMenu
	public static String countingDataHistoryTabLabel = "Données de comptage";
	// WeatherDataHistoryMenu
	public static String weatherDataHistoryTabLabel = "Données météo";
	// WeatherParametersPanel
	public static String weatherStations = "Station";
	public static String temperaturesAndHumidityPrefix = "T/H";
	public static String surfaceStateAndPrecipitationPrefix = "C/P";
	// WeatherPlotPanel
	public static String temperatureYLabel = "Température (°C)";
	public static String humidityYLabel = "Hygrométrie (%)";
	public static String temperatureUnit = "°C";
	public static String humidityUnit = "%";
	public static String airTemperatureLegend = "Tair";
	public static String dewTemperatureLegend = "Trosée";
	public static String humidityLegend = "Hygrométrie";
	public static String roadTemperatureLegend = "Tchaussée";
	public static String belowSurfTemperatureLegend = "T5cm";
	public static String freezingTemperatureLegend = "Tcongélation";
	// WeatherStatesPlotPanel
	public static String salinityYLabel = "Concentration (%)";
	public static String surfaceStateLegend = "État de la chaussée";
	public static String freezingRiskLegend = "Risque de verglas";
	public static String salinityLegend = "Concentration de sel";
	public static String precipitationTypeLegend = "Nature des précipitations";
	public static String precipitationIntensityLegend = "Intensité des précipitations";
	public static String surfaceState_0 = "Sec";
	public static String surfaceState_1 = "Humide";
	public static String surfaceState_2 = "Mouillé";
	public static String surfaceState_3 = "Ruisselant";
	public static String surfaceState_4 = "Givré";
	public static String surfaceState_5 = "Verglacé";
	public static String surfaceState_6 = "Humide salé";
	public static String surfaceState_7 = "Mouillé salé";
	public static String surfaceState_8 = "Enneigé";
	public static String surfaceState_9 = "En défaut";
	public static String freezingRisk_0 = "Aucun";
	public static String freezingRisk_1 = "Faible";
	public static String freezingRisk_2 = "Moyen";
	public static String freezingRisk_3 = "Fort";
	public static String freezingRisk_4 = "Certain";
	public static String freezingRisk_9 = "En défaut";
	public static String salinity0 = "0 %";
	public static String salinity20 = "20 %";
	public static String salinity40 = "40 %";
	public static String salinity60 = "60 %";
	public static String salinity80 = "80 %";
	public static String salinity100 = "100 %";
	public static String precipitationType_0 = "Aucune";
	public static String precipitationType_1 = "Pluie";
	public static String precipitationType_2 = "Pluie-Neige";
	public static String precipitationType_3 = "Neige";
	public static String precipitationType_9 = "En défaut";
	public static String precipitationIntensity_0 = "Aucune précipitation";
	public static String precipitationIntensity_1 = "Très peu";
	public static String precipitationIntensity_2 = "Peu";
	public static String precipitationIntensity_3 = "Moyen";
	public static String precipitationIntensity_4 = "Fort";
	public static String precipitationIntensity_5 = "Très fort";
	public static String precipitationIntensity_9 = "En défaut";
	// RsuDiagnosticPlotPanel
	public static String rsuBatteryLevelLegend = "Niveau de batterie";
	public static String rsuTemperatureLegend = "Température";
	public static String rsuLoadLegend = "CPU";
	public static String rsuMemoryLegend = "RAM";
	// SvgWeatherTimer
	public static String display_surfaceState_0 = "Sec";
	public static String display_surfaceState_1 = "Humide";
	public static String display_surfaceState_2 = "Mouillé";
	public static String display_surfaceState_3 = "Ruisselant";
	public static String display_surfaceState_4 = "Givré";
	public static String display_surfaceState_5 = "Verglacé";
	public static String display_surfaceState_6 = "Humide salé";
	public static String display_surfaceState_7 = "Mouillé salé";
	public static String display_surfaceState_8 = "Enneigé";
	public static String display_surfaceState_9 = "En défaut";
	public static String display_freezingRisk_0 = "Aucun";
	public static String display_freezingRisk_1 = "Faible";
	public static String display_freezingRisk_2 = "Moyen";
	public static String display_freezingRisk_3 = "Fort";
	public static String display_freezingRisk_4 = "Certain";
	public static String display_freezingRisk_9 = "En défaut";
	public static String display_precipitationType_0 = "Aucune";
	public static String display_precipitationType_1 = "Pluie";
	public static String display_precipitationType_2 = "Pluie-Neige";
	public static String display_precipitationType_3 = "Neige";
	public static String display_precipitationType_9 = "En défaut";
	public static String display_precipitationIntensity_0 = "Aucune précipitation";
	public static String display_precipitationIntensity_1 = "Très peu";
	public static String display_precipitationIntensity_2 = "Peu";
	public static String display_precipitationIntensity_3 = "Moyen";
	public static String display_precipitationIntensity_4 = "Fort";
	public static String display_precipitationIntensity_5 = "Très fort";
	public static String display_precipitationIntensity_9 = "En défaut";
	// SelectExportPanel
	public static String plotLabel = "Graphique";
	public static String timestampString = "Horodate";
	public static String travelTimeHorizonString = "Horizon (min)";
	public static String travelTimeIndividualDataString = "Export des données individuelles";
	public static String travelTimePeriodString = "Période (min)";
	public static String travelTimeStatisticalDataString = "Export des statistiques";
	public static String travelTimeTimestampMiddleOfIntervalString = "centré";
	public static String travelTimeTimestampEndOfIntervalString = "sortant";
	// SelectDataSourcesPanel (diagnostic)
	public static String stationsLabel = "Station";
	// SelectItineraryPanel
	public static String itineraryString = "Itinéraire";
	//TravelTimeParametersPanel
	public static String travelTimeBluetoothMenu = "Temps de parcours";
	public static String individualTtExportButton = "Export TPI";
	public static String statisticalTtExportButton = "Export TPM";
	//TravelTimePlotsPanel
	public static String decile1090String = "10%-90%";
	public static String decile2080String = "20%-80%";
	public static String decile3070String = "30%-70%";
	public static String decile4060String = "40%-60%";
	public static String envBtPlotDefaultVar = "bt_plot_default";
	public static String filteredTravelTimesString = "TPI filtrés";
	public static String maxString = "Max";
	public static String medianTravelTimesString = "TP médians";
	public static String meanTravelTimesString = "TP moyens";
	public static String minString = "Min";
	public static String sampleNumberTitle = "Taille de l'echantillon";
	public static String standardDeviationsString = "Ecarts types";
	public static String tpTitle(String itinerary){return "Temps de parcours pour l'itinéraire "+itinerary;};
	public static String travelTimesMinTitle = "Temps de parcours (min)";
	public static String ttDecile10 = "tt_decile_10";
	public static String ttDecile20 = "tt_decile_20";
	public static String ttDecile30 = "tt_decile_30";
	public static String ttDecile40 = "tt_decile_40";
	public static String ttFiltered = "tt_filtered";
	public static String ttMean = "tt_mean";
	public static String ttMedian = "tt_median";
	public static String ttMin = "tt_min";
	public static String ttMax = "tt_max";
	public static String ttSamples = "tt_samples";
	public static String ttStd = "tt_std";
	public static String ttValids = "tt_valid";
	public static String validSampleSizeString = "Echantillons TPI valides";
	public static String validTravelTimesString = "TPI valides";
	// TravelTimesHistoryMenu
	public static String bluetoothString = "Bluetooth";
	public static String travelTimeMenu = "TP";
	// ExportServiceImpl
	public static String envGwtVar = "gwt";
	public static String originColumn = "origine";
	public static String destinationColumn = "destination";
	public static String timestampColumn = "horodate";
	public static String macColumn = "adresse mac anonymisée";
	public static String travelTimesSecColumn = "temps de parcours (s)";
	public static String classColumn = "classe équipement";
	public static String validColumn = "valide";
	public static String statisticalTtExportOriginColumn = "origine";
	public static String statisticalTtExportDestinationColumn = "destination";
	public static String statisticalTtExportTimestampColumn = "horodate";
	public static String statisticalTtExportMedianColumn = "temps de parcours médian (s)";
	public static String nominalTravelTimeColumn = "temps de parcours nominal (s)";
	public static String maxTravelTimeColumn = "temps de parcours max (s)";
	public static String levelOfServiceThreshold1Column = "1er seuil niveau de service (s)";
	public static String levelOfServiceThreshold2Column = "2ème seuil niveau de service (s)";
	public static String scaleColumn = "échelle (s)";
	public static String weatherExportTimestampColumn = "Horodate";
	public static String weatherExportStationColumn = "Station";
	public static String weatherExportAirTemperatureColumn = "Tair";
	public static String weatherExportDewTemperatureColumn = "Trosée";
	public static String weatherExportHumidityColumn = "Hygrométrie";
	public static String weatherExportRoadTemperatureColumn = "Tchaussée";
	public static String weatherExportBelowSurfTemperatureColumn = "T5cm";
	public static String weatherExportSurfaceStateColumn = "Etat_chaussée";
	public static String weatherExportSalinityColumn = "Concentration_sel";
	public static String weatherExportFreezingTemperatureColumn = "Tcongélation";
	public static String weatherExportPrecipitationTypeColumn = "Nature_pécipitations";
	public static String weatherExportPrecipitationIntensityColumn = "Intensité_pécipitations";
	public static String weatherExportFreezingRiskColumn = "Risque_verglas";
	public static String weatherExportLatencyColumn = "Latence";
	public static String lanesExportStationColumn = "Station";
	public static String lanesExportLaneColumn = "Voie";
	public static String lanesExportRoadColumn = "Route";
	public static String lanesExportWayColumn = "Sens";
	public static String lanesExportPositionColumn = "Position";
	// BtConfigurationMenu
	public static String btBoxesConfigString = "Balises";
	public static String sectionsConfigString = "Tronçons";
	// TtBtBoxEditableParametersPanel
	public static String commentString = "Commentaire";
	public static String enabledString = "En service";
	public static String installationDateString = "Date d'installation";
	public static String nameString = "Nom";
	public static String serialEmptyError = "Le numéro de série ne doit pas être vide.";
	public static String btBoxNameAlreadyUsedError(String name){ return "Une balise est déjà identifiée avec le nom '"+name+"'. Entrez un nouveau nom.";};
	public static String btBoxSerialAlreadyUsedError(String serial){ return "Une balise est déjà identifiée avec le numéro de série '"+serial+"'. Entrez un nouveau numéro de série.";};
	public static String btBoxSaved(String btBoxName){return "La balise '"+btBoxName+"' a été sauvegardée.";};
	public static String btBoxNotSaved(String btBoxName){return "La balise '"+btBoxName+"' n'a pas été sauvegardée.";};
	// btBoxesConfigurationPanel
	public static String removeTtBtBoxString = "Supprimer une balise";
	public static String btBoxesTitle = "Balises :";
	public static String editTtBtBoxString = "Editer la balise";
	public static String addNewTtBtBoxString = "Ajouter une nouvelle balise";
	// btBoxesTable
	public static String noSelectedTtBtBoxError = "Une balise doit être sélectionnée dans le tableau pour l'éditer ou la supprimer.";
	public static String btBoxRemoved(String btBoxName){return "La balise '"+btBoxName+"' a été supprimée.";};
	public static String btBoxNotRemoved(String btBoxName){return "La balise '"+btBoxName+"' n'a pas été supprimée.";};
	public static String confirmRemovingTtBtBox(String btBoxName){return "Confirmer la suppression de la balise '"+btBoxName+"' ?";};
	// ExportServiceImpl
	public static String btNameColumn = "nom";
	public static String serialColumn = "numéro de série";
	public static String installationDateColumn = "date d'installation";
	public static String enabledColumn = "en service";
	public static String aggregatesIdColumn = "id";
	public static String aggregatesLanesListColumn = "liste des voies";
	// ItinerariesTable
	public static String itineraryRemoved(String itineraryName){return "Le tronçon '"+itineraryName+"' a été supprimé.";};
	public static String itineraryNotRemoved(String itineraryName){return "Le tronçon '"+itineraryName+"' n'a pas été supprimé.";};
	public static String confirmRemovingItinerary(String itineraryName){return "Confirmer la suppression du tronçon '"+itineraryName+"' ?";};
	public static String itineraryConstraintViolationError(String origin, String destination) {return "Le couple origine : '" + origin + "', destination : '" + destination +  "' est déjà utilisé, vous devez en choisir un autre.";}
	public static String originString = "Origine";
	public static String noSelectedItineraryError = "Un tronçon doit être sélectionné dans le tableau pour l'éditer ou le supprimer.";
	public static String nominalTravelTimeString = "TP nominal (s)";
	public static String maxTravelTimeString = "TP max (s)";
	public static String levelOfServiceThreshold1String = "1er seuil niveau de service (s)";
	public static String levelOfServiceThreshold2String = "2ème seuil niveau de service (s)";
	public static String scaleString = "Echelle (s)";
	public static String destinationString = "Destination";
	// ItineraryEditableParametersPanel
	public static String itinerarySaved(String itineraryName){return "Le tronçon '"+itineraryName+"' a été sauvegardé.";};
	public static String itineraryNotSaved(String itineraryName){return "Le tronçon '"+itineraryName+"' n'a pas été sauvegardé.";};
	public static String itineraryNameAlreadyUsedError(String name){ return "Un tronçon est déjà identifié avec le nom '"+name+"'. Entrez un nouveau nom.";};
	public static String emptyError = " ne doit pas être vide.";
	// ItinerariesConfigurationPanel
	public static String removeItineraryString = "Supprimer un tronçon";
	public static String itinerariessTitle = "Tronçons :";
	public static String editItineraryString = "Editer le tronçon";
	public static String addNewItineraryString = "Ajouter un nouveau tronçon";
	// l2WebApp
	public static String alarmMenu = "Alarmes";
	public static String configMenu = "Configuration";
	public static String dataMenu = "Données";
	public static String systemMenu = "Système";
	// AggregatesConfigurationPanel
	public static String removeAggregateString = "Supprimer un aggrégat";
	public static String aggregatesTitle = "Aggrégats :";
	public static String editAggregateString = "Editer l'aggrégat";
	public static String addNewAggregateString = "Ajouter un nouvel aggrégat";
	// AggregateEditableParametersPanel
	public static String aggregateIdAlreadyUsedError(String id){ return "Un aggrégat est déjà identifié avec l'id '"+id+"'. Entrez un nouvel id.";};
	public static String aggregateNotSaved(String aggregateId){return "L'aggrégat '"+aggregateId+"' n'a pas été sauvegardé.";};
	public static String aggregateSaved(String aggregateId){return "L'aggrégat '"+aggregateId+"' a été sauvegardé.";};
	public static String aggregateId = "Id";
	public static String aggregateLanesList = "Listes des voies";
	public static String aggregateIdEmptyError = "L'id ne doit pas être vide.";
	// AggregatesConfigurationMenu
	public static String aggregatesLabel = "Aggrégats";
	// AggregatesTable
	public static String noSelectedAggregateError = "Un aggrégat doit être sélectionné dans le tableau pour l'éditer ou le supprimer.";
	public static String aggregateRemoved(String aggregateId){return "L'aggrégat '"+aggregateId+"' a été supprimé.";};
	public static String aggregateNotRemoved(String aggregateId){return "L'aggrégat '"+aggregateId+"' n'a pas été supprimé.";};
	public static String confirmRemovingAggregate(String aggregateId){return "Confirmer la suppression de l'aggrégat '"+aggregateId+"' ?";};
	public static String aggregateIdHeader = "Id";
	public static String aggregateLanesListHeader = "Listes des voies";
	// GenericDatabaseDriver
	public static String noLanesDefined(String accessPoint, String way) {return "Il n'y a pas de voies définies pour la station " + accessPoint + " et le sens " + way;}
	public static String systemEnvironmentDatabaseError(String envVariable) { return "Il n'y a pas d'enregistrement dans la table d'environnement pour le paramètre " + envVariable;}
	// GenericDatabaseServiceImpl
	public static String instance = "instance";
	// AlarmNotificationPanel
	public static String networkAlarmType = "RESEAU";
	public static String rdtAlimAlarmType = "RDT/ALIM";
	public static String rdtTransAlarmType = "RDT/TRANS";
	public static String rdtSyncAlarmType = "RDT/SYNC";
	public static String rdtSensorAlarmType = "RDT/CAPTEUR";
	public static String rdtCardAlarmType = "RDT/CARTE";
	public static String rdtLatencyAlarmType = "RDT/LATENCE";
	public static String rdtDataAlarmType = "RDT/DONNEES";
	public static String serverCpuAlarmType = "SERVEUR/CPU";
	public static String serverDiskAlarmType = "SERVEUR/DISQUE";
	public static String serverRamAlarmType = "SERVEUR/RAM";
	public static String serverProcessAlarmType = "SERVEUR/PROCESSUS";
	public static String serverTomcatAlarmType = "SERVEUR/TOMCAT";
	public static String serverPostgresqlAlarmType = "SERVEUR/POSTGRESQL";
	public static String serverNtpAlarmType = "SERVEUR/NTP";
	public static String serverSshAlarmType = "SERVEUR/SSH";
	public static String serverApacheAlarmType = "SERVEUR/APACHE";
	public static String serverMySqlAlarmType = "SERVEUR/MYSQL";
	public static String serverAlimAlarmType = "SERVEUR/ALIM";
	public static String serverRaid1AlarmType = "SERVEUR/RAID1";
	public static String serverRaid5AlarmType = "SERVEUR/RAID5";
	public static String newAlarmStatus = "Nouveau";
	public static String acknowledgedAlarmStatus = "Acquittée";
	public static String disappearedAlarmStatus = "Disparue";
	public static String solvedAlarmStatus = "Résolue";
	public static String alarmTypes= "alarm_types";
	// AlarmsStatusBar
	public static String unAcknowledgedAlarm = "Alarme(s) non\nacquittée(s)";
	// CurrentAlarmsMenu
	public static String currentAlarmsString = "Alarmes en cours";
	public static String currentAlarmsSubMenu = "En cours";
	// CurrentAlarmsPanel
	public static String reloadAlarmsString = "Rafraîchir";
	// AlarmsHistoryMenu
	public static String AlarmsHistorySubMenu = "Historique";
	// EnvironmentPanel
	public static String unknownString = "Inconnu";
	public static String principalString = "Principal";
	public static String secondaryString = "Secondaire";
	public static String serverString = "Serveur";
	public static String instanceString = "Instance";
	public static String applyString = "Appliquer";
	public static String reloadString = "Recharger";
	// ReferentialPanel
	public static String availableReferentials = "Référentiels disponibles :";
	public static String uploadString = "Charger";
	public static String uploadReferentialString = "Charger un nouveau référentiel : ";
	public static String saveCurrentReferentialString = "Sauvegarder le référentiel courant : ";
	public static String saveCurrentReferentialButtonName = "Sauvegarder";
	public static String currentReferentialSavedString = "Le référentiel courant a été sauvegardé";
	public static String uploadReferentialFailureMessage(String err){ return "Erreur lors du chargement du référentiel : "+err;};
	public static String uploadReferentialOkMessage(String name){ return "Le référentiel a été chargé sous le nom : '"+name+"'.";};
	public static String referentialFileNameExtensionError = "Il faut choisir un fichier avec l'extension .ref";
	// ReferentialTable
	public static String downloadReferentialString = "Télécharger";
	public static String executeReferentialString = "Exécuter";
	public static String deleteReferentialString = "Supprimer";
	public static String confirmExecuteReferentialQuestion(String ref){ return "Voulez-vous exécuter le référentiel '"+ref+"'? Il faudra ensuite recharger les pages des navigateurs";};
	public static String executeReferentialOkMessage(String ref){ return "L'exécution du référentiel '"+ref+"' a été effectuée. Pour prendre en compte les changements, recharger les pages des navigateurs.";};
	public static String executeReferentialFailureMessage(String ref){ return "Erreur lors de l'exécution du référentiel '"+ref+"'.";};
	public static String deleteReferentialOkMessage(String ref){ return "Le référentiel '"+ref+"' a bien été supprimé.";};
	public static String deleteReferentialFailureMessage(String ref){ return "Erreur lors de la suppression du référentiel '"+ref+"'.";};
	public static String confirmDeleteReferentialQuestion(String ref){ return "Voulez-vous supprimer le référentiel '"+ref+"'?";};
	public static String referentialString = "Référentiel";
	// AskAdminPasswordDialogBox
	public static String passwordNotCorrectError(int nbTries){return "Mot de passe incorrect (" + nbTries + ") :";};
	// SvgStation
	public static String erroDescription = "Description : ";
	// SvgAlarmsTimer (count)
	public static String openingTimestampPrefix = "Horodate de création";
	public static String sourcePrefix = "Source";
	public static String typePrefix = "Type";
	public static String separationLine = "________";
	// SvgAlarmsTimer (travel time)
	public static String ttOpeningTimestampPrefix = "Horodate de création";
	public static String ttSourcePrefix = "Source";
	public static String ttTypePrefix = "Type";
	public static String ttSeparationLine = "________";
	// OpcuaTagsConfigurationPanel
	public static String opcuaTagsConfigurationTitle = "Tags OPC-UA";
	public static String removeOpcuaTagString = "Supprimer un tag opcua";
	public static String editOpcuaTagString = "Editer le tag opcua";
	public static String addNewOpcuaTagString = "Ajouter un nouveau tag opcua";
	// OpcuaTagsTable
	public static String opcuaTagIdHeader = "Id";
	public static String opcuaTagTypeHeader = "Type";
	public static String opcuaTagValueHeader = "Valeur";
	public static String opcuaTagEnabledHeader = "Activé";
	public static String noSelectedOpcuaTagError = "Un tag opcua doit être sélectionné dans le tableau pour l'éditer ou le supprimer.";
	public static String confirmRemovingOpcuaTag(String opcuaTagId){return "Confirmer la suppression du tag opcua '"+opcuaTagId+"' ?";};
	public static String opcuaTagNotRemoved(String opcuaTagId){return "Le tag opcua '"+opcuaTagId+"' n'a pas été supprimé.";};
	public static String opcuaTagRemoved(String opcuaId){return "Le tag opcua '"+opcuaId+"' a été supprimé.";};
	// OpcuaTagsParametersPanel
	public static String opcuaTagIdEmptyError = "L'id ne doit pas être vide.";
	public static String opcuaTagTypeEmptyError = "Le type ne doit pas être vide.";
	public static String opcuaTagIdAlreadyUsedError(String id){ return "Un tag opcua est déjà identifié avec l'id '"+id+"'. Entrez un nouvel id.";};
	public static String opcuaTagNotSaved(String opcuaId){return "Le tag opcua '"+opcuaId+"' n'a pas été sauvegardé.";};
	public static String opcuaTagSaved(String opcuaId){return "Le tag opcua '"+opcuaId+"' a été sauvegardé.";};
	public static String opcuaTagId = "Id";
	public static String opcuaTagType = "Type";
	public static String opcuaTagValue = "Valeur";
	public static String opcuaTagEnabled = "Activé";
	// TrafficAlarmsConfigurationPanel
	public static String trafficAlarmsConfigurationTitle = "Alarmes trafic";
	public static String removeTrafficAlarmString = "Supprimer une alarme trafic";
	public static String editTrafficAlarmString = "Editer l'alarme trafic";
	public static String addNewTrafficAlarmString = "Ajouter une nouvelle alarme trafic";
	// TrafficAlarmsTable
	public static String trafficAlarmLabelHeader = "Label";
	public static String trafficAlarmSourceHeader = "Source";
	public static String trafficAlarmDataHeader = "Donnée";
	public static String trafficAlarmHorizonHeader = "Horizon";
	public static String trafficAlarmPeriodicityHeader = "Périodicité";
	public static String trafficAlarmThresholdHeader = "Seuil";
	public static String noSelectedTrafficAlarmError = "Une alarme trafic doit être sélectionnée dans le tableau pour l'éditer ou le supprimer.";
	public static String confirmRemovingTrafficAlarm(String trafficAlarmLabel){return "Confirmer la suppression de l'alarme trafic '"+trafficAlarmLabel+"' ?";};
	public static String trafficAlarmNotRemoved(String trafficAlarmLabel){return "L'alarme trafic '"+trafficAlarmLabel+"' n'a pas été supprimée.";};
	public static String trafficAlarmRemoved(String trafficAlarmLabel){return "L'alarme trafic '"+trafficAlarmLabel+"' a été supprimée.";};
	// TrafficAlarmsParametersPanel
	public static String trafficAlarmLabelEmptyError = "Le label ne doit pas être vide.";
	public static String trafficAlarmLabelAlreadyUsedError(String label){ return "Une alarme trafic est déjà identifiée avec le label '"+label+"'. Entrez un nouveau label.";};
	public static String trafficAlarmNotSaved(String trafficAlarmLabel){return "L'alarme trafic '"+trafficAlarmLabel+"' n'a pas été sauvegardée.";};
	public static String trafficAlarmSaved(String trafficAlarmLabel){return "L'alarme trafic '"+trafficAlarmLabel+"' a été sauvegardée.";};
	public static String trafficAlarmLabel = "Label";
	public static String trafficAlarmSource = "Source";
	public static String trafficAlarmData = "Donnée";
	public static String trafficAlarmHorizon = "Horizon";
	public static String trafficAlarmPeriodicity = "Périodicité";
	public static String trafficAlarmThreshold = "Seuil";
	// CredentialsConfigurationPanel
	public static String credentialsTitle = "Groupes :";
	public static String addNewCredentialString = "Ajouter un nouveau groupe";
	public static String editCredentialString = "Editer le groupe";
	public static String removeCredentialString = "Supprimer le groupe";
	// CredentialsTable
	public static String credentialGroupHeader = "Groupe";
	public static String credentialStationHeader = "Stations";
	public static String credentialSynopticHeader = "Synoptique";
	public static String credentialAlarmsHeader = "Alarmes";
	public static String credentialRdtCtDataHeader = "RDT/Données";
	public static String credentialRdtCtStationsHeader = "RDT/Stations";
	public static String credentialRdtCtAggregatesHeader = "RDT/Agrégats";
	public static String credentialRdtTtDataHeader = "TT/Données";
	public static String credentialRdtTtBoxesHeader = "TT/Balises";
	public static String credentialRdtTtItinerariesHeader = "TT/Tronçons";
	public static String credentialSystemHeader = "Système";
	public static String noSelectedCredentialError = "Un groupe doit être sélectionné dans le tableau pour l'éditer ou le supprimer.";
	public static String currentUserGroupModificationError = "Vous ne pouvez pas éditer le groupe de l'utilistauer courant";
	public static String currentUserGroupRemovalError = "Vous ne pouvez pas supprimer le groupe de l'utilistauer courant";
	public static String usedGroupRemovalError(String groupName) {return "Un ou plusieurs comptes utilisateur sont dans le groupe '" + groupName + "'."
																				  + " Supprimez ces comptes ou modifiez les avant de pouvoir le supprimer.";}
	public static String confirmRemovingCredential(String credentialName){return "Confirmer la suppression du groupe '"+credentialName+"' ?";};
	public static String credentialNotRemoved(String credentialName){return "Le groupe '"+credentialName+"' n'a pas été supprimé.";};
	public static String credentialRemoved(String credentialName){return "Le groupe '"+credentialName+"' a été supprimé.";};
	// CredentialEditableParametersPanel
	public static String allRights = "tout";
	public static String noRight = "rien";
	public static String visibleButNotModifiableRight = "visible non modifiable";
	public static String credentialGroup = "Groupe";
	public static String credentialStation = "Stations";
	public static String credentialSynoptic = "Synoptique";
	public static String credentialAlarms = "Alarmes";
	public static String credentialRdtCtData = "RDT/Données";
	public static String credentialRdtCtStations = "RDT/Stations";
	public static String credentialRdtCtAggregates = "RDT/Agrégats";
	public static String credentialRdtTtData = "TT/Données";
	public static String credentialRdtTtBoxes = "TT/Balises";
	public static String credentialRdtTtItineraries = "TT/Tronçons";
	public static String credentialSystem = "Système";
	public static String credentialNotSaved(String credentialName){return "Le groupe '"+credentialName+"' n'a pas été sauvegardé.";};
	public static String credentialSaved(String credentialName){return "Le groupe '"+credentialName+"' a été sauvegardé.";};
	public static String credentialGroupEmptyError = "Le groupe ne doit pas être vide.";
	public static String credentialConstraintViolationError(String credentialName) {return "Le nom de groupe '" + credentialName + "' est déjà utilisé, vous devez en choisir un autre.";}
	// LoginDialogBox
	public static String noCredentialDefinedForThisUserError(UsrUser user) {return "L'utilisateur '" + user.getLogin() + "' est dans le groupe '" + user.getCredential() + "', or ce dernier n'a pas été configuré";}
	public static String disconnectedString = "Déconnecté";
	// OpenedAlarmsGrid
	public static String openedAlarmTypeHeader = "Type";
	public static String openedAlarmSourceHeader = "Equipement";
	public static String openedAlarmStatusHeader = "Statut";
	public static String openedAlarmOpeningTimestampHeader = "Horodate de création";
	public static String openedAlarmAcknowledgementTimestampHeader = "Horodate d'acquittement";
	// Utils
	public static String sizeError = "La chaîne de caractères à formatter est plus longue que la taille de la sortie";
	// WeatherDataDashboardTimer
	public static String airTemperature = "Tair";
	public static String dewTemperature = "Trosée";
	public static String roadTemperature= "Tchaussée";
	public static String belowSurfTemperature = "T5cm";
	public static String freezingTemperature = "Tcongélation";
	// V2xAlarmsParametersPanel
	public static String displayV2xAlarmsButtonLabel = "Afficher";
	public static String v2xAlarmsExtractionLabel = "Extraction";
	public static String v2xAlarmsDateRangeLabel = "Plage horaire";
	// V2xAlarmsGrid
	public static String v2xAlarmsGridTimestampHeaderLabel = "Horodate";
	public static String v2xAlarmsGridStationHeaderLabel = "Station";
	public static String v2xAlarmsGridTypeHeaderLabel = "Type";
	public static String v2xAlarmsGridCodeHeaderLabel = "Code";
	public static String v2xAlarmsGridIdEventHeaderLabel = "Id-Evènement";
	public static String v2xAlarmsGridIdVehicleHeaderLabel = "Id-Vehicule";
	public static String v2xAlarmsGridGpsLatitudeHeaderLabel = "Latitude";
	public static String v2xAlarmsGridGpsLongitudeHeaderLabel = "Longitude";
	public static String v2xAlarmsGridStatusHeaderLabel = "Statut";
	public static String v2xAlarmsGridTextHeaderLabel = "Texte";
	// MySqlDriver
	public static String databaseCredentialsEnvironmentVariable = "db";
	// LanesConfigurationMenu
	public static String lanesConfigurationMenu = "Voies";
	public static String lanesConfigurationMenuTabLabel = "Voies";
	public static String lanesConfigurationMenuId = "lanes";
	// LanesConfigurationPanel
	public static String lanesConfigurationPanelExportButton = "Exporter";
	public static String lanesConfigurationPanelEditButton = "Editer la voie";
	public static String lanesConfigurationPanelAddButton = "Ajouter une nouvelle voie";
	public static String lanesConfigurationPanelRemoveButton = "Supprimer une voie";
	public static String lanesConfigurationPanelTitle = "Voies :";
	// LanesTable
	public static String noSelectedLaneError = "Une voie doit être sélectionnée dans le tableau pour l'éditer ou la supprimer.";
	public static String confirmRemovingLane(CtLane ctLane) {return "Confirmer la suppression de la voie (station : '" + ctLane.getId().getStation() + "', voie : '" + ctLane.getId().getLane() +  "') ?";}
	public static String laneRemoved(CtLane ctLane){return "La voie (station : '" + ctLane.getId().getStation() + "', voie : '" + ctLane.getId().getLane() +  "') a été supprimée.";};
	public static String laneNotRemoved(CtLane ctLane){return "La voie (station : '" + ctLane.getId().getStation() + "', voie : '" + ctLane.getId().getLane() +  "') n'a pas été supprimée.";};
	// LanesGrid
	public static String lanesGridStationHeader = "Station";
	public static String lanesGridLaneHeader = "Voie";
	public static String lanesGridRoadHeader = "Route";
	public static String lanesGridWayHeader = "Sens";
	public static String lanesGridPositionHeader = "Position";
	// LaneEditableParametersPanel
	public static String laneFieldStation = "Station";
	public static String laneFieldLane = "Voie";
	public static String laneFieldRoad = "Route";
	public static String laneFieldWay = "Sens";
	public static String laneFieldPosition = "Position";
	public static String laneEditionSaveButton = "Sauvegarder";
	public static String laneEditionCancelButton = "Annuler";
	public static String laneFieldStationEmptyError = "La station ne doit pas être vide.";
	public static String laneFieldLaneEmptyError = "La voie ne doit pas être vide.";
	public static String laneSaved(CtLane ctLane) {return "La voie (station : '" + ctLane.getId().getStation() + "', voie : '" + ctLane.getId().getLane() +  "') a été sauvegardée.";};
	public static String laneNotSaved(CtLane ctLane) {return "La voie (station : '" + ctLane.getId().getStation() + "', voie : '" + ctLane.getId().getLane() +  "') n'a pas été sauvegardée.";};
	// ConfigurationDatabaseDriver
	public static String laneConstraintViolationError(String station, String lane) {return "Le couple (station : '" + station + "', voie : '" + lane +  "') est déjà utilisé, vous devez en choisir un autre.";}
	// TimeSeriesPlot
	public static String autoScaleButton = "Ajuster l'échelle";
}

