package us.codecraft.webmagic.newscrawler;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.FilePipeline;
import us.codecraft.webmagic.pipeline.Pipeline;

public class AsiaNewsPipeline implements Pipeline {

	private static int counter = 0;

	@Override
	public void process(ResultItems resultItems, Task task) {
		// TODO Auto-generated method stub
		final String url = resultItems.getRequest().getUrl();
		System.out.println("****************--Entry Pipeline Process--*****************");
		System.out.println("Get page: " + url);

		/*
		 * if(url.matches(
		 * "http://blog\\.sina\\.com\\.cn/s/articlelist_.*\\.html")){//文章列表
		 * System.out.println("No Op in Article List"); // }else
		 * if(url.matches("http://www\\.asianews\\.it/news-en/.*")){
		 */
		// 具体文章内容
		String time = null, title = null, content = null, abstracts = null, convertUrl = null;

		for (Map.Entry<String, Object> entry : resultItems.getAll().entrySet()) {
			// System.out.println(entry.getKey()+":\n"+entry.getValue());

			if (AsianViewDetailItem.TIME.equals(entry.getKey())) {
				time = (String) entry.getValue();
			} else if (AsianViewDetailItem.TITLE.equals(entry.getKey())) {
				title = (String) entry.getValue();
			} else if (AsianViewDetailItem.ABSTRACT.equals(entry.getKey())) {
				abstracts = (String) entry.getValue();
			} else if (AsianViewDetailItem.CONTENT.equals(entry.getKey())) {
				content = (String) entry.getValue();
			} else if (AsianViewDetailItem.URL.equals(entry.getKey())) {
				assert url.equals(entry.getValue());
			} else if (AsianViewDetailItem.CONVERT_URL.equals(entry.getKey())) {
				convertUrl = (String) entry.getValue();
			}

		}
		// System.out.println("Time:\n"+CorpusFormatter.timeFormat(time));
		//
		// System.out.println("Title:\n"+title);
		//
		// System.out.println("Abstract:\n"+abstracts);
		//
		// System.out.println("Content:\n"+content);
		//
		// System.out.println("Url:\n"+url);

		String id = String.format("%s-%s", time, ++counter);

		final String fileName = String.format(".//" + FileUtils.docName + "//%s.txt", id);
		System.out.println("File name :" + fileName);
		// FileUtils.writeContent(fileName, time, title, content);
		FileUtils.writeCorpus(fileName,
				convertUrl != null
						? CorpusFormatter.format(id, time, url, "T", "P", title, abstracts,
								content, "", "", convertUrl)
						: CorpusFormatter.format(id, time, url, "T", "P", title, abstracts,
								content, "", ""));

		// }else{
		// System.out.println("Can't match Url");
		// }
		System.out.println("****************--Exit Pipeline Process--*****************");

	}
}