package tz.go.moh.him.hfr.mediator.orchestrator;

import akka.actor.ActorSelection;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.google.gson.Gson;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.http.HttpStatus;
import org.openhim.mediator.engine.MediatorConfig;
import org.openhim.mediator.engine.messages.FinishRequest;
import org.openhim.mediator.engine.messages.MediatorHTTPRequest;
import org.openhim.mediator.engine.messages.MediatorHTTPResponse;
import tz.go.moh.him.hfr.mediator.domain.EpicorHfrRequest;
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

            List<Pair<String, String>> params = new ArrayList<>();

            MediatorHTTPRequest epicorRequest = new MediatorHTTPRequest(workingRequest.getRequestHandler(), getSelf(), "Sending data to EPICOR", HfrRequest.OPERATION_MAP.get(hfrRequest.getPostOrUpdate()),
                    config.getProperty("epicor.scheme"), config.getProperty("epicor.host"), Integer.parseInt(config.getProperty("epicor.api.port")), config.getProperty("epicor.api.path"),
                    gson.toJson(epicorHfrRequest), headers, params);

            ActorSelection httpConnector = getContext().actorSelection(config.userPathFor("http-connector"));
            httpConnector.tell(epicorRequest, getSelf());

            FinishRequest finishRequest = new FinishRequest("Success", "text/plain", HttpStatus.SC_OK);
            workingRequest.getRequestHandler().tell(finishRequest, getSelf());

        } else if (msg instanceof MediatorHTTPResponse) {
            if (((MediatorHTTPResponse) msg).getStatusCode() == 200) {
                FinishRequest finishRequest = new FinishRequest("Success", "text/plain", HttpStatus.SC_OK);
                workingRequest.getRequestHandler().tell(finishRequest, getSelf());
            } else {
                workingRequest.getRequestHandler().tell(((MediatorHTTPResponse) msg).toFinishRequest(), getSelf());
            }
        } else {
            unhandled(msg);
        }
    }
}
