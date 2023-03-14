package org.example;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ArticleCheckTest {

    SentenceTagger tagger = new SentenceTagger();

    @Test
    public void testSuperlative1() {
        MissingArticleFinder finder = new MissingArticleFinder(tagger.runTagging("He is fastest runner in his division."));
        List<String> insertionArticleSuggestion = finder.findMissingArticles();
        MissingArticleSuggestion suggestion = new MissingArticleSuggestion(insertionArticleSuggestion);
        String outcome = suggestion.createStringSuggestion();
        assertEquals("He is the fastest runner in his division.", outcome);
    }

    @Test
    public void testSuperlativeWithAdverb1() {
        MissingArticleFinder finder = new MissingArticleFinder(tagger.runTagging("Most beautiful city is Paris."));
        List<String> insertionArticleSuggestion = finder.findMissingArticles();
        MissingArticleSuggestion suggestion = new MissingArticleSuggestion(insertionArticleSuggestion);
        String outcome = suggestion.createStringSuggestion();
        assertEquals("The most beautiful city is Paris.", outcome);
    }

   @Test
    public void testQuotationMark1() {
        MissingArticleFinder finder = new MissingArticleFinder(tagger.runTagging("He said he was by far one of \"best players\"."));
        List<String> insertionArticleSuggestion = finder.findMissingArticles();
        MissingArticleSuggestion suggestion = new MissingArticleSuggestion(insertionArticleSuggestion);
        String outcome = suggestion.createStringSuggestion();
        assertEquals("He said he was by far one of \"the best players\".", outcome);
    }

    @Test
    public void testAllUpperCase1() {
        MissingArticleFinder finder = new MissingArticleFinder(tagger.runTagging("GIVE ME BOOK!"));
        List<String> insertionArticleSuggestion = finder.findMissingArticles();
        MissingArticleSuggestion suggestion = new MissingArticleSuggestion(insertionArticleSuggestion);
        String outcome = suggestion.createStringSuggestion();
        assertEquals("GIVE ME THE BOOK!; GIVE ME A BOOK!", outcome);
    }

    @Test
    public void testAllUpperCase2() {
        MissingArticleFinder finder = new MissingArticleFinder(tagger.runTagging("HE'D BEEN TO PLACE BEFORE?!"));
        List<String> insertionArticleSuggestion = finder.findMissingArticles();
        MissingArticleSuggestion suggestion = new MissingArticleSuggestion(insertionArticleSuggestion);
        String outcome = suggestion.createStringSuggestion();
        assertEquals("HE'D BEEN TO THE PLACE BEFORE?!; HE'D BEEN TO A PLACE BEFORE?!", outcome);
    }

    @Test
    public void testWhQuestion1() {
        MissingArticleFinder finder = new MissingArticleFinder(tagger.runTagging("What car is this?"));
        List<String> insertionArticleSuggestion = finder.findMissingArticles();
        MissingArticleSuggestion suggestion = new MissingArticleSuggestion(insertionArticleSuggestion);
        String outcome = suggestion.createStringSuggestion();
        assertEquals("", outcome);
    }

    @Test
    public void testCardinalNumber1() {
        MissingArticleFinder finder = new MissingArticleFinder(tagger.runTagging("No thanks, I've already one cat."));
        List<String> insertionArticleSuggestion = finder.findMissingArticles();
        MissingArticleSuggestion suggestion = new MissingArticleSuggestion(insertionArticleSuggestion);
        String outcome = suggestion.createStringSuggestion();
        assertEquals("", outcome);
    }

    @Test
    public void testAdjectiveVBGConstruction1() {
        MissingArticleFinder finder = new MissingArticleFinder(tagger.runTagging("They serve best looking dishes."));
        List<String> insertionArticleSuggestion = finder.findMissingArticles();
        MissingArticleSuggestion suggestion = new MissingArticleSuggestion(insertionArticleSuggestion);
        String outcome = suggestion.createStringSuggestion();
        assertEquals("They serve the best looking dishes.", outcome);
    }

    @Test
    public void testLongSentence1() {
        MissingArticleFinder finder = new MissingArticleFinder(tagger.runTagging("If you want to go to supermarket you have to walk down the street, turn left at the end of the road and then turn right at yellow building."));
        List<String> insertionArticleSuggestion = finder.findMissingArticles();
        MissingArticleSuggestion suggestion = new MissingArticleSuggestion(insertionArticleSuggestion);
        String outcome = suggestion.createStringSuggestion();
        assertEquals("If you want to go to the supermarket you have to walk down the street, turn left at the end of the road and then turn right at the yellow building.; If you want to go to a supermarket you have to walk down the street, turn left at the end of the road and then turn right at a yellow building.", outcome);
    }

    @Test
    public void testCommonNounContraction1() {
        MissingArticleFinder finder = new MissingArticleFinder(tagger.runTagging("He wouldn't like actor."));
        List<String> insertionArticleSuggestion = finder.findMissingArticles();
        MissingArticleSuggestion suggestion = new MissingArticleSuggestion(insertionArticleSuggestion);
        String outcome = suggestion.createStringSuggestion();
        assertEquals("He wouldn't like the actor.; He wouldn't like an actor.", outcome);
    }

    @Test
    public void testCommonNounSuggestion1() {
        MissingArticleFinder finder = new MissingArticleFinder(tagger.runTagging("Boy came to me."));
        List<String> insertionArticleSuggestion = finder.findMissingArticles();
        MissingArticleSuggestion suggestion = new MissingArticleSuggestion(insertionArticleSuggestion);
        String outcome = suggestion.createStringSuggestion();
        assertEquals("The boy came to me.; A boy came to me.", outcome);
    }

    @Test
    public void testCommonNounAdjectiveSuggestion1() {
        MissingArticleFinder finder = new MissingArticleFinder(tagger.runTagging("It's beautiful river."));
        List<String> insertionArticleSuggestion = finder.findMissingArticles();
        MissingArticleSuggestion suggestion = new MissingArticleSuggestion(insertionArticleSuggestion);
        String outcome = suggestion.createStringSuggestion();
        assertEquals("It's the beautiful river.; It's a beautiful river.", outcome);
    }

    @Test
    public void testWrongIndefiniteArticle1() {
        MissingArticleFinder finder = new MissingArticleFinder(tagger.runTagging("I'd seen an dog and a owl."));
        List<String> insertionArticleSuggestion = finder.findMissingArticles();
        MissingArticleSuggestion suggestion = new MissingArticleSuggestion(insertionArticleSuggestion);
        String outcome = suggestion.createStringSuggestion();
        assertEquals("I'd seen a dog and an owl.", outcome);
    }

    @Test
    public void testWrongIndefiniteArticle2() {
        MissingArticleFinder finder = new MissingArticleFinder(tagger.runTagging("An rules aren't fair!"));
        List<String> insertionArticleSuggestion = finder.findMissingArticles();
        MissingArticleSuggestion suggestion = new MissingArticleSuggestion(insertionArticleSuggestion);
        String outcome = suggestion.createStringSuggestion();
        assertEquals("The rules aren't fair!", outcome);
    }

    @Test
    public void testProperNounSuggestion1() {
        MissingArticleFinder finder = new MissingArticleFinder(tagger.runTagging("I saw Empire State Building."));
        List<String> insertionArticleSuggestion = finder.findMissingArticles();
        MissingArticleSuggestion suggestion = new MissingArticleSuggestion(insertionArticleSuggestion);
        String outcome = suggestion.createStringSuggestion();
        assertEquals("I saw the Empire State Building.", outcome);
    }

    @Test
    public void testProperNounSuggestion2() {
        MissingArticleFinder finder = new MissingArticleFinder(tagger.runTagging("I'll go to 'Netherlands' and 'United Kingdom'."));
        List<String> insertionArticleSuggestion = finder.findMissingArticles();
        MissingArticleSuggestion suggestion = new MissingArticleSuggestion(insertionArticleSuggestion);
        String outcome = suggestion.createStringSuggestion();
        assertEquals("I'll go to 'the Netherlands' and 'the United Kingdom'.", outcome);
    }

    @Test
    public void testUncountableNoun1() {
        MissingArticleFinder finder = new MissingArticleFinder(tagger.runTagging("He was guest of honor."));
        List<String> insertionArticleSuggestion = finder.findMissingArticles();
        MissingArticleSuggestion suggestion = new MissingArticleSuggestion(insertionArticleSuggestion);
        String outcome = suggestion.createStringSuggestion();
        assertEquals("He was the guest of honor.; He was a guest of honor.", outcome);
    }

    @Test
    public void testUncountableNoun2() {
        MissingArticleFinder finder = new MissingArticleFinder(tagger.runTagging("Tea or coffee?"));
        List<String> insertionArticleSuggestion = finder.findMissingArticles();
        MissingArticleSuggestion suggestion = new MissingArticleSuggestion(insertionArticleSuggestion);
        String outcome = suggestion.createStringSuggestion();
        assertEquals("", outcome);
    }

    @Test
    public void testCompoundNoun1() {
        MissingArticleFinder finder = new MissingArticleFinder(tagger.runTagging("Let's look at int array closer."));
        List<String> insertionArticleSuggestion = finder.findMissingArticles();
        MissingArticleSuggestion suggestion = new MissingArticleSuggestion(insertionArticleSuggestion);
        String outcome = suggestion.createStringSuggestion();
        assertEquals("Let's look at the int array closer.; Let's look at an int array closer.", outcome);
    }

    @Test
    public void testCompoundNoun2() {
        MissingArticleFinder finder = new MissingArticleFinder(tagger.runTagging("They're gone, I think they went to basketball court."));
        List<String> insertionArticleSuggestion = finder.findMissingArticles();
        MissingArticleSuggestion suggestion = new MissingArticleSuggestion(insertionArticleSuggestion);
        String outcome = suggestion.createStringSuggestion();
        assertEquals("They're gone, I think they went to the basketball court.; They're gone, I think they went to a basketball court.", outcome);
    }

    @Test
    public void testSuperlativeAdjective1() {
        MissingArticleFinder finder = new MissingArticleFinder(tagger.runTagging("That's coldest winter you've experienced so far?!"));
        List<String> insertionArticleSuggestion = finder.findMissingArticles();
        MissingArticleSuggestion suggestion = new MissingArticleSuggestion(insertionArticleSuggestion);
        String outcome = suggestion.createStringSuggestion();
        assertEquals("That's the coldest winter you've experienced so far?!", outcome);
    }

    @Test
    public void testExcessiveArticle1() {
        MissingArticleFinder finder = new MissingArticleFinder(tagger.runTagging("This is the the food."));
        List<String> insertionArticleSuggestion = finder.findMissingArticles();
        MissingArticleSuggestion suggestion = new MissingArticleSuggestion(insertionArticleSuggestion);
        String outcome = suggestion.createStringSuggestion();
        assertEquals("This is the food.", outcome);
    }

}