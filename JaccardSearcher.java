//Name: Wachirayana Wanprasert
//Section: 3
//ID: 6088082

//Name: Chavanont Sakolpongpairoj
//Section: 3
//ID: 6088157

//Name: Panaya Sirilertworakul
//Section: 3
//ID: 6088164

import java.text.DecimalFormat;
import java.util.*;

public class JaccardSearcher extends Searcher{
	//private static DecimalFormat df = new DecimalFormat("#.#################");
	public static List<String> term(List<String> query){
		Set<String> hash_Set = new HashSet<>();
		for(String token : query) {
			hash_Set.add(token);
		}
		List<String> list = new ArrayList<String>();
		list.addAll(hash_Set);
		return list;
	}
		

	public JaccardSearcher(String docFilename) {
		super(docFilename);
		/************* YOUR CODE HERE ******************/
		Map<String, Set<Integer>> termDocs = new HashMap<>();
		for(Document doc : documents) {
			for(String s : doc.getTokens()) {
				if(! termDocs.containsKey(s)) {
					termDocs.put(s,new HashSet<>());
				}
				termDocs.get(s).add(doc.getId());
				}
			}
		}
	
		/***********************************************/
	

	@Override
	public List<SearchResult> search(String queryString, int k) {
		/************* YOUR CODE HERE ******************/
//		Set<String> hash_Set = new HashSet<String>(); 
//		for(String token : hash_Set) {
//			hash_Set.add(token);
//		}
		List<String> query = term(tokenize(queryString));
		List<SearchResult> score = new ArrayList<>();
		for(Document doc : documents) {
			List<String> docList = term(doc.getTokens());
			int union = docList.size();
			int intersect = 0;
			double jaccard;
			for(String term:query) {
				if(docList.contains(term)) {
					intersect++;
				}
				else {
					union++;
				}
				
			}
			if(union == 0) {
				jaccard = 0;
			}
			else {
				jaccard = 1.0*intersect/union;//1.0 to make it decimal number	
			}
			SearchResult result = new SearchResult(doc,jaccard);
			score.add(result);
		}
		Collections.sort(score);
		return score.subList(0,k);
		

		/***********************************************/
	}
		

}
