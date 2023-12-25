package uta.cse.cse3310.webchat;

import java.io.IOException;
import net.freeutils.httpserver.HTTPServer.Context;
import net.freeutils.httpserver.HTTPServer.Request;
import net.freeutils.httpserver.HTTPServer.Response;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

class Handlers {

    @Context("/")
    public int defaultHandler(Request req, Response resp) throws IOException {
        String path = req.getPath();
        // Path testing
        System.out.println("Requested path: " + path);
        
        if (path.equals("/") || path.equals("/index.html"))  {
            resp.getHeaders().add("Content-Type", "text/html");
            String filePath = "html/index.html";
            byte[] fileBytes = Files.readAllBytes(Paths.get(filePath));
            String htmlContent = new String(fileBytes, StandardCharsets.UTF_8);
            resp.send(200, htmlContent);
        } else if (path.endsWith("webchat.html")) {
            resp.getHeaders().add("Content-Type", "text/html");
            String filePath = "html/webchat.html"; // Update with the correct file path.
            byte[] fileBytes = Files.readAllBytes(Paths.get(filePath));
            String htmlContent = new String(fileBytes, StandardCharsets.UTF_8);
            resp.send(200, htmlContent);
        } else if (path.endsWith("signup.html")) {
            resp.getHeaders().add("Content-Type", "text/html");
            String filePath = "html/signup.html"; // Update with the correct file path.
            byte[] fileBytes = Files.readAllBytes(Paths.get(filePath));
            String htmlContent = new String(fileBytes, StandardCharsets.UTF_8);
            resp.send(200, htmlContent);
        } else if (path.endsWith(".css")) {
            resp.getHeaders().add("Content-Type", "text/css");
            String filePath = "css/style.css";
            byte[] fileBytes = Files.readAllBytes(Paths.get(filePath));
            String cssContent = new String(fileBytes, StandardCharsets.UTF_8);
            resp.send(200, cssContent);
        } else {
            resp.send(404, "Not found");
        }
        
        return 0;
    }
}






