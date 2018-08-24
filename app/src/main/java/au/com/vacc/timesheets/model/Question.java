package au.com.vacc.timesheets.model;

import android.graphics.Color;
import android.os.Handler;
import android.view.View;
import android.widget.RadioButton;

import au.com.vacc.timesheets.fragment.TimeSheetFragment;

public class Question {
    //The index of the question
    public int ID = 0;

    public View questionView;				//The view of the question
    public RadioButton breadcrumbView;		//The radio button that represents the breadcrumb
    public TimeSheetFragment timesheet;				//Programming in business for 2 days and already have a back flowing reference :|

    //A handle to the next question. Used purely for UI (scrolling the scroll bar automatically)
    public Question nextQuestion = null;

    public boolean greyedOut = false;

    public void OpenQuestion()
    {
        timesheet.CloseAllQuestions ();
        questionView.setVisibility(View.VISIBLE);

        timesheet.currentQuestion = this;

        timesheet.ClearApprovalMode();

        HighlightBreadcrumbText ();

    }

    public void ActivateBreadcrumb(boolean scroll)
    {
        breadcrumbView.setChecked(true);

        if(scroll)
        {
           DelayedScroll();
        }
    }

    public void GreyOut()
    {
        breadcrumbView.setTextColor(Color.argb(255, 200, 200, 220));
        greyedOut = true;
    }

    public void Restore()
    {
        RestoreBreadcrumbText ();
        greyedOut = false;
    }

    public void DelayedScroll()
    {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
               ScrollToBreadcrumb();
            }
        }, 50);
    }

    public void ScrollToBreadcrumb()
    {
        //The next question doesn't exist, which means we're at the end of breadcrumbs. Just scroll to the end then.
        if(nextQuestion == null)
        {
            timesheet.BreadcrumbScrollbar.smoothScrollTo(timesheet.BreadcrumbGroup.getWidth(), 0);
            return;		//Early out from here.
        }

        //Locations come in int arrays, as (x,y). Only using X, but gotta grab both anyway due to API.
        int[] position = new int[2];
        breadcrumbView.getLocationOnScreen (position);
        int currentScrollPosition = timesheet.BreadcrumbScrollbar.getScrollX();

        //Scroll the scrollbar to the location of the active breadcrumb, minus its own width (halved) to center it a bit more.
        //Gotta add the current scroll position on because the positions of the breadcrumbs are moving when the scroll bar scrolls them :|
        //Think relativity.
        //current position  +   radioPos   -       half screen width	 +	  half breadcrumb width   y isn't scrolled
        timesheet.BreadcrumbScrollbar.smoothScrollTo (currentScrollPosition + (position[0] - (timesheet.ScreenWidth / 2) + (breadcrumbView.getWidth() / 2)),   0);

    }

    public void HighlightBreadcrumbText()
    {
        //Probably want a colour that isn't so gross.
        breadcrumbView.setTextColor (Color.argb(255, 10, 36, 140));
    }

    public void RestoreBreadcrumbText()
    {
        if(!greyedOut) breadcrumbView.setTextColor (Color.BLACK);
    }

    public void CloseQuestion()
    {
        RestoreBreadcrumbText ();
        questionView.setVisibility(View.GONE);
    }

    public void HideBreadcrumb()
    {
        //Hacky solution
        breadcrumbView.setVisibility(View.INVISIBLE);
    }

    public void RemoveBreadcrumb()
    {
        breadcrumbView.setVisibility(View.GONE);
    }

    public void ShowBreadcrumb()
    {
        breadcrumbView.setVisibility(View.VISIBLE);
    }

    public void ClickEventHandler()
    {
        timesheet.OpenQuestion (ID, true);
    }

}
