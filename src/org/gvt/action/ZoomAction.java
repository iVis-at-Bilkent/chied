package org.gvt.action;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.gvt.ChisioMain;
import org.gvt.editpart.ChsRootEditPart;
import org.gvt.editpart.ChsScalableRootEditPart;
import org.gvt.model.CompoundModel;

/**
 * Action for zoom operation. Zoom in, out, Fit in window operations are done
 * with this action. Fit in window is rewritten because of the improvement of
 * action. Now makes a marquee zoom to complete graph in fit in window
 * operation.
 *
 * @author Cihan Kucukkececi
 *
 * Copyright: i-Vis Research Group, Bilkent University, 2007 - present
 */
public class ZoomAction extends Action
{
	private static final double zoomStep = 1.1;

	private int zoom;

	private ChisioMain main;

	private Point clickLocation;

	/**
	 * Constructor
	 *
	 * @param main		  Main application
	 * @param zoom		  ZOOM IN, ZOOM OUT, FIT IN WINDOW or ZOOM BY %
	 * @param clickLocation ZOOM LOCATION
	 */
	public ZoomAction(ChisioMain main, int zoom, Point clickLocation)
	{
		this.main = main;
		this.zoom = zoom;
		this.clickLocation = clickLocation;

		if (zoom == 1)
		{
			super.setText("Zoom In");
			super.setImageDescriptor(
				ImageDescriptor.createFromFile(ChisioMain.class,
					"icon/zoom-in.gif"));
			this.setToolTipText("Zoom In");
		}
		else if (zoom == -1)
		{
			super.setText("Zoom Out");
			super.setImageDescriptor(
				ImageDescriptor.createFromFile(ChisioMain.class,
					"icon/zoom-out.gif"));
			this.setToolTipText("Zoom Out");
		}
		else if (zoom == 0)
		{
			super.setText("Fit in Window");
			super.setImageDescriptor(
				ImageDescriptor.createFromFile(ChisioMain.class,
					"icon/zoom-fit.gif"));
			this.setToolTipText("Fit in Window");
		}
		else
		{
			super.setText(zoom + "%");
		}
	}

	public void run()
	{
		double zoomLevel = ((ChsScalableRootEditPart) this.main.getViewer().
			getRootEditPart()).getZoomManager().getZoom();

		super.run();

		if (this.zoom == 0) // Fit in Window
		{
			// Reset zoom properties to initial values
			((ChsScalableRootEditPart) this.main.getViewer().getRootEditPart()).
				getZoomManager().setZoom(1);
			((ChsScalableRootEditPart) this.main.getViewer().getRootEditPart()).
				getZoomManager().setViewLocation(new Point(0, 0));

			// Calculate the marque zoom rectangle to zoom.
			CompoundModel root = (CompoundModel)
				((ChsRootEditPart)this.main.getViewer().getRootEditPart().
					getChildren().get(0)).getModel();
			Rectangle r = root.calculateBounds();

			// Graph Margins are added
			r.expand(CompoundModel.MARGIN_SIZE, CompoundModel.MARGIN_SIZE);

			// Zoom to rectangle to fit in window
			((ChsScalableRootEditPart) this.main.getViewer().getRootEditPart()).
				getZoomManager().zoomTo(r);
		}
		else
		{
			if (this.zoom == 1) // Zoom in
			{
				zoomLevel *= zoomStep;
			}
			else if (this.zoom == -1) // Zoom out
			{
				zoomLevel /= zoomStep;
			}
			else // Level is given in terms of percentage
			{
				zoomLevel = this.zoom / 100.0;
			}

			if (this.clickLocation !=  null)
			{
				new CenterViewAction(this.main, this.clickLocation).run();
			}

			((ChsScalableRootEditPart) this.main.getViewer().getRootEditPart()).
				getZoomManager().setZoom(zoomLevel);
		}

		// Selection Tool is selected
		this.main.getViewer().getEditDomain().setActiveTool(
			this.main.getViewer().getEditDomain().getDefaultTool());

		this.main.getHighlightLayer().refreshHighlights();
	}
}