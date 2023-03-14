package org.example;

import java.util.List;

public class MissingArticleSuggestion {

    private final List<String> insertedArticles;

    // constructor takes list of tokens including missing articles as argument
    public MissingArticleSuggestion(List<String> insertedArticles) {
        this.insertedArticles = insertedArticles;
    }

    // replace unwanted extra space between contraction/quotation/exclamation/question mark and full stop
    public String removeWhitespacePunctuation(String input){
        return input.replaceAll("\"\\s([^\"]*?)\\s\"", "\"$1\"").replaceAll("'\\s([^']*?)\\s'", "'$1'").replaceAll("(?i)(\\w)\\s+(n't|N'T)", "$1$2").replaceAll("(?i)(\\w)\\s+(['’])(d|m|s|ve|ll|re)\\b", "$1$2$3").replaceAll("(\\w|\"|')\\s+([.,’!?])", "$1$2");
    }

    public String createStringSuggestion() {
        String suggestion = "";

        if (!insertedArticles.isEmpty()) {
            String sntDef = "";
            String sntInd = "";

            // make first letter of second word upper case and excluding all upper case sentences
            boolean wordIsUpperCase = insertedArticles.get(1).matches(insertedArticles.get(1).toUpperCase());
            if (!wordIsUpperCase && insertedArticles.get(0).matches("^(the/a/an|the)$")){
                insertedArticles.set(1, insertedArticles.get(1).substring(0, 1).toLowerCase() + insertedArticles.get(1).substring(1));
            }

            // create consecutive string from list of string elements
            suggestion = String.join(" ", insertedArticles);

            // replace all the/a/an-token before a vowel with "an" and "a" before consonant
            sntInd = suggestion.replaceAll("(?i)\\b(the/a/an)\\b(?=\\s+[aeiou])", "an").replaceAll("(?i)\\b(the/a/an)\\b(?=\\s+[^aeiou])", "a");
            sntInd = sntInd.substring(0,1).toUpperCase() + sntInd.substring(1);

            // replace all the/a/an-token with "the"
            sntDef = suggestion.replaceAll("the/a/an", "the");
            sntDef = sntDef.substring(0,1).toUpperCase() + sntDef.substring(1);

            // if sentence with indefinite article and definite article are equal continue with only one (for example, exceptional "the" inserted before NNP)
            boolean indDefAreEqual = sntDef.equals(sntInd);
            if (!sntInd.isEmpty() && !indDefAreEqual){
                suggestion = sntDef + ";" + " " + sntInd;
            }
            else {
                suggestion = sntDef;
            }

            // handle check if all letters are uppercase
            String sentenceWithoutArticle = suggestion.replaceAll("(?i)\\b(the|a|an)\\b", "").replaceAll("\\s+","");
            boolean isAllUpperCase = sentenceWithoutArticle.matches("[A-Z.!?,;\"']+");

            if (isAllUpperCase){
                suggestion = suggestion.toUpperCase();
            }

            // return output string including article suggestions without extra whitespace
            return removeWhitespacePunctuation(suggestion);
        }

        // return empty string if no suggestion was found
        else {
            return "";
        }
    }
}
