package org.gvt.inspector;

import java.util.Iterator;

import org.eclipse.swt.widgets.*;
import org.eclipse.swt.graphics.Font;
import org.gvt.model.CompoundModel;
import org.gvt.model.GraphObject;
import org.gvt.model.NodeModel;
import org.gvt.ChisioMain;
import org.ivis.layout.Cluster;

/**
 * This class maintains the compound inspector window.
 *
 * @author Cihan Kucukkececi
 *
 * Copyright: i-Vis Research Group, Bilkent University, 2007 - present
 */
public class CompoundInspector extends Inspector
{
	/**
	 * Constructor to open the inspector window
	 */
	private CompoundInspector(GraphObject model, String title, ChisioMain main)
	{
		super(model, title, main);

		TableItem item = addRow(table, "Name");
		item.setText(1, model.getText());

		item = addRow(table, "Text Font");
		Font font = model.getTextFont();
		String fontName = font.getFontData()[0].getName();
		int fontSize = font.getFontData()[0].getHeight();
		int fontStyle = font.getFontData()[0].getStyle();

		if (fontSize > 14)
		{
			fontSize = 14;
		}

		item.setText(1, fontName);
		item.setFont(1,	new Font(null, fontName, fontSize, fontStyle));
		item.setForeground(1, model.getTextColor());

		item = addRow(table, "Color");
		item.setBackground(1, model.getColor());

		item = addRow(table, "Border Color");
		item.setBackground(1, ((CompoundModel) model).getBorderColor());

		item = addRow(table, "Cluster ID");
		// set clusters with commas
		String clusterText = "";
		Iterator<Cluster> itr = ((CompoundModel) model).getClusters().iterator();
		// if there is no cluster
		if ( !itr.hasNext() )
		{
			item.setText(1, "0");
		}
		else 
		{
			clusterText += itr.next().getClusterID();
			while ( itr.hasNext() )
			{
				clusterText += ", " + itr.next().getClusterID();
			}
			item.setText(1, clusterText);
		}
		
		createContents(shell);

		shell.setLocation(calculateInspectorLocation(main.clickLocation.x,
			main.clickLocation.y));
		shell.open();
	}

public static void getInstance(GraphObject model,
	String title,
	ChisioMain main)
	{
		if (isSingle(model))
		{
			instances.add(new CompoundInspector(model, title, main));
		}
	}

	/**
	 * Default parameters for creation is changed with the current parameters.
	 */
	public void setAsDefault()
	{
		TableItem[] items = table.getItems();

		for (TableItem item : items)
		{
			if (item.getText().equals("Name"))
			{
				CompoundModel.DEFAULT_TEXT = item.getText(1);
			}
			else if (item.getText().equals("Text Font"))
			{
				CompoundModel.DEFAULT_TEXT_FONT = newFont;
				CompoundModel.DEFAULT_TEXT_COLOR = item.getForeground(1);
			}
			else if (item.getText().equals("Color"))
			{
				CompoundModel.DEFAULT_COLOR = item.getBackground(1);
			}
			else if (item.getText().equals("Border Color"))
			{
				CompoundModel.DEFAULT_BORDER_COLOR = item.getBackground(1);
			}
		}
	}
}