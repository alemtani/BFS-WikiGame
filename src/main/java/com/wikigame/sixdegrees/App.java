package com.wikigame.sixdegrees;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Hello world!
 *
 */
public class App 
{
	/**
	 * Converts parameters for API request into a valid URL
	 * @param params
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String getParamsString( Map<String, String> params ) throws UnsupportedEncodingException
	{
		StringBuilder result = new StringBuilder();

		for (Map.Entry<String, String> entry : params.entrySet()) {
			result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
			result.append("=");
			result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
			result.append("&");
		}

		String resultString = result.toString();
		return resultString.length() > 0
				? resultString.substring(0, resultString.length() - 1)
						: resultString;
	}

	/**
	 * Makes API request for internal links to WikiMedia API
	 * API responds with links in the article denoted with the title
	 * @param title
	 * @return
	 * @throws Exception
	 */
	public static StringBuffer requestData( String title ) throws Exception
	{
		URL url = new URL("https://en.wikipedia.org/w/api.php");
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestMethod("GET");
		
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("action", "parse");
		parameters.put("page", title);
		parameters.put("prop", "links");
		parameters.put("format", "json");

		con.setDoOutput(true);
		DataOutputStream out = new DataOutputStream(con.getOutputStream());
		out.writeBytes(getParamsString(parameters));
		out.flush();
		out.close();

		BufferedReader in = new BufferedReader(
				new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer content = new StringBuffer();
		while ((inputLine = in.readLine()) != null) {
			content.append(inputLine);
		}
		in.close();

		con.disconnect();

		return content;
	}
	
	/**
	 * Implements breadth-first search to get from one Wikipedia article to the next
	 * While guarantees a lowest-cost path can also be very inefficient and time-consuming
	 * @param args
	 * @throws Exception
	 */
	public static void main( String[] args ) throws Exception
	{
		String start = "Adolf Hitler"; // Change start article here
		String target = "Middle Ages"; // Change target article here

		// Store a queue of nodes as possible routes in reverse order to access origin from end node
		// Set stores visited articles so to avoid cyclic searching
		Queue<Node> paths = new LinkedList<Node>();
		Set<String> visited = new HashSet<String>();
		
		paths.add(new Node(start));
		visited.add(start);
		
		boolean foundPath = false;
		Node path = null;
		
		while (!paths.isEmpty()) {
			// Take first path from queue
			path = paths.poll();
			String title = path.val;
			
			// Once the article reaches the target the path has been found
			if (title.equalsIgnoreCase(target)) {
				foundPath = true;
				break;
			}
			
			// Get the response body for the parsing of the links of the article
			JSONArray links = new JSONObject(requestData(title).toString())
					.getJSONObject("parse")
					.getJSONArray("links");
			
			// Iterate through the links and add to queue if not in visited
			for (int i = 0; i < links.length(); i++) {
				JSONObject link = links.getJSONObject(i);
				if (link.getInt("ns") == 0 && link.has("exists")) {
					String tag = link.get("*").toString();
					if (!visited.contains(tag)) {
						paths.offer(new Node(tag, path));
						visited.add(tag);
					}
				}
			}
		}

		if (foundPath) {
			// Reverse linked list to get path in correct order: start --> end
			Node node = path;
			while (node.next != null) {
				Node temp = path;
				path = node.next;
				node.next = node.next.next;
				path.next = temp;
			}
			
			while (path != null) {
				System.out.println(path.val);
				path = path.next;
			}
		} else {
			System.out.println("No path found");
		}
	}
}
