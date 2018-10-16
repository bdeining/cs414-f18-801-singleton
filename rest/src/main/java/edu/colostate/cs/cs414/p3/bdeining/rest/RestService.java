package edu.colostate.cs.cs414.p3.bdeining.rest;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import edu.colostate.cs.cs414.p3.bdeining.api.MySqlHandler;
import edu.colostate.cs.cs414.p3.bdeining.api.Trainer;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.List;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/")
public class RestService {

  private static final Logger LOGGER = LoggerFactory.getLogger(RestService.class);

  private MySqlHandler mySqlHandler;

  private Gson gson = new Gson();

  public RestService(MySqlHandler mySqlHandler) {
    this.mySqlHandler = mySqlHandler;
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/trainer")
  public Response getTrainer() {
    try {
      List<Trainer> trainerList = mySqlHandler.getTrainers();
      return Response.ok().entity(trainerList).build();
    } catch (SQLException e) {
      LOGGER.warn("Could not get trainer list", e);
      return Response.serverError().build();
    }
  }

  @PUT
  @Path("/trainer")
  @Produces(MediaType.APPLICATION_JSON)
  public Response createTrainer(InputStream requestBody) {

    try {
      Trainer trainer = gson.fromJson(new InputStreamReader(requestBody), Trainer.class);
      mySqlHandler.addTrainer(trainer);
      return Response.ok().build();
    } catch (JsonSyntaxException | JsonIOException e) {
      LOGGER.warn("Could not parse trainer", e);
      return Response.serverError().build();
    } catch (SQLException e) {
      LOGGER.warn("Could not add trainer", e);
      return Response.serverError().build();
    }
  }

  @DELETE
  @Path("/trainer")
  @Produces(MediaType.APPLICATION_JSON)
  public Response deleteTrainer(@QueryParam("id") String id) {
    try {
      mySqlHandler.removeTrainer(id);
      return Response.ok().build();
    } catch (SQLException e) {
      LOGGER.warn("Could not delete trainer {}", id, e);
      return Response.serverError().build();
    }
  }
}
