Development Environment
-----------------------
`git clone https://github.com/bdeining/cs414.git`
The .gitignore file is set up to ignore both Intellij and Eclipse IDE specific files; thus, once can
use whichever IDE they prefer to develop in.

This project uses maven as its build management tool.  To build, one must have Apache Maven 3.5.3 and Java 8+ installed.
It is built on top of Apache Karaf 4.2.0, a polymorphic OSGi container that has MySQL support and allows for rapid
development.  No additional preliminary actions are required to build the application.

`mvn clean install`

Usage Instructions
------------------
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
