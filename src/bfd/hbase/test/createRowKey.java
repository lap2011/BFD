package bfd.hbase.test;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class createRowKey extends Thread{
	private getRandmKey getRandmKey;

	private String rowKeyFile;
	private List<String> keyList = new ArrayList<String>();
	private int MIN = 5000;
	private int MAX = 10000;
	private int getIndex = 0;
	public FileReader fReader = null;
	public BufferedReader bf = null;
	public boolean isEnd = false;
	
	public createRowKey(getRandmKey getRandmKey,String rowKeyFile){
		this.getRandmKey = getRandmKey;
		this.rowKeyFile = rowKeyFile;
	}
	
	//异步的线程类读取文件，如果内存中，存数的量小于总量的一半的时候，就去读取
	public class GetkeyFromFile extends Thread{

		private String rowKey;
		
		public void run() {
			System.out.println("Start to get row from file to memory!");
			try {
				while (keyList.size() <= MAX) {
					rowKey = bf.readLine();
					if (rowKey == "" || rowKey == null){
						System.out.println("File is read end!");
						isEnd = true;
					} else {
						keyList.add(rowKey);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println(e);
			} finally {

			}
			System.out.println("End to get row from file to memory!");
		}
	}
	
	public void run(){
		boolean isRead = true;
		System.out.println("Start to create key!");
		
		try {
			fReader = new FileReader(new File(rowKeyFile));
			bf = new BufferedReader(fReader);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		while (isRead){	
			//如果文件已经读完且已经消费完，则推出
			if (isEnd && keyList.isEmpty()){
				System.out.println("Read to end!");
				isRead = false;
				continue;
			}
			
			//如果没有读完，则启动异步的线程去读取
			if (keyList.size() < MIN && !isEnd ){
				System.out.println("Start to update memory!");
				GetkeyFromFile getkeyFromFile = new GetkeyFromFile();
				getkeyFromFile.start();
			}
			
			//如果列表中不为空，则从中获取相应的值
			if (!keyList.isEmpty() ){
				getRandmKey.createRowKey(keyList.get(getIndex));
				keyList.remove(getIndex);
			} else {
				try {
					sleep(5000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		try {
			bf.close();
			fReader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}