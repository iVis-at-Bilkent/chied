package org.gvt.editpart;

import java.beans.PropertyChangeEvent;
import java.util.List;

import org.eclipse.draw2d.*;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.*;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.gvt.*;
import org.gvt.editpolicy.ChsComponentEditPolicy;
import org.gvt.editpolicy.ChsDirectEditPolicy;
import org.gvt.editpolicy.ChsGraphicalNodeEditPolicy;
import org.gvt.figure.*;
import org.gvt.model.NodeModel;
import org.gvt.model.EdgeModel;

/**
 * This class maintains the editpart for Nodes.
 *
 * @author Cihan Kucukkececi
 *
 * Copyright: i-Vis Research Group, Bilkent University, 2007 - present
 */
public class ChsNodeEditPart extends EditPartWithListener
	implements NodeEditPart
{
	ChsDirectEditManager directManager = null;

	protected IFigure createFigure()
	{
		NodeModel model = getNodeModel();
		NodeFigure nFigure = new NodeFigure(model.getLocationAbs(),
			model.getSize(),
			model.getText(),
			model.getTextFont(),
			model.getTextColor(),
			model.getColor(),
			model.getBorderColor(),
			model.getShape(),
			model.getHighlightColor(),
			model.isHighlight());

		nFigure.updateHighlight(
			(HighlightLayer) getLayer(HighlightLayer.HIGHLIGHT_LAYER),
			getNodeModel().isHighlight());
		
		return nFigure;
	}

	public DragTracker getDragTracker(Request request)
	{
		return new ChsDragEditPartsTracker(this);
	}

	public void performRequest(Request req)
	{
		if (req.getType().equals(RequestConstants.REQ_DIRECT_EDIT))
		{
			performDirectEdit();

			return;
		}

		super.performRequest(req);
	}

	private void performDirectEdit()
	{
		if (directManager == null)
		{
			directManager =
				new ChsDirectEditManager(
					this,
					TextCellEditor.class,
					new ChsCellEditorLocator(getFigure()));
		}

		directManager.show();
	}

	protected void createEditPolicies()
	{
		installEditPolicy(EditPolicy.COMPONENT_ROLE,
			new ChsComponentEditPolicy());
		installEditPolicy(
			EditPolicy.DIRECT_EDIT_ROLE,
			new ChsDirectEditPolicy());
		installEditPolicy(
			EditPolicy.GRAPHICAL_NODE_ROLE,
			new ChsGraphicalNodeEditPolicy());
	}

	public void propertyChange(PropertyChangeEvent evt)
	{
		if (evt.getPropertyName().equals(NodeModel.P_CONSTRAINT))
		{
			refreshVisuals();
		}
		else if (evt.getPropertyName().equals(NodeModel.P_TEXT))
		{
			((NodeFigure)figure).updateText((String) evt.getNewValue());
		}
		else if (evt.getPropertyName().equals(NodeModel.P_TEXTFONT))
		{
			((NodeFigure)figure).updateTextFont((Font) evt.getNewValue());
		}
		else if (evt.getPropertyName().equals(NodeModel.P_TEXTCOLOR))
		{
			((NodeFigure)figure).updateTextColor((Color) evt.getNewValue());
		}
		else if (evt.getPropertyName().equals(NodeModel.P_COLOR))
		{
			((NodeFigure)figure).updateColor((Color) evt.getNewValue());
		}
		else if (evt.getPropertyName().equals(NodeModel.P_BORDERCOLOR))
		{
			((NodeFigure)figure).updateBorderColor((Color) evt.getNewValue());
		}
		else if (evt.getPropertyName().equals(NodeModel.P_SHAPE))
		{
			((NodeFigure)figure).updateShape((String) evt.getNewValue());
		}
		else if (evt.getPropertyName().equals(NodeModel.P_CONNX_SOURCE))
		{
			refreshSourceConnections();
		}
		else if (evt.getPropertyName().equals(NodeModel.P_CONNX_TARGET))
		{
			refreshTargetConnections();
		}
		else if (evt.getPropertyName().equals(EdgeModel.P_HIGHLIGHT))
		{
			((NodeFigure)figure).updateHighlight(
				(Layer) getLayer(HighlightLayer.HIGHLIGHT_LAYER),
				getNodeModel().isHighlight());
		}
		else if (evt.getPropertyName().equals(EdgeModel.P_HIGHLIGHTCOLOR))
		{
			((NodeFigure)figure).
				updateHighlightColor((Color) evt.getNewValue());
		}
	}

	protected void refreshVisuals()
	{
		Rectangle constraint = getNodeModel().getConstraint();

		((GraphicalEditPart) getParent()).setLayoutConstraint(
			this,
			getFigure(),
			constraint);
	}

	protected List getModelSourceConnections()
	{
		return getNodeModel().getSourceConnections();
	}

	protected List getModelTargetConnections()
	{
		return getNodeModel().getTargetConnections();
	}

	public ConnectionAnchor getSourceConnectionAnchor(ConnectionEditPart connection)
	{
		return new ChsChopboxAnchor(getFigure());
	}

	public ConnectionAnchor getTargetConnectionAnchor(ConnectionEditPart connection)
	{
		return new ChsChopboxAnchor(getFigure());
	}

	public ConnectionAnchor getSourceConnectionAnchor(Request request)
	{
		return new ChsChopboxAnchor(getFigure());
	}

	public ConnectionAnchor getTargetConnectionAnchor(Request request)
	{
		return new ChsChopboxAnchor(getFigure());
	}
}