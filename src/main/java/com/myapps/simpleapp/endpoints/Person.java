package com.myapps.simpleapp.endpoints;

import com.google.protobuf.InvalidProtocolBufferException;
import com.myapps.proto.simpleapp.Person.PersonMessage;
import com.myapps.simpleapp.util.ProtobufUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.json.JsonObject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.io.IOException;

/**
 * Root person exposed at "person" path
 */
@Path("person")
public class Person {

    private static final Logger LOGGER = LoggerFactory.getLogger(Person.class);
    private static final String FILE_NAME = "person_messages_yyyy_mm_dd.bin";

    /**
     * Method handling HTTP POST requests.
     * Receives a JSON object as body with format {“name:” “<name>”, “id”: <number>}
     * Saves the data into files using google protobuf
     * Protocol Buffer messages has the format {"name", "id"}
     * The files are rolled over every 24 hours at midnight
     *
     * @return Response object
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response create(JsonObject json) {
        try {
            PersonMessage message = ProtobufUtils.parseJSON(json);
            ProtobufUtils.writeProtoBuf(message, FILE_NAME);
        } catch (InvalidProtocolBufferException e) {
            LOGGER.error(e.getMessage());
            return Response.status(Status.BAD_REQUEST).
                    entity("Bad Request").build();
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
            return Response.status(Status.INTERNAL_SERVER_ERROR)
                    .entity("Internal Server Error").build();
        }
        LOGGER.info("Returning status {}", Status.OK);
        return Response.status(Status.OK).entity("Created!").build();
    }
}
