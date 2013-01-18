package org.gvt.command;

import org.eclipse.gef.commands.Command;
import org.gvt.model.GraphObject;
import org.gvt.model.EdgeModel;
import org.gvt.model.NodeModel;

/**
 * @author Cihan Kucukkececi
 *
 * Copyright: i-Vis Research Group, Bilkent University, 2007 - present
 */
public class ReconnectConnectionCommand extends Command
{
	private EdgeModel connection;

	private NodeModel newSource = null;

	private NodeModel newTarget = null;

	private NodeModel oldSource = null;

	private NodeModel oldTarget = null;


	/* (�� Javadoc)
	 * @see org.eclipse.gef.commands.Command#execute()
	 */
	public void execute()
	{
		if (newSource != null)
		{
			oldSource = connection.getSource();
			reconnectSource(newSource);
		}

		if (newTarget != null)
		{
			oldTarget = connection.getTarget();
			reconnectTarget(newTarget);
		}
	}

	private void reconnectSource(NodeModel source)
	{
		connection.detachSource();
		connection.setSource(source);
		connection.attachSource();
	}

	private void reconnectTarget(NodeModel target)
	{
		connection.detachTarget();
		connection.setTarget(target);
		connection.attachTarget();
	}

	public void setConnectionModel(Object model)
	{
		connection = (EdgeModel) model;
	}

	public void setNewSource(Object model)
	{
		newSource = (NodeModel) model;
	}

	public void setNewTarget(Object model)
	{
		newTarget = (NodeModel) model;
	}

	/* (�� Javadoc)
	 * @see org.eclipse.gef.commands.Command#undo()
	 */
	public void undo()
	{
		if (oldSource != null)
			reconnectSource(oldSource);

		if (oldTarget != null)
			reconnectTarget(oldTarget);

		oldSource = null;
		oldTarget = null;
	}
}