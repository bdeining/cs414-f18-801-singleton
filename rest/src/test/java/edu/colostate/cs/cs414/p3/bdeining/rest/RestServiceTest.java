package edu.colostate.cs.cs414.p3.bdeining.rest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import edu.colostate.cs.cs414.p3.bdeining.api.Activity;
import edu.colostate.cs.cs414.p3.bdeining.api.Customer;
import edu.colostate.cs.cs414.p3.bdeining.api.Exercise;
import edu.colostate.cs.cs414.p3.bdeining.api.Machine;
import edu.colostate.cs.cs414.p3.bdeining.api.Trainer;
import edu.colostate.cs.cs414.p3.bdeining.api.WorkoutRoutine;
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
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.ws.rs.core.Response;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

public class RestServiceTest {

  private RestService restService;

  private CustomerHandler customerHandler;

  private TrainerHandler trainerHandler;

  private ExerciseHandler exerciseHandler;

  private WorkoutRoutineHandler workoutRoutineHandler;

  private MachineHandler machineHandler;

  private static final String ADDRESS = "123 Fake St.";

  private static final String MACHINE_NAME = "aMachine";

  private static final String ROUTINE_NAME = "routineName";

  private static final String EXERCISE_NAME = "exerciseName";

  private static final int QUANTITY = 1;

  private static final String FIRST_NAME = "Bob";

  private static final String LAST_NAME = "Hope";

  private static final String PHONE = "123-456-7890";

  private static final String EMAIL = "excellent@exmaple.com";

  private static final String HEALTH_INSURANCE_PROVIDER = "Kaiser";

  private static final List<String> ID_LIST = Arrays.asList("id1", "id2");

  private static final Activity ACTIVITY = Activity.ACTIVE;

  private static final List<String> QUALIFICATIONS = Arrays.asList("qual1", "qual2");

  private static final int WORK_HOURS = 12;

  private ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);

  private ArgumentCaptor<Machine> machineArgumentCaptor = ArgumentCaptor.forClass(Machine.class);

  private ArgumentCaptor<Trainer> trainerArgumentCaptor = ArgumentCaptor.forClass(Trainer.class);

  private ArgumentCaptor<WorkoutRoutine> workoutRoutineArgumentCaptor =
      ArgumentCaptor.forClass(WorkoutRoutine.class);

  private ArgumentCaptor<Exercise> exerciseArgumentCaptor = ArgumentCaptor.forClass(Exercise.class);

  @Before
  public void setUp() throws Exception {
    setUpMocks();
    restService =
        new RestService(
            customerHandler,
            trainerHandler,
            exerciseHandler,
            workoutRoutineHandler,
            machineHandler);
  }

  @Test
  public void testGetWorkoutRoutineNames() {
    Response response = restService.getWorkoutRoutineNames();
    assertThat(response, notNullValue());
    assertThat(response.getStatus(), is(200));
    List<Map<String,Object>> workoutRoutines = (List<Map<String,Object>>) response.getEntity();
    assertThat(workoutRoutines, hasSize(1));
    Map<String, Object> workoutRoutine = workoutRoutines.get(0);
    assertThat(workoutRoutine.get(RestService.LABEL_KEY), is(ROUTINE_NAME));
    assertThat(workoutRoutine.get(RestService.VALUE_KEY), notNullValue());
  }

  @Test
  public void testGetWorkoutRoutineNamesSqlException() throws SQLException {
    when(workoutRoutineHandler.getWorkoutRoutines()).thenThrow(SQLException.class);
    Response response = restService.getWorkoutRoutineNames();
    assertThat(response, notNullValue());
    assertThat(response.getStatus(), is(500));
  }

  @Test
  public void testGetExerciseNames() {
    Response response = restService.getExerciseNames();
    assertThat(response, notNullValue());
    assertThat(response.getStatus(), is(200));
    List<Map<String,Object>> exercises = (List<Map<String,Object>>) response.getEntity();
    assertThat(exercises, hasSize(1));
    Map<String, Object> workoutRoutine = exercises.get(0);
    assertThat(workoutRoutine.get(RestService.LABEL_KEY), is(EXERCISE_NAME));
    assertThat(workoutRoutine.get(RestService.VALUE_KEY), notNullValue());
  }

  @Test
  public void testGetExerciseNamesSqlException() throws SQLException {
    when(exerciseHandler.getExercises()).thenThrow(SQLException.class);
    Response response = restService.getExerciseNames();
    assertThat(response, notNullValue());
    assertThat(response.getStatus(), is(500));
  }

  @Test
  public void testGetMachineNames() {
    Response response = restService.getMachineNames();
    assertThat(response, notNullValue());
    assertThat(response.getStatus(), is(200));
    List<Map<String,Object>> machines = (List<Map<String,Object>>) response.getEntity();
    assertThat(machines, hasSize(1));
    Map<String, Object> workoutRoutine = machines.get(0);
    assertThat(workoutRoutine.get(RestService.LABEL_KEY), is(MACHINE_NAME));
    assertThat(workoutRoutine.get(RestService.VALUE_KEY), notNullValue());
  }

  @Test
  public void testGetMachineNamesSqlException() throws SQLException {
    when(machineHandler.getMachines()).thenThrow(SQLException.class);
    Response response = restService.getMachineNames();
    assertThat(response, notNullValue());
    assertThat(response.getStatus(), is(500));
  }

  @Test
  public void testGetCustomer() {
    Response response = restService.getCustomers();
    assertThat(response, notNullValue());
    assertThat(response.getStatus(), is(200));
    List<Customer> customerList = (List<Customer>) response.getEntity();
    assertThat(customerList, hasSize(1));
    Customer customer = customerList.get(0);
    assertThat(customer.getFirstName(), is(FIRST_NAME));
    assertThat(customer.getLastName(), is(LAST_NAME));
  }

  @Test
  public void testGetCustomerSqlException() throws Exception {
    when(customerHandler.getCustomers()).thenThrow(SQLException.class);
    Response response = restService.getCustomers();
    assertThat(response, notNullValue());
    assertThat(response.getStatus(), is(500));
  }

  @Test
  public void testCreateCustomer() {
    InputStream inputStream =
        RestServiceTest.class.getClassLoader().getResourceAsStream("createCustomer.json");
    Response response = restService.createOrUpdateCustomer(inputStream);

    Customer customer = customerArgumentCaptor.getValue();
    assertThat(response.getStatus(), is(200));
    assertThat(customer.getFirstName(), is("Ben"));
    assertThat(customer.getLastName(), is("Deininger"));
    assertThat(customer.getAddress(), is("2516 S Scap"));
    assertThat(customer.getEmail(), is("test@example.com"));
    assertThat(customer.getPhone(), is("520-123-1234"));
    assertThat(customer.getHealthInsuranceProvider(), is("health insurance"));
    assertThat(customer.getActivity(), is(Activity.ACTIVE));
    assertThat(
        customer.getWorkoutRoutineIds(),
        is(Collections.singletonList("7a3d4681-0640-4741-bc94-d4d445a0c6ee")));
  }

  @Test
  public void testCreateCustomerSqlException() throws Exception {
    InputStream inputStream =
        RestServiceTest.class.getClassLoader().getResourceAsStream("createCustomer.json");
    when(customerHandler.addCustomer(any(Customer.class))).thenThrow(SQLException.class);
    Response response = restService.createOrUpdateCustomer(inputStream);
    assertThat(response.getStatus(), is(500));
  }

  @Test
  public void testCreateCustomerBadJson() {
    InputStream inputStream =
        RestServiceTest.class.getClassLoader().getResourceAsStream("bad.json");
    Response response = restService.createOrUpdateCustomer(inputStream);
    assertThat(response.getStatus(), is(500));
  }

  @Test
  public void testDeleteCustomer() throws Exception {
    Response response = restService.deleteCustomer("anId");
    assertThat(response.getStatus(), is(200));
    verify(customerHandler, times(1)).removeCustomer(anyString());
  }

  @Test
  public void testDeleteCustomerSqlException() throws Exception {
    when(customerHandler.removeCustomer(anyString())).thenThrow(SQLException.class);
    Response response = restService.deleteCustomer("anId");
    assertThat(response.getStatus(), is(500));
  }

  @Test
  public void testGetMachine() {
    Response response = restService.getMachines();
    assertThat(response, notNullValue());
    assertThat(response.getStatus(), is(200));
    List<Machine> machineList = (List<Machine>) response.getEntity();
    assertThat(machineList, hasSize(1));
    Machine machine = machineList.get(0);
    assertThat(machine.getName(), is(MACHINE_NAME));
    assertThat(machine.getQuantity(), is(QUANTITY));
  }

  @Test
  public void testGetMachineSqlException() throws Exception {
    when(machineHandler.getMachines()).thenThrow(SQLException.class);
    Response response = restService.getMachines();
    assertThat(response, notNullValue());
    assertThat(response.getStatus(), is(500));
  }

  @Test
  public void testCreateMachine() {
    InputStream inputStream =
        RestServiceTest.class.getClassLoader().getResourceAsStream("createMachine.json");
    Response response = restService.createOrUpdateMachine(inputStream);

    Machine machine = machineArgumentCaptor.getValue();
    assertThat(response.getStatus(), is(200));
    assertThat(machine.getName(), is("Leg Row"));
    assertThat(machine.getPicture(), is("123123"));
    assertThat(machine.getQuantity(), is(2));
  }

  @Test
  public void testCreateMachineSqlException() throws Exception {
    InputStream inputStream =
        RestServiceTest.class.getClassLoader().getResourceAsStream("createMachine.json");
    when(machineHandler.addMachine(any(Machine.class))).thenThrow(SQLException.class);
    Response response = restService.createOrUpdateMachine(inputStream);
    assertThat(response.getStatus(), is(500));
  }

  @Test
  public void testCreateMachineBadJson() {
    InputStream inputStream =
        RestServiceTest.class.getClassLoader().getResourceAsStream("bad.json");
    Response response = restService.createOrUpdateMachine(inputStream);
    assertThat(response.getStatus(), is(500));
  }

  @Test
  public void testDeleteMachine() throws Exception {
    Response response = restService.deleteMachine("anId");
    assertThat(response.getStatus(), is(200));
    verify(machineHandler, times(1)).removeMachine(anyString());
  }

  @Test
  public void testDeleteMachineSqlException() throws Exception {
    when(machineHandler.removeMachine(anyString())).thenThrow(SQLException.class);
    Response response = restService.deleteMachine("anId");
    assertThat(response.getStatus(), is(500));
  }

  @Test
  public void testGetTrainer() {
    Response response = restService.getTrainers();
    assertThat(response, notNullValue());
    assertThat(response.getStatus(), is(200));
    List<Trainer> trainerList = (List<Trainer>) response.getEntity();
    assertThat(trainerList, hasSize(1));
    Trainer trainer = trainerList.get(0);
    assertThat(trainer.getFirstName(), is(FIRST_NAME));
    assertThat(trainer.getLastName(), is(LAST_NAME));
  }

  @Test
  public void testGetTrainerSqlException() throws Exception {
    when(trainerHandler.getTrainers()).thenThrow(SQLException.class);
    Response response = restService.getTrainers();
    assertThat(response, notNullValue());
    assertThat(response.getStatus(), is(500));
  }

  @Test
  public void testCreateTrainer() {
    InputStream inputStream =
        RestServiceTest.class.getClassLoader().getResourceAsStream("createTrainer.json");
    Response response = restService.createOrUpdateTrainer(inputStream);

    Trainer trainer = trainerArgumentCaptor.getValue();
    assertThat(response.getStatus(), is(200));
    assertThat(trainer.getFirstName(), is("Ben"));
    assertThat(trainer.getLastName(), is("Deininger"));
  }

  @Test
  public void testCreateTrainerSqlException() throws Exception {
    InputStream inputStream =
        RestServiceTest.class.getClassLoader().getResourceAsStream("createTrainer.json");
    when(trainerHandler.addTrainer(any(Trainer.class))).thenThrow(SQLException.class);
    Response response = restService.createOrUpdateTrainer(inputStream);
    assertThat(response.getStatus(), is(500));
  }

  @Test
  public void testCreateTrainerBadJson() {
    InputStream inputStream =
        RestServiceTest.class.getClassLoader().getResourceAsStream("bad.json");
    Response response = restService.createOrUpdateTrainer(inputStream);
    assertThat(response.getStatus(), is(500));
  }

  @Test
  public void testDeleteTrainer() throws Exception {
    Response response = restService.deleteTrainer("anId");
    assertThat(response.getStatus(), is(200));
    verify(trainerHandler, times(1)).removeTrainer(anyString());
  }

  @Test
  public void testDeleteTrainerSqlException() throws Exception {
    when(trainerHandler.removeTrainer(anyString())).thenThrow(SQLException.class);
    Response response = restService.deleteTrainer("anId");
    assertThat(response.getStatus(), is(500));
  }

  @Test
  public void testGetExercise() {
    Response response = restService.getExercises();
    assertThat(response, notNullValue());
    assertThat(response.getStatus(), is(200));
    List<Exercise> exerciseList = (List<Exercise>) response.getEntity();
    assertThat(exerciseList, hasSize(1));
    Exercise exercise = exerciseList.get(0);
    assertThat(exercise.getCommonName(), is(EXERCISE_NAME));
    assertThat(exercise.getSets(), is(12));
  }

  @Test
  public void testGetExerciseSqlException() throws Exception {
    when(exerciseHandler.getExercises()).thenThrow(SQLException.class);
    Response response = restService.getExercises();
    assertThat(response, notNullValue());
    assertThat(response.getStatus(), is(500));
  }

  @Test
  public void testCreateExercise() {
    InputStream inputStream =
        RestServiceTest.class.getClassLoader().getResourceAsStream("createExercise.json");
    Response response = restService.createOrUpdateExercise(inputStream);

    Exercise exercise = exerciseArgumentCaptor.getValue();
    assertThat(response.getStatus(), is(200));
    assertThat(exercise.getCommonName(), is("Leg Row"));
    assertThat(exercise.getSets(), is(2));
    assertThat(exercise.getDurationPerSet(), is(2));
  }

  @Test
  public void testCreateExerciseSqlException() throws Exception {
    InputStream inputStream =
        RestServiceTest.class.getClassLoader().getResourceAsStream("createExercise.json");
    when(exerciseHandler.addExercise(any(Exercise.class))).thenThrow(SQLException.class);
    Response response = restService.createOrUpdateExercise(inputStream);
    assertThat(response.getStatus(), is(500));
  }

  @Test
  public void testCreateExerciseBadJson() {
    InputStream inputStream =
        RestServiceTest.class.getClassLoader().getResourceAsStream("bad.json");
    Response response = restService.createOrUpdateExercise(inputStream);
    assertThat(response.getStatus(), is(500));
  }

  @Test
  public void testDeleteExercise() throws Exception {
    Response response = restService.deleteExercise("anId");
    assertThat(response.getStatus(), is(200));
    verify(exerciseHandler, times(1)).removeExercise(anyString());
  }

  @Test
  public void testDeleteExerciseSqlException() throws Exception {
    when(exerciseHandler.removeExercise(anyString())).thenThrow(SQLException.class);
    Response response = restService.deleteExercise("anId");
    assertThat(response.getStatus(), is(500));
  }

  @Test
  public void testGetWorkoutRoutine() {
    Response response = restService.getWorkoutRoutines();
    assertThat(response, notNullValue());
    assertThat(response.getStatus(), is(200));
    List<WorkoutRoutine> workoutRoutineList = (List<WorkoutRoutine>) response.getEntity();
    assertThat(workoutRoutineList, hasSize(1));
    WorkoutRoutine workoutRoutine = workoutRoutineList.get(0);
    assertThat(workoutRoutine.getName(), is(ROUTINE_NAME));
  }

  @Test
  public void testGetWorkoutRoutineSqlException() throws Exception {
    when(workoutRoutineHandler.getWorkoutRoutines()).thenThrow(SQLException.class);
    Response response = restService.getWorkoutRoutines();
    assertThat(response, notNullValue());
    assertThat(response.getStatus(), is(500));
  }

  @Test
  public void testCreateWorkoutRoutine() {
    InputStream inputStream =
        RestServiceTest.class.getClassLoader().getResourceAsStream("createWorkoutRoutine.json");
    Response response = restService.createOrUpdateWorkoutRoutine(inputStream);

    WorkoutRoutine workoutRoutine = workoutRoutineArgumentCaptor.getValue();
    assertThat(response.getStatus(), is(200));
    assertThat(workoutRoutine.getName(), is("Leg Row"));
    assertThat(
        workoutRoutine.getExerciseIds(),
        is(
            Arrays.asList(
                "dd863f12-a283-4bbe-9afa-41103615d61a", "e188d22b-40f3-4f61-ab1b-680da3cba778")));
  }

  @Test
  public void testCreateWorkoutRoutineSqlException() throws Exception {
    InputStream inputStream =
        RestServiceTest.class.getClassLoader().getResourceAsStream("createWorkoutRoutine.json");
    when(workoutRoutineHandler.addWorkoutRoutine(any(WorkoutRoutine.class)))
        .thenThrow(SQLException.class);
    Response response = restService.createOrUpdateWorkoutRoutine(inputStream);
    assertThat(response.getStatus(), is(500));
  }

  @Test
  public void testCreateWorkoutRoutineBadJson() {
    InputStream inputStream =
        RestServiceTest.class.getClassLoader().getResourceAsStream("bad.json");
    Response response = restService.createOrUpdateWorkoutRoutine(inputStream);
    assertThat(response.getStatus(), is(500));
  }

  @Test
  public void testDeleteWorkoutRoutine() throws Exception {
    Response response = restService.deleteRoutine("anId");
    assertThat(response.getStatus(), is(200));
    verify(workoutRoutineHandler, times(1)).removeWorkoutRoutine(anyString());
  }

  @Test
  public void testDeleteWorkoutRoutineSqlException() throws Exception {
    when(workoutRoutineHandler.removeWorkoutRoutine(anyString())).thenThrow(SQLException.class);
    Response response = restService.deleteRoutine("anId");
    assertThat(response.getStatus(), is(500));
  }

  private CustomerHandler setUpMocks() throws Exception {
    customerHandler = mock(CustomerHandler.class);
    trainerHandler = mock(TrainerHandler.class);
    exerciseHandler = mock(ExerciseHandler.class);
    workoutRoutineHandler = mock(WorkoutRoutineHandler.class);
    machineHandler = mock(MachineHandler.class);
    when(customerHandler.getCustomers()).thenReturn(Collections.singletonList(generateCustomer()));
    when(trainerHandler.getTrainers()).thenReturn(Collections.singletonList(generateTrainer()));
    when(machineHandler.getMachines()).thenReturn(Collections.singletonList(generateMachine()));
    when(workoutRoutineHandler.getWorkoutRoutines())
        .thenReturn(Collections.singletonList(generateWorkoutRoutine()));
    when(exerciseHandler.getExercises()).thenReturn(Collections.singletonList(generateExercise()));

    when(customerHandler.removeCustomer(anyString())).thenReturn(true);
    when(machineHandler.removeMachine(anyString())).thenReturn(true);
    when(trainerHandler.removeTrainer(anyString())).thenReturn(true);
    when(exerciseHandler.removeExercise(anyString())).thenReturn(true);
    when(workoutRoutineHandler.removeWorkoutRoutine(anyString())).thenReturn(true);

    when(trainerHandler.addTrainer(trainerArgumentCaptor.capture())).thenReturn(true);
    when(machineHandler.addMachine(machineArgumentCaptor.capture())).thenReturn(true);
    when(customerHandler.addCustomer(customerArgumentCaptor.capture())).thenReturn(true);
    when(exerciseHandler.addExercise(exerciseArgumentCaptor.capture())).thenReturn(true);
    when(workoutRoutineHandler.addWorkoutRoutine(workoutRoutineArgumentCaptor.capture()))
        .thenReturn(true);
    return customerHandler;
  }

  private Machine generateMachine() {
    return new MachineImpl(UUID.randomUUID().toString(), MACHINE_NAME, "1234", QUANTITY);
  }

  private Customer generateCustomer() {
    return new CustomerImpl(
        UUID.randomUUID().toString(),
        ADDRESS,
        FIRST_NAME,
        LAST_NAME,
        PHONE,
        EMAIL,
        HEALTH_INSURANCE_PROVIDER,
        ID_LIST,
        ACTIVITY);
  }

  private Trainer generateTrainer() {
    return new TrainerImpl(
        UUID.randomUUID().toString(),
        ADDRESS,
        FIRST_NAME,
        LAST_NAME,
        PHONE,
        EMAIL,
        HEALTH_INSURANCE_PROVIDER,
        WORK_HOURS,
        QUALIFICATIONS);
  }

  private WorkoutRoutine generateWorkoutRoutine() {
    return new WorkoutRoutineImpl(UUID.randomUUID().toString(), ROUTINE_NAME, ID_LIST);
  }

  private Exercise generateExercise() {
    return new ExerciseImpl(
        UUID.randomUUID().toString(), EXERCISE_NAME, UUID.randomUUID().toString(), 12, 12);
  }
}
