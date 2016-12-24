package test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Readfile {
	
	public static void main(String[] args) {
		try {
			
			//读文件
			FileReader reader = new FileReader("F:/WT10G/Query451-550.topics");
			BufferedReader br = new BufferedReader(reader);
			
			//写文件
            FileWriter writer = new FileWriter("F:/WT10G/Result451-550.txt");
            BufferedWriter bw = new BufferedWriter(writer);
            String s = null;
            while((s = br.readLine()) != null){
            	System.out.println(s.substring(4));
            	bw.write(s);
            	bw.newLine();
            }
            br.close();
			bw.close();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
	}
	
}
