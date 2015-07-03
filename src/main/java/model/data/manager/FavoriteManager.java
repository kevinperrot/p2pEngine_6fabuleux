package model.data.manager;

import java.util.HashMap;

import org.jdom2.Element;

import util.Printer;
import util.StringToElement;
import model.data.favorites.Favorites;
import model.data.item.Item;
import model.data.user.User;

public class FavoriteManager {
	private HashMap<String, Favorites> favorites = new HashMap<String, Favorites>();
	private Manager manager;
	
	
	public FavoriteManager(Manager m) {
		manager = m;
	}
	
	/**
	 * Get the current user's favorites. If doesn't exist, return null;
	 * @return Favorites
	 */
	public Favorites getUserFavorites(String publicKey){
		if(!favorites.containsKey(publicKey))
			return null;
		return favorites.get(publicKey);
	}
	
	/**
	 * Get the current user's favorites. If doesn't exist, it will return null.
	 * @return Favorites
	 */
	public Favorites getFavoritesCurrentUser(){
		User currentUser = manager.getUserManager().getCurrentUser();
		if(currentUser == null) {
			System.err.println("no user logged");
			return null;
		}
		String publicKey = currentUser.getKeys().getPublicKey().toString(16);
		if(!favorites.containsKey(publicKey))
			favorites.put(publicKey, new Favorites(currentUser));
		return getUserFavorites(publicKey);
	}
	
	/**
	 * Add Favorites to the owner of the Favorites. If the user isn't in the manager, abort.
	 * @param f
	 */
	public void addFavorites(Favorites f){
		if(f == null){
			Printer.printError(this, "addFavorites","This Favorites is null !");
			return;
		}
		String owner = f.getOwner();
		if(owner.isEmpty()){
			Printer.printError(this, "addFavorites","No owner found !");
			return;
		}
		if(manager.getUserManager().getUser(owner) == null){
			Printer.printError(this, "addFavorites","Owner unknown "+owner);
			return;
		}
		if(!f.checkSignature(manager.getUserManager().getUser(owner).getKeys())){
			Printer.printError(this, "addFavorites","Bad Signature for Favorite");
			return;
		}
		favorites.put(f.getOwner(), f);
	}
	
	/**
	 * Add an item to current user's Favorites
	 * @param item
	 */
	public void addFavoritesItem(Item item){
		User currentUser = manager.getUserManager().getCurrentUser();
		String publicKey = currentUser.getKeys().getPublicKey().toString(16); //TODO verification currentUser existe ?
		if(publicKey == null || publicKey.isEmpty()){
			Printer.printError(this, "addFavoritesItem", "Not user logged or PublicKey empty !");
			return;
		}
		if(!favorites.containsKey(publicKey)){
			Favorites f = new Favorites(currentUser);
			f.sign(currentUser.getKeys());
			addFavorites(f);
		}
		if(item == null){
			Printer.printError(this, "addFavoritesItem","This Item is null !");
			return;
		}
		favorites.get(publicKey).addItem(item);
		favorites.get(publicKey).sign(currentUser.getKeys());
	}
	
	/**
	 * Get an XML string representing all the favorites that are saved on this device.
	 * @return A string, XML formated
	 */
	protected String getFavoritesXML(){
		StringBuffer s = new StringBuffer();
		for(Favorites f : favorites.values()) {
			s.append(f);
		}
		return s.toString();
	}
	
	
	/**
	 * Load all the favorites in this element
	 * @param e an element that contains messages in XML format.
	 */
	protected void loadFavorites(Element e) {
		Element root = StringToElement.getElementFromString(e.getValue(), e.getName());
		for(Element f: root.getChildren()){
			addFavorites(new Favorites(f));
		}
	}
	
	
}
