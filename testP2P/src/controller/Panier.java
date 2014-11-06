package controller;

import view.Application;
import model.Objet;
import model.ObjetsManagement;
import model.User;
import model.UsersManagement;

public class Panier implements Validator {
	
	public static enum Action {
		DELETE,
		UP,
		DOWN
	}
	
	private int i;
	private ObjetsManagement panier;
	private Action action;
	
	public Panier(int i, Action action) {
		panier = Application.getInstance().getUsers().getConnectedUser().getPanier();
		this.i = i;
		this.action = action;
	}

	private boolean validateDelete() {
		return panier.get(i) != null;
	}
	
	private boolean validateUp() {
		return panier.get(i) != null;
	}
	
	private boolean validateDown() {
		return panier.get(i) != null;
	}
	
	@Override
	public boolean validate() {
		switch(action) {
		case DELETE: return validateDelete();
		case UP: return validateUp();
		case DOWN: return validateDown();
		default: return false;
		}
	}

	@Override
	public boolean process() {
		switch(action) {
		case DELETE: panier.delete(i); break;
		case UP: panier.upPos(panier.get(i)); break;
		case DOWN: panier.downPos(panier.get(i)); break;
		default: return false;
		}
		
		return true;
	}

}