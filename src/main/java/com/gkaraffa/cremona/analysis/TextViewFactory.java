package com.gkaraffa.cremona.analysis;

import java.util.List;

public class TextViewFactory extends ViewFactory{
  @Override
  public View renderView(TabularAnalytic tabularAnalytic) {
    StringBuilder sB = new StringBuilder();
    int[] columnMaxs = determineColumnMaxs(tabularAnalytic);
    int fieldSpace = determineFieldSpace(columnMaxs);
    
    sB.append(this.renderHeader(columnMaxs, fieldSpace, tabularAnalytic));
    sB.append(this.renderBody(columnMaxs, fieldSpace, tabularAnalytic));
    sB.append(this.renderFoot(fieldSpace));
    sB.append("\n");
    
    String viewString = sB.toString();
    View outputView = new View(viewString, viewString.getBytes());
    
    return outputView;
  }
  
  private int[] determineColumnMaxs(TabularAnalytic tabularAnalytic) {
    int numCols = tabularAnalytic.getColumnCount();
    int[] columnMaxs = new int[numCols];

    for (int index = 0; index < numCols; index++) {
      int maxLen = 0;

      // check all rows
      List<AnalyticCell> analyticCells = tabularAnalytic.getAnalyticColumn(index);
      for (AnalyticCell analyticCell : analyticCells) {
        int currentLength = analyticCell.getAnalyticText().length();
        if (currentLength > maxLen) {
          maxLen = currentLength;
        }
      }

      // check header
      int headerLength = tabularAnalytic.getHeaderRow().get(index).getHeaderText().length();
      if (headerLength > maxLen) {
        maxLen = headerLength;
      }

      columnMaxs[index] = maxLen;
    }

    return columnMaxs;
  }
  
  private int determineFieldSpace(int[] columnMaxs) {
    int fieldCount = columnMaxs.length;
    int fieldSpace = fieldCount + 1;

    for (int columnMax : columnMaxs) {
      fieldSpace = fieldSpace + columnMax + 2;
    }
    
    return fieldSpace;
  }
  
  private String renderHeader(int[] columnMaxs, int fieldSpace, TabularAnalytic tabularAnalytic) {
    StringBuilder sB = new StringBuilder();
    
    sB.append(renderBar(fieldSpace) + "\n");
    sB.append(renderTitles(columnMaxs, tabularAnalytic) + "\n");
    sB.append(renderBar(fieldSpace) + "\n");

    return sB.toString();
  }
  
  private String renderBody(int[] columnMaxs, int fieldSpace, TabularAnalytic tabularAnalytic) {
    StringBuilder sB = new StringBuilder();
    int numRows = tabularAnalytic.getRowCount();
    
    for(int index = 0; index < numRows; index++) {
      List<AnalyticCell> analyticCells = tabularAnalytic.getAnalyticRow(index);
      int columnCount = 0;
      
      for(AnalyticCell analyticCell: analyticCells) {
        sB.append("| ");
        sB.append(String.format(createFormatString(columnMaxs[columnCount]), analyticCell));
        sB.append(" ");
        columnCount++;
      }
      sB.append("|\n");
    }
    
    return sB.toString();
  }
  
  private String renderFoot(int fieldSpace) {
    return (renderBar(fieldSpace) + "\n");
  }
  
  private String renderBar(int fieldSpace) {
    StringBuilder sB = new StringBuilder();
    
    for (int index = 0; index < fieldSpace; index++) {
      sB.append('-');
    }

    return sB.toString();
  }

  private String renderTitles(int[] columnMaxs, TabularAnalytic tabularAnalytic) {
    List<HeaderCell> headerCells = tabularAnalytic.getHeaderRow();
    int numCols = tabularAnalytic.getColumnCount();
    StringBuilder sB = new StringBuilder();

    for (int index = 0; index < numCols; index++) {
      sB.append("| ");
      sB.append(String.format(createFormatString(columnMaxs[index]), headerCells.get(index)));
      sB.append(" ");
    }

    sB.append("|");

    return sB.toString();
  }
  
  private String createFormatString(int columnMax) {
    StringBuilder sB = new StringBuilder();

    sB.append("%-");
    sB.append(columnMax);
    sB.append(".");
    sB.append(columnMax);
    sB.append("s");

    return sB.toString();
  }
}
