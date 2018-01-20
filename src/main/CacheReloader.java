package main;

import java.util.Date;

public class CacheReloader implements Runnable {
	/**
	 * Le but de cette classe est de permettre la mise à jour régulière du cache.
	 * Pour cela, cette classe implémente est de type runnable, c'est à dire qu'elle possède une méthode pour creer un Thread qui vient ensuite se charger de la Mise à Jour.
	 * Une fois une instance créée, elle se lance par start()
	 * 
	 */
		private Thread thread;
		private String threadName;
		private long timeout;
		boolean end = false;
		
	/**
	 * Contructeur de la classe
	 * @param timeout Temps à attendre entre chaque mise à jour
	 */

	public CacheReloader(long timeout) {
		this.threadName="Cache reloader";
		this.timeout=timeout;
	}

	/**
	 * Met à jour le cache. Méthode appelée lors de la création du thread sur une instance de cette classe.
	 */
	@Override
	public void run() {
		try {
			do {
			Cache c = Cache.getInstance();
			long lastCacheUpdate = c.getLastCacheUpdate().getTime();
			long now = new Date().getTime();
			if(lastCacheUpdate+timeout < now) {
				if(Main.isDebug()) {
					Date d = new Date();
					System.out.println(d+" : "+"Mise à jour du cache à partir de DB");
				}
				c.reload();
			}
			Thread.sleep(timeout*1000);
			}while(!end);
			
		}catch(InterruptedException ie) {
			System.err.println("Le thread de mise à jour du cache a été interompu");
			
		}

	}
	
	/**
	 * Methode pour lancer le cache reloader
	 */
	
	public void start() {
		System.out.println("Démarage du cache Reloader. Mise à jour toute les "+timeout+" secondes.");
		if(thread == null) {
			thread = new Thread(this, this.threadName);
			thread.start();
		}
		
	}

}
