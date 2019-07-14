package main;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.Scanner;
import tables.Login;
import tables.Musteri;
import tables.Siparis;
import tables.Stok;
import tables.Urun;
import database.Database;

public class Main {

	public static void main(String[] args) {

		SimpleDateFormat bicim=new SimpleDateFormat("dd/MM/yyyy");
		Date tarih=new Date();
		System.out.println("Tarih: "+bicim.format(tarih));
		 
		
		Scanner in = new Scanner(System.in);
		
		
								//////////////// Giri� Ekran� ////////////////////////
		Login login = new Login();		
		System.out.println("Kullan�c� Giri�i Sayfas�");
		
		do{
			System.out.print("Kullan�c� Ad�n� Giriniz: ");
			login.setKullaniciAdi(in.next());
		}while(Database.loginAvailable(login.getKullaniciAdi())==false);
		
		do{
			System.out.print("�ifreyi giriniz: ");
			login.setKullanciSifre(in.next());
		}while(Database.passwordControl(login) == false);
		
		login.setMusteriID(Database.musteriIDSearch(login.getKullaniciAdi()));
		
	
		
		
		
		///////////////////////			  Y�netici Aray�z�  		//////////////////////////////////////////
		if(login.getMusteriID() == 0){
	
			int menu = 10;
			while(menu != 0){
				System.out.println("\nALI�VER�� VER�TABANINA HO�GELD�N�Z\n");
				System.out.println("1) M��teri ��lemleri | 2) �r�n ��lemleri | 3) Stok ��lemleri 4) Sipari� ��lemleri | 0) Exit");
				System.out.print("��lemi Se�iniz: ");
				menu = in.nextInt();
				
				int islem = 10;
				
				if(menu == 1){
					while(islem != 0){
						System.out.println("M��teri ��lemleri Men�s�ne Ho�geldiniz\n");
						Database.musteriList();
						System.out.println("\n");
						System.out.println("1) Yeni m��teri ekle | 2) M��teri G�ncelle | 3) M��teri Sil | 4) Login ��lemleri | 0) Geri");
						System.out.print("��lemi se�iniz: ");
						islem = in.nextInt();
						
						if(islem == 1){
							System.out.println("M��teri ekleme i�lemi ba�lat�l�yor..");
							Musteri musteri = new Musteri();
							
							do{ //MusteriID girilirken kay�tlarda bu ID var m� diye kontrol edecek.
								System.out.print("MusteriID'sini giriniz: ");
								musteri.setMusteriID(in.nextInt());
							}while(Database.recordAvailable(musteri.getMusteriID(), "musteri"));
							
							System.out.print("M��terinin ad�n� giriniz: ");
							musteri.setMusteriAd(in.next());
							
							System.out.print("M��terinin adresini giriniz: ");
							musteri.setMusteriAdres(in.next());
							
							System.out.print("M��terinin telefon numaras�n� giriniz: ");
							musteri.setMusteriTel(in.next());
							
							do{
								System.out.print("M��terinin cinsiyetini giriniz (K/E): ");
								musteri.setMusteriCinsiyet(in.next());
							}while(musteri.getMusteriCinsiyet().length()!=1 );
							
							Database.musteriInsert(musteri);
							
							
							login.setMusteriID(musteri.getMusteriID());
							
							do{ //yeni m��teri i�in kullan�c� ad� olu�turmas�n� �sted�k. Bu kullan�c� ad�n�n daha �nce
								// kullan�lmad���n� anlamak i�in loginAvailable fonksiyonunu cag�rd�k.
								System.out.print(musteri.getMusteriAd()+" i�in kullan�c� ad� olu�turunuz: ");
								login.setKullaniciAdi(in.next());
							}while(Database.loginAvailable(login.getKullaniciAdi()));
							
							do{//Kullan�c�n�n olu�turaca�� �ifre 8 harften fazla 15 harften az olmak zorunda!!
								System.out.print(musteri.getMusteriAd()+" i�in bir �ifre olu�turunuz: ");
								login.setKullanciSifre(in.next());
							}while(login.getKullanciSifre().length() < 8 || login.getKullanciSifre().length() > 15);
							
							Database.insertLogin(login);
						}
						
						if(islem == 2){
							System.out.println("M��teri g�ncelleme i�lemi ba�lat�l�yor..");
							Musteri musteri = new Musteri();
							//g�ncellenecek olan musterinin musterinin ID'si istenilecek
							do{//girilen bu ID'nin do�rulu�u kontrol edilecek.
								System.out.print("G�ncellenecek olan m��terinin ID'sini giriniz: ");
								musteri.setMusteriID(in.nextInt());
							}while(Database.recordAvailable(musteri.getMusteriID(), "musteri")==false);
							
							System.out.print("M��terinin ad�n� giriniz:");
							musteri.setMusteriAd(in.next());
							
							System.out.print("M��terinin adresini giriniz: ");
							musteri.setMusteriAdres(in.next());
							
							System.out.print("M��terinin telefon numaras�n� giriniz: ");
							musteri.setMusteriTel(in.next());
							
							do{
								System.out.print("M��terinin cinsiyetini giriniz (K/E): ");
								musteri.setMusteriCinsiyet(in.next());
							}while(musteri.getMusteriCinsiyet().length()!=1 );
							
							Database.musteriUpdate(musteri);
							
							System.out.print(musteri.getMusteriAd()+" kay�t�n login bilgilerini g�ncellemek istiyor musunuz? (Y/N): ");
							if(in.next().equalsIgnoreCase("Y")){
								
								login.setMusteriID(musteri.getMusteriID());
								do{
									System.out.print(musteri.getMusteriAd()+" i�in yeni bir kullan�c� ad� giriniz: ");
									login.setKullaniciAdi(in.next());
								}while(Database.loginAvailable(login.getKullaniciAdi()));
								
								do{
								System.out.print(musteri.getMusteriAd()+" i�in yeni bir �ifre giriniz: ");
								login.setKullanciSifre(in.next());
								}while(login.getKullanciSifre().length() < 8 || login.getKullanciSifre().length() > 15);
								
								Database.loginUpdate(login);
							}
							
						}
						
						if(islem == 3){
							System.out.println("M��teri silme i�lemi ba�lat�l�yor..");
							int musteriID;
							do{
								System.out.print("Silinecek m��terinin musteriID'sini giriniz: ");
								musteriID = in.nextInt();
							}while(Database.recordAvailable(musteriID, "musteri")==false);
							
							Database.recordDelete(musteriID, "login");
							Database.recordDelete(musteriID,"musteri");
							
						}
						int loginIslem = 10;
						
						if(islem == 4){
							while(loginIslem != 0){
							System.out.println("Login ��lemleri Men�s�ne Ho�geldiniz");
							System.out.println("\n");
							Database.loginList();
							System.out.println("\n");
							System.out.println("1) Login Ekle | 2) Login G�ncelle | 3) Login Silme 0)Geri");
							System.out.print("�slem se�iniz: ");
							loginIslem = in.nextInt();
							
							
							if(loginIslem == 1){
								System.out.println("Login ekleme i�lemi ba�lat�l�yor..");
								do{
									System.out.print("Login eklenicek musteriID'yi giriniz: ");
									login.setMusteriID(in.nextInt());
								}while(Database.recordAvailable(login.getMusteriID(), "musteri") == false );
								
								do{
									System.out.print(" Yeni bir kullan�c� ad� giriniz: ");
									login.setKullaniciAdi(in.next());
								}while(Database.loginAvailable(login.getKullaniciAdi()));
								
								do{
								System.out.print(" Yeni bir �ifre giriniz: ");
								login.setKullanciSifre(in.next());
								}while(login.getKullanciSifre().length() < 8 || login.getKullanciSifre().length() > 15);
								
								Database.insertLogin(login);
								
							}
							
							
							if(loginIslem == 2){
								System.out.println("Login g�ncelleme islemi ba�lat�l�yor..");
																
								do{
									System.out.print("G�ncellenecek olan Login'in ID'sini giriniz: ");
									login.setMusteriID(in.nextInt());
								}while(Database.recordAvailable(login.getMusteriID(), "login") == false);
								
								do{
									System.out.print(" Yeni bir kullan�c� ad� giriniz: ");
									login.setKullaniciAdi(in.next());
								}while(Database.loginAvailable(login.getKullaniciAdi()));
								
								do{
								System.out.print(" Yeni bir �ifre giriniz: ");
								login.setKullanciSifre(in.next());
								}while(login.getKullanciSifre().length() < 8 || login.getKullanciSifre().length() > 15);
								
								Database.loginUpdate(login);
							}
							
							if(loginIslem == 3){
								System.out.println("Login silme i�lemi ba�lat�l�yor..");
								int musteriID;
								do{
									System.out.print("Silinecek olan Login'in musteriID'sini giriniz: ");
									musteriID = in.nextInt();
								}while(Database.recordAvailable(musteriID, "login") == false);
							
								Database.recordDelete(musteriID, "login");
							}
							
							
							}
						}
						
						
					}
				}
				
				if(menu == 2){
					while(islem != 0){
						System.out.println("�r�n ��lemleri Men�s�ne Ho�geldiniz");
						Database.urunList();
						System.out.println("1) �r�n Ekle | 2) �r�n G�ncelle | 3) �r�n Sil | 0) Geri");
						System.out.print("��lemi se�iniz:  ");
						islem = in.nextInt();
						
											Urun urun = new Urun();
						if(islem == 1){
							System.out.println("�r�n ekleme i�lemi ba�lat�l�yor..");
							do{
								System.out.print("urunID giriniz: ");
								urun.setUrunID(in.nextInt());
							}while(Database.recordAvailable(urun.getUrunID(), "urun"));
							
							System.out.print("�r�n ad�n� giriniz: ");
							urun.setUrunAd(in.next());
							
							System.out.print("�r�n�n markas�n� giriniz: ");
							urun.setUrunMarka(in.next());
							
							do{
								System.out.print("�r�n�n kategorisini giriniz (Giyim/Elektronik/Kozmetik/Kitap): ");
								urun.setUrunKategori(in.next());
							}while(Database.kategoriler(urun.getUrunKategori())==false);
							
							System.out.print("�r�n�n fiyat�n� giriniz: ");
							urun.setUrunFiyat(in.nextInt());
							
							Database.urunInsert(urun);
							
							Stok stok = new Stok();
							stok.setUrunID(urun.getUrunID());
							do{
								System.out.print("�r�n�n adetini giriniz:");
								stok.setUrunAdet(in.nextInt());
							}while(stok.getUrunAdet() < 0);
							
							Database.stokInsert(stok);
							
						}	
						
						if(islem == 2){
							System.out.println("�r�n g�ncelleme i�lemi ba�lat�l�yor..");
							do{
								System.out.print("G�ncellenecek olan �r�n�n urunID'sini giriniz: ");
								urun.setUrunID(in.nextInt());
							}while(Database.recordAvailable(urun.getUrunID(), "urun") == false);
							
							System.out.print("�r�n yeni ad�n� giriniz: ");
							urun.setUrunAd(in.next());
							
							System.out.print("�r�n�n yeni markas�n� giriniz: ");
							urun.setUrunMarka(in.next());
							
							do{
								System.out.print("�r�n�n yeni kategorisini giriniz (Giyim/Elektronik/Kozmetik/Kitap): ");
								urun.setUrunKategori(in.next());
							}while(Database.kategoriler(urun.getUrunKategori())==false);
							
							System.out.print("�r�n�n yeni fiyat�n� giriniz: ");
							urun.setUrunFiyat(in.nextInt());
							
							Database.urunUpdate(urun);
														
							System.out.print("�r�n�n stok adeti g�ncellensin mi?(E/H): ");
							if(in.next().equalsIgnoreCase("E")){
								Stok stok = new Stok();
								stok.setUrunID(urun.getUrunID());
								do{
									System.out.print("�r�n�n adetini giriniz:");
									stok.setUrunAdet(in.nextInt());
								}while(stok.getUrunAdet() < 0);
								
								Database.stokUpdate(stok);
							}
						}
						
						if(islem == 3){
							System.out.println("�r�n silme i�lemi ba�lat�l�yor..");
							int urunID;
							do{
								System.out.print("Silinecek olan �r�n�n urunID'sini giriniz: ");
								urunID = in.nextInt();
							}while(Database.recordAvailable(urunID, "urun")==false);
							
							Database.recordDelete(urunID, "stok");
							Database.recordDelete(urunID, "urun");
							
						}
						
						
					}
				}
				
				if(menu == 3){
					while(islem!=0){
						System.out.println("Stok ��lemleri Men�s�ne Ho�geldiniz");
						Database.stokList();
						System.out.println("1) Stok G�ncelleme | 0) Geri");
						System.out.print("Bir i�lem se�iniz: ");
						islem = in.nextInt();
						if(islem == 1){
							Stok stok = new Stok();
							System.out.println("Stok g�ncelleme islemi ba�lat�l�yor..");
							
							do{
								System.out.print("G�ncellenecek olan �r�n�n urunID'sini giriniz:");
								stok.setUrunID(in.nextInt());
							}while(Database.recordAvailable(stok.getUrunID(), "stok")==false);
							
							do{
							System.out.print("�r�n�n yeni adetini giriniz: ");
							stok.setUrunAdet(in.nextInt());
							}while(stok.getUrunAdet() < 0 );
							
							Database.stokUpdate(stok);
						}
						
					}
					
				}
				if(menu == 4){
					while(islem!=0){
						System.out.println("Sipari� ��lemleri Men�s�ne Ho�geldiniz");
						Database.siparisList();
						System.out.println("1) Siparis Sil | 0) Geri");
						System.out.print("Bir i�lem se�iniz: ");
						islem = in.nextInt();
						
						if(islem == 1 ){
							
							int siparisID;
							
							do{
								System.out.print("Silinecek olan siparisin ID'sini giriniz: ");
								siparisID = in.nextInt();
							}while(Database.recordAvailable(siparisID, "siparis") == false);
							
							Database.recordDelete(siparisID, "siparis");
							Stok stok = new Stok();
							stok.setUrunID(siparisID);
							stok.setUrunAdet(Database.stokTotal(siparisID) + 1);
							Database.stokUpdate(stok);
							
						}
					
					}
					
				}
			
				
			}
		}
		
		
		
		
		
		
								////////////////// M��teri Aray�z� /////////////////////////////////////	
		else{
			
			int menu = 10;
			System.out.println("\nALI�VER�� VER�TABANINA HO�GELD�N "+ Database.musteriAdSearch(login.getMusteriID())+"\n");
			
			while(menu != 0){
				System.out.println(" 1) �r�nler | 2) Sipari� ��lemleri | 0) Exit");
				System.out.print("��lem se�iniz: ");
				menu = in.nextInt();
				
				int islem=10;
				if(menu == 1){
					
					System.out.println("�r�nler");
					Database.urunList();
					
				}
				
				if(menu == 2){
					while(islem != 0){
						System.out.println("Siparisler Men�s�");
						Siparis siparis = new Siparis();
						System.out.println("1) Sipari� Ekle | 2)Sipari�lerimi G�r�nt�le | 3) Siparis Sil | 0) Geri");
						System.out.print("��lemi se�iniz: ");
						islem = in.nextInt();
						//Database.stokList();
						if(islem == 1){
							
							System.out.println("Sipari� Ekleme ��lemi Ba�lat�l�yor..");
							
							Random random = new Random();
							int rg = 1 + random.nextInt(999);
							
							do{ //Random siparisID olu�turduk. Bu �d numaras� daha �nce kullan�lmad���n� kontrol ettik
								siparis.setSiparisID(rg);
							}while(Database.recordAvailable(siparis.getSiparisID(), "siparis"));
							
							//giri� yapt��� m��teriID'yi siparis tablosundaki musteriID'ye yerle�tirdik.
								siparis.setMusteriID(login.getMusteriID());
								Database.urunList();
								
							do{ //urunleri listeledik ve istedi�i �r�n�n ID'sini yazmas�n� istedik. Bu ID'in tabloda olup olmad���n� kontrol ettik.
									System.out.print("Siparis etmek istedi�iniz �r�n�n ID'sini giriniz:");
									siparis.setUrunID(in.nextInt());
							}while(Database.recordAvailable(siparis.getUrunID(), "urun") == false);
							
							
							//siparis tablosuna siparisTarihini ald��� g�n� kaydettik.
							siparis.setSiparisTarih(bicim.format(tarih));
							
							//stokta o �r�n�n yeterli say�da olup olmad���n� kontol ettik
							System.out.println("�stedi�iniz �r�n stokta kontrol ediliyor..");
							if(Database.stokTotal(siparis.getUrunID()) > 0){
								System.out.println("�r�n mevcut siparis kayd� olu�turuluyor..");
								Database.siparisInsert(siparis);
								
								//Stok tablosunu g�ncelledik
								Stok stok = new Stok();
								stok.setUrunID(siparis.getUrunID());
								stok.setUrunAdet(Database.stokTotal(siparis.getUrunID()) - 1);
								Database.stokUpdate(stok);
							}
							else{
								System.out.println("�stedi�iniz �r�n�n adeti stokta mevcut de�il!!");
							}
						}

						if(islem == 2){
							Database.MusteriSiparisList(login.getMusteriID());
						}
						
						if(islem == 3){
							int siparisID;
							System.out.println("Siparis silme i�lemi ba�lat�l�yor..");
							Database.MusteriSiparisList(login.getMusteriID());
							
							do{
								System.out.print("Silinecek olan siparisin ID'sini giriniz: ");
								siparisID = in.nextInt();
							}while(Database.recordAvailable(siparisID, "siparis") == false);
							
							Database.recordDelete(siparisID, "siparis");
							Stok stok = new Stok();
							stok.setUrunID(siparisID);
							stok.setUrunAdet(Database.stokTotal(siparisID) + 1);
							Database.stokUpdate(stok);
							
						}
						
					}
				}
				
			
			}
		}
		
		
		
		
		
   }

}
