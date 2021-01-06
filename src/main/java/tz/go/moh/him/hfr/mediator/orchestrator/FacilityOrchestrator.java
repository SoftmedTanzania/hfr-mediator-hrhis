package tz.go.moh.him.hfr.mediator.orchestrator;

import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.http.HttpStatus;
import org.openhim.mediator.engine.MediatorConfig;
import org.openhim.mediator.engine.messages.FinishRequest;
import org.openhim.mediator.engine.messages.MediatorHTTPRequest;
import org.openhim.mediator.engine.messages.MediatorHTTPResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents a facility orchestrator.
 */
public abstract class FacilityOrchestrator extends UntypedActor {
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
     * Process the message.
     *
     * @param message The message.
     */
    protected abstract void processMessage(Map<String, String> headers, List<Pair<String, String>> parameters, Object message);

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

            Map<String, String> headers = new HashMap<>();

            headers.put("Content-Type", "application/json");

            List<Pair<String, String>> parameters = new ArrayList<>();

            this.processMessage(headers, parameters, msg);

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
