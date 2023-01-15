/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.core.MediaType;
import org.json.JSONObject;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeoutException;
import javax.ws.rs.QueryParam;

@Path("submit")
public class Submit {

    private static enum EXCHANGE_TYPE {DIRECT, FANOUT, TOPIC, HEADERS};

    private final static String TOPIC_KEY_NAME = "*intent";
    
    @Context
    private UriInfo context;

    public Submit() {
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getJson(@QueryParam("x")String userID, @QueryParam("y")String location, @QueryParam("z")String date,@QueryParam("w")String weatherData) throws MalformedURLException, IOException, TimeoutException {
        
        //URL web = new URL("http://localhost:8080/WebApp/webresources/randomID");
        //HttpURLConnection accept = (HttpURLConnection) web.openConnection();
        //accept.connect();
        //BufferedReader readWeb = new BufferedReader(new InputStreamReader(accept.getInputStream()));
        //String messageID = readWeb.readLine();
        //accept.disconnect();
        String messageID = "08274362";
        JSONObject trip = new JSONObject();
        trip.put("messageID", messageID);
        trip.put("userID" , userID);
        trip.put("location", location);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date today = new Date();
        trip.put("date of submission", dateFormat.format(today));
        trip.put("date of trip", date);
        trip.put("weather", weatherData);
        
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("152.71.155.95");
        factory.setUsername("student");
        factory.setPassword("COMP30231");
        
        try (Connection connection = factory.newConnection();
        Channel channel = connection.createChannel()) {
            channel.exchangeDeclare("tripsahoy", EXCHANGE_TYPE.FANOUT.toString().toLowerCase());
            String message = trip.toString();
            channel.basicPublish("tripsahoy", 
                    TOPIC_KEY_NAME,
                    new AMQP.BasicProperties.Builder()
                        .contentType("text/plain")
                        .deliveryMode(2)
                        .priority(1)
                        .userId("student")
                        .build(),
                    message.getBytes(StandardCharsets.UTF_8));
            System.out.println(" "+userID+" Sent a trip intent'" + TOPIC_KEY_NAME + ":" + trip.toString() + "'");
        
        return "Posted "+trip.toString()+" to rabbitMQ";
        }
    }    
        
    /**
     * PUT method for updating or creating an instance of Submit
     * @param content representation for the resource
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public void putJson(String content) {
    }
}
