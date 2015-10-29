package spark.model.manager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;

import spark.Constant;
import spark.exception.UploadException;
import spark.model.bean.Notification;

public class ManageResource extends Manager {
	
	public void upload(HttpServletRequest request) throws UploadException {
		try {
			Part filePart = request.getPart("file");
			String fileName = filePart.getSubmittedFileName();
			InputStream inputStream = filePart.getInputStream();
			
			save(fileName, inputStream);
			
			notifications.add(new Notification("success", "Upload "+ fileName +" is successul."));
		}
		catch(ServletException | IOException exception) {
			notifications.add(new Notification("alert", "Upload to encountered a problem."));
			throw new UploadException();
		}
	}
	
	private File save(String name, InputStream inputStream) throws IOException {
		OutputStream outputStream = null;
		
		try {
			File file = new File(Constant.STORAGE_TEMPORARY_FOLDER + name);
			outputStream = new FileOutputStream(file);
			
			int read = 0;
			byte[] bytes = new byte[1024];
			
			while((read = inputStream.read(bytes)) != -1) {
				outputStream.write(bytes, 0, read);
			}
			
			return file;
		}
		finally {
			if (inputStream != null)
				inputStream.close();

			if (outputStream != null)
				outputStream.close();
		}
	}
	
}