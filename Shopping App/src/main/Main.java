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
		
		
								//////////////// Giriþ Ekraný ////////////////////////
		Login login = new Login();		
		System.out.println("Kullanýcý Giriþi Sayfasý");
		
		do{
			System.out.print("Kullanýcý Adýný Giriniz: ");
			login.setKullaniciAdi(in.next());
		}while(Database.loginAvailable(login.getKullaniciAdi())==false);
		
		do{
			System.out.print("Þifreyi giriniz: ");
			login.setKullanciSifre(in.next());
		}while(Database.passwordControl(login) == false);
		
		login.setMusteriID(Database.musteriIDSearch(login.getKullaniciAdi()));
		
	
		
		
		
		///////////////////////			  Yönetici Arayüzü  		//////////////////////////////////////////
		if(login.getMusteriID() == 0){
	
			int menu = 10;
			while(menu != 0){
				System.out.println("\nALIÞVERÝÞ VERÝTABANINA HOÞGELDÝNÝZ\n");
				System.out.println("1) Müþteri Ýþlemleri | 2) Ürün Ýþlemleri | 3) Stok Ýþlemleri 4) Sipariþ Ýþlemleri | 0) Exit");
				System.out.print("Ýþlemi Seçiniz: ");
				menu = in.nextInt();
				
				int islem = 10;
				
				if(menu == 1){
					while(islem != 0){
						System.out.println("Müþteri Ýþlemleri Menüsüne Hoþgeldiniz\n");
						Database.musteriList();
						System.out.println("\n");
						System.out.println("1) Yeni müþteri ekle | 2) Müþteri Güncelle | 3) Müþteri Sil | 4) Login Ýþlemleri | 0) Geri");
						System.out.print("Ýþlemi seçiniz: ");
						islem = in.nextInt();
						
						if(islem == 1){
							System.out.println("Müþteri ekleme iþlemi baþlatýlýyor..");
							Musteri musteri = new Musteri();
							
							do{ //MusteriID girilirken kayýtlarda bu ID var mý diye kontrol edecek.
								System.out.print("MusteriID'sini giriniz: ");
								musteri.setMusteriID(in.nextInt());
							}while(Database.recordAvailable(musteri.getMusteriID(), "musteri"));
							
							System.out.print("Müþterinin adýný giriniz: ");
							musteri.setMusteriAd(in.next());
							
							System.out.print("Müþterinin adresini giriniz: ");
							musteri.setMusteriAdres(in.next());
							
							System.out.print("Müþterinin telefon numarasýný giriniz: ");
							musteri.setMusteriTel(in.next());
							
							do{
								System.out.print("Müþterinin cinsiyetini giriniz (K/E): ");
								musteri.setMusteriCinsiyet(in.next());
							}while(musteri.getMusteriCinsiyet().length()!=1 );
							
							Database.musteriInsert(musteri);
							
							
							login.setMusteriID(musteri.getMusteriID());
							
							do{ //yeni müþteri için kullanýcý adý oluþturmasýný ýstedýk. Bu kullanýcý adýnýn daha önce
								// kullanýlmadýðýný anlamak için loginAvailable fonksiyonunu cagýrdýk.
								System.out.print(musteri.getMusteriAd()+" için kullanýcý adý oluþturunuz: ");
								login.setKullaniciAdi(in.next());
							}while(Database.loginAvailable(login.getKullaniciAdi()));
							
							do{//Kullanýcýnýn oluþturacaðý þifre 8 harften fazla 15 harften az olmak zorunda!!
								System.out.print(musteri.getMusteriAd()+" için bir þifre oluþturunuz: ");
								login.setKullanciSifre(in.next());
							}while(login.getKullanciSifre().length() < 8 || login.getKullanciSifre().length() > 15);
							
							Database.insertLogin(login);
						}
						
						if(islem == 2){
							System.out.println("Müþteri güncelleme iþlemi baþlatýlýyor..");
							Musteri musteri = new Musteri();
							//güncellenecek olan musterinin musterinin ID'si istenilecek
							do{//girilen bu ID'nin doðruluðu kontrol edilecek.
								System.out.print("Güncellenecek olan müþterinin ID'sini giriniz: ");
								musteri.setMusteriID(in.nextInt());
							}while(Database.recordAvailable(musteri.getMusteriID(), "musteri")==false);
							
							System.out.print("Müþterinin adýný giriniz:");
							musteri.setMusteriAd(in.next());
							
							System.out.print("Müþterinin adresini giriniz: ");
							musteri.setMusteriAdres(in.next());
							
							System.out.print("Müþterinin telefon numarasýný giriniz: ");
							musteri.setMusteriTel(in.next());
							
							do{
								System.out.print("Müþterinin cinsiyetini giriniz (K/E): ");
								musteri.setMusteriCinsiyet(in.next());
							}while(musteri.getMusteriCinsiyet().length()!=1 );
							
							Database.musteriUpdate(musteri);
							
							System.out.print(musteri.getMusteriAd()+" kayýtýn login bilgilerini güncellemek istiyor musunuz? (Y/N): ");
							if(in.next().equalsIgnoreCase("Y")){
								
								login.setMusteriID(musteri.getMusteriID());
								do{
									System.out.print(musteri.getMusteriAd()+" için yeni bir kullanýcý adý giriniz: ");
									login.setKullaniciAdi(in.next());
								}while(Database.loginAvailable(login.getKullaniciAdi()));
								
								do{
								System.out.print(musteri.getMusteriAd()+" için yeni bir þifre giriniz: ");
								login.setKullanciSifre(in.next());
								}while(login.getKullanciSifre().length() < 8 || login.getKullanciSifre().length() > 15);
								
								Database.loginUpdate(login);
							}
							
						}
						
						if(islem == 3){
							System.out.println("Müþteri silme iþlemi baþlatýlýyor..");
							int musteriID;
							do{
								System.out.print("Silinecek müþterinin musteriID'sini giriniz: ");
								musteriID = in.nextInt();
							}while(Database.recordAvailable(musteriID, "musteri")==false);
							
							Database.recordDelete(musteriID, "login");
							Database.recordDelete(musteriID,"musteri");
							
						}
						int loginIslem = 10;
						
						if(islem == 4){
							while(loginIslem != 0){
							System.out.println("Login Ýþlemleri Menüsüne Hoþgeldiniz");
							System.out.println("\n");
							Database.loginList();
							System.out.println("\n");
							System.out.println("1) Login Ekle | 2) Login Güncelle | 3) Login Silme 0)Geri");
							System.out.print("Ýslem seçiniz: ");
							loginIslem = in.nextInt();
							
							
							if(loginIslem == 1){
								System.out.println("Login ekleme iþlemi baþlatýlýyor..");
								do{
									System.out.print("Login eklenicek musteriID'yi giriniz: ");
									login.setMusteriID(in.nextInt());
								}while(Database.recordAvailable(login.getMusteriID(), "musteri") == false );
								
								do{
									System.out.print(" Yeni bir kullanýcý adý giriniz: ");
									login.setKullaniciAdi(in.next());
								}while(Database.loginAvailable(login.getKullaniciAdi()));
								
								do{
								System.out.print(" Yeni bir þifre giriniz: ");
								login.setKullanciSifre(in.next());
								}while(login.getKullanciSifre().length() < 8 || login.getKullanciSifre().length() > 15);
								
								Database.insertLogin(login);
								
							}
							
							
							if(loginIslem == 2){
								System.out.println("Login güncelleme islemi baþlatýlýyor..");
																
								do{
									System.out.print("Güncellenecek olan Login'in ID'sini giriniz: ");
									login.setMusteriID(in.nextInt());
								}while(Database.recordAvailable(login.getMusteriID(), "login") == false);
								
								do{
									System.out.print(" Yeni bir kullanýcý adý giriniz: ");
									login.setKullaniciAdi(in.next());
								}while(Database.loginAvailable(login.getKullaniciAdi()));
								
								do{
								System.out.print(" Yeni bir þifre giriniz: ");
								login.setKullanciSifre(in.next());
								}while(login.getKullanciSifre().length() < 8 || login.getKullanciSifre().length() > 15);
								
								Database.loginUpdate(login);
							}
							
							if(loginIslem == 3){
								System.out.println("Login silme iþlemi baþlatýlýyor..");
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
						System.out.println("Ürün Ýþlemleri Menüsüne Hoþgeldiniz");
						Database.urunList();
						System.out.println("1) Ürün Ekle | 2) Ürün Güncelle | 3) Ürün Sil | 0) Geri");
						System.out.print("Ýþlemi seçiniz:  ");
						islem = in.nextInt();
						
											Urun urun = new Urun();
						if(islem == 1){
							System.out.println("Ürün ekleme iþlemi baþlatýlýyor..");
							do{
								System.out.print("urunID giriniz: ");
								urun.setUrunID(in.nextInt());
							}while(Database.recordAvailable(urun.getUrunID(), "urun"));
							
							System.out.print("Ürün adýný giriniz: ");
							urun.setUrunAd(in.next());
							
							System.out.print("Ürünün markasýný giriniz: ");
							urun.setUrunMarka(in.next());
							
							do{
								System.out.print("Ürünün kategorisini giriniz (Giyim/Elektronik/Kozmetik/Kitap): ");
								urun.setUrunKategori(in.next());
							}while(Database.kategoriler(urun.getUrunKategori())==false);
							
							System.out.print("Ürünün fiyatýný giriniz: ");
							urun.setUrunFiyat(in.nextInt());
							
							Database.urunInsert(urun);
							
							Stok stok = new Stok();
							stok.setUrunID(urun.getUrunID());
							do{
								System.out.print("Ürünün adetini giriniz:");
								stok.setUrunAdet(in.nextInt());
							}while(stok.getUrunAdet() < 0);
							
							Database.stokInsert(stok);
							
						}	
						
						if(islem == 2){
							System.out.println("Ürün güncelleme iþlemi baþlatýlýyor..");
							do{
								System.out.print("Güncellenecek olan ürünün urunID'sini giriniz: ");
								urun.setUrunID(in.nextInt());
							}while(Database.recordAvailable(urun.getUrunID(), "urun") == false);
							
							System.out.print("Ürün yeni adýný giriniz: ");
							urun.setUrunAd(in.next());
							
							System.out.print("Ürünün yeni markasýný giriniz: ");
							urun.setUrunMarka(in.next());
							
							do{
								System.out.print("Ürünün yeni kategorisini giriniz (Giyim/Elektronik/Kozmetik/Kitap): ");
								urun.setUrunKategori(in.next());
							}while(Database.kategoriler(urun.getUrunKategori())==false);
							
							System.out.print("Ürünün yeni fiyatýný giriniz: ");
							urun.setUrunFiyat(in.nextInt());
							
							Database.urunUpdate(urun);
														
							System.out.print("Ürünün stok adeti güncellensin mi?(E/H): ");
							if(in.next().equalsIgnoreCase("E")){
								Stok stok = new Stok();
								stok.setUrunID(urun.getUrunID());
								do{
									System.out.print("Ürünün adetini giriniz:");
									stok.setUrunAdet(in.nextInt());
								}while(stok.getUrunAdet() < 0);
								
								Database.stokUpdate(stok);
							}
						}
						
						if(islem == 3){
							System.out.println("Ürün silme iþlemi baþlatýlýyor..");
							int urunID;
							do{
								System.out.print("Silinecek olan ürünün urunID'sini giriniz: ");
								urunID = in.nextInt();
							}while(Database.recordAvailable(urunID, "urun")==false);
							
							Database.recordDelete(urunID, "stok");
							Database.recordDelete(urunID, "urun");
							
						}
						
						
					}
				}
				
				if(menu == 3){
					while(islem!=0){
						System.out.println("Stok Ýþlemleri Menüsüne Hoþgeldiniz");
						Database.stokList();
						System.out.println("1) Stok Güncelleme | 0) Geri");
						System.out.print("Bir iþlem seçiniz: ");
						islem = in.nextInt();
						if(islem == 1){
							Stok stok = new Stok();
							System.out.println("Stok güncelleme islemi baþlatýlýyor..");
							
							do{
								System.out.print("Güncellenecek olan ürünün urunID'sini giriniz:");
								stok.setUrunID(in.nextInt());
							}while(Database.recordAvailable(stok.getUrunID(), "stok")==false);
							
							do{
							System.out.print("Ürünün yeni adetini giriniz: ");
							stok.setUrunAdet(in.nextInt());
							}while(stok.getUrunAdet() < 0 );
							
							Database.stokUpdate(stok);
						}
						
					}
					
				}
				if(menu == 4){
					while(islem!=0){
						System.out.println("Sipariþ Ýþlemleri Menüsüne Hoþgeldiniz");
						Database.siparisList();
						System.out.println("1) Siparis Sil | 0) Geri");
						System.out.print("Bir iþlem seçiniz: ");
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
		
		
		
		
		
		
								////////////////// Müþteri Arayüzü /////////////////////////////////////	
		else{
			
			int menu = 10;
			System.out.println("\nALIÞVERÝÞ VERÝTABANINA HOÞGELDÝN "+ Database.musteriAdSearch(login.getMusteriID())+"\n");
			
			while(menu != 0){
				System.out.println(" 1) Ürünler | 2) Sipariþ Ýþlemleri | 0) Exit");
				System.out.print("Ýþlem seçiniz: ");
				menu = in.nextInt();
				
				int islem=10;
				if(menu == 1){
					
					System.out.println("Ürünler");
					Database.urunList();
					
				}
				
				if(menu == 2){
					while(islem != 0){
						System.out.println("Siparisler Menüsü");
						Siparis siparis = new Siparis();
						System.out.println("1) Sipariþ Ekle | 2)Sipariþlerimi Görüntüle | 3) Siparis Sil | 0) Geri");
						System.out.print("Ýþlemi seçiniz: ");
						islem = in.nextInt();
						//Database.stokList();
						if(islem == 1){
							
							System.out.println("Sipariþ Ekleme Ýþlemi Baþlatýlýyor..");
							
							Random random = new Random();
							int rg = 1 + random.nextInt(999);
							
							do{ //Random siparisID oluþturduk. Bu ýd numarasý daha önce kullanýlmadýðýný kontrol ettik
								siparis.setSiparisID(rg);
							}while(Database.recordAvailable(siparis.getSiparisID(), "siparis"));
							
							//giriþ yaptýðý müþteriID'yi siparis tablosundaki musteriID'ye yerleþtirdik.
								siparis.setMusteriID(login.getMusteriID());
								Database.urunList();
								
							do{ //urunleri listeledik ve istediði ürünün ID'sini yazmasýný istedik. Bu ID'in tabloda olup olmadýðýný kontrol ettik.
									System.out.print("Siparis etmek istediðiniz ürünün ID'sini giriniz:");
									siparis.setUrunID(in.nextInt());
							}while(Database.recordAvailable(siparis.getUrunID(), "urun") == false);
							
							
							//siparis tablosuna siparisTarihini aldýðý günü kaydettik.
							siparis.setSiparisTarih(bicim.format(tarih));
							
							//stokta o ürünün yeterli sayýda olup olmadýðýný kontol ettik
							System.out.println("Ýstediðiniz ürün stokta kontrol ediliyor..");
							if(Database.stokTotal(siparis.getUrunID()) > 0){
								System.out.println("Ürün mevcut siparis kaydý oluþturuluyor..");
								Database.siparisInsert(siparis);
								
								//Stok tablosunu güncelledik
								Stok stok = new Stok();
								stok.setUrunID(siparis.getUrunID());
								stok.setUrunAdet(Database.stokTotal(siparis.getUrunID()) - 1);
								Database.stokUpdate(stok);
							}
							else{
								System.out.println("Ýstediðiniz ürünün adeti stokta mevcut deðil!!");
							}
						}

						if(islem == 2){
							Database.MusteriSiparisList(login.getMusteriID());
						}
						
						if(islem == 3){
							int siparisID;
							System.out.println("Siparis silme iþlemi baþlatýlýyor..");
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
