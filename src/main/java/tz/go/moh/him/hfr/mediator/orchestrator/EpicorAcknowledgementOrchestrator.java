package tz.go.moh.him.hfr.mediator.orchestrator;

import akka.actor.ActorSelection;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.google.gson.Gson;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.http.HttpStatus;
import org.json.JSONObject;
import org.openhim.mediator.engine.MediatorConfig;
import org.openhim.mediator.engine.messages.FinishRequest;
import org.openhim.mediator.engine.messages.MediatorHTTPRequest;
import org.openhim.mediator.engine.messages.MediatorHTTPResponse;
import tz.go.moh.him.hfr.mediator.domain.EpicorAck;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents an EPICOR acknowledgement orchestrator.
 */
public class EpicorAcknowledgementOrchestrator extends UntypedActor {
    /**
     * The logger instance.
     */
    private final LoggingAdapter log = Logging.getLogger(getContext().system(), this);

    /**
     * The mediator configuration.
     */
    private final MediatorConfig config;

    /**
     * Represents an EPICOR ACK.
     */
    private EpicorAck epicorAck;

    /**
     * Represents a mediator request.
     */
    private MediatorHTTPRequest workingRequest;

    /**
     * Initializes a new instance of the {@link EpicorAcknowledgementOrchestrator} class.
     *
     * @param config The mediator configuration.
     */
    public EpicorAcknowledgementOrchestrator(MediatorConfig config) {
        this.config = config;
    }

    /**
     * Handles the received message.
     *
     * @param msg The received message.
     * @throws Exception
     */
    @Override
    public void onReceive(Object msg) throws Exception {
        if (msg instanceof MediatorHTTPRequest) {
            this.workingRequest = (MediatorHTTPRequest) msg;
            this.epicorAck = new Gson().fromJson(((MediatorHTTPRequest) msg).getBody(), EpicorAck.class);
            obtainOpenHIMTransactionByTransactionId(epicorAck.getTransactionIdNumber());
        } else if (msg instanceof MediatorHTTPResponse) {
            this.log.info("Received feedback from core");
            this.log.debug("Core Response code = " + ((MediatorHTTPResponse) msg).getStatusCode());
            this.log.debug("Core Response body = " + ((MediatorHTTPResponse) msg).getBody());
            updateOpenHIMTransactionByTransactionId(new JSONObject(((MediatorHTTPResponse) msg).getBody()));

            FinishRequest finishRequest = new FinishRequest("", "text/plain", HttpStatus.SC_OK);

            (this.workingRequest).getRequestHandler().tell(finishRequest, getSelf());
        }
    }

    /**
     * Retrieves the OpenHIM transaction by id.
     *
     * @param transactionId The transaction id.
     */
    private void obtainOpenHIMTransactionByTransactionId(String transactionId) {

        Map<String, String> headers = new HashMap<>();

        headers.put("Content-Type", "application/json");

        List<Pair<String, String>> params = new ArrayList<>();

        MediatorHTTPRequest obtainOpenHIMTransactionRequest = new MediatorHTTPRequest(
                (this.workingRequest).getRequestHandler(), getSelf(), "Obtaining OpenHIM Transaction by transactionId", "GET", this.config.getProperty("core.scheme"),
                this.config.getProperty("core.host"), Integer.parseInt(config.getProperty("core.api.port")), "/transactions/" + transactionId,
                null, headers, params
        );

        ActorSelection coreApiConnector = getContext().actorSelection(config.userPathFor("core-api-connector"));

        coreApiConnector.tell(obtainOpenHIMTransactionRequest, getSelf());
    }

    /**
     * Updates the OpenHIM transaction status.
     *
     * @param transaction The transaction.
     */
    private void updateOpenHIMTransactionByTransactionId(JSONObject transaction) {
        this.log.info("Updating OpenHIM Transaction with ELMIS ACK");
        if (this.epicorAck.getStatus().equals("Success")) {
            transaction.getJSONObject("response").put("status", HttpStatus.SC_OK);
            transaction.put("status", "Successful");
        } else {
            transaction.getJSONObject("response").put("status", HttpStatus.SC_BAD_REQUEST);
            transaction.put("status", "Failed");
        }
        transaction.getJSONObject("response").put("body", new Gson().toJson(this.epicorAck));
        transaction.getJSONObject("response").put("timestamp", new Timestamp(System.currentTimeMillis()));


        Map<String, String> headers = new HashMap<>();

        headers.put("Content-Type", "application/json");

        List<Pair<String, String>> params = new ArrayList<>();

        MediatorHTTPRequest obtainOpenHIMTransactionRequest = new MediatorHTTPRequest(
                (this.workingRequest).getRequestHandler(), getSelf(), "Updating OpenHIM Transaction by transactionId", "PUT", this.config.getProperty("epicor.scheme"),
                this.config.getProperty("core.host"), Integer.parseInt(this.config.getProperty("core.api.port")), "/transactions/" + this.epicorAck.getTransactionIdNumber(),
                transaction.toString(), headers, params
        );

        ActorSelection coreApiConnector = getContext().actorSelection(config.userPathFor("core-api-connector"));

        coreApiConnector.tell(obtainOpenHIMTransactionRequest, getSelf());
    }
}