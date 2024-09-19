package edu.uwb.css143.service;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class IndexerImpl implements Indexer {
    public Map<String, List<List<Integer>>> createIndex(List<String> docs) {
        Map<String, List<List<Integer>>> indexes = new HashMap<>();

        for (int i = 0; i < docs.size(); i++) {
            String doc = docs.get(i).trim().toLowerCase();
            if (doc.isEmpty()) {
                continue;
            }
            String[] words = doc.split("\\s+");

            List<List<Integer>> locationInDocs;
            for (int j = 0; j < words.length; j++) {
                String word = words[j];
                if (!indexes.containsKey(word)) {
                    locationInDocs = initializeLists(docs.size());
                    indexes.put(word, locationInDocs);
                } else {
                    locationInDocs = indexes.get(word);
                }

                // word appears in doc i at location j
                locationInDocs.get(i).add(j);
            }
        }
        return indexes;
    }

    private List<List<Integer>> initializeLists(int numOfDocs) {
        List<List<Integer>> list = new ArrayList<>();
        for (int i = 0; i < numOfDocs; i++) {
            list.add(new ArrayList<>());
        }
        return list;
    }
}
