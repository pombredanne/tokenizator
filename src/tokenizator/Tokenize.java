/*
 * SPDXVersion: SPDX-1.1
 * Creator: Person: Nuno Brito (nuno.brito@triplecheck.de)
 * Creator: Organization: TripleCheck (contact@triplecheck.de)
 * Created: 2015-01-21T11:46:10Z
 * LicenseName: EUPL-1.1-without-appendix
 * FileName: Tokenize.java  
 * FileType: SOURCE
 * FileCopyrightText: <text> Copyright 2015 Nuno Brito, TripleCheck </text>
 * FileComment: <text> Main class for the tokenization actions. </text> 
 */
package tokenizator;

import java.io.File;
import tokenizedLanguages.Java.CodeConvertToTokensJava;

/**
 *
 * @author Nuno Brito, 21st of January 2014 in Soufflenheim, France
 */
public class Tokenize {

    public static CodeConvertToTokensJava java = new CodeConvertToTokensJava();
    
    // future implementation
    //public static CodeProcessC c = new CodeProcessC();
    //public static CodeProcessCpp cpp = new CodeProcessCpp();
    //public static CodeProcessCSharp csharp = new CodeProcessCSharp();
    //public static CodeProcessPython python = new CodeProcessPython();
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        
        File fileTest = new File("sample/java/Preferences.java");
        
        // demonstrate the project in functioning
        SourceCodeFile result = Tokenize.java.convertFile(fileTest);
        
        // iterate each of the results, show to screen what we have
        for(SourceCodeSnippet snippet : result.getSnippetsToMatch()){
            System.out.println(snippet.getLineStart() 
                    + ".."
                    + snippet.getLineEnd()
                    + " "
                    + snippet.getTokens());
        }
    }
    
}
