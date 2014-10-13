package bca.websocket;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ejb.Stateless;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.websocket.EncodeException;
import javax.websocket.OnMessage;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import bca.log.Logged;
import bca.model.Contact;
import bca.model.ContactId;

@ServerEndpoint(
        value = "/import-contact",
        encoders = { ContactListEncoder.class }
)
@Stateless
@Logged
public class ImportContactEndpoint{
	
	private static final Set<Session> SESSIONS = Collections.synchronizedSet(new HashSet<Session>());

	@PersistenceContext(unitName="websocketbackend")
    private EntityManager em;
	
    @OnMessage 
    public void handleMessage(final Session session, String s) throws IOException, EncodeException, NamingException{
    	SESSIONS.add(session);

    	Collection<Contact> contacts = importCsv(s);	    	
    	
    	session.getBasicRemote().sendObject(contacts);
    }
    
	public Collection<Contact> importCsv(String fileContent) throws IOException {
		
		List<Contact> contacts = new ArrayList<>();
		
		try (Reader in = new StringReader(fileContent); CSVParser parser = new CSVParser(in, CSVFormat.DEFAULT)) {
		
			List<CSVRecord> records = parser.getRecords();
			
			for (CSVRecord record:records) {
				ContactId contactId = getContactId(record);
				Contact contact = em.find(Contact.class, contactId);
				if (contact == null) {
					contact = new Contact(contactId);
					em.persist(contact);
				} 
				contact.setField(record.get(2));
				em.merge(contact);
				contacts.add(contact);
			}

		}
		return contacts;
	}
	
	protected ContactId getContactId(CSVRecord record) {
		return new ContactId(record.get(0), record.get(1));
	}
    
}
