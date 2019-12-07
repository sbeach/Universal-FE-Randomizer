package fedata.gcnwii.fe9;

public class FE9Data {
	
	public static final String FriendlyName = "Fire Emblem: Path of Radiance";
	public static final String GameCode = "GFEE01";

	public static final long CleanCRC32 = 0xF24CB38AL;
	public static final long CleanSize = 1459978240L;
	
	// GCN and above start to use different files in file systems, so these
	// offsets are going to be read from files.
	
	public static final long CharacterDataStartOffset = 0x30;
	public static final String CharacterDataFilename = "system.cmp/FE8Data.bin";
	public static final int CharacterCount = 0x154; // Maybe?
	public static final int CharacterDataSize = 0x54;
	
	public static final long ClassDataStartOffset = 0x6FC4;
	public static final String ClassDataFilename = "system.cmp/FE8Data.bin";
	public static final int ClassCount = 0x73;
	public static final int ClassDataSize = 0x64;
	
	public static final long ItemDataStartOffset = 0x9CB4;
	public static final String ItemDataFilename = "system.cmp/FE8Data.bin";
	public static final int ItemCount = 0xBD;
	public static final int ItemDataSize = 0x60;
	
	public static final long SkillDataStartOffset = 0xE398;
	public static final String SkillDataFilename = "system.cmp/FE8Data.bin";
	public static final int SkillCount = 0x62;
	public static final int SkillDataSize = 0x28;
	
	public static final long CommonTextDataStartOffset = 0x155CC;
	public static final long CommonTextIDStartOffset = 0x1A4DC;
	public static final String CommonTextFilename = "mess/common.m";
	public static final int CommonTextCount = 0x9E2;
	public static final int CommonTextEntrySize = 0x8;

}
