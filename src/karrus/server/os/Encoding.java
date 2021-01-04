package karrus.server.os;

/**
 * Tools.
 * 
 * @author Estelle DUMAS: estelle.dumas@karrus-its.com
 */
public class Encoding {

	public static final String fileFormat = "UTF-8";
	
	/**
	 * Encoding function: replaces simple quote by double quotes for sql needs
	 * @param string string
	 * @return String
	 */
	public static String sqlEncode(String string){
		return string.replace("'", "''");
	}
}
