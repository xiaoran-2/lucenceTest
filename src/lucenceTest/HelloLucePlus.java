package lucenceTest;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.Date;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.util.Version;
import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.parser.html.HtmlParser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

public class HelloLucePlus {
	static private final long LEN = 1351970;
	
	public String fileToTxt(File f){
		//鑷姩鎵惧埌涓�釜鏈�悎閫傜殑绫诲瀷杩涜瑙ｆ瀽
		Parser parser = new HtmlParser();
		InputStream is = null;
		try {
			Metadata metadata = new Metadata();
			
			
			is = new FileInputStream(f);
			ContentHandler hander = new BodyContentHandler(10000000);
			ParseContext context = new ParseContext();
			context.set(Parser.class, parser);
			
			
			parser.parse(is, hander, metadata, context);
			metadata.set("id", f.getPath());
			
			
			for(String name:metadata.names()){
				System.out.println(name+"-----"+metadata.get(name));
			}
			
			return hander.toString();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (TikaException e) {
			e.printStackTrace();
		}finally{
			if(is != null)
				try {
					is.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
		}
		return null;
	}
	/*
	 * 寤虹珛绱㈠紩
	 */
	public void index(){	
		IndexWriter writer = null;
		try {
			//1銆佸垱寤篋irectory
//			Directory directory  = new RAMDirectory();//寤虹珛鍦ㄥ唴瀛樹腑
			
			Directory directory = FSDirectory.open(new File("E:/lucene/ucasIR_IndexPlus1"));//绱㈠紩瀛樺湪纭洏涓�
			//2銆佸垱寤篒ndexWriter
			IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_35,new StandardAnalyzer(Version.LUCENE_35));
			writer = new IndexWriter(directory, iwc);
			//3銆佸垱寤篋ocument瀵硅薄
			Document doc = null;
			//4銆佷负姣忎竴涓狣ocument娣诲姞Filed锛寋澶у皬锛岃矾寰勶紝鐩綍}
//			File f = new File("E:/lucene/ucasIRPlus");
			 
			File f = new File("E:/lucene/ucasPlus/4");

//			long len = f.list().length;					
//			System.out.println(len);
			System.out.println("-------------");
			int j=0;
			for(File file: f.listFiles()){
//			for(int i = 0;i<LEN;i++){
//				System.out.println(i);
				doc = new Document();
//				File file = f.listFiles()[i];
				
//				System.out.println(file.getAbsoluteFile());
				doc.add(new Field("content",new Tika().parse(file.getAbsoluteFile())));//鏂囦欢鐨勫唴瀹�
				doc.add(new Field("filename",file.getName(),Field.Store.YES,Field.Index.NOT_ANALYZED));
//				doc.add(new Field("path",file.getPath(),Field.Store.YES,Field.Index.NOT_ANALYZED));

				//5銆侀�杩嘔ndexWriter娣诲姞鏂囨。鍒�
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
			if(writer!=null){
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

	/*
	 * 建立全部索引
	 */
	public void indexAll(){	
		long start = new Date().getTime();
		for(int k=5;k<=19;k++){
			System.out.println(k);
			IndexWriter writer = null;
			try {
				//1銆佸垱寤篋irectory
	//			Directory directory  = new RAMDirectory();//寤虹珛鍦ㄥ唴瀛樹腑
				
				Directory directory = FSDirectory.open(new File("E:/lucene/ucasIR_IndexPlus1"));//绱㈠紩瀛樺湪纭洏涓�
				//2銆佸垱寤篒ndexWriter
				IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_35,new StandardAnalyzer(Version.LUCENE_35));
				writer = new IndexWriter(directory, iwc);
				//3銆佸垱寤篋ocument瀵硅薄
				Document doc = null;
				//4銆佷负姣忎竴涓狣ocument娣诲姞Filed锛寋澶у皬锛岃矾寰勶紝鐩綍}
	//			File f = new File("E:/lucene/ucasIRPlus");
				
				File f = new File("E:/lucene/ucasPlus/"+k);
				
	//			long len = f.list().length;					
	//			System.out.println(len);
				System.out.println("-------------");
				int j=0;
				for(File file: f.listFiles()){
	//			for(int i = 0;i<LEN;i++){
	//				System.out.println(i);
					doc = new Document();
	//				File file = f.listFiles()[i];
					
	//				System.out.println(file.getAbsoluteFile());
					doc.add(new Field("content",new Tika().parse(file.getAbsoluteFile())));//鏂囦欢鐨勫唴瀹�
					doc.add(new Field("filename",file.getName(),Field.Store.YES,Field.Index.NOT_ANALYZED));
	//				doc.add(new Field("path",file.getPath(),Field.Store.YES,Field.Index.NOT_ANALYZED));
					
					//5銆侀�杩嘔ndexWriter娣诲姞鏂囨。鍒�
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
				if(writer!=null){
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
		long end = new Date().getTime();
		
		System.out.println("时间分钟："+(end-start)/60000);
		
	}
	
	/*
	 * 鎼滅储
	 */
	public void searcher(){
		//1銆佸垱寤篋irectory
		Directory directory=null;
		try {
			directory = FSDirectory.open(new File("E:/lucene/ucasIR_IndexPlus1"));//绱㈠紩瀛樺湪纭洏涓�
			//2銆佸垱寤篒ndexReader
			IndexReader reader= IndexReader.open(directory);
			
			//3銆佹牴鎹甀ndexReader鍒涘缓IndexSearcher
			IndexSearcher searcher = new IndexSearcher(reader);
			//4銆佸垱寤烘悳绱㈢殑Query
			QueryParser parser = new QueryParser(Version.LUCENE_35, "content", new StandardAnalyzer(Version.LUCENE_35));
			Query query = parser.parse("under page");
			//5銆佹牴鎹畇earcher鎼滅储骞惰繑鍥瀟opCase
			TopDocs tds = searcher.search(query,25);
			//6銆佹牴鎹畉opCase鑾峰彇ScoreDoc瀵硅薄
			ScoreDoc[] sds = tds.scoreDocs;
			System.out.println(sds.length);
			query.getBoost();
			for(ScoreDoc sd: sds){
				//7銆佹牴鎹畇earcher鍜孲coreDoc鑾峰緱鍏蜂綋鐨凞ocument鐨勫璞�
				Document d = searcher.doc(sd.doc);
				
				//8銆佹牴鎹瓺ocument瀵硅薄鑾峰緱闇�鐨勫�
				System.out.println(d.get("filename")+" "+sd.score);
			}
			//9銆佸叧闂璻eader
			reader.close();
			
		} catch (IOException | ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/*
	 *搜索所有的查询语句，并讲排名最高的25个返回文档的ID写入文件
	 */
	public void searcherAll(){
		//1銆佸垱寤篋irectory
		Directory directory=null;
		try {
			directory = FSDirectory.open(new File("E:/lucene/ucasIR_IndexPlus1"));//绱㈠紩瀛樺湪纭洏涓�
			//2銆佸垱寤篒ndexReader
			IndexReader reader= IndexReader.open(directory);
			
			//3銆佹牴鎹甀ndexReader鍒涘缓IndexSearcher
			IndexSearcher searcher = new IndexSearcher(reader);
			//4銆佸垱寤烘悳绱㈢殑Query
			QueryParser parser = new QueryParser(Version.LUCENE_35, "content", new StandardAnalyzer(Version.LUCENE_35));
			
			//打开文件的读写流
			FileReader read = new FileReader("F:/WT10G/Query451-550.topics");
			BufferedReader br = new BufferedReader(read);
			
			//写文件
            FileWriter writer = new FileWriter("F:/WT10G/Result451-550_50.txt");
            BufferedWriter bw = new BufferedWriter(writer);
            
            Query query = null;
            String query_s = null;
            StringBuffer sb = null;
			
            //按行读取文件，进行搜索
            while((query_s = br.readLine()) != null){
            	int end = query_s.length();
            	if(query_s.indexOf('?') != -1){
            		end = query_s.indexOf('?');
            	}
            	System.out.println(query_s.substring(4,end));
            	
            	query = parser.parse(query_s.substring(4,end));
				
				TopDocs tds = searcher.search(query,50);
				ScoreDoc[] sds = tds.scoreDocs;
				System.out.println(sds.length);
				query.getBoost();
				int i = 1;
				
				//写入文件的内容
				for(ScoreDoc sd: sds){
					Document d = searcher.doc(sd.doc);
					sb = new StringBuffer();
					
					sb.append(query_s.substring(0,3)+" ");//添加查询ID
					sb.append((i++)+" ");//添加排序顺序
					sb.append(d.get("filename")+" ");//添加查询得到的文档的ID
					sb.append(sd.score);//文档得分
					
					bw.write(sb.toString());
					bw.newLine();//换行
//					System.out.println(d.get("filename")+" "+sd.score);
				}
            	            	
            }
            
            //关闭所有的流
            bw.close();
            br.close();
			reader.close();
			
		} catch (IOException | ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
		
}
