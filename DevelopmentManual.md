Required Software
-----------------
Apache Maven 3.5.3
Java 8+
Git

Development Environment
-----------------------
`git clone https://github.com/bdeining/cs414.git`
The .gitignore file is set up to ignore both Intellij and Eclipse IDE specific files; thus, one can
use whichever IDE they prefer to develop in.  The preferred IDE is Intellij Community Edition.

Open the project in Intellij / Eclipse by Navigating to File -> Open

This project uses maven as its build management tool. It is built on top of Apache Karaf 4.2.0, a polymorphic OSGi
container that has MySQL support and allows for rapid development.  No additional preliminary actions are required to build the application.

`mvn clean install`

JavaScript Formatting
---------------------
The .js files in this project are formatted using prettier.  This can be installed with the following command
`npm install -g prettier`
Run the formatter before committing files.
`prettier --write "**/*.js"`

Installation and Usage Instructions
-----------------------------------
Once built, once can navigate to the distribution directory and run the Karaf executable in either windows or mac.

`cd distribution/target
unzip distribution-1.0-SNAPSHOT
cd distribution-1.0-SNAPSHOT/bin
./karaf`

Once the karaf instance is started, one can navigate to the UI. It is recommended to use Google Chrome.
`http://localhost:8181/search/`

The karaf instance is configured to use SLF4J for logging.  One can turn up the logging level on a specific package
for debugging and tracing.

`log:set TRACE edu.colostate.cs.cs414.p3.bdeining.sql
log:tail`

The upload functionality for pictures in the Machines tab should be less than 20kb.  Images above this size will fail.

The default manager login is manager, password is manager.  This currently is the only manager known to the system.  Trainers
that are added to the system by a manager will specify a password and will be able to login.

Debugging
---------
Karaf supports attaching IDE debuggers and setting breakpoints; once must set up a remote configuration in the IDE
and set it to port 5005.

`./karaf debug`

This will allow for setting breakpoints in a running instance of karaf.

Further Readings
-----------------
http://karaf.apache.org/manual/latest/

Technologies Used
-----------------
Apache Maven 3.5.3
Apache Karaf 4.2.0
Apache CXF 3.2.5
Java 8+
React 16.4.0
react-dom 16.4.0
react-router-dom 4.3.1
react-select 2.1.1
bootstrap3 3.3.5
react-bootstrap 0.32.1
react-table 6.8.6
react-router-bootstrap 0.24.4
react-redux 5.0.7
redux 4.0.0
axios 0.18.0
Jackson Faster XML 2.9.6
GSON 2.8.5
SLF4J 1.7.25
Pax JDBC 1.3.1

Demo Video
----------
Iteration 1 : https://youtu.be/-kWzcN7gv0I

TODOs
----------
Finish coverage
CSS styles on tables