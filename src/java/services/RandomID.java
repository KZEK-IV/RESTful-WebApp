/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.core.MediaType;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

@Path("randomID")
public class RandomID {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of RandomID
     */
    public RandomID() {
    }

    /**
     * Retrieves representation of an instance of services.RandomID
     * @return an instance of java.lang.String
     * @throws java.net.MalformedURLException
     * @throws java.io.IOException
     */
    @GET
    @Produces(MediaType.TEXT_HTML)
    public String getHtml() throws MalformedURLException, IOException {
        //TODO return proper representation object
        URL web = new URL("https://www.random.org/integers/?num=8&min=1&max=9&col=8&base=10&format=plain&rnd=new");
        HttpURLConnection accept = (HttpURLConnection) web.openConnection();
        accept.connect();
        BufferedReader readWeb = new BufferedReader(new InputStreamReader(accept.getInputStream()));
        String newID = readWeb.readLine();
        return newID.replaceAll("\\s+","");
    }

    /**
     * PUT method for updating or creating an instance of RandomID
     * @param content representation for the resource
     */
    @PUT
    @Consumes(MediaType.TEXT_HTML)
    public void putHtml(String content) {
    }
}
