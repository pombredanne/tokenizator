/*
 * SPDXVersion: SPDX-1.1
 * Creator: Person: Nuno Brito (nuno.brito@triplecheck.de)
 * Creator: Organization: TripleCheck (contact@triplecheck.de)
 * Created: 2014-10-26T17:31:14Z
 * LicenseName: EUPL-1.1-without-appendix
 * FileName: SourceCodeFile.java  
 * FileCopyrightText: <text> Copyright 2014 Nuno Brito, TripleCheck </text>
 * FileComment: <text> 
        This structure represents a source code file and respective code snippets.
        The code is intended to be independent from the programming language
        under which the tokenized code was written.
    </text> 
 */

package tokenizator;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;


/**
 *
 * @author Nuno Brito, 26th of October 2014 in Tettnang, Germany
 */
public class SourceCodeFile implements Comparable<SourceCodeFile>{
    
    // is this a file on our disk?
    private File file;
    
    // where we place the information about the snippetsMatched inside this source code file
    private ArrayList<SourceCodeSnippet> 
            snippetsToMatch = new ArrayList();    

    private final ArrayList<SourceCodeSnippet> 
            snippetsMatched = new ArrayList();
    
    private int
            // the number of snippetsMatched that were not yet matched (original code)
            matchesYetToFind;
    
    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    /**
     * Has this source code file ran out of original code?
     * @return True if all code was matched against the knowledge base,
     * or return false otherwise
     */
    public boolean hasMaxedMatches() {
        return snippetsToMatch.isEmpty();
    }

    /**
     * Set the snippetsMatched that compose this source code
     * @param snippets 
     */
    public void setSnippets(final ArrayList<SourceCodeSnippet> snippets) {
        //this.snippetsMatched = snippetsMatched;
        this.snippetsToMatch = snippets;
    }

    /**
     * Which snippetsMatched do we make available for match-making?
     * @return An array with the available snippetsMatched to which no corresponding
 match was found yet.
     */
    public ArrayList<SourceCodeSnippet> getSnippetsToMatch() {
        return snippetsToMatch;
    }

    
    /**
     * Provides the number of separate code blocks that were found on this
     * source code file
     * @return A number above zero if anything was found
     */
    public int getNumberOfSnippetsMatched(){
        return snippetsMatched.size();
    }
    
    public int getNumberOfSnippetsToMatch(){
        return snippetsToMatch.size();
    }

    public int getMatchesYetToFind() {
        return matchesYetToFind;
    }
    
    /**
     * Goes through each snippet and verifies if we are maxed out or not
     */
    public void doMatchingMath() {
        matchesYetToFind = 0;
        
        // do an iteration over our list
        final Iterator<SourceCodeSnippet> iterator = snippetsToMatch.iterator();
        
        while(iterator.hasNext()){
            // get the next item
            final SourceCodeSnippet snippet = iterator.next();
            // is full of matches?
            if(snippet.hasReachedMaxMatches()){
                // add to the matched list
                snippetsMatched.add(snippet);
                // remove this from the old list
                iterator.remove();
                // move to the next item
                continue;
            }
                        
            // was this snippet found (at least one) elsewhere on the web?
            if(snippet.wasMatchedBefore()){
                // not original code, move to the next snippet
                continue;
            }
            // increase our counter for original pieces of work
            matchesYetToFind++;
        }
    }

    public ArrayList<SourceCodeSnippet> getSnippetsMatched() {
        return snippetsMatched;
    }

    /**
     * How original is this source code file? Based on the snippets being
     * processed, give a percentage of original vs non-original code.
     * @return A percentage indicating how original this source code is.
     */
    public int getPercentageOriginal() {
        int totalSnippets = getNumberOfSnippetsMatched() + getNumberOfSnippetsToMatch();
        return (getNumberOfSnippetsToMatch() * 100) / totalSnippets;
    }
    
    /**
     * How non-unique is this source code file? Based on the snippets being
     * processed, give a percentage of original vs non-original code.
     * @return A percentage indicating how original this source code is.
     */
    public int getPercentageNonOriginal() {
        int totalSnippets = getNumberOfSnippetsMatched() + getNumberOfSnippetsToMatch();
        return (getNumberOfSnippetsMatched() * 100) / totalSnippets;
    }

    @Override
    public int compareTo(final SourceCodeFile o) {
        return o.getPercentageNonOriginal() - this.getPercentageNonOriginal();
    }

    
}
