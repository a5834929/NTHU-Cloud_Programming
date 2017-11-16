package query;

public class Page{
	private String pageName;
	private Double rank;

	public Page(){
		pageName = "";
		rank = 0.0;
	}
	
	public Page(String pageName, Double rank){
		this.pageName = pageName;
		this.rank = rank;
	}
	
	public String getPageName(){
		return pageName;
	}
	
	public Double getRank(){
		return rank;
	}
}
