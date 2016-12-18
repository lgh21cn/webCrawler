package us.codecraft.webmagic.newscrawler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

public class DuplicatedFileRemover {
	
//	Set<String> titleSet;
//	Set<String> abstractSet;
//	Set<String> urlSet;
	Map<String,String> attributePathMap;
	
	
	List<String> deleteFileList;
	Map<String, String> deleted_scrMap;
	
	List<Pattern> patterns;
	List<RelevantFile> similarFiles;
//	List<String> 
	
	
	
	private String getContentBy(String content,String start,String end){
		if(content!=null && start!=null && end!=null){
			int start_index=content.indexOf(start);
			int end_index=content.indexOf(end);
			return start_index<end_index?content.substring(start_index+start.length(), end_index).trim():"";
		}
		return "";
	}
	
	
	
	private boolean verifyFile(String filePath){
		String allContent=FileUtils.readFile(filePath);
		String title=getContentBy(allContent, CorpusFormatter.TAG_TITLE_START, CorpusFormatter.TAG_TITLE_END);
		String abstracts=getContentBy(allContent, CorpusFormatter.TAG_ABSTRACT_START, CorpusFormatter.TAG_ABSTRACT_END);
		String url=getContentBy(allContent, CorpusFormatter.TAG_URL_START, CorpusFormatter.TAG_URL_END);
		String similarityPath=null;
		if((similarityPath=isToBeDeleted(title, abstracts, url))!=null){
			
			deleteFileList.add(filePath);
			similarFiles.add(new RelevantFile(filePath, title, abstracts, url).setSimilarFilePath(similarityPath));
			return false;
		}else{
//			titleSet.add(title);
//			abstractSet.add(abstracts);
//			urlSet.add(url);
			
			attributePathMap.put(title, filePath);
			if(abstracts!=null && !abstracts.isEmpty())
				attributePathMap.put(abstracts, filePath);
			
			attributePathMap.put(url, filePath);
			return true;
		}
	}
	private static final String TMP_LOG =".//"+FileUtils.docName+"//total//tmp_log.txt";
	private static final String DELETED_FILES_LOG=".//"+FileUtils.docName+"//total//deleted_files_log.txt";
	private static final String SIMILARITY_FILE_PATTERN="File path:%s\n"
														+ "Similarity File Path:%s\n";
	private void verifyFiles(String rootPath){
		Set<String> filePaths=new HashSet<String>();
		
		FileUtils.readFileNames(rootPath, filePaths, patterns);
		
		StringBuilder builder=new StringBuilder();
		for (String path : filePaths) {
			builder.append(path);
			builder.append("\n");
		}
		
		FileUtils.writeErrorLog(TMP_LOG, builder.toString());
		
		for (String path : filePaths) {
			if(verifyFile(path)){
				System.out.println("Path is added.");
			}else{
				System.out.println("Path is deleted");
			}
		}
		
		builder.setLength(0);
		for (RelevantFile file: similarFiles) {
			builder.append(String.format(SIMILARITY_FILE_PATTERN, file.filePath,file.similarFilePath));
		}
		
		FileUtils.writeErrorLog(DELETED_FILES_LOG, builder.toString());
		
		
//		return filePaths;
	}
	
	public void duplicatedFileRemove(String root){
		verifyFiles(root);
		
		Set<String> deleteFileSet=new HashSet<String>(deleteFileList);
		moveDeletedFiles(deleteFileSet);
	}
	
	private static final String DELETED_FILE_ROOT_PATH=".//"+FileUtils.docName+"//total//DELETE";
	private void moveDeletedFiles(Set<String> deletedFiles){
//		for (String delPath : deletedFiles) {
			FileUtils.moveFiles(DELETED_FILE_ROOT_PATH, deletedFiles.toArray(new String[0]));
//		}
	}
	
	private String isToBeDeleted(String title,String abstracts,String url){
		if(attributePathMap.getOrDefault(title,null)!=null  ){
			return attributePathMap.get(title);
		}
		if(abstracts!=null && !abstracts.isEmpty()&& attributePathMap.getOrDefault(abstracts,null)!=null){
			return attributePathMap.get(abstracts);
		}
		if(attributePathMap.getOrDefault(url,null)!=null){
			return attributePathMap.get(url);
		}
		
		return null;

	}
	
	private Pattern[] getPatterns(String...patterns){
		List<Pattern> patternList=new ArrayList<Pattern>();
		for (String pattern : patterns) {
			patternList.add(Pattern.compile(pattern));
		}
		return patternList.toArray(new Pattern[0]);
	}

	public DuplicatedFileRemover() {
		super();
		// TODO Auto-generated constructor stub
		attributePathMap=new HashMap<String, String>();
//		titleSet=new HashSet<String>();
//		abstractSet=new HashSet<String>();
//		urlSet=new HashSet<String>();
//		
		deleteFileList=new ArrayList<String>();
		
		deleted_scrMap=new HashMap<String, String>();
		
		patterns=new ArrayList<Pattern>();
		
		String[] t_pattern={"\\d+-\\d+.txt"};
		for (String t_p : t_pattern) {
//			Pattern pattern=;
			patterns.add(Pattern.compile(t_p));
		}
		
		similarFiles=new ArrayList<RelevantFile>();
	}


// 
//	private static final String TMP_DELETE_FILE_PATH=".//asianews//total//202//20050309-102.txt";
	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		FileUtils.moveFiles(DELETED_FILE_ROOT_PATH, TMP_DELETE_FILE_PATH);
		new DuplicatedFileRemover().duplicatedFileRemove(".//"+FileUtils.docName+"//total");
		
		
		
	}

}
