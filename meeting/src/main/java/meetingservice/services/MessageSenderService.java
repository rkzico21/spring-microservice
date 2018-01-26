package meetingservice.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageSenderService {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	RabbitTemplate rabbitTemplate;
	
	
	public void SendMessage(String routingKey , Object object) {
		
		try {
			rabbitTemplate.convertAndSend(routingKey, object);
		}
		catch (Exception ex) {
			
			logger.error("Error", ex);
		}
		
	}
}