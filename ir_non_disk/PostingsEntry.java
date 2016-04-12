/*  
 *   This file is part of the computer assignment for the
 *   Information Retrieval course at KTH.
 * 
 *   First version:  Johan Boye, 2010
 *   Second version: Johan Boye, 2012
 */  

package ir;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.ListIterator;

public class PostingsEntry implements Comparable<PostingsEntry>, Serializable {
    
    public int docID;
    public double score;
    private LinkedList<Integer> plist = new LinkedList<Integer>();

    public PostingsEntry(int docID){
        this.docID=docID;
    }
    
    public PostingsEntry(int docID, int offset){
        this.docID=docID;
        plist.add(offset);
    }
    
    public void addoffset(int offset){
        plist.add(offset);
        
    }
    
    
    
  
    //public ListIterator<Integer> getoffsets(){
    //    return  plist.listIterator(0);
    public LinkedList<Integer> getoffsets(){
        return  plist;
    }
    
    /**
     *  PostingsEntries are compared by their score (only relevant 
     *  in ranked retrieval).
     *
     *  The comparison is defined so that entries will be put in 
     *  descending order.
     */
    public int compareTo( PostingsEntry other ) {
	return Double.compare( other.score, score );
    }

    //
    //  YOUR CODE HERE
    //
    
     public int Returndocid(){
        return docID;
    }
    
}

    
