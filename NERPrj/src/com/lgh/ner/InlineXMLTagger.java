package com.lgh.ner;

import com.lgh.ner.InlineXMLTagger.Tagger.NoSplitException;

public class InlineXMLTagger {
	
	public static final String INLINEXML_TAGGER_ERROR_LOG="C:/eclipse/workspace/NERPrj/inline_xml_tagger_error.log";
	public static final String FAKE_TEST_SCR_PATH="C:/eclipse/workspace/NERPrj/fake_test.txt";
	public static final String FAKE_TEST_DEST_PATH="C:/eclipse/workspace/NERPrj/fake_test_dest.txt";
	
	Tagger[] taggers=new Tagger[SlashTagsToInlineXMLConverter.TAGS.length];
	
	public InlineXMLTagger() {
		super();
		
		//添加除【OTHER】以外的类别
		for(int index=0;index<taggers.length-1;index++){
			taggers[index]=new Tagger(SlashTagsToInlineXMLConverter.TAGS[index]);
		}
		//添加【OTHER】类别
		taggers[taggers.length-1]=new Tagger.OtherTagger(SlashTagsToInlineXMLConverter.TAGS[taggers.length-1]);
		
	}
	
	public String resolve(String path,String content){
		String newContent=content;
		
		try {
			for (Tagger tagger : taggers) {				
				newContent=tagger.resolve(path, newContent);				
			}
		} catch (NoSplitException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			StringBuilder errorBuilder=new StringBuilder();
			errorBuilder.append("Error Path:\n");
			errorBuilder.append(path);
			errorBuilder.append("\nContent:\n");
			errorBuilder.append(content);
			errorBuilder.append("\n");
			FileUtils.writeErrorLog(INLINEXML_TAGGER_ERROR_LOG, errorBuilder.toString(), true);
		}
		
		return newContent;
	}
	
	static class Tagger{
		protected String tag;
		protected String startTag;
		protected String endTag;
		
		static final String B_TAG="B";
		static final String I_TAG="I";
		static final String E_TAG="E";
		
		static final String SPACE=" ";
		
		//eg. B_ORG
		static final String MULTI_TAG="%s/%s_%s"+SPACE;
		static final String SINGLE_TAG="%s/%s"+SPACE;
		static final String XML_START_TAG="<%s>";
		static final String XML_END_TAG="</%s>";
		public Tagger(String tag) {
			super();
			this.tag = tag;
			this.startTag=String.format(XML_START_TAG, tag);
			this.endTag=String.format(XML_END_TAG, tag);
		}
		
		
		static class NoSplitException extends Exception{

			public NoSplitException() {
				super();
				// TODO Auto-generated constructor stub
			}

			public NoSplitException(String message) {
				super(message);
				// TODO Auto-generated constructor stub
			}
			
		}
		
		static class OtherTagger extends Tagger{

			private static final String OTHER_TAG="O";
			
			public OtherTagger(String tag) {
				super(tag);
				// TODO Auto-generated constructor stub
			}
			
			@Override
			protected String makeNewTags(String[] noTags) {
				// TODO Auto-generated method stub
				if(noTags.length==0) return "";
				else {
					StringBuilder builder=new StringBuilder();
					for(int i=0;i<noTags.length;i++){
						builder.append(String.format(SINGLE_TAG, noTags[i],OTHER_TAG));
					}
					
					return builder.toString();
				}
			}
			
		}

		private static String[] clearTags(String[] subs) throws NoSplitException{
			String[] noTags=new String[subs.length];
			for(int i=0;i<subs.length;i++){
				int lastSplitIndex=subs[i].lastIndexOf("/");
				if(lastSplitIndex!=-1)
					noTags[i]=subs[i].substring(0, lastSplitIndex);
				else{
					throw new NoSplitException("No Split");
				}
			}
			return noTags;
		}
		
		protected String makeNewTags(String[] noTags){
			if(noTags.length==0) return "";
			else if(noTags.length==1) return String.format(SINGLE_TAG, noTags[0],tag);
			else {
				StringBuilder builder=new StringBuilder();
				for(int i=0;i<noTags.length;i++){
					if(i==0){
						builder.append(String.format(MULTI_TAG, noTags[i], B_TAG,tag));
					}else if(i==noTags.length-1){
						builder.append(String.format(MULTI_TAG, noTags[i], E_TAG,tag));
					}else{
						builder.append(String.format(MULTI_TAG, noTags[i], I_TAG,tag));
					}
				}
				
				return builder.toString();
			}
		}
		
		public  String resolve(String path,String content) throws NoSplitException{
			if(content ==null || content.length()==0) return content;
			int startIndex=content.indexOf(startTag);
			if(startIndex==-1) return content;
			
			int endIndex=content.indexOf(endTag, startIndex);
			int preEndIndex=-endTag.length();
			StringBuilder builder=new StringBuilder();
			
			final int start_tag_length=startTag.length();
			final int end_tag_length=endTag.length();
//			try {
				while(startIndex!=-1 && endIndex!=-1 &&startIndex+start_tag_length<endIndex){
					
					System.out.println("preEnd Index:");
					System.out.println(preEndIndex);
					System.out.println("Start Index:");
					System.out.println(startIndex);
					System.out.println("End Index:");
					System.out.println(endIndex);
					
					
					if(preEndIndex+end_tag_length <startIndex)
						builder.append(content.substring(preEndIndex+end_tag_length, startIndex));

					
					String subContent=content.substring(startIndex+start_tag_length, endIndex);
					String[] subs=subContent.split(" ");
					
					String[] noTags;
					
					noTags = clearTags(subs);
					builder.append(makeNewTags(noTags));
					
					
					
					preEndIndex=endIndex;
					startIndex=content.indexOf(startTag,endIndex+end_tag_length);
					endIndex=content.indexOf(endTag, startIndex);
				}
				builder.append(content.substring(preEndIndex+end_tag_length));
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//				StringBuilder errorBuilder=new StringBuilder();
//				errorBuilder.append("Error Path:\n");
//				errorBuilder.append(path);
//				errorBuilder.append("\nContent:\n");
//				errorBuilder.append(content);
//				errorBuilder.append("\n");
//				FileUtils.writeErrorLog(INLINEXML_TAGGER_ERROR_LOG, errorBuilder.toString(), true);
//			}
			String newContent=builder.toString();
			newContent=newContent.replaceAll(SlashTagsToInlineXMLConverter.MORE_SPACE, SlashTagsToInlineXMLConverter.SPACE);
			return newContent;
		}
	}




	public static final String CANDIDATES_DEST_PATH="C:/myResearch/URL_parse/gdelt/self_tagged_converted/searchResults.log";
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		InlineXMLTagger.Tagger tagger=new InlineXMLTagger.Tagger.OtherTagger("OTHER"
				/*"U_ORGANIZATION"*/
				/*"ORGANIZATION"*/);
		String tmp="Send/O <ORGANIZATION>Facebook/B_ORGANIZATION</ORGANIZATION> <ORGANIZATION>Twitter/I_ORGANIZATION</ORGANIZATION> <ORGANIZATION>Google/E_ORGANIZATION</ORGANIZATION> +/O <ORGANIZATION>Whatsapp/B_ORGANIZATION</ORGANIZATION> <ORGANIZATION>Tumblr/E_ORGANIZATION</ORGANIZATION> linkedin/O stumble/O Digg/ORGANIZATION reddit/O Newsvine/ORGANIZATION Permalink/O http://dw.com/p/1BIEI/O ";
		String tmp2="How/O to/O <U_ORGANIZATION>stay/O successful/O on/O</U_ORGANIZATION> Facebook/ORGANIZATION ?/O ";
		String tmp3="However/O ,/O the/O new/O <LOCATION>censorship/O tool/O would/O</LOCATION> allow/O censors/O to/O <ORGANIZATION>manipulate/O public/O</ORGANIZATION> opinion/O directly/O by/O essentially/O <U_PERSON>preventing/O users/O</U_PERSON> from/O posting/O articles/O that/O offended/O the/O <U_LOCATION>state/O</U_LOCATION> ./O";
		String multiOther="Earlier/O this/O month/O ,/O <OTHER>Facebook/ORGANIZATION CEO/O Mark/B_PERSON Zuckerberg/E_PERSON</OTHER> said/O the/O company/O was/O building/O new/O tools/O to/O address/O fake/O news/O ./O ";
		String singleOther="According/O to/O the/O <OTHER>Times/ORGANIZATION</OTHER> report/O ,/O the/O software/O ,/O therefore/O ,/O caused/O a/O controversy/O within/O Facebook/ORGANIZATION ,/O with/O ``/O several/O employees/O ''/O quitting/O in/O protest/O after/O working/O on/O it/O ./O ";
		try {
			System.out.println(tagger.resolve("Fake File Path",singleOther));
		} catch (NoSplitException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String content=FileUtils.readFile(FAKE_TEST_SCR_PATH,false);
		InlineXMLTagger inlineTagger=new InlineXMLTagger();
		
		
		
		
		FileUtils.writeErrorLog(FAKE_TEST_DEST_PATH, inlineTagger.resolve(FAKE_TEST_SCR_PATH, content));
		
		String candidate_paths=FileUtils.readFile(CANDIDATES_DEST_PATH, false);
		String[] cand_paths=candidate_paths.split("\n");
		
		for (String path : cand_paths) {
			content=FileUtils.readFile(path,false);
			String newContent=inlineTagger.resolve(path, content);
			
			path=path.replace("bios", "candidate").replace("\\", "/");
			
			FileUtils.writeErrorLog(path,newContent );
			
		}
	}

}
