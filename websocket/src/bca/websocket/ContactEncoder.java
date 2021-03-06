package bca.websocket;



import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

import bca.log.Logged;
import bca.model.Contact;

import com.google.gson.Gson;

@Logged
public class ContactEncoder implements Encoder.Text<Contact>, Decoder.Text<Contact> {

    @Override
    public String encode(Contact o) throws EncodeException {
    	return new Gson().toJson(o);
    }

	@Override
	public void destroy() {
		
	}

	@Override
	public void init(EndpointConfig arg0) {
		
	}

	@Override
	public Contact decode(String s) throws DecodeException {
		return new Gson().fromJson(s, Contact.class);
	}

	@Override
	public boolean willDecode(String arg0) {
		return true;
	}

}
