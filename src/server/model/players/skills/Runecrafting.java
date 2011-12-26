    package server.model.players.skills;

    import server.Config;
    import server.model.players.Client;
    /**
     * RuneCrafting.java
     *
     * @author Sanity
     *
     **/

    public class Runecrafting {

            private Client c;

            public Runecrafting(Client c) {
                    this.c = c;
            }

        /**
         * Essence's  ID constants.
         */
        private static final int RUNE_ESS = 1436;
        private static final int PURE_ESS = 7936;

        /**
         * An array c


	/**
	 * Handles the Runes being crafted, also about the Multiple Runes (200 = "Not")
	 */
	public static enum Runes {
		AIR_RUNE(556, 1, 11, 22, 33, 44, 55, 66, 77, 88, 99, 5, 10, 2478),
		MIND_RUNE(558, 2, 14, 28, 42, 56, 70, 84, 98, 200, 200, 5, 11, 2479),
		WATER_RUNE(555, 5, 19, 38, 57, 76, 95, 200, 200, 200, 200, 6, 12, 2480),
		EARTH_RUNE(557, 9, 26, 52, 78, 104, 200, 200, 200, 200, 200, 6, 13, 2481),
		FIRE_RUNE(554, 14, 35, 70, 105, 200, 200, 200, 200, 200, 200, 7, 14, 2482),
		BODY_RUNE(559, 20, 46, 92, 200, 200, 200, 200, 200, 200, 200, 7, 15, 2483),
		COSMIC_RUNE(564, 27, 59, 108, 200, 200, 200, 200, 200, 200, 200, 8, 16, 2484),
		CHAOS_RUNE(562, 35, 74, 200, 200, 200, 200, 200, 200, 200, 200, 8, 17, 2487),
		ASTRAL_RUNE(9075, 40, 82, 200, 200, 200, 200, 200, 200, 200, 200, 9, 17, 17010),
		NATURE_RUNE(561, 44,  91, 200, 200, 200, 200, 200, 200, 200, 200, 9, 18, 2486),
		LAW_RUNE(563, 54, 54, 200, 200, 200, 200, 200, 200, 200, 200, 10, 19, 2485),
		DEATH_RUNE(560, 65, 200, 200, 200, 200, 200, 200, 200, 200, 200, 10, 20, 2488),
		BLOOD_RUNE(565, 77, 200, 200, 200, 200, 200, 200, 200, 200, 200, 11, 21, 30624),
		SOUL_RUNE(566, 90, 200, 200, 200, 200, 200, 200, 200, 200, 200, 11, 22, 2489);

	public int itemId, levelRequired, doubleRunes, tripleRunes, quadRunes, fiveRunes, sixRunes, sevenRunes, eightRunes, nineRunes, tenRunes, exp, zmiExp, altarId;

	private Runes(int itemId, int levelRequired, int doubleRunes, int tripleRunes, int quadRunes, int fiveRunes, int sixRunes, int sevenRunes, int eightRunes, int nineRunes, int tenRunes, int exp, int zmiExp, int altarId) {
		this.itemId = itemId;
		this.levelRequired = levelRequired;
		this.doubleRunes = doubleRunes;
		this.tripleRunes = tripleRunes;
		this.quadRunes = quadRunes;
		this.fiveRunes = fiveRunes;
		this.sixRunes = sixRunes;
		this.sevenRunes = sevenRunes;
		this.eightRunes = eightRunes;
		this.nineRunes = nineRunes;
		this.tenRunes = tenRunes;
		this.exp = exp;
		this.zmiExp = zmiExp;
		this.altarId = altarId;
	}
}
		public void HandleObjectClick() {
			for (Runes r : Runes.values()) {
				if (c.objectId == r.altarId) {
					c.getRunecrafting().craftRunes(c.objectId);
				}
		}
	}

            /**
         * Checks through all 28 item inventory slots for the specified item.
         */
        private boolean itemInInv(int itemID, int slot, boolean checkWholeInv) {
                    if (checkWholeInv) {
                            for (int i = 0; i < 28; i++) {
                                    if (c.playerItems[i] == itemID + 1) {
                                            return true;
                    }
                            }
                    } else {
                            if (c.playerItems[slot] == itemID + 1) {
                                    return true;
                            }
                    }
            return false;
        }

        /**
         * Replaces essence in the inventory with the specified rune.
         */
        private void replaceEssence(int essType, int runeID, int multiplier) {
			int exp = 0;
			for (int i = 0; i < 28; i++) {
				if (itemInInv(essType, i, false)) {
                    c.getItems().deleteItem(essType, i, 1);
                    c.getItems().addItem(runeID, 1 * multiplier);
					for (Runes r : Runes.values()) {
                    exp += r.exp;
				}
                }
            }
            c.getPA().addSkillXP(exp * Config.RUNECRAFTING_EXPERIENCE, c.playerRunecrafting);
        }

        /**
         * Crafts the specific rune.
         */
 public void craftRunes(int altarID) {

 for (Runes r : Runes.values()) {
 int runeID = 0;
                if (altarID == r.altarId) {
					runeID = r.itemId;
                }
                if (runeID == r.itemId) {
                   if (c.playerLevel[20] >= r.levelRequired) {
                        if (c.getItems().playerHasItem(RUNE_ESS) || c.getItems().playerHasItem(PURE_ESS)) {
							int multiplier = 1;
								if (c.playerLevel[20] >= r.doubleRunes) {
									multiplier += 1;
                                }
                                if (c.playerLevel[20] >= r.tripleRunes) {
									multiplier += 1;
                                }
                                if (c.playerLevel[20] >= r.tripleRunes) {
									multiplier += 1;
                                }
                                if (c.playerLevel[20] >= r.fiveRunes) {
									multiplier += 1;
                                }
                                if (c.playerLevel[20] >= r.sixRunes) {
									multiplier += 1;
                                }
                                if (c.playerLevel[20] >= r.sevenRunes) {
									multiplier += 1;
                                }
                                if (c.playerLevel[20] >= r.eightRunes) {
									multiplier += 1;
                                }
                                if (c.playerLevel[20] >= r.nineRunes) {
									multiplier += 1;
                                }
                                if (c.playerLevel[20] >= r.tenRunes) {
									multiplier += 1;
                                }
                            replaceEssence(RUNE_ESS, runeID, multiplier);
							 replaceEssence(PURE_ESS, runeID, multiplier);
                            c.startAnimation(791);
                            //c.frame174(481, 0, 0); for sound
                            c.gfx100(186);
                            return;
                        }
                        c.sendMessage("You need to have essence to craft runes!");
                        return;
                    }
                    c.sendMessage("You need a Runecrafting level of "+ r.levelRequired +" to craft this rune.");
                }
           }
         }
    }
