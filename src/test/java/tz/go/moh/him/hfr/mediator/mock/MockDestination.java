package tz.go.moh.him.hfr.mediator.mock;

import com.google.gson.Gson;
import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.openhim.mediator.engine.messages.MediatorHTTPRequest;
import org.openhim.mediator.engine.testing.MockHTTPConnector;
import tz.go.moh.him.hfr.mediator.hrhis.domain.HfrRequest;
import tz.go.moh.him.hfr.mediator.hrhis.domain.HrhisMessage;
import tz.go.moh.him.hfr.mediator.hrhis.orchestrator.FacilityOrchestratorOrchestratorTest;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Map;

/**
 * Represents a mock destination.
 */
public class MockDestination extends MockHTTPConnector {

    /**
     * Initializes a new instance of the {@link MockDestination} class.
     */
    public MockDestination() {
    }

    /**
     * Gets the response.
     *
     * @return Returns the response.
     */
    @Override
    public String getResponse() {
        return null;
    }

    /**
     * Gets the status code.
     *
     * @return Returns the status code.
     */
    @Override
    public Integer getStatus() {
        return 200;
    }

    /**
     * Gets the HTTP headers.
     *
     * @return Returns the HTTP headers.
     */
    @Override
    public Map<String, String> getHeaders() {
        return Collections.emptyMap();
    }

    /**
     * Handles the message.
     *
     * @param msg The message.
     */
    @Override
    public void executeOnReceive(MediatorHTTPRequest msg) {

        InputStream stream = FacilityOrchestratorOrchestratorTest.class.getClassLoader().getResourceAsStream("hrhisrequest.json");

        Assert.assertNotNull(stream);

        Gson gson = new Gson();

        HrhisMessage expected;

        try {
            expected = gson.fromJson(IOUtils.toString(stream), HrhisMessage.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        HrhisMessage actual = gson.fromJson(msg.getBody(), HrhisMessage.class);

        Assert.assertNotNull(actual);
        Assert.assertNotNull(expected);

        Assert.assertEquals(expected.getName(), actual.getName());
        Assert.assertEquals(expected.getCode(), actual.getCode());
        Assert.assertEquals(expected.getShortName(), actual.getShortName());
        Assert.assertEquals(expected.getCoordinates(), actual.getCoordinates());
        Assert.assertEquals(expected.isActive(), actual.isActive());
    }
}
