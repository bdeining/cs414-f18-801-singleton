package edu.colostate.cs.cs414.p3.bdeining.sql;

import static edu.colostate.cs.cs414.p3.bdeining.sql.HandlerUtils.createTable;
import static edu.colostate.cs.cs414.p3.bdeining.sql.HandlerUtils.getExistingTables;
import static edu.colostate.cs.cs414.p3.bdeining.sql.HandlerUtils.getResultSetById;
import static edu.colostate.cs.cs414.p3.bdeining.sql.TableConstants.QUALIFICATION_TABLE_DEF;
import static edu.colostate.cs.cs414.p3.bdeining.sql.TableConstants.QUALIFICATION_TABLE_NAME;
import static edu.colostate.cs.cs414.p3.bdeining.sql.TableConstants.TRAINER_TABLE_DEF;
import static edu.colostate.cs.cs414.p3.bdeining.sql.TableConstants.TRAINER_TABLE_NAME;

import edu.colostate.cs.cs414.p3.bdeining.api.Trainer;
import edu.colostate.cs.cs414.p3.bdeining.api.handlers.TrainerHandler;
import edu.colostate.cs.cs414.p3.bdeining.impl.TrainerImpl;
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
  name = "TrainerHandlerImpl", //
  property = { //
    "service.exported.interfaces=*", //
  }
)
public class TrainerHandlerImpl implements TrainerHandler {

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

    if (!tables.contains(TRAINER_TABLE_NAME)) {
      createTable(dataSource, TRAINER_TABLE_NAME, TRAINER_TABLE_DEF);
    }

    if (!tables.contains(QUALIFICATION_TABLE_NAME)) {
      createTable(dataSource, QUALIFICATION_TABLE_NAME, QUALIFICATION_TABLE_DEF);
    }
  }

  /** {@inheritDoc} */
  @Override
  public boolean addTrainer(Trainer trainer) throws SQLException {
    String address = trainer.getAddress();
    String firstName = trainer.getFirstName();
    String lastName = trainer.getLastName();
    String phone = trainer.getPhone();
    String healthInsuranceProvider = trainer.getHealthInsuranceProvider();
    String email = trainer.getEmail();
    String id = trainer.getId();
    String password = trainer.getPassword();
    int workHours = trainer.getWorkHours();
    String branch = trainer.getBranch();
    List<String> qualifications = trainer.getQualifications();

    Trainer existingTrainer = getTrainerById(id);
    if (existingTrainer != null) {
      LOGGER.trace("Updating Customer : ID {}", id);

      try (Connection con = dataSource.getConnection()) {
        LOGGER.trace("Adding customer : {}", existingTrainer);

        PreparedStatement update =
            con.prepareStatement(
                "update "
                    + TRAINER_TABLE_NAME
                    + " SET first_name=?, last_name=?, address=?, phone=?, email=?, health_insurance_provider=?, work_hours=?, password=?, branch=? WHERE id=?");

        update.setString(1, firstName);
        update.setString(2, lastName);
        update.setString(3, address);
        update.setString(4, phone);
        update.setString(5, email);
        update.setString(6, healthInsuranceProvider);
        update.setInt(7, workHours);
        update.setString(8, password);
        update.setString(9, branch);
        update.setString(10, id);
        update.execute();
        update.close();
      }

      HandlerUtils.removeById(dataSource, "id", id, QUALIFICATION_TABLE_NAME);

      for (String qualification : qualifications) {

        try (Connection con = dataSource.getConnection()) {
          LOGGER.trace("Adding customer : {}", qualification);
          PreparedStatement insert =
              con.prepareStatement(
                  "INSERT INTO " + QUALIFICATION_TABLE_NAME + " (id, qualification) VALUES (?,?)");
          insert.setString(1, id);
          insert.setString(2, qualification);
          insert.execute();
          insert.close();
        }
      }

    } else {

      try (Connection con = dataSource.getConnection()) {
        LOGGER.trace("Adding trainer : {}", trainer);

        PreparedStatement update =
            con.prepareStatement(
                "INSERT INTO "
                    + TRAINER_TABLE_NAME
                    + " (first_name, last_name, address, phone, email, id, health_insurance_provider, work_hours, password, branch) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);");

        update.setString(1, firstName);
        update.setString(2, lastName);
        update.setString(3, address);
        update.setString(4, phone);
        update.setString(5, email);
        update.setString(6, id);
        update.setString(7, healthInsuranceProvider);
        update.setInt(8, workHours);
        update.setString(9, password);
        update.setString(10, branch);
        update.execute();
        update.close();

        for (String qualification : qualifications) {
          addQualification(qualification, id);
        }
      }
    }
    return true;
  }

  /** {@inheritDoc} */
  @Override
  public boolean removeTrainer(String trainerId) throws SQLException {
    HandlerUtils.removeById(dataSource, trainerId, TRAINER_TABLE_NAME);
    HandlerUtils.removeById(dataSource, trainerId, QUALIFICATION_TABLE_NAME);
    return true;
  }

  /** {@inheritDoc} */
  @Override
  public List<Trainer> getTrainers() throws SQLException {
    try (Connection con = dataSource.getConnection()) {

      PreparedStatement preparedStatement =
          con.prepareStatement("SELECT * FROM " + TRAINER_TABLE_NAME);

      ResultSet resultSet = preparedStatement.executeQuery();

      if (resultSet == null) {
        preparedStatement.close();
        return Collections.emptyList();
      }

      List<Trainer> trainers = new ArrayList<>();
      while (resultSet.next()) {
        Trainer trainer = getTrainer(resultSet);
        if (trainer != null) {
          trainers.add(trainer);
        }
      }
      preparedStatement.close();
      return trainers;
    }
  }

  /**
   * Converts a {@link ResultSet} into a {@link Trainer}.
   *
   * @param resultSet the given result set
   * @return the trainer converted, null otherwise
   */
  private Trainer getTrainer(ResultSet resultSet) {
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
      List<String> qualifications = getQualificationsForTrainer(id);
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
   * Gets a trainer by the given id
   *
   * @param id the given id
   * @return the {@link Trainer}, or null if none was found
   * @throws SQLException
   */
  private Trainer getTrainerById(String id) throws SQLException {
    try (Connection con = dataSource.getConnection()) {
      PreparedStatement preparedStatement =
          con.prepareStatement("SELECT * FROM " + TRAINER_TABLE_NAME + " where id=?");

      ResultSet resultSet = getResultSetById(preparedStatement, id);
      if (resultSet == null) {
        return null;
      }

      while (resultSet.next()) {
        Trainer trainer = getTrainer(resultSet);
        if (trainer != null) {
          return trainer;
        }
      }
      preparedStatement.close();
    }
    return null;
  }

  /**
   * Adds a qualification to the Qualification table with the given parameters.
   *
   * @param qualification the qualification
   * @param trainerId the id of the trainer
   * @throws SQLException when a database error occurs
   */
  private void addQualification(String qualification, String trainerId) throws SQLException {
    try (Connection con = dataSource.getConnection()) {
      PreparedStatement preparedStatement =
          con.prepareStatement(
              "INSERT INTO " + QUALIFICATION_TABLE_NAME + " (id, qualification) VALUES (?, ?);");
      preparedStatement.setString(1, trainerId);
      LOGGER.trace("Adding qualification {} to trainer with id {}", qualification, trainerId);
      preparedStatement.setString(2, qualification);
      preparedStatement.execute();
    }
  }

  /**
   * Gets the lists of qualifications for the given trainer's id.
   *
   * @param id the given id
   * @return the list of qualifications, or an empty list if the trainer has none
   */
  private List<String> getQualificationsForTrainer(String id) {
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
}
