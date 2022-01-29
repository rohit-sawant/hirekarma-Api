package com.hirekarma.helper;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import com.hirekarma.entity.Student;

public class Helper {

//	To get if the file is excel or not
	public static boolean checkExcelFormat(MultipartFile file) {
	
		String contentType = file.getContentType();
		return contentType.equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
	}
	
//	convert the data in excel to list of Student.class
	public static List<Student> convertExcelToListOfStudent(InputStream is){
		List<Student> students = new ArrayList<>();
		try {
			
			XSSFWorkbook workbook = new XSSFWorkbook(is);
			
			XSSFSheet workSheet =  workbook.getSheetAt(0);
			
			int rowNumber = 0;
			
			Iterator<Row> iterator = workSheet.iterator();
			
			while(iterator.hasNext()) {
				Row row = iterator.next();
				if(rowNumber==0) {
					rowNumber++;
					continue;
				}
				Iterator<Cell> cells = row.iterator();
				int cid = 0 ; 
				Student student = new Student();
				while(cells.hasNext()) {
					Cell cell = cells.next();
					switch(cid) {
					case 0:
						
						student.setName(cell.getStringCellValue());
						break;
					case 1:
						student.setEmail(cell.getStringCellValue());
						break;
					case 2:
						student.setPhone(NumberToTextConverter.toText(cell.getNumericCellValue()));
						break;
					default:
						break;
					}
					cid++;
				}
				students.add(student);
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		System.out.print(students);
		return students;
	}
	
//	exportExcel function 
	public static void export(List<Student> students, HttpServletResponse response) {
		try(Workbook workbook = new XSSFWorkbook()){
			Sheet sheet = workbook.createSheet("Students");
			
			Row row = sheet.createRow(0);
			
			// Define header cell style
	        CellStyle headerCellStyle = workbook.createCellStyle();
	        headerCellStyle.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
	        headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
	        
	        // Creating header cells 
	        Cell cell = row.createCell(0);
	        cell.setCellValue("Name");
	        cell.setCellStyle(headerCellStyle);
	        
	        cell = row.createCell(1);
	        cell.setCellValue("Email");
	        cell.setCellStyle(headerCellStyle);
	
	        cell = row.createCell(2);
	        cell.setCellValue("Phone");
	        cell.setCellStyle(headerCellStyle);
	        
	        // Creating data rows for each student
	        for(int i = 0; i < students.size(); i++) {
	        	Row dataRow = sheet.createRow(i + 1);
	        	dataRow.createCell(0).setCellValue(students.get(i).getName());
	        	dataRow.createCell(1).setCellValue(students.get(i).getEmail());
	        	dataRow.createCell(2).setCellValue(students.get(i).getPhone());
	        }
	
	        // Making size of column auto resize to fit with data
	        sheet.autoSizeColumn(0);
	        sheet.autoSizeColumn(1);
	        sheet.autoSizeColumn(2);
	        
	        ServletOutputStream outputStream = response.getOutputStream();
	        workbook.write(outputStream);
	        workbook.close();
	       return ;
		} catch (IOException ex) {
			ex.printStackTrace();
			return ;
		}
	}
}