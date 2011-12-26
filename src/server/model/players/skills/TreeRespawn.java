package server.model.players.skills;

import server.Server;
import server.model.players.*;

/**
* @Coded by Xaero
*/

public class TreeRespawn {

	public TreeRespawn(int i){
		this.treeId = i;
	}

	public long respawnDelay;
	public int respawnTimer;
	public boolean respawnRequired;
	public int respawnTree;
	public int respawnX;
	public int respawnY;
	public int stumpId;
	public int stumpX;
	public int stumpY;
	public int treeId;

	public void respawnTree(){
			if(this.respawnRequired){
				if(System.currentTimeMillis() - this.respawnDelay > 1000 && this.respawnTimer > 0){
					this.respawnTimer--;
					this.respawnDelay = System.currentTimeMillis();
					return;
				}
				if(this.respawnTimer == 0){
					for(int a = 0; a < Server.playerHandler.players.length; a++){
						Client all = (Client)Server.playerHandler.players[a];
						if(all != null){
							all.getPA().object(this.respawnTree, this.respawnX, this.respawnY, 0, 10);
						}
					}
					Server.trees[this.treeId] = null;
				}
			}
	}

}