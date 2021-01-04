package karrus.server.service.referential;

import gwtupload.server.UploadAction;
import gwtupload.server.exceptions.UploadActionException;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;

import karrus.server.os.Environment;
import karrus.server.os.FileTools;

import org.apache.commons.fileupload.FileItem;
import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

public class UploadReferentialServlet extends UploadAction {

	  private static final long serialVersionUID = 1L;
	  private static Logger logger1 = Logger.getLogger(UploadReferentialServlet.class);
	  private Hashtable<String, String> receivedContentTypes = new Hashtable<String, String>();
	  //Maintain a list with received files and their content types. 
	  private Hashtable<String, File> receivedFiles = new Hashtable<String, File>();

	  /**
	   * Override executeAction to save the received files in a custom place
	   * and delete this items from session.  
	   */
	  @Override
	  public String executeAction(HttpServletRequest request, List<FileItem> sessionFiles) throws UploadActionException {
	    String response = "";
	    for (FileItem item : sessionFiles) {
			logger1.debug(item.getName());
			String ref = "";
			try {
				/// Create a new file based on the remote file name in the client
				ref = Environment.getReferentialFile();
				String d = new SimpleDateFormat("yyyy-MM-dd_HH-mm").format(new Date());
				ref = ref.substring(0, ref.indexOf('.'))+"_"+d+".ref";
				logger1.info("creation de "+ref);	
				File file = new File(ref);//Environment.getPropertiesDirectory()+"referentiel.xml.uploaded"); 
				item.write(file);
				/// Save a list with the received files
				receivedFiles.put(item.getFieldName(), file);
				receivedContentTypes.put(item.getFieldName(), item.getContentType());
				/// Send a customized message to the client.
				response += "File saved as " + file.getAbsolutePath().replace("\\", "/");
				logger1.info(response);
				logger1.debug(response);
			} catch (ParserConfigurationException e) {
				logger1.info("error 1");
				e.printStackTrace();
				response += "ERROR : "+e.getMessage();
//				throw new UploadActionException(e.getMessage());
			} catch (SAXException e) {
				logger1.info("error 2");
				e.printStackTrace();
				response += "ERROR : "+e.getMessage();
//				throw new UploadActionException(e.getMessage());
			} catch (Exception e) {
				logger1.error("error dans executeAction");
				logger1.error(e.getMessage());
				response += "ERROR : "+e.getMessage();
				try {
					if (new File(ref).exists())
						FileTools.delete(ref);
				} catch (IOException e1) {
					logger1.error(e1.getMessage());
				}
//				throw new UploadActionException(e);
			}
		}    
	    /// Remove files from session because we have a copy of them
	    removeSessionFileItems(request);
	    
	    /// Send your customized message to the client.
	    return response;
	  }
	  
	  /**
	   * Get the content of an uploaded file.
	   */
	  @Override
	  public void getUploadedFile(HttpServletRequest request, HttpServletResponse response) throws IOException {
//	    String fieldName = request.getParameter(UConsts.PARAM_SHOW);
//	    File f = receivedFiles.get(fieldName);
//	    if (f != null) {
//	      response.setContentType(receivedContentTypes.get(fieldName));
//	      FileInputStream is = new FileInputStream(f);
//	      copyFromInputStreamToOutputStream(is, response.getOutputStream());
//	    } else {
//	      renderXmlResponse(request, response, XML_ERROR_ITEM_NOT_FOUND);
//	   }
	  }
	  
	  /**
	   * Remove a file when the user sends a delete request.
	   */
	  @Override
	  public void removeItem(HttpServletRequest request, String fieldName)  throws UploadActionException {
//	    File file = receivedFiles.get(fieldName);
//	    receivedFiles.remove(fieldName);
//	    receivedContentTypes.remove(fieldName);
//	    if (file != null) {
//	      file.delete();
//	    }
	  }
}