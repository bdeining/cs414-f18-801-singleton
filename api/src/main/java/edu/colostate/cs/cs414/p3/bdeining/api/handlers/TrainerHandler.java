package edu.colostate.cs.cs414.p3.bdeining.api.handlers;

import java.sql.SQLException;
import java.util.List;

import edu.colostate.cs.cs414.p3.bdeining.api.Trainer;

public interface TrainerHandler {

  boolean addTrainer(Trainer trainer) throws SQLException;

  boolean removeTrainer(String id) throws SQLException;

  List<Trainer> getTrainers() throws SQLException;
}
