/******************************************************************************
 *
 * JQTCmd.java
 *
 * This is main app class that creates a tabbed interface with each tab
 * containing a group of camera functions, including:
 *
 * COMs - Connect to camera and display metadata from camera
 * Images - Save images from the camera as Apple QTK files
 * Control - 'Remote control' the camera and update various settings
 *
 ******************************************************************************/

package com.crazylegs.JQTCmd;

import com.crazylegs.JQTCmd.*;
import com.crazylegs.JQuickTake.*;
import java.io.*;
import java.util.*;
import java.net.*;
import java.nio.file.*;
/**
 * @author Kevin Godin
 * @version $Revision: 1.0 $
 **/
public class JQTCmd 
{
  
	HashMap<String, Object>      ivMap;

  
	Camera		ivCamera;
  
	ImageRoll		ivImageRoll;
  
	String		ivFileSeparator;
  
    DebugLog ivDebugLog;

	String[]	ivCOMPorts;
	
	boolean	ivPort, ivMeta, ivSilent, ivSave, ivSavePath, ivSavePrefix, ivDebug, ivDebugV, ivHelp, ivDelete;
	String	ivPortNum, ivSaveNum, ivSaveFilePath, ivSaveFilePrefix, ivDebugPath;


//
// Process passed parms, etc.
//

  public JQTCmd(String[] args)
  {
	  
	int i,j;
	
	boolean	tvProceed;

	ivPort = false;
	ivMeta = false;
	ivSilent = false;
	ivSave = false;
	ivDebug = false;
	ivDebugV = false;
	ivSave = false;
	ivSavePath = false;
	ivSavePrefix = false;
	ivHelp = false;
	ivDelete = false;
	
	tvProceed = true;


//  Init environment

    ivMap = new HashMap<String, Object>(25);
    
    Environment.init(ivMap);
    
    Environment.setValue("Parent",this);

	ivFileSeparator = System.getProperty("file.separator");



//  Initialize Debugging

    ivDebugLog = new DebugLog();
	Environment.setValue("DebugLog",ivDebugLog);
//	ivDebugLog.initLoggingDirectory();
	ivDebugLog.setMode(false);
	ivDebugLog.setVerbose(false);

	
// Create a Camera instance

    ivCamera = new Camera();
	
	Environment.setValue("Camera", ivCamera);

// Create an Image Roll instance (will manage images taken off camera)

    ivImageRoll = new ImageRoll();
	
	Environment.setValue("ImageRoll", ivImageRoll);
	
	ivSaveFilePath = "..";
	
	ivSaveFilePrefix = " ";


//    Check to see if messages are suppressed

	for(i = 0; i < args.length; i++)
	{
		if(args[i].equals("-silent"))
		{
			ivSilent = true;
		}
	}
	
	this.writeMessage("\nJQTCmd v1.0 April 2026");
	this.writeMessage("======================\n");

	tvProceed = this.checkParms(args);
	
	if(!tvProceed)
		return;
	

	if(ivHelp && !ivSilent)
	{
		this.showHelp();
	}
	
	tvProceed = this.portCheck();
	
	if(!tvProceed)
		return;



	if(ivMeta && !ivSilent)
	{
		this.showMeta();
	}


	if(ivSave)
	{
		tvProceed = this.savePic();
	}
	
	if(!tvProceed)
		return;
	
	if(ivDelete)
	{
		this.deletePics();
	}


	return;
  }

// Loop thru passed parms and determine what needs to be done

	public boolean checkParms(String[] args)
	{
		boolean tvProceed;
		
		int i,j;
		
		tvProceed = true;
	
		for(i = 0; i < args.length; i++)
		{
			switch(args[i])
			{
				case "-help":
					ivHelp = true;
					break;
				case "-port":
					if(i+1 < args.length && !args[i+1].startsWith("-"))
					{
						ivPortNum = args[i+1];
						ivPort = true;					
					} else {
						this.writeMessage("No Port value found");
						tvProceed = false;
					}
					break;
				case "-meta":
					ivMeta = true;
					break;
				case "-save":
					if(i+1 < args.length && !args[i+1].startsWith("-"))
					{
						ivSaveNum = args[i+1];
						ivSave = true;
					} else {
						this.writeMessage("No picture specified for Save");
						tvProceed = false;
					}
					break;
				case "-savepath":
					if(i+1 < args.length && !args[i+1].startsWith("-"))
					{
						ivSaveFilePath = args[i+1];
						ivSavePath = true;
					} else {
						this.writeMessage("No Save path specified");
						tvProceed = false;
					}
					break;
				case "-saveprefix":
					if(i+1 < args.length && !args[i+1].startsWith("-"))
					{
						ivSaveFilePrefix = args[i+1];
						ivSavePrefix = true;
					} else {
						this.writeMessage("No Save file prefix specified");
						tvProceed = false;
					}
					break;
				case "-delete":
					ivDelete = true;
					break;
				default:
					break;
			}
		}
			
		return tvProceed;		
	}

// Process the -help parm

  public void showHelp()
  {
		this.writeMessage("HELP for JQTCmd:\n");
		this.writeMessage("-port\t\t: Specify the serial port for the camera connection OR specify '?' to display a list of ports (REQUIRED)");
		this.writeMessage("-help\t\t: List of parameters supported by JQTCmd (OPTIONAL)");
		this.writeMessage("-meta\t\t: Display camera data such as name, pictures taken, etc. (OPTIONAL)");
		this.writeMessage("-silent\t\t: Suppress any messages that are normally displayed (OPTIONAL)");
		this.writeMessage("-save\t\t: Specify which picture# to save to a file OR specify 'all' to save all pictures from camera. Filename used will be IMAGEnn.qtk (OPTIONAL)");
		this.writeMessage("-savepath\t: The filepath to which pictures will be saved. Default is the local directory (OPTIONAL)");
		this.writeMessage("-saveprefix\t: Specify text that will be used as a prefix to each picture filename. Default is no prefix. (OPTIONAL)");
		this.writeMessage("-delete\t\t: Delete ALL pictures on camera. This will be processed AFTER all other actions (e.g. after pictures are saved). (OPTIONAL)");
		this.writeMessage(" ");
		this.writeMessage("WARNING!! Any filenames that already exist will be overwritten when pictures are being saved!");
		this.writeMessage(" ");
		this.writeMessage("Example 1: JQTCmd -port COM3 -save 3 -prefix 'Vacation'\n");
		this.writeMessage("This will access the camera on COM3 and save picture #3 from the camera to the local directory as filename Vacation-IMAGE03.qtk");
		this.writeMessage(" ");
		this.writeMessage("Example 2: JQTCmd -port COM7 -save all -savepath 'd:\\My Pictures\\'\n");
		this.writeMessage("This will access the camera on COM7 and save all pictures from the camera to directory d:\\My Pictures as filename IMAGE01.qtk, IMAGE02.qtk, etc.\n\n");
	  
	  return;
  }

// Process the -port parm

  public boolean portCheck()
  {
	  int i,j;
	  
	if(!ivPort)
	{
		this.writeMessage("Required Port parameter is missing!");
		return false;
	}

	if(ivPortNum.equals("?"))
	{
		ivCOMPorts = ivCamera.getCOMPorts();	// Get a list of available COM (Serial) ports for selection
		this.writeMessage("Available Ports:");
		if(ivCOMPorts == null)
		{
			this.writeMessage("No ports detected");
		} else {
			for(j = 0; j < ivCOMPorts.length; j++)
			{
				this.writeMessage(ivCOMPorts[j]);
			}
		}
		return false;
	} else {
		Environment.setValue("COMPort",ivPortNum);
		Environment.setValue("PortSpeed","57600");
		ivCamera.openCamera();	
	}
	
	if(ivCamera.getOpenStatus())
	{
		this.writeMessage("Camera open on Port " + ivPortNum + "\n");
	} else {
		this.writeMessage("Camera not found. Check cables, power, and specified port");
		return false;
	}
	  
	  return true;
  }

// Process -meta parm

  public void showMeta()
  {
		this.writeMessage("CAMERA METADATA:\n");
		this.writeMessage("Camera name: \t\t" + ivCamera.getName());
		this.writeMessage("Model: \t\t\t" + ivCamera.getCameraModel());
		this.writeMessage("Pictures Taken: \t" + ivCamera.getTaken());
		this.writeMessage("Pictures Remaining: \t" + ivCamera.getRemaining());
		this.writeMessage("Battery Level: \t\t" + ivCamera.getBatteryLevel());
		this.writeMessage("Picture Quality: \t" + ivCamera.getQuality());
		this.writeMessage("Flash Mode: \t\t" + ivCamera.getFlash());
		this.writeMessage("Camera Date: \t\t" + ivCamera.getDisplayDate());
		this.writeMessage("Camera Time: \t\t" + ivCamera.getDisplayTime() + "\n\n");
	  
	  return;
  }

// Process -save parm

  public boolean savePic()
  {
	int tvPicNum;
	
	String	tvPath, tvPrefix;
	
	boolean	tvPicAll, tvProceed;	  
	  
		tvPicAll = false;
		tvProceed = true;
		tvPicNum = 99;
		
		tvPath = " ";
		tvPrefix = " ";

				
	   if(ivSaveNum.toLowerCase().equals("all"))
	   {
		   tvPicAll = true;   
	   } else 
	   {
			try
			{
				tvPicNum = Integer.parseInt(ivSaveNum);
			}
			catch (NumberFormatException e) 
			{
				this.writeMessage("The specified Save parameter is not valid.");
				return false;
			}	
	   }		
		
		try
		{
			if((tvPicNum > Integer.parseInt(ivCamera.getTaken()) || tvPicNum < 1) && !tvPicAll)
			{
				this.writeMessage("The requested picture to Save does not exist.");
				return false;
			}
		}
		catch (NumberFormatException e) 
		{
			this.writeMessage("Critical error with camera's value for Pictures Taken");
			return false;		
		}
		
//		check file

		tvPath = ivSaveFilePath;
		
//		check prefix

		tvPrefix = ivSaveFilePrefix;
		
//		do save

		if(tvPicAll)
		{
			tvProceed = ivImageRoll.saveAllImages(tvPath, tvPrefix, ivSilent);
		} else 
		{
			tvProceed = ivImageRoll.saveImage(tvPicNum, tvPath, tvPrefix, ivSilent);
		}
	  
	  return tvProceed;
  }
  
// Process -delete parm

  public void deletePics()
  {	  
		this.writeMessage("\nPicture deletion starting.");
		
		ivImageRoll.deleteImages();
		
		this.writeMessage("\nAll pictures have been deleted!");
		
	  return;
  }

// Write to Terminal

  public void writeMessage(String message)
  {
	  if(!ivSilent)
	  {
		  System.out.println(message);
	  }
	  
	 return;
  }
  
//
// Run from the command line. 

//
  public static void main(String[] args)
  {

	if(args.length < 0)
	{
		System.out.println("No parameters were found");
		return;
	} else {
		JQTCmd client = new JQTCmd(args);
	}
		
	
  }

}
