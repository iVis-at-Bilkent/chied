package org.gvt.figure;

import org.eclipse.draw2d.*;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;

/**
 * This class maintains the edge figure which is the UI of edges. Each edge has
 * its own color, label, style and arrow.
 *
 * @author Cihan Kucukkececi
 *
 * Copyright: i-Vis Research Group, Bilkent University, 2007 - present
 */
public class EdgeFigure extends PolylineConnection
{
	// label of the edge
	Label label;
	// style of edge. SOLID or DASHED
	String style;
	// arrow type of edge. NONE, SOURCE, TARGET or BOTH
	String arrow;
	// array for dash pattern
	int[] dash;
	// width of line
	int width;
	// whether it is highlighted
	boolean highlight;
	// highlight color if highlighted
	Color highlightColor;

	/**
	 * Constructor
	 */
	public EdgeFigure(String text,
		Font textFont,
		Color textColor,
		Color color,
		String style,
		String arrow,
		int width,
		Color highlightColor,
		boolean highlight)
	{
		super();

		this.highlight = highlight;

		this.label = new Label();
		this.label.setOpaque(true);

		MidpointLocator locator = new MidpointLocator(this, 0);
		add(label, locator);

		updateText(text);
		updateTextFont(textFont);
		updateTextColor(textColor);
		updateColor(color);
		updateStyle(style);
		updateArrow(arrow);
		updateWidth(width);
		updateHighlightColor(highlightColor);
	}

// -----------------------------------------------------------------------------
// Section: Update methods
// -----------------------------------------------------------------------------
	public void updateText(String str)
	{
		this.label.setText(str);

		if (str.equals(""))
		{
			label.setVisible(false);
			this.label.setValid(false);
		}
		else
		{
			label.setVisible(true);
			this.label.setValid(true);
		}                          		
	}

	public void updateTextFont(Font f)
	{
		this.label.setFont(f);
	}

	public void updateTextColor(Color c)
	{
		this.label.setForegroundColor(c);
	}

	public void updateColor(Color color)
	{
		setForegroundColor(color);
	}

	public void updateStyle(String s)
	{
		style = s;

		if (style.equals("Dashed"))
		{
			this.dash = new int[] { 5, 5 };
		}
		else
		{
			this.dash = null;
		}

		repaint();
	}

	public void updateArrow(String s)
	{
		this.arrow = s;

		if (this.arrow.equals("None"))
		{
			setTargetDecoration(null);
			setSourceDecoration(null);
		}
		else
		{
			if (this.arrow.equals("Target"))
			{
				setTargetDecoration(new PolygonDecoration());
				setSourceDecoration(null);
			}
			else if (this.arrow.equals("Source"))
			{
				setTargetDecoration(null);
				setSourceDecoration(new PolygonDecoration());
			}
			else if (this.arrow.equals("Both"))
			{
				setTargetDecoration(new PolygonDecoration());
				setSourceDecoration(new PolygonDecoration());
			}
		}
	}

	public void updateWidth(int w)
	{
		width = w;
		repaint();
	}

	public void updateHighlight(Layer highlight, boolean isHighlight)
	{
		this.highlight = isHighlight;

		if (this.highlight)
		{
			((HighlightLayer) highlight).addHighlightToEdge(this);
		}
		else
		{
			((HighlightLayer) highlight).removeHighlight(this);
		}

		repaint();
	}

	public void updateHighlightColor(Color color)
	{
		this.highlightColor = color;
		repaint();
	}

	/**
	 * Draws the edge with dashed style or solid style
	 */
	public void paintFigure(Graphics graphics)
	{
		fillShape(graphics);
		graphics.setLineWidth(width);
		graphics.setLineDash(dash);
		outlineShape(graphics);
	}
}