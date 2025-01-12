package edu.moonlightmoth.HiStat.controller;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.io.BufferedReader;
import java.io.InputStreamReader;

@RestController
@RequestMapping("/api")
public class FileController {

    @PostMapping("/calculate")
    public ResponseEntity<?> uploadCsv(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty() || !file.getOriginalFilename().endsWith(".csv")) {
            return ResponseEntity.badRequest().body("Invalid file. Please upload a CSV file.");
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            // Split CSV into rows and columns
            String[] headers = reader.readLine().split(",");
            ArrayNode jsonArray = new ObjectMapper().createArrayNode();

            reader.lines().forEach(line -> {
                String[] values = line.split(",");
                ObjectNode jsonObject = new ObjectMapper().createObjectNode();
                for (int i = 0; i < headers.length; i++) {
                    jsonObject.put(headers[i].trim(), values[i].trim());
                }
                jsonArray.add(jsonObject);
            });

            // Return JSON
            return ResponseEntity.ok(jsonArray.toPrettyString());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error processing the file: " + e.getMessage());
        }
    }
}
