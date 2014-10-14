package bfd.hbase.test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import org.apache.hadoop.conf.Configuration;
//import org.apache.hadoop.hbase.Cell;
//import org.apache.hadoop.hbase.CellScanner;
//import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;


@SuppressWarnings("rawtypes")
public class localClientThread implements Callable{
	
	private TestOptions testOptions;
	private getRandmKey getRandmKey;
	private Configuration conf;
	
	public localClientThread() {
		// TODO Auto-generated constructor stub
	}
	
	public localClientThread(TestOptions testOptions,Configuration conf,getRandmKey getRandmKey){
		this.testOptions = testOptions;
		this.getRandmKey = getRandmKey;
		this.conf = conf;
	}
	
	public String getRandomRowKey(){
		return getRandmKey.getRowKey();
	}
	
	//gid reverse
	public static String reverString(String str) {
		StringBuffer stringBuffer = new StringBuffer(str);
		return stringBuffer.reverse().toString();
	}

	@Override
	public Object call() throws Exception {
		long execTime = 0; 
		
		System.out.println("Will get talbe :" + testOptions.tableName);
		
		HTable hTable = new HTable(conf, testOptions.tableName);
		List <Get> allRows = new ArrayList<Get>();
		for (int j = 0; j < testOptions.requeryCount; j += testOptions.eachTimeGet) {
			if ( testOptions.eachTimeGet == 1 ){
				String currentRowKey = reverString(getRandomRowKey());
				System.out.println(Thread.currentThread().getId() + " get rowkey: " + currentRowKey);
				if (currentRowKey == null ){
					continue;
				}
				Get get = new Get(Bytes.toBytes(currentRowKey));
				get.setMaxVersions(1);
				get.addFamily(testOptions.cfName.getBytes());
				Result result;
				long start = System.nanoTime();
				try{
					result = hTable.get(get);
				}catch (Exception e) {
					System.out.println("when get rowkey:"+ currentRowKey + "get exception:"+ e.toString());
					continue;
					// TODO: handle exception
				}
				execTime += System.nanoTime() - start;
				if (result.containsColumn(testOptions.cfName.getBytes(), testOptions.qualifiter.getBytes())){
					System.out.println("Row key:" + currentRowKey + "is not in table");
				}
				if (testOptions.showDetail) {
//					System.out.println("get:"+result.toString());
//					for (Cell cell : result.rawCells()) {
//						System.out.println("get row name:"+ new String(CellUtil.cloneRow(cell) + ""));
//						System.out.println("get time stamp"+ cell.getTimestamp());
//						System.out.println("column Family:"+ new String(CellUtil.cloneFamily(cell)) + " ");
//						System.out.println("row Name:" + new String(CellUtil.cloneQualifier(cell)) + " ");
//						System.out.println("value:" + new String(CellUtil.cloneValue(cell)) + " ");
//					}
				}
			} else {
				allRows.clear();
				for (int i = 0; i < testOptions.eachTimeGet; i++) {
					Get get = new Get(Bytes.toBytes(getRandomRowKey()));
//					get.setFilter(filter)
					allRows.add(get);
				}
				long start = System.nanoTime();
				hTable.get(allRows);
				execTime += System.nanoTime() - start;
			}
		}
		hTable.close();
		execTime /= 1000000;
		System.out.println("Client-"+Thread.currentThread().getId()+": total time:"+execTime+"\n");
		return execTime;
	}

}
