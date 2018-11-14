package edu.colostate.cs.cs414.p3.bdeining.sql;

import static edu.colostate.cs.cs414.p3.bdeining.sql.HandlerUtils.createTable;
import static edu.colostate.cs.cs414.p3.bdeining.sql.HandlerUtils.getExistingTables;
import static edu.colostate.cs.cs414.p3.bdeining.sql.TableConstants.EXERCISE_TABLE_DEF;
import static edu.colostate.cs.cs414.p3.bdeining.sql.TableConstants.EXERCISE_TABLE_NAME;

import edu.colostate.cs.cs414.p3.bdeining.api.Exercise;
import edu.colostate.cs.cs414.p3.bdeining.api.handlers.ExerciseHandler;
import edu.colostate.cs.cs414.p3.bdeining.impl.ExerciseImpl;
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
  name = "ExerciseHandlerImpl", //
  property = { //
    "service.exported.interfaces=*", //
  }
)
public class ExerciseHandlerImpl implements ExerciseHandler {
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

    if (!tables.contains(EXERCISE_TABLE_NAME)) {
      createTable(dataSource, EXERCISE_TABLE_NAME, EXERCISE_TABLE_DEF);
    }
  }

  @Override
  public boolean addExercise(Exercise exercise) throws SQLException {
    String id = exercise.getId();
    String name = exercise.getCommonName();
    int duration = exercise.getDurationPerSet();
    int sets = exercise.getSets();
    String machineId = exercise.getMachineId();

    Exercise existingCustomer = getExerciseById(id);
    if (existingCustomer != null) {
      LOGGER.trace("Updating Customer : ID {}", id);

      try (Connection con = dataSource.getConnection()) {
        LOGGER.trace("Adding exercise : {}", existingCustomer);

        PreparedStatement update =
            con.prepareStatement(
                "update "
                    + EXERCISE_TABLE_NAME
                    + " SET name=?, duration=?, sets=?, machineId=? WHERE id=?");

        update.setString(1, name);
        update.setInt(2, duration);
        update.setInt(3, sets);
        update.setString(4, machineId);
        update.setString(5, id);
        update.execute();
        update.close();
      }

    } else {

      try (Connection con = dataSource.getConnection()) {
        LOGGER.trace("Adding Exercise : {}", exercise);
        PreparedStatement insert =
            con.prepareStatement(
                "INSERT INTO "
                    + EXERCISE_TABLE_NAME
                    + " (name, id, machineId, sets, duration) VALUES (?,?,?,?,?)");
        insert.setString(1, name);
        insert.setString(2, id);
        insert.setString(3, machineId);
        insert.setInt(4, sets);
        insert.setInt(5, duration);
        insert.execute();
        insert.close();
      }
    }
    return true;
  }

  @Override
  public List<Exercise> getExercises() throws SQLException {
    try (Connection con = dataSource.getConnection()) {

      PreparedStatement preparedStatement =
          con.prepareStatement("SELECT * FROM " + EXERCISE_TABLE_NAME);

      ResultSet resultSet = preparedStatement.executeQuery();

      if (resultSet == null) {
        preparedStatement.close();
        return Collections.emptyList();
      }

      List<Exercise> exerciseList = new ArrayList<>();
      while (resultSet.next()) {
        Exercise exercise = getExercise(resultSet);
        if (exercise != null) {
          exerciseList.add(exercise);
        }
      }

      preparedStatement.close();
      return exerciseList;
    }
  }

  @Override
  public boolean removeExercise(String id) throws SQLException {
    HandlerUtils.removeById(dataSource, id, EXERCISE_TABLE_NAME);
    return true;
  }

  private Exercise getExerciseById(String id) throws SQLException {
    try (Connection con = dataSource.getConnection()) {
      PreparedStatement preparedStatement =
          con.prepareStatement("SELECT * FROM " + EXERCISE_TABLE_NAME + " where id=?");

      preparedStatement.setString(1, id);
      ResultSet resultSet = preparedStatement.executeQuery();

      if (resultSet == null) {
        preparedStatement.close();
        return null;
      }

      while (resultSet.next()) {
        Exercise exercise = getExercise(resultSet);
        if (exercise != null) {
          preparedStatement.close();
          return exercise;
        }
      }
      preparedStatement.close();
      return null;
    }
  }

  private Exercise getExercise(ResultSet resultSet) {
    try {
      String id = resultSet.getString("id");
      String name = resultSet.getString("name");
      String machineId = resultSet.getString("machineId");
      int sets = resultSet.getInt("sets");
      int duration = resultSet.getInt("duration");

      return new ExerciseImpl(id, name, machineId, sets, duration);
    } catch (SQLException e) {
      LOGGER.error("No data", e);
      return null;
    }
  }
}
