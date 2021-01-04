package karrus.server.service;

import java.io.FileInputStream;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import karrus.server.os.Environment;
import karrus.server.os.Encoding;

public class ExportCountDataTableServlet extends HttpServlet{
	
	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(ExportCountDataTableServlet.class);

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String uri = request.getRequestURI();
		int index = uri.lastIndexOf('/');
		String fileName;
		try {
			fileName = Environment.getTempDirectory()+uri.substring(index+1);
			response.setContentType("text/csv;charset=" + Encoding.fileFormat);
			response.setHeader("Content-Disposition","attachment;filename" + fileName);
			ServletOutputStream out = response.getOutputStream();
			FileInputStream fIn = new FileInputStream(fileName);
			byte[] buffer = new byte[4096]; 
			int length;
			while ((length = fIn.read(buffer)) > 0){
				out.write(buffer, 0, length);
			}
			fIn.close();
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());		
		}
	}
}
