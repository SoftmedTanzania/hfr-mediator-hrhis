package tz.go.moh.him.hfr.mediator.mock;

import org.apache.commons.io.IOUtils;
import org.json.JSONObject;
import org.junit.Assert;
import org.openhim.mediator.engine.MediatorConfig;
import org.openhim.mediator.engine.connectors.CoreAPIConnector;
import org.openhim.mediator.engine.messages.MediatorHTTPRequest;
import org.openhim.mediator.engine.messages.MediatorHTTPResponse;
import tz.go.moh.him.hfr.mediator.hrhis.orchestrator.BaseOrchestratorTest;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents a mock OpenHIM.
 */
public class MockOpenHIM extends CoreAPIConnector {

    /**
     * Represents a sample OpenHIM transaction.
     */
    private static String openHIMSampleTransaction;

    /**
     * Initializes a new instance of the {@link MockOpenHIM} class.
     */
    public MockOpenHIM() {
        this(BaseOrchestratorTest.loadConfig(null));
    }

    /**
     * Initializes a new instance of the {@link MockOpenHIM} class.
     *
     * @param config The configuration.
     */
    public MockOpenHIM(MediatorConfig config) {
        super(config);

        try {

            InputStream stream = MockOpenHIM.class.getClassLoader().getResourceAsStream("openhim_transaction.json");

            if (stream == null) {
                throw new RuntimeException("File not found: openhim_transaction.json");
            }

            openHIMSampleTransaction = IOUtils.toString(stream);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Handles the message.
     *
     * @param msg The message.
     */
    @Override
    public void onReceive(Object msg) {
        if (!(msg instanceof MediatorHTTPRequest)) {
            return;
        }

        MediatorHTTPRequest workingRequest = (MediatorHTTPRequest) msg;

        if (workingRequest.getMethod().equals("GET")) {
            Map<String, String> headers = new HashMap<>();
            headers.put("Content-Type", "text/application/json");

            MediatorHTTPResponse mediatorHTTPResponse = new MediatorHTTPResponse((MediatorHTTPRequest) msg, openHIMSampleTransaction, 200, headers);
            workingRequest.getRespondTo().tell(mediatorHTTPResponse, getSelf());
        } else if (workingRequest.getMethod().equals("PUT")) {
            JSONObject receivedUpdatedTransaction = new JSONObject(((MediatorHTTPRequest) msg).getBody());

            Assert.assertEquals(200, receivedUpdatedTransaction.getJSONObject("response").getInt("status"));
            Assert.assertEquals("Successful", receivedUpdatedTransaction.getString("status"));
        }
    }
}
