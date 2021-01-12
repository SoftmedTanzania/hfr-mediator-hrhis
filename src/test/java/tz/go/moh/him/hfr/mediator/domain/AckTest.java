package tz.go.moh.him.hfr.mediator.domain;

import com.google.gson.Gson;
import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;
import tz.go.moh.him.mediator.core.exceptions.ArgumentNullException;

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

    /**
     * Tests the serialization of an Ack.
     */
    @Test
    public void testSerializeAck() {

        Ack ack = new Ack("123412341234", "Fail");

        Gson gson = new Gson();

        String actual = gson.toJson(ack);

        Assert.assertTrue(actual.contains("Fail"));
        Assert.assertTrue(actual.contains("123412341234"));
    }

    /**
     * Tests the exception when instantiating an Ack.
     */
    @Test(expected = ArgumentNullException.class)
    public void testSerializeAckExceptionStatus() {
        new Ack("123456789", null);
    }

    /**
     * Tests the exception when instantiating an Ack.
     */
    @Test(expected = ArgumentNullException.class)
    public void testSerializeAckExceptionTransactionIdNumber() {
        new Ack(null, "Success");
    }
}
