//Name: Wachirayana Wanprasert
//Section: 3
//ID: 6088082

//Name: Chavanont Sakolpongpairoj
//Section: 3
//ID: 6088157

//Name: Panaya Sirilertworakul
//Section: 3
//ID: 6088164

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TFIDFSearcher extends Searcher
{	
	Map<String, Integer> cache = new HashMap<>();
	public TFIDFSearcher(String docFilename) {
		super(docFilename);
		/************* YOUR CODE HERE ******************/
		Map<String, Set<Integer>> termDocs = new HashMap<>();
		for(Document doc : documents) {
			for(String string : doc.getTokens()) {
				if(! termDocs.containsKey(string)) {
					termDocs.put(string,new HashSet<>());
				}
				termDocs.get(string).add(doc.getId());
			}
		}
		for(String i : termDocs.keySet()) {
			cache.put(i, termDocs.get(i).size());
		}
		/***********************************************/
	}
	
	@Override
	public List<SearchResult> search(String queryString, int k) {
		/************* YOUR CODE HERE ******************/
		List<String> tfidsearcher = tokenize(queryString);
		List<SearchResult> results = new ArrayList<>();
		for(Document doc : documents) {
			double similarity = 0;
			double dotProduct = 0.0;
			double normD = 0.0;
			double normQ = 0.0;
			double vectorD,vectorQ;
			
			List<String> union = Stream.concat(doc.getTokens().stream(), tfidsearcher.stream()).distinct().collect(Collectors.toList()); 
			
			for(String v : union) {
				vectorD = 0;
				vectorQ = 0;
				if(doc.getTokens().contains(v)) {
					vectorD = tfidf(v,doc.getTokens(),documents);
				}
				if(tfidsearcher.contains(v)) {
					vectorQ = tfidf(v,tfidsearcher, documents);
				}
				dotProduct += vectorD * vectorQ;
			    normD += Math.pow(vectorD, 2);
			    normQ += Math.pow(vectorQ, 2);
			}
			
			if(normD == 0 && normQ == 0) {
				similarity = 0.0 / 0.0;	//Not-a-Number
			}
			else {
				similarity = dotProduct/(Math.sqrt(normD*normQ));
			}

			results.add(new SearchResult(doc,similarity));
		}
		Collections.sort(results);
		return results.subList(0, k);
		/***********************************************/	
	}
	
	public double tfidf(String t, List<String> d, List<Document> docs) { 
		return tf(t,d) * idf(t,docs);
	}
	
	public double tf(String t, List<String> d) {	// tf = Term frequency
		int freq = 0;
		for(String word : d){
			if(t.equalsIgnoreCase(word)) {
				freq++;
			}
		}
		if(d.contains(t)) {
			return 1 + Math.log10(freq); 
		}
		else {
			return 0;
		}
	}
	
	public double idf(String t, List<Document> docs) {		//idf = Term Scarcity
		if(cache.containsKey(t)) {
			return Math.log10(1+1.0*docs.size()/cache.get(t));
			}
		int freq = 0;
		for(Document doc : docs) {
			if(doc.getTokens().contains(t)) {
				freq++;
			}
		}
		cache.put(t, freq);
		return Math.log10(1+1.0*docs.size()/freq );
	}
	
}
