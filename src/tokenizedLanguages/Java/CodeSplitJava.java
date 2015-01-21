/**
 * SPDXVersion: SPDX-1.1
 * Creator: Person: Nuno Brito (nuno.brito@triplecheck.de)
 * Creator: Organization: TripleCheck (contact@triplecheck.de)
 * Created: 2014-08-16T15:03:05Z
 * LicenseName: EUPL-1.1-without-appendix
 * FileName: GrabJavaMethodsUsingRawInterpretation.java  
 * FileType: SOURCE
 * FileCopyrightText: <text> Copyright 2014 Nuno Brito, TripleCheck </text>
 * FileComment: <text> 
 
 Grab the java methods using in-house code to interpret source code. Not ideal
 as using tested libraries would be preferred but ends up working accurately too.
 
 </text> 
 */

package tokenizedLanguages.Java;

import tokenizator.SourceCodeSnippet;
import java.io.File;
import java.util.ArrayList;
import templates.TemplateCodeSplit;
import utils.text;

/**
 *
 * @author Nuno Brito, 16th of August 2014 in Darmstadt, Germany
 */
public class CodeSplitJava implements TemplateCodeSplit {

    
    // settings
    final boolean debug = false;
    static File sourceFile = new File("test/tokenize_samples", "source_android.java");
        
    
    // final definitions
    final String blockCommentStart = " " + "/" + "*";
    
    /**
     * Converts a Java Source code sample into an array of strings where each
     * string contains the source code for each method.
     * @param sourceCode    The source code to process
     * @return              The array with methods or empty if something went wrong
     */
    @Override
    public ArrayList<SourceCodeSnippet> split(final String sourceCode) {
        // prepare the result
        ArrayList<SourceCodeSnippet> methods = new ArrayList();
        
        // add up the line counter
        int lineCounter = 0;
        // the moustache "{ }" counters
        int moustacheTotal = 0;
        
        // iterate each line of the code
        String[] lines = sourceCode.split("\n");
        boolean classNotStarted = true;
        // where we record the data for each method
        String methodText = "";
        int lineStart = -1;
        int lineEnd;
        // are we still inside a block comment?
        boolean isBlockComment = false;
        
        // iterate each line of the source code
        for(String line : lines){
            // count up
            lineCounter++;
           
            // avoid block comments
            if(line.contains("*"+"/")){
                isBlockComment = false;
                int i1 = line.indexOf("*"+"/");
                line = line.substring(i1+2);
            }
            
            if(isBlockComment){
                continue;
            }
            
            if(line.contains(blockCommentStart)){
                isBlockComment = true;
                int i1 = line.indexOf(blockCommentStart);
                line = line.substring(0,i1);
            }
            
            
            // get a trimmed line for verification
            final String trimmedLine = line.trim();
            // check for normal comments
            if(isValidLine(trimmedLine)==false){
                continue;
            }       
            // have we entered the class field?
            if(trimmedLine.startsWith("public class ") 
                    ||(trimmedLine.startsWith("class "))
                    ||(trimmedLine.startsWith("public final class"))
                    ||(trimmedLine.startsWith("public abstract class"))
                    ||(trimmedLine.startsWith("public enum"))
                    
                    ){
                classNotStarted = false;
                // increase the moustache counter
                //moustacheTotal = 1;
                moustacheTotal = countMoustache(line, moustacheTotal);
                // skip to the next line
                continue;
            }
            // skip reading until we find the class declaration
            if(classNotStarted){
                continue;
            }

            // clean up the line from comments
            line = cleanUpLine(line);
            
            // do the moustache count
            int mOpen = text.countMatches("\\{", line);
            int mClosed = text.countMatches("\\}", line);
        
            // do the math
            moustacheTotal += mOpen;
            moustacheTotal -= mClosed;
            
            // are we inside a method block?
            if(moustacheTotal > 1){
                // if we have no line start defined, start counting here
                if(lineStart == -1){
                    lineStart = lineCounter;
                }
                // add up this line of code
                methodText = methodText.concat(line).concat("\n");
                //debug("1->" + line);
            }
            
            // special cases
            if(moustacheTotal == 1){
                // last line of a method, we have a closing moustache
                if(mClosed > 0){
                    // get the last line of code
                    methodText = methodText.concat(line);
                    // split the current line as last line of the method
                    lineEnd = lineCounter;
                    
                    if(lineStart >= lineEnd){
                        System.out.println("ERROR CSJ148 - Invalid Java source code provided as input");
                        System.out.println(methodText);
                        System.out.println("------------");
                        System.out.println(lineStart + ".." + lineEnd);
                        System.out.println(sourceCode);
                        return null;
                    }
                    
                    // create the Java Method object
                    SourceCodeSnippet method = new SourceCodeSnippet();
                    // fill up the needed results
                    method.setLineStart(lineStart);
                    method.setLineEnd(lineEnd);
                    method.setText(methodText);
                    
                    // add the method to our list
                    methods.add(method);
//                    debug("2->" 
//                            + line
//                            +"\n"
//                            + "------------");
                    // clean up the variables
                    methodText = "";
                    lineStart = -1;
                    continue;
                }

                // text in limbo, presumed as part of the next method
                methodText = methodText.concat(line).concat("\n");
                // if we have no line start defined, start counting here
                if(lineStart == -1){
                    lineStart = lineCounter;
                }
//                debug("3->" + line);
            }
        }
        
        // a valid source code file ends with 0 open brackets
        if(moustacheTotal != 0){
            // it wasn't zero, something is wrong so we provide an empty array
            methods.clear();
        }
        
        // final moustache count needs to be 0
//        debug("Moustache count: " + moustacheTotal);
        
        // provide the result separated as an array
       return methods;
    }

    /**
     * Counts the number of parenthesis inside a line
     * @param line              The line to process
     * @param moustacheTotal    The current number of found parenthesis
     * @return                  The up to date count of parenthesis
     */
    private int countMoustache(final String line, final int moustacheTotal){
        // do the moustache count
        int mOpen = text.countMatches("\\{", line);
        int mClosed = text.countMatches("\\}", line);

        int result = moustacheTotal;
        // do the math
        result += mOpen;
        result -= mClosed;

        return result;
    }
    
    /**
     * Checks if a line contains valid code sequences or not
     * @param line  The line to process
     * @return      True if we can go ahead, false otherwise
     */
    private boolean isValidLine(final String line){
     
     if(line.isEmpty()){
         return false;
     }else   
     if(line.startsWith("//")){
         return false;
     }else
          
     if(line.startsWith("/*")){
         return false;
     }else

     if(line.startsWith("*")){
         return false;
     }
    //TODO still not handling code inside the /** declaration block
     return true;
    }
    
    

    /**
     * Ensures that we don't have commented code which might cause
     * trouble when processing the moustaches
     * @param line  The line to process
     * @return      The line without comments
     */
    private String cleanUpLine(final String line) {
        /**
         * Note: Intentionally is neglected the case when we are looking
         * for something that contains an // inside a functional code portion
         * such as the indexOf usage on this method. It was considered that
         * the frequency of such cases is remote, not relevant enough to
         * justify worsening the processing performance by adding an extra
         * IF comparison cycle per line processed.
         */
        // get the position of the last //
        int i1 = line.indexOf("//");
        if(i1 == -1){
            // return the full line
            return line;
        }
        
        // do we have a comment within a string portion?
        final String stringTest = line.substring(0, i1);
        int i2 = text.countMatches("\"", stringTest);
        // is the number odd?
        if((i2 % 2)!=0){
            // return the full string because the quotes are open
            return line;
        }
        
        // return the line without the comment position
        return line.substring(0, i1-1);
    }
    
    
//    /**
//     * Test if our code is working or not.
//     * @param args 
//     */
//    public static void main(String[] args) {
//        // test if our code is working as intended
//        CodeSplitJava scan = new CodeSplitJava();
//        // define a file to read source code and use as test
//        if(sourceFile.exists() == false){
//            System.err.println("Didn't found: " + sourceFile.getAbsolutePath());
//            return;
//        }
//        String sourceCode = files.readAsString(sourceFile);
////        System.out.println(sourceCode);
//        // split the methods
//        ArrayList<SourceCodeSnippet> methods = scan.split(sourceCode);
//        // iterate methods
//        for(SourceCodeSnippet method : methods){
//            // print them to screen
//            System.out.println(method.getLineStart() + ".." + method.getLineEnd());
//            System.out.println(method.getText()
//                    + "\n------------------");
//        }
//    }
    
    
}
