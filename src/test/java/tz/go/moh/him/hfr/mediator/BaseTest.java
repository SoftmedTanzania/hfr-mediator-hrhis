package tz.go.moh.him.hfr.mediator;

import akka.actor.ActorSystem;
import akka.testkit.JavaTestKit;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.openhim.mediator.engine.MediatorConfig;
import org.openhim.mediator.engine.RoutingTable;
import org.openhim.mediator.engine.testing.MockLauncher;
import org.openhim.mediator.engine.testing.TestingUtils;
import tz.go.moh.him.hfr.mediator.mock.MockDestination;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

public abstract class BaseTest {

    /**
     * Represents the configuration.
     */
    protected static MediatorConfig configuration;

    /**
     * Represents the system actor.
     */
    protected static ActorSystem system;

    /**
     * Runs cleanup after each test execution.
     *
     * @throws Exception
     */
    @After
    public void after() throws Exception {
    }

    /**
     * Runs cleanup after class execution.
     *
     * @throws Exception
     */
    @AfterClass
    public static void afterClass() throws Exception {
        TestingUtils.clearRootContext(system, configuration.getName());
        JavaTestKit.shutdownActorSystem(system);
        system = null;
    }

    /**
     * Runs initialization before each test execution.
     *
     * @throws Exception
     */
    @Before
    public void before() throws Exception {
        List<MockLauncher.ActorToLaunch> toLaunch = new LinkedList<>();
        toLaunch.add(new MockLauncher.ActorToLaunch("http-connector", MockDestination.class));
        TestingUtils.launchActors(system, configuration.getName(), toLaunch);
    }

    /**
     * Runs initialization before each class execution.
     *
     * @throws Exception
     */
    @BeforeClass
    public static void beforeClass() throws Exception {
        try {
            configuration = loadConfig(null);
            system = ActorSystem.create();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Loads the mediator configuration.
     *
     * @param configPath The configuration path.
     * @return Returns the mediator configuration.
     * @throws IOException
     * @throws RoutingTable.RouteAlreadyMappedException
     */
    protected static MediatorConfig loadConfig(String configPath) throws IOException, RoutingTable.RouteAlreadyMappedException {
        MediatorConfig config = new MediatorConfig();

        if (configPath != null) {
            Properties props = new Properties();
            File conf = new File(configPath);
            InputStream in = FileUtils.openInputStream(conf);
            props.load(in);
            IOUtils.closeQuietly(in);

            config.setProperties(props);
        } else {
            config.setProperties("mediator.properties");
        }

        config.setName(config.getProperty("mediator.name"));
        config.setServerHost(config.getProperty("mediator.host"));
        config.setServerPort(Integer.parseInt(config.getProperty("mediator.port")));
        config.setRootTimeout(Integer.parseInt(config.getProperty("mediator.timeout")));

        config.setCoreHost(config.getProperty("core.host"));
        config.setCoreAPIUsername(config.getProperty("core.api.user"));
        config.setCoreAPIPassword(config.getProperty("core.api.password"));

        config.setCoreAPIPort(Integer.parseInt(config.getProperty("core.api.port")));
        config.setHeartbeatsEnabled(true);

        return config;
    }
}
