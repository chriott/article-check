# Missing article checker

## Features

- Detects missing articles (a, an, the) in an English sentence using POS-tagged word sequences.
- Detects wrong usage of indefinite articles (for example: "I saw an dog.” → "I saw a dog.”).
- Detects double usage of articles in a sentence (for example: "I read the the book” → "I read the book”).

## About

This is a Java program that searches for missing articles in a given English sentence. If a missing article is detected, the "Missing article checker" provides a suggestion for an article insertion. 

The Stanford CoreNLP library is used to tokenize and part-of-speech-tag English sentences. In a second step, the parsing result is used to test POS-tag based word sequences to determine where articles are needed and an article suggestion can be made. The Standardized Gutenberg Corpus and online grammars (see resources) were utilized to detect such rules (for example, a singular common noun at the beginning of a sentence demands an article before the noun, an adjective-common-noun sequence demands an article before the adjective, definite articles before a distinct subset of proper nouns, ...). The tests for verifying the article rules and edge cases are written with JUnit Jupiter.

## Usage

### Terminal:

1. make sure you are in the main folder of the program
2. run `mvn clean install` to install required dependencies (CoreNLP, JUnit), run tests and compile the project
3. run `mvn exec:java` to execute the project. Enter your sentence in question into the user prompt.

(run `mvn test` to independently run all tests)

**Attention**: when working in IntelliJ IDEA, you might need to tick the option "Plugin registry" in `Settings/Build, Execution, Deployment/Maven` to run your project with `mvn exec:java`.

### Build and execute jar file:

1. make sure you are in the main folder of the program
2. run `mvn clean package shade:shade` to build a jar file including necessary dependencies
3. run `java -jar articlechecker-maven-demo-1.0-SNAPSHOT.jar` file in `/target` to execute the new jar file 
4. enter your sentence in question into the user prompt

## Resources

- https://dictionary.cambridge.org/grammar/british-grammar/a-an-and-the (Article rules)
- https://learnenglish.britishcouncil.org/grammar/english-grammar-reference/the-definite-article-the (Article rules)
- https://dictionary.cambridge.org/de/grammatik/britisch-grammatik/uncountable-nouns (Uncountable nouns)
- https://7esl.com/uncountable-nouns/ (List of uncountable nouns)
- https://stanfordnlp.github.io/CoreNLP/api.html (CoreNLP api documentation)
- https://github.com/pgcorpus/gutenberg (Standardized Gutenberg Corpus)
