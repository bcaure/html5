package bca.websocket;
import java.io.IOException;
import java.io.StringReader;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.Stateless;
import javax.inject.Named;
import javax.json.Json;
import javax.json.stream.JsonParser;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.websocket.EncodeException;
import javax.websocket.OnMessage;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import bca.log.Logged;
import bca.model.Contact;

@Named
@ServerEndpoint(
	value = "/load-contact"
	,encoders = { ContactListEncoder.class } 
)
@Stateless
@Logged
public class LoadContactEndpoint {
	
	private static final Set<Session> SESSIONS = Collections.synchronizedSet(new HashSet<Session>());
	
	@PersistenceContext(unitName="websocketbackend")	
	EntityManager em;
	
    @SuppressWarnings("unchecked")
	@OnMessage 
    public void handleMessage(final Session session, String data) throws IOException, EncodeException, NamingException{
    	
    	List<Contact> resultList = null;
    	
    	SESSIONS.add(session);
    	
    	JsonParser parser = Json.createParser(new StringReader(data));
    	Map<String, Integer> params =  new HashMap<>();
    	String key = null;
    	while (parser.hasNext()) {
    	   JsonParser.Event event = parser.next();
    	   switch(event) {
    	      case KEY_NAME: key = parser.getString(); break;
    	      case VALUE_NUMBER: params.put(key, parser.getInt()); break;
    	      default:
    	   }
    	}
    	
    	
    	// Init DB
//    	loadDatabaseSingleton.invokeMe();
    	
    	Query query = em.createNamedQuery("Contact.load");
    	if (params.containsKey("start")) query.setFirstResult(params.get("start"));
    	if (params.containsKey("max")) query.setMaxResults(params.get("max"));

		resultList = query.getResultList();
	    	
    	
    	session.getBasicRemote().sendObject(resultList);

  	
    }
    
}
