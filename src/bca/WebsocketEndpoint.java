package bca;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.websocket.EncodeException;
import javax.websocket.OnMessage;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint(
        value = "/websocket",  
        decoders = { MessageEncoder.class }, 
        encoders = { MessageEncoder.class } 
)
public class WebsocketEndpoint {
	
	private static final Set<Session> sessions = Collections.synchronizedSet(new HashSet<Session>());
	
    @OnMessage 
    public void handleMessage(final Session session, Contact bean) throws IOException, EncodeException{
    	sessions.add(session);
    	
    	System.out.println("Message :"+bean.getFirstName()+" "+bean.getLastName());
    	
    	try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    	
    	session.getBasicRemote().sendObject(bean);
    	try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    	
    	System.out.println("End");
  	
    }
}
