package edu.uwb.css143.service;

import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SearcherImpl implements Searcher {
    /*
    Extra credit
    this search won't work. why?
    The problem won't work because it .contains searches by substring. In this situation you do not want to use this.
     */
    // public List<Integer> search(String keyPhrase, List<String> docs) {
    //
    //     List<Integer> result = new ArrayList<>();
    //
    //     for (int i = 0; i < docs.size(); i++) {
    //         if (docs.get(i).contains(keyPhrase)) {
    //             result.add(i);
    //         }
    //     }
    //     return result;
    // }

    // a naive search
    // DO NOT CHANGE
    public List<Integer> search(String searchPhrase, List<String> docs) {
        List<Integer> result = new ArrayList<>();
        String[] searchWords = searchPhrase.trim().toLowerCase().split("\\s+");

        // search in each doc for consecutive matches of each word in the search phrase
        for (int i = 0; i < docs.size(); i++) {
            String doc = docs.get(i).trim();
            if (doc.isEmpty()) {
                continue;
            }
            String[] wordsInADoc = doc.split("\\s+");

            for (int j = 0; j < wordsInADoc.length; j++) {
                boolean matchFound = true;
                for (int k = 0; k < searchWords.length; k++) {
                    if (j + k >= wordsInADoc.length || !searchWords[k].equals(wordsInADoc[j + k])) {
                        matchFound = false;
                        break;
                    }
                }
                if (matchFound) {
                    result.add(i);
                    break;
                }
            }
        }
        return result;
    }

    /*
     Raymod, Ramazan, Rashid
      */
    public List<Integer> search(String searchPhrase, Map<String, List<List<Integer>>> index) {
        List<Integer> searchResult = new ArrayList<>();// do not change
        if (searchPhrase == null || searchPhrase.trim().isEmpty()) {
            return searchResult;
        }

        String[] searchPhraseWords = searchPhrase.trim().toLowerCase().split("\\s+");
        Set<Integer> commonDocs = getCommonDocuments(searchPhraseWords, index);

        for (Integer doc : commonDocs) {
            if (hasConsecutiveMatch(doc, searchPhraseWords, index)) {
                searchResult.add(doc);
            }
        }

        Collections.sort(searchResult);
        return searchResult;
    }

    private Set<Integer> getCommonDocuments(String[] searchPhraseWords, Map<String, List<List<Integer>>> index) {
        Set<Integer> commonDocuments = null;

        for (String word : searchPhraseWords) {
            if (!index.containsKey(word)) {
                return new HashSet<>();
            }

            Set<Integer> docIds = new HashSet<>();
            List<List<Integer>> wordLocations = index.get(word);
            for (int docId = 0; docId < wordLocations.size(); docId++) {
                if (!wordLocations.get(docId).isEmpty()) {
                    docIds.add(docId);
                }
            }

            if (commonDocuments == null) {
                commonDocuments = docIds;
            } else {
                commonDocuments.retainAll(docIds);
            }
        }

        return commonDocuments != null ? commonDocuments : new HashSet<>();
    }

    private boolean hasConsecutiveMatch(int doc, String[] searchPhraseWords, Map<String, List<List<Integer>>> index) {
        List<List<Integer>> locations = new ArrayList<>();
        for (String word : searchPhraseWords) {
            List<List<Integer>> wordLocations = index.get(word);
            if (wordLocations == null || wordLocations.size() <= doc) {
                return false;
            }
            locations.add(wordLocations.get(doc));
        }

        return hasMatch(locations);
    }

    private boolean hasMatch(List<List<Integer>> locations) {
        if (locations.isEmpty()) {
            return false;
        }

        Map<Integer, Integer> differenceCount = new HashMap<>();
        for (int i = 0; i < locations.size(); i++) {
            for (Integer position : locations.get(i)) {
                int difference = position - i;
                differenceCount.put(difference, differenceCount.getOrDefault(difference, 0) + 1);
            }
        }

        for (Integer count : differenceCount.values()) {
            if (count == locations.size()) {
                return true;
            }
        }

        return false;
    }

    /*
    Extra credit
     "a programmer is explaining what you do for a living non tech people often stare blankly unable"
      Found: "a programmer is explaining what you do for a living non tech people often stare blankly unable" in document [44]
      My search took 98061 nanoseconds.
      Naive search took 407692 nanoseconds.

      "the"
      Found: "the" in document [0, 1, 2, 3, 4, 5, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 18, 19, 20, 21, 23, 24, 25, 26, 28, 29, 30, 31, 32, 33, 34, 37, 38, 39, 41, 42, 44, 45, 46, 47, 48]
      My search took 68521 nanoseconds.
      Naive search took 364500 nanoseconds.

      "you are"
      Found: "you are" in document [34, 48]
      My search took 51578 nanoseconds.
      Naive search took 380165 nanoseconds.
     */
}
