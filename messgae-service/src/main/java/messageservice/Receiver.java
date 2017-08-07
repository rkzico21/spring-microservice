package messageservice;

import org.springframework.stereotype.Component;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;

@Component
public class Receiver implements MessageListener {

    @Override
    public void onMessage(Message message) {
        System.out.println("Received <" + new String(message.getBody()) + ">");
    }
}