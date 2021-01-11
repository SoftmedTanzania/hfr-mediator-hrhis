package tz.go.moh.him.hfr.mediator.domain;

import com.google.gson.Gson;
import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

/**
 * Contains tests for the {@link Ack} class.
 */
public class AckTest {

    /**
     * Tests the deserialization of an Ack.
     */
    @Test
    public void testDeserializeAck() {
        InputStream stream = AckTest.class.getClassLoader().getResourceAsStream("success_response.json");

        Assert.assertNotNull(stream);

        String data;

        try {
            data = IOUtils.toString(stream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Assert.assertNotNull(data);

        Gson gson = new Gson();

        Ack ack = gson.fromJson(data, Ack.class);

        Assert.assertEquals("Success", ack.getStatus());
        Assert.assertEquals("123456789", ack.getTransactionIdNumber());
    }
}
