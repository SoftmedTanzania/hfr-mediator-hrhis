package tz.go.moh.him.hfr.mediator;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.testkit.JavaTestKit;
import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;
import org.openhim.mediator.engine.messages.FinishRequest;
import org.openhim.mediator.engine.messages.MediatorHTTPRequest;
import tz.go.moh.him.hfr.mediator.orchestrator.FacilityOrchestrator;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;

/**
 * Contains tests for the {@link FacilityOrchestrator} class.
 */
public class FacilityOrchestratorTest extends BaseTest {

    /**
     * Tests the mediator.
     *
     * @throws Exception
     */
    @Test
    public void testMediatorHTTPRequest() throws Exception {
        new JavaTestKit(system) {{
            final ActorRef orchestrator = system.actorOf(Props.create(FacilityOrchestrator.class, configuration));

            InputStream stream = FacilityOrchestratorTest.class.getClassLoader().getResourceAsStream("request.json");

            Assert.assertNotNull(stream);

            MediatorHTTPRequest request = new MediatorHTTPRequest(
                    getRef(),
                    getRef(),
                    "unit-test",
                    "POST",
                    "http",
                    null,
                    null,
                    "/hfr",
                    IOUtils.toString(stream),
                    Collections.singletonMap("Content-Type", "application/json"),
                    Collections.emptyList()
            );

            orchestrator.tell(request, getRef());

            final Object[] out = new ReceiveWhile<Object>(Object.class, duration("1 second")) {
                @Override
                protected Object match(Object msg) {
                    if (msg instanceof FinishRequest) {
                        return msg;
                    }
                    throw noMatch();
                }
            }.get();

            Assert.assertTrue(Arrays.stream(out).anyMatch(c -> c instanceof FinishRequest));
        }};
    }
}