package us.codecraft.webmagic.processor.example;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.FilePipeline;
import us.codecraft.webmagic.processor.PageProcessor;

public class SinaBlogContentWebCrawler implements PageProcessor {

	Site site=Site.me().setRetryTimes(3).setTimeOut(1000).setCycleRetryTimes(3).setCharset("utf-8");
	
	int count=0;
	
	@Override
	public void process(Page page) {
		// TODO Auto-generated method stub
		if(page.getUrl().regex("http://blog\\.sina\\.com\\.cn/s/articlelist_.*\\.html").match()){//文章列表
			//提取文章内容连接
			List<String> detail_urls=page.getHtml().xpath("//div[@class='articleList']/div/p/span/a[@href]").regex("http://blog\\.sina\\.com\\.cn/s/blog_.*\\.html").all();
			System.out.println("Detail Url List:");
			System.out.println(detail_urls);

			count+=detail_urls.size();System.out.println("File Number is "+count);
//			page.addTargetRequests(detail_urls);
			
			
			//提取下一页文章列表
			List<String> split_urls=page.getHtml().xpath("//div[@class='SG_page']/ul/li/a[@href]").regex("http://blog\\.sina\\.com\\.cn/s/articlelist_.*\\.html").all();
			Set<String> split_urls_set=new HashSet<String>(split_urls);
			System.out.println("Split Url Set:");
			System.out.println(split_urls_set);
			
			split_urls.clear();
			split_urls.addAll(split_urls_set);
			
			page.addTargetRequests(split_urls);
			
		}else if(page.getUrl().regex("http://blog\\.sina\\.com\\.cn/s/blog_.*\\.html").match()){//具体文章内容
			//other:
			//"//div[@class='articalTitle']/h2"
			//mine:
			//"//div[@class='articalTitle']/h2/text()"
			page.putField("title", page.getHtml().xpath("//div[@class='articalTitle']/h2/text()").toString());
			//other:
			//"//div[@id='articlebody']//span[@class='time SG_txtc']"
			//mine:
			//"//div[@class='articalTitle']/span[@class='time SG_txtc']/text()"
			page.putField("time", page.getHtml().xpath("//div[@class='articalTitle']/span[@class='time SG_txtc']/text()").regex("\\((.*)\\)").toString());
			//other:
			//"//div[@id='articlebody']//div[@class='articalContent']"
			//mine:
			//"//div[@id='sina_keyword_ad_area2']//tidytext"
			page.putField("content", page.getHtml().xpath("//div[@id='sina_keyword_ad_area2']//tidyText()").toString());
		}else{
			assert false:"Can't match above two types of Url";
		}
		
		
		
	}

	@Override
	public Site getSite() {
		// TODO Auto-generated method stub
		return site;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Spider.create(new SinaBlogContentWebCrawler())
			.addPipeline(new FirstWebCrawlerPipeline())
//			.addPipeline(new FilePipeline("./"))
//			.addUrl("http://blog.sina.com.cn/s/blog_58ae76e80100gcsa.html")
			.addUrl("http://blog.sina.com.cn/s/articlelist_1487828712_0_1.html")
			.thread(4)
			.run();
	}

}
