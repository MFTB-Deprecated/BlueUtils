package com.madefromtheblue.blueutils.voxel;

import java.util.ArrayList;

import javax.vecmath.Matrix3f;
import javax.vecmath.Vector3f;

import com.google.common.collect.Lists;
import com.madefromtheblue.blueutils.EnumAxis;

public class AxisSwap
{
	private final Matrix3f matrix;
	public final int id;
	
	public AxisSwap(int id)
	{
		this.id = id;
		if (id < 0 || id >= 48)
		{
			throw new IllegalArgumentException(String.format("%d is not a positive integer less than 48", id));
		}
		ArrayList<Vector3f> avalible = Lists.newArrayList(EnumAxis.X_UP.vector, EnumAxis.Y_UP.vector, EnumAxis.Z_UP.vector);
		int i;
		
		i = id % 6;
		Vector3f x = avalible.get(i / 2);
		if (i % 2 > 0)
		{
			x.negate();
		}
		avalible.remove(i / 2);
		id /= 6;
		
		i = id % 4;
		Vector3f y = avalible.get(i / 2);
		if (i % 2 > 0)
		{
			y.negate();
		}
		avalible.remove(i / 2);
		id /= 4;
		
		i = id % 2;
		Vector3f z = avalible.get(0);
		if (i > 0)
		{
			z.negate();
		}
		this.matrix = new Matrix3f();
		this.matrix.setZero();
		this.matrix.setColumn(0, x);
		this.matrix.setColumn(0, y);
		this.matrix.setColumn(0, z);
	}
	
	public AxisSwap(EnumAxis x, EnumAxis y, EnumAxis z)
	{
		this(x.vector, y.vector, z.vector);
	}
	
	public AxisSwap(Matrix3f matrix)
	{
		this.matrix = (Matrix3f) matrix.clone();
		
		this.id = this.create();
	}
	
	public AxisSwap(Vector3f x, Vector3f y, Vector3f z)
	{
		this.matrix = new Matrix3f();
		this.matrix.setColumn(0, x);
		this.matrix.setColumn(0, y);
		this.matrix.setColumn(0, z);
		
		this.id = this.create();
	}
	
	private int create()
	{
		Vector3f x = new Vector3f();
		Vector3f y = new Vector3f();
		Vector3f z = new Vector3f();
		
		this.matrix.getColumn(0, x);
		this.matrix.getColumn(1, y);
		this.matrix.getColumn(2, z);
		
		boolean valid = true;
		
		int xid = AxisSwap.getColumnId(x);
		int yid = AxisSwap.getColumnId(y);
		int zid = AxisSwap.getColumnId(z);
		
		if (xid / 2 == yid / 2 || yid / 2 == zid / 2 || zid / 2 == xid / 2)
		{
			valid = false;
		}
		
		valid &= Math.abs(this.matrix.determinant()) == 1.0F;
		
		if (!valid)
		{
			throw new IllegalArgumentException("No non-zero component of the rotation matrix can be in the same row or column as another non-zero component. The matrix must also contain only 1s, -1s, and 0s.");
		}
		
		int id = 0;
		id += xid;
		if (yid > xid)
		{
			id += (yid - 2) * 6;
		}
		else
		{
			id += yid * 6;
		}
		id += (zid % 2) * 24;
		return id;
	}
	
	public Matrix3f getMatrix()
	{
		return (Matrix3f) this.matrix.clone();
	}
	
	private static int getColumnId(Vector3f vec)
	{
		if (vec.x > 0)
			return 0;
		else if (vec.x < 0)
			return 1;
		else if (vec.y > 0)
			return 2;
		else if (vec.y < 0)
			return 3;
		else if (vec.z > 0)
			return 4;
		else if (vec.z < 0)
			return 5;
		return -1;
	}

	public Vector3f swap(Vector3f src, Vector3f dst)
	{
		if (dst == null)
		{
			dst = new Vector3f();
		}
		this.matrix.transform(src, dst);
		return dst;
	}
	
	public Vector3f swap(Vector3f in)
	{
		return this.swap(in, null);
	}
	
	public Vector3f apply(Vector3f vec)
	{
		return this.swap(vec, vec);
	}
	
	private Vector3f getDst(int col)
	{
		Vector3f dst = new Vector3f();
		this.matrix.getColumn(col, dst);
		return dst;
	}
	
	public Vector3f getXDst()
	{
		return this.getDst(0);
	}
	
	public Vector3f getYDst()
	{
		return this.getDst(1);
	}
	
	public Vector3f getZDst()
	{
		return this.getDst(2);
	}
	
	public static AxisSwap combine(AxisSwap base, AxisSwap swap)
	{
		Matrix3f out = new Matrix3f();
		out.mul(swap.matrix, base.matrix);
		return new AxisSwap(out);
	}
}
