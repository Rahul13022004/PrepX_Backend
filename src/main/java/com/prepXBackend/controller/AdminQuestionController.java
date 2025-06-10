package com.prepXBackend.controller;

import com.prepXBackend.model.Question;
import com.prepXBackend.repository.QuestionRepository;
import com.prepXBackend.service.AdminService;
import com.prepXBackend.service.QuestionService;
import com.prepXBackend.helper.CSVHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/admin")
public class AdminQuestionController {

    @Autowired
    private QuestionRepository questionRepository;
    
    @Autowired
    private QuestionService questionService;

    @Autowired
    private AdminService adminService;

    // ✅ Upload CSV File (Only for Admin)
    @PostMapping("/upload")
    public ResponseEntity<String> uploadCSVFile(@RequestParam("file") MultipartFile file, @RequestParam("password") String password) {
        if (!adminService.isAdmin(password)) {
            return ResponseEntity.status(403).body("Error: Unauthorized access. Invalid admin password.");
        }

        if (file == null || file.isEmpty()) {
            return ResponseEntity.badRequest().body("Error: No file uploaded.");
        }

        if (!CSVHelper.hasCSVFormat(file)) {
            return ResponseEntity.badRequest().body("Error: Invalid file format. Please upload a CSV file.");
        }

        questionService.saveQuestionsFromCSV(file);
        return ResponseEntity.ok("CSV file uploaded and questions saved successfully!");
    }

    // ✅ Add Multiple Questions at Once
    @PostMapping("/add-questions")
    public ResponseEntity<String> addQuestions(@RequestBody List<Question> questions, @RequestParam("password") String password) {
        if (!adminService.isAdmin(password)) {
            return ResponseEntity.status(403).body("Error: Unauthorized access. Invalid admin password.");
        }

        questionRepository.saveAll(questions);
        return ResponseEntity.ok("Questions added successfully!");
    }
    
 // ✅ Delete Questions by IDs (Only for Admin)
    @DeleteMapping("/delete-questions")
    public ResponseEntity<String> deleteQuestions(
        @RequestParam("ids") List<Long> ids,
        @RequestParam("password") String password
    ) {
        if (!adminService.isAdmin(password)) {
            return ResponseEntity.status(403).body("Error: Unauthorized access. Invalid admin password.");
        }

        if (ids == null || ids.isEmpty()) {
            return ResponseEntity.badRequest().body("Error: No question IDs provided.");
        }

        questionRepository.deleteAllById(ids);
        return ResponseEntity.ok("Questions deleted successfully!");
    }

}
