package com.batch.demo.reader;

import java.io.InputStream;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemStream;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import com.batch.demo.dto.PersonaExcelDTO;

@Component
public class ExcelItemReader implements ItemReader<PersonaExcelDTO>, ItemStream {

    private Iterator<Row> rows;
    private Workbook workbook;

    @Override
    public void open(ExecutionContext executionContext) {
        try {
            InputStream is = new ClassPathResource("personas.xlsx").getInputStream();
            workbook = new XSSFWorkbook(is);
            Sheet sheet = workbook.getSheetAt(0);

            rows = sheet.iterator();

            // saltar header
            if (rows.hasNext()) {
                rows.next();
            }

        } catch (Exception e) {
            throw new RuntimeException("Error leyendo Excel", e);
        }
    }

    @Override
    public PersonaExcelDTO read() {
        if (rows != null && rows.hasNext()) {
            Row row = rows.next();

            PersonaExcelDTO dto = new PersonaExcelDTO();
            dto.setNombre(row.getCell(0).getStringCellValue());
            dto.setEdad((int) row.getCell(2).getNumericCellValue());
            dto.setEmail(row.getCell(3).getStringCellValue());

            return dto;
        }
        return null;
    }

    @Override
    public void update(ExecutionContext executionContext) {
        // opcional para restart
    }

    @Override
    public void close() {
        try {
            if (workbook != null) {
                workbook.close();
            }
        } catch (Exception e) {
            // log si querés
        }
    }
}