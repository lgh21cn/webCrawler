package com.lgh.webcrawler.prj;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileUtils {
	
	
	private static final String seperator=System.getProperty("line.separator");
	public static void writeContent(String fileName,String time,String title,String content){
		FileWriter file=null;
		BufferedWriter writer=null;
		try {
			file = new FileWriter("./"+fileName);
			writer=new BufferedWriter(file);
			
			writer.write("Time:"+time);
			writer.newLine();
			
			writer.write("Title:"+title);
			writer.newLine();
			
			writer.write("Content:"+content);
			writer.newLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if(writer!=null){
				try {
					writer.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			if(file!=null){
				try {
					file.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
