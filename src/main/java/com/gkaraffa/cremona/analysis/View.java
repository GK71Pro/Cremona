package com.gkaraffa.cremona.analysis;

public abstract class View {
  private String viewString;
  private byte[] byteArray;

  public View(String viewString, byte[] byteArray) {
    this.viewString = viewString;
    this.byteArray = byteArray;
  }
  
  public byte[] getByteArray() {
    return this.byteArray;
  }
  
  public String toString() {
    return this.viewString;
  }
}