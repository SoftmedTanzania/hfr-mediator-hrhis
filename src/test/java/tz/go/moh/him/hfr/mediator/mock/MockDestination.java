package tz.go.moh.him.hfr.mediator.mock;

import com.google.gson.Gson;
import org.openhim.mediator.engine.messages.MediatorHTTPRequest;
import org.openhim.mediator.engine.testing.MockHTTPConnector;
import tz.go.moh.him.hfr.mediator.domain.HfrRequest;

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

    @Override
    public void executeOnReceive(MediatorHTTPRequest msg) {
        System.out.println(msg.getBody());

        Gson gson = new Gson();

        HfrRequest request = gson.fromJson(msg.getBody(), HfrRequest.class);

//        assertEquals(expectedPayload.getPartNum(), dailyStockStatus.getPartNum());
//        assertEquals(expectedPayload.getDate(), dailyStockStatus.getDate());
//        assertEquals(expectedPayload.getMonthOfStock(), dailyStockStatus.getMonthOfStock());
//        assertEquals(expectedPayload.getOnHandQty(), dailyStockStatus.getOnHandQty());
//        assertEquals(expectedPayload.getOum(), dailyStockStatus.getOum());
//        assertEquals(expectedPayload.getPartDescription(), dailyStockStatus.getPartDescription());
//        assertEquals(expectedPayload.getPlant(), dailyStockStatus.getPlant());
    }
}
