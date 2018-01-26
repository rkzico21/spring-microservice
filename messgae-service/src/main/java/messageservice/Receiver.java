package messageservice;

import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;

import messageservice.Dto.MailMessage;
import messageservice.Services.MailService;
import messageservice.Services.DummyMailService;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.beans.factory.annotation.Autowired;

@Component
public class Receiver implements MessageListener {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	
	@Autowired
	private MailService mailService;
	
    @Override
    public void onMessage(Message message) {

       byte[] bytes = message.getBody();
       ObjectMapper mapper = new ObjectMapper();
       logger.debug(String.format("Received message: %s", message.toString()));
       try {
    	   
    	   MailMessage mailMessage = mapper.readValue(bytes , MailMessage.class);
    	   this.mailService.sendEmail(mailMessage);
    	} catch (IOException e) {
		// TODO Auto-generated catch block
    		 //e.printStackTrace();
    		logger.error("Error sending mail", e);
    	       
    	}
    }
    
    
}