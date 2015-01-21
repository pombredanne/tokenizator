/*
 * SPDXVersion: SPDX-1.1
 * Creator: Person: Nuno Brito (nuno.brito@triplecheck.de)
 * Creator: Organization: TripleCheck (contact@triplecheck.de)
 * Created: 2015-01-21T18:50:10Z
 * LicenseName: EUPL-1.1-without-appendix
 * FileName: Tokenize.java  
 * FileType: SOURCE
 * FileCopyrightText: <text> Copyright 2015 Nuno Brito, TripleCheck </text>
 * FileComment: <text> Test the tokenization of code. </text> 
 */

import java.io.File;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import tokenizator.SourceCodeFile;
import tokenizator.SourceCodeSnippet;
import tokenizator.Tokenize;

/**
 *
 * @author Nuno Brito, 21st of January 2014 in Soufflenheim, France
 */
public class JavaCodeTest {
    
    public JavaCodeTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    @Test
     public void testJava() {
     
         System.out.println("Testing Java tokenization");
         // define the file that we use for our test
         File fileTest = new File("sample/java/Preferences.java");
         
         System.out.println("File: " + fileTest.getAbsolutePath());
        
        // get the tokenized result
        SourceCodeFile result = Tokenize.java.convertFile(fileTest);
        
        // we expect exaclty 22 methods to have been detected
        assertEquals(result.getSnippetsToMatch().size(), 22);
                
        // iterate each of the results, show to screen what we have
        for(SourceCodeSnippet snippet : result.getSnippetsToMatch()){
            System.out.println(snippet.getLineStart() 
                    + ".."
                    + snippet.getLineEnd()
                    + " "
                    + snippet.getTokens());
        }
        
        System.out.println("Number of methods found: " + result.getSnippetsToMatch().size());
        
     }
}
