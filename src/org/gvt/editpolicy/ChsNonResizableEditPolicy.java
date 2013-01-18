package org.gvt.editpolicy;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.PositionConstants;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.Handle;
import org.eclipse.gef.editpolicies.NonResizableEditPolicy;
import org.gvt.figure.ResizeHandle;

/**
 * @author Cihan Kucukkececi
 *
 * Copyright: i-Vis Research Group, Bilkent University, 2007 - present
 */
public class ChsNonResizableEditPolicy extends NonResizableEditPolicy
{
	protected List createSelectionHandles()
	{
		List list = new ArrayList();

		addHandles((GraphicalEditPart) getHost(), list);

		return list;
	}

	static Handle createHandle(GraphicalEditPart owner, int direction)
	{
		ResizeHandle handle = new ResizeHandle(owner, direction, false);
		return handle;
	}

	/**
	 * Fills the given List with handles at each corner
	 * and the north, south, east, and west of the GraphicalEditPart.
	 *
	 * @param part	the owner GraphicalEditPart of the handles
	 * @param handles the List to add the handles to
	 */
	public static void addHandles(GraphicalEditPart part, List handles)
	{
		handles.add(createHandle(part, PositionConstants.SOUTH_EAST));
		handles.add(createHandle(part, PositionConstants.SOUTH_WEST));
		handles.add(createHandle(part, PositionConstants.NORTH_WEST));
		handles.add(createHandle(part, PositionConstants.NORTH_EAST));
	}
}