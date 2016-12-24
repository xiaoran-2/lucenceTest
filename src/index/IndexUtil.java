package index;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.NumericField;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.util.Version;

public class IndexUtil {
	//创建6个文档
	private String[] ids = {"1","2","3","4","5","6"};
	private String[] emails = {"xiao1@126.com","xiao2@163.com","xiao3@qq.com","xiao4@qq.com","xiao5@qq.com","xiao6@126.com"};
	private String[] contents ={"xa xa ix ra on 1111","xa i rao n 2222","xa xa xa ira on 3333","xa irao n4444","xairaon 5555","xairao n6666"};
	private Date[] dates = null;
	private int[] attchs = {2,3,1,4,5,5};
	private String[] names = {"zahangsan","lisi","john","jetty","mike","jake"};
	private Directory directory = null;
	private Map<String,Float> scores = new HashMap<String,Float>();
	private static IndexReader reader = null;//生命周期问题
	
	public IndexUtil(){
		try {
			setDates();
			
			scores.put("126.com", 2.0f);
			scores.put("163.com", 2.5f);
			
			directory = FSDirectory.open(new File("E:/lucene/index02"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
	public IndexSearcher getSearcher(){
		try {
			if(reader == null){			
				reader = IndexReader.open(directory);
			}else{
				IndexReader tr = IndexReader.openIfChanged(reader);
				if(tr != null){//原始的reader发生了改变
					reader = tr;
				}
			}
			return new IndexSearcher(reader);
		} catch (CorruptIndexException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	private void setDates() {
		// TODO Auto-generated method stub
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		dates = new Date[ids.length];
		try {
			dates[0] =  sdf.parse("2010-11-1");
			dates[1] =  sdf.parse("2011-3-2");
			dates[2] =  sdf.parse("2012-11-1");
			dates[3] =  sdf.parse("2013-4-1");
			dates[4] =  sdf.parse("2014-12-1");
			dates[5] =  sdf.parse("2015-1-1");
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public void index(){
		IndexWriter writer = null;
		try {
			writer = new IndexWriter(directory, new IndexWriterConfig(Version.LUCENE_35, new StandardAnalyzer(Version.LUCENE_35)));
			writer.deleteAll();
			Document doc = null;
			for(int i=0;i<ids.length;i++){
				doc = new Document();
				doc.add(new Field("id", ids[i],Field.Store.YES,Field.Index.NOT_ANALYZED_NO_NORMS));
				doc.add(new Field("email",emails[i],Field.Store.YES,Field.Index.NOT_ANALYZED));
				doc.add(new Field("content",contents[i],Field.Store.NO,Field.Index.ANALYZED));
				doc.add(new Field("name",names[i],Field.Store.YES,Field.Index.NOT_ANALYZED_NO_NORMS));
				doc.add(new NumericField("attach",Field.Store.YES,true).setIntValue(attchs[i]));
				doc.add(new NumericField("date",Field.Store.YES,true).setLongValue(dates[i].getTime()));
				
				String et = emails[i].substring(emails[i].lastIndexOf("@")+1);
				System.out.println(et);
				if(scores.containsKey(et)){
					doc.setBoost(scores.get(et));
				}else{
					doc.setBoost(0.5f);
				}
				writer.addDocument(doc);
			}
		} catch (CorruptIndexException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (LockObtainFailedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(writer != null){
				try {
					writer.close();
				} catch (CorruptIndexException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	public void query(){
		try {
			//有效的获得文档的数量
			IndexReader reader = IndexReader.open(directory);
			System.out.println("numDocs"+reader.numDocs());
			System.out.println("maxDocs"+reader.maxDoc());
			System.out.println("deleteDocus"+reader.numDeletedDocs());
			reader.close();
		
		} catch (CorruptIndexException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void delete(){
		IndexWriter writer = null;
		
		try {
			writer = new IndexWriter(directory, new IndexWriterConfig(Version.LUCENE_35, new StandardAnalyzer(Version.LUCENE_35)));
			
			//参数是一个选项，可以是一个Query，也可以是一个Term，Term是一个精确查找的值
			//此时删除的文档，并不会完全删除，而是存储在回收站中，可以恢复
			writer.deleteDocuments(new Term("id","1"));
		
		} catch (CorruptIndexException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (LockObtainFailedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(writer != null)
				try {
					writer.close();
				} catch (CorruptIndexException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
		}
	}
	public void undelete(){
		//使用indexreade进行恢复
		IndexReader reader = null;

		try {
			reader = IndexReader.open(directory,false);
			//回恢复删除文件时，必须把IndexReader.open(directory,false);
			reader.undeleteAll();//恢复删除的文档
			reader.close();
			
		} catch (CorruptIndexException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void forceDelete(){
		IndexWriter writer = null;
		
		try {
			writer = new IndexWriter(directory, new IndexWriterConfig(Version.LUCENE_35, new StandardAnalyzer(Version.LUCENE_35)));
			writer.forceMergeDeletes();
			
			
		} catch (CorruptIndexException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (LockObtainFailedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(writer != null)
				try {
					writer.close();
				} catch (CorruptIndexException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	}
	public void merge(){
		IndexWriter writer = null;
		
		try {
			writer = new IndexWriter(directory, new IndexWriterConfig(Version.LUCENE_35, new StandardAnalyzer(Version.LUCENE_35)));
			//会将索引合并为两段，这两端中被删除的的数据，会被清空，此处在3.5之后不建议使用，因为会消耗大量的开销，
			//lucene会根据清空自动删除
			writer.forceMerge(2);
			writer.commit();
		} catch (CorruptIndexException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (LockObtainFailedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(writer != null)
				try {
					writer.close();
				} catch (CorruptIndexException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	}
	public void update(){
		IndexWriter writer = null;
		
		try {
			writer = new IndexWriter(directory, new IndexWriterConfig(Version.LUCENE_35, new StandardAnalyzer(Version.LUCENE_35)));
			
			/*
			 * lucene并没有提供更新操作，这里只是进行如下两个操作
			 * 先删除在进行添加
			 */
			Document doc = new Document();
			doc.add(new Field("id", "111",Field.Store.YES,Field.Index.NOT_ANALYZED_NO_NORMS));
			doc.add(new Field("email",emails[0],Field.Store.YES,Field.Index.NOT_ANALYZED));
			doc.add(new Field("content",contents[0],Field.Store.NO,Field.Index.ANALYZED));
			doc.add(new Field("name",names[0],Field.Store.YES,Field.Index.NOT_ANALYZED_NO_NORMS));
			
			
			writer.updateDocument(new Term("id","1"),doc);

			
		} catch (CorruptIndexException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (LockObtainFailedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(writer != null)
				try {
					writer.close();
				} catch (CorruptIndexException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	}
	
	public void search01(){
		try {
			IndexReader reader = IndexReader.open(directory);
			IndexSearcher searcher = new IndexSearcher(reader);
			TermQuery query = new TermQuery(new Term("content","xa"));
			TopDocs tds = searcher.search(query, 10);
			System.out.println(tds.getMaxScore());
			for (ScoreDoc sd:tds.scoreDocs){
				Document doc = searcher.doc(sd.doc);
				System.out.println("("+sd.doc +")"+doc.getBoost()+"!!!"+sd.score+"+++"+doc.get("name")+"["+doc.get("email")+"]"+doc.get("id")+
						doc.get("attach")+","+new Date(Long.parseLong(doc.get("date"))));
				
			}
			reader.close();
		
		} catch (CorruptIndexException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void search02(){
		try {
			IndexSearcher searcher = getSearcher();
			TermQuery query = new TermQuery(new Term("content","xa"));
			TopDocs tds = searcher.search(query, 10);
			System.out.println(tds.getMaxScore());
			for (ScoreDoc sd:tds.scoreDocs){
				Document doc = searcher.doc(sd.doc);
				System.out.println(doc.get("id")+"!!!"+sd.score+"+++"+doc.get("name")+"["+doc.get("email")+"]"+doc.get("id")+
						doc.get("attach")+","+new Date(Long.parseLong(doc.get("date"))));
				
			}
			searcher.close();
			
		} catch (CorruptIndexException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	
}	
