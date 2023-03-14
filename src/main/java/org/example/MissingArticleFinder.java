package org.example;

import java.util.*;

public class MissingArticleFinder {
    private final List<List<String>> pairedPosTokenList;

    // hashset of proper nouns subset which require definite article "the", such as entities that represent geographic locations, rivers, buildings, organizations, art, scriptures, events and countries
    private final Set<String> properNounsWithArticles = new HashSet<>(Arrays.asList("Mona Lisa", "Eiffel Tower", "Grand Canyon", "Himalayas", "Rhine", "Amazon", "Niles", "Spree", "Empire State Building", "Berlin Wall", "Louvre", "French Revolution", "Bible", "Quran", "Big Bang", "Netherlands", "Bahamas", "Maldives", "Philippines", "Mississippi", "Thames", "United States", "United Kingdom", "Czech Republic"));

    // hashset of uncountable nouns subset, including languages, certain food, some materials, abstract nouns, areas of study
    private final Set<String> uncountableNouns = new HashSet<>(Arrays.asList("aggression", "attention", "alcohol", "anger", "aid", "arithmetic", "beauty", "beef", "bravery", "blood", "ballet", "butter", "biology", "chaos", "cash", "courage", "comprehension", "coal", "commerce", "confusion", "countryside", "chocolate", "coffee", "childhood", "data", "democracy", "damage", "design", "engineering", "entertainment", "evolution", "ethics", "enthusiasm", "fuel", "finance", "fame", "fun", "faith", "food", "forgiveness", "flesh", "fashion", "genetics", "gold", "guilt", "ground", "growth", "garbage", "garlic", "grief", "harm", "hardware", "health", "heat", "hatred", "hunger", "honor", "humour", "height", "help", "information", "imagination", "independence", "inflation", "judo", "jealousy", "justice", "joy", "jam", "kindness", "knowledge", "karate", "laughter", "labour", "land", "linguistics", "leisure", "literature", "magic", "money", "music", "mercy", "maths", "motivation", "milk", "mankind", "nature", "nitrogen", "nurture", "nothing", "obedience", "oxygen", "paper", "poetry", "physics", "psychology", "pepper", "peace", "plastic", "progress", "pork", "quartz", "revenge", "rice", "rain", "respect", "research", "religion", "salt", "soil", "speed", "strength", "strength", "stress", "sleep", "snow", "silver", "spaghetti", "sport", "sugar", "tea", "tennis", "traffic", "transportation", "usage", "unity", "violence", "vengeance", "weight", "whiskey", "wisdom", "wealth", "width", "water", "yoga", "zinc"));

    // constructor of MissingArticleFinder takes a tuple of (POS tag list, token word list)
    public MissingArticleFinder(List<List<String>> pairedPosTokenList){
        this.pairedPosTokenList = pairedPosTokenList;
    }

    // findMissingArticles probes for POS tag sequences where articles are missing and returns a list of token words, including inserted missing articles
    public List<String> findMissingArticles(){

        List<String> posList = new ArrayList<>(pairedPosTokenList.get(0));
        List<String> tokenList = new ArrayList<>(pairedPosTokenList.get(1));
        List<String> sourceSentence = new ArrayList<>(pairedPosTokenList.get(1));

        int index = 0;

        for (int i = 0; i < posList.size(); i++) {
            String currentPos = posList.get(i);
            String currentTok = tokenList.get(index);

            // test wrong usage of indefinite articles
            if (currentTok.matches("(?i)(a)") && tokenList.get(index + 1).matches("(?i)^[aeiou].*")){
                tokenList.set(index, "an");
            }
            if (currentTok.matches("(?i)(an)") && tokenList.get(index + 1).matches("(?i)[^aeiou].*")){
                tokenList.set(index, "a");
            }
            if (currentTok.matches("(?i)(an|a)") && posList.get(i + 1).equals("NNS")) {
                tokenList.set(index, "the");
            }

            // handle definite article use of proper nouns
            // DT -> NNP (requires definite article): The Louvre
            if (currentPos.equals("NNP") && properNounsWithArticles.contains(currentTok)) {

                if (beginningOfSentence(i)) {
                    tokenList.add(index, "The");
                    index++;
                }
                else if (!posList.get(i - 1).equals("DT")) {
                    tokenList.add(index, "the");
                    index++;
                }
            }

            // DT -> NNP -> NNP (multiword NNPs that require definite article): The French Revolution
            if (currentPos.equals("NNP") && posList.get(i + 1).equals("NNP")) {
                String properNoun = currentTok + " " + tokenList.get(index + 1);
                if (properNounsWithArticles.contains(properNoun)) {
                    if (beginningOfSentence(i)) {
                        tokenList.add(index, "The");
                        index++;
                    }
                    else if (!posList.get(i - 1).equals("DT")) {
                        tokenList.add(index, "the");
                        index++;

                    }
                }
            }

            // DT -> NNP -> NNP -> NNP (multiword NNPs that require definite article): The Empire State Building
            if (currentPos.equals("NNP") && posList.get(i + 1).equals("NNP") && indexInBounds(i, 2, posList) && posList.get(i + 2).equals("NNP")) {
                String properNoun = currentTok + " " + tokenList.get(index + 1) + " " + tokenList.get(index + 2);
                if (properNounsWithArticles.contains(properNoun)) {
                    if (beginningOfSentence(i)) {
                        tokenList.add(index, "The");
                        index++;
                    }
                    else if (!posList.get(i - 1).equals("DT")) {
                        tokenList.add(index, "the");
                        index++;
                    }
                }
            }

            // NN at the beginning of a sentence
            if (currentPos.equals("NN") && (beginningOfSentence(i))) {
                if (uncountableNouns.contains(currentTok.toLowerCase())) {
                    index++;
                    continue;
                }
                //tokenList.set(index, currentTokLower);
                tokenList.add(index, "the/a/an");
                index++;
            }

            // NN between beginning and end of sentence
            else if (currentPos.equals("NN")) {
                // check if corresponding CD/DT/PRP$/WP$/WDT exists
                if(posList.get(i - 1).matches("(CD|WDT|WP\\$|DT|PRP\\$)")){
                    index++;
                    continue;
                }

                // NP -> DT -> ADJP -> NN
                else if (indexInBounds(index, -2, posList) && (posList.get(i-2).matches("(RB|RBR)") && posList.get(i-1).matches("JJ"))){
                    if (beginningOfSentence(i - 2)){
                        tokenList.add(index - 2, "the/a/an");
                        index++;
                    }

                    // check if corresponding CD|DT|PRP$ exists
                    else if (posList.get(i - 3).matches("(CD|DT|PRP\\$)")) {
                        index++;
                        continue;
                    }

                    // neither beginning of sentence nor prior DT|PRP$
                    else {
                        tokenList.add(index - 2, "the/a/an");
                        index++;
                    }

                }

                // DT -> JJS|RBS -> JJ|VBG
                else if (indexInBounds(index, -2, posList) && (posList.get(i-2).matches("(JJS|RBS)") && posList.get(i-1).matches("(JJ|VBG)"))){
                    if (beginningOfSentence(i - 2)){
                        tokenList.add(index - 2, "the");
                        index++;
                    }

                    // check if corresponding DT/PRP$ exists
                    else if (posList.get(i - 3).matches("(CD|DT|PRP\\$)")) {
                        index++;
                        continue;
                    }

                    // neither beginning of sentence nor prior DT/PRP$
                    else {
                        tokenList.add(index - 2, "the");
                        index++;
                    }

                }

                // DT -> JJS|JJR -> NN
                else if(posList.get(i-1).matches("(JJS|JJR)")) {

                    // check if corresponding DT|PRP$ exists
                    if (indexInBounds(i, -2, posList) && posList.get(i - 2).matches("(DT|PRP\\$)")) {
                        index++;
                        continue;
                    }

                    else {
                        tokenList.add(index - 1, "the");
                        index++;
                    }

                }

                // DT -> JJ|JJS -> NN; The smartest boy in the world.
                else if(posList.get(i - 1).equals("JJ")) {
                    // check if corresponding DT/PRP$ exists
                    if (indexInBounds(i, -2, posList) && posList.get(i - 2).matches("(DT|PRP\\$)")) {
                        index++;
                        continue;

                    } else {
                        tokenList.add(index - 1, "the/a/an");
                        index++;
                    }

                }

                // DT -> NN -> NN: The basketball court.
                else if(posList.get(i - 1).equals("NN")) {
                    if (beginningOfSentence(i - 1)) {
                        tokenList.add(index - 1, "the/a/an");
                        index++;
                    }
                    // check if corresponding DT/PRP$ exists
                    else if (posList.get(i - 2).matches("(DT|PRP\\$)")) {
                        index++;
                        continue;
                    }

                }

                // DT -> uncountable NN
                else if(!uncountableNouns.contains(currentTok)){
                    tokenList.add(index, "the/a/an");
                    index++;
                }

            }

            // DT -> JJS -> NNS: She is one of the best players.
            else if (currentPos.equals("NNS") && indexInBounds(i, -2, posList)) {
                if (posList.get(i - 1).equals("JJS") && !posList.get(i - 2).matches("(DT|PRP\\$)")){
                    tokenList.add(index - 1, "the");
                    index++;
                }

                // DT -> JJ|RB -> JJS -> NNS: They serve the best looking dishes.
                else if (indexInBounds(i, -3, posList) && posList.get(i - 1).matches("(JJ|VBG)") && posList.get(i - 2).equals("JJS") && !posList.get(i - 3).matches("(DT|PRP\\$)")){
                    tokenList.add(index - 2, "the");
                    index++;
                }

            }

            // test excessive usage of determiner
            if (currentPos.equals("DT") && posList.get(i + 1).equals("DT")) {
                if (currentTok.equals(tokenList.get(index + 1))){
                    tokenList.remove(index);
                    index--;
                }

            }

            // increment index to access token list
            index++;

        }

        // return empty list if tokenList is not modified
        if (tokenList.equals(sourceSentence)){
            return new ArrayList<>();
        }

        // return token list including missing articles
        else {
            return tokenList;
        }

    }

    // test if index + index lookahead is in token list boundary
    public boolean indexInBounds(int currentIndex, int lookaheadIndex, List<String> tokenlist){
        int number = currentIndex + lookaheadIndex;
        return (tokenlist.size() > number && number >= 0);
    }

    // test if the respective token is at the beginning of a sentence
    public boolean beginningOfSentence(int index){
        return (index == 0);
    }

}
