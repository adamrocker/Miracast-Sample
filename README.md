# Miracast Sample

Test application of Android using Presentation class which displays something on the remote display like a TV over Miracast technology.

## Demo
![Demo image](https://lh3.googleusercontent.com/-IWkNvP0iCEw/UNwPCDMGbcI/AAAAAAAAQQM/101yztAhSLg/s1024/IMG_20121227_180023.jpg)

Install this apk to the Nexus4 and display a simple text "Hello world,…" to the remote display which is connected the Miracast receiver(PTV3000 /firmware 2.2.2).

## Digest

#### How to get displays 

DisplayManger handles all displays including the local display.

	mDisplayManager = (DisplayManager)getSystemService(Context.DISPLAY_SERVICE);

You can get all displays via DisplayManager.

	Display[] displays = mDisplayManager.getDisplays();


#### Show the remote display

Super easy to show something on the remote display. Just call show() method which is implemented in Presentation class provided by Android SDKr17.

	private void showPresentation(Display display) {
        RemotePresentation presentation = new RemotePresentation(this, display);
        presentation.show();
    }

RemotePresentation class is extended Presentation.
Presentation class looks like Activity. You can set up the display overriding the onCreate method.

    private final class RemotePresentation extends Presentation {
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.remote_display);
        }
    }

This demo apk shows only TextView on the remote display. The layout resource is remote_display.xml.

	<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
        xmlns:tools="http://schemas.android.com/tools"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        tools:context=".MainActivity" >

        <TextView
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_centerHorizontal="true"
            android:layout_centerVertical="true"
            android:textSize="22sp"
            android:textStyle="bold"
            android:text="@string/hello_world" />

    </RelativeLayout>


## How to setup the miracast environment

#### Test Enviroinment

Nexus4: Currently only Nexus4 supports the Miracast(27 Dec. 2012).
PTV3000: I've tested this apk with [PTV3000](http://www.netgear.com/home/products/hometheater/media-players/PTV3000.aspx).

#### Nexus4

Turn on the "Wireless Display" on the Settings.

![Wireless Display](https://lh5.googleusercontent.com/-jsRDm7OwPcA/UNwZf_w53II/AAAAAAAAQSU/gwoLDrnoLi8/s800/device-2012-12-27-184713.png)

You can find wireless displays and tap an item to connect.

![Connecting the wireless display](https://lh6.googleusercontent.com/-dL8nnpfbEts/UNwZf37W6rI/AAAAAAAAQSY/NG9y4QrC36A/s800/device-2012-12-27-184741.png)

#### PTV3000

Check the firmware version. I bought this gadget from Amazon.com and the version is 1.0.13. TOO LOW!! This version does not support Miracast. You need to update the firmware in 2 steps.

***STEP1:*** Download the latest firmware which is on Internet. I could find the version 2.2.2. This might not be official, I guess.

***STEP2:*** Update the firmware. See the [Installation Guide (PDF)](http://www.downloads.netgear.com/files/GDC/PTV3000/PTV3000_IG_10AUG2012.pdf)


