package test;

import org.junit.Test;

import index.IndexUtil;

public class TestIndex {
	
	@Test
	public void testIndex(){
		IndexUtil iu = new IndexUtil();
		iu.index();
	}
	@Test
	public void testQuery(){
		IndexUtil iu = new IndexUtil();
		iu.query();
	}
	@Test
	public void testDelete(){
		IndexUtil iu = new IndexUtil();
		iu.delete();
	}
	@Test
	public void testunDelete(){
		IndexUtil iu = new IndexUtil();
		iu.undelete();
	}
	
	@Test
	public void testupdate(){
		IndexUtil iu = new IndexUtil();
		iu.update();
	}
	
	@Test
	public void testsearch01(){
		IndexUtil iu = new IndexUtil();
		iu.search01();
	}
	@Test
	public void testsearch02(){
		IndexUtil iu = new IndexUtil();
		for(int i=0;i<5;i++){
			iu.search02();
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("----------------");
			
		}
		
	}
}
