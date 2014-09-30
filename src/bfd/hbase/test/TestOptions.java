package bfd.hbase.test;

import org.apache.hadoop.conf.Configuration;

public class TestOptions {
	
	public TestOptions(){}
	
	public TestOptions(TestOptions testOptions){
		this.tableName = testOptions.tableName;
		this.cfName = testOptions.cfName;
		this.rowkeyFile = testOptions.rowkeyFile;
		this.requeryCount = testOptions.requeryCount;
		this.readTime = testOptions.readTime;
		this.eachTimeGet = testOptions.eachTimeGet;
		this.readFromCount = testOptions.readFromCount;
		this.qualifiter = testOptions.qualifiter;
		this.showDetail = testOptions.showDetail;
	}
	
	public String toString(){
		String resString = "Get Args : tableName :" + tableName + "\n"
				           +  "           cfName   :" + cfName + "\n"
				           +  "          rowkeyFile:" + rowkeyFile + "\n"
				           +  "       requeryCount :" + requeryCount + "\n"
				           +  "        readTime    :" + readTime + "\n"
				           +  "         eachTimeGet:" + eachTimeGet + "\n"
				           +  "       readFromCount:" + readFromCount + "\n"
				           +  "        qualifiter  :" + qualifiter + "\n"
				           +  "          showDetail:" + showDetail + "\n"
				           +  "           cfName   :" + cfName + "\n";
		return resString;
	}
	
	public String tableName = "TestTable";
	public String cfName = "up";
	public String qualifiter = "gid";
	public String rowkeyFile = "";
	public int requeryCount = 100*10000;
	public int readTime = 60; 
	public int eachTimeGet = 1;
	public boolean readFromCount = true;
	public boolean showDetail = true;
}
