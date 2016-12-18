package com.lgh.ner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CorpusResolver {
	
	public static final String ROOT_CORPUS_PATH="C:/eclipse/workspace/NERPrj/corpus";
	public static final String ROOT_UNTAGGED_CORPUS_PATH="C:/eclipse/workspace/NERPrj/untagged";

	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		String path=ROOT_CORPUS_PATH+"/asianews/20050309-102.txt";
		List<String> paths=new ArrayList<String>();
		try {
			FileUtils.readAllFilePaths(ROOT_CORPUS_PATH, paths);
			for (String p : paths) {
				createNewCorpus(p.replace("\\", FileUtils.seperator));
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	public static void createNewCorpus(String path){
		String fileName=getFileName(path);
		String belong=getBelongName(path);
		
		String corpus=FileUtils.readFile(path);
		CorpusResolver cr=new CorpusResolver();
		String new_corpus=cr.resolver(corpus, ROOT_UNTAGGED_CORPUS_PATH+FileUtils.seperator+belong);
		
		FileUtils.writeErrorLog(ROOT_UNTAGGED_CORPUS_PATH+FileUtils.seperator+belong+FileUtils.seperator+fileName, new_corpus);
	}
	
	public static String getFileName(String path){
		if(path!=null && !path.isEmpty()){
			final int seperator_index=path.lastIndexOf(FileUtils.seperator);
			
			String filename=path.substring(seperator_index+1);
			return filename;
		}
		return "";
	}
	
	public static String getBelongName(String path){
		if(path!=null && !path.isEmpty()){
			System.out.println("Path:"+path);
			final int seperator_index=path.lastIndexOf(FileUtils.seperator);
			final int second_seperator_index=path.lastIndexOf(FileUtils.seperator,seperator_index-1);
			
			String belong=path.substring(second_seperator_index+1, seperator_index);
			return belong;
		}
		return "";
	}
	
	
	
	public String resolver(String corpus,String path){
		if(corpus!=null && !corpus.isEmpty()){
			String abstracts=CorpusFormatter.getContentBy(corpus, CorpusFormatter.TAG_ABSTRACT_START, CorpusFormatter.TAG_ABSTRACT_END);
			String contents=CorpusFormatter.getContentBy(corpus, CorpusFormatter.TAG_CONTENT_START, CorpusFormatter.TAG_CONTENT_END);
			
			StringBuilder builder=new StringBuilder();
			if(abstracts!=null && !abstracts.isEmpty() && !abstracts.contains("null")){
				builder.append(abstracts);
				builder.append("\n");
			}
			
			if(contents!=null && !contents.isEmpty()){
				builder.append(contents);
//				builder.append("\n");
			}
			
			return builder.toString();
		}
		
		return null;
	}

}
