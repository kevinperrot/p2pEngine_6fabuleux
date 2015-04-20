package model.user;

import java.math.BigInteger;

import model.advertisement.AbstractAdvertisement;
import model.objet.Item;
import net.jxta.document.Element;
import net.jxta.document.XMLElement;
import net.jxta.id.ID;
import util.Hasher;
import util.secure.AsymKeysImpl;

/**
 * TODO Description et test
 * @author
 *
 */
public class User extends AbstractAdvertisement{
	private String nick;
	private String hashPwd;
	private String name;
	private String firstName;
	private String email;
	private String phone;
	private AsymKeysImpl key;
	
	/**
	 * To edit existing users in the XML file
	 * @param nick
	 * @param password
	 * @param name
	 * @param firstName
	 * @param email
	 * @param phone
	 * @param key
	 */
	public User(String nick,String password,String name,
			String firstName,String email,
			String phone,AsymKeysImpl key
			){
		super();
		this.nick = nick;
		this.hashPwd = password;
		this.name = name;
		this.firstName = firstName; 
		this.email = email;
		this.phone = phone;
		this.key = key;
		setKeys();
	}
	
	/**
	 * To make new User (during registration)
	 * @param nick
	 * @param password
	 * @param name
	 * @param firstName
	 * @param email
	 * @param phone
	 * @param peerID
	 */
	public User(String nick,String password,String name,
			String firstName,String email,
			String phone
			){
		super();
		this.nick = nick;
		try {
			this.hashPwd = Hasher.SHA256(password);
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.name = name;
		this.firstName = firstName; 
		this.email = email;
		this.phone = phone;
		this.key = new AsymKeysImpl(false);
		setKeys();
	}
	
	/**
	 * Verifies that the password in parameter corresponds to hashPwd
	 * @param password
	 * @return
	 */
	public boolean isPassword(String password){
		try {
			String hash = Hasher.SHA256(password);
			if(hash.equals(this.hashPwd))
				return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	//////////// GETTERS \\\\\\\\\\\\\\\\
	public String getNick() {
		return nick;
	}
	public String getPassword() {
		return hashPwd;
	}
	public String getName() {
		return name;
	}
	public String getFirstName() {
		return firstName;
	}
	public String getEmail() {
		return email;
	}
	public String getPhone() {
		return phone;
	}
	public AsymKeysImpl getKey(){
		return key;
	}
	public BigInteger getPublicKey(){
		return key.getPublicKey();
	}
	public BigInteger getPrivateKey(){
		return key.getPrivateKey();
	}
	public BigInteger getP(){
		return key.getP();
	}
	public BigInteger getG(){
		return key.getG();
	}
	
	//////////// SETTERS \\\\\\\\\\\\\\\\
	public void setNick(String login) {
		this.nick = login;
	}
	public void setPassword(String password) {
		this.hashPwd = password;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public void setKey(AsymKeysImpl key){
		this.key = key;
	}
	
	/*
	 * TODO A SUPPRIMER ULTERIEUREMENT (UTILE POUR LES TESTS)
	 */
	public String toString(){
		String ret = "";
		ret += "  Nickname : "+this.getNick()+"\n";
		ret += "     Login : "+this.getPublicKey().toString(16)+"\n";
		ret += "Private key: "+this.getPrivateKey().toString(16)+"\n";
		ret += "HashPassword:"+this.getPassword()+"\n";
		ret += "      Name : "+this.getName()+"\n";
		ret += "First name : "+this.getFirstName()+"\n";
		ret += "     Email : "+this.getEmail()+"\n";
		ret += "     Phone : "+this.getPhone()+"\n";
		return ret;
	}
	
	//////////////////////////////////////////////// ADVERTISEMENT \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
	/**
	 * Used to define Keys
	 */
	@Override
	protected void setKeys() {
		this.addKey("nick", false);
		this.addKey("hashPwd", false);
		this.addKey("name", false);
		this.addKey("firstName", false);
		this.addKey("email", false);
		this.addKey("phone", false);
		this.addKey("privatekey", false);
		this.addKey("publicKey", true);
	}

	/**
	 * Used to add all keys
	 */
	@Override
	protected void putValues() {
		addValue("nick", this.getNick());
		addValue("hashPwd", this.getPassword());
		addValue("name", this.getName());
		addValue("firstName", this.getFirstName());
		addValue("email", this.getEmail());
		addValue("phone", this.getPhone());
		addValue("privatekey", this.getPrivateKey().toString(16));
		addValue("publicKey", this.getPublicKey().toString(16));
	}
	
	
	public static void main(String[] args){
		User u = new User("pja35", "pwd", "Arrighi", "Pablo", "arrighi.pablo@hotmail.fr", "0612345678");
		System.out.println(u.toString());
		System.out.println();
		System.out.println("pwd : "+u.isPassword("pwd"));
		System.out.println("PWD : "+u.isPassword("PWD"));
	}

	@Override
	protected String getAdvertisementName() {
		return this.getClass().getSimpleName();
	}

	@Override
	protected boolean handleElement(org.jdom2.Element e) {
		// TODO Auto-generated method stub
		return false;
	}
}
