package trie;
import java.util.ArrayList;
/**
* This class implements a Trie.
*
* @author Sesh Venugopal
*
*/
public class Trie {
  // prevent instantiation
 private Trie() { }
  
 public static TrieNode buildTrie(String[] allWords) {
     TrieNode root = new TrieNode (null, null, null);

     if (allWords.length == 0){
          return root;
     }
     if (root.firstChild==null) {
         Indexes substr = new Indexes (0, (short) 0, (short) (allWords[0].length()-1));

         TrieNode newLeaf = new TrieNode (substr, null, null);

         root.firstChild = newLeaf;
     }
     for (int x=1; x<allWords.length; x++) {

        newTrie(root.firstChild, allWords, x, root);
     }
     //return the beginngin
     return root;
 
 }

 private static void alterNode (TrieNode rootNode, TrieNode childNode, int arrayIndex, String[] allWords ) {
    int initialIndex = childNode.substr.startIndex;
    
    int primary = Math.min(childNode.substr.wordIndex, arrayIndex);

    int y=0;

    for (y=initialIndex; y<childNode.substr.endIndex; y++) {

        if (allWords[childNode.substr.wordIndex].charAt(y) == allWords[arrayIndex].charAt(y) ) {
            continue;
        }
        break;
    }
    Indexes indexTemp = new Indexes (primary, (short) initialIndex, (short)(y-1));

    TrieNode tempNOde = new TrieNode (indexTemp, childNode, childNode.sibling);

    if (rootNode.firstChild == childNode) {

        rootNode.firstChild=tempNOde;
    } else {

        rootNode.sibling=tempNOde;
    }
    childNode.sibling = null;
    childNode.substr.startIndex = (short) (y);
    
   
    Indexes substr = new Indexes (arrayIndex, (short)y, (short)(allWords[arrayIndex].length()-1));
    TrieNode newLeaf = new TrieNode (substr, null, null);
    //check if sibling contains prefix?
    childNode.sibling = newLeaf;
}

private static int nodeValues (TrieNode root, String wordInPrefix, String[] allWords) {
    if (root.substr==null){
        return -1;
    }

    String word = allWords[root.substr.wordIndex];
    int tick=root.substr.startIndex;

    int end = root.substr.endIndex;
    int start = root.substr.startIndex;
    
    
    for (int z=start; z< Math.min(word.length(), wordInPrefix.length()); z++) {

        if (word.charAt(z)==wordInPrefix.charAt(z)) {
              tick++;
            continue;

        }
        break;
    }
   
    if (tick==start) return -1;
    else if (tick<end+1)return 1;
    else if (tick == end+1)return 0;
    else return 2;
    
}

 private static void newTrie (TrieNode currNode, String[] allWords, int locationOfIndex, TrieNode root) {
     int value = nodeValues(currNode, allWords[locationOfIndex], allWords);

     if (value==0) {
         if (currNode.firstChild==null) {
           
         }else {
            newTrie(currNode.firstChild, allWords, locationOfIndex, currNode);
         }
     } else if (value == -1) {
         if (currNode.sibling==null) {
             //create the substr of necessary prefix nodes
             Indexes substr;
             if (root.substr==null) {
                 substr = new Indexes (locationOfIndex, (short)0, (short)(allWords[locationOfIndex].length()-1));
             }else {
                 if (root.sibling == currNode) {
                     substr = new Indexes (locationOfIndex, (short)root.substr.startIndex, (short)(allWords[locationOfIndex].length()-1));
                 }else{
                     substr = new Indexes (locationOfIndex, (short)(root.substr.endIndex+1), (short)(allWords[locationOfIndex].length()-1));
                 }
             }
             
             TrieNode newLeaf = new TrieNode(substr, null, null);
             currNode.sibling = newLeaf;
         } else {
             if (currNode.sibling != null) {
                    newTrie(currNode.sibling, allWords, locationOfIndex, currNode);
             } else {
                 Indexes substr;
                 if (root.substr==null) {
                     substr = new Indexes(locationOfIndex, (short)0, (short)(allWords[locationOfIndex].length()-1));
                 }else {
                     if (root.sibling == currNode) {
                         substr = new Indexes(locationOfIndex, (short)root.substr.startIndex, (short)(allWords[locationOfIndex].length()-1));
                     }else{
                         substr = new Indexes(locationOfIndex, (short)(root.substr.endIndex+1), (short)(allWords[locationOfIndex].length()-1));
                     }                   
                    }
                 TrieNode newLeaf = new TrieNode (substr, null, null);
                 currNode.sibling = newLeaf;
             }
         }
     } else if (value == 1) alterNode (root, currNode, locationOfIndex, allWords);
      else {
        newTrie(currNode.firstChild, allWords, locationOfIndex, currNode);
     }
 }
 
 
public static ArrayList<TrieNode> completionList(TrieNode root, String[] allWords, String prefix) {
  ArrayList<TrieNode> list = new ArrayList<TrieNode>();
   TrieNode childNode = root;
  childNode = childNode.firstChild;
  TrieNode siblingNode = childNode;
  boolean isFound = false;
  boolean inFirstRow = false;
   while(childNode != null){
      while(siblingNode != null){
          //going down nodes. prefix = pot. currently in po
          if(prefix.contains(allWords[siblingNode.substr.wordIndex].substring(0,siblingNode.substr.endIndex+1)) && prefix.length() > allWords[siblingNode.substr.wordIndex].substring(0,siblingNode.substr.endIndex+1).length()){
              inFirstRow = true;
               childNode = siblingNode;
              break;
          }//prefix == prefix in node
          else if ((prefix.compareTo(allWords[siblingNode.substr.wordIndex].substring(0,siblingNode.substr.endIndex+1)) == 0) || (allWords[siblingNode.substr.wordIndex].substring(0,siblingNode.substr.endIndex+1).contains(prefix))){
             
               list = leafNodes(siblingNode,allWords, prefix, list);
              isFound = true;
              inFirstRow = true;
              if(isFound = true){
                  break;
              }
          }
          siblingNode = siblingNode.sibling;
      }
      if(isFound == true){
          break;
      }
      if(inFirstRow == false){
          return null;
      }
      childNode = childNode.firstChild;
      siblingNode = childNode;
  }
  if(isFound == false){
      return null;
  }
  return list;
}
public static ArrayList<TrieNode> leafNodes(TrieNode root,String[] allWords, String prefix, ArrayList list){
   if(root.firstChild == null && allWords[root.substr.wordIndex].contains(prefix)){
 
       list.add(root);
 
   }
  
   TrieNode childNode = root;
   childNode = childNode.firstChild;
   TrieNode siblingNode = childNode;
   boolean flag = false;
  
   while(childNode != null){
       while(siblingNode != null){
 
           if(siblingNode.firstChild == null && allWords[siblingNode.substr.wordIndex].contains(prefix)){
               flag = true;
               list.add(siblingNode);
           }else if((siblingNode.firstChild != null) && (prefix.compareTo(allWords[siblingNode.substr.wordIndex].substring(0,siblingNode.substr.endIndex+1)) == 0) || allWords[siblingNode.substr.wordIndex].substring(0,siblingNode.substr.endIndex+1).contains(prefix)){
               flag = true;
               list = leafNodes(siblingNode,allWords, prefix, list);
           }
           siblingNode = siblingNode.sibling;
          
       }
       if(flag = true){
           break;
       }
       childNode = childNode.firstChild;
       siblingNode = childNode;
   }
 
   return list;
}

 public static void print(TrieNode root, String[] allWords) {
     System.out.println("\nTRIE\n");
     print(root, 1, allWords);
 }
  private static void print(TrieNode root, int indent, String[] words) {
     if (root == null) {
         return;
     }
     for (int i=0; i < indent-1; i++) {
         System.out.print("    ");
     }
  
     if (root.substr != null) {
         String pre = words[root.substr.wordIndex]
                         .substring(0, root.substr.endIndex+1);
         System.out.println("      " + pre);
     }
  
     for (int i=0; i < indent-1; i++) {
         System.out.print("    ");
     }
     System.out.print(" ---");
     if (root.substr == null) {
         System.out.println("root");
     } else {
         System.out.println(root.substr);
     }
  
     for (TrieNode ptr=root.firstChild; ptr != null; ptr=ptr.sibling) {
         for (int i=0; i < indent-1; i++) {
             System.out.print("    ");
         }
         System.out.println("     |");
         print(ptr, indent+1, words);
     }
 }
}
 
 