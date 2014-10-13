package bca.websocket;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.ejb.Stateless;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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
@Stateless
@Logged
public class PushContactEndpoint{
	
	private static final Set<Session> SESSIONS = Collections.synchronizedSet(new HashSet<Session>());
	
	@PersistenceContext(unitName="websocketbackend")	
	EntityManager em;
	
	
    @OnMessage 
    public void handleMessage(final Session session, Contact bean) throws IOException, EncodeException, NamingException, InterruptedException{
    	SESSIONS.add(session);
    	
    	em.merge(bean);
    	
    	session.getBasicRemote().sendObject(bean);

  	
    }
}
