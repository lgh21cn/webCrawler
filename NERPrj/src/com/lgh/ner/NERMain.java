package com.lgh.ner;

import java.io.IOException;
import java.util.List;

import edu.stanford.nlp.ie.AbstractSequenceClassifier;
import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.io.IOUtils;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;

public class NERMain {
	
	public static final String STANFORD_NER_ROOT_PATH="C:/stanford-ner-2015-12-09/";
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String serializedClassifier = STANFORD_NER_ROOT_PATH+"classifiers/english.all.3class.distsim.crf.ser.gz";
		
		AbstractSequenceClassifier<CoreLabel> classifier;
		try {
			classifier = CRFClassifier.getClassifier(serializedClassifier);
			
			String fileContents = IOUtils.slurpFile(STANFORD_NER_ROOT_PATH+"dwNews.txt");
			StringBuilder builder=new StringBuilder();
		      List<List<CoreLabel>> out = classifier.classify(fileContents);
		      for (List<CoreLabel> sentence : out) {
		        for (CoreLabel word : sentence) {
		        	builder.append(word.word() + '/' + word.get(CoreAnnotations.AnswerAnnotation.class) + ' ');
		          System.out.print(word.word() + '/' + word.get(CoreAnnotations.AnswerAnnotation.class) + ' ');
		        }
		        builder.append("\n");
		        System.out.println();
		      }
		      
		      FileUtils.writeErrorLog(STANFORD_NER_ROOT_PATH+"dwNews_NER1.txt", builder.toString());

		      System.out.println("---");
		      builder.setLength(0);
		      out = classifier.classifyFile(STANFORD_NER_ROOT_PATH+"dwNews.txt");
		      for (List<CoreLabel> sentence : out) {
		        for (CoreLabel word : sentence) {
		        	builder.append(word.word() + '/' + word.get(CoreAnnotations.AnswerAnnotation.class) + ' ');
		          System.out.print(word.word() + '/' + word.get(CoreAnnotations.AnswerAnnotation.class) + ' ');
		        }
		        builder.append("\n");
		        System.out.println();
		      }
		      
		      FileUtils.writeErrorLog(STANFORD_NER_ROOT_PATH+"dwNews_NER2.txt", builder.toString());
		      
		} catch (ClassCastException | ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
	}

}
