package edu.colostate.cs.cs414.p3.bdeining.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.osgi.service.component.annotations.Component;

@SuppressWarnings("serial")
@Component(
  service = {Servlet.class},
  property = {"alias=" + ApplicationConstants.APPLICATION_PATH + "/api/increment"}
)
public class IncrementerServlet extends HttpServlet {
  static final ObjectMapper mapper = new ObjectMapper();

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    response.setContentType("application/json");
    try {
      try (ServletInputStream postBody = request.getInputStream()) {
        Counter incrementedCounter = increment(mapper.readValue(postBody, Counter.class));
        response.setStatus(HttpServletResponse.SC_OK);
        try (PrintWriter responseBody = response.getWriter()) {
          mapper.writeValue(responseBody, incrementedCounter);
        }
      }
    } catch (Exception e) {
      response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
      try (PrintWriter responseBody = response.getWriter()) {
        responseBody.println("{ \"value\": 0, \"delta\": 0 }");
      } catch (Exception e2) {
        // Swallow exception quietly and just return the error code
      }
    }
  }

  Counter increment(Counter counter) {
    int delta = counter.getDelta();
    return new Counter(counter.getValue() + delta, delta);
  }
}
