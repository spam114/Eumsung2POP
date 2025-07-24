package com.symbol.eumsung2pop.model.object;

public class MainMenuItem {
    public int menuType;//1: ViewOnly, 2:Enalbe Click
    public int group;
    public String menuName="";
    public boolean lastItem=false;
    public int imageID;

    public MainMenuItem(int menuType, int group, String menuName, boolean lastItem, int imageID){
        this.menuType=menuType;
        this.group=group;
        this.menuName=menuName;
        this.lastItem=lastItem;
        this.imageID=imageID;
    }
}
