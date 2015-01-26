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
import tokenizator.SourceCodeFile;
import tokenizator.SourceCodeSnippet;

/**
 *
 * @author Nuno Brito, 21st of January 2014 in Soufflenheim, France
 * @author Vasiliy Vadimov, Nizhny Novgorod, Russia
 */
public abstract class TemplateCodeConvertToTokens {

    protected String tokensFile = "";
    protected int tokenMinSize = 60;
    
    /**
     * Read the list of tokens to translate from disk. This method is only
     * called when the object is first initialized.
     */
    public abstract void initializeTokens();
    
    /**
     * Converts a piece of code to tokens. On c-based languages, this
     * snippet is equivalent to the text inside a method.
     * @param textSnippet
     * @return null when something wrong happened
     */
    public abstract SourceCodeSnippet ConvertSnippetToTokens(final String textSnippet);
    
        
    public abstract SourceCodeFile convertFile(final File sourceCodeFile);

    
    /**
     * Gets the content from a source code file as input, converts each
     * relevant portion of code to source code snippet objects where each
     * snippet contains the tokenized version of the code.
     * @param sourceCodeText
     * @return 
     */
    public abstract SourceCodeFile convertSourceText(final String sourceCodeText);
    
    
    @SuppressWarnings("OverridableMethodCallInConstructor")
    public TemplateCodeConvertToTokens(){
        // get the tokensListJava read from disk into memory
        initializeTokens();
    }
    
}
