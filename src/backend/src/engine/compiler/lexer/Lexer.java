package engine.compiler.lexer;

import engine.compiler.utils.Token;
import engine.errors.UndefinedKeywordException;

import java.util.List;
import java.util.MissingResourceException;

/**
 * This interface handles the tokenization of input strings into a set of tokens.
 *
 * @author Haotian Wang
 */
public interface Lexer {
    /**
     * This processes the user input raw String.
     *
     * @param input: A user input raw String.
     */
    void readString(String input) throws UndefinedKeywordException;

    /**
     * Return a list of Token from the input String, after translation by two translators.
     *
     * @return A list of Token from the input String.
     */
    List<Token> getTokens();

    /**
     * Reset the language dictionary to use the default language only, which is English.
     */
    void resetLanguage() throws MissingResourceException;

    /**
     * Set the language dictionary to use the designated languages.
     *
     * @param languages: A String array of languages
     * @throws MissingResourceException
     */
    void setLanguage(String... languages) throws MissingResourceException;

    /**
     * Add more languages to the internal dictionary.
     *
     * @param languages: A String array of languages.
     * @throws MissingResourceException
     */
    void addLanguage(String... languages) throws MissingResourceException;
}
