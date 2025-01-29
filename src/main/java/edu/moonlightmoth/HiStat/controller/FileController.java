package edu.moonlightmoth.HiStat.controller;

import com.fasterxml.jackson.databind.node.ObjectNode;
import edu.moonlightmoth.HiStat.service.Report;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class FileController {

    @PostMapping("/calculate")
    public ResponseEntity<?> uploadCsv(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty() || !file.getOriginalFilename().endsWith(".csv")) {
            return ResponseEntity.badRequest().body("Invalid file. Please upload a CSV file.");
        }
        String[] names;
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(file.getInputStream()))) {
            names = reader.readLine().split(",");
            List<String> read = new ArrayList<>();
            reader.lines().forEach(read::add);
            double[][] sampling = new double[names.length][read.size()];
            for (int i = 0; i < read.size(); i++) {
                String[] split = read.get(i).split(",");
                for (int j = 0; j < sampling.length; j++) {
                    double parsed;
                    try {
                        parsed = Double.parseDouble(split[j]);
                    }
                    catch (NumberFormatException e) {
                        parsed = Double.NaN;
                    }
                    sampling[j][i] = parsed;
                }
            }
            Report report = new Report(sampling, names);
            return ResponseEntity.ok(report.toJsonString());
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Error processing the file: " + e.getMessage());
        }
    }
}
