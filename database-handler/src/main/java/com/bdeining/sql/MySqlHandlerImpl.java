package com.bdeining.sql;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.sql.DataSource;

import com.bdeining.api.Customer;
import com.bdeining.api.Exercise;
import com.bdeining.api.Machine;
import com.bdeining.api.MySqlHandler;
import com.bdeining.api.Trainer;
import com.bdeining.api.WorkoutRoutine;
import com.bdeining.impl.TrainerImpl;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(immediate = true, //
        name = "MySqlHandlerImpl", //
        property = { //
                "service.exported.interfaces=*", //
        })

// TODO : Prepared Statements
// TODO : Decouple marshal and unmarshal
// TODO : deduplicate?
public class MySqlHandlerImpl implements MySqlHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(MySqlHandlerImpl.class);

    private static final String CUSTOMER_TABLE_NAME = "CUSTOMER";

    private static final String EXERCISE_TABLE_NAME = "EXERCISE";

    private static final String MACHINE_TABLE_NAME = "MACHINE";

    private static final String TRAINER_TABLE_NAME = "TRAINER";

    private static final String WORKOUT_ROUTINE_TABLE_NAME = "WORKOUT";

    private static final String QUALIFICATION_TABLE_NAME = "QUALIFICATIONS";

    private static final String TABLE_DEF = "(name varchar(100), email varchar(100))";

    private static final String QUALIFICATION_TABLE_DEF = "(id varchar(100), qualification varchar(100))";

    private static final String TRAINER_TABLE_DEF = "(first_name varchar(100), last_name varchar(100), address varchar(100), phone varchar(100), email varchar(100), id varchar(100), health_insurance_provider varchar(100), work_hours integer)";

    private DataSource dataSource;

    public void init() {
        LOGGER.trace("Initializing {}", MySqlHandlerImpl.class.getName());
        createTablesIfNonExistent();
    }

    private void createTablesIfNonExistent() {
        List<String> tables = getExistingTables();
        LOGGER.trace("Existing tables : {}", tables);

        if (!tables.contains(CUSTOMER_TABLE_NAME)) {
            createTable(CUSTOMER_TABLE_NAME, TABLE_DEF);
        }

        if (!tables.contains(EXERCISE_TABLE_NAME)) {
            createTable(EXERCISE_TABLE_NAME, TABLE_DEF);
        }

        if (!tables.contains(MACHINE_TABLE_NAME)) {
            createTable(MACHINE_TABLE_NAME, TABLE_DEF);
        }

        if (!tables.contains(TRAINER_TABLE_NAME)) {
            createTable(TRAINER_TABLE_NAME, TRAINER_TABLE_DEF);
        }

        if (!tables.contains(WORKOUT_ROUTINE_TABLE_NAME)) {
            createTable(WORKOUT_ROUTINE_TABLE_NAME, TABLE_DEF);
        }

        if (!tables.contains(QUALIFICATION_TABLE_NAME)) {
            createTable(QUALIFICATION_TABLE_NAME, QUALIFICATION_TABLE_DEF);
        }

        testAddTrainer();
        testAddTrainer();
        List<Trainer> trainers = getTrainers();
        for (Trainer trainer : trainers) {
            LOGGER.trace("{}", trainer);
        }
    }

    private void testAddTrainer() {
        Trainer trainer = new TrainerImpl("anAddress", "ben", "deininger", "", "", "", 80, Arrays.asList("software", "candy"));
        addTrainer(trainer);
    }

    private List<String> getExistingTables() {
        List<String> existingTables = new ArrayList<>();
        try (Connection con = dataSource.getConnection()) {
            DatabaseMetaData meta = con.getMetaData();

            ResultSet res = meta.getTables(null, null, "%",
                    new String[] {"TABLE"});

            while (res.next()) {
                existingTables.add(res.getString("TABLE_NAME"));
            }

        } catch (SQLException e) {
            LOGGER.error("Unable to fetch exiting tables.", e);
        }
        return existingTables;
    }

    private void createTable(String tableName, String tableDefinition) {
        try (Connection con = dataSource.getConnection(); Statement stmt = con.createStatement()) {
            LOGGER.trace("Creating table : {}", tableDefinition);
            stmt.execute("create table " + tableName + " " + tableDefinition);
        } catch (SQLException e) {
            LOGGER.error("Unable to create table {}", tableName, e);
        }
    }

    @Reference
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
        init();
    }

    @Override
    public boolean addCustomer(Customer customer) {
        return false;
    }

    @Override
    public boolean addExercise(Exercise exercise) {
        return false;
    }

    @Override
    public boolean addMachine(Machine machine) {
        return false;
    }

    @Override
    public boolean addTrainer(Trainer trainer) {
        String address = trainer.getAddress();
        String firstName = trainer.getFirstName();
        String lastName = trainer.getLastName();
        String phone = trainer.getPhone();
        String healthInsuranceProvider = trainer.getHealthInsuranceProvider();
        String email = trainer.getEmail();
        String id = trainer.getId();
        int workHours = trainer.getWorkHours();
        List<String> qualifications = trainer.getQualifications();

        try (Connection con = dataSource.getConnection(); Statement stmt = con.createStatement()) {
            LOGGER.trace("Adding trainer : {}", trainer);
            stmt.execute(String.format("INSERT INTO %s (first_name, last_name, address, phone, email, id, health_insurance_provider, work_hours) VALUES ('%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s');",TRAINER_TABLE_NAME, firstName, lastName, address, phone, email, id, healthInsuranceProvider, workHours));

            for (String qualification : qualifications) {
                addQualification(stmt, qualification, id);
            }

        } catch (SQLException e) {
            LOGGER.error("Unable to add trainer {}", trainer, e);
            return false;
        }

        return true;
    }

    @Override
    public boolean removeTrainer(String trainerId) {
        try (Connection con = dataSource.getConnection(); Statement stmt = con.createStatement()) {
            LOGGER.trace("Removing trainer : {}", trainerId);
            stmt.execute(String.format("DELETE FROM %s WHERE ID = %s;",TRAINER_TABLE_NAME, trainerId));
            removeById(trainerId, QUALIFICATION_TABLE_NAME);
        } catch (SQLException e) {
            LOGGER.error("Could not remove trainer {}", trainerId, e);
            return false;
        }
        return true;
    }

    @Override
    public List<Trainer> getTrainers() {
        try (Connection con = dataSource.getConnection(); Statement stmt = con.createStatement()) {
            //LOGGER.trace("Removing from table {} : {}", tableName, id);
            ResultSet resultSet = stmt.executeQuery(String.format("SELECT * FROM %s;", TRAINER_TABLE_NAME));

            List<Trainer> trainers = new ArrayList<>();
            while (resultSet.next()) {
                Trainer trainer = getTrainer(resultSet);
                if (trainer != null) {
                  trainers.add(trainer);
                }
            }
            return trainers;
        } catch (SQLException e) {
            LOGGER.error("Could not remove trainer {}", e);
        }
        return new ArrayList<>();
    }

    private Trainer getTrainer(ResultSet resultSet) {
        try {
        // (first_name varchar(100), last_name varchar(100), address varchar(100), phone varchar(100), email varchar(100), id varchar(100), health_insurance_provider varchar(100), work_hours integer);
        String firstName = resultSet.getString("first_name");
        String lastName = resultSet.getString("last_name");
        String address = resultSet.getString("address");
        String phone = resultSet.getString("phone");
        String email = resultSet.getString("email");
        String id = resultSet.getString("id");
        String healthInsuranceProvider = resultSet.getString("health_insurance_provider");
        int workHours = resultSet.getInt("work_hours");
        return new TrainerImpl(address, firstName, lastName, phone, email, healthInsuranceProvider, workHours, new ArrayList<>());
        } catch (SQLException e) {
            LOGGER.error("No data", e);
            return null;
        }

    }

    private void removeById(String id, String tableName) {
        try (Connection con = dataSource.getConnection(); Statement stmt = con.createStatement()) {
            LOGGER.trace("Removing from table {} : {}", tableName, id);
            stmt.execute(String.format("DELETE FROM %s WHERE ID = %s;",tableName, id));
        } catch (SQLException e) {
            LOGGER.error("Could not remove trainer {}", id, e);
        }
    }

    private void addQualification(Statement statement, String qualification, String trainerId) {
        try {
            LOGGER.trace("Adding qualification {} to trainer with id {}", qualification, trainerId);
            statement.execute(String.format("INSERT INTO %s (id, qualification) VALUES ('%s', '%s');",QUALIFICATION_TABLE_NAME, trainerId, qualification));
        } catch (SQLException e) {
            LOGGER.error("Could not add qualification {} to {}", qualification, trainerId, e);
        }
    }

    @Override
    public boolean addWorkoutRoutine(WorkoutRoutine workoutRoutine) {
        return false;
    }
}
