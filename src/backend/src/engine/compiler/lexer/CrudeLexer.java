package engine.compiler.lexer;

import engine.compiler.Token;
import engine.errors.CommandSyntaxException;
import engine.compiler.translator.LanguageTranslator;
import engine.compiler.translator.TypeTranslator;
import engine.errors.UndefinedKeywordException;

import java.util.*;

/**
 * This CrudeLexer reads in a String input and output a queue of Tokens representing the basic syntactic building blocks in the SLogo language.
 *
 * @author Haotian Wang
 */
public class CrudeLexer implements Lexer {
    private static final String PREFIX = "engine/compiler/languages/";
    private static final String DEFAULT_LANGUAGE = "English";
    private static final String SYNTAX = "Syntax";
    private static final String SYNTACTICAL_CATEGORIES = "SyntacticCategories";
    private static final Map<String, String> myGrammerMap;
    static {
        Map<String, String> grammarMap = new HashMap<>();
        ResourceBundle categories = ResourceBundle.getBundle(PREFIX + SYNTACTICAL_CATEGORIES);
        for (Enumeration<String> e = categories.getKeys(); e.hasMoreElements(); ) {
            String action = e.nextElement();
            grammarMap.put(action, categories.getString(action));
        }
        myGrammerMap = Collections.unmodifiableMap(grammarMap);
    }

    private TypeTranslator myType;
    private LanguageTranslator myLanguage;
    private List<Token> myTokens;

    /**
     * Constructs a CrudeParser with the user-defined language as the starting recognized language.
     *
     * @param language: A String such as "English", "Chinese" or "French".
     */
    public CrudeLexer(String language) {
        myType = new TypeTranslator(PREFIX + SYNTAX);
        myLanguage = new LanguageTranslator(PREFIX + language);
        myTokens = new ArrayList<>();
    }

    /**
     * The default constructor will choose English.properties as its starting recognized languages and Syntax.properties as its starting recognized syntax.
     */
    public CrudeLexer() {
        this(DEFAULT_LANGUAGE);
    }

    /**
     * Reset the language dictionary to use the default language only, which is English.
     */
    @Override
    public void resetLanguage() {
        myLanguage.setPatterns(PREFIX + DEFAULT_LANGUAGE);
    }

    /**
     * Set the language dictionary to use the designated languages.
     *
     * @param languages : A String array of languages
     * @throws MissingResourceException
     */
    @Override
    public void setLanguage(String... languages) throws MissingResourceException {
        if (languages.length == 0 || languages == null) {
            return;
        }
        myLanguage.setPatterns(null);
        addLanguage(languages);
    }

    /**
     * Add more languages to the internal dictionary.
     *
     * @param languages : A String array of languages.
     * @throws MissingResourceException
     */
    @Override
    public void addLanguage(String... languages) throws MissingResourceException {
        if (languages.length == 0 || languages == null) {
            return;
        }
        for (String language : languages) {
            myLanguage.addPatterns(PREFIX + language);
        }
    }

    /**
     * This processes the user input raw String and tokenizes the input String.
     *
     * @param input : A user input raw String.
     */
    @Override
    public void readString(String input) throws UndefinedKeywordException {
        myTokens.clear();
        if (input == null || input.isEmpty()) {
            return;
        }
        int start = 0;
        int end = 0;
        while (start < input.length() && end < input.length()) {
            try {
                myType.getSymbol(input.substring(start, end + 1));
                while (end < input.length() && myType.containsString(input.substring(start, end + 1))) {
                    end++;
                }
                end--;
                String chunk = input.substring(start, end + 1);
                String type = myType.getSymbol(chunk);
                if (type.equals("Comment") || type.equals("Whitespace") || type.equals("Newline")) {
                    start = end + 1;
                    end++;
                    continue;
                } else if (type.equals("Command")) {
                    try {
                        chunk = myLanguage.getSymbol(chunk);
                    } catch (UndefinedKeywordException e) {
                        /*if (myTokens.isEmpty() || !myTokens.get(myTokens.size() - 1).getString().equals("MakeUserInstruction")) {
                            throw e;
                        }*/
                        myTokens.add(new Token(chunk, "Variable"));
                        start = end + 1;
                        end++;
                        continue;
                    }
                    type = myGrammerMap.get(chunk);
                    start = end + 1;
                    end++;
                } else {
                    start = end + 1;
                    end++;
                }
                myTokens.add(new Token(chunk, type));
            } catch (UndefinedKeywordException e) {
                if (input.charAt(end) == ' ' || input.charAt(end) == '\n') {
                    throw new UndefinedKeywordException(String.format("The input String \"%s\" is not defined in the properties files", input.substring(start, end)));
                }
                end++;
            }
        }
        if (start != input.length() || end != input.length()) {
            throw new UndefinedKeywordException("The input String contains tokens that are not properly defined in the properties files.");
        }
    }

    /**
     * Return a list of Token from the input String, after translation by two translators.
     *
     * @return A list of Token from the input String.
     */
    @Override
    public List<Token> getTokens() {
        return myTokens;
    }
}
