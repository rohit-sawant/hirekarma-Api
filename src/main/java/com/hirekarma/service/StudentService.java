package com.hirekarma.service;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.hirekarma.entity.Student;
import com.hirekarma.helper.Helper;
import com.hirekarma.repository.StudentRepository;

@Service
public class StudentService {

	@Autowired
	private StudentRepository studentRepository;

	@Autowired
	EmailSenderService emailSenderService;

	Logger logger = LoggerFactory.getLogger(StudentService.class);

//	save and send the mail to user simulataneously
	public List<Student> save(MultipartFile file) {
		List<Student> students = null;
		try {
			students = Helper.convertExcelToListOfStudent(file.getInputStream());
			this.studentRepository.saveAll(students);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		long start = System.currentTimeMillis();
		for (Student student : students) {

			if (!student.getEmail().equals("")) {
				emailSenderService.sendSimpleEmail(student.getEmail(),
						"Hello " + student.getName() + " , this is a test application", "testing application");
			}

		}
		long end = System.currentTimeMillis();
		logger.info("Total time {}", (end - start));
		return students;

	}


	public List<Student> getAllStudents() {
		logger.info("geting list of user");
		List<Student> students = this.studentRepository.findAll();
		return students;
	}
}
