package tz.go.moh.him.hfr.mediator.orchestrator;

import akka.actor.ActorSelection;
import com.google.gson.Gson;
import org.apache.commons.lang3.tuple.Pair;
import org.openhim.mediator.engine.MediatorConfig;
import org.openhim.mediator.engine.messages.MediatorHTTPRequest;
import tz.go.moh.him.hfr.mediator.domain.EpicorHfrRequest;
import tz.go.moh.him.hfr.mediator.domain.HfrRequest;

import java.util.List;
import java.util.Map;

public class EpicorFacilityOrchestrator extends FacilityOrchestrator {

    /**
     * Initializes a new instance of the {@link EpicorFacilityOrchestrator} class.
     *
     * @param config The mediator configuration.
     */
    public EpicorFacilityOrchestrator(MediatorConfig config) {
        super(config);
    }

    @Override
    protected void processMessage(Map<String, String> headers, List<Pair<String, String>> parameters, Object message) {
        Gson gson = new Gson();

        HfrRequest hfrRequest = gson.fromJson(workingRequest.getBody(), HfrRequest.class);

        EpicorHfrRequest epicorHfrRequest = new EpicorHfrRequest(hfrRequest);

        epicorHfrRequest.setTransactionIdNumber(workingRequest.getHeaders().get("x-openhim-transactionid"));

        MediatorHTTPRequest epicorRequest = new MediatorHTTPRequest(workingRequest.getRequestHandler(), getSelf(), "Sending data to EPICOR", HfrRequest.OPERATION_MAP.get(hfrRequest.getPostOrUpdate()),
                config.getProperty("epicor.scheme"), config.getProperty("epicor.host"), Integer.parseInt(config.getProperty("epicor.api.port")), config.getProperty("epicor.api.path"),
                gson.toJson(epicorHfrRequest), headers, parameters);

        ActorSelection httpConnector = getContext().actorSelection(config.userPathFor("http-connector"));
        httpConnector.tell(epicorRequest, getSelf());
    }
}
