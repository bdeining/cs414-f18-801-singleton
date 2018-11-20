package edu.colostate.cs.cs414.p3.bdeining.web;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.List;
import javax.servlet.Servlet;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.osgi.service.component.annotations.Component;

/**
 * This class is adapted from
 * https://github.com/steinarb/frontend-karaf-demo/blob/master/src/main/java/no/priv/bang/demos/frontendkarafdemo/ReactServlet.java
 */
@Component(
  service = {Servlet.class},
  property = {"alias=/search"}
)
public class ReactServlet extends HttpServlet {
  private static final long serialVersionUID = 250817058831319271L;

  private final List<String> routes =
      Arrays.asList("/", "/trainer", "/workouts", "/machine", "/exercise", "/login");

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) {
    String pathInfo = request.getPathInfo();
    try {
      if (pathInfo == null) {
        addSlashToServletPath(request, response);
        return;
      }

      String resource = findResourceFromPathInfo(pathInfo);
      String contentType = guessContentTypeFromResourceName(resource);
      response.setContentType(contentType);
      try (ServletOutputStream responseBody = response.getOutputStream()) {
        try (InputStream resourceFromClasspath =
            getClass().getClassLoader().getResourceAsStream(resource)) {
          if (resourceFromClasspath != null) {
            copyStream(resourceFromClasspath, responseBody);
            response.setStatus(200);
            return;
          }

          String message = String.format("Resource \"%s\" not found on the classpath", resource);
          response.sendError(404, message);
        }
      }
    } catch (IOException e) {
      response.setStatus(500);
    }
  }

  String guessContentTypeFromResourceName(String resource) {
    String contentType = URLConnection.guessContentTypeFromName(resource);
    if (contentType != null) {
      return contentType;
    }

    String extension = resource.substring(resource.lastIndexOf('.') + 1);
    if ("xhtml".equals(extension)) {
      return "text/html";
    }

    if ("js".equals(extension)) {
      return "application/javascript";
    }

    if ("css".equals(extension)) {
      return "text/css";
    }

    return null;
  }

  private String findResourceFromPathInfo(String pathInfo) {
    if (routes.contains(pathInfo)) {
      return "index.xhtml";
    }

    return pathInfo;
  }

  private void addSlashToServletPath(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    response.sendRedirect(String.format("%s/", request.getServletPath()));
  }

  private void copyStream(InputStream input, ServletOutputStream output) throws IOException {
    int c;
    while ((c = input.read()) != -1) {
      output.write(c);
    }
  }
}
