package hello;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;


@Controller
@Service
public class GreetingController {

	private SimpMessageSendingOperations messaging;
	
	@Autowired
	public GreetingController( SimpMessageSendingOperations messaging){
		this.messaging=messaging;
	}

    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public Greeting greeting(HelloMessage message) throws Exception {
        Thread.sleep(100); // simulated delay
        System.out.println("HelloMessage message "+ message.getName());
        return new Greeting("Hello, " + message.getName() + "!");
    }

    @SubscribeMapping("/helloagain")
    @SendTo("/topic/greetings")
    public Greeting greetingAgain(HelloMessage message) throws Exception {
//        Thread.sleep(100); // simulated delay

//        greetingAuto(message);
//        Thread.sleep(1000); // simulated delay
        broadcastMess(message);
        Thread.sleep(1000); // simulated delay
        System.out.println("HelloMessage again message "+ message.getName());
        return new Greeting("Hello again, " + message.getName() + "!");
    }


    @SendTo("/topic/greetings")
    public Greeting greetingAuto(HelloMessage message) throws Exception {
        Thread.sleep(1000); // simulated delay
        message.setName(message.getName()+" Auto!");
        System.out.println("Auto HelloMessage message "+ message.getName());
        return new Greeting("Auto Hello, " + message.getName() + "!");
    }
    // greetingAuto nie wysyła do /topic/greetings a jedynie zmienia message
    
    public void broadcastMess(HelloMessage message) throws Exception {

        System.out.println("broadcastMess "+ message.getName());
    	messaging.convertAndSend("/topic/greetings", new Greeting("broadcastMess " + message.getName() + "!"));
    	// javascript musi odbierać tylko Greeting
    }

}
