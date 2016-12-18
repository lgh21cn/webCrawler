package us.codecraft.webmagic.newscrawler;

public class RelevantFile  {
	
	public static final int TITLE_SIMILARITY=1;
	public static final int ABSTRACT_SIMILARITY=2;
	public static final int URL_SIMILARITY=3;
	public static final int NONE_SIMILARITY=0;
	
	String filePath;
	String title;
	String abstracts;
	String url;
	
	String similarFilePath; 
	public RelevantFile(String filePath,String title, String abstracts, String url) {
		super();
		this.filePath=filePath;
		this.title = title;
		this.abstracts = abstracts;
		this.url = url;
	}
	
	
	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		return this.title.equals(((RelevantFile)obj).title)
				||this.abstracts.equals(((RelevantFile)obj).abstracts)
				||this.url.equals(((RelevantFile)obj).abstracts);
	}
	
	
	public RelevantFile setSimilarFilePath(String similarFilePath) {
		this.similarFilePath = similarFilePath;
		return this;
	}



	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}


}
