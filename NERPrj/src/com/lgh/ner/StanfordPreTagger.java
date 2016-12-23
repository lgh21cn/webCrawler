package com.lgh.ner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import edu.stanford.nlp.ie.AbstractSequenceClassifier;
import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.io.IOUtils;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;

public class StanfordPreTagger {

	public static final String ROOT_UNTAGGED_CORPUS_PATH = "C:/eclipse/workspace/NERPrj/untagged";
	public static final String ROOT_PRETAGGED_CORPUS_PATH = "C:/eclipse/workspace/NERPrj/pre_tagged";

	private void preTagProcessing() {
		List<String> paths = new ArrayList<String>();

		try {
			FileUtils.readAllFilePaths(ROOT_UNTAGGED_CORPUS_PATH, paths);

			for (String path : paths) {
				createPreTaggedCorpus(path.replace("\\", FileUtils.seperator));
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static final String STANFORD_NER_ROOT_PATH = "C:/stanford-ner-2015-12-09/";

	private void createPreTaggedCorpus(String path) {
		String serializedClassifier = STANFORD_NER_ROOT_PATH + "classifiers/english.all.3class.distsim.crf.ser.gz";
		AbstractSequenceClassifier<CoreLabel> classifier;
		try {
			classifier = CRFClassifier.getClassifier(serializedClassifier);

			String fileContents = IOUtils.slurpFile(path);
			StringBuilder builder = new StringBuilder();
			List<List<CoreLabel>> out = classifier.classify(fileContents);
			for (List<CoreLabel> sentence : out) {
				for (CoreLabel word : sentence) {
					builder.append(word.word() + '/' + word.get(CoreAnnotations.AnswerAnnotation.class) + ' ');
					System.out.print(word.word() + '/' + word.get(CoreAnnotations.AnswerAnnotation.class) + ' ');
				}
				builder.append("\n");
				System.out.println();
			}
			
			String fileName=CorpusResolver.getFileName(path);
			String belong=CorpusResolver.getBelongName(path);
			
			String tagged_corpus_path=ROOT_PRETAGGED_CORPUS_PATH+FileUtils.seperator+belong+FileUtils.seperator+fileName;
			

			FileUtils.writeErrorLog(tagged_corpus_path, builder.toString());

			// System.out.println("---");
			// builder.setLength(0);
			// out =
			// classifier.classifyFile(STANFORD_NER_ROOT_PATH+"dwNews.txt");
			// for (List<CoreLabel> sentence : out) {
			// for (CoreLabel word : sentence) {
			// builder.append(word.word() + '/' +
			// word.get(CoreAnnotations.AnswerAnnotation.class) + ' ');
			// System.out.print(word.word() + '/' +
			// word.get(CoreAnnotations.AnswerAnnotation.class) + ' ');
			// }
			// builder.append("\n");
			// System.out.println();
			// }
			//
			// FileUtils.writeErrorLog(STANFORD_NER_ROOT_PATH+"dwNews_NER2.txt",
			// builder.toString());

		} catch (ClassCastException | ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		StanfordPreTagger tagger=new StanfordPreTagger();
//		tagger.createPreTaggedCorpus(ROOT_UNTAGGED_CORPUS_PATH+FileUtils.seperator+"asianews/"+"20050309-102.txt");
		tagger.preTagProcessing();
//		String tmp1="http://www.dw.com/search/en?languageCode=en&origin=gN&item=Xinjiang&searchNavigationId=9097"
//+"\n"+"http://www.dw.com/search/en?languageCode=en&origin=gN&item=Uyghur&searchNavigationId=9097"
//+"\n"+"http://www.dw.com/search/en?languageCode=en&origin=gN&item=Uighur&searchNavigationId=9097"
//+"\n"+"http://www.dw.com/search/en?languageCode=en&origin=gN&item=WUC&searchNavigationId=9097"
//+"\n"+"http://www.dw.com/search/en?languageCode=en&origin=gN&item=World+Uighur+Congress&searchNavigationId=9097"
//+"\n"+"http://www.dw.com/search/en?languageCode=en&origin=gN&item=east+turkestan&searchNavigationId=9097"
//+"\n"+"http://www.dw.com/search/en?languageCode=en&origin=gN&item=Urumqi&searchNavigationId=9097"
//+"\n"+"http://www.dw.com/search/en?languageCode=en&origin=gN&item=Rebiya+Kadeer&searchNavigationId=9097"
//+"\n"+"http://www.dw.com/search/en?languageCode=en&origin=gN&item=Dilxat+Raxit&searchNavigationId=9097";
//		String tmp2=tmp1.replace("+", "_");
//		System.out.println("Origin:\n"+tmp1);
//		System.out.println("New:\n"+tmp2);

	}

}
