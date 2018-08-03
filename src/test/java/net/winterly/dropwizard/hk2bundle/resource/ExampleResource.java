package net.winterly.dropwizard.hk2bundle.resource;

import net.winterly.dropwizard.hk2bundle.jdbi.ExampleDAO;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("/")
public class ExampleResource {

    @Inject
    private ExampleDAO dao;

    @GET
    @Path("ping")
    public int ping(@QueryParam("value") int value) {
        return dao.selectNumber(value);
    }
}
