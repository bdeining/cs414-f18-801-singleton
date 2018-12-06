package edu.colostate.cs.cs414.p3.bdeining.sql;

import static edu.colostate.cs.cs414.p3.bdeining.sql.TableConstants.CUSTOMER_WORKOUT_ROUTINE_TABLE_NAME;
import static edu.colostate.cs.cs414.p3.bdeining.sql.TableConstants.EXERCISE_WORKOUT_ROUTINE_TABLE_NAME;
import static edu.colostate.cs.cs414.p3.bdeining.sql.TableConstants.QUALIFICATION_TABLE_NAME;

import edu.colostate.cs.cs414.p3.bdeining.api.Activity;
import edu.colostate.cs.cs414.p3.bdeining.api.Customer;
import edu.colostate.cs.cs414.p3.bdeining.api.Exercise;
import edu.colostate.cs.cs414.p3.bdeining.api.Machine;
import edu.colostate.cs.cs414.p3.bdeining.api.Trainer;
import edu.colostate.cs.cs414.p3.bdeining.api.WorkoutRoutine;
import edu.colostate.cs.cs414.p3.bdeining.impl.CustomerImpl;
import edu.colostate.cs.cs414.p3.bdeining.impl.ExerciseImpl;
import edu.colostate.cs.cs414.p3.bdeining.impl.MachineImpl;
import edu.colostate.cs.cs414.p3.bdeining.impl.TrainerImpl;
import edu.colostate.cs.cs414.p3.bdeining.impl.WorkoutRoutineImpl;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Factory {

  private static final Logger LOGGER = LoggerFactory.getLogger(CustomerHandlerImpl.class);

  private Factory() {}

  /**
   * Converts a result set into a {@link Customer} object
   *
   * @param resultSet the result set from the data source
   * @return customer object read from the data source
   */
  public static Customer createCustomer(DataSource dataSource, ResultSet resultSet) {
    try {
      String firstName = resultSet.getString("first_name");
      String lastName = resultSet.getString("last_name");
      String address = resultSet.getString("address");
      String phone = resultSet.getString("phone");
      String email = resultSet.getString("email");
      String id = resultSet.getString("id");
      String branch = resultSet.getString("branch");
      String healthInsuranceProvider = resultSet.getString("health_insurance_provider");
      Activity activity = Activity.valueOf(resultSet.getString("activity"));

      List<String> workoutRoutines = new ArrayList<>();
      try (Connection con = dataSource.getConnection()) {
        PreparedStatement insert =
            con.prepareCall(
                "SELECT * FROM " + CUSTOMER_WORKOUT_ROUTINE_TABLE_NAME + " WHERE CUSTOMERID = ?");
        insert.setString(1, id);
        ResultSet resultSetWorkouts = insert.executeQuery();
        while (resultSetWorkouts.next()) {
          workoutRoutines.add(resultSetWorkouts.getString("WORKOUTROUTINEID"));
        }
        insert.close();
      }

      Customer customer =
          new CustomerImpl(
              id,
              address,
              firstName,
              lastName,
              phone,
              email,
              healthInsuranceProvider,
              branch,
              workoutRoutines,
              activity);

      LOGGER.trace("Got customer {}", customer);
      return customer;
    } catch (SQLException e) {
      LOGGER.error("No data", e);
      return null;
    }
  }

  /**
   * Converts a {@link ResultSet} into an {@link Exercise}
   *
   * @param resultSet the given result set
   * @return an {@link Exercise}, or null if none was found
   */
  public static Exercise createExercise(ResultSet resultSet) {
    try {
      String id = resultSet.getString("id");
      String name = resultSet.getString("name");
      String machineId = resultSet.getString("machineId");
      String branch = resultSet.getString("branch");
      int sets = resultSet.getInt("sets");
      int duration = resultSet.getInt("duration");

      return new ExerciseImpl(id, name, machineId, sets, duration, branch);
    } catch (SQLException e) {
      LOGGER.error("No data", e);
      return null;
    }
  }

  /**
   * Converts a {@link ResultSet} into a {@link Machine}
   *
   * @param resultSet the given result set
   * @return the machine, or null if the conversion fails
   */
  public static Machine createMachine(ResultSet resultSet) {
    try {
      String id = resultSet.getString("id");
      String name = resultSet.getString("name");
      String picture = resultSet.getString("picture");
      int quantity = resultSet.getInt("quantity");
      String branch = resultSet.getString("branch");

      Machine machine = new MachineImpl(id, name, picture, quantity, branch);
      LOGGER.trace("Got machine {}", machine);
      return machine;
    } catch (SQLException e) {
      LOGGER.error("No data", e);
      return null;
    }
  }

  /**
   * Converts a {@link ResultSet} into a {@link Trainer}.
   *
   * @param resultSet the given result set
   * @return the trainer converted, null otherwise
   */
  public static Trainer createTrainer(DataSource dataSource, ResultSet resultSet) {
    try {
      String firstName = resultSet.getString("first_name");
      String lastName = resultSet.getString("last_name");
      String address = resultSet.getString("address");
      String phone = resultSet.getString("phone");
      String email = resultSet.getString("email");
      String branch = resultSet.getString("branch");
      String id = resultSet.getString("id");
      String healthInsuranceProvider = resultSet.getString("health_insurance_provider");
      String password = resultSet.getString("password");
      int workHours = resultSet.getInt("work_hours");
      List<String> qualifications = getQualificationsForTrainer(dataSource, id);
      Trainer trainer =
          new TrainerImpl(
              id,
              address,
              firstName,
              lastName,
              phone,
              email,
              healthInsuranceProvider,
              branch,
              workHours,
              qualifications,
              password);
      LOGGER.trace("Got trainer {}", trainer);
      return trainer;
    } catch (SQLException e) {
      LOGGER.error("No data", e);
      return null;
    }
  }

  /**
   * Gets the lists of qualifications for the given trainer's id.
   *
   * @param id the given id
   * @return the list of qualifications, or an empty list if the trainer has none
   */
  private static List<String> getQualificationsForTrainer(DataSource dataSource, String id) {
    try (Connection con = dataSource.getConnection()) {

      PreparedStatement preparedStatement =
          con.prepareStatement("SELECT * from " + QUALIFICATION_TABLE_NAME + " WHERE ID = ?;");

      preparedStatement.setString(1, id);

      ResultSet resultSet = preparedStatement.executeQuery();

      List<String> qualifications = new ArrayList<>();
      while (resultSet.next()) {
        String qualificaiton = resultSet.getString("qualification");
        qualifications.add(qualificaiton);
      }

      preparedStatement.close();
      return qualifications;
    } catch (SQLException e) {
      LOGGER.error("Could not remove trainer {}", id, e);
    }

    return Collections.emptyList();
  }

  public static WorkoutRoutine createWorkoutRoutine(DataSource dataSource, ResultSet resultSet) {
    try (Connection con = dataSource.getConnection()) {
      String id = resultSet.getString("id");
      String name = resultSet.getString("name");
      String branch = resultSet.getString("branch");

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
      return new WorkoutRoutineImpl(id, name, exerciseIds, branch);
    } catch (SQLException e) {
      LOGGER.debug("Could not get routine", e);
      return null;
    }
  }
}
