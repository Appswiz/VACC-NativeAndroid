package au.com.vacc.timesheets.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.widget.ImageView;

class InfoButton {
    int defaultColour = Color.argb (255, 4, 41, 138);
int highlightColour = Color.argb (255, 255, 90, 0);

        TimeSheetFragment timeSheet;
private String infoToDisplay;
        ImageView image;

    public InfoButton(String s, ImageView imageView, TimeSheetFragment timeSheetFragment) {
        timeSheet = timeSheetFragment;
        infoToDisplay = s;
        this.image = imageView;

        image.setColorFilter (defaultColour);
    }

    public void ShowInfoDialog()
    {
        //Highlight the info icon to show its showing something.
        image.setColorFilter (highlightColour);

        AlertDialog.Builder dialog = new AlertDialog.Builder(timeSheet.getActivity());
        dialog.setTitle("Timesheet Info");
        dialog.setMessage(infoToDisplay);
        dialog.setPositiveButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                image.setColorFilter(defaultColour);
            }
        });

        dialog.show();
    }


    public void ClickEventHandler()
    {
        ShowInfoDialog();
    }
}
