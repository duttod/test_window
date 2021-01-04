package karrus.client.login;

import karrus.shared.hibernate.UsrCredential;
import karrus.shared.hibernate.UsrUser;
import karrus.shared.language.Language;

/**
 * Saves the current user logged in.
 * 
 * @author Estelle DUMAS : estelle.dumas@karrus-its.com
 */
public class UserManagement {
	
	private static UsrUser currentUser;
	private static UsrCredential currentCredential;
	
	public static UsrUser getUser() {
		return currentUser;
	}
	
	public static void setUser(UsrUser user){
		currentUser = user;
	}
	
	public static UsrCredential getCredential() {
		return currentCredential;
	}
	
	public static void setCredential(UsrCredential credential){
		currentCredential = credential;
	}
	
	public static String getUserLogin(){
		return currentUser.getLogin();
	}
	
	public static String getUserPassword(){
		return currentUser.getPassword();
	}
	
	public static String getUserGroup(){
		return currentUser.getCredential();
	}
	
	public static boolean isOperator(){
		return (getUserGroup().equals(Language.userProfileOperator));		
	}
	
	public static boolean isAdmin(){
		return (getUserGroup().equals(Language.userProfileAdmin));		
	}
	
	public static boolean isGuest(){
		return (getUserGroup().equals(Language.userProfileGuest));		
	}
}
