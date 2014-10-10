package bca.websocket;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.websocket.EncodeException;
import javax.websocket.OnMessage;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import bca.log.Logged;
import bca.model.Contact;

@ServerEndpoint(
        value = "/push-contact",  
        decoders = { ContactEncoder.class }, 
        encoders = { ContactEncoder.class } 
)
@Transactional
@Logged
public class PushContactEndpoint{
	
	private static final Logger LOGGER = Logger.getLogger(PushContactEndpoint.class.getName());
	private static final Set<Session> SESSIONS = Collections.synchronizedSet(new HashSet<Session>());
	
	
	EntityManager em;
	
	
    @OnMessage 
    public void handleMessage(final Session session, Contact bean) throws IOException, EncodeException, NamingException, InterruptedException{
    	SESSIONS.add(session);
    	
    	LOGGER.info("Message :"+bean.getId().getFirstName()+" "+bean.getId().getLastName());
    	em.merge(bean);
	    	
		Thread.sleep(1000);
    	
    	session.getBasicRemote().sendObject(bean);
    	
    	Thread.sleep(1000);
    	
    	LOGGER.info("End");
  	
    }
}
