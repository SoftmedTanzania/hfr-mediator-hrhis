package tz.go.moh.him.hfr.mediator.orchestrator;

import akka.actor.ActorSelection;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.google.gson.Gson;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.tuple.Pair;
import org.openhim.mediator.engine.MediatorConfig;
import org.openhim.mediator.engine.messages.MediatorHTTPRequest;
import org.openhim.mediator.engine.messages.MediatorHTTPResponse;
import tz.go.moh.him.hfr.mediator.domain.HfrRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents a facility orchestrator.
 */
public class FacilityOrchestrator extends UntypedActor {
    /**
     * The logger instance.
     */
    protected final LoggingAdapter log = Logging.getLogger(getContext().system(), this);

    /**
     * The mediator configuration.
     */
    protected final MediatorConfig config;

    /**
     * Represents a mediator request.
     */
    protected MediatorHTTPRequest workingRequest;

    /**
     * Initializes a new instance of the {@link FacilityOrchestrator} class.
     *
     * @param config The mediator configuration.
     */
    public FacilityOrchestrator(MediatorConfig config) {
        this.config = config;
    }

    /**
     * Handles the received message.
     *
     * @param msg The received message.
     */
    @Override
    public void onReceive(Object msg) {
        if (msg instanceof MediatorHTTPRequest) {

            workingRequest = (MediatorHTTPRequest) msg;

            log.info("Received request: " + workingRequest.getHost() + " " + workingRequest.getMethod() + " " + workingRequest.getPath());

            Map<String, String> headers = new HashMap<>();

            headers.put("Content-Type", "application/json");
            headers.put("Authorization", String.format("Basic: %s", Base64.encodeBase64String(String.format("%s:%s", config.getProperty("destination.username"), config.getProperty("destination.password")).getBytes())));

            List<Pair<String, String>> parameters = new ArrayList<>();

            Gson gson = new Gson();

            HfrRequest hfrRequest = gson.fromJson(workingRequest.getBody(), HfrRequest.class);

            hfrRequest.setTransactionIdNumber(workingRequest.getHeaders().get("x-openhim-transactionid"));

            MediatorHTTPRequest request = new MediatorHTTPRequest(workingRequest.getRequestHandler(), getSelf(), "Sending data", HfrRequest.OPERATION_MAP.get(hfrRequest.getPostOrUpdate()),
                    config.getProperty("destination.scheme"), config.getProperty("destination.host"), Integer.parseInt(config.getProperty("destination.api.port")), config.getProperty("destination.api.path"),
                    gson.toJson(hfrRequest), headers, parameters);

            ActorSelection httpConnector = getContext().actorSelection(config.userPathFor("http-connector"));
            httpConnector.tell(request, getSelf());

        } else if (msg instanceof MediatorHTTPResponse) {
            workingRequest.getRequestHandler().tell(((MediatorHTTPResponse) msg).toFinishRequest(), getSelf());
        } else {
            unhandled(msg);
        }
    }
}
