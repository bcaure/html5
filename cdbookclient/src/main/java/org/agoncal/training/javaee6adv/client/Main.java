package org.agoncal.training.javaee6adv.client;

import java.net.URI;

import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class Main {

	public static void main(String[] args) {
		URI location = null;
		{
			Client client = ClientBuilder.newClient();
			JsonObject json = Json.createObjectBuilder().add("title", "toto").add("price", 12).add("description", "titi").add("imageURL", "http://google.com").add("isbn","4454454545544").build();
			WebTarget target = client.target("http://localhost:8080/cdbookstore").path("rest").path("books");
			Response response = target.request(MediaType.APPLICATION_JSON).post(Entity.json(json.toString()));
			System.out.println("status : " + response.getStatus());
			location = response.getLocation();
			System.out.println(location);
			client.close();
		}
		{
			Client client = ClientBuilder.newClient();
			WebTarget target = client.target(location);
			Response response = target.request(MediaType.APPLICATION_JSON).get();
			System.out.println("status : " + response.getStatus());
			if (response.getStatus() == 200) {
//				System.out.println(response.);
			}
			client.close();
		}

	}
}