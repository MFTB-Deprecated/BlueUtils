package com.madefromtheblue.blueutils;

import javax.vecmath.Vector3f;

public enum EnumAxis
{
	X_UP(1, 0, 0, true),
	X_DOWN(-1, 0, 0, false),
	Y_UP(0, 1, 0, true),
	Y_DOWN(0, -1, 0, false),
	Z_UP(0, 0, 1, true),
	Z_DOWN(0, 0, -1, false);
	
	public final Vector3f vector;
	
	public final boolean up;
	
	EnumAxis(int x, int y, int z, boolean up)
	{
		this.vector = new Vector3f(x, y, z);
		this.up = up;
	}
	
	public EnumAxis getFlip()
	{
		switch (this)
		{
			case X_UP:
				return X_DOWN;
			case Y_UP:
				return Y_DOWN;
			case Z_UP:
				return Z_DOWN;
			case X_DOWN:
				return X_UP;
			case Y_DOWN:
				return Y_UP;
			case Z_DOWN:
				return Z_UP;
		}
		return null;
	}
	
	public EnumAxis getAbs()
	{
		switch (this)
		{
			case X_DOWN:
				return X_UP;
			case Y_DOWN:
				return Y_UP;
			case Z_DOWN:
				return Z_UP;
			default:
				return this;
		}
	}
}
