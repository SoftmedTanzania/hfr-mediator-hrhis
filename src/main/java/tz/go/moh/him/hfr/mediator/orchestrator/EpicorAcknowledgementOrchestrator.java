package tz.go.moh.him.hfr.mediator.orchestrator;

import akka.actor.ActorSelection;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.google.gson.Gson;
import org.apache.http.HttpStatus;
import org.openhim.mediator.engine.MediatorConfig;
import org.openhim.mediator.engine.messages.FinishRequest;
import org.openhim.mediator.engine.messages.MediatorHTTPRequest;
import org.openhim.mediator.engine.messages.MediatorHTTPResponse;
import tz.go.moh.him.hfr.mediator.domain.EpicorHfrRequest;
import tz.go.moh.him.hfr.mediator.domain.HfrRequest;

import java.util.HashMap;
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

            workingRequest = (MediatorHTTPRequest) msg;

            log.info("Received request: " + workingRequest.getHost() + " " + workingRequest.getMethod() + " " + workingRequest.getPath());

            Gson gson = new Gson();

            HfrRequest hfrRequest = gson.fromJson(workingRequest.getBody(), HfrRequest.class);

            EpicorHfrRequest epicorHfrRequest = new EpicorHfrRequest(hfrRequest);

            epicorHfrRequest.setTransactionIdNumber(workingRequest.getHeaders().get("x-openhim-transactionid"));

            Map<String, String> headers = new HashMap<>();

            headers.put("Content-Type", "application/json");

            MediatorHTTPRequest epicorRequest = new MediatorHTTPRequest(workingRequest.getRequestHandler(), getSelf(), "Sending data to EPICOR", HfrRequest.OPERATION_MAP.get(hfrRequest.getPostOrUpdate()),
                    config.getProperty("epicor.scheme"), config.getProperty("epicor.host"), Integer.parseInt(config.getProperty("epicor.api.port")), config.getProperty("epicor.api.path"),
                    gson.toJson(epicorHfrRequest), headers, null);

            ActorSelection httpConnector = getContext().actorSelection(config.userPathFor("http-connector"));
            httpConnector.tell(epicorRequest, getSelf());
        } else {
            unhandled(msg);
        }
    }
}
