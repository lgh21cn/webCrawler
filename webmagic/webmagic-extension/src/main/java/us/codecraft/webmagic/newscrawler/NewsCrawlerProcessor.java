package us.codecraft.webmagic.newscrawler;

import org.apache.http.HttpHost;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.SpiderListener;
import us.codecraft.webmagic.processor.PageProcessor;

public class NewsCrawlerProcessor implements PageProcessor,SpiderListener{
	Site site=Site.me().setRetryTimes(3).setTimeOut(1000).setCycleRetryTimes(3).setCharset("utf-8").setHttpProxy(new HttpHost("127.0.0.1",8087))
			//.setUserAgent("Mozilla/5.0 (Windows NT 6.3; WOW64; rv:50.0) Gecko/20100101 Firefox/50.0");
			.setUserAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_2) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.65 Safari/537.31");
//			.addCookie("www.asianews.it", "PHPSESSID", "7l0qd80dktlh7g1l1rqrs95a50")
//			.addCookie("PHPSESSID", "7l0qd80dktlh7g1l1rqrs95a50")
//			.addHeader("Cookie", "PHPSESSID=7l0qd80dktlh7g1l1rqrs95a50")
//			.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
//			.addHeader("Connection", "keep-alive");
	
	public NewsCrawlerProcessor() {
		// TODO Auto-generated constructor stub
		//初始化SuccessLog文件
		FileUtils.writeErrorLog(SUCCESS_LOG_PATH, "");
		//初始化ErrorLog文件
		FileUtils.writeErrorLog(ERROR_LOG_PATH, "");
	}

	public interface NewsProcess{
		//判断类型
		public boolean isConvert(Page page);
		public boolean isList(Page page);
		public boolean isContent(Page page);
		//处理
		public void convertProcess(Page page);
		public void listProcess(Page page);
		public void contentProcess(Page page);
		
	}
	
	protected NewsProcess mNewsProcess;	
	
	public void setNewsProcess(NewsProcess newsProcess) {
		this.mNewsProcess = newsProcess;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	
	public boolean skipCrawler(String l_content){
		return //地点
				!l_content.contains("tiananmen")&&!l_content.contains("xinjiang")
				&&!l_content.contains("urumqi")
				//&&!l_content.contains("china")					
				//&&!l_content.contains("autonomy")
				&&!l_content.contains("kashgar")
				
				//种群
				&&!l_content.contains("uyghur")
				//&&!l_content.contains("chinese")
//				&&!l_content.contains("muslim")
//				&&!l_content.contains("islamic")
				&&!l_content.contains("east turkestan")
				//人物
				&&!l_content.contains("kadeer")&&!l_content.contains("rebiya")
				
				//思想
//				&&!l_content.contains("extremism")
//				&&!l_content.contains("terrorism")
//				&&!l_content.contains("racism")
				//?
//				&&!l_content.contains("religious")&&!l_content.contains("ethnic")	
				//组织
				&&!l_content.contains("world uyghur congress")
				&&!l_content.contains("WUC")
				//诉求
//				&&!l_content.contains("independence")&&!l_content.contains("freedom")
//				&&!l_content.contains("human rights")
				
				//对立面（人称）				
//				&&!l_content.contains("extremist")&&!l_content.contains("activist")
//				&&!l_content.contains("protester")
				//行动
//				&&!l_content.contains("unrest")&&!l_content.contains("violence")
//				&&!l_content.contains("attack")
//				&&!l_content.contains("self-immolation")
				&&!(l_content.contains("ramadam") &&l_content.contains("china"))					
//				&&!l_content.contains("threat")
				
				//影响
//				&&!l_content.contains("oppression")&&!l_content.contains("crackdown")
//				&&!l_content.contains("arrest")&&!l_content.contains("repression")
//				&&!l_content.contains("persecution")&&!l_content.contains("clash")
				//结果
//				&&!l_content.contains("death")&&!l_content.contains("sentence")
				//其它
				&&!l_content.contains("uighur");
	}

	private int mMatchCounter=0;
	private int mErrorCounter=0;
	
	@SuppressWarnings("serial")
	class MatchException extends Exception{
		public MatchException() {
			// TODO Auto-generated constructor stub
			super("No Matches");
		}
		
		
		
	}
	
	
	public static final String MATCH_PATH=".//"+FileUtils.docName+"//Match_Error//";
	public static final String ERROR_PATH=".//"+FileUtils.docName+"//Error_Log//";
	
	public static final String LOG_ORIGIN_URL_FORMAT="<ORIGIN_URL>"+CorpusFormatter.CRLF+"%s"+CorpusFormatter.CRLF+"<\\ORIGIN_URL>"+CorpusFormatter.CRLF;
	public static final String LOG_URL_FORMAT=CorpusFormatter.URL;
	@Override
	public void process(Page page) {
		// TODO Auto-generated method stub
		System.out.println("******************************Entry Page Process*******************************");
		System.out.println("Url :"+page.getUrl().toString()+" accessed");
		System.out.println("This URL is from "+page.getRequest().getExtra(AsianViewDetailItem.ORIGIN_URL));
		try {
			if(mNewsProcess.isConvert(page)){
				page.setSkip(true);
				mNewsProcess.convertProcess(page);
			}else if(mNewsProcess.isList(page)){
				page.setSkip(true);
				mNewsProcess.listProcess(page);
			}else if(mNewsProcess.isContent(page)){
				mNewsProcess.contentProcess(page);
			}else{
				System.out.println("No Matches");page.setSkip(true);
				throw new MatchException();
			}
		} catch(MatchException e){
			// TODO: handle exception
			String url=page.getUrl().toString();
			final String fileName=String.format(MATCH_PATH+"%s.match.log", ++mMatchCounter);
			FileUtils.writeErrorLog(fileName, e.toString()+"\n"+String.format(LOG_ORIGIN_URL_FORMAT, page.getRequest().getExtra(AsianViewDetailItem.ORIGIN_URL))+String.format(CorpusFormatter.URL, url));
		}catch (Exception e) {
			// TODO: handle exception
			String url=page.getUrl().toString();
			final String fileName=String.format(ERROR_PATH+"%s.err.log", ++mErrorCounter);
			FileUtils.writeErrorLog(fileName, e.toString()+"\n"+String.format(LOG_ORIGIN_URL_FORMAT, page.getRequest().getExtra(AsianViewDetailItem.ORIGIN_URL))+String.format(CorpusFormatter.URL, url));
		}
		
		
		System.out.println("******************************Exit Page Process*******************************");
	}

	@Override
	public Site getSite() {
		// TODO Auto-generated method stub
		return site;
	}
	
	public static Request getInitRequest(String url){
		return new Request(url).putExtra(AsianViewDetailItem.ORIGIN_URL, AsianViewDetailItem.ORIGIN_URL_CONTENT);
	}

	
	public static final String SUCCESS_LOG_PATH=".//"+FileUtils.docName+"//success_log.txt";
	public static final String ERROR_LOG_PATH=".//"+FileUtils.docName+"//error_log.txt";
	
	@Override
	public void onSuccess(Request request) {
		// TODO Auto-generated method stub
		System.out.println("****************Spider onSuccess Start**************");
		
		boolean append=true;
		FileUtils.writeErrorLog(SUCCESS_LOG_PATH, String.format("<URL>%s</URL>\n", request.getUrl()),append);
		
		System.out.println("****************Spider onSuccess End**************");
	}

	@Override
	public void onError(Request request) {
		// TODO Auto-generated method stub
		System.out.println("****************Spider onError Start**************");
		
		boolean append=true;
		FileUtils.writeErrorLog(ERROR_LOG_PATH, String.format("<URL>%s</URL>\n", request.getUrl()),append);
		
		System.out.println("****************Spider onError End**************");
	}
	
	

}
