package bfd.hbase.test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.PosixParser;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;

public class bfdTestHbase {
	
	private static String tableName = "TestTable";
	private static int threadCount = 1;
	private TestOptions testOptions =  new TestOptions();
	private static Configuration conf;
	
	@SuppressWarnings("static-access")
	public Options buildOptions() {
		Options options = new Options();
		
		options.addOption(OptionBuilder.withLongOpt("thread-number")
				.hasArg().withDescription("run with thread count").create("n"));
		
		options.addOption(OptionBuilder.withLongOpt("table-name")
				.hasArg().withDescription("operation table name").create("b"));

		options.addOption(OptionBuilder.withLongOpt("each-thread-total")
				.hasArg().withDescription("each read total count").create("e"));
		
		options.addOption(OptionBuilder.withLongOpt("type")
				.hasArg().withDescription("if count is true").create("t"));
		
		options.addOption(OptionBuilder.withLongOpt("each-time-read")
				.hasArg().withDescription("each read count").create("r"));
		
		options.addOption(OptionBuilder.withLongOpt("rowkey-file")
				.hasArg().withDescription("row key file name").create("f"));
		
		options.addOption(OptionBuilder.withLongOpt("cf-name")
				.hasArg().withDescription("cloumn family name").create("c"));
		return options;
	}
	
	public void parseOptions(String[] args) throws Exception{
		CommandLineParser parser = new PosixParser();
		Options options = buildOptions();
		CommandLine cmd = parser.parse(options, args);
		
		if (cmd.hasOption("thread-number")){
			threadCount = Integer.parseInt(cmd.getOptionValue("thread-number"));
		}
		if (cmd.hasOption("table-name")){
			tableName = cmd.getOptionValue("table-name");
			testOptions.tableName = tableName;
		}
		if (cmd.hasOption("each-thread-total")){
			testOptions.requeryCount = Integer.parseInt(cmd.getOptionValue("each-thread-total"));
		}
		if (cmd.hasOption("type") ){
			testOptions.readFromCount = Boolean.parseBoolean(cmd.getOptionValue("type"));
		}
		if (cmd.hasOption("each-time-read")){
			testOptions.eachTimeGet = Integer.parseInt(cmd.getOptionValue("each-time-read"));
		}
		if (cmd.hasOption("rowkey-file")){
			testOptions.rowkeyFile = cmd.getOptionValue("rowkey-file");
		}
		if (cmd.hasOption("cf-name")){
			testOptions.cfName = cmd.getOptionValue("cf-name");
		}
		
		/**
		threadCount = 4;
		tableName = "lapNe";
		testOptions.tableName = "lapNe";
		testOptions.cfName = "info";
		testOptions.requeryCount = 4;
		testOptions.readFromCount = true;
		testOptions.eachTimeGet = 1;
		testOptions.rowkeyFile = "test.txt";
		**/
	}
	
	@SuppressWarnings("unchecked")
	public void execTest() throws Exception{
		long time = 0;
		
		//create row key
		getRandmKey getRandmKey = new getRandmKey();
		createRowKey createRowKey = new createRowKey(getRandmKey, testOptions.rowkeyFile);
		createRowKey.start();
		
		ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
		List<Future<Object>>  allReadThread = new ArrayList<Future<Object>>();
		for ( int i = 0; i < threadCount; i++){
			System.out.print(testOptions.toString() + "thread count " + threadCount + "\n");
			Future<Object> future = executorService.submit(new localClientThread(testOptions,conf,getRandmKey));
			allReadThread.add(future);
		}
		
		for (Future<Object> future : allReadThread){
			try{
				String returnValue = future.get().toString();
				if (returnValue != null ){
					time += Long.parseLong(future.get().toString());
				}
			} catch (Exception e){
				System.out.println("get thread error and execption:" + e);
			} finally{
				executorService.shutdown();
			}
		}
		System.out.println("At  Last  total time:" + time);
	}
	
	public void init(){
		conf = HBaseConfiguration.create();
		conf.addResource("conf/hbase-site.xml");
	}
	
	//-n 4 -b lap -e 40 -t true -r 4 -f test.txt
	public static void main(String[] args){
		bfdTestHbase bfdTestHbase = new bfdTestHbase();
		bfdTestHbase.init();
		try {
			bfdTestHbase.parseOptions(args);
			bfdTestHbase.execTest();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
