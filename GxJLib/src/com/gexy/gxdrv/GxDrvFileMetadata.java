package com.gexy.gxdrv;

import java.util.ArrayList;

public class GxDrvFileMetadata {
	public String name;
	public int inode;
	public int type;
	public String mime;
	public boolean inUse =false;
	public boolean deleted =false;
	public int parentInode;
	public ArrayList<String> tags;
	public String hash;
}
