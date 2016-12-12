package us.codecraft.webmagic.processor.example;

import java.util.List;

import org.apache.http.HttpHost;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

public class AsiaNewsWebCrawler implements PageProcessor{

	Site site=Site.me().setRetryTimes(3).setTimeOut(1000).setCycleRetryTimes(3).setCharset("utf-8").setHttpProxy(new HttpHost("127.0.0.1",8087))
			//.setUserAgent("Mozilla/5.0 (Windows NT 6.3; WOW64; rv:50.0) Gecko/20100101 Firefox/50.0");
			.setUserAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_2) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.65 Safari/537.31");
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
//		String[] Urls={
//				//关键词：新疆、维族、乌鲁木齐
//				"http://www.asianews.it/index.php?l=en&dacercare=xinjiang",
//				"http://www.asianews.it/index.php?l=en&dacercare=uyghur",
//				"http://www.asianews.it/index.php?l=en&dacercare=urumqi",
//				//关键词：热比娅、卡德尔
//				"http://www.asianews.it/index.php?l=en&dacercare=kadeer",
//				"http://www.asianews.it/index.php?l=en&dacercare=rebiya",
//				
//				};
		Spider.create(new AsiaNewsWebCrawler())
			.addPipeline(new AsiaNewsPipeline())
//			.addPipeline(new FilePipeline(".\\asianews2"))
	//		.addPipeline(new FilePipeline("./"))
	//		.addUrl("http://blog.sina.com.cn/s/blog_58ae76e80100gcsa.html")
//			.scheduler(new FileCacheQueueScheduler)
			//详细内容英文版
//			.addUrl("http://www.asianews.it/news-en/Rebiya-Kadeer-says-10,000-people-disappeared-in-one-night-in-Urumqi,-complains-about-US-silence-15911.html")
			//详细内容意大利版
			.addUrl("http://www.asianews.it/index.php?idn=1&art=36147")
//			.addUrl("http://www.asianews.it/notizie-it/Cina:-graziata-la-moglie-di-Bo-Xilai,-in-carcere-un-avvocato-per-i-diritti-umani-36147.html")
			//列表
//			.addUrl("http://www.asianews.it/index.php?l=en&dacercare=rebiya")
			.thread(4)
			.run();
	}
	
	int counter=0;

	public boolean isStartCrawlering(String l_content){
		return //地点
				!l_content.contains("tiananmen")||!l_content.contains("xinjiang")
				||!l_content.contains("urumqi")||!l_content.contains("china")					
				//||!l_content.contains("autonomy")
				||!l_content.contains("kashgar")
				
				//种群
				||!l_content.contains("uyghur")
				||!l_content.contains("chinese")
				||!l_content.contains("muslim")
				||!l_content.contains("islamic")
				||!l_content.contains("east turkestan")
				//人物
				||!l_content.contains("kadeer")||!l_content.contains("rebiya")
				
				//思想
				||!l_content.contains("extremism")
				||!l_content.contains("terrorism")
				||!l_content.contains("racism")
				//?
				||!l_content.contains("religious")||!l_content.contains("ethnic")	
				//组织
				||!l_content.contains("world uyghur congress")
				//诉求
				||!l_content.contains("independence")||!l_content.contains("freedom")
				||!l_content.contains("human rights")
				
				//对立面（人称）				
				||!l_content.contains("extremist")||!l_content.contains("activist")
				||!l_content.contains("protester")
				//行动
				||!l_content.contains("unrest")||!l_content.contains("violence")
				||!l_content.contains("attack")
				||!l_content.contains("self-immolation")||!l_content.contains("ramadam")					
				||!l_content.contains("threat")
				
				//影响
				||!l_content.contains("oppression")||!l_content.contains("crackdown")
				||!l_content.contains("arrest")||!l_content.contains("repression")
				||!l_content.contains("persecution")
				//结果
				||!l_content.contains("death")||!l_content.contains("sentence");
				
				
	}
	
	@Override
	public void process(Page page) {
		// TODO Auto-generated method stub
		System.out.println("*************Entry Asia News Page Processor **************");
		
		System.out.println("Url :"+page.getUrl().toString()+" accessed");
		
		if(page.getUrl().regex("http://www\\.asianews\\.it/news-en/.*").match()){
			//提取内容
			page.putField(AsianViewDetailItem.TIME, page.getHtml().xpath("//div[@class='pre_articolo_det']/text()").toString());
			page.putField(AsianViewDetailItem.URL, page.getUrl().toString());
			page.putField(AsianViewDetailItem.TITLE, page.getHtml().xpath("//div[@class='titolo_articolo_det']/h1/text()").toString());
			String abstracts=page.getHtml().xpath("//div[@class='sub_title_det']/text()").toString();
//			System.out.println("Abstract: "+ abstracts);
			page.putField(AsianViewDetailItem.ABSTRACT, page.getHtml().xpath("//div[@class='sub_title_det']/text()").toString());
			String content=page.getHtml().xpath("//div[@class='articolo']/tidyText()").toString();
//			System.out.println("Content: "+CorpusFormatter.removeTag(content));
			page.putField(AsianViewDetailItem.CONTENT, CorpusFormatter.removeTag(content));
			String l_content=content.toLowerCase();
			
			if(isStartCrawlering(l_content)){
				page.setSkip(true);				
			}
//			if(content.co)
//			if(counter<1){
			//提取链接
//			List<String> urls=page.getHtml().xpath("//td[@class='testo']/a[@href]").regex("href=\"(.*)\"").all();
			List<String> urls=page.getHtml().xpath("//td[@class='testo']/a/@href")/*.regex("href=\"(.*)\"")*/.all();
			urls.add(page.getUrl().toString());
			System.out.println(String.format("Import %s Urls",urls.size()));
			
			page.addTargetRequests(urls);
			
//			counter++;
//			}
			
		}else if(page.getUrl().regex("http://www\\.asianews\\.it/index\\.php\\?l=en&dacercare=.*").match()){
			System.out.println("列表");
			List<String> urls=page.getHtml().xpath("//tbody/tr/a[@href]").regex("href=\"(.*)\"").regex("http://www\\.asianews\\.it/index.php\\?idn=\\d+&art=\\d+").all();
			System.out.println(page.getHtml().toString());
		}else if(page.getUrl().regex("http://www\\.asianews\\.it/index\\.php\\?idn=\\d+&art=\\d+").match() || page.getUrl().regex("http://www.asianews.it/notizie-it/.*").match()){
			
//			System.out.println("Url Convert:"+page.getHtml().xpath("//div/div/div/a[2]/@href"));
			page.addTargetRequest(page.getHtml().xpath("//div/div/div/a[2]/@href").toString());
			
		}
		
		System.out.println("*************Exit Asia News Page Processor **************");
	}

	@Override
	public Site getSite() {
		// TODO Auto-generated method stub
		return site;
	}

}
