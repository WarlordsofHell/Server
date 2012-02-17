package server.model.players;

public class PotionMixing {

	Client c;

	public PotionMixing(final Client c) {
		this.c = c;
	}

	public void mixPotion2(final int id, final int id2) {
		final String id11 = this.c.getItems().getItemName(id);
		final String id22 = this.c.getItems().getItemName(id2);
		if (id11.substring(0, id11.indexOf("(")).equalsIgnoreCase(
				id22.substring(0, id22.indexOf("(")))) {
			try {
				int amount1 = Integer.parseInt(id11.substring(
						id11.indexOf("(") + 1, id11.indexOf("(") + 2));
				int amount2 = Integer.parseInt(id22.substring(
						id22.indexOf("(") + 1, id22.indexOf("(") + 2));
				final int totalAmount = amount1 + amount2;
				if (totalAmount > 4) {
					amount1 = 4;
					amount2 = totalAmount - 4;
					final String item1 = id11.substring(0,
							id11.indexOf("(") + 1) + amount1 + ")";
					final String item2 = id11.substring(0,
							id11.indexOf("(") + 1) + amount2 + ")";
					this.c.getItems().deleteItem(id,
							this.c.getItems().getItemSlot(id), 1);
					this.c.getItems().deleteItem(id2,
							this.c.getItems().getItemSlot(id2), 1);
					this.c.getItems().addItem(
							this.c.getItems().getItemId(item1), 1);
					this.c.getItems().addItem(
							this.c.getItems().getItemId(item2), 1);
				} else {
					amount1 = totalAmount;
					final String item1 = id11.substring(0,
							id11.indexOf("(") + 1) + amount1 + ")";
					this.c.getItems().deleteItem(id,
							this.c.getItems().getItemSlot(id), 1);
					this.c.getItems().deleteItem(id2,
							this.c.getItems().getItemSlot(id2), 1);
					this.c.getItems().addItem(
							this.c.getItems().getItemId(item1), 1);
					this.c.getItems().addItem(229, 1);
				}
			} catch (final Exception e) {
				e.printStackTrace();
			}
		}
	}

	public int[] notedPotions = { 3025, 3027, 3029, 3031, 140, 142, 144, 2435 };

	private final int[][] potMixing = { { 117, 117, 113, 227 },
			{ 119, 115, 113, 227 }, { 115, 119, 113, 227 },
			{ 123, 123, 2428, 227 }, { 121, 125, 2428, 227 },
			{ 125, 121, 2428, 227 }, { 133, 133, 2432, 227 },
			{ 133, 137, 2432, 227 }, { 137, 133, 2432, 227 },
			{ 159, 159, 2440, 227 }, { 157, 161, 2440, 227 },
			{ 161, 157, 2440, 227 }, { 147, 147, 2436, 227 },
			{ 149, 145, 2436, 227 }, { 145, 149, 2436, 227 },
			{ 165, 165, 2442, 227 }, { 163, 167, 2442, 227 },
			{ 167, 163, 2442, 227 }, { 2442, 227 }, { 2442, 227 },
			{ 171, 171, 2444, 227 }, { 169, 173, 2444, 227 },
			{ 173, 169, 2444, 227 }, { 177, 177, 2446, 227 },
			{ 179, 175, 2446, 227 }, { 175, 179, 2446, 227 },
			{ 3028, 3028, 3024, 227 }, { 3030, 3026, 3024, 227 },
			{ 3026, 3030, 3024, 227 }, { 141, 141, 2434, 227 },
			{ 139, 143, 2434, 227 }, { 143, 139, 2434, 227 },
			{ 3044, 3044, 3040, 227 }, { 3042, 3046, 3040, 227 },
			{ 3046, 3042, 3040, 227 }, { 2456, 2456, 2452, 227 },
			{ 2454, 2458, 2452, 227 }, { 2458, 2454, 2452, 227 } };

	public void mixPotion(final int id, final int id2) {
		for (final int[] element : this.potMixing) {
			if (((element[0] == id) && (element[1] == id))
					|| ((element[1] == id) && (element[0] == id2))) {
				this.c.getItems().deleteItem(id,
						this.c.getItems().getItemSlot(id), 1);
				this.c.getItems().deleteItem(id2,
						this.c.getItems().getItemSlot(id2), 1);
				this.c.getItems().addItem(element[2], 1);
				this.c.getItems().addItem(element[3], 1);
				break;
			}
		}
	}

}