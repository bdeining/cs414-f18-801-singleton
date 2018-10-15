package edu.colostate.cs.cs414.p3.bdeining.rest;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;


import edu.colostate.cs.cs414.p3.bdeining.api.MySqlHandler;
import edu.colostate.cs.cs414.p3.bdeining.api.Trainer;

@Path("/")
public class RestService {

    private MySqlHandler mySqlHandler;

    public void setMySqlHandler(MySqlHandler mySqlHandler) {
        this.mySqlHandler = mySqlHandler;
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("trainer")
    public List<Trainer> getTrainer() {
        return mySqlHandler.getTrainers();
    }


}
