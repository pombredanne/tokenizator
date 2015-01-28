/*
 * SPDXVersion: SPDX-1.1
 * Creator: Person: Nuno Brito (nuno.brito@triplecheck.de)
 * Creator: Organization: TripleCheck (contact@triplecheck.de)
 * Created: 2015-01-21T19:15:10Z
 * LicenseName: EUPL-1.1-without-appendix
 * FileName: TemplateCodeConvertToTokens.java  
 * FileType: SOURCE
 * FileCopyrightText: <text> Copyright 2015 Nuno Brito, TripleCheck </text>
 * FileComment: <text> Template for tokenizing source code. </text> 
 */

package templates;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import tokenizator.SourceCodeFile;
import tokenizator.SourceCodeSnippet;
import utils.files;

/**
 *
 * @author Nuno Brito, 21st of January 2014 in Soufflenheim, France
 * @author Vasiliy Vadimov, Nizhny Novgorod, Russia
 */
public abstract class TemplateCodeConvertToTokens {

    // definitions that can be changed
    protected int tokenMinSize = 60;
    
    
    // variables used internally
    protected String tokensFile;
    // variables used inside the code
    protected String tokenContent;
    // where we store our tokensListJava
    protected ArrayList<String[]> tokensList; 
    
    protected String defaultTokenContent;
    
    protected TemplateCodeSplit methodSplitter;
    
    /**
     * Read the list of tokens to translate from disk. This method is only
     * called when the object is first initialized.
     */
    public void initializeTokens() {
        // initialize our java tokensListJava file
        final File tokenFile = new File(tokensFile);
        // intialize the array list
        tokensList= new ArrayList();
        
        // does this token file exists somewhere?
        if(tokenFile.exists()){
            // read the file from disk
            tokenContent = files.readAsString(tokenFile);
        } else{
            // use a version that we included on this source code file
            tokenContent = defaultTokenContent;
        }    
    }
    
    /**
     * Converts a piece of code to tokens. On c-based languages, this
     * snippet is equivalent to the text inside a method.
     * @param textSnippet
     * @return null when something wrong happened
     */
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
        // remove the empty spaces
        result = result.replaceAll(" ", "");
        
        // avoid empty results
        if(result.isEmpty()){
            return null;
        }
        
        // avoid short tokenized methods because of too many false positives
        if(result.length() < tokenMinSize){
            return null;
        }
        
        // add this information inside an object
        final SourceCodeSnippet snippet = new SourceCodeSnippet();
        // add the original code and the transformed code
        // TODO, redundant creation of two snippet objects, needs to be improved
        snippet.setText(textSnippet);
        snippet.setTokens(result);
        // all done
        return snippet;
    }
    
    
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
     * Gets the content from a source code file as input, converts each
     * relevant portion of code to source code snippet objects where each
     * snippet contains the tokenized version of the code.
     * @param sourceCodeText
     * @return 
     */
    public SourceCodeFile convertSourceText(final String sourceCodeText) {
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
            // avoid null results
            if(tokenizedCode == null){
                iterator.remove();
                continue;
            }

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
     * Process a line of source code and return the tokenized result
     * @param line  The line to be processed
     * @return      A token string or null if not possible to process
     */
    protected abstract String processLine(String line);
    
    /*@SuppressWarnings("OverridableMethodCallInConstructor")
    public TemplateCodeConvertToTokens(){
        // get the tokensListJava read from disk into memory
        initializeTokens();
    }*/
    
}
