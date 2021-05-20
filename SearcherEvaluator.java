//Name: Wachirayana Wanprasert
//Section: 3
//ID: 6088082

//Name: Chavanont Sakolpongpairoj
//Section: 3
//ID: 6088157

//Name: Panaya Sirilertworakul
//Section: 3
//ID: 6088164

import java.io.File;
import java.io.IOException;
import java.util.*;

import org.apache.commons.io.FileUtils;

public class SearcherEvaluator {
	private List<Document> queries = null;				//List of test queries. Each query can be treated as a Document object.
	private  Map<Integer, Set<Integer>> answers = null;	//Mapping between query ID and a set of relevant document IDs
	
	public List<Document> getQueries() {
		return queries;
	}

	public Map<Integer, Set<Integer>> getAnswers() {
		return answers;
	}

	/**
	 * Load queries into "queries"
	 * Load corresponding documents into "answers"
	 * Other initialization, depending on your design.
	 * @param corpus
	 */
	public SearcherEvaluator(String corpus)
	{
		String queryFilename = corpus+"/queries.txt";
		String answerFilename = corpus+"/relevance.txt";
		
		//load queries. Treat each query as a document. 
		this.queries = Searcher.parseDocumentFromFile(queryFilename);
		this.answers = new HashMap<Integer, Set<Integer>>();
		//load answers
		try {
			List<String> lines = FileUtils.readLines(new File(answerFilename), "UTF-8");
			for(String line: lines)
			{
				line = line.trim();
				if(line.isEmpty()) continue;
				String[] parts = line.split("\\t");
				Integer qid = Integer.parseInt(parts[0]);
				String[] docIDs = parts[1].trim().split("\\s+");
				Set<Integer> relDocIDs = new HashSet<Integer>();
				for(String docID: docIDs)
				{
					relDocIDs.add(Integer.parseInt(docID));
				}
				this.answers.put(qid, relDocIDs);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Returns an array of 3 numbers: precision, recall, F1, computed from the top *k* search results 
	 * returned from *searcher* for *query*
	 * @param query
	 * @param searcher
	 * @param k
	 * @return
	 */
	public double[] getQueryPRF(Document query, Searcher searcher, int k)
	{
		/*********************** YOUR CODE HERE *************************/
		List<SearchResult> results = searcher.search(query.getRawText(), k);
		Set<Integer> docIDs = new HashSet<>();
		for(SearchResult result : results) {
			docIDs.add(result.getDocument().getId());
		}

		Set<Integer> relevantID = answers.get(query.getId());
		int retrieved = docIDs.size();
		int relevant = relevantID.size();

		docIDs.retainAll(relevantID);
		double truepositive = docIDs.size();

//		double precision,recall,f1;
		double[] PRF = new double[3];

		PRF[0] = truepositive/(double)retrieved; //precision
		PRF[1] = truepositive/(double)relevant; //recall

		//f1
		if(PRF[0] + PRF[1] == 0) {
			PRF[2] = 0;
		}
		else {
			PRF[2] = (2 * PRF[0] * PRF[1]) / (PRF[0] + PRF[1]);
		}

		return PRF;
		/****************************************************************/
	}
	
	/**
	 * Test all the queries in *queries*, from the top *k* search results returned by *searcher*
	 * and take the average of the precision, recall, and F1. 
	 * @param searcher
	 * @param k
	 * @return
	 */
	public double[] getAveragePRF(Searcher searcher, int k)
	{
		/*********************** YOUR CODE HERE *************************/
		double[] average = new double[3];
		double[] array;
		for(Document query : getQueries()) {
			array = getQueryPRF(query,searcher,k);
			for (int i=0; i<3;i++){
				average[i] += array[i];
			}
		}
		for (int j=0;j<3;j++){
			average[j] /= getQueries().size();
		}

		return average;
		/****************************************************************/
	}
}
