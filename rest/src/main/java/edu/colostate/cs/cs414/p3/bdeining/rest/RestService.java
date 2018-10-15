package edu.colostate.cs.cs414.p3.bdeining.rest;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import edu.colostate.cs.cs414.p3.bdeining.sql.MySqlHandler;
import edu.colostate.cs.cs414.p3.bdeining.sql.Trainer;

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
