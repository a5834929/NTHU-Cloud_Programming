
import java.io.FileWriter;
import java.net.URL;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;


public class Test {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
			Parsing();
	}
	public static void Parsing() throws Exception {
		FileWriter fw = new FileWriter("1-10000.txt");
		for(int s=1, e=1000;s<=9001;s+=1000, e+=1000){
			URL url = new URL("http://en.wiktionary.org/wiki/Wiktionary:Frequency_lists/TV/2006/"+s+"-"+e); 
			Document xmlDoc =  Jsoup.parse(url, 3000); //使用Jsoup jar 去解析網頁
						
			Elements trs = xmlDoc.select("table tr");
			for(Element tr : trs){
				List<Node> nodes = tr.childNodes();
				Elements td = tr.getElementsByTag("td");
				Element wordTd = td.get(1);
				if(!wordTd.getElementsByTag("a").hasAttr("class"))
					fw.write(td.get(1).text().toLowerCase()+"\t"+td.get(2).text()+"\n");
				
			}
		}				
		

        fw.flush();
        fw.close();
        System.out.println("done");
		
	}
}


//Elements title = xmlDoc.select("title");
/*Elements title = xmlDoc.getElementsByClass("pg-headline");
Elements articleBody = xmlDoc.getElementsByAttributeValue("itemprop", "articleBody");
Elements paragraph = xmlDoc.getElementsByClass("zn-body__paragraph"); 
//System.out.println(paragraph.size());
for(Element element : paragraph)
	System.out.println(element.text()+"\n");*/