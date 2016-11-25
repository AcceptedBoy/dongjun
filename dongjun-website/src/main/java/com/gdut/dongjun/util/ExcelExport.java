package com.gdut.dongjun.util;

import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class ExcelExport<T> {

	public void excelExport(List<T> list, String name,HttpServletRequest req,
			String fileName,String[] headers,String title,String path,List<String> exception) throws IOException {
		try {
			try {
//				resp.setHeader("Content-Disposition", "attachment;filename="+new String(fileName.getBytes("utf-8"),"iso8859-1"));
//				resp.setContentType("application/ynd.ms-excel;charset=UTF-8");req.getSession().getServletContext().getRealPath("/")+
				String realPath = path+"\\"+fileName;
//				OutputStream out=resp.getOutputStream();
				Workbook wb = new XSSFWorkbook();
				Sheet sheet = wb.createSheet();
				Row row = sheet.createRow(0);
				Object object =Class.forName(name).newInstance();
				Class clazz = object.getClass();
				Field[] fs = clazz.getDeclaredFields();
				Cell cell2 = row.createCell(0);
				sheet.addMergedRegion(new CellRangeAddress(0,0,0,headers.length-1));
				CellStyle cellStyle = wb.createCellStyle();
				cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
				cell2.setCellValue(title);
				cell2.setCellStyle(cellStyle);
				Row row2 = sheet.createRow(1);
				for(int i=0;i<headers.length;i++) {
					Cell cell = row2.createCell(i);
					cell.setCellValue(headers[i]);
				}
				for(int i=2;i<list.size()+2;i++) {
					Row row1 = sheet.createRow(i);
					int count = 0;
					for(int j=0;j<fs.length;j++) {
						
						if(exception.contains(fs[j].getName())){
							count++;
							continue;
						}
						
						fs[j].setAccessible(true);
						Cell cell1 = row1.createCell(j-count);
						if(fs[j].get(list.get(i-2))==null) {
							continue;
						}
						cell1.setCellValue(fs[j].get(list.get(i-2)).toString());
					}
				}
				FileOutputStream out = new FileOutputStream(realPath);
//				FileOutputStream out = new FileOutputStream("e:\\workbook1.xls");
				wb.write(out);
				out.flush();
				out.close();
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}