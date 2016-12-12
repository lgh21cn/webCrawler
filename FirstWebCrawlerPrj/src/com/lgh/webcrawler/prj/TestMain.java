package com.lgh.webcrawler.prj;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestMain {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		Pattern pattern=Pattern.compile("http://blog\\.sina\\.com\\.cn/s/blog_([\\w\\W]*)\\.html");
		Pattern pattern=Pattern.compile("http://blog\\.sina\\.com\\.cn/s/blog_(.*)\\.html");
		String url="http://blog.sina.com.cn/s/blog_58ae76e80100hs00.html";
		Matcher m=pattern.matcher(url);
		while (m.find()) {
            System.out.println(m.group(1));            
        } 
	}

}