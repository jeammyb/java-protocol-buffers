package com.myapps.simpleapp;

import com.myapps.simpleapp.util.ProtobufUtils;
import org.apache.commons.io.FileUtils;
import org.glassfish.grizzly.http.server.HttpServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class PersonTest {

    private HttpServer server;
    private WebTarget target;
    private static final String BASE_URI = "http://0.0.0.0:8080/api/";

    @Before
    public void setUp() throws Exception {
        // start the server
        server = Main.startServer();
        // create the client
        Client c = ClientBuilder.newClient();
        target = c.target(BASE_URI);
    }

    @After
    public void tearDown() throws Exception {
        // shutdown server
        server.shutdown();
        // removing created data
        FileUtils.deleteDirectory(new File("../pb-test-data/"));
    }

    /**
     * Test to validate that the json data is sent in the response and if it is processed correctly
     * It is validated the response status
     */
    @Test
    public void testPerson() {
        ProtobufUtils.FILE_BASE = "../pb-test-data/";
        Map<String, String> data = new HashMap<>();
        data.put("name", "Test");
        data.put("id", "1");

        Response response = target.path("person")
                .request()
                .post(Entity.json(data));

        assertEquals(200, response.getStatus());
    }
}
