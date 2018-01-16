package main;

import java.util.List;

import com.sun.corba.se.impl.orbutil.RepositoryIdUtility;

import data.ISP;
import data.ISPDAO;

public class Cache {
	/**
	 * Cette classe sert de cache pour les infos de DB
	 * Elle implemente le Design Pattern Singleton dans la mesure où cette classe doit être l'unique instance de référence peu importe le Thread l'utilisant, et que le cache est unique.
	 */
	
	public static volatile Cache instance = null;
	private List<ISP> cache;
	
	private Cache() {
		ISPDAO idao = ISPDAO.getInstance();
		try {
			cache = idao.getISPs();
		} catch (Exception e) {
			System.err.println("Le cache n'as pas pu être géré à cause de l'exception suivante");
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Méthode du design patern singleton. Permet de récuperer et si besoin d'initialiser, l'unique instance de la classe.
	 * @return Instance du Cache
	 */
	public final static Cache getInstance() {
		if (Cache.instance == null) {
			synchronized (ISPDAO.class) {
				if(Cache.instance == null) {
					Cache.instance = new Cache();
				}
			}
		}
		return Cache.instance;
	}
	
	/**
	 * Cette methode vient mettre à jour le cache des FAI. Pour cela, il récupere l'instance du DAO et récupère la liste des ISP. Si tout cse passe correctement, il supprime le cache précédent et le remplace par les nouvelles valeurs. Sinon, il maaintient le cache précédent.
	 * @return True si l'operation s'est passée correctement, False sinon et affiche la cause
	 */
	public boolean reload() {
		try {
			ISPDAO idao = ISPDAO.getInstance();
			List<ISP> l = idao.getISPs();
			if(l != null) {
				cache.clear();
				cache.addAll(l);
			}
		}catch (Exception e) {
			System.err.println("Erreur au moment du reload() :\n "+e.getMessage());
			return false;
		}
		return true;
	}
	
	public int size() {
		return cache.size();
	}

	public List<ISP> getListe(){
		return cache;
	}
}
