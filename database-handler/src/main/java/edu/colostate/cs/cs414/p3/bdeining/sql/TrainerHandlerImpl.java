package edu.colostate.cs.cs414.p3.bdeining.sql;

import static edu.colostate.cs.cs414.p3.bdeining.sql.TableConstants.QUALIFICATION_TABLE_NAME;
import static edu.colostate.cs.cs414.p3.bdeining.sql.TableConstants.TABLES;
import static edu.colostate.cs.cs414.p3.bdeining.sql.TableConstants.TABLES_DEF;
import static edu.colostate.cs.cs414.p3.bdeining.sql.TableConstants.TRAINER_TABLE_NAME;

import edu.colostate.cs.cs414.p3.bdeining.api.Trainer;
import edu.colostate.cs.cs414.p3.bdeining.api.handlers.TrainerHandler;
import edu.colostate.cs.cs414.p3.bdeining.impl.TrainerImpl;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
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

  @Reference
  public void setDataSource(DataSource dataSource) {
    this.dataSource = dataSource;
    init();
  }

  public void init() {
    LOGGER.trace("Initializing {}", CustomerHandlerImpl.class.getName());
    createTablesIfNonExistent();
  }

  private List<String> getExistingTables() {
    List<String> existingTables = new ArrayList<>();
    try (Connection con = dataSource.getConnection()) {
      DatabaseMetaData meta = con.getMetaData();

      ResultSet res = meta.getTables(null, null, "%", new String[] {"TABLE"});

      while (res.next()) {
        existingTables.add(res.getString("TABLE_NAME"));
      }

    } catch (SQLException e) {
      LOGGER.error("Unable to fetch exiting tables.", e);
    }
    return existingTables;
  }

  private void createTablesIfNonExistent() {
    List<String> tables = getExistingTables();
    LOGGER.trace("Existing tables : {}", tables);

    for (int i = 0; i < TABLES.size(); i++) {
      String tableName = TABLES.get(i);
      String tableDef = TABLES_DEF.get(i);
      if (!tables.contains(tableName)) {
        createTable(tableName, tableDef);
      }
    }
  }

  private void createTable(String tableName, String tableDefinition) {
    try (Connection con = dataSource.getConnection();
        Statement stmt = con.createStatement()) {
      LOGGER.trace("Creating table : {}", tableDefinition);
      stmt.execute("create table " + tableName + " " + tableDefinition);
    } catch (SQLException e) {
      LOGGER.error("Unable to create table {}", tableName, e);
    }
  }

  @Override
  public boolean addTrainer(Trainer trainer) throws SQLException {
    String address = trainer.getAddress();
    String firstName = trainer.getFirstName();
    String lastName = trainer.getLastName();
    String phone = trainer.getPhone();
    String healthInsuranceProvider = trainer.getHealthInsuranceProvider();
    String email = trainer.getEmail();
    String id = trainer.getId();
    int workHours = trainer.getWorkHours();
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
                    + " SET first_name=?, last_name=?, address=?, phone=?, email=?, health_insurance_provider=?, work_hours=? WHERE id=?");

        update.setString(1, firstName);
        update.setString(2, lastName);
        update.setString(3, address);
        update.setString(4, phone);
        update.setString(5, email);
        update.setString(6, healthInsuranceProvider);
        update.setInt(7, workHours);
        update.setString(8, id);
        update.execute();
        update.close();
      }

      removeById("id", id, QUALIFICATION_TABLE_NAME);

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

      try (Connection con = dataSource.getConnection();
          Statement stmt = con.createStatement()) {
        LOGGER.trace("Adding trainer : {}", trainer);
        stmt.execute(
            String.format(
                "INSERT INTO %s (first_name, last_name, address, phone, email, id, health_insurance_provider, work_hours) VALUES ('%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s');",
                TRAINER_TABLE_NAME,
                firstName,
                lastName,
                address,
                phone,
                email,
                id,
                healthInsuranceProvider,
                workHours));

        for (String qualification : qualifications) {
          addQualification(stmt, qualification, id);
        }
      }
    }
    return true;
  }

  @Override
  public boolean removeTrainer(String trainerId) throws SQLException {
    removeById(trainerId, TRAINER_TABLE_NAME);
    removeById(trainerId, QUALIFICATION_TABLE_NAME);
    return true;
  }

  @Override
  public List<Trainer> getTrainers() throws SQLException {
    try (Connection con = dataSource.getConnection();
        Statement stmt = con.createStatement()) {
      ResultSet resultSet =
          stmt.executeQuery(String.format("SELECT * FROM %s;", TRAINER_TABLE_NAME));

      List<Trainer> trainers = new ArrayList<>();
      while (resultSet.next()) {
        Trainer trainer = getTrainer(resultSet);
        if (trainer != null) {
          trainers.add(trainer);
        }
      }
      return trainers;
    }
  }

  private void removeById(String id, String tableName) throws SQLException {
    try (Connection con = dataSource.getConnection();
        Statement stmt = con.createStatement()) {
      LOGGER.trace("Removing from table {} : {}", tableName, id);
      stmt.execute(String.format("DELETE FROM %s WHERE ID = '%s';", tableName, id));
    }
  }

  private Trainer getTrainer(ResultSet resultSet) {
    try {
      String firstName = resultSet.getString("first_name");
      String lastName = resultSet.getString("last_name");
      String address = resultSet.getString("address");
      String phone = resultSet.getString("phone");
      String email = resultSet.getString("email");
      String id = resultSet.getString("id");
      String healthInsuranceProvider = resultSet.getString("health_insurance_provider");
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
              workHours,
              qualifications);
      LOGGER.trace("Got trainer {}", trainer);
      return trainer;
    } catch (SQLException e) {
      LOGGER.error("No data", e);
      return null;
    }
  }

  private Trainer getTrainerById(String id) throws SQLException {
    try (Connection con = dataSource.getConnection();
        Statement stmt = con.createStatement()) {
      ResultSet resultSet =
          stmt.executeQuery(
              String.format("SELECT * FROM %s where id='%s'", TRAINER_TABLE_NAME, id));

      if (resultSet == null) {
        return null;
      }

      while (resultSet.next()) {
        Trainer trainer = getTrainer(resultSet);
        if (trainer != null) {
          return trainer;
        }
      }
      return null;
    }
  }

  private void removeById(String idFieldName, String id, String tableName) throws SQLException {
    try (Connection con = dataSource.getConnection();
        Statement stmt = con.createStatement()) {
      LOGGER.trace("Removing from table {} : {}", tableName, id);
      stmt.execute(String.format("DELETE FROM %s WHERE %s = '%s';", tableName, idFieldName, id));
    }
  }

  private void addQualification(Statement statement, String qualification, String trainerId)
      throws SQLException {
    LOGGER.trace("Adding qualification {} to trainer with id {}", qualification, trainerId);
    statement.execute(
        String.format(
            "INSERT INTO %s (id, qualification) VALUES ('%s', '%s');",
            QUALIFICATION_TABLE_NAME, trainerId, qualification));
  }

  private List<String> getQualificationsForTrainer(String id) {
    try (Connection con = dataSource.getConnection();
        Statement stmt = con.createStatement()) {

      ResultSet resultSet =
          stmt.executeQuery(
              String.format("SELECT * from %s WHERE ID = '%s';", QUALIFICATION_TABLE_NAME, id));

      List<String> qualifications = new ArrayList<>();
      while (resultSet.next()) {
        String qualificaiton = resultSet.getString("qualification");
        qualifications.add(qualificaiton);
      }
      return qualifications;
    } catch (SQLException e) {
      LOGGER.error("Could not remove trainer {}", id, e);
    }

    return new ArrayList<>();
  }
}
