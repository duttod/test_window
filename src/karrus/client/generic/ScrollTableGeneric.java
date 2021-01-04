package karrus.client.generic;

import karrus.client.FrontalWebApp;
import karrus.client.appearance.LayoutInfo;

import com.google.gwt.gen2.table.client.FixedWidthFlexTable;
import com.google.gwt.gen2.table.client.FixedWidthGrid;
import com.google.gwt.gen2.table.client.ScrollTable;
import com.google.gwt.gen2.table.override.client.FlexTable.FlexCellFormatter;
import com.google.gwt.gen2.table.override.client.HTMLTable.CellFormatter;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;

/**
 * Creates a scroll table with a predefined size.
 * 
 * @author Estelle DUMAS: estelle.dumas@karrus-its.com
 */
public class ScrollTableGeneric extends ScrollTable {
	
	/**
	 * Constructor.
	 * @param dataTable
	 * @param headerTable
	 */
	public ScrollTableGeneric(FixedWidthGrid dataTable, FixedWidthFlexTable headerTable) {
		super(dataTable, headerTable);
		this.setResizePolicy(ScrollTable.ResizePolicy.FILL_WIDTH);
		this.setSize(FrontalWebApp.getWidthForMainPanel()+"px", FrontalWebApp.getHeightForTable()+"px");
		for (int i=0; i<dataTable.getColumnCount(); i++) {
			this.setColumnSortable(i, false);
		}	
		this.setCellPadding(0);
		this.setCellSpacing(0);
		FlexCellFormatter formatter = headerTable.getFlexCellFormatter();
		for (int i=0; i<headerTable.getColumnCount(); i++) {
			formatter.setHeight(0, i, LayoutInfo.tableHeaderHeight);
		}	
	}

	/**
	 * Adds empty lines to complete the table.
	 * @param indexToStartFillingWithEmptyLines
	 * @param maxIndex
	 */
	public void addEmptyLines(int indexToStartFillingWithEmptyLines, int maxIndex){
		FixedWidthGrid dataTable = this.getDataTable();
		CellFormatter formatter = dataTable.getCellFormatter();
		while (indexToStartFillingWithEmptyLines<maxIndex){
			int colNb = dataTable.getColumnCount();
			for (int i=0; i<colNb; i++){
				dataTable.setWidget(indexToStartFillingWithEmptyLines, i, null);
				formatter.setAlignment(indexToStartFillingWithEmptyLines, i, HasHorizontalAlignment.ALIGN_CENTER, HasVerticalAlignment.ALIGN_MIDDLE);
			}
			indexToStartFillingWithEmptyLines++;
		}
	}
}