package lucenceTest;

import org.junit.Test;

public class TestLucene {
	@Test
	public void testIndex(){
		HelloLecene h1 = new HelloLecene();
		h1.index();
	}
	@Test
	public void testIndexAll(){
		HelloLecene h1 = new HelloLecene();
		h1.indexAll();
	}
	@Test
	public void testSearch(){
		
		HelloLecene h1 = new HelloLecene();
		h1.searcher();
	}
	@Test
	public void testSearchAll(){
		
		HelloLecene h1 = new HelloLecene();
		h1.searcherAll();
	}
	
	//写入到文件
	@Test
	public void testWriteSearchAll(){
		
		HelloLucePlus h1 = new HelloLucePlus();
		h1.searcherAll();
	}
	
	
}
