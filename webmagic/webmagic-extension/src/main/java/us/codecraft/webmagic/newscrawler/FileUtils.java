package us.codecraft.webmagic.newscrawler;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import us.codecraft.webmagic.Request;


public class FileUtils {

	private static final String seperator = "/";

	public static void writeCorpus(String fileName, String corpus) {
		PrintWriter printWriter = null;
		try {
			printWriter = new PrintWriter(new OutputStreamWriter(new FileOutputStream(getFile(fileName)), "UTF-8"));

			printWriter.print(corpus);
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

	public static void loadUrlFile(String filePath,List<Request> requests,boolean addNew) {
		FileInputStream fis = null;
		InputStreamReader isr = null;
		BufferedReader br = null; // 用于包装InputStreamReader,提高处理性能。因为BufferedReader有缓冲的，而InputStreamReader没有。
		try {
			String url = "";
			fis = new FileInputStream(filePath);// FileInputStream
			// 从文件系统中的某个文件中获取字节
			isr = new InputStreamReader(fis);// InputStreamReader 是字节流通向字符流的桥梁,
			br = new BufferedReader(isr);// 从字符输入流中读取文件中的内容,封装了一个new
											// InputStreamReader的对象
			while ((url = br.readLine()) != null) {
				url=FileNumberVerifier.removeURLTag(url);
				requests.add(new Request(url).putExtra(AsianViewDetailItem.ORIGIN_URL, AsianViewDetailItem.ORIGIN_URL_CONTENT)
						.putExtra(AsianViewDetailItem.ADDNEW, addNew));
			}
//			System.out.println(str1);// 打印出str1
		} catch (FileNotFoundException e) {
			System.out.println("找不到指定文件");
		} catch (IOException e) {
			System.out.println("读取文件失败");
		} finally {
			try {
				br.close();
				isr.close();
				fis.close();
				// 关闭的时候最好按照先后顺序关闭最后开的先关闭所以先关s,再关n,最后关m
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}


	public static void writeContent(String fileName, String time, String title, String content) {
		FileWriter file = null;
		BufferedWriter writer = null;
		PrintWriter printWriter = null;

		try {
			printWriter = new PrintWriter(new OutputStreamWriter(new FileOutputStream(getFile(fileName)), "UTF-8"));

			printWriter.println("Time:" + time);
			// writer.write("Time:"+time);
			// writer.newLine();

			printWriter.println("Title:" + title);
			// writer.write("Title:"+title);
			// writer.newLine();

			printWriter.println("Content:" + content);
			// writer.write("Content:"+content);
			// writer.newLine();
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
			System.out.println(printWriter.checkError());
		}
		// try {
		//
		//
		// file = new FileWriter("./"+fileName);
		// writer=new BufferedWriter(file);
		//
		// writer.write("Time:"+time);
		// writer.newLine();
		//
		// writer.write("Title:"+title);
		// writer.newLine();
		//
		// writer.write("Content:"+content);
		// writer.newLine();
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// } finally {
		// if(writer!=null){
		// try {
		// writer.close();
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// }
		//
		// if(file!=null){
		// try {
		// file.close();
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// }
		// }

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
	
	public static void readFileNames(String path,Set<String> names){
		File root=new File(path);
		if(root.exists() && root.isDirectory()){
			File[] subFiles=root.listFiles();
			for (File file : subFiles) {
				if(file.isFile()){
//					System.out.println(file.getName());
					String fileName=file.getName();
					if(fileName.matches("\\d+-\\d+(\\.txt)?"))
						names.add(fileName);
					
					else if(fileName.matches("\\d+\\.match\\.log")){
						names.add(fileName);
					}
				}
			}
		}
	}
	
	public static String readFile(String filePath){
		FileInputStream fis = null;
		InputStreamReader isr = null;
		BufferedReader br = null; // 用于包装InputStreamReader,提高处理性能。因为BufferedReader有缓冲的，而InputStreamReader没有。
		StringBuilder sb=new StringBuilder();
		try {
			String line = "";
			fis = new FileInputStream(filePath);// FileInputStream
			// 从文件系统中的某个文件中获取字节
			isr = new InputStreamReader(fis);// InputStreamReader 是字节流通向字符流的桥梁,
			br = new BufferedReader(isr);// 从字符输入流中读取文件中的内容,封装了一个new
											// InputStreamReader的对象
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
//			System.out.println(str1);// 打印出str1
		} catch (FileNotFoundException e) {
			System.out.println("找不到指定文件");
		} catch (IOException e) {
			System.out.println("读取文件失败");
		} finally {
			try {
				br.close();
				isr.close();
				fis.close();
				// 关闭的时候最好按照先后顺序关闭最后开的先关闭所以先关s,再关n,最后关m
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			
		}
		return sb.toString();
	}

	static final String docName="dwNews";
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		List<Request> requests=new ArrayList<Request>();
//		Boolean addNew=null;
//		FileUtils.loadUrlFile("./tmp_url_removed.txt", requests,addNew);
//		
//		for (Request request : requests) {
//			System.out.println("Index("+requests.indexOf(request)+")"+request.getUrl());
//		}
		Set<String> names=new HashSet<String>();
		readFileNames(".//"+docName, names);
		System.out.println(names.size());
	}

}
