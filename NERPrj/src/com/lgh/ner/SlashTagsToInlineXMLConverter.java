package com.lgh.ner;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SlashTagsToInlineXMLConverter {
	
	
	private String startTag;
	private String endTag;
	private String tag;
	private String final_tag;
	
	private int start_tag_length;
	private int end_tag_length;
	
	public static final String START_FORMAT="<%s>";
	public static final String END_FORMAT="</%s>";
	public static final String MORE_SPACE="\\s{2,}";
	public static final String SPACE=" ";
	public static final String WORD_SLASH_TAG_FORMAT="%s/%s"+SPACE;
	
	public static final String STANFORD_NER_ROOT_PATH="C:/stanford-ner-2015-12-09/";

	public SlashTagsToInlineXMLConverter(String tag) {
		this.startTag = String.format(START_FORMAT, tag);
		this.endTag = String.format(END_FORMAT, tag);
		this.tag=tag;
		this.final_tag=tag.equals(OTHER)?"O":tag;
		
		start_tag_length=startTag.length();
		end_tag_length=endTag.length();
	}
	
	public String getResult(String content){
		if(content==null || (content=content.trim()).length()==0) return "";
		
		int startIndex=content.indexOf(startTag);
		int endIndex=content.indexOf(endTag, startIndex);

		StringBuilder sb=new StringBuilder();
		while(startIndex+start_tag_length<endIndex){
			
			
			String subContent=content.substring(startIndex+start_tag_length, endIndex);
			String[] subs=subContent.split(" ");
			
			
			for (String sub : subs) {
//				System.out.println(sub);
				
				String[] wordAndTag=sub.split("/");
				if(wordAndTag.length!=2){
					
					sb.append(String.format(WORD_SLASH_TAG_FORMAT, sub,final_tag));
					continue;
				}
				
				sb.append(String.format(WORD_SLASH_TAG_FORMAT, wordAndTag[0],final_tag));
				
				
			}
			subContent=sb.toString();
			sb.setLength(0);
			
			
			//pre+sub+post
			content=content.substring(0,startIndex)+subContent+content.substring(endIndex+end_tag_length, content.length());
			
			startIndex=content.indexOf(startTag, startIndex);
			endIndex=content.indexOf(endTag, startIndex);
			
			
		}
		
		content=content.replaceAll(MORE_SPACE, SPACE);
		
		return content;
	}

	public static final String PERSON="PERSON";
	public static final String ORGNIAZTION="ORGANIZATION";
	public static final String LOCATION="LOCATION";
	public static final String OTHER="OTHER";
	
	public static final String UYGHUR_PREFIX="U_";
	
	public static final String U_PERSON=UYGHUR_PREFIX+PERSON;
	public static final String U_ORGNIAZTION=UYGHUR_PREFIX+ORGNIAZTION;
	public static final String U_LOCATION=UYGHUR_PREFIX+LOCATION;
	
	public static final String[] TAGS={
			PERSON,
			ORGNIAZTION,
			LOCATION,
			OTHER,
			U_PERSON,
			U_ORGNIAZTION,
			U_LOCATION,
	};

	public static final String NER_ROOT_PATH="C:/eclipse/workspace/NERPrj/";
	public static final String NER_UN_CONVERTED_PATH=NER_ROOT_PATH+"self_tagged_un_converted";
	public static final String NER_CONVERTED_PATH=NER_ROOT_PATH+"self_tagged_converted";
	

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String content="aaabbb";
		System.out.println("Origin:\n"+content);
		System.out.println("New:\n"+content.replace("a", "c"));
		System.out.println(content.split("/")[0]);
		
		String tmp="The/O foreign/O minister/O criticizes/O <PERSON>countries/O th</PERSON>at/O welcome/O the/O <PERSON>Dalai/O Lama/O</PERSON> ,/O seen/O as/O merely/O ``/O a/O political/O leader/O ./O ''/O \n\r";
		
		SlashTagsToInlineXMLConverter converter=new SlashTagsToInlineXMLConverter("PERSON");
		System.out.println("Origin");
		System.out.println(tmp);
		System.out.println("New:");
		System.out.println(converter.getResult(tmp));
//		System.out.println(converter.getResult(tmp).replaceAll(MORE_SPACE, SPACE));
		
		String contents=FileUtils.readFile(STANFORD_NER_ROOT_PATH+"20031204-222.txt",false);
		String[] subs=contents.split("\n\r|\r\n|\n");
		StringBuilder sb=new StringBuilder();
		for (String sub : subs) {
			System.out.println("Origin:");
			System.out.println(sub);
			System.out.println("News:");
			System.out.println(converter.getResult(sub));
			sb.append(converter.getResult(sub));
			sb.append("\n");
		}
		
		FileUtils.writeErrorLog(STANFORD_NER_ROOT_PATH+"20031204-222_compared.txt",sb.toString());
		sb.setLength(0);
		
		SlashTagsToInlineXMLConverter[] converters=new SlashTagsToInlineXMLConverter[TAGS.length];
		for(int i=0;i<TAGS.length;i++){
			converters[i]=new SlashTagsToInlineXMLConverter(TAGS[i]);
		}

		convert(STANFORD_NER_ROOT_PATH+"20031204-222.txt", STANFORD_NER_ROOT_PATH+"20031204-222_compared.txt", converters);

		
		List<String> filePaths=new ArrayList<>();
		try {
			FileUtils.readAllFilePaths(NER_UN_CONVERTED_PATH, filePaths, new FileFilter() {
				
				@Override
				public boolean accept(File pathname) {
					// TODO Auto-generated method stub
					
					if(pathname.isDirectory())
						return true;
					else if(pathname.getName().matches("\\d+-\\d+.txt")){
						return true;
					}else{
						return false;
					}
				}
			});
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			
			String destPath;
			final int length=NER_UN_CONVERTED_PATH.length();
			for (String path : filePaths) {
				String subPath=path.substring(length).replace("\\", FileUtils.seperator);
				System.out.println(subPath);
				destPath= NER_CONVERTED_PATH+subPath;
				convert(path, destPath, converters);
				System.out.println(path);
			}
			System.out.println("Number of Files is "+filePaths.size());
		}
		String path="dwNews_Œ Ã‚ª„◊‹.xlsx";
		System.out.println(path.matches("\\d+-\\d+.txt"));
	}
	
	public static void convert(String src,String dest,SlashTagsToInlineXMLConverter[] converters){
		String contents=FileUtils.readFile(src,false);
		for (SlashTagsToInlineXMLConverter converter : converters) {
			
			String[] subs=contents.split("\n\r|\r\n|\n");
			
			StringBuilder sb=new StringBuilder();
			for (String sub : subs) {
				System.out.println("Origin:");
				System.out.println(sub);
				System.out.println("News:");
				System.out.println(converter.getResult(sub));
				sb.append(converter.getResult(sub));
				sb.append("\n");
			}
			
			contents=sb.toString();
			sb.setLength(0);
			
		}
		
		
		FileUtils.writeErrorLog(dest,contents);
		
	}

}
