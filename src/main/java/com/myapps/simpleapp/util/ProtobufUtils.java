package com.myapps.simpleapp.util;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.util.JsonFormat;
import com.myapps.proto.simpleapp.Person.PersonMessage;
import org.eclipse.jetty.util.RolloverFileOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.json.JsonObject;
import java.io.File;
import java.io.IOException;

/**
 * Protobuf Utility class, contained methods for handling Protocol buffers.
 */

public class ProtobufUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProtobufUtils.class);
    public static String FILE_BASE = "../pb-data/";

    /**
     * This method parse a JSON object into a PersonMessage.
     *
     * @param json to be parsed, it must have the format {“name:” “<name>”, “id”: <number>}
     *
     * @return PersonMessage it has the format {"name", "id"}
     * @throws InvalidProtocolBufferException
     */
    public static PersonMessage parseJSON(JsonObject json) throws InvalidProtocolBufferException {
        // crating builder
        PersonMessage.Builder builder = PersonMessage.newBuilder();

        // parse JSON into Protobuf
        JsonFormat.parser()
                .merge(json.toString(), builder);

        // build message
        PersonMessage message = builder.build();
        LOGGER.info("Message content:\n name: {} id: {}", message.getName(), message.getId());

        return message;
    }

    /**
     * This method saves the data into files.
     * The files are rolled over every 24 hours at midnight.
     * The filename must include the string "yyyy_mm_dd" to be rolled over.
     *
     * @param message to be written to disk
     * @param fileName to store the data
     * @throws IOException
     */
    public static void writeProtoBuf(PersonMessage message, String fileName) throws IOException {
        // write message
        createDir(FILE_BASE);
        RolloverFileOutputStream rolloverFileOutputStream =
                new RolloverFileOutputStream(FILE_BASE + fileName, true);

        message.writeDelimitedTo(rolloverFileOutputStream);
        rolloverFileOutputStream.close();
    }

    /**
     * Creates a file directory if it does not exist yet.
     */
    public static void createDir(String path) {
        File file = new File(path);

        if (!file.exists()) {
            file.mkdirs();
        }
    }
}
