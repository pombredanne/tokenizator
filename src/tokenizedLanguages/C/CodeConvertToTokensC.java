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

package tokenizedLanguages.C;

import templates.TemplateCodeConvertToTokens;
import tokenizator.SourceCodeFile;
import tokenizedLanguages.C.CodeSplitC;
import utils.regex;
import utils.text;


/**
 *
 * @author Vasiliy Vadimov, Nizhny Novgorod, Russia
 */
public final class CodeConvertToTokensC extends TemplateCodeConvertToTokens {
    /**
     * Reads the file with Java tokensListfrom disk. This file contains the definitions
     * that we will use for translating the normal source code onto the tokenized
     * form.
     */
    @Override
    public void initializeTokens() {
        super.initializeTokens();
        // create an array with the lines
        final String[] lines = tokenContent.split("\n");
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
                tokensList.add(item);
            }
        }
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
        // initializate our method that breaks methods into an array
        methodSplitter = new CodeSplitC();
        // use parent method for convertSourceText
        return super.convertSourceText(sourceCodeText);
    }
    
    
    /**
     * Process a line of source code and return the tokenized result
     * @param line  The line to be processed
     * @return      A token string or null if not possible to process
     */
    @Override
    protected String processLine(String line) {
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
        for(String[] token : tokensList){
            line = regex.replaceWithKeyword(token[0], "º"+token[1], line);
        }

        // remove the white spaces
        line = regex.removeWhiteSpaces(line);
        // convert the token separator
        line = line.replaceAll("º", " ");
        // all done
        return line;
    }
    
    public CodeConvertToTokensC() {
        // what we use as default to convert java code to tokenized form
        defaultTokenContent = 
            "// Tokens related to C language up to version C11\n" +
            "// to remember, replacements occur by order of listing\n" +
            "// based on http://en.cppreference.com/w/c/keyword"
            + "/jls-3.html#jls-3.9\n" +
            "//\n" +
            "FILE FILE\n" +
            "// known keywords (and reserved values)\n" +
            "auto AU\n" +
            "break BR\n" +
            "case CS\n" +
            "char CH\n" +
            "const CO\n" +
            "continue CN\n" +
            "default DE\n" +
            "do DO\n" +
            "double DB\n" +
            "else EL\n" +
            "enum EN\n" +
            "extern EX\n" +
            "float FL\n" +
            "for FO\n" +
            "goto GO\n" +
            "if IF\n" +
            "inline IL//(since C99)\n" +
            "int I\n" +
            "long L\n" +
            "register RG\n" +
            "restrict RS//(since C99)\n" +
            "return R\n" +
            "short SH\n" +
            "signed SI\n" +
            "sizeof SO\n" +
            "static ST\n" +
            "struct SR\n" +
            "switch S\n" + 
            "typedef TD\n" +
            "union UN\n" +
            "unsigned US\n" + 
            "void VD\n" +
            "volatile VO\n" +
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
            ">?\n" +
            ":";
        tokensFile = "tokens-c.txt";
        tokenMinSize = 60;
        this.initializeTokens();
    }
}
