package com.hirekarma.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.util.IOUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hirekarma.entity.Student;
import com.hirekarma.helper.Helper;
import com.hirekarma.service.EmailSenderService;
import com.hirekarma.service.StudentService;

@RestController
public class StudentController {

	@Autowired
	private EmailSenderService service;
	@Autowired
	private StudentService studentService;
	
	

	Logger logger = LoggerFactory.getLogger(StudentService.class);

//	importing student into database, and sending mail to all the students using async
	@PostMapping("/student/importExcel")
	public ResponseEntity<?> upload(@RequestParam("file") MultipartFile file) {
		try {
			
			if (Helper.checkExcelFormat(file)) {
				
				List<Student> students = this.studentService.save(file);
				long start = System.currentTimeMillis();
				
//				sending each student a mail
				for (Student student : students) {

					if (!student.getEmail().equals("")) {
						service.sendSimpleEmail(student.getEmail(),
								"Hello " + student.getName() + " , this is a test application", "testing application");
					}

				}
				
				long end = System.currentTimeMillis();
				logger.info("Total time {}", (end - start));
				
				
				
				return ResponseEntity.ok(new JSONObject().put("message", "imported and mail sent succesfully"));
				
			}
		} catch (Exception e) {
			
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new JSONObject().put("message", "something went wrong"));
		
		}
		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new JSONObject().put("message", "Please upload excel file"));

	}

//	return response from Excel
	@GetMapping("/student/readExcel")
	public ResponseEntity<?> readJsonFromExcel(@RequestParam("file") MultipartFile file) {
		
		if (Helper.checkExcelFormat(file)) {
			
			List<Student> students = null;
			
			try {
				
				students = Helper.convertExcelToListOfStudent(file.getInputStream());
				return ResponseEntity.ok(students);
				
			} catch (IOException e) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new JSONObject().put("message", "Please upload excel file"));
			}
		}
		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new JSONObject().put("message", "Please upload excel file"));

	}

	
//	get all the student
	@GetMapping("/student")
	public ResponseEntity<?> getStudents() {
		return ResponseEntity.ok(this.studentService.getAllStudents());
	}

	
//	Exporting excel file from database
	@GetMapping("/student/exportExcel")
	public void downloadExcelFile(HttpServletResponse response) throws IOException {
		
		List<Student> students = this.studentService.getAllStudents();
		
		response.setContentType("application/octet-stream");
		response.setHeader("Content-Disposition", "attachment; filename=student.xlsx");
		
		Helper.export(students,response);
		
		
	}
}
