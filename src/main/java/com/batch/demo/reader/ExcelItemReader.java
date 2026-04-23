package com.batch.demo.reader;

import java.io.FileInputStream;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemStream;
import org.springframework.batch.item.ExecutionContext;

import com.batch.demo.dto.PersonaExcelDTO;
import org.springframework.stereotype.Component;

@Component
public class ExcelItemReader implements ItemReader<PersonaExcelDTO> {

	  private Iterator<Row> rows;

	    public void open(ExecutionContext executionContext) {
	        try {
	            FileInputStream file = new FileInputStream("src/main/resources/data/personas.xlsx");
	            Workbook workbook = new XSSFWorkbook(file);
	            Sheet sheet = workbook.getSheetAt(0);

	            rows = sheet.iterator();
	            rows.next(); // skip header

	        } catch (Exception e) {
	            throw new RuntimeException(e);
	        }
	    }

	    @Override
	    public PersonaExcelDTO read() {
	        if (!rows.hasNext()) return null;

	        Row row = rows.next();

	        PersonaExcelDTO dto = new PersonaExcelDTO();
	        dto.setNombre(row.getCell(0).getStringCellValue());
	        dto.setEmail(row.getCell(1).getStringCellValue());
	        dto.setEdad((int) row.getCell(2).getNumericCellValue());

	        return dto;
	    }

	    public void update(ExecutionContext executionContext) {}
	    public void close() {}

}
