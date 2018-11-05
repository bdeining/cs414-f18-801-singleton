`git clone https://github.com/bdeining/cs414.git`
The .gitignore file is set up to ignore both Intellij and Eclipse IDE specific files; thus, once can
use whichever IDE they prefer to develop.

This project uses maven as its build management tool.  To build, one must have Apache Maven 3.5.3 and Java 8+ installed.
It is built on top of Apache Karaf 4.2.0, a polymorphic OSGi container that has MySQL support and allows for rapid
development.  No additional preliminary actions are required to build the application.

`mvn clean install
cd distribution/target
unzip distribution-1.0-SNAPSHOT
cd distribution-1.0-SNAPSHOT/bin
./karaf`

Once the karaf instance is started, one can navigate to the UI
`http://localhost:8181/search/`

The karaf instance is configured to use SLF4J for logging.  One can turn up the logging level on a specific package
for debugging and tracing.

`log:set TRACE edu.colostate.cs.cs414.p3.bdeining.sql
log:tail`
