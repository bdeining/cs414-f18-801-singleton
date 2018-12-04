package edu.colostate.cs.cs414.p3.bdeining.rest;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import edu.colostate.cs.cs414.p3.bdeining.api.Customer;
import edu.colostate.cs.cs414.p3.bdeining.api.Exercise;
import edu.colostate.cs.cs414.p3.bdeining.api.Machine;
import edu.colostate.cs.cs414.p3.bdeining.api.Trainer;
import edu.colostate.cs.cs414.p3.bdeining.api.WorkoutRoutine;
import edu.colostate.cs.cs414.p3.bdeining.api.handlers.BranchHandler;
import edu.colostate.cs.cs414.p3.bdeining.api.handlers.CustomerHandler;
import edu.colostate.cs.cs414.p3.bdeining.api.handlers.ExerciseHandler;
import edu.colostate.cs.cs414.p3.bdeining.api.handlers.MachineHandler;
import edu.colostate.cs.cs414.p3.bdeining.api.handlers.TrainerHandler;
import edu.colostate.cs.cs414.p3.bdeining.api.handlers.WorkoutRoutineHandler;
import edu.colostate.cs.cs414.p3.bdeining.impl.CustomerImpl;
import edu.colostate.cs.cs414.p3.bdeining.impl.ExerciseImpl;
import edu.colostate.cs.cs414.p3.bdeining.impl.MachineImpl;
import edu.colostate.cs.cs414.p3.bdeining.impl.TrainerImpl;
import edu.colostate.cs.cs414.p3.bdeining.impl.WorkoutRoutineImpl;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
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

@Path("/")
public class RestService {

  public static final String LABEL_KEY = "label";

  public static final String VALUE_KEY = "value";

  private static final String MANAGER_USER = "manager";

  private static final String MANAGER_PASSWORD = "manager";

  private static final Logger LOGGER = LoggerFactory.getLogger(RestService.class);

  private CustomerHandler customerHandler;

  private TrainerHandler trainerHandler;

  private ExerciseHandler exerciseHandler;

  private WorkoutRoutineHandler workoutRoutineHandler;

  private MachineHandler machineHandler;

  private BranchHandler branchHandler;

  private Gson gson = new Gson();

  public RestService(
      CustomerHandler customerHandler,
      TrainerHandler trainerHandler,
      ExerciseHandler exerciseHandler,
      WorkoutRoutineHandler workoutRoutineHandler,
      MachineHandler machineHandler,
      BranchHandler branchHandler) {
    this.customerHandler = customerHandler;
    this.trainerHandler = trainerHandler;
    this.exerciseHandler = exerciseHandler;
    this.workoutRoutineHandler = workoutRoutineHandler;
    this.machineHandler = machineHandler;
    this.branchHandler = branchHandler;
  }

  /** {@inheritDoc} */
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/login")
  public Response login(
      @QueryParam("email") String email, @QueryParam("password") String password) {

    if (email.equals(MANAGER_USER) && password.equals(MANAGER_PASSWORD)) {
      return Response.ok().build();
    }

    try {
      for (Trainer trainer : trainerHandler.getTrainers()) {
        if (email.equals(trainer.getEmail()) && password.equals(trainer.getPassword())) {
          return Response.ok().build();
        }
      }
    } catch (SQLException e) {
      return Response.status(500).build();
    }

    return Response.status(500).build();
  }

  /** {@inheritDoc} */
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/routinenames")
  public Response getWorkoutRoutineNames() {
    try {
      List<WorkoutRoutine> trainerList = workoutRoutineHandler.getWorkoutRoutines();
      List<Map<String, Object>> machineNames =
          trainerList
              .stream()
              .map(
                  machine -> {
                    Map<String, Object> objectMap = new HashMap<>();
                    objectMap.put(LABEL_KEY, machine.getName());
                    objectMap.put(VALUE_KEY, machine.getId());
                    return objectMap;
                  })
              .collect(Collectors.toList());
      return Response.ok().entity(machineNames).build();
    } catch (SQLException e) {
      LOGGER.warn("Could not get trainer list", e);
      return Response.serverError().build();
    }
  }

  /** {@inheritDoc} */
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/exercisenames")
  public Response getExerciseNames() {
    try {
      List<Exercise> trainerList = exerciseHandler.getExercises();
      List<Map<String, Object>> machineNames =
          trainerList
              .stream()
              .map(
                  machine -> {
                    Map<String, Object> objectMap = new HashMap<>();
                    objectMap.put(LABEL_KEY, machine.getCommonName());
                    objectMap.put(VALUE_KEY, machine.getId());
                    return objectMap;
                  })
              .collect(Collectors.toList());
      return Response.ok().entity(machineNames).build();
    } catch (SQLException e) {
      LOGGER.warn("Could not get trainer list", e);
      return Response.serverError().build();
    }
  }

  /** {@inheritDoc} */
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/machinenames")
  public Response getMachineNames() {
    try {
      List<Machine> trainerList = machineHandler.getMachines();
      List<Map<String, Object>> machineNames =
          trainerList
              .stream()
              .map(
                  machine -> {
                    Map<String, Object> objectMap = new HashMap<>();
                    objectMap.put(LABEL_KEY, machine.getName());
                    objectMap.put(VALUE_KEY, machine.getId());
                    return objectMap;
                  })
              .collect(Collectors.toList());
      return Response.ok().entity(machineNames).build();
    } catch (SQLException e) {
      LOGGER.warn("Could not get trainer list", e);
      return Response.serverError().build();
    }
  }

  /** {@inheritDoc} */
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/branch")
  public Response getBranch() {
    try {
      List<String> branches = branchHandler.getBranches();
      return Response.ok().entity(branches).build();
    } catch (SQLException e) {
      LOGGER.warn("Could not get branch list", e);
      return Response.serverError().build();
    }
  }

  /** {@inheritDoc} */
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/trainer")
  public Response getTrainers() {
    try {
      List<Trainer> trainerList = trainerHandler.getTrainers();
      return Response.ok().entity(trainerList).build();
    } catch (SQLException e) {
      LOGGER.warn("Could not get trainer list", e);
      return Response.serverError().build();
    }
  }

  /** {@inheritDoc} */
  @PUT
  @Path("/trainer")
  @Consumes(MediaType.APPLICATION_JSON)
  public Response createOrUpdateTrainer(InputStream requestBody) {
    try {
      Trainer trainer = gson.fromJson(new InputStreamReader(requestBody), TrainerImpl.class);
      trainerHandler.addTrainer(trainer);
      return Response.ok().build();
    } catch (JsonSyntaxException | JsonIOException e) {
      LOGGER.warn("Could not parse trainer", e);
      return Response.serverError().build();
    } catch (SQLException e) {
      LOGGER.warn("Could not add trainer", e);
      return Response.serverError().build();
    }
  }

  /** {@inheritDoc} */
  @PUT
  @Path("/branch")
  @Consumes(MediaType.APPLICATION_JSON)
  public Response createBranch(InputStream requestBody) {
    try {
      String branch = gson.fromJson(new InputStreamReader(requestBody), String.class);
      branchHandler.addBranch(branch);
      return Response.ok().build();
    } catch (JsonSyntaxException | JsonIOException e) {
      LOGGER.warn("Could not parse branch", e);
      return Response.serverError().build();
    } catch (SQLException e) {
      LOGGER.warn("Could not add branch", e);
      return Response.serverError().build();
    }
  }

  /** {@inheritDoc} */
  @DELETE
  @Path("/trainer")
  @Produces(MediaType.APPLICATION_JSON)
  public Response deleteBranch(@QueryParam("branch") String branchH) {
    try {
      branchHandler.removeBranch(branchH);
      return Response.ok().build();
    } catch (SQLException e) {
      LOGGER.warn("Could not delete branch {}", branchH, e);
      return Response.serverError().build();
    }
  }

  /** {@inheritDoc} */
  @DELETE
  @Path("/trainer")
  @Produces(MediaType.APPLICATION_JSON)
  public Response deleteTrainer(@QueryParam("id") String id) {
    try {
      trainerHandler.removeTrainer(id);
      return Response.ok().build();
    } catch (SQLException e) {
      LOGGER.warn("Could not delete trainer {}", id, e);
      return Response.serverError().build();
    }
  }

  /** {@inheritDoc} */
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/machine")
  public Response getMachines() {
    try {
      List<Machine> machineList = machineHandler.getMachines();
      return Response.ok().entity(machineList).build();
    } catch (SQLException e) {
      LOGGER.warn("Could not get machine list", e);
      return Response.serverError().build();
    }
  }

  /** {@inheritDoc} */
  @PUT
  @Path("/machine")
  @Consumes(MediaType.APPLICATION_JSON)
  public Response createOrUpdateMachine(InputStream requestBody) {
    try {
      Machine machine = gson.fromJson(new InputStreamReader(requestBody), MachineImpl.class);
      machineHandler.addMachine(machine);
      return Response.ok().build();
    } catch (JsonSyntaxException | JsonIOException e) {
      LOGGER.warn("Could not parse machine", e);
      return Response.serverError().build();
    } catch (SQLException e) {
      LOGGER.warn("Could not add machine", e);
      return Response.serverError().build();
    }
  }

  /** {@inheritDoc} */
  @DELETE
  @Path("/machine")
  @Produces(MediaType.APPLICATION_JSON)
  public Response deleteMachine(@QueryParam("id") String id) {
    try {
      machineHandler.removeMachine(id);
      return Response.ok().build();
    } catch (SQLException e) {
      LOGGER.warn("Could not delete machine {}", id, e);
      return Response.serverError().build();
    }
  }

  /** {@inheritDoc} */
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/exercise")
  public Response getExercises() {
    try {
      List<Exercise> exerciseList = exerciseHandler.getExercises();
      return Response.ok().entity(exerciseList).build();
    } catch (SQLException e) {
      LOGGER.warn("Could not get exercise list", e);
      return Response.serverError().build();
    }
  }

  /** {@inheritDoc} */
  @PUT
  @Path("/exercise")
  @Consumes(MediaType.APPLICATION_JSON)
  public Response createOrUpdateExercise(InputStream requestBody) {
    try {
      Exercise exercise = gson.fromJson(new InputStreamReader(requestBody), ExerciseImpl.class);
      exerciseHandler.addExercise(exercise);
      return Response.ok().build();
    } catch (JsonSyntaxException | JsonIOException e) {
      LOGGER.warn("Could not parse exercise", e);
      return Response.serverError().build();
    } catch (SQLException e) {
      LOGGER.warn("Could not add exercise", e);
      return Response.serverError().build();
    }
  }

  /** {@inheritDoc} */
  @DELETE
  @Path("/exercise")
  @Produces(MediaType.APPLICATION_JSON)
  public Response deleteExercise(@QueryParam("id") String id) {
    try {
      exerciseHandler.removeExercise(id);
      return Response.ok().build();
    } catch (SQLException e) {
      LOGGER.warn("Could not delete exercise {}", id, e);
      return Response.serverError().build();
    }
  }

  /** {@inheritDoc} */
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/routine")
  public Response getWorkoutRoutines() {
    try {
      List<WorkoutRoutine> routineList = workoutRoutineHandler.getWorkoutRoutines();
      return Response.ok().entity(routineList).build();
    } catch (SQLException e) {
      LOGGER.warn("Could not get workout routine list", e);
      return Response.serverError().build();
    }
  }

  /** {@inheritDoc} */
  @PUT
  @Path("/routine")
  @Consumes(MediaType.APPLICATION_JSON)
  public Response createOrUpdateWorkoutRoutine(InputStream requestBody) {
    try {
      WorkoutRoutine workoutRoutine =
          gson.fromJson(new InputStreamReader(requestBody), WorkoutRoutineImpl.class);
      workoutRoutineHandler.addWorkoutRoutine(workoutRoutine);
      return Response.ok().build();
    } catch (JsonSyntaxException | JsonIOException e) {
      LOGGER.warn("Could not parse routine", e);
      return Response.serverError().build();
    } catch (SQLException e) {
      LOGGER.warn("Could not add routine", e);
      return Response.serverError().build();
    }
  }

  /** {@inheritDoc} */
  @DELETE
  @Path("/routine")
  @Produces(MediaType.APPLICATION_JSON)
  public Response deleteRoutine(@QueryParam("id") String id) {
    try {
      workoutRoutineHandler.removeWorkoutRoutine(id);
      return Response.ok().build();
    } catch (SQLException e) {
      LOGGER.warn("Could not delete routine {}", id, e);
      return Response.serverError().build();
    }
  }

  /** {@inheritDoc} */
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/customer")
  public Response getCustomers() {
    try {
      List<Customer> customerList = customerHandler.getCustomers();
      return Response.ok().entity(customerList).build();
    } catch (SQLException e) {
      LOGGER.warn("Could not get customer list", e);
      return Response.serverError().build();
    }
  }

  /** {@inheritDoc} */
  @PUT
  @Path("/customer")
  @Consumes(MediaType.APPLICATION_JSON)
  public Response createOrUpdateCustomer(InputStream requestBody) {
    try {
      Customer customer = gson.fromJson(new InputStreamReader(requestBody), CustomerImpl.class);
      customerHandler.addCustomer(customer);
      return Response.ok().build();
    } catch (JsonSyntaxException | JsonIOException e) {
      LOGGER.warn("Could not parse customer", e);
      return Response.serverError().build();
    } catch (SQLException e) {
      LOGGER.warn("Could not add customer", e);
      return Response.serverError().build();
    }
  }

  /** {@inheritDoc} */
  @DELETE
  @Path("/customer")
  @Produces(MediaType.APPLICATION_JSON)
  public Response deleteCustomer(@QueryParam("id") String id) {
    try {
      customerHandler.removeCustomer(id);
      return Response.ok().build();
    } catch (SQLException e) {
      LOGGER.warn("Could not delete customer {}", id, e);
      return Response.serverError().build();
    }
  }
}
