/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.TimeoutException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.QueryParam;

@Path("queryMessage")
public class QueryMessage {

    private static enum EXCHANGE_TYPE {DIRECT, FANOUT, TOPIC, HEADERS};
    
    // Notice how the queue and exchange names are defined as constants for easy reuse
    private final static String EXCHANGE_NAME = "tripsahoy";
    private final static String QUEUE_NAME = "tripsahoy";
    
    // Set this for topic or direct exchanges. Leave empty for fanout.
    private final static String TOPIC_KEY_NAME = "*intent"; // For direct use full name. For topic use * to match one word or # to match multiple: *.blue, red.#, etc.
    
    
    @Context
    private UriInfo context;

    public QueryMessage() {
    }
    
    @GET
    @Produces(MediaType.TEXT_HTML)
    public String getHtml(@QueryParam("x")Integer topic) throws FileNotFoundException, IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("152.71.155.95");
        factory.setUsername("student");
        factory.setPassword("COMP30231");
        
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, EXCHANGE_TYPE.FANOUT.toString().toLowerCase());
        channel.queueDeclare(QUEUE_NAME, true, false, false, null);
        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, TOPIC_KEY_NAME);
        
        System.out.println(" [*] Waiting for " + TOPIC_KEY_NAME +  " messages. To exit press CTRL+C");
        
        // This code block indicates a callback which is like an event triggered ONLY when a message is received
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println(" [x] Received '" + message + "'");
            reMessage = message;
        };

        // Consume messages from the queue by using the callback
        channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> { });
        deliverCallback.toString();
        return reMessage;
               //deliverCallback.toString();
    }
    
    private String reMessage;

    /**
     * PUT method for updating or creating an instance of QueryMessage
     * @param content representation for the resource
     */
    @PUT
    @Consumes(MediaType.TEXT_HTML)
    public void putHtml(String content) {
    }
}
