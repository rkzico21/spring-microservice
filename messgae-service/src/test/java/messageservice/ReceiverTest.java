package messageservice;

import static org.mockito.Mockito.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.amqp.core.Message;

import messageservice.Dto.MailMessage;
import messageservice.Services.MailService;


public class ReceiverTest {
	
	@InjectMocks
	Receiver receiver;
	
	@Mock
	MailService mailService;
	 
	
	
	@Before
	public void setUp() {
    
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
    public void onMessageTest()  {
		Message message = mock(Message.class);
		String messageJson = "{\"id\": \"1\", \"subject\":\"Subject\",  \"body\": \"message body\"}";
		byte[] bytes = messageJson.getBytes();
		
		when(message.getBody()).thenReturn(bytes);
		receiver.onMessage(message);
		verify(mailService, times(1)).sendEmail(any(MailMessage.class));;
	} 
	
	
	@Test
    public void onMessageTestWithInvalidData()  {
		Message message = mock(Message.class);
		String messageJson = "invalid json";
		byte[] bytes = messageJson.getBytes();
		
		when(message.getBody()).thenReturn(bytes);
		receiver.onMessage(message);
		verify(mailService, times(0)).sendEmail(any(MailMessage.class));;
	}
}