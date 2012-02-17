package server.model.players.packets;

import server.model.players.Client;
import server.util.Misc;
import server.Server;

import java.util.ArrayList;
import java.io.File;
import java.io.*;
import java.util.Properties;
import java.io.FileInputStream;



/**
 *
 * @authors PJNoMore
 *
 */

public class PJClans {

	Properties p = new Properties();
	int saveCount = 0;
	
	public void initialize() {
		loadClans();
	}
	
	public void process() {
		if(saveCount == 30) {
			saveClans();
			//checkClans();
			saveCount = 0;
		}
		saveCount++;
	}
	/*int[] checkEmpty = new int[50];
	public void checkClans() {
		for(int i = 0; i < Server.clanChat.clans.length; i++){
			if(Server.clanChat.clans[i] != null){
				for(int j = 0; j < Server.clanChat.clans[i].members.length; j++){
					if(Server.clanChat.clans[i].members[j] == -1){
						checkEmpty[i]++;
						if(checkEmpty[i] > 49 &&){
							Server.clanChat.destructClan(i);
						}
					}
				}
			}
		}
	}*/
	
	public void saveClans() {
		for(int i = 0; i < Server.clanChat.clans.length; i++) {
			if(Server.clanChat.clans[i] != null) {
				String owner = Server.clanChat.clans[i].owner;
				String name = Server.clanChat.clans[i].name;
				String password = Server.clanChat.clans[i].password;
				boolean lootshare = Server.clanChat.clans[i].lootshare;
				boolean hasPass = Server.clanChat.clans[i].hasPassword;
			
				int loot = 0;
				int pass = 0;
			
				if (lootshare) {
					loot = 1;
				} else {
					loot = 0;
				}
				
				if (hasPass) {
					pass = 1;
				} else {
					pass = 0;
				}
			
				try {
					BufferedWriter out = new BufferedWriter(new FileWriter("./Data/Clans/"+name+".ini"));
					try {
						out.write("owner="+owner);
						out.newLine();
						out.write("name="+name);
						out.newLine();
						out.write("password="+password);
						out.newLine();
						out.write("lootshare="+loot);
						out.newLine();
						out.write("haspass="+pass);
					} finally {
						out.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
				//System.out.println("Clan saved! Owner: "+owner+" Name: "+name+" Password: "+password+" Lootshare: "+loot);
			}
		}
	}
	
	public void saveClan(String owner, String name, String password, boolean lootshare, boolean hasPass){
				int loot = 0;
				int pass = 0;
				if (lootshare) { loot = 1; } 
				else { loot = 0; }
				if (hasPass) { pass = 1; } 
				else { pass = 0; }
			
				try {
					BufferedWriter out = new BufferedWriter(new FileWriter("./Data/Clans/"+name+".ini"));
					try {
						out.write("owner="+owner);
						out.newLine();
						out.write("name="+name);
						out.newLine();
						out.write("password="+password);
						out.newLine();
						out.write("lootshare="+loot);
						out.newLine();
						out.write("haspass="+pass);
					} finally {
						out.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
				//System.out.println("Clan created! Owner: "+owner+" Name: "+name+" Password: "+password+" Lootshare: "+loot);
	}
	
	public void loadClans() {
		File clanDir = new File("./Data/Clans/");
		File[] files = clanDir.listFiles();
		int numClans = files.length;
		boolean loot = false;
		boolean pass = false;
		
		for(int i = 0; i < numClans; i++) {
			String clanFile = files[i].toString();
			try {
				p.load(new FileInputStream(clanFile));
				if(p.getProperty("lootshare").equals(1)){
					loot = true;
				} else {
					loot = false;
				}
				if(p.getProperty("haspass").equals(1)){
					pass = true;
				} else {
					pass = false;
				}
				Server.clanChat.createClan(
					p.getProperty("owner"), 
					p.getProperty("name"), 
					p.getProperty("password"),
					loot,
					pass
				);
				//System.out.println("Clan loaded! Owner: "+p.getProperty("owner")+" Name: "+p.getProperty("name")+" Password: "+p.getProperty("password")+" Lootshare: "+loot);
			} catch(Exception e) {}
		}
	}
	
	
}