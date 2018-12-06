package edu.colostate.cs.cs414.p3.bdeining.sql;

import static edu.colostate.cs.cs414.p3.bdeining.sql.HandlerUtils.createTable;
import static edu.colostate.cs.cs414.p3.bdeining.sql.HandlerUtils.getExistingTables;
import static edu.colostate.cs.cs414.p3.bdeining.sql.HandlerUtils.getResultSetById;
import static edu.colostate.cs.cs414.p3.bdeining.sql.TableConstants.EXERCISE_WORKOUT_ROUTINE_TABLE_DEF;
import static edu.colostate.cs.cs414.p3.bdeining.sql.TableConstants.EXERCISE_WORKOUT_ROUTINE_TABLE_NAME;
import static edu.colostate.cs.cs414.p3.bdeining.sql.TableConstants.WORKOUT_ROUTINE_TABLE_DEF;
import static edu.colostate.cs.cs414.p3.bdeining.sql.TableConstants.WORKOUT_ROUTINE_TABLE_NAME;

import edu.colostate.cs.cs414.p3.bdeining.api.WorkoutRoutine;
import edu.colostate.cs.cs414.p3.bdeining.api.handlers.WorkoutRoutineHandler;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.sql.DataSource;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(
  immediate = true, //
  name = "WorkoutRoutineHandlerImpl", //
  property = { //
    "service.exported.interfaces=*", //
  }
)
public class WorkoutRoutineHandlerImpl implements WorkoutRoutineHandler {
  private static final Logger LOGGER = LoggerFactory.getLogger(CustomerHandlerImpl.class);

  private DataSource dataSource;

  /**
   * Sets the data source for the class; this is a reference to the data source that is registered
   * as a service in OSGi
   *
   * @param dataSource the given data source
   */
  @Reference
  public void setDataSource(DataSource dataSource) {
    this.dataSource = dataSource;
    init();
  }

  /** Called when the class is instantiated. */
  public void init() {
    LOGGER.trace("Initializing {}", CustomerHandlerImpl.class.getName());
    createTablesIfNonExistent();
  }

  /** Creates the tables that this handler uses if they have not been added in data store. */
  private void createTablesIfNonExistent() {
    List<String> tables = getExistingTables(dataSource);
    LOGGER.trace("Existing tables : {}", tables);

    if (!tables.contains(WORKOUT_ROUTINE_TABLE_NAME)) {
      createTable(dataSource, WORKOUT_ROUTINE_TABLE_NAME, WORKOUT_ROUTINE_TABLE_DEF);
    }

    if (!tables.contains(EXERCISE_WORKOUT_ROUTINE_TABLE_NAME)) {
      createTable(
          dataSource, EXERCISE_WORKOUT_ROUTINE_TABLE_NAME, EXERCISE_WORKOUT_ROUTINE_TABLE_DEF);
    }
  }

  /** {@inheritDoc} */
  @Override
  public boolean addWorkoutRoutine(WorkoutRoutine workoutRoutine) throws SQLException {
    String name = workoutRoutine.getName();
    String id = workoutRoutine.getId();
    List<String> workoutRoutineExerciseIds = workoutRoutine.getExerciseIds();
    String branch = workoutRoutine.getBranch();

    WorkoutRoutine existingCustomer = getWorkoutRoutineById(id);
    if (existingCustomer != null) {
      LOGGER.trace("Updating Customer : ID {}", id);

      try (Connection con = dataSource.getConnection()) {
        LOGGER.trace("Adding exercise : {}", existingCustomer);

        PreparedStatement update =
            con.prepareStatement(
                "update " + WORKOUT_ROUTINE_TABLE_NAME + " SET name=?, branch=? WHERE id=?");

        update.setString(1, name);
        update.setString(2, branch);
        update.setString(3, id);
        update.execute();
        update.close();

        HandlerUtils.removeById(
            dataSource, "workoutRoutineId", id, EXERCISE_WORKOUT_ROUTINE_TABLE_NAME);

        for (String exerciseId : workoutRoutineExerciseIds) {

          PreparedStatement updateExercise =
              con.prepareStatement(
                  "INSERT INTO "
                      + EXERCISE_WORKOUT_ROUTINE_TABLE_NAME
                      + " (workoutRoutineId, exerciseId) VALUES (?,?)");
          updateExercise.setString(1, id);
          updateExercise.setString(2, exerciseId);
          updateExercise.execute();
          updateExercise.close();
        }
      }

    } else {

      try (Connection con = dataSource.getConnection()) {
        LOGGER.trace("Adding workoutRoutine : {}", workoutRoutine);

        PreparedStatement insert =
            con.prepareStatement(
                "INSERT INTO " + WORKOUT_ROUTINE_TABLE_NAME + " (name, id, branch) VALUES (?,?,?)");
        insert.setString(1, name);
        insert.setString(2, id);
        insert.setString(3, branch);
        insert.execute();
        insert.close();

        for (String exerciseId : workoutRoutineExerciseIds) {
          insert =
              con.prepareStatement(
                  "INSERT INTO "
                      + EXERCISE_WORKOUT_ROUTINE_TABLE_NAME
                      + " (workoutRoutineId, exerciseId) VALUES (?,?)");
          insert.setString(1, id);
          insert.setString(2, exerciseId);
          insert.execute();
          insert.close();
        }
      }
    }

    return true;
  }

  /** {@inheritDoc} */
  @Override
  public List<WorkoutRoutine> getWorkoutRoutines(String branch) throws SQLException {
    try (Connection con = dataSource.getConnection()) {

      PreparedStatement preparedStatement =
          con.prepareStatement("SELECT * FROM " + WORKOUT_ROUTINE_TABLE_NAME + " where branch=?");
      preparedStatement.setString(1, branch);
      ResultSet resultSet = preparedStatement.executeQuery();

      if (resultSet == null) {
        preparedStatement.close();
        return Collections.emptyList();
      }

      List<WorkoutRoutine> workoutRoutineList = new ArrayList<>();
      while (resultSet.next()) {
        WorkoutRoutine workoutRoutine = Factory.createWorkoutRoutine(dataSource, resultSet);
        if (workoutRoutine != null) {
          workoutRoutineList.add(workoutRoutine);
        }
      }
      preparedStatement.close();
      return workoutRoutineList;
    }
  }

  /** {@inheritDoc} */
  @Override
  public boolean removeWorkoutRoutine(String id) throws SQLException {
    HandlerUtils.removeById(dataSource, id, WORKOUT_ROUTINE_TABLE_NAME);
    try (Connection con = dataSource.getConnection()) {
      LOGGER.trace("Removing from table {} : {}", EXERCISE_WORKOUT_ROUTINE_TABLE_NAME, id);

      PreparedStatement preparedStatement =
          con.prepareStatement(
              "DELETE FROM "
                  + EXERCISE_WORKOUT_ROUTINE_TABLE_NAME
                  + " WHERE workoutRoutineId = ?;");
      preparedStatement.setString(1, id);
      preparedStatement.execute();
      return true;
    }
  }

  /**
   * Gets a {@link WorkoutRoutine} by the given id
   *
   * @param id the given id
   * @return a workoutroutine, null if none is found
   * @throws SQLException when a database error occurs
   */
  private WorkoutRoutine getWorkoutRoutineById(String id) throws SQLException {
    try (Connection con = dataSource.getConnection()) {
      PreparedStatement preparedStatement =
          con.prepareStatement("SELECT * FROM " + WORKOUT_ROUTINE_TABLE_NAME + " where id=?");

      ResultSet resultSet = getResultSetById(preparedStatement, id);
      if (resultSet == null) {
        return null;
      }

      while (resultSet.next()) {
        WorkoutRoutine workoutRoutine = Factory.createWorkoutRoutine(dataSource, resultSet);
        if (workoutRoutine != null) {
          return workoutRoutine;
        }
      }
      preparedStatement.close();
    }
    return null;
  }
}
