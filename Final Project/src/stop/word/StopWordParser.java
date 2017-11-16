package stop.word;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class StopWordParser {
	
	public static void main(String[] args) throws Exception {
		String url = "http://www.ranks.nl/stopwords";
		StopWordParser.parse(url); 
	}
	
	public static void parse(String urlStr) throws Exception {
		//http://www.ranks.nl/stopwords
		Response response = Jsoup.connect(urlStr).followRedirects(true).execute();
		Document xmlDoc = response.parse();
		urlStr = response.url().toString();
		
		HashSet<String> stopWordSet = new HashSet<String>();
		Elements tdEle = xmlDoc.getElementsByTag("td");
		Pattern pattern = Pattern.compile("<td.*?>(.*?)</td>");	
		
		for(Element element : tdEle){
			String s = element.toString();
			Matcher matcher = pattern.matcher(s);
			if(matcher.find()){
				String[] array = matcher.group(1).split("<br>");
				for(String word:array)
					stopWordSet.add(word);
			}
		}
		ArrayList<String> wordList = new ArrayList<String>();
		for(String word:stopWordSet){
			//System.out.println(word);
			wordList.add(word);
		}
		Collections.sort(wordList);
		for(String word:wordList)
			System.out.println(word+"\t0");
	}
}
