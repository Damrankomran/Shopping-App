package database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


import tables.Login;
import tables.Musteri;
import tables.Siparis;
import tables.Stok;
import tables.Urun;

public class Database {

	//Connection metodlarý
	static Connection connection;
	public static Connection openConnection(){
		
		try{
			//Look Oracle PL/SQL Connection - Java
			connection = DriverManager.getConnection("jdbc:oracle:thin:@//....","username","password");
			
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("Connection failed!");
		}
		//System.out.println("Connection successful!");
		
		return connection;
	}
	
	public static void closeConnection(){
		
		try{
			connection.close();
		}catch(SQLException e){
			e.printStackTrace();
			System.out.println("Connection not closed!");
		}catch(Exception e){
			e.printStackTrace();
		}
		//System.out.println("Connection closed!");
	}
	
	
	//Record Delete and record available metods
	public static void recordDelete(int ID, String kayit){
		String sql ; 
		if(kayit.equalsIgnoreCase("musteri"))			sql = "DELETE FROM musteri WHERE musteriID = ?";
		else if(kayit.equalsIgnoreCase("urun")) 		sql = "DELETE FROM urun WHERE urunID = ?";
		else if(kayit.equalsIgnoreCase("stok")) 	    sql = "DELETE FROM urun_stok WHERE urunID = ?";
		else if(kayit.equalsIgnoreCase("siparis")) 	    sql = "DELETE FROM siparis WHERE siparisID = ?";
		else 											sql = "DELETE FROM musteriLogin WHERE musteriID = ?";
		try{
			Connection connection = openConnection();
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setInt(1,ID);
			ps.executeUpdate();
			
			ps.close();
			closeConnection();
			System.out.println(kayit +" registration has been successfully deleted.");	
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	
	public static boolean recordAvailable(int ID, String tablo)
	{
		int kayit;
		String sql;
		if (tablo.equalsIgnoreCase("musteri")) 				sql = "SELECT *FROM musteri WHERE musteriID = "+ID;
		else if (tablo.equalsIgnoreCase("urun")) 			sql = "SELECT *FROM urun WHERE urunID = "+ID;
		else if (tablo.equalsIgnoreCase("stok")) 			sql = "SELECT *FROM urun_stok WHERE urunID = "+ID;
		else if (tablo.equalsIgnoreCase("login")) 			sql = "SELECT *FROM musteriLogin WHERE musteriID = "+ID;
		else												sql = "SELECT *FROM siparis WHERE siparisID = "+ID; 									
		
		try{	
			Connection connection = openConnection();
			Statement st = connection.createStatement();
			ResultSet rs = st.executeQuery(sql);
			System.out.println("Record is checking");
			
			while(rs.next()){
				if(tablo.equalsIgnoreCase("musteri")) 				kayit = rs.getInt("musteriID");
				else if (tablo.equalsIgnoreCase("urun"))    	    kayit = rs.getInt("urunID");
				else if (tablo.equalsIgnoreCase("stok"))    	    kayit = rs.getInt("urunID");
				else if(tablo.equalsIgnoreCase("login")) 			kayit = rs.getInt("musteriID");
				else		 										kayit = rs.getInt("siparisID");
				if(kayit == ID){
					rs.close();
					st.close();
					closeConnection();
					System.out.println("There's a record with this ID.");
					return true; 
					//bu musteriID'sine sahip bir kayýt bulursa true dönecek
				}
			}
			rs.close();
			st.close();
			closeConnection();
			System.out.println("The requested records could not be retrieved.");
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return false;
	}

	
	
	//customer add,list and update methods
	public static void musteriInsert(Musteri musteri){
		String sql = "INSERT INTO musteri(musteriID, musteriAd, musteriAdres, musteriTel, musteriCinsiyet) VALUES(?,?,?,?,?)";
		try{
			Connection connection = openConnection();
			PreparedStatement ps = connection.prepareStatement(sql);
			
			ps.setInt(1, musteri.getMusteriID());
			ps.setString(2, musteri.getMusteriAd());
			ps.setString(3, musteri.getMusteriAdres());
			ps.setString(4, musteri.getMusteriTel());
			ps.setString(5, musteri.getMusteriCinsiyet());
					
			ps.executeUpdate();
			ps.close();
			closeConnection();
		}catch(Exception e){
			e.printStackTrace();
		}
		System.out.println("The information was recorded successfully.");
	}
	
	public static void musteriList(){
		String sql = "SELECT *FROM musteri ORDER BY musteriID";
		try{
			Connection connection = openConnection();
			Statement st 	      = connection.createStatement();
			ResultSet rs 	      = st.executeQuery(sql);
			System.out.println("ID   	Ad	          Adres		  	  Tel	      Cinsiyet");
			System.out.println("----------------------------------------------------------------------------------");
			while(rs.next()){
				String zeropad 	= ".................";
				int musteriID 	       = rs.getInt("musteriID");
				String musteriAd 	   = rs.getString("musteriAd");
				String musteriAdres    = rs.getString("musteriAdres");
				String musteriTel 	   = rs.getString("musteriTel");
				String musteriCinsiyet = rs.getString("musteriCinsiyet");
				System.out.println(musteriID+"  "+ (musteriAd+zeropad.substring(musteriAd.length())) + "	 "+ (musteriAdres+zeropad.substring(musteriAdres.length())) + "	 "+ musteriTel + "       "+ musteriCinsiyet);
			}
			rs.close();
			st.close();
			closeConnection();
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	public static void musteriUpdate(Musteri musteri){
		String sql = "UPDATE musteri SET musteriAd = ?,  musteriAdres = ?, musteriTel = ?, musteriCinsiyet = ? WHERE musteriID = ?";
		try{
			Connection connection = openConnection();
			PreparedStatement ps = connection.prepareStatement(sql);
			
			ps.setInt(5, musteri.getMusteriID());
			ps.setString(1, musteri.getMusteriAd());
			ps.setString(2, musteri.getMusteriAdres());
			ps.setString(3, musteri.getMusteriTel());
			ps.setString(4, musteri.getMusteriCinsiyet());
			
			ps.executeUpdate();
			ps.close();
			closeConnection();
			
		}catch(Exception e){
			e.printStackTrace();
		}
		System.out.println("Customer updated successfully.");
	}
	
	public static int musteriIDSearch(String kullaniciAdi){
		String sql = "SELECT *FROM musteriLogin WHERE kullaniciAdi = '"+kullaniciAdi+"' ";
		int musteriID=-1;
		try{
			Connection connection = openConnection();
			Statement st		  = connection.createStatement();
			ResultSet rs		  = st.executeQuery(sql);
			
			while(rs.next()){
				musteriID = rs.getInt("musteriID");
			}
			rs.close();
			st.close();
			closeConnection();
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return musteriID;
	}
	
	public static String musteriAdSearch(int musteriID){
		String musteriAd=null;
		String sql = "SELECT *FROM musteri WHERE musteriID = "+musteriID;
		try{
			Connection connection = openConnection();
			Statement st		  = connection.createStatement();
			ResultSet rs 		  = st.executeQuery(sql);
			
			while(rs.next()){
				musteriAd = rs.getString("musteriAd");
			}
			
			rs.close();
			st.close();
			closeConnection();
		}catch(Exception e){
			e.printStackTrace();
		}
		return musteriAd;
	}
	
	
	
	
	
	
	
	
	public static void insertLogin(Login login){
		String sql = "INSERT INTO musteriLogin(musteriID, kullaniciAdi, kullaniciSifre) VALUES (?,?,?)";
		try{
			Connection connection = openConnection();
			PreparedStatement ps  = connection.prepareStatement(sql);
			
			ps.setInt(1, login.getMusteriID());
			ps.setString(2, login.getKullaniciAdi());
			ps.setString(3, login.getKullanciSifre());
			
			ps.executeUpdate();
			ps.close();
			closeConnection();
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	public static void loginList(){
		String sql = "SELECT l.musteriID, m.musteriAd, l.kullaniciAdi, l.kullaniciSifre  FROM musteriLogin l, musteri m WHERE m.musteriID = l.musteriID";
		String zeropad 	= ".................";
		try{
			Connection connection = openConnection();
			Statement st 		  = connection.createStatement();
			ResultSet rs		  = st.executeQuery(sql);
			System.out.println("ID        Müþteri Adý     Kullanýcý Adý      Þifre");
			System.out.println("--------------------------------------------");
			while(rs.next()){
				int musteriID         = rs.getInt("musteriID");
				String musteriAd 	  = rs.getString("musteriAd");
				String kullaniciAdi   = rs.getString("kullaniciAdi");
				String kullaniciSifre = rs.getString("kullaniciSifre");
				System.out.println(musteriID+"        "+( musteriAd+zeropad.substring( musteriAd.length())) +" "+ (kullaniciAdi+zeropad.substring(kullaniciAdi.length()))+ "  "+ kullaniciSifre);
			}
			rs.close();
			st.close();
			closeConnection();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static boolean loginAvailable(String kullaniciAdi){
		String sql = "SELECT *FROM musteriLogin WHERE kullaniciAdi = '"+kullaniciAdi+"' ";
		try{
			Connection connection = openConnection();
			Statement st		  = connection.createStatement();
			ResultSet rs		  = st.executeQuery(sql);
			
			while(rs.next()){
				String kullanici = rs.getString("kullaniciAdi");
				if(kullanici.equalsIgnoreCase(kullaniciAdi)){
					rs.close();
					st.close();
					closeConnection();
					return true;
				}
			}
			rs.close();
			st.close();
			closeConnection();
		}catch(Exception e){
			e.printStackTrace();
		}
		return false;
	}
	
	public static void loginUpdate(Login login){
		String sql = "UPDATE musteriLogin SET kullaniciAdi = ?, kullaniciSifre = ? WHERE musteriID = ?";
		try{
			Connection connection = openConnection();
			PreparedStatement ps  = connection.prepareStatement(sql);
			
			ps.setString(1, login.getKullaniciAdi());
			ps.setString(2, login.getKullanciSifre());
			ps.setInt(3, login.getMusteriID());
			
			ps.executeUpdate();
			ps.close();
			closeConnection();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static boolean passwordControl(Login login){
		String sql = "SELECT *FROM musteriLogin WHERE kullaniciAdi = '"+login.getKullaniciAdi()+"' AND kullaniciSifre = '"+login.getKullanciSifre()+"'";
		try{
			Connection connection = openConnection();
			Statement st		  = connection.createStatement();
			ResultSet rs		  = st.executeQuery(sql);
			
			while(rs.next()){
				rs.close();
				st.close();
				closeConnection();
				return true;
			}
			
			rs.close();
			st.close();
			closeConnection();
		}catch(Exception e){
			e.printStackTrace();
		}
		return false;
	}
	
	
	
	
	
	
	
	public static void urunInsert(Urun urun){
		String sql = "INSERT INTO urun( urunID, urunAd, urunMarka, urunKategori, urunFiyat) VALUES(?,?,?,?,?)";
		try{
			Connection connection = openConnection();
			PreparedStatement ps  = connection.prepareStatement(sql);
			
			ps.setInt(1, urun.getUrunID());
			ps.setString(2, urun.getUrunAd());
			ps.setString(3, urun.getUrunMarka());
			ps.setString(4, urun.getUrunKategori());
			ps.setInt(5, urun.getUrunFiyat());
			
			ps.executeUpdate();
			ps.close();
			closeConnection();
			System.out.println("The information was recorded successfully.");
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void urunList(){
		String sql = "SELECT u.urunID, u.urunAd, u.urunMarka, u.urunKategori, u.urunFiyat, s.urunAdet FROM urun u, urun_stok s WHERE u.urunID = s.urunID ORDER BY urunKategori";
		try{
			Connection connection = openConnection();
			Statement st 		  = connection.createStatement();
			ResultSet rs 		  = st.executeQuery(sql);
			System.out.println("ID         AD         	         Marka       		  Kategori            Fiyat      Adet");
			System.out.println("----------------------------------------------------------------------------");
			while(rs.next()){
				String zeropad 	= ".........................";
				String zeropad2 = "...............";
				int urunID 			= rs.getInt("urunID");
				String urunAd 		= rs.getString("urunAd");
				String urunMarka 	= rs.getString("urunMarka"); 
				String urunKategori = rs.getString("urunKategori");
				int urunFiyat 		= rs.getInt("urunFiyat");
				String numberAsString = String.valueOf(urunFiyat); //urun fiyatý ekranda düzenli görünmesi için string e çevirdik ve ekrand gösterdik
				int urunAdet	    = rs.getInt("urunAdet");
				System.out.println(urunID+"	"+ (urunAd+zeropad.substring(urunAd.length()))+" "+( urunMarka+zeropad.substring( urunMarka.length()))+" "+ (urunKategori +zeropad2.substring(urunKategori.length()))+"   "+(numberAsString+(".....".substring(numberAsString.length())))+"       "+urunAdet);
			}
			rs.close();
			st.close();
			closeConnection();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void urunUpdate(Urun urun){
		String sql = "UPDATE urun SET urunAd = ?, urunMarka = ?, urunKategori = ?, urunFiyat = ? WHERE urunID = ?";
		try{
			Connection connection = openConnection();
			PreparedStatement ps  = connection.prepareStatement(sql);
			
			ps.setString(1, urun.getUrunAd());
			ps.setString(2, urun.getUrunMarka());
			ps.setString(3, urun.getUrunKategori());
			ps.setInt(4, urun.getUrunFiyat());
			ps.setInt(5, urun.getUrunID());
			
			ps.executeUpdate();
			ps.close();
			closeConnection();
			System.out.println("The product was successfully updated.");
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static boolean kategoriler(String kategoriAdi){
		String[] kategoriler = {"Giyim","Elektronik","Kozmetik","Kitap"};
		
		for(int i=0; i<kategoriler.length; i++){
			if(kategoriler[i].equalsIgnoreCase(kategoriAdi)){
				return true;
			}
		}
		
		return false;
	}
	
	
	
	
	
	
	public static void stokInsert(Stok stok){
		String sql = "INSERT INTO urun_stok (urunID, urunAdet) VALUES (?,?)";
		try{
			Connection connection = openConnection();
			PreparedStatement ps  = connection.prepareStatement(sql);
			
			ps.setInt(1, stok.getUrunID());
			ps.setInt(2, stok.getUrunAdet());
			
			ps.executeUpdate();
			ps.close();
			closeConnection();
			System.out.println("The information was recorded successfully.");
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	public static void stokUpdate(Stok stok){
		String sql = "UPDATE urun_stok SET urunAdet = ? WHERE urunID = ?";
		try{
			Connection connection = openConnection();
			PreparedStatement ps  = connection.prepareStatement(sql);
			
			ps.setInt(1, stok.getUrunAdet());
			ps.setInt(2, stok.getUrunID());
			
			ps.executeUpdate();
			ps.close();
			closeConnection();
			System.out.println("The inventory was successfully updated.");
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void stokList(){
		String sql = "SELECT s.urunID, u.urunAd, urunMarka, urunKategori, s.urunAdet FROM urun_stok s, urun u WHERE s.urunID = u.urunID ORDER BY u.urunkategori ";
		String zeropad 	= ".........................";
		String zeropad2 = "...............";
		try{
			Connection connection = openConnection();
			Statement st		  = connection.createStatement();
			ResultSet rs 		  = st.executeQuery(sql);
			System.out.println("UrunID  Urun Ad                    Urun Marka              Urun Kategori     Ürün Adet");
			System.out.println("---------------------------------------------------------------");
			while(rs.next()){
				int urunID    = rs.getInt("urunID");
				String urunAd = rs.getString("urunAd");
				String urunMarka = rs.getString("urunMarka");
				String urunKategori = rs.getString("urunKategori");
				int urunAdet  = rs.getInt("urunAdet");
				String numberAsString = String.valueOf(urunID);
				System.out.println((numberAsString+("...".substring(numberAsString.length())))+"    "+(urunAd+zeropad.substring(urunAd.length()))+" "+(urunMarka+zeropad.substring(urunMarka.length())) +" "+ (urunKategori +zeropad2.substring(urunKategori.length()))+"        "+ urunAdet);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static int stokTotal(int urunID){
		String sql = "SELECT *FROM urun_stok WHERE urunID = "+urunID;
		int urunAdet = 0;
		try{
			Connection connection = openConnection();
			Statement st		  = connection.createStatement();
			ResultSet rs 	      = st.executeQuery(sql);
			
			while(rs.next()){
				urunAdet = rs.getInt("urunAdet");
			}
			rs.close();
			st.close();
			closeConnection();
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
		return urunAdet;
	}
	
	

	
	
	
	
	public static void siparisInsert(Siparis siparis){
		String sql = "INSERT INTO siparis(siparisID, musteriID, urunID, siparisTarih) VALUES (?,?,?,?)";
		try{
			Connection connection = openConnection();
			PreparedStatement ps  = connection.prepareStatement(sql);
			
			ps.setInt(1, siparis.getSiparisID());
			ps.setInt(2, siparis.getMusteriID());
			ps.setInt(3, siparis.getUrunID());
			ps.setString(4, siparis.getSiparisTarih());
			
			ps.executeUpdate();
			ps.close();
			closeConnection();
			System.out.println("The information was recorded successfully");
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void MusteriSiparisList(int musteriID){
		String sql = "SELECT s.siparisID, u.urunAd, u.urunMarka, u.urunKategori, u.urunFiyat, s.siparisTarih  FROM siparis s, urun u WHERE s.musteriID = "+musteriID+" AND s.urunID = u.urunID";

		String zeropad 	= ".........................";
		String zeropad2 = "...............";
		String zeropad3 = ".....";
		String zeropad4 = "..........";
		
		try{
			Connection connection = openConnection();
			Statement st		  = connection.createStatement();
			ResultSet rs		  = st.executeQuery(sql);
			
			System.out.println("Sipariþ ID    Ürün Ad                   Urun Marka           Ürün Kategori          Fiyat     Sipariþ Tarih");
			System.out.println("----------------------------------------------------------------------------------------------------------------------");
			while(rs.next()){
				int siparisID 		= rs.getInt("siparisID");
				String urunAd 		= rs.getString("urunAd");
				String urunMarka 	= rs.getString("urunMarka");
				String urunKategori = rs.getString("urunKategori");
				int urunFiyat 		= rs.getInt("urunFiyat");
				String numberAsString = String.valueOf(urunFiyat);
				String siparisTarih = rs.getString("siparisTarih");
				
				System.out.println(siparisID+"            "+ (urunAd+zeropad.substring(urunAd.length()))+" "+ (urunMarka +zeropad2.substring(urunMarka.length()))+  " "+ (urunKategori +zeropad2.substring(urunKategori.length()))+ " "+ (numberAsString +zeropad3.substring(numberAsString.length()))+" "+(siparisTarih +zeropad4.substring(siparisTarih.length())));
			}
			rs.close();
			st.close();
			closeConnection();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void siparisList(){
		String sql = "SELECT s.siparisID, u.urunAd, u.urunKategori, u.urunFiyat  FROM siparis s, urun u WHERE s.urunID = u.urunID";
		
		String zeropad 	= ".........................";
		String zeropad2 = "...............";
		try{
			Connection connection = openConnection();
			Statement st		  = connection.createStatement();
			ResultSet rs		  = st.executeQuery(sql);
			
			System.out.println("Sipariþ ID    Ürün Ad       Ürün Kategori          Fiyat     Sipariþ Tarih");
			System.out.println("----------------------------------------------------------------------------------");
			while(rs.next()){
				int siparisID = rs.getInt("siparisID");
				String urunAd = rs.getString("urunAd");
				String urunKategori = rs.getString("urunKategori");
				int urunFiyat = rs.getInt("urunFiyat");
				
				System.out.println(siparisID+" "+ (urunAd+zeropad.substring(urunAd.length()))+" "+ (urunKategori +zeropad2.substring(urunKategori.length()))+ " "+ urunFiyat);
			}
			rs.close();
			st.close();
			closeConnection();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	







}

