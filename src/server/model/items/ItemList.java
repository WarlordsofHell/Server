package server.model.items;

public class ItemList {
	public int itemId;
	public String itemName;
	public String itemDescription;
	public double ShopValue;
	public double LowAlch;
	public double HighAlch;
	public int[] Bonuses = new int[100];
	public double[] soakingInfo = new double[3];

	public ItemList(int _itemId) {
		itemId = _itemId;
	}
}
