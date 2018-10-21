package edu.colostate.cs.cs414.p3.bdeining.rest;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import edu.colostate.cs.cs414.p3.bdeining.api.Customer;
import edu.colostate.cs.cs414.p3.bdeining.api.Exercise;
import edu.colostate.cs.cs414.p3.bdeining.api.Machine;
import edu.colostate.cs.cs414.p3.bdeining.api.MySqlHandler;
import edu.colostate.cs.cs414.p3.bdeining.api.Trainer;
import edu.colostate.cs.cs414.p3.bdeining.api.WorkoutRoutine;
import edu.colostate.cs.cs414.p3.bdeining.impl.CustomerImpl;
import edu.colostate.cs.cs414.p3.bdeining.impl.ExerciseImpl;
import edu.colostate.cs.cs414.p3.bdeining.impl.MachineImpl;
import edu.colostate.cs.cs414.p3.bdeining.impl.TrainerImpl;
import edu.colostate.cs.cs414.p3.bdeining.impl.WorkoutRoutineImpl;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.List;
import javax.ws.rs.Consumes;
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

// TODO: Check logs and variable names

@Path("/")
public class RestService {

  private static final Logger LOGGER = LoggerFactory.getLogger(RestService.class);

  private MySqlHandler mySqlHandler;

  private Gson gson = new Gson();
  // new GsonBuilder().registerTypeAdapter(Machine.class, new MachineDeserializer()).create();

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
  @Consumes(MediaType.APPLICATION_JSON)
  public Response createTrainer(InputStream requestBody) {
    try {
      Trainer trainer = gson.fromJson(new InputStreamReader(requestBody), TrainerImpl.class);
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

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/machine")
  public Response getMachine() {
    try {
      List<Machine> machineList = mySqlHandler.getMachines();
      return Response.ok().entity(machineList).build();
    } catch (SQLException e) {
      LOGGER.warn("Could not get machine list", e);
      return Response.serverError().build();
    }
  }

  @PUT
  @Path("/machine")
  @Consumes(MediaType.APPLICATION_JSON)
  public Response createMachine(InputStream requestBody) {
    try {
      Machine machine = gson.fromJson(new InputStreamReader(requestBody), MachineImpl.class);
      mySqlHandler.addMachine(machine);
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
  @Path("/machine")
  @Produces(MediaType.APPLICATION_JSON)
  public Response deleteMachine(@QueryParam("id") String id) {
    try {
      mySqlHandler.removeMachine(id);
      return Response.ok().build();
    } catch (SQLException e) {
      LOGGER.warn("Could not delete trainer {}", id, e);
      return Response.serverError().build();
    }
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/exercise")
  public Response getExercise() {
    try {
      List<Exercise> machineList = mySqlHandler.getExercises();
      return Response.ok().entity(machineList).build();
    } catch (SQLException e) {
      LOGGER.warn("Could not get exercise list", e);
      return Response.serverError().build();
    }
  }

  @PUT
  @Path("/exercise")
  @Consumes(MediaType.APPLICATION_JSON)
  public Response createExercise(InputStream requestBody) {
    try {
      Exercise exercise = gson.fromJson(new InputStreamReader(requestBody), ExerciseImpl.class);
      mySqlHandler.addExercise(exercise);
      return Response.ok().build();
    } catch (JsonSyntaxException | JsonIOException e) {
      LOGGER.warn("Could not parse exercise", e);
      return Response.serverError().build();
    } catch (SQLException e) {
      LOGGER.warn("Could not add exercise", e);
      return Response.serverError().build();
    }
  }

  @DELETE
  @Path("/exercise")
  @Produces(MediaType.APPLICATION_JSON)
  public Response deleteExercise(@QueryParam("id") String id) {
    try {
      mySqlHandler.removeExercise(id);
      return Response.ok().build();
    } catch (SQLException e) {
      LOGGER.warn("Could not delete trainer {}", id, e);
      return Response.serverError().build();
    }
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/routine")
  public Response getRoutine() {
    try {
      List<WorkoutRoutine> routineList = mySqlHandler.getWorkoutRoutines();
      return Response.ok().entity(routineList).build();
    } catch (SQLException e) {
      LOGGER.warn("Could not get workout routine list", e);
      return Response.serverError().build();
    }
  }

  @PUT
  @Path("/routine")
  @Consumes(MediaType.APPLICATION_JSON)
  public Response createWorkoutRoutine(InputStream requestBody) {
    try {
      WorkoutRoutine workoutRoutine =
          gson.fromJson(new InputStreamReader(requestBody), WorkoutRoutineImpl.class);
      mySqlHandler.addWorkoutRoutine(workoutRoutine);
      return Response.ok().build();
    } catch (JsonSyntaxException | JsonIOException e) {
      LOGGER.warn("Could not parse routine", e);
      return Response.serverError().build();
    } catch (SQLException e) {
      LOGGER.warn("Could not add routine", e);
      return Response.serverError().build();
    }
  }

  @DELETE
  @Path("/routine")
  @Produces(MediaType.APPLICATION_JSON)
  public Response deleteRoutine(@QueryParam("id") String id) {
    try {
      mySqlHandler.removeWorkoutRoutine(id);
      return Response.ok().build();
    } catch (SQLException e) {
      LOGGER.warn("Could not delete trainer {}", id, e);
      return Response.serverError().build();
    }
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/customer")
  public Response getCustomer() {
    try {
      List<Customer> customerList = mySqlHandler.getCustomers();
      return Response.ok().entity(customerList).build();
    } catch (SQLException e) {
      LOGGER.warn("Could not get customer list", e);
      return Response.serverError().build();
    }
  }

  @PUT
  @Path("/customer")
  @Consumes(MediaType.APPLICATION_JSON)
  public Response createCustomer(InputStream requestBody) {
    try {
      Customer customer = gson.fromJson(new InputStreamReader(requestBody), CustomerImpl.class);
      mySqlHandler.addCustomer(customer);
      return Response.ok().build();
    } catch (JsonSyntaxException | JsonIOException e) {
      LOGGER.warn("Could not parse customer", e);
      return Response.serverError().build();
    } catch (SQLException e) {
      LOGGER.warn("Could not add customer", e);
      return Response.serverError().build();
    }
  }

  @DELETE
  @Path("/customer")
  @Produces(MediaType.APPLICATION_JSON)
  public Response deleteCustomer(@QueryParam("id") String id) {
    try {
      mySqlHandler.removeCustomer(id);
      return Response.ok().build();
    } catch (SQLException e) {
      LOGGER.warn("Could not delete customer {}", id, e);
      return Response.serverError().build();
    }
  }

  /*  private class MachineDeserializer implements JsonDeserializer<Machine> {

      @Override
      public Machine deserialize(JsonElement jsonElement, Type type,
              JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {

          JsonObject jsonObject = jsonElement.getAsJsonObject();

          return new MachineImpl(
                  jsonObject.get("name").getAsString(),
                  jsonObject.get("picture").getAsString(),
                  jsonObject.get("quantity").getAsInt()
          );
      }
  }*/
}
