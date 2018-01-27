package search;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;

public class LittleSearchEngineApp {
	
	public static void main(String args[]){

		LittleSearchEngine a= new LittleSearchEngine();
		
		
		try {
			//System.out.println(a.getKeyWord("3.0"));
			//HashMap<String, Occurrence> load = a.loadKeyWords("AliceCh1.txt");
			//System.out.println("Keyset is: "+load.keySet());
			//a.mergeKeyWords(load);
			//System.out.println(a.keywordsIndex);
			
			a.makeIndex("test.txt", "noisewords.txt");
			System.out.print("Keywords is:"+a.keywordsIndex);
			System.out.println("Top5 is:"+a.top5search( "Bus", "Car"));
			
			//System.out.println(a.loadKeyWords("AliceCh1.txt"));
		
			//System.out.println(a.keywordsIndex);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		
	


}
	}
}
	


