package net.winterly.dropwizard.hk2bundle.resource;

import net.winterly.dropwizard.hk2bundle.jdbi.ExampleDAO;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("/")
public class ExampleResource {

    @Inject
    private ExampleDAO dao;

    @POST
    @Path("ping")
    public Pong ping(@Valid Ping ping) {
        return new Pong(ping);
    }

    public static class Ping {
        public int value;
    }

    public static class Pong {
        public final int value;

        public Pong(Ping ping) {
            this.value = ping.value;
        }
    }
}
