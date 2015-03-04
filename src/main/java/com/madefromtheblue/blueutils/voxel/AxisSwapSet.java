package com.madefromtheblue.blueutils.voxel;

import com.madefromtheblue.blueutils.EnumAxis;

import gnu.trove.iterator.TByteIterator;
import gnu.trove.set.TByteSet;
import gnu.trove.set.hash.TByteHashSet;

public class AxisSwapSet
{
	public static final AxisSwapSet identity = new AxisSwapSet();
	public static final AxisSwapSet turn = new AxisSwapSet();
	public static final AxisSwapSet slabs = new AxisSwapSet();
	public static final AxisSwapSet sides = new AxisSwapSet();
	public static final AxisSwapSet faces = new AxisSwapSet();
	public static final AxisSwapSet rotatedSlabs;
	public static final AxisSwapSet rotatedSides;
	public static final AxisSwapSet rotatedFaces = new AxisSwapSet();
	public static final AxisSwapSet sideEdges;
	public static final AxisSwapSet edges = new AxisSwapSet();
	public static final AxisSwapSet corners;
	
	static
	{
		identity.addIdentity();
		
		turn.addIdentity();
		turn.add((new AxisSwap(EnumAxis.Z_UP, EnumAxis.Y_UP, EnumAxis.X_DOWN)).id);
		turn.add((new AxisSwap(EnumAxis.X_DOWN, EnumAxis.Y_UP, EnumAxis.Z_DOWN)).id);
		turn.add((new AxisSwap(EnumAxis.Z_DOWN, EnumAxis.Y_UP, EnumAxis.X_UP)).id);
		
		slabs.addIdentity();
		slabs.add((new AxisSwap(EnumAxis.X_UP, EnumAxis.Y_DOWN, EnumAxis.Z_UP)).id);
		
		sides.add((new AxisSwap(EnumAxis.Y_UP, EnumAxis.Z_UP, EnumAxis.X_UP)).id);
		sides.add((new AxisSwap(EnumAxis.X_UP, EnumAxis.Z_UP, EnumAxis.Y_DOWN)).id);
		sides.add((new AxisSwap(EnumAxis.Y_DOWN, EnumAxis.Z_UP, EnumAxis.X_DOWN)).id);
		sides.add((new AxisSwap(EnumAxis.X_DOWN, EnumAxis.Z_UP, EnumAxis.Y_UP)).id);
		
		faces.add(slabs);
		faces.add(sides);
		
		rotatedSlabs = AxisSwapSet.permute(turn, slabs);
		rotatedSides = AxisSwapSet.permute(turn, sides);
		rotatedFaces.add(rotatedSlabs);
		rotatedFaces.add(rotatedSides);
		
		AxisSwapSet edge = new AxisSwapSet();
		edge.add((new AxisSwap(EnumAxis.Y_UP, EnumAxis.Z_UP, EnumAxis.X_UP)).id);
		
		sideEdges = AxisSwapSet.permute(edge, turn);
		edges.add(sideEdges);
		edges.add(rotatedSlabs);
		
		corners = AxisSwapSet.permute(slabs, sideEdges);
	}
	
	private TByteSet swaps = new TByteHashSet();
	
	public void addIdentity()
	{
		this.swaps.add((byte) 0);
	}
	
	public void add(AxisSwapSet add)
	{
		this.swaps.addAll(add.swaps);
	}
	
	public void remove(AxisSwapSet remove)
	{
		this.swaps.removeAll(remove.swaps);
	}
	
	public void add(int id)
	{
		this.swaps.add((byte) id);
	}
	
	public void remove(int id)
	{
		this.swaps.remove((byte) id);
	}
	
	public static AxisSwapSet permute(AxisSwapSet base, AxisSwapSet swaps)
	{
		AxisSwapSet out = new AxisSwapSet();
		TByteIterator swapsi = swaps.swaps.iterator();
		while (swapsi.hasNext())
		{
			AxisSwap ss = new AxisSwap(swapsi.next());
			TByteIterator basei = base.swaps.iterator();
			while (basei.hasNext())
			{
				AxisSwap bs = new AxisSwap(basei.next());
				out.add((byte) AxisSwap.combine(bs, ss).id);
			}
		}
		return out;
	}
}
