package org.ToMar.wordlist;

import java.util.*;
import java.io.*;
/**
 * marie 6/10/2013
 * Importing the word files from ToMarGames into NetBeans.
 * This class will be the contact point for word lists of all types.
 */
public class WordList
{
    private ArrayList<String> wordMaster;
	private String masterFile = "tmWords.txt";

    public WordList()
    {
        if (wordMaster == null)
        {
			loadMaster();
        }
    }
	private void loadMaster()
	{
		wordMaster = new ArrayList<>();
		String s;
		try
		{
			InputStream is = this.getClass().getResourceAsStream(masterFile);
			try (BufferedReader br = new BufferedReader(new InputStreamReader(is)))
			{
				while   (true)
				{
					s = br.readLine();
					if  (s == null)
					{
						break;
					}
					wordMaster.add(s.toLowerCase());
				}
			}
		}
		catch   (Exception e)
		{
			log("Exception = " + e);
		}
	}
    public ArrayList<String> getWordListByLength(int wordLength, boolean uppercase)
    {
        ArrayList<String> al = new ArrayList<>();
        for (String w : wordMaster)
        {
            if (w.length() == wordLength
            || wordLength == 0)
            {
				w = (uppercase) ? w.toUpperCase() : w.toLowerCase();
                al.add(w);
//				System.out.println(w);
            }
        }
        return al;
    }
    public ArrayList<String> getWordListByPattern(String pattern, boolean uppercase)
    {
        ArrayList<String> al = getWordListByLength(pattern.length(), uppercase);
		ArrayList<String> a2 = new ArrayList<>();
        for (String w : al)
        {
			boolean match = true;
			for (int i = 0; i < w.length(); i++)
			{
				if (pattern.substring(i, i + 1).equalsIgnoreCase("?")
				||  pattern.substring(i, i + 1).equalsIgnoreCase(w.substring(i, i + 1)))
				{
					continue;
				}
				match = false;
				break;
			}
			if (match)
			{
				a2.add(w);
			}
        }
        return a2;
    }
	private boolean matchNumberedPositions(String s, Integer a, Integer b)
	{
		if (s.substring(a, a+1).equalsIgnoreCase(s.substring(b, b+1)))
		{
			return true;
		}
		return false;
	}
    public ArrayList<String> getWordListByNumberPattern(String pattern, boolean uppercase)
    {
        ArrayList<String> al = getWordListByLength(pattern.length(), uppercase);
		ArrayList<String> a2 = new ArrayList<>();
		// parse the pattern and store positions that must be alike in array
		// pattern must be just numbers and ?
		// so now the arrayList in position
		HashMap<Integer, ArrayList<Integer>> map = new HashMap<>();
		for (int i = 0; i < pattern.length(); i++)
		{
			if (!("?".equalsIgnoreCase(pattern.substring(i, i+1))))
			{
				Integer num = new Integer(pattern.substring(i, i+1));
				if (!(map.containsKey(num)))
				{
					map.put(num, new ArrayList<Integer>());
//					log("added arrayList for position " + num);
				}
				((ArrayList<Integer>) map.get(num)).add(new Integer(i));
//				log("added " + i + " to arrayList for " + num);
			}
		}
        for (String w : al)
        {
			boolean match = true;
			for (ArrayList<Integer> list : map.values())
			{
				for (int i = 0; i < list.size(); i++)
				{
					for (int j = i + 1; j < list.size(); j++)
					{
						match = matchNumberedPositions(w, list.get(i), list.get(j));
						if (!match)
						{
							break;
						}
					}
					if (!match)
					{
						break;
					}
				}
				if (!match)
				{
					break;
				}
			}
			if (match)
			{
				a2.add(w);
			}
        }
        return a2;
    }
	public  void setMasterFile(String masterFile)
	{
		this.masterFile = masterFile;
		loadMaster();
	}
	private static void log(String s)
	{
		System.out.println(s);
	}
	private void crypto()
	{
		ArrayList<String> al = getWordListByLength(4, true);
		for (int i = 0; i < al.size(); i++)
		{
			String pattern = al.get(i);
			pattern = pattern.substring(0,2) + "?" + pattern.substring(3, 4);
			ArrayList<String> a2 = getWordListByPattern(pattern, true);
			for (int j = 0; j < a2.size(); j++)
			{
				if (!(al.get(i).equalsIgnoreCase(a2.get(j))))
				{
					pattern = "?" + al.get(i).substring(2, 3) + "?" + a2.get(j).substring(2, 3) + al.get(i).substring(2, 3) + al.get(i).substring(1, 2) + a2.get(j).substring(2, 3);
					ArrayList<String> a3 = getWordListByPattern(pattern, true);
					for (int k = 0; k < a3.size(); k++)
					{
						log(al.get(i) + ", " + a2.get(j) + ", " + a3.get(k));
					}
				}
			}
		}
	}
	public static void main (String[] args)
	{
        WordList w = new WordList();
//		w.setMasterFile("zynga.txt");
//		w.crypto();
		ArrayList<String> al = w.getWordListByNumberPattern("12?1?2", false);
		for (int i = 0; i < al.size(); i++)
		{
			log(al.get(i));
		}
	}
}
