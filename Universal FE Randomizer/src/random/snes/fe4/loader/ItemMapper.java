package random.snes.fe4.loader;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import fedata.snes.fe4.FE4Data;
import io.FileHandler;
import util.DebugPrinter;
import util.Diff;
import util.DiffCompiler;
import util.recordkeeper.RecordKeeper;

public class ItemMapper {
	
	private Map<Integer, FE4Data.Item> playerEquipmentIDToItem;
	
	private boolean isHeadered;
	
	public static final String RecordKeeperCategoryKey = "Item Map - Rings";
	
	public ItemMapper(FileHandler handler, boolean isHeadered) {
		super();
		this.isHeadered = isHeadered;
		
		initializeMap(handler);
	}
	
	public FE4Data.Item getItemAtIndex(int index) {
		if (index == 0) { return null; }
		return playerEquipmentIDToItem.get(index);
	}
	
	public void setItemAtIndex(int indexToReplace, FE4Data.Item newItem) {
		if (newItem == null) {
			System.err.println("Attempting to set an unknown item to player equipment.");
			return;
		}
		
		if (indexToReplace == 0) {
			System.err.println("Attempting to set item 0.");
			return;
		}
		
		FE4Data.Item oldItem = playerEquipmentIDToItem.get(indexToReplace);
		if (oldItem == null) {
			System.err.println("Attempting to set item to invalid index.");
		} else {
			playerEquipmentIDToItem.put(indexToReplace, newItem);
			DebugPrinter.log(DebugPrinter.Key.FE4_ITEM_MAPPER, "Setting item at index 0x" + Integer.toHexString(indexToReplace).toUpperCase() + " to " + newItem.toString());
		}
	}
	
	public Set<Integer> allIndices() {
		Set<Integer> indices = playerEquipmentIDToItem.keySet();
		indices.remove(0);
		return indices;
	}
	
	private void initializeMap(FileHandler handler) {
		DebugPrinter.log(DebugPrinter.Key.FE4_ITEM_MAPPER, "Reading item map...");
		
		playerEquipmentIDToItem = new HashMap<Integer, FE4Data.Item>();
		
		long baseOffset = FE4Data.PlayerItemMappingTableOffset;
		int count = FE4Data.PlayerItemMappingTableCount;
		
		for (int i = 0; i < count; i++) {
			long offset = baseOffset + (i * FE4Data.PlayerItemMappingTableItemSize);
			if (!isHeadered) {
				offset -= 0x200;
			}
			byte[] data = handler.readBytesAtOffset(offset, FE4Data.PlayerItemMappingTableItemSize);
			if (data != null && data.length > 0) {
				int itemID = data[0] & 0xFF;
				FE4Data.Item item = FE4Data.Item.valueOf(itemID);
				if (item != null) {
					DebugPrinter.log(DebugPrinter.Key.FE4_ITEM_MAPPER, "Loaded Item " + item.toString() + " into index 0x" + Integer.toHexString(i).toUpperCase());
					playerEquipmentIDToItem.put(i, item);
				} else {
					System.err.println("Unknown item encountered. Item ID = 0x" + Integer.toHexString(itemID));
				}
			}	
		}
		
		DebugPrinter.log(DebugPrinter.Key.FE4_ITEM_MAPPER, "Finished reading item map!");
	}

	public void commitChanges() {
		
	}
	
	public void compileDiff(DiffCompiler compiler) {
		long baseOffset = FE4Data.PlayerItemMappingTableOffset;
		
		for (int i : playerEquipmentIDToItem.keySet()) {
			long offset = baseOffset + (i * FE4Data.PlayerItemMappingTableItemSize);
			if (!isHeadered) {
				offset -= 0x200;
			}
			FE4Data.Item item = playerEquipmentIDToItem.get(i);
			compiler.addDiff(new Diff(offset, FE4Data.PlayerItemMappingTableItemSize, new byte[] {(byte)item.ID}, null));
		}
	}
	
	public void recordRingMap(RecordKeeper rk, Boolean isInitial) {
		for (int index : allIndices()) {
			FE4Data.Item item = playerEquipmentIDToItem.get(index);
			if (item.isRing()) {
				recordData(rk, isInitial, "Ring List", String.format("0x%s", Integer.toHexString(index).toUpperCase()), item.toString());
			}
		}
	}
	
	private void recordData(RecordKeeper rk, boolean isInitial, String entryKey, String key, String value) {
		if (isInitial) {
			rk.recordOriginalEntry(RecordKeeperCategoryKey, entryKey, key, value);
		} else {
			rk.recordUpdatedEntry(RecordKeeperCategoryKey, entryKey, key, value);
		}
	}
}