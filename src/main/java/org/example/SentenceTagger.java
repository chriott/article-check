package org.example;

import edu.stanford.nlp.ling.CoreAnnotations.*;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;

import java.util.*;

public class SentenceTagger {
    private final StanfordCoreNLP pipeline;

    public SentenceTagger() {
        // set up parameters for CoreNLP pipeline
        Properties props = new Properties();
        props.put("annotators", "tokenize, pos, lemma");

        // create pipeline
        pipeline = new StanfordCoreNLP(props);

    }

    // runs annotation pipeline and returns a tuple that consists of (token list, POS list) of a given text input
    public List<List<String>> runTagging(String input){

        // create an annotation document with input sentence
        Annotation document = new Annotation(input);

        // initialize pipeline on document; the corresponding output can be accessed using the data structures CoreMap and CoreLabel.
        pipeline.annotate(document);

        // CoreMap uses class objects as keys and has values with custom types
        List<CoreMap> sentences = document.get(SentencesAnnotation.class);

        // List of POS tag from sentence
        List<String> posList = new ArrayList<>();

        // List of tokens from sentence
        List<String> tokenList = new ArrayList<>();

        // Pair of token list and corresponding pos list
        List<List<String>> pairedPosTokenList = new ArrayList<>();

        for (CoreMap sentence : sentences) {
            // a CoreLabel is a CoreMap with additional token-specific methods
            for (CoreLabel token : sentence.get(TokensAnnotation.class)) {
                String word = token.get(TextAnnotation.class);
                String pos = token.get(PartOfSpeechAnnotation.class);
                tokenList.add(word);
                posList.add(pos);
            }
        }

        pairedPosTokenList.add(posList);
        pairedPosTokenList.add(tokenList);

        // return tuple of (token list, POS list)
        return pairedPosTokenList;

    }
}
