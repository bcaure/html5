package bca.websocket;



import java.util.Collection;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

import bca.log.Logged;
import bca.model.Contact;

import com.google.gson.Gson;

@Logged
public class ContactListEncoder implements Encoder.Text<Collection<Contact>> {

	@Override
	public void destroy() {
		
	}

	@Override
	public void init(EndpointConfig arg0) {
		
	}

	@Override
	public String encode(Collection<Contact> o) throws EncodeException {
    	return new Gson().toJson(o);
	}

}
