package nl.appsource.stream.demo.config;

import lombok.extern.slf4j.Slf4j;
import org.h2.tools.Server;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ApplicationContextEvent;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Profile("development")
public class H2 {

    private Server webServer;

    private final Integer h2ConsolePort = 8081;

    @EventListener(ContextRefreshedEvent.class)
    public void start(final ApplicationContextEvent ignore) throws java.sql.SQLException {
        log.info("Starting h2 console at port " + h2ConsolePort);
        this.webServer = org.h2.tools.Server.createWebServer("-webPort", h2ConsolePort.toString(), "-tcpAllowOthers").start();
    }

    @EventListener(ContextClosedEvent.class)
    public void stop(final ApplicationContextEvent ignore) {
        log.info("Stopping h2 console at port " + h2ConsolePort);
        this.webServer.stop();
    }

}