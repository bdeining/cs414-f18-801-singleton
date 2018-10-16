package edu.colostate.cs.cs414.p3.bdeining.rest;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;

import edu.colostate.cs.cs414.p3.bdeining.api.MySqlHandler;
import edu.colostate.cs.cs414.p3.bdeining.api.Trainer;

@Path("/")
public class RestService {

    private MySqlHandler mySqlHandler;

    private Gson gson = new Gson();

    public RestService(MySqlHandler mySqlHandler) {
        this.mySqlHandler = mySqlHandler;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/trainer")
    public Response getTrainer() {
        return Response.ok().entity(mySqlHandler.getTrainers()).build();
    }


    @PUT
    @Path("/trainer")
    @Produces(MediaType.APPLICATION_JSON)
    public Response createTrainer(InputStream requestBody){

        Trainer trainer = gson.fromJson(new InputStreamReader(requestBody), Trainer.class);

        mySqlHandler.addTrainer(trainer);

    return Response.ok().build();
    }
}
