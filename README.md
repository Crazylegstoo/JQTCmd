# JQTCmd - Apple QuickTake Manager v1.0

Every story has a beginning, and in this case that beginning starts with JQuickTake, which you can [find over here](https://github.com/Crazylegstoo/JQuickTake).

With that out of the way, let's talk about JQTCmd, which is essentially a stripped-down command-line version of JQuickTake. 

Note that JQTCmd 1.0 is based on capabilities within JQuickTake v1.4.


# The Solution
JQTCmd is a Java application that can be run from a console/terminal or embedded in a custom script. It's main purpose is to connect to an Apple QuickTake 100/150 camera and save pictures from the camera to a local PC. Those resulting pictures files are saved in Apple .QTK format.

## What The JQuickTake Software Does

The software will currently run on Windows and ARM-based Linux (i.e. Raspberry Pi) computers and offers the following capabilities.

1 - It will connect to an Apple QuickTake 100 or 150 camera and display its metadata, including: camera name, pictures taken, pictures remaining, Flash mode, Quality mode, etc.

2 - It will allow you to save Selected or All pictures to local storage as an Apple QTK picture file, using a customizable file-naming format.


## What The JQuickTake Software Does Not Do

JQTCmd v1.0 does lack a few features:

1. **This software does not work for the QuickTake 200 camera.** It has been developed and tested for QuickTake 100 and 150 camera models. However, it definitely will not work with a QuickTake 200 camera.
2. It does not convert Apple .QTK images to any other format (such as .JPG). 


# Installing and Running JQTCmd

The fully-built solution has been tested successfully on Windows and Raspberry Pi platforms - specifically Windows 10, Windows 11, and Raspberry Pi OS Bookworm . MacOS and AMD/Intel Linux is not specifically supported at this time, the application can definitely be packaged for those platforms if there is a demand for it.

The install packages all include a bundled Java 17 runtime to provide an 'all-in-one' solution.

**Windows**

 1. Create a folder on your PC
 2. Download *JQTCmd-1.0-WIN.zip*
 3. Unzip the contents into your folder
 4. Execute **YOURFOLDER\JQTCmd *-parms***

**Raspberry Pi OS**

 1. Create a folder on your PC
 2. Download *JQTCmd-1.0-ARM.tar.gz*
 3. Unzip the contents into your folder
 4. Execute **YOURFOLDER/bin/JQTCmd *-parms***

Parameters You Need!

JQTCmd execution is controlled through various command line parameters you specify. The full list of parms is:

**-port**           : Specify the serial port for the camera connection OR specify '?' to display a list of ports (REQUIRED)
**-help**           : List of parameters supported by JQTCmd (OPTIONAL)
**-meta**           : Display camera data such as name, pictures taken, etc. (OPTIONAL)
**-silent**         : Suppress any messages that are normally displayed (OPTIONAL)
**-save**           : Specify which picture# to save to a file OR specify 'all' to save all pictures from camera. Filename used will be IMAGEnn.qtk (OPTIONAL)
**-savepath**       : The filepath to which pictures will be saved. Default is the local directory (OPTIONAL)
**-saveprefix**     : Specify text that will be used as a prefix to each picture filename. Default is no prefix. (OPTIONAL)
**-delete**         : Delete ALL pictures on camera. This will be processed AFTER all other actions (e.g. after pictures are saved). (OPTIONAL)

**WARNING!!** Any filenames that already exist will be overwritten when pictures are being saved!

*Example 1:* `JQTCmd -port COM3 -save 3 -prefix 'Vacation'`

This will access the camera on COM3 and save picture #3 from the camera to the local directory as filename Vacation-IMAGE03.qtk

**Example 2:** `JQTCmd -port COM7 -save all -savepath 'd:\My Pictures\'`

This will access the camera on COM7 and save all pictures from the camera to directory d:\My Pictures as filename IMAGE01.qtk, IMAGE02.qtk, etc.

**Example 3:** `JQTCmd -port ttyUSB0 -meta`

This will display metadata from the camera - e.g. camera name, pictures taken, pictures remaining, camera model. etc.

**Example 4:** `JQtCmd -port ?`

This will display a list of all serial ports found on your PC


# Other Things You May Need

To understand about serial ports, QuickTake cables, serial adapters, what to with .QTK files, and the like, please refer to the [JQuickTake documentation](https://github.com/Crazylegstoo/JQuickTake).



## There Are Some Major Thank-You's Required!

 - [Colin Leroy-Mira](https://www.colino.net/wordpress/) - Again, thank you for sharing your excellent work on the QuickTake Protocol.  It made my little project much easier.
 - [Dave Coffin](https://www.dechifro.org/dcraw/), for creating *dcraw* and making sure it supports Apple QKTK and QKTN compression.
 - [Fazecast Inc](https://fazecast.github.io/jSerialComm/) for creating and maintaining the excellent jSerialComm library.
 - [Frank Siegert](http://www.wizards.de/rawdrop/) for writing the lean, mean, and dead-simple RawDrop app that made my dev/test efforts that much easier.
 

## Where You Can Find Me

If you have a question, or just want to virtually point and laugh:

Reddit - https://www.reddit.com/user/Crazylegstoo/

Email - jquicktake@gmail.com

Many Thanks, and I truly hope JQuickTake actually works. ;)

**K. Godin** - London, Ontario, Canada (2026)


