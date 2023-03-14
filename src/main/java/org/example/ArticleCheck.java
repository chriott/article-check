package org.example;

import java.util.*;

public class ArticleCheck {

    public static void main(String[] args) {

        while (true) {
            System.out.println("Please enter a sentence to detect possible missing/wrong articles. Type exit to quit the program.");
            Scanner scanner = new Scanner(System.in);
            String s = scanner.nextLine();

            if (s.equalsIgnoreCase("exit")) {
                System.out.println("Exiting program.");
                break;
            }

            SentenceTagger tagger = new SentenceTagger();
            MissingArticleFinder finder = new MissingArticleFinder(tagger.runTagging(s));
            List<String> insertionArticleSuggestion = finder.findMissingArticles();
            MissingArticleSuggestion suggestion = new MissingArticleSuggestion(insertionArticleSuggestion);
            System.out.println(suggestion.createStringSuggestion());

        }

    }

}