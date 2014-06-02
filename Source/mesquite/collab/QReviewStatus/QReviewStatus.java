/* Mesquite source code.  Copyright 1997-2009 W. Maddison and D. Maddison. Version 2.7, August 2009.Disclaimer:  The Mesquite source code is lengthy and we are few.  There are no doubt inefficiencies and goofs in this code. The commenting leaves much to be desired. Please approach this source code with the spirit of helping out.Perhaps with your help we can be more than a few, and make Mesquite better.Mesquite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY.Mesquite's web site is http://mesquiteproject.orgThis source code and its compiled class files are free and modifiable under the terms of GNU Lesser General Public License.  (http://www.gnu.org/copyleft/lesser.html)*/package mesquite.collab.QReviewStatus; import java.awt.*;import mesquite.lib.*;import mesquite.lib.characters.*;import mesquite.lib.duties.*;import mesquite.lib.table.*;/*   to do:	- deal with Option cursor, Shift cursor	- pass taxaAreRows into this*//** ======================================================================== */public class QReviewStatus extends DataWindowAssistantI implements CellColorer, CellColorerCharacters, CellColorerMatrix {	TableTool reviewstatusTool;	CharacterData data;	MesquiteTable table;	 public String getFunctionIconPath(){   		 return getPath() + "review.gif";   	 }		//####################	//to add new categories, leave the existing constants as they are, add new constants, then enter the constants and their menu equivalents in sequence in the int and String arrays	public static final int UNASSIGNED = 0;	public static final int TOBEDELETED = 1;	public static final int EXPERIMENTAL = 2;	public static final int NEEDSREVIEW = 3;	public static final int APPROVED = 4;	public static int[] choices = new int[]{UNASSIGNED, TOBEDELETED, EXPERIMENTAL, NEEDSREVIEW, APPROVED};	public static String[] menuItems = new String[]{"Unassigned (White)", "Will Be Deleted (Red)", "Experimental (Orange)", "Needs Review (Yellow)","Approved (Green)"};	//####################	/*.................................................................................................................*/	public boolean startJob(String arguments, Object condition, boolean hiredByName){		if (containerOfModule() instanceof MesquiteWindow) {			reviewstatusTool = new TableTool(this, "matrixReviewstatus", getPath(), "review.gif", 1, 8, "Review Status", "This tool controls assignment of review status to cell of a character matrix.", MesquiteModule.makeCommand("reviewstatus", this), null, null); 			((MesquiteWindow)containerOfModule()).addTool(reviewstatusTool);		}		else return false;		return true;	}   	 public boolean setActiveColors(boolean active){ 		return true;   	 }   	 public void viewChanged(){   	 }	/*.................................................................................................................*/   	 public boolean isSubstantive(){   	 	return true;   	 }   	 public boolean isPrerelease(){   	 	return true;   	 }	public void setTableAndData(MesquiteTable table, CharacterData data){		this.table = table;		this.data = data;	}	/*.................................................................................................................*/   	public boolean hasDisplayModifications(){   		return false;   	}   	   	private int getReviewStatus(int ic){   		if (data == null)   			return UNASSIGNED;   		if (ic<0)   			return UNASSIGNED;   		Object obj = data.getAssociatedObject(reviewstatusNameRef, ic);   		if (obj instanceof MesquiteInteger)   			return ((MesquiteInteger)obj).getValue();   		return UNASSIGNED;   	}   	public String getCellString(int ic, int it){   		return "Review Status: " + menuItems[getReviewStatus(ic)];   	}   	ColorRecord[] legend;   	public ColorRecord[] getLegendColors(){   		if (legend == null) {   			legend = new ColorRecord[5];   			legend[0] = new ColorRecord(Color.white, "Unassigned");   			legend[1] = new ColorRecord(Color.red, "To Be Deleted");   			legend[2] = new ColorRecord(Color.orange, "Experimental");   			legend[3] = new ColorRecord(Color.yellow, "Needs Review");   			legend[4] = new ColorRecord(ColorDistribution.lightGreen, "Approved");   		}   		return legend;   	}   	public String getColorsExplanation(){   		return null;   	}   	public Color getCellColor(int ic, int it){   		int reviewstatus = getReviewStatus(ic);   		if (reviewstatus == TOBEDELETED)   			return Color.red;   		else if (reviewstatus == EXPERIMENTAL)   			return Color.orange;   		else if (reviewstatus == NEEDSREVIEW)   			return Color.yellow;   		else if (reviewstatus == APPROVED)   			return ColorDistribution.lightGreen;   		return Color.white;   	}	public void colorsLegendGoAway(){  	}	/*.................................................................................................................*/	MesquiteInteger pos = new MesquiteInteger();	NameReference reviewstatusNameRef = NameReference.getNameReference("reviewstatus");	/*.................................................................................................................*/    	 public Object doCommand(String commandName, String arguments, CommandChecker checker) {    	 	if (checker.compare(this.getClass(),  "Shows popup to choose review status", "[column touched][row touched]", commandName, "reviewstatus")) {	   	 		if (data == null)	   	 			return null;	   	 		MesquiteInteger io = new MesquiteInteger(0);	   			int column= MesquiteInteger.fromString(arguments, io);	   			int row= MesquiteInteger.fromString(arguments, io);				if (MesquiteInteger.isNonNegative(column)&& (MesquiteInteger.isNonNegative(row))) {						int reviewstatus = getReviewStatus(column);			     			MesquitePopup popup = new MesquitePopup(table.getMatrixPanel());						for (int i=0; i<menuItems.length; i++)							popup.addItem(menuItems[i], this, MesquiteModule.makeCommand("setReviewstatus", this).setDefaultArguments(" " + column + " " + row + " " + choices[i]));						int x = table.getLeftOfColumn(column+1);						int y = table.getTopOfRow(row+1);						popup.showPopup(x,y);									   	 		return null;		   	 				   	 	}   	 	}    	 	else if (checker.compare(this.getClass(),  "Sets review status", "[column touched][row touched][reviewstatus value]", commandName, "setReviewstatus")) {	   	 		if (data == null)	   	 			return null;	   	 		MesquiteInteger io = new MesquiteInteger(0);	   			int column= MesquiteInteger.fromString(arguments, io);	   			int row= MesquiteInteger.fromString(arguments, io);	   			int reviewstatus= MesquiteInteger.fromString(arguments, io);				if (MesquiteInteger.isCombinable(reviewstatus) && MesquiteInteger.isNonNegative(column)&& (MesquiteInteger.isNonNegative(row))) {						MesquiteInteger ms = new MesquiteInteger(reviewstatus);						data.setAssociatedObject(reviewstatusNameRef, column, ms);						if (table != null)							table.repaintAll();						if (!MesquiteThread.isScripting()){							MesquiteModule mb = findEmployerWithDuty(DataWindowMaker.class);							if (mb != null && mb instanceof DataWindowMaker)								((DataWindowMaker)mb).requestCellColorer(this,0,0, "Do you want the cells to be colored to show Review Status?");						}			   	 		return null;		   	 				   	 	}   	 	}    	 	else    	 		return  super.doCommand(commandName, arguments, checker);		return null;   	 }	/*.................................................................................................................*/    	 public String getName() {		return "Review Status";   	 }    	 public String getNameForMenuItem() {		return "*Review Status";   	 }	/*.................................................................................................................*/  	 public String getVersion() {		return null;   	 }   	 	/*.................................................................................................................*/  	 public String getExplanation() {		return "Provides a tool with which to set the review status for cells of a character matrix.";   	 }}