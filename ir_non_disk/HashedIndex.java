/*
 *   This file is part of the computer assignment for the
 *   Information Retrieval course at KTH.
 *
 *   First version:  Johan Boye, 2010
 *   Second version: Johan Boye, 2012
 *   Additions: Hedvig Kjellström, 2012-14
 */


package ir;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;


/**
 *   Implements an inverted index as a Hashtable from words to PostingsLists.
 */
public class HashedIndex implements Index {

    /** The index as a hashtable. */
    private HashMap<String,PostingsList> index = new HashMap<String,PostingsList>();



    /**
     *  Inserts this token in the index.
     */
    public void insert( String token, int docID, int offset ) {
	//
	//  YOUR CODE HERE
	//

        if (index.containsKey(token)==false) {
    		index.put(token, new PostingsList());
    		//System.out.println("New token********"+token);
    	}
    	index.get(token).addListEntry(docID, offset);
    	//System.out.println("DocID********"+docID+"OFFset**********"+offset);
    }


    /**
     *  Returns all the words in the index.
     */
    public Iterator<String> getDictionary() {
	//
	//  REPLACE THE STATEMENT BELOW WITH YOUR CODE
	//
	//return null;
         return index.keySet().iterator();
    }


    /**
     *  Returns the postings for a specific term, or null
     *  if the term is not in the index.
     */
    public PostingsList getPostings( String token ) {
	//
	//  REPLACE THE STATEMENT BELOW WITH YOUR CODE
	//
	//return null;
        //if (index.containsKey(token)){
            return index.get(token);
        //}
        //return null;
    }


    /**
     *  Searches the index for postings matching the query.
     */

  public PostingsList search( Query query, int queryType, int rankingType, int structureType ) {
	//
	//  REPLACE THE STATEMENT BELOW WITH YOUR CODE
	//
	//return null;
        //LinkedList<String> assorted = new LinkedList<>();
        //  assorted=Genascterms(query);
     //   switch (queryType) {
     // case 0:
     LinkedList<String> assorted = new LinkedList<>();
          assorted=Genascterms(query);
     if(queryType==Index.INTERSECTION_QUERY)
		{
        return intersectSearch(assorted);}
     else if(queryType==Index.PHRASE_QUERY)
     {
      return phraseSearch(query);}
     else{
        return intersectSearch(assorted);}
     }

//private PostingsList intersectSearch(Query q) {
private LinkedList<String> Genascterms(Query q) {
    LinkedList<String> terms = q.copy().terms;
    int finalsize = terms.size();
    System.out.println("Searching for " + terms.peekFirst() + " among " + index.size());
    LinkedList<String> ascterms = new LinkedList<String>();
    LinkedList<String> temp = new LinkedList<String>();
    //temp.add("lalal");
    //System.out.println("size "+temp.size());

    while(ascterms.size()!=finalsize){
		System.out.println("enter asc while\n");
        String minterm = terms.peekFirst();
        PostingsList l1 = getPostings(terms.removeFirst());
        System.out.println("minterm " +minterm);
        String tempterm;
	  //PostingsList l1 = getPostings(terms.removeFirst());

        int min = l1.size();
        while (!terms.isEmpty()) {
			System.out.println("enter TERMS while\n");
      //  System.out.println("Searching for " + terms.peekFirst() + " among " + index.size());
     // list = intersect(list.getListEntires(), getPostings(terms.removeFirst()).getListEntires());
            tempterm = terms.peekFirst();
            PostingsList temppl = getPostings(terms.removeFirst());
            if(min > temppl.size()){
                temp.add(minterm);
                minterm = tempterm;
                min = temppl.size();
            }
            else{
                temp.add(tempterm);
            }
        }
        ascterms.add(minterm);

        System.out.println("size "+temp.size());
        if (!temp.isEmpty()) {
			minterm = temp.peekFirst();
			PostingsList l2 = getPostings(temp.removeFirst());
			//l1 = getPostings(temp.removeFirst());
			min = l2.size();
			while (!temp.isEmpty()) {
				tempterm = temp.peekFirst();
				PostingsList temppl2 = getPostings(temp.removeFirst());
				if(min > temppl2.size()){
					terms.add(minterm);
					minterm = tempterm;
					min = temppl2.size();
				}
				else{
					terms.add(tempterm);
				}
			}
			ascterms.add(minterm);
		}
	}
        /*PostingsList ll1 = getPostings(ascterms.removeFirst());
	if(ascterms.size()>1){

            PostingsList ll2 = getPostings(ascterms.removeFirst());
            if(ll1.size()>ll2.size()){
             ll1 = intersect(ll1, ll2 );
            }
            else{
                ll1 = intersect(ll2,ll1);
            }

    //System.out.println("min term "+minterm);

 }*/
        return ascterms;
}

private PostingsList intersectSearch(LinkedList<String> terms) {
	//for (int i=0;i<=terms.size();i++){
	//	System.out.println(terms.get(i));
	//


    PostingsList list = getPostings(terms.removeFirst());

    while (!terms.isEmpty()) {

      list = intersect(list, getPostings(terms.removeFirst()));
    }
    return list;
  }

//****NEW
private PostingsList phraseSearch(Query q) {
	//System.out.println("here 1");
	System.out.println("size "+index.size());
	Iterator<String> d1 = getDictionary();
	Iterator<String> d1copy = getDictionary();

  /* while(d1.hasNext()){
	  // int ik=0;
      //System.out.println(" "+d1.toString());
      // System.out.println(" "+d1.next());
       PostingsList temp= new PostingsList();
       temp = getPostings(d1.next());
       for(int xtemp=0;xtemp!=temp.size();xtemp++){
				System.out.print(" "+d1copy.next()+"   offsets are \t"+temp.get(xtemp).getoffsets());
				System.out.println("");
			}

   }*/

	//System.out.println("q zize"+q.size());
    LinkedList<String> terms = q.copy().terms;
    PostingsList list = getPostings(terms.removeFirst());
	System.out.println("terms zize"+terms.size());
    while (!terms.isEmpty()) {
		System.out.println("calling func phrasesear");
      list = phrasequery(list, getPostings(terms.removeFirst()));
    }
    return list;
  }


private PostingsList intersect(PostingsList p1, PostingsList p2) {
    PostingsList answer = new PostingsList();

     int i=0;
     int j=0;
     System.out.println("p1 zize"+p1.size());
     System.out.println("p1 zize"+p2.size());
     while(i<p1.size() && j<p2.size()){
         if((p1.get(i).docID) == (p2.get(j).docID)){
             answer.addListEntry(p1.get(i).docID);
             System.out.println("doc id"+p1.get(i).docID);
             i++;
             j++;
         }
         else if(p1.get(i).docID<p2.get(j).docID){
             i++;
         }
         else{
             j++;
         }
     }
     return answer;
 }

private PostingsList phrasequery(PostingsList p1, PostingsList p2) {
	  PostingsList answer = new PostingsList();
    System.out.println("inside pquery");
    int i,j,m,n;
    i=j=0;
    while(i<p1.size() && j<p2.size()){
	/*	for(;i<p1.size();i++){
				System.out.print("docs 1 are \t"+p1.get(i).docID);
				System.out.println("");
			}
			for(;j<p2.size();j++){
				System.out.print("docs  2 are \t"+p2.get(j).docID);
			}*/
			//i=0;j=0;
        if((p1.get(i).docID) == (p2.get(j).docID)){
			System.out.println("found doc "+p1.get(i).docID);

           // PostingsList l = new PostingsList();

            LinkedList<Integer> pp1 = p1.get(i).getoffsets();
            LinkedList<Integer> pp2 = p2.get(j).getoffsets();
           // System.out.println("size 1 "+pp1.size());
           // System.out.println("size 2 "+pp2.size());
            m=n=0;
            int x =0;
            int y=0;
            /*for(x=0;x<pp1.size();x++){
				System.out.print("offsets 1 are \t"+pp1.get(x));
			}
			for(y=0;y<pp2.size();y++){
				System.out.print("offsets  2 are \t"+pp2.get(y));
			}*/
            while(m<pp1.size() && n<pp2.size()){
					System.out.println("iinside loop");
					System.out.println("pp2 n "+pp2.get(n));
					System.out.println("pp1 m "+pp1.get(m));
					if(pp2.get(n)-pp1.get(m)==1){
						System.out.println("iinside if");
						answer.add(p2.get(j));
						m++;
						n++;
					}
					else if ((pp2.get(n)) > pp1.get(m)) {
							m++;
						}
						else {
							n++;
						}

				}
                i++;
                j++;

            }

            else if (p1.get(i).docID < p2.get(j).docID) {
               // System.out.println(".docid"+p1.get(i).docID);
               // System.out.println("Return docid"+p1.get(i).Returndocid());
                i++;
            }
            else {
                j++;
            }
        }
        return answer;
        }

    /**
     *  No need for cleanup in a HashedIndex.
     */
    public void cleanup() {
    }
}
