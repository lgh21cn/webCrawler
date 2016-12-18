package com.lgh.ner;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.List;

public class FileUtils {

	public static final String seperator = "/";
	
	public static void readAllFilePaths(String root,List<String> paths) throws IOException{
		if(root==null ||root.trim().isEmpty()) return;
		
		File rootFile=new File(root);
		if(rootFile==null && !rootFile.exists()) return;
		
		File[] subFiles=rootFile.listFiles();
		
		for (File file : subFiles) {
			
			System.out.println(file.getCanonicalPath());
			System.out.println("Change path seperator,Results:"+file.getCanonicalPath().replace("\\", seperator));
			if(file.isDirectory()){
				readAllFilePaths(file.getCanonicalPath().replace("\\", seperator), paths);
			}else if(file.isFile()){
				paths.add(file.getCanonicalPath());
			}
		}
	}
	
	public static String readFile(String filePath)
	{
		return readFile( filePath, true);
	}
	public static String readFile(String filePath,boolean oneline){
		FileInputStream fis = null;
		InputStreamReader isr = null;
		BufferedReader br = null; // ���ڰ�װInputStreamReader,��ߴ������ܡ���ΪBufferedReader�л���ģ���InputStreamReaderû�С�
		StringBuilder sb=new StringBuilder();
		try {
			String line = "";
			fis = new FileInputStream(filePath);// FileInputStream
			// ���ļ�ϵͳ�е�ĳ���ļ��л�ȡ�ֽ�
			isr = new InputStreamReader(fis);// InputStreamReader ���ֽ���ͨ���ַ���������,
			br = new BufferedReader(isr);// ���ַ��������ж�ȡ�ļ��е�����,��װ��һ��new
											// InputStreamReader�Ķ���
			while ((line = br.readLine()) != null) {
				line=line.trim();
				if(line.isEmpty())
					continue;
				sb.append(line);
				if(!oneline)
					sb.append("\n");
				else
					sb.append(" ");
			}
//			System.out.println(str1);// ��ӡ��str1
		} catch (FileNotFoundException e) {
			System.out.println("�Ҳ���ָ���ļ�");
		} catch (IOException e) {
			System.out.println("��ȡ�ļ�ʧ��");
		} finally {
			try {
				br.close();
				isr.close();
				fis.close();
				// �رյ�ʱ����ð����Ⱥ�˳��ر���󿪵��ȹر������ȹ�s,�ٹ�n,����m
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			
		}
		return sb.toString();
	}
	
	public static void writeErrorLog(String fileName, String log,boolean append) {
		PrintWriter printWriter = null;
		try {
			printWriter = new PrintWriter(new OutputStreamWriter(new FileOutputStream(getFile(fileName),append), "UTF-8"));

			printWriter.print(log);
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} finally {
			if (printWriter != null) {
				printWriter.close();
			}
			// System.out.println(printWriter.checkError());
		}
	}
	
	public static void writeErrorLog(String fileName, String log) {
		writeErrorLog(fileName, log, false);
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
