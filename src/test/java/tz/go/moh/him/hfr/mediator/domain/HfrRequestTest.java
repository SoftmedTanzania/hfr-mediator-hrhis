package tz.go.moh.him.hfr.mediator.domain;

import com.google.gson.Gson;
import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

/**
 * Contains tests for the {@link HfrRequest} class.
 */
public class HfrRequestTest {

    /**
     * Tests the deserialization of an HFR request.
     */
    @Test
    public void testDeserializeHfrRequest() {
        InputStream stream = HfrRequestTest.class.getClassLoader().getResourceAsStream("request.json");

        Assert.assertNotNull(stream);

        String data;

        try {
            data = IOUtils.toString(stream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Assert.assertNotNull(data);

        Gson gson = new Gson();

        HfrRequest hfrRequest = gson.fromJson(data, HfrRequest.class);

        Assert.assertEquals("105651-4", hfrRequest.getFacilityIdNumber());
        Assert.assertEquals("Muhimbili", hfrRequest.getName());
        Assert.assertEquals("-6.801501", hfrRequest.getLatitude());
        Assert.assertEquals("2017-10-27T05:54:48.000Z", hfrRequest.getUpdatedAt());

    }
}
