package edu.colostate.cs.cs414.p3.bdeining.sql;

public final class TableConstants {

  public static final String CUSTOMER_TABLE_NAME = "CUSTOMER";

  public static final String EXERCISE_TABLE_NAME = "EXERCISE";

  public static final String MACHINE_TABLE_NAME = "MACHINE";

  public static final String TRAINER_TABLE_NAME = "TRAINER";

  public static final String WORKOUT_ROUTINE_TABLE_NAME = "WORKOUT";

  public static final String QUALIFICATION_TABLE_NAME = "QUALIFICATIONS";

  public static final String EXERCISE_WORKOUT_ROUTINE_TABLE_NAME = "EXERCISE_WOR";

  public static final String CUSTOMER_WORKOUT_ROUTINE_TABLE_NAME = "CUSTOMER_WOR";

  public static final String BRANCH_TABLE_NAME = "BRANCH";

  public static final String EXERCISE_TABLE_DEF =
      "(name varchar(100), id varchar(100), machineId varchar(100), sets integer, duration integer, workoutRoutineId varchar(100))";

  public static final String MACHINE_TABLE_DEF =
      "(name varchar(100), id varchar(100), picture varchar(65535), quantity integer)";

  public static final String EXERCISE_WORKOUT_ROUTINE_TABLE_DEF =
      "(workoutRoutineId varchar(100), exerciseId varchar(100))";

  public static final String CUSTOMER_WORKOUT_ROUTINE_TABLE_DEF =
      "(workoutRoutineId varchar(100), customerId varchar(100))";

  public static final String CUSTOMER_TABLE_DEF =
      "(first_name varchar(100), last_name varchar(100), address varchar(100), phone varchar(100), email varchar(100), id varchar(100), health_insurance_provider varchar(100), activity varchar(100), branch varchar(100))";

  public static final String WORKOUT_ROUTINE_TABLE_DEF = "(id varchar(100), name varchar(100))";

  public static final String QUALIFICATION_TABLE_DEF =
      "(id varchar(100), qualification varchar(100))";

  public static final String TRAINER_TABLE_DEF =
      "(first_name varchar(100), last_name varchar(100), address varchar(100), phone varchar(100), email varchar(100), id varchar(100), health_insurance_provider varchar(100), work_hours integer, password varchar(100), branch varchar(100))";

  public static final String BRANCH_TABLE_DEF = "(name varchar(100))";

  private TableConstants() {}
}
