package us.codecraft.webmagic.newscrawler;

import java.util.ArrayList;
import java.util.List;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.SpiderListener;
import us.codecraft.webmagic.newscrawler.NewsCrawlerProcessor.NewsProcess;
import us.codecraft.webmagic.scheduler.FileCacheQueueScheduler;

public class DWNewsPageProcessor extends NewsCrawlerProcessor implements NewsProcess {

	public DWNewsPageProcessor() {
		super();
		// TODO Auto-generated constructor stub
		mNewsProcess=this;
	}
	//http://www.dw.com/search/en?languageCode=en&origin=gN&item=Xinjiang&searchNavigationId=9097
	@Override
	public boolean isConvert(Page page) {
		// TODO Auto-generated method stub
		return page.getUrl().regex("http://www\\.dw\\.com/search/en\\?languageCode=en&origin=gN&item=.*&searchNavigationId=\\d+").match();
	}
//http://www.dw.com/search/?languageCode=en&item=Hui&searchNavigationId=9097&sort=RELEVANCE&resultsCounter=79
	@Override
	public boolean isList(Page page) {
		// TODO Auto-generated method stub
		return page.getUrl().regex("http://www\\.dw\\.com/search/\\?languageCode=en&item=.*&searchNavigationId=9097&sort=RELEVANCE&resultsCounter=\\d+").match();
	}
//http://www.dw.com/en/the-hui-chinas-preferred-muslims/a-36699666
	@Override
	public boolean isContent(Page page) {
		// TODO Auto-generated method stub
		return page.getUrl().regex("http://www\\.dw\\.com/en/.*/a-\\d+").match();
	}

	@Override
	public void convertProcess(Page page) {
		// TODO Auto-generated method stub
		String num=page.getHtml().xpath("//span[@class='lotsOfResults']/span[@class='hits all']/text()").toString().trim();
		String query=page.getUrl().regex("item=(.*)&").toString();
		String url="http://www.dw.com/search/?languageCode=en&item="+query+"&searchNavigationId=9097&sort=RELEVANCE&resultsCounter="+num;
		page.addTargetRequest(new Request(url)
				.putExtra(AsianViewDetailItem.ORIGIN_URL, page.getUrl().toString())
				.putExtra(AsianViewDetailItem.CONVERT_URL, page.getUrl().toString())
				.putExtra(AsianViewDetailItem.ADDNEW, page.getRequest().getExtra(AsianViewDetailItem.ADDNEW)));
	}

	@Override
	public void listProcess(Page page) {
		// TODO Auto-generated method stub
		List<String> urls=page.getHtml().xpath("//div[@class='searchResult']/a/@href").all();
		
		for (String url : urls) {
			page.addTargetRequest(new Request(url)
				.putExtra(AsianViewDetailItem.ORIGIN_URL, page.getUrl().toString())
//				.putExtra(AsianViewDetailItem.CONVERT_URL, page.getUrl().toString())
				.putExtra(AsianViewDetailItem.ADDNEW, page.getRequest().getExtra(AsianViewDetailItem.ADDNEW)));
		}
	}
	
	public static String timeFormat(String time){
//		DateFormat originFormat=new SimpleDateFormat(ASIANEWS_DATE_FORMAT);
//		String corpus_time= originFormat.format(time);
//		return corpus_time;
		
		String[] date=time.trim().split("\\.");
		return String.format(CorpusFormatter.CORPUS_DATE_FORMAT, date[2],date[1],date[0]);
	}

	@Override
	public void contentProcess(Page page) throws ContentMatchException{
		//提取内容
		String time=timeFormat(page.getHtml().xpath("//ul[@class='smallList']/li[1]/text()").toString().trim());
		
		page.putField(AsianViewDetailItem.TIME, time);
		page.putField(AsianViewDetailItem.URL, page.getUrl().toString());
		page.putField(AsianViewDetailItem.TITLE, page.getHtml().xpath("//div[@id='bodyContent']/div[@class='col3']/h1/text()").toString());
//		String abstracts=page.getHtml().xpath("//div[@class='sub_title_det']/text()").toString();
//		System.out.println("Abstract: "+ abstracts);
		page.putField(AsianViewDetailItem.ABSTRACT, page.getHtml().xpath("//div[@id='bodyContent']/div[@class='col3']/p[@class='intro']/text()").toString());
//		String content=page.getHtml().xpath("//div[@class='longText']/tidyText()").toString();
		List<String> contents=page.getHtml().xpath("//div[@class='longText']/p/text()").all();
		StringBuilder builder=new StringBuilder();
		for (String para : contents) {
			builder.append(para);
			builder.append("\n");
			
		}
		
		String content=builder.toString();
		
//		System.out.println("Content: "+CorpusFormatter.removeTag(content));
		content=CorpusFormatter.removeTag(content);
		page.putField(AsianViewDetailItem.CONTENT, content);
		String l_content=content.toLowerCase();
		
		if(skipCrawler(l_content)){
			page.setSkip(true);		
			System.out.println("[WARNING]Page :"+page.getUrl().toString()+" will NOT download......[WARNING]");
			throw new ContentMatchException();
		}

		if(page.getRequest().getExtra(AsianViewDetailItem.ADDNEW) !=null &&(Boolean) page.getRequest().getExtra(AsianViewDetailItem.ADDNEW)){
			//提取链接
			List<String> urls=page.getHtml().xpath("//div[@class='group']/div[@class='linkList intern']/a").regex("href=\"(.*)\"").all();

			System.out.println(String.format("Import %s Urls",urls.size()));
			System.out.println(urls);
			
			
			//在Url的基础上添加【链接来源】
			//DW recommends
			for (String url : urls) {
				page.addTargetRequest(new Request(url).putExtra(AsianViewDetailItem.ORIGIN_URL, page.getUrl().toString()).putExtra(AsianViewDetailItem.ADDNEW, true));
			}
			
			//Related content
			List<String> relateUrls=page.getHtml().xpath("//div[@class='col3 relatedContent']/div[@class='col1']/div[@class='news']/a").regex("href=\"(.*)\"").all();
			System.out.println(String.format("Import %s Related Urls",relateUrls.size()));
			System.out.println(relateUrls);
			
			for (String url : relateUrls) {
				page.addTargetRequest(new Request(url).putExtra(AsianViewDetailItem.ORIGIN_URL, page.getUrl().toString()).putExtra(AsianViewDetailItem.ADDNEW, true));
			}
			
		}
		
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		List<Request> requests=new ArrayList<Request>();
		boolean addNew=false;
		FileUtils.loadUrlFile("./dwNews_url.txt", requests,addNew);
//		FileUtils.loadUrlFile(FileNumberVerifier.VERIFIED_SUCCESS_CRAWLER_LOG_PATH, requests,addNew);
		Request[] requests2=requests.toArray(new Request[requests.size()]);
		for (Request request : requests2) {
			System.out.println("Index("+requests.indexOf(request)+")"+request.getUrl()+"\nOrigin Url:"+request.getExtra(AsianViewDetailItem.ORIGIN_URL)
			+"\nAddNew:"+request.getExtra(AsianViewDetailItem.ADDNEW));
		}
		
		//添加监听器，用于观察下载器Downloader是否成功
		DWNewsPageProcessor processor=new DWNewsPageProcessor();
		List<SpiderListener> sl=new ArrayList<SpiderListener>();
		sl.add(processor);
		
		
		Spider.create(processor).setSpiderListeners(sl).scheduler(new FileCacheQueueScheduler(".//"+FileUtils.docName+"//FileCache//"))
		.addPipeline(new AsiaNewsPipeline())

		//详细内容英文版
//		.addRequest(requests2/*requests.toArray(new Request[requests.size()])*/)
//		.addRequest(getInitRequest("http://www.dw.com/en/tibetan-exiles-vote-from-around-the-world/a-19129677"))
//		.addRequest(getInitRequest("http://www.dw.com/en/muslims-in-china/av-18494285"))
//		.addRequest(getInitRequest("http://www.dw.com/en/multiple-bombings-hit-thailand/a-19469420"))
//		.addRequest(getInitRequest("http://www.dw.com/en/australia-cambodia-refugee-resettlement-deal-slammed-by-rights-groups/a-17957640"))
//		.addRequest(getInitRequest("http://www.dw.com/en/human-rights-still-being-violated-in-china-activists/a-5213690"))
//		.addRequest(getInitRequest("http://www.dw.com/en/chinese-police-shoot-dead-terrorist-uighurs/a-18582486"))
//		.addRequest(getInitRequest("http://www.dw.com/en/xinjiang-restrictions-on-religion-may-lead-to-uighur-radicalization/a-17841070"))
//		.addRequest(getInitRequest("http://www.dw.com/en/chinese-police-shoot-dead-terrorist-uighurs/a-18582486"))
		//列表
		.addRequest(getInitRequest("http://www.dw.com/search/en?languageCode=en&origin=gN&item=Rebiya+Kadeer&searchNavigationId=9097"))
		.thread(4)
		.run();
	}

}
