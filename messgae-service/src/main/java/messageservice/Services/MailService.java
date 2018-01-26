package messageservice.Services;

import messageservice.Dto.MailMessage;

public interface MailService {
	void sendEmail(MailMessage message);
}


