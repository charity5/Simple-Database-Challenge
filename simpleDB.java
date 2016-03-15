import java.util.*;
import java.io.*;

public class simpleDB {
			
	//For each block (includeing base block), there are two hashtables store name_value and value_count.
	//LinkedList records all the blocks in order.
	private LinkedList<Hashtable<String, Integer>> database;
	private LinkedList<Hashtable<Integer, Integer>> count;
	
	//Initialize
	public simpleDB(){
		database = new LinkedList<Hashtable<String, Integer>>();
		count = new LinkedList<Hashtable<Integer, Integer>>();
		
		//Add first/base block
		Hashtable<String, Integer> baseData = new Hashtable<String, Integer>();
		Hashtable<Integer, Integer> baseCount = new Hashtable<Integer, Integer>();
		database.add(baseData);
		count.add(baseCount);
	}
	
	public void set(String name, int value) {
		//Get current block
		Hashtable<String, Integer> curDB = database.getLast();
		Hashtable<Integer, Integer> curCount = count.getLast();
		
		//If name_value already exists, delete its count first.
		if (curDB.containsKey(name)) {      
			int valTmp = curDB.get(name);
			curCount.put(valTmp, curCount.get(valTmp) - 1);
			if (curCount.get(valTmp) == 0) {
				curCount.remove(valTmp);
			}
		}
		
		//Set new name_value and value's corresponding count
		curDB.put(name, value);
		if (curCount.containsKey(value)) {
			curCount.put(value, curCount.get(value) + 1);
		}else {
			curCount.put(value, 1);
		}		
	}
	
	
	public Integer get(String name) {
		return database.getLast().get(name);
		
	}
	
	public void unset(String name) {
		Hashtable<String, Integer> curData = database.getLast();
		Hashtable<Integer, Integer> curCount = count.getLast();
		
		int valTmp = curData.get(name);
		curData.remove(name);                                 //Remove name_value
		curCount.put(valTmp, curCount.get(valTmp) - 1);       //Count of value -1
		if (curCount.get(valTmp) == 0) {                      //If count is already 0, remove object
			curCount.remove(valTmp);			
		}
	}
	
	public int numEqualTo(int value) {
		Integer rst;
		rst = count.getLast().get(value);
		return rst == null ? 0 : (int) rst; //If can't find value in hashtable, return 0 instead of null
	}
	
	public void begin() {
		//For new block, create new hashtable, content is copied from previous block
		Hashtable<String, Integer> ndb = new Hashtable<String, Integer>(database.getLast());
		Hashtable<Integer, Integer> ncount = new Hashtable<Integer, Integer>(count.getLast());		
		database.add(ndb);
		count.add(ncount);
	}
	
	public void rollback() {
		if (database.size() == 1) {     //Keep first hashtable in linkedlist, which is the base block
			System.out.println("NO TRANSACTION");
		}else {
			database.removeLast();
			count.removeLast();
		}
	}
	
	public void commit() {
		if (database.size() == 1) {     //Keep first hashtable in linkedlist, which is the base block
			System.out.println("NO TRANSACTION");
		}else {
			//Get base block
			Hashtable<String, Integer> baseData = database.get(0);
			Hashtable<Integer, Integer> baseCount = count.get(0);

			Hashtable<String, Integer> ht;
			Enumeration<String> hashKeys;
			String name;	
			int value;
			//Go through linkedList starting from secoend one, overwrite name_value info on the base block
			for (int i = 1; i < database.size(); i++) {
				ht = database.get(i);
				hashKeys = ht.keys();
				while (hashKeys.hasMoreElements()) {
					name = (String) hashKeys.nextElement();
					value = ht.get(name);
					
					//If name_value already exists, delete its count first.
					if (baseData.containsKey(name)) {    
						int valTmp = baseData.get(name);
						baseCount.put(valTmp, baseCount.get(valTmp) - 1);
						if (baseCount.get(valTmp) == 0) {
							baseCount.remove(valTmp);
						}
					}

					//Set new name_value and value's corresponding count
					baseData.put(name, value);           
					if (baseCount.containsKey(value)) {
						baseCount.put(value, baseCount.get(value) + 1);
					}else {
						baseCount.put(value, 1);
					}
				}

			}
			
			//Remove all the blocks except the first base block.
			for (int i = 1; i< database.size(); i++) {
				database.removeLast();
				count.removeLast();
			}
		}
	}		

}
