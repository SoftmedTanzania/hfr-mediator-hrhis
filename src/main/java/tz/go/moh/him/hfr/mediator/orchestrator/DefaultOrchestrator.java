package tz.go.moh.him.hfr.mediator.orchestrator;

import akka.actor.UntypedActor;
import org.apache.http.HttpStatus;
import org.openhim.mediator.engine.MediatorConfig;
import org.openhim.mediator.engine.messages.FinishRequest;
import org.openhim.mediator.engine.messages.MediatorHTTPRequest;

public class DefaultOrchestrator extends UntypedActor {

    private final MediatorConfig config;


    public DefaultOrchestrator(MediatorConfig config) {
        this.config = config;
    }

    @Override
    public void onReceive(Object msg) throws Exception {
        if (msg instanceof MediatorHTTPRequest) {
            FinishRequest finishRequest = new FinishRequest("Success", "text/plain", HttpStatus.SC_OK);
            ((MediatorHTTPRequest) msg).getRequestHandler().tell(finishRequest, getSelf());
        } else {
            unhandled(msg);
        }
    }
}
