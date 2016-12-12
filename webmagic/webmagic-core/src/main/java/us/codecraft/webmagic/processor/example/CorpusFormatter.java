package us.codecraft.webmagic.processor.example;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;

public class CorpusFormatter {
	public static final String CRLF="\r\n";
	public static final String ID="<ID>%s"+CRLF;
	public static final String DATE="<DATE>%s<\\DATE>"+CRLF;
	public static final String URL="<URL>"+CRLF+"%s"+CRLF+"<\\URL>"+CRLF;
	public static final String CATEGORY="<CATEGORY>%s<\\CATEGORY>"+CRLF;
	public static final String SENTIMENT="<SENTIMENT>%s<\\SENTIMENT>"+CRLF;
	public static final String TITLE="<TITLE>"+CRLF+"%s"+CRLF+"<\\TITLE>"+CRLF;
	public static final String ABSTRACT="<ABSTRACT>"+CRLF+"%s"+CRLF+"<\\ABSTRACT>"+CRLF;
	public static final String CONTENT="<CONTENT>"+CRLF+"%s"+CRLF+"<\\CONTENT>"+CRLF;
	public static final String POS="<POS>"+CRLF+"%s"+CRLF+"<\\POS>"+CRLF;
	public static final String NER="<NER>"+CRLF+"%s"+CRLF+"<\\NER>";
	
	public static String format(String id,String date,String url
			,String category,String sentiment
			,String title,String abstracts,String content,String pos,String ner){
		StringBuilder builder=new StringBuilder();
		
		builder.append(String.format(CorpusFormatter.ID, id));
		builder.append(String.format(CorpusFormatter.DATE, date));
		builder.append(String.format(CorpusFormatter.URL, url));
		builder.append(String.format(CorpusFormatter.CATEGORY, category));
		builder.append(String.format(CorpusFormatter.SENTIMENT, sentiment));
		builder.append(String.format(CorpusFormatter.TITLE, title));
		builder.append(String.format(CorpusFormatter.ABSTRACT, abstracts));
		builder.append(String.format(CorpusFormatter.CONTENT, content));
		builder.append(String.format(CorpusFormatter.POS, pos));
		builder.append(String.format(CorpusFormatter.NER, ner));

		
		return builder.toString();
	}
	
	public static final String ASIANEWS_DATE_FORMAT="MM/dd/yyyy, HH.mm";
//	public static final String CORPUS_DATE_FORMAT="yyyyMMdd";
	
	public static final String CORPUS_DATE_FORMAT="%s%s%s";
	public static String timeFormat(String time){
//		DateFormat originFormat=new SimpleDateFormat(ASIANEWS_DATE_FORMAT);
//		String corpus_time= originFormat.format(time);
//		return corpus_time;
		
		String date_time=time.trim().split(", ")[0];
		String[] date=date_time.split("/");
		return String.format(CORPUS_DATE_FORMAT, date[2],date[0],date[1]);
	}
	public static final String reg = "\\<[^\\>]*\\>";
	public static String removeTag(String content){
		return content.replaceAll(reg, "");
	}
	
	public static void main(String[] args){
//		String time=timeFormat("09/05/2009, 00.00");
//		System.out.println(time);
		
//		DateFormatUtils
		String time="09/05/2009";
		System.out.println(timeFormat(time));
		
		String reg = "\\<[^\\>]*\\>";
		String str = "zhong gongzhongyang <http://www.asianews.it/./files/img/CHINA_kASHGAR_31JULI.jpg>  "
+"\n<https://pinterest.com/pin/create/bookmarklet/?media=http://www.asianews.it/files/img/CHINA_kASHGAR_31JULI.jpg&url=http://www.asianews.it/news-en/More-deaths-in-Xinjiang,-Beijing-announces-tough-measures-during-Ramadan--22250.html&is_video=&description=More+deaths+in+Xinjiang%2C+Beijing+announces+tough+measures+during+Ramadan+>"
+" Beijing (AsiaNews / Agencies) - Beijing blames Islamic militants for attacks "
+"on passers-by yesterday in Kashgar (Xinjiang) which have caused at least 14 "
+"dead and 40 wounded, and announced a crackdown against \"illegal religious "
+"activities\", just as today Muslims began the holy month of Ramadan. Experts ";
		str = str.replaceAll(reg, "");
		System.out.println(str);
		
	}
	
}
