package messageservice.Services;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

import messageservice.Dto.MailMessage;

public class DummyMailService implements MailService {
	
	@Override
	public void sendEmail(MailMessage message) {
    	for(String recipient : message.getParticipants()) {
    		deliverMail( message.getId(),  message.getSubject(),  message.getBody(),  recipient);
    	}
    }
    
    private void deliverMail(String id, String subject, String body, String recipient) {
    	FileOutputStream fop = null;
		File file;
		
		String content = String.format("To:%s", recipient );
		content = String.format("%sSubject:%s", content, subject );
		content = String.format("%s, %s", content, body);

		try {

			String fileName = String.format("D:/Message/%s.txt", UUID.randomUUID());
			file = new File(fileName);
			fop = new FileOutputStream(file);

			if (!file.exists()) {
				file.createNewFile();
			}

			byte[] contentInBytes = content.getBytes();

			fop.write(contentInBytes);
			fop.flush();
			fop.close();

		
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (fop != null) {
					fop.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}


