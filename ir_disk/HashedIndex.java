

/*  
 *   This file is part of the computer assignment for the
 *   Information Retrieval course at KTH.
 * 
 *   First version:  Johan Boye, 2010
 *   Second version: Johan Boye, 2012
 *   Additions: Hedvig Kjellström, 2012-14
 */  

////////possible check indexer insertinto function
package ir;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *   Implements an inverted index as a Hashtable from words to PostingsLists.
 */
public class HashedIndex implements Index {

    /** The index as a hashtable. */
    private HashMap<String,PostingsList> index = new HashMap<String,PostingsList>();

    
    private File tokenFile;     
    private ArrayList<String>  terms1 = new ArrayList<> ();
    private Object FileUtils;
    /**
     *  Inserts this token in the index.
     */
    
	//
	//  YOUR CODE HERE
	//

			public void insert( String token, int docID, int offset ) {
			System.out.println ("inserting docID -> " + docID );
			try {
            tokenFile = new File("terms/"+token);

            if (!terms1.contains(token)) {
                terms1.add(token);
                tokenFile.createNewFile();
            }
            FileWriter  fr = new FileWriter (tokenFile, true);
            fr.write(docID + " " + offset + "\n");
            fr.close();

        } catch (FileNotFoundException ex) {
            Logger.getLogger(HashedIndex.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(HashedIndex.class.getName()).log(Level.SEVERE, null, ex);
        }
	
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
     
   /////////////////////////////////////////////  
    public PostingsList search( Query query, int queryType, int rankingType, int structureType ) {
	// 
	//  REPLACE THE STATEMENT BELOW WITH YOUR CODE
	//
	//return null;
        //LinkedList<String> assorted = new LinkedList<>();
        //  assorted=Genascterms(query);
     //   switch (queryType) {
     // case 0:

        
        if (!new File("/home/sugandh/Downloads/ir-assignments/azitems.txt").isFile()){
            System.out.println("Making index...Wait!");
            //MakingPrinter(query);
            readtermsdir();
        }
        else{
        System.out.println("Index present on disk! Not making...");}
        LinkedList<String> assorted = new LinkedList<>();
        //assorted=Genascterms(query);
        if(queryType==Index.INTERSECTION_QUERY)
		{       
                  return DiskintersectSearch(query);}
                //return intersectSearch(assorted);}
        else if(queryType==Index.PHRASE_QUERY)
                {
                return DiskphraseSearch(query);}
        else{
                return DiskintersectSearch(query);}
                //return intersectSearch(assorted);}
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
						//bug was here
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
    
    
    private String GenerateFileName(String s){
        String fileName = new String();
        if(s.startsWith("a")||s.startsWith("z")){
            fileName = "azitems.txt";
        }
        else if(s.startsWith("b")||s.startsWith("y")){
            fileName = "byitems.txt";
        }
        else if(s.startsWith("c")||s.startsWith("x")){
            fileName = "cxitems.txt";
        }
        else if(s.startsWith("d")||s.startsWith("w")){
            fileName = "dwitems.txt";
        }
        else if(s.startsWith("e")||s.startsWith("v")){
            fileName = "evitems.txt";
        }
        else if(s.startsWith("f")||s.startsWith("u")){
            fileName = "fuitems.txt";
        }
        else if(s.startsWith("g")||s.startsWith("t")){
            fileName = "gtitems.txt";
        }
        else if(s.startsWith("h")||s.startsWith("s")){
            fileName = "hsitems.txt";
        }
        else if(s.startsWith("i")||s.startsWith("r")){
            fileName = "iritems.txt";
        }
        else if(s.startsWith("j")||s.startsWith("q")){
            fileName = "jqitems.txt";
        }
        else if(s.startsWith("k")||s.startsWith("p")){
            fileName = "kpitems.txt";
        }
        else if(s.startsWith("l")||s.startsWith("o")){
            fileName = "loitems.txt";
        }
        else if(s.startsWith("m")||s.startsWith("n")){
            fileName = "mnitems.txt";
        }
        else{
            fileName = "everythingelse.txt";
        }
        return fileName;
    }
    
    private void MakingPrinter( Query q){
    //To print term+docid+offsets within that doc
    System.out.println("size "+index.size());
    Iterator<String> d1 = getDictionary();
    Iterator<String> d1copy = getDictionary();
	
    
    while(d1.hasNext()){
	  // int ik=0;
        //System.out.println(" "+d1.toString());
        // System.out.println(" "+d1.next());
        PostingsList temp= new PostingsList();
        //temp = getPostings(d1.next());
        String d1n=d1.next();
        temp = getPostings(d1n);
        for(int xtemp=0;xtemp<temp.size();xtemp++){
            //System.out.print(" "+d1copy.next()+"   offsets are \t"+temp.get(xtemp).getoffsets().listIterator(0).toString());
            //System.out.println("");
            //}
            //System.out.print(d1n+"\t"+temp.get(xtemp).docID+"\t");
            CreateFiles(d1n, d1n+"\t"+temp.get(xtemp).docID+"\t");
            int i =0;
            while(i<temp.get(xtemp).getoffsets().size()){
                //System.out.print(temp.get(xtemp).getoffsets().get(i)+" ");
                CreateFiles(d1n, temp.get(xtemp).getoffsets().get(i)+" ");
                i++;
            }
            System.out.println("");
            CreateFiles(d1n, "\n");
				
	}
       
    }
}
    
    public void CreateFiles(String s1, String s2) {
        //s1 - string to create/find the name of the file
        //s2 - string containing the actual data
        
        // The name of the file to open.
        //int fcount = 0;
        //int count=0;
        
        
        //String fileName = "temp"+fcount+".txt";
        String fileName = GenerateFileName(s1);
        
        
        BufferedWriter writer = null;
        try {
            // Assume default encoding.
           // FileWriter fileWriter =
             //   new FileWriter(fileName);

            // Always wrap FileWriter in BufferedWriter.
            File logFile = new File(fileName);
            writer = new BufferedWriter(new FileWriter(fileName,true));

            // Note that write() does not automatically
            // append a newline character.
            writer.write(s2);
            

            // Always close files.
            writer.close();
        }
        catch(IOException ex) {
            System.out.println(
                "Error writing to file '"
                + fileName + "'");
            // Or we could just do this:
            // ex.printStackTrace();
        }
    }
    
    private LinkedList<String> term1 = new LinkedList();
    private LinkedList<String> term2 = new LinkedList();
    private LinkedList<String> term3temp = new LinkedList();
    
    public LinkedList<String> ReadFromFileIntersect(String term){
        //LinkedList<String> term1 = new LinkedList();
        //LinkedList<String> term2 = new LinkedList();
        String filename = GenerateFileName(term);
        term1.clear();
        Scanner sc2 = null;
        try {
            sc2 = new Scanner(new File(filename));
        }   catch (FileNotFoundException e) {
            e.printStackTrace();  
        }
        while (sc2.hasNextLine()) {
            String[] words = sc2.nextLine().split("\\s");
        
            
            //System.out.println(words[0]);
            if(words[0].equals(term)){
                
                term1.add(words[1]);
            
            }
				
        }
        
        /*for(int i=0;i<term1.size();i++){
            System.out.println("term1111");
            System.out.println("term1 item "+term1.get(i));
        }*/
        //term3temp=(LinkedList<String>)term1.clone();
        
        //tem2=tem1.;
	//term3temp.retainAll(term1);
        //for(int i=0;i<term1.size();i++){
        //    System.out.println("term1 item "+term1.get(i));
        //}
        if(term2.isEmpty()){
            term2=(LinkedList<String>)term1.clone();
        }
        else{
            term2.retainAll(term1);
        }
        /*for(int i=0;i<term2.size();i++){
            System.out.println("term2 item "+term2.get(i));
        }*/
        term3temp=(LinkedList<String>)term2.clone();
        return term3temp;
    }
    
private PostingsList DiskintersectSearch(Query q) {
	//for (int i=0;i<=terms.size();i++){
    System.out.println("inside disk, query size =  "+q.size());
	//
    
    LinkedList<String> terms = q.copy().terms;
    LinkedList<String> linkedlist = new LinkedList<>();
    
    PostingsList list = new PostingsList();

    while (!terms.isEmpty()) {
        System.out.println("inside While");
        linkedlist = ReadFromFileIntersect(terms.removeFirst());
    }
     
    /*for(int i=0;i<linkedlist.size();i++){
        System.out.println("linked list "+linkedlist.get(i));
    }*/
    
    
    while (!term2.isEmpty()) {
        //System.out.println("dele yayayaya ");
        term2.removeFirst();
    }
    //System.out.println("linked list 2 "+linkedlist.size());
    /*for(int i=0;i<linkedlist.size();i++){
		
        System.out.println("linked list 2 "+linkedlist.get(i));
    }*/
    //System.out.println("linked list yayayaya ");
    for (int i = 0; i < linkedlist.size(); i++) {
        list.addListEntry(Integer.parseInt(linkedlist.get(i)));
    }
    /*for (int i=0;i<list.size();i++){
        System.out.println("post list "+list.get(i).toString());
    }*/
    System.out.println("Search finished!");
    return list;
  }

    private PostingsList DiskphraseSearch(Query q) {
    System.out.println("inside disk, query size = "+q.size());
	//
    String first, second;
    LinkedList<String> terms = q.copy().terms;
    LinkedList<String> linkedlist = new LinkedList<>();
    
    PostingsList list = new PostingsList();
    if(terms.size()==1){
        linkedlist = ReadFromFileIntersect(terms.removeFirst());
    }
    
    else if(terms.size()==2){
        first = terms.removeFirst();
        System.out.println(first);
        second = terms.removeFirst();
        System.out.println(second);
        linkedlist = ReadfromFilePhrase(first, second);
    }
    else{
        first = terms.removeFirst();
        second = terms.removeFirst();
        ReadfromFilePhrase(first, second);
        while (!terms.isEmpty()) {
            first = second;
            second = terms.removeFirst();
            linkedlist = ReadfromFilePhrase(first, second);
        }
    }
    for (int i = 0; i < linkedlist.size(); i++) {
        list.addListEntry(Integer.parseInt(linkedlist.get(i)));
    }
    /*for (int i=0;i<list.size();i++){
        System.out.println("post list "+list.get(i).toString());
    }*/
    
    System.out.println("Search finished!");
    aret.clear();
    return list;
    }

    private LinkedList<String> aret = new LinkedList<>();
    private LinkedList<String> ReadfromFilePhrase(String first, String second){
        String filename1 = GenerateFileName(first);
        String filename2 = GenerateFileName(second);
        //List<List<String>> fword = new ArrayList<>();
        //List<List<String>> sword = new ArrayList<>();
        Map<String, ArrayList<String>> w1 = new HashMap<String, ArrayList<String>>();
        Map<String, ArrayList<String>> w2 = new HashMap<>();
        LinkedList<String> atemp = new LinkedList<>();
        //ArrayList<String> a2 = new ArrayList<>();
        System.out.println("Calling phrase search disk");
        //term1.clear();
        Scanner sc1 = null;
        try {
            sc1 = new Scanner(new File(filename1));
        }   catch (FileNotFoundException e) {
            e.printStackTrace();  
        }
        while (sc1.hasNextLine()) {
            String[] words = sc1.nextLine().split("\\s");
        
            
            //System.out.println(words[0]);
            if(words[0].equals(first)){
                
                //term1.add(words[1]);
                int i=2;
                ArrayList<String> a1= new ArrayList<>();
                while(i<words.length){
                    //System.out.println("adding" + words[i]);
                    a1.add(words[i]);
                    //System.out.println(a1.get(0));
                    i++;
                }
                //System.out.println("words1"+words[1]);
                w1.put(words[1],a1);
                
            }
				
        }
        /*for(String key:w1.keySet()){
            System.out.println("print "+key+"\t"+w1.get(key));
        }*/
        //System.out.println("w1 "+w1);
        
        Scanner sc2 = null;
        try {
            sc2 = new Scanner(new File(filename2));
        }   catch (FileNotFoundException e) {
            e.printStackTrace();  
        }
        while (sc2.hasNextLine()) {
            String[] words = sc2.nextLine().split("\\s");
        
            
            //System.out.println(words[0]);
            if(words[0].equals(second)){
                
                if(words[0].equals(second)){
                
                //term1.add(words[1]);
                int i=2;
                ArrayList<String> a2 = new ArrayList<>();
                while(i<words.length){
                    //System.out.println("adding" + words[i]);
                    a2.add(words[i]);
                    //System.out.println(a2.get(0));
                    i++;
                }
                //System.out.println("words1"+words[1]);
                w2.put(words[1],a2);
                
            }
				
        }
        
        //for(int ik=0;ik<w1.size();ik++)
        //    System.out.println(w1.get(ik));
        }
        /*for(String key:w2.keySet()){
            System.out.println("print222 "+key+"\t"+w2.get(key));
        }*/
        for(String key1:w1.keySet()){
            for(String key2:w2.keySet()){
                if(key1.equals(key2)){
                    //System.out.println("offset word 2 "+w2);
                    //System.out.println("offset word 1 "+w1);
                    int i=0,j=0;
                    
                    while(i<w1.get(key1).size() && j<w2.get(key2).size()){
							
			    //System.out.println("offset word 2 "+Integer.parseInt(w2.get(key2).get(j)));
			    //System.out.println("offset word 1 "+Integer.parseInt(w1.get(key1).get(i)));
                            if(Integer.parseInt(w2.get(key2).get(j))-Integer.parseInt(w1.get(key1).get(i))==1){
                                //System.out.println("docid 2 "+w2);
                                //System.out.println("docid 1 "+w1);
                                //System.out.println("key1 "+key1+" key2 "+key2);
                                atemp.add(key1);
                                //System.out.println("atemp "+atemp.size());
                                i++;
                                j++;
                            }
                            else if(Integer.parseInt(w2.get(key2).get(j))>Integer.parseInt(w1.get(key1).get(i))){
                                i++;
                            }
                            else{
                                j++;
                            }
                        }
                    
                }
            }
        }
        //System.out.println("atemp size " +atemp.size());
        if(aret.size()==0){
            aret = (LinkedList<String>) atemp.clone();
            
        }
        else{
            aret.retainAll(atemp);
        }
        //System.out.println("aret size " +aret.size());
        /*for(int i=0;i<aret.size();i++){
            System.out.println("Aret "+aret.get(i));
        }*/
    return aret;
    }
    
    
      public void loadnames(){
		System.out.println("calling indexhashd");
       /* try {
            String line;
            BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream("/home/sugandh/Downloads/ir-assignments/doclist.txt")));
            String[]    t;
            while ((line = in.readLine()) != null){
                t = line.split("\\s+");
                docIDs.put( t[0], t[1]);
            }
            in.close();
        } 
        catch (IOException ex) {
            Logger.getLogger(HashedIndex.class.getName()).log(Level.SEVERE, null, ex);
        }*/
         String filename = "/home/sugandh/Downloads/ir-assignments/doclist.txt";
         Scanner sc2 = null;
         
         try {
            sc2 = new Scanner(new File(filename));
        }   catch (FileNotFoundException e) {
            e.printStackTrace();  
        }
        while (sc2.hasNextLine()) {
            String[] words = sc2.nextLine().split("\\s");
            docIDs.put(words[0], words[1]);
		}
	}

    public void readtermsdir(){
        ArrayList<PostingsList> narray = new ArrayList<>();
         File dir = new File("/home/sugandh/Downloads/ir-assignments/terms");
    File[] directoryListing = dir.listFiles();
    if (directoryListing != null) {
        for (File child : directoryListing) {
            //System.out.println("file "+child);
            String fname;
            fname = child.toString();
            PostingsList ptemp = new PostingsList();
            Scanner sc2 = null;
         
            try {
            sc2 = new Scanner(new File(fname));
            }   catch (FileNotFoundException e) {
            e.printStackTrace();  
            }
            while (sc2.hasNextLine()) {
      // Do something with child
                String[] words = sc2.nextLine().split("\\s");
                int ip=1;
                //System.err.println("words length "+words.length);
                while(ip<words.length){
                ptemp.addListEntry(Integer.parseInt(words[0]), Integer.parseInt(words[ip]));
                ip++;
                }
               
            }
             narray.add(ptemp);
            makealphafiles(fname, child.getName(), narray);
        }
    }
    else {
    // Handle the case where dir is not really a directory.
    // Checking dir.isDirectory() above would not be sufficient
    // to avoid race conditions with another process that deletes
    // directories.
    }
    }
    
    private void makealphafiles(String fileName, String child, ArrayList<PostingsList> narray){
       
	//System.out.println("inside makeaklpha "+child);
        //for (Iterator<PostingsList> it = narray.iterator(); it.hasNext();) {
        // PostingsList entry = it.next();
                while(!narray.isEmpty()){
                PostingsList entry = narray.get(0);
                narray.remove(0);
                for(int xtemp=0;xtemp<entry.size();xtemp++){
                    //System.out.println("xtem "+xtemp);
            //System.out.print(" "+d1copy.next()+"   offsets are \t"+temp.get(xtemp).getoffsets().listIterator(0).toString());
            //System.out.println("entry size "+entry.size());
            //}
            //System.out.print(child+"\t"+entry.get(xtemp).docID+"\t");
            CreateFiles(child, child+"\t"+entry.get(xtemp).docID+"\t");
            int i =0;
            while(i<entry.get(xtemp).getoffsets().size()){
                //System.out.print(entry.get(xtemp).getoffsets().get(i)+" ");
                CreateFiles(child, entry.get(xtemp).getoffsets().get(i)+" ");
                i++;
            }
            //System.out.println("");
            CreateFiles(child, "\n");
                }    
            }
            narray.clear();
           
    }
}
