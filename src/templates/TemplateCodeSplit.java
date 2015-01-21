/**
 * SPDXVersion: SPDX-1.1
 * Creator: Person: Nuno Brito (nuno.brito@triplecheck.de)
 * Creator: Organization: TripleCheck (contact@triplecheck.de)
 * Created: 2014-08-16T14:11:05Z
 * LicenseName: EUPL-1.1-without-appendix
 * FileName: InterfaceToGrabJavaMethods.java  
 * FileType: SOURCE
 * FileCopyrightText: <text> Copyright 2014 Nuno Brito, TripleCheck </text>
 * FileComment: <text> 
    An interface providing a structure for extracting code portions since this
    is a problematic exercise and different solutions need to be tested.
</text> 
 */

package templates;

import tokenizator.SourceCodeSnippet;
import java.util.ArrayList;

/**
 *
 * @author Nuno Brito, 16th of August 2014 in Darmstadt, Germany
 */
public interface TemplateCodeSplit {
    
    /**
     * Split a source code file on a given language onto relevant
     * portions of code. For example, in C-based languages this would
     * be on the method level. If Delphi, it would be on function/procedure
     * level.
     * @param sourceCode    The source code for a file (or similar)
     * @return An array of snippets or null if something went wrong.
     */
    public ArrayList<SourceCodeSnippet> split(final String sourceCode);
    
}
