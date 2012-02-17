package server.model.npcs;	

import server.Server;

public class EmoteHandler {
	public static int getCombatEmote(int i, String type) {
		int attack = -1, dead = 2304;
		String name = Server.npcHandler.getNpcListName(Server.npcHandler.npcs[i].npcType);
		if (name.toLowerCase().contains("frog")) {
			attack = 1793;
			dead = 1795;
		} else if (name.toLowerCase().contains("cave slime")) {
			attack = 1788;
			dead = 1792;
		} else if (name.toLowerCase().contains("wolf") && !name.toLowerCase().contains("werewolf")) {
			attack = 6581;
			dead = 6576;
		}
		switch (Server.npcHandler.npcs[i].npcType) {
			case 1676: // Experiment
				attack = 1626;
				dead = 1628;
			break;
			case 1265: // Experiment
				attack = 1312;
				dead = 1314;
			break;
			case 1677: // Experiment
				attack = 1616;
				dead = 1618;
			break;
			case 1678: // Experiment
				attack = 1612;
				dead = 1611;
			break;
			case 8324: // Elite Black Knight
				attack = 10854;
				dead = 10856;
			break;
			case 9463: case 9465: case 9467: // Strykewyrms
				attack = 12791;
				dead = 12793;
			break;
			case 19: // White knight
				attack = 7041;
			break;
			case 7133: // Bork
				attack = 8754;
				dead = 8756;
			break;
			case 10604: case 10773: // New dragons
				attack = 13151;
				dead = 13153;
			break;
			case 50: case 53: case 54: case 55: case 941: case 1590: case 1591: case 1592: // Dragons
				attack = 80;
				dead = 92;
			break;
			case 8133:
				dead = 10059;
			break;
			case 51: case 52: case 1589: case 3376: // Baby dragons
				attack = 25;
				dead = 28;
			break;
			case 82: case 83: case 84: case 1472: // Demons
				attack = 64;
				dead = 67;
			break;
			case 118: case 119:
				attack = 99;
				dead = 102;
			break;
			case 122: case 123: // Hobgoblin
				attack = 164;
				dead = 167;
			break;
			case 125: case 178: // Warriors
				attack = 451;
			break;
			case 2361: case 2362: case 1183:// Elf warrior (ranged)
				attack = 426;
			break;
			case 1338: case 1340: case 1342: // Dagganoths
				attack = 1341;
				dead = 1342;
			break;
			case 1605: // Abberant spectre
				attack = 1507;
				dead = 1508;
			break;
			case 1610: case 1611: // Gargoyle
				attack = 1519;
				dead = 1518;
			break;
			case 1612: // Banshee
				attack = 1525;
				dead = 1524;
			break;
			case 1613: // Nechryael
				attack = 1528;
				dead = 1530;
			break;
			case 1615: // Abyssal demon
				attack = 1537;
				dead = 1538;
			break;
			case 1616: // Basilisk
				attack = 1546;
				dead = 1548;
			break;
			case 1618: case 1619: // Bloodveld
				attack = 1551;
				dead = 1553;
			break;
			case 1620: case 1621: // Cockatrice
				attack = 1562;
				dead = 1563;
			break;
			case 1624: // Dust devil
				attack = 1557;
				dead = 1558;
			break;
			case 1626: case 1627: case 1628: case 1629: case 1630: case 1631: case 1632: // Turoth
				attack = 1595;
				dead = 1597;
			break;
			case 1633: case 1634: case 1635: case 1636: // Elemental fiends
				attack = 1582;
				dead = 1580;
			break;
			case 1653: case 1648: case 1649: case 1650: case 1651: case 1652: case 1654: case 1655: case 1656: case 1657: // Crawling hand
				attack = 1592;
				dead = 1590;
			break;
			case 2031: // Bloodworm
				attack = 2070;
				dead = 2073;
			break;
			case 2783: // Dark beast
				attack = 2731;
				dead = 2733;
			break;
			case 2881: //Daggonoth Supreme
				attack = 2855;
			case 2882: //Daggonoth Prime
				attack = 2854;
			case 2883: // Dagganoth Rex
				attack = 2851;
				dead = 2856;
			break;
			case 3066: // Zombie champion
				attack = 5581;
				dead = 5580;
			break;
			case 3200: // Chaos elemental
				attack = 3147;
				dead = 3147;
			break;
			case 3313: // Tanglefoot
				attack = 3262;
				dead = 3263;
			break;
			case 4397: case 4398: case 4399: // Catablepon
				attack = 4273;
				dead = 4270;
			break;
			case 4418: case 6218: // Gorak
				attack = 4300;
				dead = 4302;
			break;
			case 4463: case 4464: case 4465: // Vampire juvenate
				attack = 7183;
			break;
			case 4527: // Suqah
				attack = 4387;
				dead = 4389;
			break;
			case 4893: // Giant lobster
				attack = 6261;
				dead = 6267;
			break;
			case 4971: // Baby roc
				attack = 5031;
				dead = 5033;
			break;
			case 4972: // Giant roc
				attack = 5024;
				dead = 5027;
			break;
			case 5174: case 5176: case 5181: case 5184: case 5187: case 5190: case 5193: // Ogre chieftain/ Various ogres
				attack = 359;
				dead = 361;
			break;
			case 5213: case 5214: case 5215: case 5216: case 5217: case 5218: case 5219: // Penance fighter
				attack = 5097;
				dead = 5098;
			break;
			case 90: // Skeleton
				attack = 5485;
				dead = 5491;
			break;
			case 5229: case 5230: case 5231: case 5232: case 5233: case 5234: case 5235: case 5236: case 5237: // Penance ranger
				attack = 5396;
				dead = 5397;
			break;
			case 5247: // Penance queen
				attack = 5411;
				dead = 5412;
			break;
			case 75: // Zombie
				attack = 5578;
				dead = 5569;
			break;
			case 2745: // Jad
				if (Server.npcHandler.npcs[i].attackType == 2)
					attack = 9300;
				else if (Server.npcHandler.npcs[i].attackType == 1)
					attack = 9276;
				else if (Server.npcHandler.npcs[i].attackType == 0)
					attack = 9277;
					dead = 9279;
			break;
			case 5248: // Penance spawn
				attack = 5092;
				dead = 5093;
			break;
			case 5385: case 5387: case 5388: case 5389: case 5411: case 5422:// Skeleton (stab)
				attack = 5487;
			case 5386: case 5390: case 5391: case 5392: case 5412: // Skeleton (crush)
				attack = 5485;
				dead = 5491;
			break;
			case 5452: case 5453: case 5454: case 5455: // Icelord
				attack = 5725;
				dead = 5726;
			break;
			case 5627: case 5628: // Sorebones
				attack = 5647;
				dead = 5649;
			break;
			case 5683: case 5691: case 5699: case 5707: case 5715: case 5723: case 5731: case 5739: case 5747: // Undead Lumberjack
				attack = 5970;
				dead = 5972;
			break;
			case 5750: // Cave bug
				attack = 6079;
				dead = 6081;
			break;
			case 5906: // A doubt
				attack = 6310;
				dead = 6315;
			break;
			case 5993: // Experiment No 2
				attack = 6513;
				dead = 6512;
			break;
			case 6212: case 6213: // Werewolf
				attack = 6536;
				dead = 6537;
			break;
			case 110: case 111: case 112: case 113: case 116: case 117: case 4291: case 4292: case 6269: case 6270: // Cyclops and Giants
				attack = 4652;
				dead = 4653;
			break;
			case 6271: case 6272: case 6273: case 6274: // Ork
				attack = 4320;
				dead = 4321;
			break;
			case 6285: case 6293: // Warped terrorbird
				attack = 7108;
				dead = 7109;
			break;
			case 6296: case 6297: // Warped tortoise
				attack = 7093;
				dead = 7091;
			break;
			case 9172: // Aquanite
				attack = 12041;
				dead = 12039;
			break;
		} 
		return (type.equals("Attack")) ? attack : dead;
	}
}