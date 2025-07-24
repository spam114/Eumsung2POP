package com.symbol.eumsung2pop.model.object;

import java.io.Serializable;

public class Packing implements Serializable {
    public String StockCommitNo = "";
    public int StockSeqNo;
    public String PartCode = "";
    public String PartName = "";
    public String PartSpec = "";
    public double StockCommitted;
    public double PackingQty;
    public String ItemTag = "";
}
