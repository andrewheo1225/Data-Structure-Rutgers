package lse;

import java.io.*;
import java.util.*;

/**
 * This class builds an index of keywords. Each keyword maps to a set of pages
 * in which it occurs, with frequency of occurrence in each page.
 *
 */
public class LittleSearchEngine {

    /**
     * This is a hash table of all keywords. The key is the actual keyword, and the
     * associated value is an array list of all occurrences of the keyword in
     * documents. The array list is maintained in DESCENDING order of frequencies.
     */
    HashMap<String, ArrayList<Occurrence>> keywordsIndex;

    /**
     * The hash set of all noise words.
     */
    HashSet<String> noiseWords;

    /**
     * Creates the keyWordsIndex and noiseWords hash tables.
     */
    public LittleSearchEngine() {
        keywordsIndex = new HashMap<String, ArrayList<Occurrence>>(1000, 2.0f);
        noiseWords = new HashSet<String>(100, 2.0f);
    }

    /**
     * Scans a document, and loads all keywords found into a hash table of keyword
     * occurrences in the document. Uses the getKeyWord method to separate keywords
     * from other words.
     * 
     * @param docFile Name of the document file to be scanned and loaded
     * @return Hash table of keywords in the given document, each associated with an
     *         Occurrence object
     * @throws FileNotFoundException If the document file is not found on disk
     */
    public HashMap<String, Occurrence> loadKeywordsFromDocument(String docFile) throws FileNotFoundException {
        HashMap<String, Occurrence> answer = new HashMap<String, Occurrence>();
        Scanner sc = new Scanner(new File(docFile));

        while (sc.hasNext()) {
            String word = getKeyword(sc.next());
            if (word == null)
                continue;
            if (!(answer.containsKey(word))) { 
                Occurrence oc = new Occurrence(docFile, 1);
                answer.put(word, oc);
            } else if (answer.containsKey(word)) {
                Occurrence oc = answer.get(word);
                oc.frequency++;
                answer.put(word, oc);
            }

        }
        return answer;
    }

    /**
     * Merges the keywords for a single document into the master keywordsIndex hash
     * table. For each keyword, its Occurrence in the current document must be
     * inserted in the correct place (according to descending order of frequency) in
     * the same keyword's Occurrence list in the master hash table. This is done by
     * calling the insertLastOccurrence method.
     * 
     * @param kws Keywords hash table for a document
     */
    public void mergeKeywords(HashMap<String, Occurrence> kws) {
        for (String word : kws.keySet()) {
            if (keywordsIndex.containsKey(word)) {
                Occurrence oc = kws.get(word);
                ArrayList<Occurrence> ocArray = keywordsIndex.get(word);                
                ocArray.add(oc);
                insertLastOccurrence(ocArray);
                keywordsIndex.put(word, ocArray);
            } else {
                Occurrence oc = kws.get(word);
                ArrayList<Occurrence> ocArray = new ArrayList<Occurrence>();
                ocArray.add(oc);
                insertLastOccurrence(ocArray);
                keywordsIndex.put(word, ocArray);
            }
        }
    }

    /**
     * Given a word, returns it as a keyword if it passes the keyword test,
     * otherwise returns null. A keyword is any word that, after being stripped of
     * any trailing punctuation(s), consists only of alphabetic letters, and is not
     * a noise word. All words are treated in a case-INsensitive manner.
     * 
     * Punctuation characters are the following: '.', ',', '?', ':', ';' and '!' NO
     * OTHER CHARACTER SHOULD COUNT AS PUNCTUATION
     * 
     * If a word has multiple trailing punctuation characters, they must all be
     * stripped So "word!!" will become "word", and "word?!?!" will also become
     * "word"
     * 
     * See assignment description for examples
     * 
     * @param word Candidate word
     * @return Keyword (word without trailing punctuation, LOWER CASE)
     */
    public String getKeyword(String word) {
        word = word.toLowerCase();
        if(noiseWords.contains(word)) return null;
        if (word.equals("")) return null;
        String trailLess = word.replaceAll("[.,?:;!]*$", "");
        String trim = word.replaceAll("[.,?:;!]*$", "").replaceAll("[^\\p{IsAlphabetic}]", "");
        if (trim.length() != trailLess.length() || noiseWords.contains(trim)) return null;
        return trailLess.toLowerCase();
    }

    /**
     * Inserts the last occurrence in the parameter list in the correct position in
     * the list, based on ordering occurrences on descending frequencies. The
     * elements 0..n-2 in the list are already in the correct order. Insertion is
     * done by first finding the correct spot using binary search, then inserting at
     * that spot.
     * 
     * @param occs List of Occurrences
     * @return Sequence of mid point indexes in the input list checked by the binary
     *         search process, null if the size of the input list is 1. This
     *         returned array list is only used to test your code - it is not used
     *         elsewhere in the program.
     */
    public ArrayList<Integer> insertLastOccurrence(ArrayList<Occurrence> occs) {
        if (occs.size() == 1) return null;

        ArrayList<Integer> answer = new ArrayList<Integer>();

        Integer lastVal = occs.get(occs.size() - 1).frequency;
        Occurrence lastValOccur = occs.get(occs.size()-1);
        occs.remove(occs.size() - 1);

        int high = 0;
        int low = occs.size() - 1;
        int mid = 0;
        int midFreq = 0;
        while (high <= low) {//reverse binary search
            mid = (low + high) / 2;
            midFreq = occs.get(mid).frequency;
            
            if (midFreq == lastVal) {
                answer.add(mid);
                break;
            } else if (midFreq < lastVal) {
                low = mid - 1;
                answer.add(mid);
            } else {
                high=mid+1;
                answer.add(mid);
                mid++;
            }
        }
        occs.add(mid, lastValOccur);
        return answer;
    }

    /**
     * This method indexes all keywords found in all the input documents. When this
     * method is done, the keywordsIndex hash table will be filled with all
     * keywords, each of which is associated with an array list of Occurrence
     * objects, arranged in decreasing frequencies of occurrence.
     * 
     * @param docsFile       Name of file that has a list of all the document file
     *                       names, one name per line
     * @param noiseWordsFile Name of file that has a list of noise words, one noise
     *                       word per line
     * @throws FileNotFoundException If there is a problem locating any of the input
     *                               files on disk
     */
    public void makeIndex(String docsFile, String noiseWordsFile) throws FileNotFoundException {
        // load noise words to hash table
        Scanner sc = new Scanner(new File(noiseWordsFile));
        while (sc.hasNext()) {
            String word = sc.next();
            noiseWords.add(word);
        }

        // index all keywords
        sc = new Scanner(new File(docsFile));
        while (sc.hasNext()) {
            String docFile = sc.next();
            HashMap<String, Occurrence> kws = loadKeywordsFromDocument(docFile);
            mergeKeywords(kws);
        }
        sc.close();
    }

    /**
     * Search result for "kw1 or kw2". A document is in the result set if kw1 or kw2
     * occurs in that document. Result set is arranged in descending order of
     * document frequencies.
     * 
     * Note that a matching document will only appear once in the result.
     * 
     * Ties in frequency values are broken in favor of the first keyword. That is,
     * if kw1 is in doc1 with frequency f1, and kw2 is in doc2 also with the same
     * frequency f1, then doc1 will take precedence over doc2 in the result.
     * 
     * The result set is limited to 5 entries. If there are no matches at all,
     * result is null.
     * 
     * See assignment description for examples
     * 
     * @param firstString First keyword
     * @param firstString Second keyword
     * @return List of documents in which either kw1 or kw2 occurs, arranged in
     *         descending order of frequencies. The result size is limited to 5
     *         documents. If there are no matches, returns null or empty array list.
     */
    public ArrayList<String> top5search(String firstString, String secondString) {

        firstString = firstString.toLowerCase();
        secondString = secondString.toLowerCase();
        ArrayList<Occurrence> firstList = keywordsIndex.get(firstString);
        ArrayList<Occurrence> secondList = keywordsIndex.get(secondString);
        final int CAP = 5;
        ArrayList<String> answer = new ArrayList<String>();

        if((!keywordsIndex.containsKey(firstString) && !keywordsIndex.containsKey(secondString))||(keywordsIndex.isEmpty()) 
            || (firstString == null && secondString == null)) return null;
        else if(!keywordsIndex.containsKey(secondString) && keywordsIndex.containsKey(firstString)){
            for(int current = 0; current < firstList.size(); current++){
                Occurrence oc = firstList.get(current);
                if(answer.size() < CAP) answer.add(oc.document);    
            }
            return answer;
        }
        else if(!keywordsIndex.containsKey(firstString) && keywordsIndex.containsKey(secondString)){
            for(int current = 0; current < secondList.size(); current++){
                Occurrence oc = secondList.get(current);
                if(answer.size() < CAP) answer.add(oc.document);
            }
            return answer;
        }
        else{

            ArrayList<Occurrence> occ = new ArrayList<Occurrence>();
            occ.addAll(keywordsIndex.get(firstString)); 
            occ.addAll(keywordsIndex.get(secondString));

            for(int count = 0; count < CAP && !occ.isEmpty(); count++){ //use slow/fast to find index of doc you want after comparison
                int fast = 0;
                int slow = -1;
                for(fast = 0; fast < occ.size() && occ.get(fast) != null; fast++){
                    if (slow == -1){
                        if (!answer.contains(occ.get(fast).document)) slow = fast;

                    } else if (occ.get(fast).frequency > occ.get(slow).frequency){
                        if(!answer.contains(occ.get(fast).document)) slow = fast;

                    } else if (occ.get(fast).frequency == occ.get(slow).frequency 
                               && keywordsIndex.get(firstString).contains(occ.get(fast))){
                        if(!answer.contains(occ.get(fast).document)) slow = fast;
                    }
                }
                if (slow != -1){
                    String doc = occ.remove(slow).document;
                    answer.add(doc); 
                } 
            }
            return answer;
        }
}
}