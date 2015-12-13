/**
 * Created by Chris on 12/9/2015.
 *
 * This represents a notification dialog
 * (like a "message box") for use in displaying 
 * important information to the user.
 */

package csit.team3.projecthub;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;


public class NotificationDialog {
    
	// Title is title, text is the message sent to the user. 
	// The user must confirm by pressing "OK":
	public static void show(String title, String text, Context context) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(text);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }
}
