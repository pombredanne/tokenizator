/*
 * SPDXVersion: SPDX-1.1
 * Creator: Person: Nuno Brito (nuno.brito@triplecheck.de)
 * Creator: Organization: TripleCheck (contact@triplecheck.de)
 * Created: 2014-08-10T12:34:23Z
 * LicenseName: EUPL-1.1-without-appendix
 * FileName: SourceCodeSnippet.java  
 * FileType: SOURCE
 * FileCopyrightText: <text> Copyright 2014 Nuno Brito, TripleCheck </text>
 * FileComment: <text> 
        The structure where we can place the results from tokenized code.
    </text> 
 */

package tokenizator;

import java.util.ArrayList;

/**
 *
 * @author Nuno Brito, 10th of August 2014 in Darmstadt, Germany
 */
public class SourceCodeSnippet {
    
    // inside a source code file, where does it start? where it ends? (lines)
    int lineStart, lineEnd;
    
    private int 
            // max accepted number of matches per snippet
            matchesLimit = 5;

    private String  
            // the original text or code
            text,
            // tokenized version of the snippet
            tokens,
            // what is the identifierHash for this method? (SHA1, ..)
            identifierHash,
            // if available, where can we find this text around the web?
            provenance;
    
    // did we found anything matching
    private final ArrayList<SourceCodeSnippet> matches = new ArrayList();
    
    public int getLineStart() {
        return lineStart;
    }

    @Override
    public String toString(){
        return text;
    }
    
    public void setLineStart(int lineStart) {
        this.lineStart = lineStart;
    }

    public int getLineEnd() {
        return lineEnd;
    }

    public void setLineEnd(int lineEnd) {
        this.lineEnd = lineEnd;
    }

    /**
     * The lines where the method is located
     * @return 
     */
    public String getLines() {
        return lineStart + ".." + lineEnd;
    }

   
    /**
     * Associates a given match with this snippet
     * @param snippet The matched snippet to be added
     */
    public void addMatch(final SourceCodeSnippet snippet){
        // check that we don't add too many matches per snippet
        if(matches.size() < matchesLimit){
            // add it up
            matches.add(snippet);
        }
    }
    

    public String getSnippet() {
        return text;
    }

    public String getIdentifierHash() {
        return identifierHash;
    }

    public void setIdentifierHash(final String identifier) {
        this.identifierHash = identifier;
    }

    public String getProvenance() {
        return provenance;
    }

    public void setProvenance(final String repository) {
        this.provenance = repository;
    }

    public String getText() {
        return text;
    }

    public void setText(final String text) {
        this.text = text;
    }

    public String getTokens() {
        return tokens;
    }

    public void setTokens(final String token) {
        this.tokens = token;
    }

    /**
     * Did this snippet had at least one match in the past?
     * @return True if there was a match, false otherwise
     */
    public boolean wasMatchedBefore() {
        return matches.size() > 0;
    }

    public void setMatchesLimit(final int matchesLimit) {
        this.matchesLimit = matchesLimit;
    }

    public ArrayList<SourceCodeSnippet> getMatches() {
        return matches;
    }
    
    /**
     * Has this snippet reached its limit in terms of matches
     * @return True if that is the case, false if we still have space for more
     */
    public boolean hasReachedMaxMatches(){
        return matches.size() >= matchesLimit;
    }

    
}
