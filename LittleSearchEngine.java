package search;

import java.io.*;
import java.util.*;

/**
 * This class encapsulates an occurrence of a keyword in a document. It stores the
 * document name, and the frequency of occurrence in that document. Occurrences are
 * associated with keywords in an index hash table.
 * 
 * @author Sesh Venugopal
 * 
 */
class Occurrence {
	/**
	 * Document in which a keyword occurs.
	 */
	String document;
	
	/**
	 * The frequency (number of times) the keyword occurs in the above document.
	 */
	int frequency;
	
	/**
	 * Initializes this occurrence with the given document,frequency pair.
	 * 
	 * @param doc Document name
	 * @param freq Frequency
	 */
	public Occurrence(String doc, int freq) {
		document = doc;
		frequency = freq;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return "(" + document + "," + frequency + ")";
	}
}

/**
 * This class builds an index of keywords. Each keyword maps to a set of documents in
 * which it occurs, with frequency of occurrence in each document. Once the index is built,
 * the documents can searched on for keywords.
 *
 */
public class LittleSearchEngine {
	
	/**
	 * This is a hash table of all keywords. The key is the actual keyword, and the associated value is
	 * an array list of all occurrences of the keyword in documents. The array list is maintained in descending
	 * order of occurrence frequencies.
	 */
	HashMap<String,ArrayList<Occurrence>> keywordsIndex;
	
	/**
	 * The hash table of all noise words - mapping is from word to itself.
	 */
	HashMap<String,String> noiseWords;
	
	/**
	 * Creates the keyWordsIndex and noiseWords hash tables.
	 */
	public LittleSearchEngine() {
		keywordsIndex = new HashMap<String,ArrayList<Occurrence>>(1000,2.0f);
		noiseWords = new HashMap<String,String>(100,2.0f);
	}
	
	/**
	 * This method indexes all keywords found in all the input documents. When this
	 * method is done, the keywordsIndex hash table will be filled with all keywords,
	 * each of which is associated with an array list of Occurrence objects, arranged
	 * in decreasing frequencies of occurrence.
	 * 
	 * @param docsFile Name of file that has a list of all the document file names, one name per line
	 * @param noiseWordsFile Name of file that has a list of noise words, one noise word per line
	 * @throws FileNotFoundException If there is a problem locating any of the input files on disk
	 */
	public void makeIndex(String docsFile, String noiseWordsFile) 
	throws FileNotFoundException {
		// load noise words to hash table
		Scanner sc = new Scanner(new File(noiseWordsFile));
		while (sc.hasNext()) {
			String word = sc.next();
			noiseWords.put(word,word);
		}
		
		// index all keywords
		sc = new Scanner(new File(docsFile));
		while (sc.hasNext()) {
			String docFile = sc.next();
			HashMap<String,Occurrence> kws = loadKeyWords(docFile);
			mergeKeyWords(kws);
		}
		
	}

	/**
	 * Scans a document, and loads all keywords found into a hash table of keyword occurrences
	 * in the document. Uses the getKeyWord method to separate keywords from other words.
	 * 
	 * @param docFile Name of the document file to be scanned and loaded
	 * @return Hash table of keywords in the given document, each associated with an Occurrence object
	 * @throws FileNotFoundException If the document file is not found on disk
	 */
	public HashMap<String,Occurrence> loadKeyWords(String docFile) 
	throws FileNotFoundException {
		HashMap<String, Occurrence> keyWords = new HashMap<String,Occurrence>();
		Scanner sc = null;
		try{
			 sc = new Scanner(new File(docFile));
		}
		catch (FileNotFoundException e){
			System.out.println("can't find the file "+docFile);
			return keyWords;
		}
		while(sc.hasNextLine()){
			Scanner sc2 = new Scanner(sc.nextLine());
			int freq = 1;
			while(sc2.hasNext()){
				String s =  sc2.next();//extract the next word
				if(getKeyWord(s)!=null){
					String key = getKeyWord(s);// get the result value of the key word
					Occurrence word = new Occurrence(docFile, freq);
					if(!keyWords.containsKey(key)){
						keyWords.put(key, word);
					}
					else{
						keyWords.get(key).frequency++;
					}
					
					
					
						
					}
				}
			}
		
		// COMPLETE THIS METHOD
		// THE FOLLOWING LINE HAS BEEN ADDED TO MAKE THE METHOD COMPILE
		return keyWords;
	}
	
	/**
	 * Merges the keywords for a single document into the master keywordsIndex
	 * hash table. For each keyword, its Occurrence in the current document
	 * must be inserted in the correct place (according to descending order of
	 * frequency) in the same keyword's Occurrence list in the master hash table. 
	 * This is done by calling the insertLastOccurrence method.
	 * 
	 * @param kws Keywords hash table for a document
	 */
	public void mergeKeyWords(HashMap<String,Occurrence> kws) {
		
		for(String key:kws.keySet()){
			if(keywordsIndex.containsKey(key)){
				ArrayList<Occurrence> temp= keywordsIndex.get(key);
				temp.add(kws.get(key));
				insertLastOccurrence(temp);
				keywordsIndex.put(key, temp);
			}
			
			else{
				ArrayList<Occurrence> temp = new ArrayList<Occurrence>();
				temp.add(kws.get(key));
				keywordsIndex.put(key, temp);
			}
		}
	}
		
		// COMPLETE THIS METHOD
	
	/**
	 * Given a word, returns it as a keyword if it passes the keyword test,
	 * otherwise returns null. A keyword is any word that, after being stripped of any
	 * TRAILING punctuation, consists only of alphabetic letters, and is not
	 * a noise word. All words are treated in a case-INsensitive manner.
	 * 
	 * Punctuation characters are the following: '.', ',', '?', ':', ';' and '!'
	 * 
	 * @param word Candidate word
	 * @return Keyword (word without trailing punctuation, LOWER CASE)
	 */
	public String getKeyWord(String word) {
		String newWord = null;
		int j =0;
		
		for(j = 0; j<word.length(); j++){//j is the index
			
			char i = word.charAt(j);//i is the current char
			
			if(i>='A'&&i<='Z'||i>='a'&&i<='z'){//if the char is a letter, continue
				i++;
				continue;
			}
			else{
				if(j==0){
					return null;
				}
				for(int k=j;k<word.length();k++){
					if(word.charAt(k)>='A'&&word.charAt(k)<='Z'
							||word.charAt(k)>='a'&&word.charAt(k)<='z'){
						return null;
					}
					
					
				}
				
					break;
				/*if((word.charAt(j-1)>='A'&&word.charAt(j-1)<='Z'
						||word.charAt(j-1)>='a'&&word.charAt(j-1)<='z'))
				{//the previous char is not a punctuation, then substring 
					
					newWord = word.substring(0, j);
					}
				
			if(j+1<word.length()){
				
				if(word.charAt(j+1)>='A'&&word.charAt(j+1)<='Z'
						||word.charAt(j+1)>='a'&&word.charAt(j+1)<='z')
				{// if the next char is a letter, then return null
					return null;
				}
					}
				
			}*/
			
			
		}
		}
		
		
		newWord = word.substring(0, j);
		newWord = newWord.toLowerCase();
		if(noiseWords.containsKey(newWord)){
			return null;
		}
		else{
			return newWord;
		} 
	}
		// COMPLETE THIS METHOD
		// THE FOLLOWING LINE HAS BEEN ADDED TO MAKE THE METHOD COMPILE
		
	
	/**
	 * Inserts the last occurrence in the parameter list in the correct position in the
	 * same list, based on ordering occurrences on descending frequencies. The elements
	 * 0..n-2 in the list are already in the correct order. Insertion of the last element
	 * (the one at index n-1) is done by first finding the correct spot using binary search, 
	 * then inserting at that spot.
	 * 
	 * @param occs List of Occurrences
	 * @return Sequence of mid point indexes in the input list checked by the binary search process,
	 *         null if the size of the input list is 1. This returned array list is only used to test
	 *         your code - it is not used elsewhere in the program.
	 */
	public ArrayList<Integer> insertLastOccurrence(ArrayList<Occurrence> occs) {
		ArrayList<Integer> binarySearch = new ArrayList<Integer>();
		if(occs.size()==1){
			return null;
			
		}
		Occurrence newAdd  = occs.get(occs.size()-1);
		occs.remove(occs.size()-1);
		int freq = newAdd.frequency;
		int rightIndex  = occs.size()-1;
		int leftIndex = 0;
		int mid = (leftIndex+rightIndex)/2;
		while(freq!=occs.get(mid).frequency && rightIndex>=leftIndex){
			
			mid = (leftIndex+rightIndex)/2;
			binarySearch.add(mid);
			
			if(freq<occs.get(mid).frequency){
				 leftIndex = mid+1;
				 
				
			}
			else{
				rightIndex = mid-1;
			
			}
			
			}
		if(freq>=occs.get(mid).frequency){
			occs.add(mid,newAdd);
		}
		if(freq<occs.get(mid).frequency){
			occs.add(mid+1,newAdd);
		
		}
		
		// COMPLETE THIS METHOD
		// THE FOLLOWING LINE HAS BEEN ADDED TO MAKE THE METHOD COMPILE
		return binarySearch;
	}
	
	/**
	 * Search result for "kw1 or kw2". A document is in the result set if kw1 or kw2 occurs in that
	 * document. Result set is arranged in descending order of occurrence frequencies. (Note that a
	 * matching document will only appear once in the result.) Ties in frequency values are broken
	 * in favor of the first keyword. (That is, if kw1 is in doc1 with frequency f1, and kw2 is in doc2
	 * also with the same frequency f1, then doc1 will appear before doc2 in the result. 
	 * The result set is limited to 5 entries. If there are no matching documents, the result is null.
	 * 
	 * @param kw1 First keyword
	 * @param kw1 Second keyword
	 * @return List of NAMES of documents in which either kw1 or kw2 occurs, arranged in descending order of
	 *         frequencies. The result size is limited to 5 documents. If there are no matching documents,
	 *         the result is null.
	 */
	public ArrayList<String> top5search(String kw1, String kw2) {
		kw1 = getKeyWord(kw1);
		kw2 = getKeyWord(kw2);
		ArrayList<Occurrence> firstKey = new ArrayList<Occurrence>() ;
		ArrayList<Occurrence> secondKey = new ArrayList<Occurrence>(); 
		if(kw1!=null){
			firstKey = keywordsIndex.get(kw1);
		}
		if(kw2!=null){
			 secondKey = keywordsIndex.get(kw2);
		}
		ArrayList<String> Top5 = new ArrayList<String>();
		if(firstKey!=null&&secondKey!=null){
		int size = firstKey.size()<secondKey.size()? firstKey.size():secondKey.size();
		int firstIndex =0;
		int secondIndex = 0;
		Occurrence keyword1 = firstKey.get(firstIndex);
		Occurrence keyword2 = secondKey.get(secondIndex);
		int compare = keyword1.frequency-keyword2.frequency;
		int count =0;
		while(firstIndex<firstKey.size()&&secondIndex<secondKey.size()){//
			if(Top5.isEmpty()){
				if(compare>0){
					Top5.add(keyword1.document);
					firstIndex++;
					count++;
				}
				else if(compare<0){
					Top5.add(keyword2.document);
					secondIndex++;
					count++;
				}
				else{
					Top5.add(keyword1.document);
					count++;
					if(!Top5.contains(keyword2.document)){
					Top5.add(keyword2.document);
					count++;}
					firstIndex++;
					secondIndex++;
				}
			}
			else{
			if(compare==0){
				
					if(!Top5.contains(keyword1.document)){
						Top5.add(keyword1.document);
						count++;
						
					}
					firstIndex++;
					if(!Top5.contains(keyword2.document)&&count<5){
						Top5.add(keyword2.document);
						count++;
						
					}
					secondIndex++;
				}
		
				
			
		   if(compare>0){
			   if(!Top5.contains(keyword1.document)){
				   Top5.add(keyword1.document);
				   count++;
			   }
			   firstIndex++;
		   }
		   if(compare<0){
			   if(!Top5.contains(keyword2.document)){
				   Top5.add(keyword2.document);
				   count++;
			   }
			   secondIndex++;
		   }
		   
		   if(count==5){
			   return Top5;
		   }
			}
			if(firstIndex<firstKey.size()){
		   keyword1 = firstKey.get(firstIndex);
			}
			else{
				break;
			}
			if(secondIndex<secondKey.size()){
		   keyword2 = secondKey.get(secondIndex);}
			else{
				break;
			}
		   compare = keyword1.frequency-keyword2.frequency;
		
		}
		if(firstIndex>=firstKey.size()&&secondIndex<secondKey.size()){
			while(count!=5){
				if(!Top5.contains(keyword2.document)&&secondIndex<secondKey.size()){
					   keyword2 = secondKey.get(secondIndex); 
					   Top5.add(keyword2.document);
					   count++;
				   }
				secondIndex++;
				
			}
			return Top5;
		}
		if(firstIndex<firstKey.size()&&secondIndex>=secondKey.size()){
			while(count!=5){
				if(!Top5.contains(keyword1.document)&&firstIndex<firstKey.size()){
					   keyword1 = firstKey.get(firstIndex); 
					   Top5.add(keyword1.document);
					   count++;
				   }
				firstIndex++;
				
			}
			return Top5;
		}
		}
		else if(firstKey==null&&secondKey!=null){
			int count=0;
			while(!(secondKey.size()<=count)&&count<5){
				Top5.add(secondKey.get(count).document);
				count++;
			}
		}
		else if(firstKey!=null&&secondKey==null){
			int count=0;
			while(!(firstKey.size()<=count)&&count<5){
				Top5.add(firstKey.get(count).document);
				count++;
			}
		}
		else{
		}
		// COMPLETE THIS METHOD
		// THE FOLLOWING LINE HAS BEEN ADDED TO MAKE THE METHOD COMPILE
		return Top5;
	}
}
