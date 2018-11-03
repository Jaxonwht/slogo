CompSci 308: SLogo Analysis
===================

> This is the link to the assignment: [SLogo](http://www.cs.duke.edu/courses/compsci308/current/assign/03_slogo/)


Design Review
=======

### Overall Design
Reflect on the coding details of the entire project by reading over all the code.
###### Describe the overall design of the complete program:
* What is the high level design of each part (front and back end) and how do they work together (i.e., what behavior, data, or resources each part depends on from the others):
    * Front end and back end employs the Model-View-Controller pattern for the SLogo project.
    * The front end module contains controllers and JavaFx views that are presented to the user as a SLogo GUI interface. The View has the whole JavaFx stage, scene, node architecture, and the controllers implement the Observer and Listener pattern such that front end JavaFx objects reflect changes in the model.
    * The back end module contains two parts, the model that contains observed data to be presented and an engine that handles the interaction between the user and the model. In summary, the back end module handles how data are modified and affected by user input.
    * The front end depends on the back end for updates in model and also partially in the storage machine used by the engine, because the front end has to present such information as what the parser thinks the user input is, extensive syntax checking and values of variables and user-defined functions that are not stored in the model but in the engine.
* What is needed to add one of the following (include all the parts of the code or resources that need to be changed):
    * New command to the language:
        * There are two cases to this scenario. In the formal ENBF grammar rule, the command may belong to an existing syntactic category (such as binary, unary) or not.
        * If the command belongs to an existing syntactic category, there is no need to make changes to the parser. One just adds one more entry to engine.compiler.slogoast, in the ifelse conditional loops, add the new command name there and write the logic of the new command there too.
        * If the command belongs to a new syntactic category, then he needs to add a new class representing this new syntactic category to the slogoast package and also a new parseNewCommand method to the parser in engine.compiler.parser.CrudeParser. The logic happens in slogoast, and the syntax checking happens in the Parser.
    * New component to the front end:
        * One just adds the respective classes in controller package and view package. The ControllerModule in controller package loads the newly added component. Right now our controllers and views are not separated that clearly, but preferably the data that the new component relies on should be fed to the view by the new controller.
    * Unimplemented specification:
        * There is only feature that we did not implement, which is unlimited paraters.
        * There should be a change in the definition of syntactic categories in the first place. My proposed solution is that there should not be Unary, Binary or the crazy Quaternary syntactic categories in the first place. They should be replaced by a Nnary category, which has an additional int parameter, the number of accepted arguments. Then when we detect brackets, our existing parser should then enter the mode that checks for unlimited parameters first before checking for other grammar rules. Then we have to write a lot of if else in this new Nnary category telling how each of the command should be interpreted. However this is not good enough, because according to the specification, all the commands should at least syntactically allow unlimited parameters. I don't have a good solution that solves this generality problem.
* Are the dependencies between the parts clear and easy to find (e.g., public methods and parameters) or do they exist through "back channels" (e.g., static calls, order of method call, sub-class type requirements)?
    * Public methods are clearly written in an interface for each API as much as possible. There are some dependencies between lexer and parser that are not that significant. One area for improvement is that the StateMachine is used at various places in back end and front end. The slogoast needs the StatemMachine to call the interpret method and the front end variable pane needs this StateMachine to present variables and user-defined functions in the storage.
    * The controller in the MVC pattern could be more abstracted out, because right now some View classes depend on the back end model, which is not desirable.
###### Reflect on the project's APIs especially the two you did not implement:
* What makes it easy to use and hard to misuse or not (i.e., do classes and methods do what you expect and have logic that is clear and easy to follow)? Give specific examples.
    * I developed the whole lexer -> parser -> slogoast (interpreter) architecture, but I didn't develop any specific commands except the simple math operations such as sine and addition. Arthur's code on specific commands is very easy to read. I will take the more complicated For loop as an example.
        ```java
        @Override
        public double interpret(TurtleManager turtleManager) throws InterpretationException, UndefinedKeywordException {
            double ret = 0;
            if (myToken.getString().equals("For")) {
                String variableName = var.getVariableName();
                boolean reset = turtleManager.memory().containsVariable(variableName);
                double old = 0;
                if (reset){
                    old = (double)turtleManager.memory().getValueInGeneralForm(variableName);
                }

                StateMachine memory = turtleManager.memory();
                for (double counter = min.evaluate(turtleManager); counter < max.evaluate(turtleManager);  counter += step.evaluate(turtleManager)){
                    memory.setVariable(var.getVariableName(), counter, VariableType.DOUBLE);
                    ret = expressionList.interpret(turtleManager);
                }
                if (reset){
                    turtleManager.memory().setVariable(variableName, old, VariableType.DOUBLE);
                } else{
                    turtleManager.memory().removeVariable(variableName);
                }
            }
            return ret;
        }
        ```
        The interpret method does exactly what the API specifies for each SLogo command. It returns double value for executing the command, and applies relevant changes to the turtle and the canvas. It throws the correct exception so that the front end module that relies on the engine could handle these exceptions.
    * However since I didn't develop front end, and it is a bit hard for me to figure out the underlying API for all the View and Controller classes especially when there is no interface or comment written for those. On the bright side, though it takes a bit of time to figure out, the front end is organized in a clear hierarchy. There is clear separation of Controller and View and a Module class in each that loads different controllers and views.
        ```java
        public class ViewModule {
            private MainView mainView;
            private SidebarView sidebarView;
            private CanvasView canvasView;
            private VariableView variableView;
            private HistoryView historyView;
            private CommandView commandView;

            public ViewModule(EngineAPI engineAPI) {
                sidebarView = new SidebarView();
                canvasView = new CanvasView();
                variableView = new VariableView(engineAPI.stateMachine());
                historyView = new HistoryView();
                commandView = new CommandView();
                mainView = new MainView(sidebarView, canvasView, variableView, historyView, commandView);
            }

            public MainView mainView() { return mainView; }
            public SidebarView sidebarView() { return sidebarView; }
            public CanvasView canvasView() { return canvasView; }
            public VariableView variableView() { return variableView; }
            public HistoryView historyView() { return historyView; }
            public CommandView commandView() { return commandView; }
        }
        ```
        This intermediate class nicely gives access to different View classes.
    * One slight drawback is some View is trying to do the work of Controller. For example, in TurtleView,
        ```java
        @Override
        public void setBackground(String colorStr) { bgColorChange.accept(Color.valueOf(colorStr)); }

        @Override
        public void setPenColor(String colorStr) { penColor = Color.valueOf(colorStr); }

        @Override
        public void setPenSize(int pixels) { penWidthChange.accept((double) pixels); }

        @Override
        public void setShape(String shapeStr) { }
        ```
        This is actually legacy code that we wrote in the first week to meet the basic requirements. It is clear that a command enters will pass through the back end and call some slogoast.interpret(turtlemanager) somwhere, but it is not clear how that interpret command takes effect and gets reflected on the screen. Instead of parsing through the Controller in MVC, the model is communicating directly with this front end view class to set pen color for example. This is not a desirable design. Though named differently, these methods are essentially getters and setters for some JavaFx properties. These getters and setters are used by a model in the back end, and a new developer may find it hard to navigate where exactly in the back end these getters and setters are callled. Even if we have to write such code, probably we should written clear documentation telling a developer why we must write these methods instead of deferring the work to the Controller. Also background color seems to be a property of the canvas and this View for Turtle is overstepping its responsibility. In effect, this TurtleView is a mashup of different view and controller that is hard to extend.
* What makes it encapsulated or not (i.e., can the implementation be easily changed without affecting the rest of the program)? Give specific examples.
    * The TurtleManager interface is well encapsulated.
        ```java
        /**
         *  An extension to the TurtleModel;
         *  It has all the methods within the TurtleModel but it applies the method to
         *  all the selected turtles.
         */
        public interface TurtleManager extends TurtleModel {
            /**
             *  SPECIAL ID to represent ALL TURTLES within this model
             */
            int ALL = 0;

            /**
             * @return id of the LAST turtle within the selected group, always > 0
             */
            int id();

            /**
             * @return id of the LAST turtle within the selected group, always > 0
             */
            List<Integer> selected();

            /**
             * @return total number of turtles created
             */
            int size();

            /**
             * Adds a turtle with the given ID
             * REJECTS ID <= 0
             * @return the ID of the turtle that we just added
             */
            int addTurtle(int id) throws IllegalParameterException;

            /**
             * Returns ObservableMap of (ID, TurtleModel)
             */
            ObservableMap<Integer, TurtleModel> turtleModels();

            /**
             * Selects all the turtles with given IDs
             * All operations will operate only to these selected turtles
             * @return id()
             */
            int tell(List<Integer> turtleIDs);

            /**
             * @return StateMachine
             */
            StateMachine memory();
            void equipMemory(StateMachine memory);

            void registerSelectionListener(SelectionListener listener);
            void registerUIListener(UIListener listener);
        }
        ```
        This class serves to handle the case of multiple turtles. It uses the delegation pattern, delegating a turtle's behavior to TurtleModel and delegating the behavior of the storage machine to StateMachine interface. On the one hand, one can easily switch to another TurtleManager that implements these methods differently, on the other hand one can easily instantiate a turtle manager with different implementations of TurtleModel and StateMachine. This class serves a nice bridge between single turtles and multiple turtles.
    * The ControllerModule class makes it easy to switch between different implementations of front end components.
        ```java
        public ControllerModule(
                    TabbedApp app,
                    ModelModule modelModule,
                    EngineAPI engineApi,
                    ViewModule viewModule,
                    Consumer<String> setEngineLanguage
            ) {
                editorController = new EditorController(viewModule.commandView(), viewModule.historyView(), engineApi);
                sidebarController = new SidebarController(DEFAULT_LANG, app, viewModule.sidebarView(), modelModule.turtleManager(), setEngineLanguage);
                canvasController = new CanvasController(modelModule.turtleManager(), viewModule.canvasView());
                assemble();
            }

            private void assemble() { sidebarController.registerControllers(editorController, canvasController); }
        ```
        This class is good because in the main Application class one can simply call
        ```java
        new ControllerModule(this, modelModule, engineApi, viewModule, engineApi::setLanguage);
        ```
        How the detailed modules are implemented can be deferred to these classes. The area for improvement is that the constructors for each module are quite specific and they take parameters from different levels of API. This dependency may be disadvantageous to encapsulation.
* What have you learned about design (either good or bad) by reading your team mates' code?
    * I have learned from the TurtleManager and TurtleModel classes that I should not simply substitute in other classes TurtleModel with List<TurtleModel> immediately when I see the client wants us to implement multiple turtles. My teammate's way of doing things ensures encapsulation and an additional layer of flexibility. Especially with the lately introduced tell, ask, askwith commands. It doesn't make sense for specific commands to make low level changes to this list of TurtleModel. Rather the batch operation should be abstracted to a TurtleManager class.
    * In back end model package, the multiple interfaces are a bit weird to me. Each of them contains one method and a class implements all these interfaces, and no other class implements these interfaces. Frankly I still don't quite understand the rationale. This shows the importance of documentation.
###### Is the total code generally consistent in its layout, naming conventions and descriptiveness, and style? Give specific examples for or against by comparing code from different team members.
* Layout
    * The layout is pretty consistent. The classes are organized into a hierarchy of packages in each module following a MVC pattern. Classes as much as possible have their implementing interfaces under the same package. A difference is that back end does not use the Resources Root feature shipped with IntelliJ Idea while front end module does.
* Naming conventions
    * All classes, variables, methods and final variables follow the Java naming conventions.
* Descriptiveness
    * To be honest, I wrote very extensive comments for my part of code in back end. I gave comments to the purpose of each interface, each class and each method, and my teammates did not write enough comment for others to understand their code. The names of methods and classes are well designed, though, to give a general sense of what each class does. It follows the principle of behavior driven classes as told from their class names.
* Style
    * The style of code is kind of different. The distinction between front end and back end modules are very different. Back end module classes function like black boxes while front end module classes have to communicate with different layers of classes. <br>
        My CrudeLexer is this,
        ```java
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
        ```
        It tends to have a streamlined processing procedure. Inchan's code,
        ```java
        public void addText(String text, double retVal) {
                display.getChildren().add(new HistoryUnit(text, retVal).flow());
            }

        public void displayError(String text, Exception e) {
            display.getChildren().add(new HistoryUnit(text, e).flow());
        }

        private class HistoryUnit {
            private TextFlow flow;

            private HistoryUnit(String command) {
                flow = new TextFlow();
                PrettyUI.litBgTheme(flow);
                flow.getChildren().addAll(PrettyUI.highlight(command.trim(), FONT_SIZE));
            }

            private HistoryUnit(String command, Exception e) {
                this(command);
                flow.getChildren().add(new Text("\n"));
                flow.getChildren().addAll(PrettyUI.error(e.toString().trim(), FONT_SIZE));
            }

            private HistoryUnit(String command, double retVal) {
                this(command);
                flow.getChildren().add(new Text("\n>> "));
                flow.getChildren().addAll(PrettyUI.number(String.valueOf(retVal), " ", FONT_SIZE));
                flow.setOnMousePressed(ev -> onHistoryClick.accept(command));
            }

            private TextFlow flow() { return flow; }
        }
        ```
        tends to have a very modular approach to composing different elements. This is probably due to the different nature of front end and back end.

### Your Design
Reflect on the coding details of your part of the project.
###### Describe how your code is designed at a high level (focus on how the classes relate to each other through behavior (methods) rather than their state (instance variables)).
* I designed the compiler and engine API, though I didn't implement what the specific commands does. The whole Engine API simply takes in an input String and tries to "execute those commands". The public methods are
    ```java
    double processString(String str) throws UndefinedKeywordException, CommandSyntaxException, InterpretationException;
    void setLanguage(String language);
    StateMachine stateMachine();
    ```
* The engine/compiler API could roughly contain Translator, Lexer, Parser, Interpreter.
* Translator translates an input String in a different language to a common english language using Regex. It also translates an input String to the type that String belongs to (such as variable, comment, command). Its public methods are
    ```java
    void addPatterns(String file) throws MissingResourceException;
    void setPatterns(String file) throws MissingResourceException;
    String getSymbol(String text) throws UndefinedKeywordException;
    boolean containsString(String text);
    ```
* Lexer tokenizes the translated String into a list of Tokens. Notice this is not just splitting the String by space. It is an O(n^3) algorithm where n is the number of characters in the input String. It public methods are
    ```java
    void readString(String input) throws UndefinedKeywordException;
    List<Token> getTokens();
    void resetLanguage() throws MissingResourceException;
    void setLanguage(String... languages) throws MissingResourceException;
    void addLanguage(String... languages) throws MissingResourceException;
    ```
    The Lexer contains methods that seem to should have belonged to Translator. This is a legacy design issue.
* Parser parses the list of Tokens into a syntax tree, which is represented by my designed Expression interface as the root node. Its public methods are
    ```java
    void readTokens(List<Token> tokens) throws CommandSyntaxException;
    void clearTokens();
    Expression returnAST();
    ```
    AST is short for abstract syntax tree. Though I learned that there is actually a difference between syntax tree and abstract syntax tree, but that name is used for our syntax tree anyway.
* Finally, Interpreter is not implemented as a separate hierarchy of classes, the reason of which will be discussed in the Alternate Designs section. For now, it suffices to show that the interpretation or execution of an AST is done by calling the method, interpret on the root Expression. The public methods of Expression are
    ```java
    String toString();
    double interpret(TurtleManager turtleManager) throws InterpretationException, UndefinedKeywordException;
    default double evaluate(TurtleManager turtleManager) throws InterpretationException, UndefinedKeywordException
    ```
    The toString() method provides a way for the front end to present the parsed command in a readable way. Besides interpret, I designed an evaluate method. Why it is a default method and why it exists shall be discussed later.
* Apart from this workflow, I designed a useful StateMachine API that can store different variables. Its public methods are
    ```java
    void setDouble(String key, double value);
    void setInteger(String key, int value);
    void setString(String key, String value);
    void setExpression(String key, Expression function);
    void setVariable(String key, Object value, VariableType type);
    VariableType getVariableType(String key) throws UndefinedKeywordException;
    Object getValueInGeneralForm(String key) throws UndefinedKeywordException;
    void removeVariable(String key) throws UndefinedKeywordException;
    void resetState();
    Map<String, Object> listOfVariables();
    void register(StateMachineObserver observer);
    boolean containsVariable(String key);
    void pushAlarm();
    String toString();
    ```
    Though this seems complicated, it is actually just a Map<String, Object> but honors different types of variables stored. It implements the Observer pattern for better synchronization with the front end.
###### Discuss any remaining Design Checklist issues within you code (justify why they do not need to be fixed or describe how they could be fixed if you had more time).
* My parser implemented a lot of syntax error checking, so I'm not sure if the String messages are considered magic values, but there are a lot of those. For example,
    ```java
    if (expressionList.isEmpty()) {
        throw generateSyntaxException("Missing a valid expression to constitute a valid list of expressions", pointer);
    }
    Pair<Token, Integer> listEndPair = parseToken(pointer, "ListEnd");
    if (listEndPair.getKey() == null) {
        throw generateSyntaxException("Missing \"]\" symbol to end a list of expressions", listEndPair.getValue());
    }
    ```
    I feel in this specific case, magic values should be allowed. These Strings should just be put in place rather that declared as private static final or put in a properties file.
* Some AST classes that implement Expression contain a lot of if else statements and do not adhere to the polymorphism principle. For example, in Binary,
    ```java
    double currentX = turtleManager.getX();
    double currentY = turtleManager.getY();
    if (myToken.getString().equals("Sum")) {
        return myFirstExpr.evaluate(turtleManager) + mySecondExpr.evaluate(turtleManager);
    } else if (myToken.getString().equals("Difference")) {
        return myFirstExpr.evaluate(turtleManager) - mySecondExpr.evaluate(turtleManager);
    } else if (myToken.getString().equals("Quotient")) {
        if (mySecondExpr.evaluate(turtleManager) == 0) {
            throw new InterpretationException("The denominator in a Quotient operation cannot be zero");
        }
        return myFirstExpr.evaluate(turtleManager) / mySecondExpr.evaluate(turtleManager);
    } else if (myToken.getString().equals("Product")) {
        return myFirstExpr.evaluate(turtleManager) * mySecondExpr.evaluate(turtleManager);
    }
    ```
    The primary reason is that actually that I don't know how to implement polymorphism clearly in this case without using (1) reflection, (2) visitor pattern. The reason for not using these methods will be discussed later. However, for now, this indeed remains as a checklist issue.
* Parser contains a load of code duplicates (though not exactly the same code). For example in CrudeParser, I have
    ```java
    private Pair<Expression, Integer> parseMakeVariable(int index) throws CommandSyntaxException {
        Pair<Expression, Integer> nullPair = new Pair<>(null, index);
        Pair<Token, Integer> makeVariablePair = parseToken(index, "MakeVariable");
        if (makeVariablePair.getKey() == null) {
            return nullPair;
        }
        Pair<Expression, Integer> variablePair = parseVariable(makeVariablePair.getValue());
        if (variablePair.getKey() == null) {
            throw generateSyntaxException("Illegal variable format after \"make\" command in a MakeVariable command", variablePair.getValue());
        }
        Pair<Expression, Integer> expressionPair = parseExpression(variablePair.getValue());
        if (expressionPair.getKey() == null) {
            throw generateSyntaxException("Illegal format for an expression that is assigned to the variable in a MakeVariable command", expressionPair.getValue());
        }
        return new Pair<>(new MakeVariable(makeVariablePair.getKey(), (Variable) variablePair.getKey(), expressionPair.getKey()), expressionPair.getValue());
    }
    ```
    Obviously there is some kind of repeated command in this piece of code. In fact, most lines in CrudeParser take a very similar structure of coding. The problem that this obvious duplicate code remains is that I do not know how to refactor these cleanly, when there are minor differences between here and there. It is a problem central to my understanding of recursive descent parsing. How to handle that recursive behavior elegantly is a question that I don't have a working solution to.
###### Describe two features that you implemented in detail — one that you feel is good and one that you feel could be improved:
* I really like my Lexer.
    * Justify why the code is designed the way it is or what issues you wrestled with that made the design challenging.
        * Lexer is designed with a clear goal in mind. It should take an input raw String and output a list of Tokens with predefined keywords in one language. With this goal in mind, I can design a very clean API and implement those methods accordingly. I particularly like it because each part of Lexer is modularized. The developer may want to change the algorithm that tokenizes the input String to simply splitting by whitespace instead of the current algorithm, which I am proud of. This doesn't affect the rest of the program as long as the method signature is correct. Lexer throws UndefinedKeywordException clearly, allowing the front end to handle it. There is some interdependency between Lexer and Translator that could be improved. There is a suite of methods that are about changing the user input languages defined in Lexer, which are simply calling corresponding methods in Translator. This part is unnecessary. For example,
            ```java
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
            ```
    * Are there any assumptions or dependencies from this code that impact the overall design of the program? If not, how did you hide or remove them?
        * One dependency is that Lexer actually depends on two Translator objects. For Lexer to function properly, Translator cannot be modified at will. Also the syntax.properties file could not be edited anyhow as well. There is code like
            ```java
            if (type.equals("Comment") || type.equals("Whitespace") || type.equals("Newline")) {
                                start = end + 1;
                                end++;
                                continue;
            }
            ```
            These are in a sense hard-coded and depend on how user defines these keywords in resource files. A better way would be to write enum types rather than Strings to represent different types of Tokens.
        * Another not so clean dependency is that I realize whether a word exists cannot be determined simply looking at the property files. Lexer must have access to the TurtleManager object's StateMachine instance to check whether a keyword has been defined by the user. To deal with this issue, I simply let the Lexer rewrite every undefined keyword's type to Variable. Syntax checking of Variable type is then deferred to the interpreter. Other than this, Lexer's dependency doesn't impact the whole project.
* I don't really like my Expression interface and classes.
    * Justify why the code is designed the way it is or what issues you wrestled with that made the design challenging.
        * As discussed before, Expression is an interface that has only two methods, interpret and execute. The main issue is assuming the Parser constructs a correct Expression tree, what are we going to do about it. I find it really hard to write some kind of interpreter that does the actual job of "executing" the syntax tree. If I use the ugly
            ```java
            void interpret(Expression expression) {
                if (expression instanceof Binary) {...}
                else if ...
                ...
            }
            ```
            I need to use even more if else statements in each if else to see what the actual commands are. Therefore here I implemented polymorphism to let the AST node itself decides what interpret method should be depending on what its type is. However there is this second layer of polymorphism I have not yet implemented. In Binary, there are many if else statements to check what specific Binary type it is. It is not simple that I can just make more classes. Say instead of writing a Binary class, I write Plus, Minus and Product classes. This would increase my work for writing Parser. I have to use reflection or something to create these classes. Reflection and if else statements have to appear in some part of the project. This is a problem I struggled with a lot.
    * Are there any assumptions or dependencies from this code that impact the overall design of the program? If not, how did you hide or remove them?
        * In a sense, the Token if else checking in each syntactic category is hard-coded,
            ```java
            if (myToken.getString().equals("PenUp")) {
                return turtleManager.setPenDown(false);
            }
            else if (myToken.getString().equals("PenDown")) {
                return turtleManager.setPenDown(true);
            }
            else if (myToken.getString().equals("ShowTurtle")) {
                return turtleManager.setVisible(true);
            }
            else if (myToken.getString().equals("HideTurtle")) {
                return turtleManager.setVisible(false);
            }
            ```
            If I choose to rename these commands, I have to change the Strings accordingly, which is not good for development. Also calling myToken.getString().equals() is cumbersome in the long run, and I should substitute Token's type with some enum other than String if I have more time.

### Flexibilty
Reflect on what makes a design flexible and extensible.
###### Describe what you think makes this project's design flexible or not (i.e., able to support adding similar features easily).
* Our program is written with a skeleton in mind. There is a clear API that governs how different components relate to each other. More specifically there are clearly written rules for input and output. This enables us to make extensions and substitutions for different modular parts of the program. A Lexer could be substituted easily by another Lexer and a HistoryView could easily be substituted by another HistoryView. In fact, I did have two versions of StateMachine. CrudeStateMachine and StateMachineV2. I can switch these back and forth without worrying about dependency in other parts of the project.
###### Describe two features from the assignment specification that you did not implement in detail (these can overlap the previous ones but must be discussed from this different perspective):
* Multiple workspaces. My teammate implemented the tabbed feature that allows the user to work in a separate environment with a new set of variables and user-defined functions.
    * What is interesting about this code (why did you choose it)?
        * The method of implementation is really not how I see myself doing it. He created a new TabbedApp interface and let the main runnable SLogoApp implement this new interface. Admittedly, this interface has only one line,
            ```java
            public interface TabbedApp { void newInstance(); }
            ```
            But it's interesting to see the first instinct to solve such a problem is to write an extra interface.
    * What classes or resources are required to implement this feature?
        * Only the SLogoApp class is required.
    * Describe the design of this feature in detail (what parts are closed? what implementation details are encapsulated? what assumptions are made? do they limit its flexibility?)
        * The newInstance method is overriden as such,
            ```java
            @Override
            public void newInstance() {
                ModelModule modelModule = new ModelModule();
                EngineAPI engineApi = new ASTEngineAPI(modelModule.turtleManager());
                ViewModule viewModule = new ViewModule(engineApi);
                new ControllerModule(this, modelModule, engineApi, viewModule, engineApi::setLanguage);

                var tab = new Tab("Untitled " + tabIndex);
                if(tabIndex == 1) tab.setClosable(false);
                tabIndex ++;
                tab.setContent(viewModule.mainView().view());
                tabPane.getTabs().add(tab);
            }
            ```
            This is really smart as it utilizes the other classes well instead of rewriting code. The merits of abstracting out the Controller and View manifest in this piece of code because create a new workspace is the same as creating a new ControllerModule and a new ViewModule. This code taps into the benefits of delegation instead of recreating the whole boiler plate again. How the ControllerModule and ViewModule are instantiated are closed. If we have more time and want to develop the feature of saving user-preferences, this design easily allows it because all that detailed work is handled by ControllerModule.
    * How extensible is the design of this feature (is it clear how to extend the code as designed? what kind of change might be hard given this design?)
        * This code is extensible because it taps into the modular design of ControllerModule and ViewModule. New changes could easily be deferred to those two classes as a delegation pattern.
        * However this design is limited as the same time if the user wants to develop features that cannot be achieved by individual ControllerModule or ViewModule alone. For example, it will be really hard for the user to merge the variables, turtles from different workspaces into one.
* Another feature is to allow the user to click on executed commands and reexecute them.
    * What is interesting about this code (why did you choose it)?
        * This is mainly because I particularly like the HistoryView design and it allows the inclusion of such feature easily. HistoryView is the history of commands window beside the main editor pane. Inside such a "side" pane, modular design can be implemented. It uses the Consumer pattern as well to avoid rewriting code or creating unnecessary pointers to instances.
    * What classes or resources are required to implement this feature?
        * HistoryView, CommandView and EditorController together implement this feature. A sketch of the calling stack is this:
            ```java
            public class EditorController {
                public EditorController(CommandView commandView, HistoryView historyView, EngineAPI engineApi) {
                    this.historyView.registerOnHistoryClick(s -> commandView.view().setText(s));
                }
            }

            public class CommandView {
                private TextArea root;

                public TextArea view() { return root; }
            }

            public class HistoryView {
                private Consumer<String> onHistoryClick;

                public void registerOnHistoryClick(Consumer<String> handler) { onHistoryClick = handler; }

                private class HistoryUnit {
                    private TextFlow flow;

                    private HistoryUnit(String command, double retVal) {
                        flow.setOnMousePressed(ev -> onHistoryClick.accept(command));
                    }
                }
            }
            ```
        * Describe the design of this feature in detail (what parts are closed? what implementation details are encapsulated? what assumptions are made? do they limit its flexibility?)
            * What happens when clicking on the executed command is closed from the HistoryView class itself and it allows that to be set by some external classes through instantiating a Consumer<String> anywhere in the whole project.
            * The assumption is made that only successfully executed commands can be re-executed.
            * This limits flexibility because the user may simply want to click on a wrongly-typed command, and correct some obvious mistakes before resubmitting. The current framework does not allow that to happen.
        * How extensible is the design of this feature (is it clear how to extend the code as designed? what kind of change might be hard given this design?)
            * Frankly there are a lot of layers in this design for the feature. Several classes are involved and the Consumer pattern is not very intuitive to understand. However the benefit lies in this Consumer pattern at the same time. Suppose the user wants some other things to happen when clicking on a command history, some other part of the project could easily call registerOnHistoryClick and pass in some other Consumer of String there. This allows for great flexibility.
            * Hard changes are bigger changes that may require us to change the whole view structure. It's not immediately clear what class is calling what or should call what, given that there is no interface written for these.


### Alternate Designs
Reflect on alternate designs for the project based on your analysis of the current design or project discussions.
###### Describe how well (or poorly) the original design handled the project's extensions to the original specifications and how these changes were discussed and decisions ultimately made.
One crucial difference is how we execute the commands. The old back end had three submodules. A TurtleModel, a package of all the command classes (for example, Sine is a class for the sine command) and a Parser class. In the old design,
* TurtleModel contains data like the turtle's positions, pen status and directions etc.
* The Command classes take in the TurtleModel and act on the TurtleModel.
* Parser parses input String by splitting at whitespace, and instantiates Command objects using reflection. TurtleModel and other parameters are passed in as arguments for the constructors of these Command objects.
* Front end observes the model and reflect relevant changes, just like now.

This original design obviously doesn't handle the extension well. In fact, it is far from meeting the basic requirements. When there is composition of commands such as sum sin 45 sin 45, reflection simply cannot instantiate the correct Sum object from this input string by simply splitting at whitespaces. However, this old model had no problem incorporating multiple turtles as we simply make use of a new TurtleManager class in place of TurtleModel. Our team's solution to this problem was an interesting one. In fact from the very start, we had a discussed proper overall Engine API for the whole backend, i.e. what does the back end Engine does. From the first day, Arthur, with the help of Inchan started coding the ad hoc parser and command classes to ensure there is something to run so we can at least see if front end is working. Also since the first day, I am tasked to write a legitimate recursive descent parser, guided by the discussed Engine API. By the end of first week, I made almost zero progress as I tried to understand all the online tutorials. With this whole task sharing scheme, though our old model was very sketchy, we didn't panic. The only decision we had to make is whether I should continue working on a "legitimate" parser or work with Arthur to figure out a working solution though it may not be perfect. Our final decision is that Arthur continued to work on writing ad hoc commands while I continued to write the parser. Arthur doesn't need one more person to do his job, and if I succeed, Arthur's work won't go to waste because he can simply transfer his code into my architecture. So we didn't make big design decisions after the first week. It was more a decision of manpower allocation.

###### Describe two design decisions discussed by the team about any part of the program in detail:
* What alternate designs were proposed?
    * We should use the Visitor pattern and write a separate Interpreter class that "interprets" the root Expression node given by Parser. An example would be like this
        ```java
        public interface Expression {}

        public class Binary implements Expression {}

        public class Unary implements Expression {}

        ...

        public class Interpreter {
            public void interpret(Binary binary);

            public void interpret(Unary unary);

            ...
        }

        public class Engine implements EngineAPI {
            Lexer lexer = new CrudeLexer();
            Parser parser = new CrudeParser();
            Interpreter interpreter = new Interpreter();

            /*
            lexer: String -> List<Token>
            parser: List<Token> -> Expression
            Interpreter: Expression -> interpret(Expression)
            */

            // When the interpreter call interpret on an expression, it will call the correct method corresponding to the most concrete type of the Expression node. This implements the Visitor pattern.
        }
        ```
    * We should use reflection in Parser to create specific Command objects to avoid ifelse loops and hard-coded Token types in Expressions. Again an example is in Binary class,
        ```java
        if (myToken.getString().equals("Sum")) {
            return myFirstExpr.evaluate(turtleManager) + mySecondExpr.evaluate(turtleManager);
        } else if (myToken.getString().equals("Difference")) {
            return myFirstExpr.evaluate(turtleManager) - mySecondExpr.evaluate(turtleManager);
        } else if (myToken.getString().equals("Quotient")) {
            if (mySecondExpr.evaluate(turtleManager) == 0) {
                throw new InterpretationException("The denominator in a Quotient operation cannot be zero");
            }
            return myFirstExpr.evaluate(turtleManager) / mySecondExpr.evaluate(turtleManager);
        }
        ```
* Visitor pattern
    * What are the trade-offs (describe the pros and cons of the different designs)?
        * pro: Visitor pattern separates the logic of executing each Expression from the data structure of each expression, which make some sense because interpreting an Expression is not behavior of that object. Interpreter is a behavior driven class.
        * pro: Interpreter class would be consistent with the lexer, parser, interpreter workflow.
        * con: A very lengthy Interpreter class that has one method for each one Expression class. When used with reflection this becomes especially horrendous. We proposed writing a meta script in bash or python that writes these methods in a Java class but feels that is counterproductive to a good development flow.
        * con: I feel the Visitor pattern in this case is just against the Object-Oriented mindset, especially if the classes implementing Expression are reduced to simple data holders. In fact, now the Expression interface has no methods declared. This interface doesn't do anything, which means the classes are indeed data holders.
    * Which would you prefer and why (it does not have to be the one that is currently implemented)?
        * I prefer our current design. It observes the Object-Oriented principle better. Using polymorphism to let the objects themselves decide what interpret method to call is better than lumping everything in an Interpreter class. The reason is that there is no compiler checking for the visitor pattern--I don't know if I have written an interpret method for all the Expression classes yet, or a newly added Expression class. However using the current design, the compiler checks that for me because the interpret method in the interface has to be implemented by the newly added Expression class.
* Reflection or Not
    * What are the trade-offs (describe the pros and cons of the different designs)?
        * pro: No more if else statements to decide what course of action to take for different type of Token.
        * pro: Maximum polymorphism.
        * con: Slow running time.
        * con: Reflection just feels hacky.
        * con: There is not just one reflection method that we have to write, because the constructors of different commands take very different forms. It's likely that we will have to write a different reflective method for each syntactic category.
    * Which would you prefer and why (it does not have to be the one that is currently implemented)?
        * I prefer our current design. The only drawback is that the Token name is hard-coded as Strings. There should be some work around by using maps and enum types.
### Conclusions
Reflect on what you have learned about designing programs through this project, using any code in the project as examples:
###### Describe the best feature of the project's current design and what did you learn from reading or implementing it?
I like the Observer pattern from reading my teammate's code, though my work did not involve any listener or observer. My teammates working on front end receive updates from my code even if I completely redesigned by whole architecture. In the worst case, only one or two lines of code have to be added. For example, when I change CrudeStateMachine to using StateMachineV2, these lines are added,

    ```java
    /**
     * Allow any observers to register as an observer to this StateMachine.
     *
     * @param observer
     */
    @Override
    public void register(StateMachineObserver observer) {
        observers.add(observer);
    }

    /**
     * Push notifications to observers whenever there's change within the StateMachine.
     */
    @Override
    public void pushAlarm() {
        observers.forEach(StateMachineObserver::notifyListener);
    }
    ```


###### Describe the worst feature that remains in the project's current design and what did you learn from reading or implementing it?
The worst design is the CrudeParser. It violates many design principles and I just have no idea how to refactor it.
###### To be a better designer in the next project, what should you
* start doing differently
    * I will have a clear estimate of my capability or call for help earlier instead of relying on luck like this time, though this time I was lucky.
* keep doing the same
    * I will be motivated and willing to spend long hours until I get things done.
* stop doing
    * Not understanding my teammates' work at all until a very late stage.

