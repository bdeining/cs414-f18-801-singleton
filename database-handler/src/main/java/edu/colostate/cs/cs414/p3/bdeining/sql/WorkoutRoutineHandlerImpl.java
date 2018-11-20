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
import edu.colostate.cs.cs414.p3.bdeining.impl.WorkoutRoutineImpl;
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

  @Reference
  public void setDataSource(DataSource dataSource) {
    this.dataSource = dataSource;
    init();
  }

  public void init() {
    LOGGER.trace("Initializing {}", CustomerHandlerImpl.class.getName());
    createTablesIfNonExistent();
  }

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

  @Override
  public boolean addWorkoutRoutine(WorkoutRoutine workoutRoutine) throws SQLException {
    String name = workoutRoutine.getName();
    String id = workoutRoutine.getId();
    List<String> workoutRoutineExerciseIds = workoutRoutine.getExerciseIds();

    WorkoutRoutine existingCustomer = getWorkoutRoutineById(id);
    if (existingCustomer != null) {
      LOGGER.trace("Updating Customer : ID {}", id);

      try (Connection con = dataSource.getConnection()) {
        LOGGER.trace("Adding exercise : {}", existingCustomer);

        PreparedStatement update =
            con.prepareStatement("update " + WORKOUT_ROUTINE_TABLE_NAME + " SET name=? WHERE id=?");

        update.setString(1, name);
        update.setString(2, id);
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
                "INSERT INTO " + WORKOUT_ROUTINE_TABLE_NAME + " (name, id) VALUES (?,?)");
        insert.setString(1, name);
        insert.setString(2, id);
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

  @Override
  public List<WorkoutRoutine> getWorkoutRoutines() throws SQLException {
    try (Connection con = dataSource.getConnection()) {

      PreparedStatement preparedStatement =
          con.prepareStatement("SELECT * FROM " + WORKOUT_ROUTINE_TABLE_NAME);
      ResultSet resultSet = preparedStatement.executeQuery();

      if (resultSet == null) {
        preparedStatement.close();
        return Collections.emptyList();
      }

      List<WorkoutRoutine> workoutRoutineList = new ArrayList<>();
      while (resultSet.next()) {
        WorkoutRoutine workoutRoutine = getWorkoutRoutine(resultSet);
        if (workoutRoutine != null) {
          workoutRoutineList.add(workoutRoutine);
        }
      }
      preparedStatement.close();
      return workoutRoutineList;
    }
  }

  private WorkoutRoutine getWorkoutRoutine(ResultSet resultSet) {
    try (Connection con = dataSource.getConnection()) {
      String id = resultSet.getString("id");
      String name = resultSet.getString("name");

      PreparedStatement preparedStatement =
          con.prepareStatement(
              "SELECT * FROM "
                  + EXERCISE_WORKOUT_ROUTINE_TABLE_NAME
                  + " WHERE workoutRoutineId = ?;");
      preparedStatement.setString(1, id);

      ResultSet exerciseResultSet = preparedStatement.executeQuery();

      if (exerciseResultSet == null) {
        preparedStatement.close();
        return null;
      }

      List<String> exerciseIds = new ArrayList<>();
      while (exerciseResultSet.next()) {
        exerciseIds.add(exerciseResultSet.getString("exerciseId"));
      }

      preparedStatement.close();
      return new WorkoutRoutineImpl(id, name, exerciseIds);
    } catch (SQLException e) {
      LOGGER.debug("Could not get routine", e);
      return null;
    }
  }

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

  private WorkoutRoutine getWorkoutRoutineById(String id) throws SQLException {
    try (Connection con = dataSource.getConnection()) {
      PreparedStatement preparedStatement =
          con.prepareStatement("SELECT * FROM " + WORKOUT_ROUTINE_TABLE_NAME + " where id=?");

      ResultSet resultSet = getResultSetById(preparedStatement, id);
      if (resultSet == null) {
        return null;
      }

      while (resultSet.next()) {
        WorkoutRoutine workoutRoutine = getWorkoutRoutine(resultSet);
        if (workoutRoutine != null) {
          return workoutRoutine;
        }
      }
      preparedStatement.close();
    }
    return null;
  }
}
