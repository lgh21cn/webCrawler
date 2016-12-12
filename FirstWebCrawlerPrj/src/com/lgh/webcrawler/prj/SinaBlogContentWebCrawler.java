package com.lgh.webcrawler.prj;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

public class SinaBlogContentWebCrawler implements PageProcessor {

	Site site=Site.me().setRetryTimes(3).setTimeOut(1000).setCycleRetryTimes(3).setCharset("utf-8");
	
	@Override
	public void process(Page page) {
		// TODO Auto-generated method stub
		//other:
		//"//div[@class='articalTitle']/h2"
		//mine:
		//"//div[@class='articalTitle']/h2/text()"
		page.putField("title", page.getHtml().xpath("//div[@class='articalTitle']/h2/text()"));
		//other:
		//"//div[@id='articlebody']//span[@class='time SG_txtc']"
		//mine:
		//"//div[@class='articalTitle']/span[@class='time SG_txtc']/text()"
		page.putField("time", page.getHtml().xpath("//div[@class='articalTitle']/span[@class='time SG_txtc']/text()").regex("\\((.*)\\)"));
		//other:
		//"//div[@id='articlebody']//div[@class='articalContent']"
		//mine:
		//"//div[@id='sina_keyword_ad_area2']//tidytext"
		page.putField("content", page.getHtml().xpath("//div[@id='sina_keyword_ad_area2']//tidyText()"));
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
			.addUrl("http://blog.sina.com.cn/s/blog_58ae76e80100gcsa.html")
			.thread(4)
			.run();
	}

}
