/*
 * SPDXVersion: SPDX-1.1
 * Creator: Person: Nuno Brito (nuno.brito@triplecheck.de)
 * Creator: Organization: TripleCheck (contact@triplecheck.de)
 * Created: 2014-10-26T13:00:00Z
 * LicenseName: EUPL-1.1-without-appendix
 * FileName: CodeConvertToTokensJava.java  
 * FileType: SOURCE
 * FileCopyrightText: <text> Copyright 2014 Nuno Brito, TripleCheck </text>
 * FileComment: <text> Miscleaneous functions to convert code from one
    shape to another. For example, normal source code snippets to tokenized
    content. See description on the header of each function for details.</text> 
 */

package tokenizedLanguages.Java;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import templates.TemplateCodeConvertToTokens;
import tokenizator.SourceCodeFile;
import tokenizator.SourceCodeSnippet;
import utils.files;
import utils.regex;
import utils.text;

/**
 *
 * @author Nuno Brito, 26th of October 2014 in Tettnang, Germany
 */
public class CodeConvertToTokensJava extends TemplateCodeConvertToTokens{

    
    // definitions that can be changed
    final private String javaTokensFile = "tokens-java.txt";
    final private int tokenMinSize = 60;
    
    
    // variables used inside the code
    private String JavaTokenContent;
    // where we store our tokensListJava
    private ArrayList<String[]> tokensListJava;
    
    
    /**
     * Reads the file with Java tokensListJava from disk. This file contains the definitions
     * that we will use for translating the normal source code onto the tokenized
     * form.
     */
    @Override
    public void initializeTokens() {
        // initialize our java tokensListJava file
        final File tokenFile = new File(javaTokensFile);
        // intialize the array list
        tokensListJava= new ArrayList();
        
        // does this token file exists somewhere?
        if(tokenFile.exists()){
            // read the file from disk
            JavaTokenContent = files.readAsString(tokenFile);
        } else{
            // use a version that we included on this source code file
            JavaTokenContent = defaultTokenContentJava;
        }
        
        // create an array with the lines
        final String[] lines = JavaTokenContent.split("\n");
        // iterate each line
        for(final String line : lines){
            // avoid comment lines
            if(line.startsWith("//")){
                continue;
            }
            // break up the line into an array, using a space as separator
            final String[] token = line.split(" ");
            // if the array specifies a token use it, otherwise keep it unchanged
            if(token.length > 1){
                // add the new token to be replaced
                String[] item = new String[]{token[0], token[1]};
                tokensListJava.add(item);
            }
        }
        
    }
   
    @Override
    public SourceCodeFile convertFile(final File sourceCodeFile) {
         // read the file from disk
        final String sourceCode = utils.files.readAsString(sourceCodeFile);
        // convert the source code text to our object
        SourceCodeFile result = convertSourceText(sourceCode);
        // was the result valid?
        if(result == null){
            return null;
        }
        // specify the file inside the source code file
        result.setFile(sourceCodeFile);
        // all done
        return result;
    }

    /**
     * Splits the source code of a given file into different methods and
     * then converts each method to a tokenized method that we use for our
     * comparisons
     * @param sourceCodeText    The source code to convert
     * @return              An array with the tokenized source code methods
     */    
    @Override
    public SourceCodeFile convertSourceText(final String sourceCodeText) {
        // don't process interface classes
        if(sourceCodeText.contains("public interface ")){
            return null;
        }
          
        // initializate our method that breaks methods into an array
        CodeSplitJava methodSplitter = 
                new CodeSplitJava();

        // get a list of all the methods
        //TODO this causes another loop, we can get rid of this ArrayList
        ArrayList<SourceCodeSnippet> snippets = methodSplitter.split(sourceCodeText);
        
        // no point in continuing when this is null
        if(snippets == null || snippets.isEmpty()){
            return null;
        }
      
        // get the iterator model
        Iterator<SourceCodeSnippet> iterator = snippets.listIterator();
        // run through each item
        while(iterator.hasNext()){
            // get the next item
            SourceCodeSnippet snippet = iterator.next();
            // convert the code to tokens
            final SourceCodeSnippet  tokenizedCode = ConvertSnippetToTokens(snippet.getText());
            // add up the new token data
            snippet.setTokens(tokenizedCode.getTokens());
        }
        
        // no point in continuing when this is null
        if(snippets.isEmpty()){
            return null;
        }
        // add this file to our list of processed files
        SourceCodeFile sourceCodeFile = new SourceCodeFile();
        sourceCodeFile.setSnippets(snippets);
        // now add it up to the main list
        return sourceCodeFile;
    }
    
    
    /**
     * Convert a piece of Java source code into a form represented by tokens.
     * @param textSnippet   A snippet of Java source code (preferably a methods)
     * @return A token string for the given piece of code
     */
    @Override
    public SourceCodeSnippet ConvertSnippetToTokens(final String textSnippet){
        // the variable where we store the results
        String result = "";
        // the lines for this method            
        String[] lines = textSnippet.split("\n");
        // now iterate each line
        for(final String line : lines){
            // get the source code line converted to tokens
            final String tokenizedLine = processLine(line);
            // if the result was null, continue to the next line
            if(tokenizedLine == null){
                continue;
            }
            // get the lines together
            result = result.concat(tokenizedLine);
        }
        // avoid empty results
        if(result.isEmpty()){
            return null;
        }
        // remove the empty spaces
        result = result.replaceAll(" ", "");
        
        // add this information inside an object
        final SourceCodeSnippet snippet = new SourceCodeSnippet();
        // add the original code and the transformed code
        // TODO, redundant creation of two snippet objects, needs to be improved
        snippet.setText(textSnippet);
        snippet.setTokens(result);
        // all done
        return snippet;
    }
    
    
    
    
    /**
     * Process a line of source code and return the tokenized result
     * @param line  The line to be processed
     * @return      A token string or null if not possible to process
     */
    private String processLine(String line) {
        // remove the empty lines
        final String trimmedLine = line.trim();
        // no need to continue if result was empty after this operation
        if(trimmedLine.isEmpty()){
            return null;
        }

        // part of a comment, no need to continue
        if(trimmedLine.startsWith("*") || trimmedLine.startsWith("/")){
            return null;
        }

        // remove comments mixed code on the same line
        int commentPosition = line.indexOf("//");
        if(commentPosition > -1){
            line = line.substring(0, commentPosition-1);
        }
        // remove the leading and trailing white spaces
        // we add a leading space to solve issues identifying keywords on the 0 position 
        line = " " + text.removeLeadingAndTrailingSpaces(line);

        // replace all the text within quotes with a keyword
        line = regex.replaceQuotesWithKeyword(line);

        // replace the variables with a defined keyword
        line = regex.replaceVariablesWithKeyword(line);

        // replace all the methods with a keyword
        line = regex.replaceMethodsWithKeyword(line);

        // replace the known tokens        
        for(String[] token : tokensListJava){
            line = regex.replaceWithKeyword(token[0], "º"+token[1], line);
        }

        // remove the white spaces
        line = regex.removeWhiteSpaces(line);
        // convert the token separator
        line = line.replaceAll("º", " ");
        // all done
        return line;
    }
    
    
    // what we use as default to convert java code to tokenized form
    final String defaultTokenContentJava = 
            "// Tokens related to Java language up to version 8\n" +
            "// to remember, replacements occur by order of listing\n" +
            "// based on http://docs.oracle.com/javase/specs/jls/se8/html"
            + "/jls-3.html#jls-3.9\n" +
            "//\n" +
            "// Known classes\n" +
            "String S\n" +
            "File FILE\n" +
            "// known keywords (and reserved values)\n" +
            "abstract AB\n" +
            "assert AS\n" +
            "byte BY\n" +
            "boolean BO\n" +
            "break BR\n" +
            "catch CA\n" +
            "case CS\n" +
            "char CH\n" +
            "const CO\n" +
            "continue CN  \n" +
            "class CL\n" +
            "default DE\n" +
            "double DB\n" +
            "do DO\n" +
            "enum EN\n" +
            "else EL\n" +
            "extends EX\n" +
            "float FL\n" +
            "final FL\n" +
            "finally FY\n" +
            "for FO\n" +
            "goto GO\n" +
            "if IF\n" +
            "implements IM\n" +
            "import IP\n" +
            "int I\n" +
            "interface IC\n" +
            "instanceof IS\n" +
            "long L\n" +
            "native NA\n" +
            "new N\n" +
            "package PA\n" +
            "protected PR\n" +
            "private PI\n" +
            "public P\n" +
            "return R\n" +
            "synchronized SY\n" +
            "switch S\n" +
            "this T\n" +
            "throw TH\n" +
            "throws TS\n" +
            "transient TT\n" +
            "try TR\n" +
            "short SH\n" +
            "strictfp SP\n" +
            "super SU\n" +
            "static ST\n" +
            "void VD\n" +
            "volatile VE\n" +
            "while W\n" +
            "// BooleanLiterals:\n" +
            "true TE\n" +
            "false F\n" +
            "null NU\n" +
            "// Separators (punctuators):\n" +
            "(\n" +
            ")\n" +
            "{\n" +
            "}\n" +
            "[\n" +
            "]\n" +
            ";\n" +
            ",\n" +
            "...\n" +
            ".\n" +
            "@\n" +
            "::\n" +
            "// Operators\n" +
            "->\n" +
            "==\n" +
            ">=\n" +
            "<=\n" +
            "!=\n" +
            "&&\n" +
            "||\n" +
            "++\n" +
            "--\n" +
            "+=\n" +
            "-=\n" +
            "*=\n" +
            "/=\n" +
            "&=\n" +
            "|=\n" +
            "^=\n" +
            "%=\n" +
            "<<=\n" +
            ">>=\n" +
            ">>>=\n" +
            "+\n" +
            "-\n" +
            "*\n" +
            "/\n" +
            "&\n" +
            "|\n" +
            "^\n" +
            "%\n" +
            "<<\n" +
            ">>>\n" +
            ">>\n" +
            "=\n" +
            ">\n" +
            "<\n" +
            "!\n" +
            "~\n" +
            "?\n" +
            ":";

}
