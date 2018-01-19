package main;

import java.util.Date;

public class CacheReloader implements Runnable {
		private Thread thread;
		private String threadName;
		private long timeout;
		boolean end = false;

	public CacheReloader(long timeout) {
		this.threadName="Cache reloader";
		this.timeout=timeout;
	}

	@Override
	public void run() {
		try {
			do {
			Cache c = Cache.getInstance();
			long lastCacheUpdate = c.getLastCacheUpdate().getTime();
			long now = new Date().getTime();
			if(lastCacheUpdate+timeout < now) {
				System.out.println("MISE A JOUR DE DB!!");
				c.reload();
			}
			Thread.sleep(timeout*1000);
			}while(!end);
			
		}catch(InterruptedException ie) {
			System.err.println("Le thread de mise à jour du cache a été interompu");
			
		}

	}
	
	
	public void start() {
		System.out.println("Démarage du cache Reloader. Mise à jour toute les "+timeout+" secondes.");
		if(thread == null) {
			thread = new Thread(this, this.threadName);
			thread.start();
		}
		
	}

}
