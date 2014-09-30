package bfd.hbase.test;


public class getRandmKey {
	private String rowkey;
	private boolean isReady = false;

	public synchronized String getRowKey(){
		while (isReady == false) {
			try {
				wait();
			} catch (InterruptedException e) {
				System.out.println(e);
			}
		}
		isReady = false;
		notifyAll();
		return rowkey;
	}
	
	public synchronized void createRowKey(String rowkey){
		while (isReady) {
			try {
				wait();
			} catch (InterruptedException e) {
				System.out.println(e);
			}
		}
		this.rowkey = rowkey;
		isReady = true;
		notifyAll();
	}
}
