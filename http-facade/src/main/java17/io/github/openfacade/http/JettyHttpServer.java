/*
 * Copyright 2024 OpenFacade Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.openfacade.http;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.ee10.servlet.ServletContextHandler;
import org.eclipse.jetty.ee10.servlet.ServletHolder;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;

import java.util.concurrent.CompletableFuture;

@Slf4j
public class JettyHttpServer extends ServletHttpServer {
    private final Server server;

    private final ServerConnector serverConnector;

    public JettyHttpServer(HttpServerConfig config, Server server, ServerConnector serverConnector) {
        super(config);
        this.server = server;
        this.serverConnector = serverConnector;
    }

    @Override
    public CompletableFuture<Void> start() {
        // Setup servlet context
        ServletContextHandler context = new ServletContextHandler();
        context.setContextPath("/");
        server.setHandler(context);

        // Add servlet handler
        context.addServlet(new ServletHolder(new RequestHandlerServlet()), "/*");
        return CompletableFuture.runAsync(() -> {
            try {
                server.start();
                log.info("Jetty HTTP server started on port {}", config.port());
            } catch (Exception e) {
                throw new RuntimeException("Failed to start Jetty server: " + e.getMessage(), e);
            }
        });
    }

    @Override
    public CompletableFuture<Void> stop() {
        return CompletableFuture.runAsync(() -> {
            try {
                if (server != null) {
                    server.stop();
                }
            } catch (Exception e) {
                throw new RuntimeException("Failed to stop Jetty server: " + e.getMessage(), e);
            }
        });
    }

    @Override
    public int listenPort() {
        return serverConnector.getLocalPort();
    }
}
