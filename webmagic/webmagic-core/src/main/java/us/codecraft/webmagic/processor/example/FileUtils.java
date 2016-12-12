package us.codecraft.webmagic.processor.example;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream.GetField;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import org.apache.commons.codec.digest.DigestUtils;

import us.codecraft.webmagic.pipeline.FilePipeline;

public class FileUtils {
	
	
	private static final String seperator="/";
	
	public static void writeCorpus(String fileName,String corpus){
		PrintWriter printWriter=null;
		try {
			printWriter = new PrintWriter(new OutputStreamWriter(new FileOutputStream(getFile(fileName)),"UTF-8"));
			
			printWriter.print(corpus);
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}finally {
			if(printWriter!=null ){
				printWriter.close();
			}
//			System.out.println(printWriter.checkError());
		}
	}
	
	
	public static void writeContent(String fileName,String time,String title,String content){
		FileWriter file=null;
		BufferedWriter writer=null;
		PrintWriter printWriter=null;
		
		
		try {
			printWriter = new PrintWriter(new OutputStreamWriter(new FileOutputStream(getFile(fileName)),"UTF-8"));
			
			printWriter.println("Time:"+time);
//			writer.write("Time:"+time);
//			writer.newLine();
			
			printWriter.println("Title:"+title);
//			writer.write("Title:"+title);
//			writer.newLine();
			
			printWriter.println("Content:"+content);
//			writer.write("Content:"+content);
//			writer.newLine();
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}finally {
			if(printWriter!=null ){
				printWriter.close();
			}
			System.out.println(printWriter.checkError());
		}
//		try {
//			
//			
//			file = new FileWriter("./"+fileName);
//			writer=new BufferedWriter(file);
//			
//			writer.write("Time:"+time);
//			writer.newLine();
//			
//			writer.write("Title:"+title);
//			writer.newLine();
//			
//			writer.write("Content:"+content);
//			writer.newLine();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} finally {
//			if(writer!=null){
//				try {
//					writer.close();
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
//			
//			if(file!=null){
//				try {
//					file.close();
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
//		}
		
	}

	public static void checkAndMakeParentDirecotry(String fullName) {
        int index = fullName.lastIndexOf(seperator);
        if (index > 0) {
            String path = fullName.substring(0, index);
            File file = new File(path);
            if (!file.exists()) {
                file.mkdirs();
            }
        }
    }
	
	public static File getFile(String fullName) {
        checkAndMakeParentDirecotry(fullName);
        return new File(fullName);
    }
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
