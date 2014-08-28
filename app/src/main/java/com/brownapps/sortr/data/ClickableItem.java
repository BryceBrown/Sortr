package com.brownapps.sortr.data;

public class ClickableItem {
	
	public enum Type{
		None,
		TeamNames,
		PersonList,
	}
	
	public String displayName = "";
	public String minorText = "";
	public Type type = Type.None;
	public long Id = -1;
}
