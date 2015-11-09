 package com.simpleApplications.audioRecorder.verticles;

import com.google.inject.Inject;
import com.simpleApplications.audioRecorder.actions.DatabaseActionFactory;
import com.simpleApplications.audioRecorder.handlers.interfaces.IRequestHandler;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;

import java.util.HashSet;
import java.util.Set;

 /**
 * @author Nico Moehring
 */
public class HttpVertical extends AbstractVerticle {

     private Logger logger = LoggerFactory.getLogger(this.getClass());

     protected Set<IRequestHandler> handlers;

     @Inject
     public HttpVertical(Set<IRequestHandler> handlers) {
         this.handlers = handlers;
     }

     @Override
    public void start() throws Exception {
         final HttpServer server = vertx.createHttpServer();
         final Router router = Router.router(vertx);
         final Set<String> addedRoutes = new HashSet<>();

         router.route().handler(BodyHandler.create());

         for (IRequestHandler tmpHandler : this.handlers) {
             tmpHandler.getRoutes().forEach(route -> {
                 if (!addedRoutes.contains(route)) {
                     logger.info("Binding route " + route + " to handler: " + tmpHandler.getClass().getCanonicalName());
                     router.route().path(route).handler(tmpHandler);
                     addedRoutes.add(route);
                 } else {
                     logger.warn("Could not handler " + tmpHandler.getClass().getCanonicalName() + " route " + route + " already bound!");
                 }
             });
         }

         server.requestHandler(router::accept).listen(8080);
    }
}
