package au.com.vacc.timesheets.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.FileProvider;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.codehaus.jackson.JsonNode;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import au.com.vacc.timesheets.MembersActivity;
import au.com.vacc.timesheets.R;
import au.com.vacc.timesheets.model.ProfileData;
import au.com.vacc.timesheets.model.Question;
import au.com.vacc.timesheets.model.TimeSheetData;
import au.com.vacc.timesheets.utils.CustomJsonCallBack;
import au.com.vacc.timesheets.utils.CustomJsonRequest;
import au.com.vacc.timesheets.utils.DateTimeHelper;
import au.com.vacc.timesheets.utils.SharedPrefManager;
import au.com.vacc.timesheets.utils.TimeSheetCore;

/**
 * Created by Juhachi on 11/10/2017.
 */

public class TimeSheetFragment extends Fragment {
    private Activity act;
    public Question currentQuestion;
    public HorizontalScrollView BreadcrumbScrollbar;

    public RadioGroup BreadcrumbGroup;
    public int ScreenWidth;

    //private handles for easier use
    private TimeSheetCore tsCore;
    private ProfileData profileData;
    private TimeSheetData tsData;

    Question[] questionArray = null;

    //for keeping track if we've launched the mail activity intent
    boolean mailingTimeSheet = (false);

    Calendar mondayDate;

    //Gross but nececsary. Indices to specific questions
		final int startWeekIndex = 0;
		final int hoursTypeIndex = 1;
    final int workHoursIndex = 2;
    final int overtimeIndex = 3;
    final int tafeHoursIndex = 4; //RTO - same thing
    final int sickLeaveIndex = 5;
    final int annualLeaveIndex = 6;
    final int bereavementLeaveIndex = 7;
    final int vaccTimeIndex = 8;
    final int notesIndex = 9;
    final int summaryIndex = 10;

    ArrayList<InfoButton> infoArray = null;

    TextView hoursRemainingText = null;

    private boolean employerApprovalMode = (false);

    List<UserInputField> userInputs = new ArrayList<>();


    CheckBox TAFEHoursType;
    CheckBox workplaceHoursType;
    CheckBox mixedHoursType;

    CheckBox sickLeaveCheckBox;
    CheckBox annualLeaveCheckBox;
    CheckBox bereavementLeaveCheckBox;
    CheckBox vaccTimeCheckBox;

    EditText workDaysText;
    EditText workHoursText;
    EditText workMinutesText;


    EditText mondayVACCTimeMinutes;
    EditText mondayVACCTimeHours;
    EditText tuesdayVACCTimeMinutes;
    EditText tuesdayVACCTimeHours;
    EditText wednesdayVACCTimeMinutes;
    EditText wednesdayVACCTimeHours;
    EditText thursdayVACCTimeMinutes;
    EditText thursdayVACCTimeHours;
    EditText fridayVACCTimeMinutes;
    EditText fridayVACCTimeHours;
    EditText saturdayVACCTimeMinutes;
    EditText saturdayVACCTimeHours;
    EditText sundayVACCTimeMinutes;
    EditText sundayVACCTimeHours;

    // AnnualLeaveUI
    EditText mondayAnnualLeaveMinutes;
    EditText mondayAnnualLeaveHours;
    EditText tuesdayAnnualLeaveMinutes;
    EditText tuesdayAnnualLeaveHours;
    EditText wednesdayAnnualLeaveMinutes;
    EditText wednesdayAnnualLeaveHours;
    EditText thursdayAnnualLeaveMinutes;
    EditText thursdayAnnualLeaveHours;
    EditText fridayAnnualLeaveMinutes;
    EditText fridayAnnualLeaveHours;
    EditText saturdayAnnualLeaveMinutes;
    EditText saturdayAnnualLeaveHours;
    EditText sundayAnnualLeaveMinutes;
    EditText sundayAnnualLeaveHours;

    // SickLeaveUI
    EditText mondaySickLeaveMinutes;
    EditText mondaySickLeaveHours;
    EditText tuesdaySickLeaveMinutes;
    EditText tuesdaySickLeaveHours;
    EditText wednesdaySickLeaveMinutes;
    EditText wednesdaySickLeaveHours;
    EditText thursdaySickLeaveMinutes;
    EditText thursdaySickLeaveHours;
    EditText fridaySickLeaveMinutes;
    EditText fridaySickLeaveHours;
    EditText saturdaySickLeaveMinutes;
    EditText saturdaySickLeaveHours;
    EditText sundaySickLeaveMinutes;
    EditText sundaySickLeaveHours;

    // BereavementLeave
    EditText mondayBereavementLeaveMinutes;
    EditText mondayBereavementLeaveHours;
    EditText tuesdayBereavementLeaveMinutes;
    EditText tuesdayBereavementLeaveHours;
    EditText wednesdayBereavementLeaveMinutes;
    EditText wednesdayBereavementLeaveHours;
    EditText thursdayBereavementLeaveMinutes;
    EditText thursdayBereavementLeaveHours;
    EditText fridayBereavementLeaveMinutes;
    EditText fridayBereavementLeaveHours;
    EditText saturdayBereavementLeaveMinutes;
    EditText saturdayBereavementLeaveHours;
    EditText sundayBereavementLeaveMinutes;
    EditText sundayBereavementLeaveHours;

    EditText mondayOvertimeMinutes;
    EditText mondayOvertimeHours;
    EditText tuesdayOvertimeMinutes;
    EditText tuesdayOvertimeHours;
    EditText wednesdayOvertimeMinutes;
    EditText wednesdayOvertimeHours;
    EditText thursdayOvertimeMinutes;
    EditText thursdayOvertimeHours;
    EditText fridayOvertimeMinutes;
    EditText fridayOvertimeHours;
    EditText saturdayOvertimeMinutes;
    EditText saturdayOvertimeHours;
    EditText sundayOvertimeMinutes;
    EditText sundayOvertimeHours;

    TextView notesSummaryText;
    TextView notesSummaryLabel;

    TextView mondayDateText;

    EditText tafeDaysText;
    EditText tafeHoursText;
    EditText tafeMinutesText;

    CheckBox overtimeCheckBox;

    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_timesheet, container, false);
        act = getActivity();

        tsCore = new TimeSheetCore(SharedPrefManager.getSharedPref(act));
        profileData = tsCore.profileData;
        tsData = tsCore.timeSheetData;

        ScreenWidth = ((WindowManager) act.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getWidth();
        mondayDate = DateTimeHelper.FindNearestMondayFromNow ();

        hoursRemainingText = (TextView) view.findViewById(R.id.hoursRemainingText);
        BreadcrumbScrollbar = (HorizontalScrollView) view.findViewById(R.id.breadcrumbView);
        BreadcrumbGroup = (RadioGroup) view.findViewById(R.id.breadcrumbGroup);
        LinearLayout questionGroup = (LinearLayout) view.findViewById(R.id.questionContainer);
        questionArray = new Question[BreadcrumbGroup.getChildCount()];

        for (int i = 0; i < BreadcrumbGroup.getChildCount(); i++)
        {
            //For every breadcrumb (our radio buttons) add a delegate that connects them to their relevant question
            //This means there has to be matching number of questions to breadcrumbs. A safe assumption to make, I believe.
            View questionChild = questionGroup.getChildAt(i);
            if(questionChild != null)
            {
                final int j = i;
                //Should probably put all this into a constructor.
                questionArray[i] = new Question();
                questionArray[i].ID = i;	//The question number, index zero. Holding this for debug reasons at the moment
                questionArray[i].questionView = questionChild;
                questionArray[i].breadcrumbView = (RadioButton) BreadcrumbGroup.getChildAt(i);
                questionArray[i].timesheet = this;

                questionArray[i].HideBreadcrumb ();
                BreadcrumbGroup.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        questionArray[j].ClickEventHandler();
                    }
                });
               

                if(i > 0) questionArray[i-1].nextQuestion = questionArray[i];

            }
        }

        //Now that the breadcrumbs and questions are hooked up, let's close them all and open our first question by default.
        CloseAllQuestions ();
        OpenQuestion (0,true);

        //Next step, connecting all the generic buttons (like all the Next buttons)
        ArrayList<Button> nextButtons = new ArrayList ();

        ArrayList<Button> skipButtons = new ArrayList();

        FindChildrenRecursive(nextButtons, questionGroup, "Button", "Next");
        FindChildrenRecursive(skipButtons, questionGroup, "Button", "Skip");
        for(int i = 0; i < nextButtons.size(); i++) {
            Button btn = nextButtons.get(i);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((MembersActivity) act).hideSoftKeyboard();
                    //Open the next question.
                    OpenQuestion(currentQuestion.ID + 1, true);
                }
            });
        }

        for(int i = 0; i < skipButtons.size(); i++) {
            skipButtons.get(i).setVisibility(View.GONE);
        }


        ArrayList<Button> backButtons = new ArrayList ();
        FindChildrenRecursive(backButtons, questionGroup, "Button", "Back");
        for(int i = 0; i < backButtons.size(); i++) {
            final Button btn = backButtons.get(i);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    currentQuestion.HideBreadcrumb();

                    //Open the last visible question (don't open question-1 cuz you'll probably open a skipped question)
                    int lastOpenQuestionIndex = 0;

                    for(int i = summaryIndex; i > 0; i--)
                    {
                        if(questionArray[i].breadcrumbView.getVisibility() ==View.VISIBLE && !questionArray[i].greyedOut)
                        {
                            lastOpenQuestionIndex = i;
                            break;
                        }
                    }

                    OpenQuestion(lastOpenQuestionIndex, true);
                }
            });
        }


        //Now the really boring part. Linking all the timesheet UI inputs to our core data
        userInputs.add(new UserTextField(this,  (EditText) view.findViewById(R.id.notesText), tsData.getNotes()));
        userInputs.add(new UserTextField(this, (EditText) view.findViewById(R.id.employerCodeText), tsData.getEmployerCode()));

        //Now to link up the info buttons to their relevant information strings
        ArrayList<ImageView> infoButtons = new ArrayList();
        FindChildrenRecursive(infoButtons, questionGroup, "ImageView", null);
        infoArray = new ArrayList ();

        for(int i = 0; i < infoButtons.size(); i++)
        {
            ImageView image = infoButtons.get(i);

            String infoText = GetInfoTextFromImageID(image.getId());
            //If we found a string for the info image, turn it into a pop-up dialog.
            if(!TextUtils.isEmpty(infoText))
            {
                final InfoButton newButton = new InfoButton (GetInfoTextFromImageID (image.getId()), image, this);

                infoArray.add(newButton);
                image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        newButton.ClickEventHandler();
                    }
                });
            }
        }

        workplaceHoursType = (CheckBox) view.findViewById(R.id.workPlaceHoursType);
        workplaceHoursType.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                OnWorkPlaceHoursTypeCheckboxUpdated();
            }
        });

        TAFEHoursType = (CheckBox) view.findViewById(R.id.TAFEHoursType);
        TAFEHoursType.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                OnTAFEHoursTypeCheckboxUpdated();
            }
        });

        mixedHoursType = (CheckBox) view.findViewById(R.id.mixedHoursType);
        mixedHoursType.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                OnMixedHoursCheckboxUpdated();
            }
        });
       

        workDaysText = (EditText) view.findViewById(R.id.workDays);
        workDaysText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                OnWorkDaysUpdated();
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        workHoursText = (EditText) view.findViewById(R.id.workHours);
        workHoursText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                OnWorkHoursUpdated();
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        workMinutesText = (EditText) view.findViewById(R.id.workMinutes);
        workMinutesText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                OnWorkHoursUpdated();
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        InitializeOvertimeEventHandler();
        InitializeSickLeaveEventHandlers();
        InitializeAnnualLeaveEventHandlers ();
        InitializeBereavementLeaveEventHandlers ();
        InitializeVACCTimeEventHandlers ();

        tafeDaysText = (EditText) view.findViewById(R.id.tafeDays);
        tafeDaysText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                OnTAFEDaysUpdated();
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });


        tafeHoursText = (EditText) view.findViewById(R.id.tafeHours);
        tafeHoursText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                OnTAFEHoursUpdated();
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });


        tafeMinutesText = (EditText) view.findViewById(R.id.tafeMinutes);
        tafeMinutesText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                OnTAFEHoursUpdated();
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        view.findViewById(R.id.clearButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Reset();
            }
        });


        ImageButton nextMonday = (ImageButton) view.findViewById(R.id.nextMonday);
        nextMonday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnNextMonday();
            }
        });


        ImageButton prevMonday = (ImageButton) view.findViewById(R.id.prevMonday);
        prevMonday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnPreviousMonday();
            }
        });

        mondayDateText = (TextView) view.findViewById(R.id.startingMondayText);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd, MMMM yyyy");
        mondayDateText.setText(dateFormat.format(mondayDate.getTime()));
        tsData.setStartDate(mondayDate);

        EditText notesText = (EditText) view.findViewById(R.id.notesText);
        notesText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                OnNotesUpdated();
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        Button sendbutton = (Button) view.findViewById(R.id.sendButton);
        sendbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VerifyTimesheet();
            }
        });

        notesSummaryText = (TextView) view.findViewById(R.id.notesSummaryText);
        notesSummaryLabel = (TextView) view.findViewById(R.id.notesSummaryLabel);

        ImageView vaccView = (ImageView) view.findViewById(R.id.VACCBanner);
        vaccView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               act.onBackPressed();
            }
        });
        return view;
    }

    private void InitializeOvertimeEventHandler() {
        overtimeCheckBox = (CheckBox) view.findViewById(R.id.overtimeCheckbox);
        overtimeCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                onCheckboxUpdate(R.id.overTimeLayout, overtimeCheckBox, 1);
            }
        });

        mondayOvertimeMinutes = (EditText) view.findViewById(R.id.mondayOvertimeMinutesText);
        mondayOvertimeHours = (EditText) view.findViewById(R.id.mondayOvertimeHoursText);

        tuesdayOvertimeMinutes = (EditText) view.findViewById(R.id.tuesdayOvertimeMinutesText);
        tuesdayOvertimeHours = (EditText) view.findViewById(R.id.tuesdayOvertimeHoursText);

        wednesdayOvertimeMinutes = (EditText) view.findViewById(R.id.wednesdayOvertimeMinutesText);
        wednesdayOvertimeHours = (EditText) view.findViewById(R.id.wednesdayOvertimeHoursText);

        thursdayOvertimeMinutes = (EditText) view.findViewById(R.id.thursdayOvertimeMinutesText);
        thursdayOvertimeHours = (EditText) view.findViewById(R.id.thursdayOvertimeHoursText);

        fridayOvertimeMinutes = (EditText) view.findViewById(R.id.fridayOvertimeMinutesText);
        fridayOvertimeHours = (EditText) view.findViewById(R.id.fridayOvertimeHoursText);

        saturdayOvertimeMinutes = (EditText) view.findViewById(R.id.saturdayOvertimeMinutesText);
        saturdayOvertimeHours = (EditText) view.findViewById(R.id.saturdayOvertimeHoursText);

        sundayOvertimeMinutes = (EditText) view.findViewById(R.id.sundayOvertimeMinutesText);
        sundayOvertimeHours = (EditText) view.findViewById(R.id.sundayOvertimeHoursText);

        setOnOverTimeTextChanged(mondayOvertimeMinutes, "Monday", R.id.mondayOvertimeMinutesText, R.id.mondayOvertimeHoursText);
        setOnOverTimeTextChanged(mondayOvertimeHours, "Monday", R.id.mondayOvertimeMinutesText, R.id.mondayOvertimeHoursText);

        setOnOverTimeTextChanged(tuesdayOvertimeMinutes, "Tuesday", R.id.tuesdayOvertimeMinutesText, R.id.tuesdayOvertimeHoursText);
        setOnOverTimeTextChanged(tuesdayOvertimeHours, "Tuesday", R.id.tuesdayOvertimeMinutesText, R.id.tuesdayOvertimeHoursText);

        setOnOverTimeTextChanged(wednesdayOvertimeMinutes, "Wednesday", R.id.wednesdayOvertimeMinutesText, R.id.wednesdayOvertimeHoursText);
        setOnOverTimeTextChanged(wednesdayOvertimeHours, "Wednesday", R.id.wednesdayOvertimeMinutesText, R.id.wednesdayOvertimeHoursText);

        setOnOverTimeTextChanged(thursdayOvertimeMinutes, "Thursday", R.id.thursdayOvertimeMinutesText, R.id.thursdayOvertimeHoursText);
        setOnOverTimeTextChanged(thursdayOvertimeHours, "Thursday", R.id.thursdayOvertimeMinutesText, R.id.thursdayOvertimeHoursText);

        setOnOverTimeTextChanged(fridayOvertimeMinutes, "Friday", R.id.fridayOvertimeMinutesText, R.id.fridayOvertimeHoursText);
        setOnOverTimeTextChanged(fridayOvertimeHours, "Friday", R.id.fridayOvertimeMinutesText, R.id.fridayOvertimeHoursText);

        setOnOverTimeTextChanged(saturdayOvertimeMinutes, "Saturday", R.id.saturdayOvertimeMinutesText, R.id.saturdayOvertimeHoursText);
        setOnOverTimeTextChanged(saturdayOvertimeHours, "Saturday", R.id.saturdayOvertimeMinutesText, R.id.saturdayOvertimeHoursText);

        setOnOverTimeTextChanged(sundayOvertimeMinutes, "Sunday", R.id.sundayOvertimeMinutesText, R.id.sundayOvertimeHoursText);
        setOnOverTimeTextChanged(sundayOvertimeHours, "Sunday", R.id.sundayOvertimeMinutesText, R.id.sundayOvertimeHoursText);
    }


    private void ResetOvertimeUI() {
        mondayOvertimeMinutes.setText("");
        mondayOvertimeHours.setText("");
        tuesdayOvertimeMinutes.setText("");
        tuesdayOvertimeHours.setText("");
        wednesdayOvertimeMinutes.setText("");
        wednesdayOvertimeHours.setText("");
        thursdayOvertimeMinutes.setText("");
        thursdayOvertimeHours.setText("");
        fridayOvertimeMinutes.setText("");
        fridayOvertimeHours.setText("");
        saturdayOvertimeMinutes.setText("");
        saturdayOvertimeHours.setText("");
        sundayOvertimeMinutes.setText("");
        sundayOvertimeHours.setText("");
        setOnOverTimeTextChanged(mondayOvertimeMinutes, "Monday", R.id.mondayOvertimeMinutesText, R.id.mondayOvertimeHoursText);
        setOnOverTimeTextChanged(tuesdayOvertimeMinutes, "Tuesday", R.id.tuesdayOvertimeMinutesText, R.id.tuesdayOvertimeHoursText);
        setOnOverTimeTextChanged(wednesdayOvertimeMinutes, "Wednesday", R.id.wednesdayOvertimeMinutesText, R.id.wednesdayOvertimeHoursText);
        setOnOverTimeTextChanged(thursdayOvertimeHours, "Thursday", R.id.thursdayOvertimeMinutesText, R.id.thursdayOvertimeHoursText);
        setOnOverTimeTextChanged(fridayOvertimeHours, "Friday", R.id.fridayOvertimeMinutesText, R.id.fridayOvertimeHoursText);
        setOnOverTimeTextChanged(saturdayOvertimeHours, "Saturday", R.id.saturdayOvertimeMinutesText, R.id.saturdayOvertimeHoursText);
        setOnOverTimeTextChanged(sundayOvertimeHours, "Sunday", R.id.sundayOvertimeMinutesText, R.id.sundayOvertimeHoursText);
    }

    private void setOnOverTimeTextChanged(EditText editText, final String day, final int minutesId, final int hoursId) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                for(int i = 0; i < tsData.getOvertimeHours().size(); i++) {
                    String item = tsData.getOvertimeHours().get(i);
                    if(item.contains(day)) {
                        tsData.getOvertimeHours().set(i,(day+"---"+GetHoursFromUI(minutesId,
                                hoursId, day)));
                        break;
                    }
                }
                tsData.calculateTimeHalfAndDoubleTime();

                UpdateHoursRemaining();
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    

    private void InitializeSickLeaveEventHandlers() {
        sickLeaveCheckBox = (CheckBox) view.findViewById(R.id.sickLeaveCheckbox);
        sickLeaveCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                onCheckboxUpdate(R.id.sickLeaveLayout, sickLeaveCheckBox, 2);
            }
        });

        mondaySickLeaveMinutes = (EditText) view.findViewById(R.id.mondaySickLeaveMinutesText);
        mondaySickLeaveHours = (EditText) view.findViewById(R.id.mondaySickLeaveHoursText);
        tuesdaySickLeaveMinutes = (EditText) view.findViewById(R.id.tuesdaySickLeaveMinutesText);
        tuesdaySickLeaveHours = (EditText) view.findViewById(R.id.tuesdaySickLeaveHoursText);
        wednesdaySickLeaveMinutes = (EditText) view.findViewById(R.id.wednesdaySickLeaveMinutesText);
        wednesdaySickLeaveHours = (EditText) view.findViewById(R.id.wednesdaySickLeaveHoursText);
        thursdaySickLeaveMinutes = (EditText) view.findViewById(R.id.thursdaySickLeaveMinutesText);
        thursdaySickLeaveHours = (EditText) view.findViewById(R.id.thursdaySickLeaveHoursText);
        fridaySickLeaveMinutes = (EditText) view.findViewById(R.id.fridaySickLeaveMinutesText);
        fridaySickLeaveHours = (EditText) view.findViewById(R.id.fridaySickLeaveHoursText);
        saturdaySickLeaveMinutes = (EditText) view.findViewById(R.id.saturdaySickLeaveMinutesText);
        saturdaySickLeaveHours = (EditText) view.findViewById(R.id.saturdaySickLeaveHoursText);
        sundaySickLeaveMinutes = (EditText) view.findViewById(R.id.sundaySickLeaveMinutesText);
        sundaySickLeaveHours = (EditText) view.findViewById(R.id.sundaySickLeaveHoursText);

        setOnSickLeaveTextChanged(mondaySickLeaveMinutes, "Monday", R.id.mondaySickLeaveMinutesText, R.id.mondaySickLeaveHoursText);
        setOnSickLeaveTextChanged(mondaySickLeaveHours, "Monday", R.id.mondaySickLeaveMinutesText, R.id.mondaySickLeaveHoursText);
        setOnSickLeaveTextChanged(tuesdaySickLeaveMinutes, "Tuesday", R.id.tuesdaySickLeaveMinutesText, R.id.tuesdaySickLeaveHoursText);
        setOnSickLeaveTextChanged(tuesdaySickLeaveHours, "Tuesday", R.id.tuesdaySickLeaveMinutesText, R.id.tuesdaySickLeaveHoursText);
        setOnSickLeaveTextChanged(wednesdaySickLeaveMinutes, "Wednesday", R.id.wednesdaySickLeaveMinutesText, R.id.wednesdaySickLeaveHoursText);
        setOnSickLeaveTextChanged(wednesdaySickLeaveHours, "Wednesday", R.id.wednesdaySickLeaveMinutesText, R.id.wednesdaySickLeaveHoursText);
        setOnSickLeaveTextChanged(thursdaySickLeaveMinutes, "Thursday", R.id.thursdaySickLeaveMinutesText, R.id.thursdaySickLeaveHoursText);
        setOnSickLeaveTextChanged(thursdaySickLeaveHours, "Thursday", R.id.thursdaySickLeaveMinutesText, R.id.thursdaySickLeaveHoursText);
        setOnSickLeaveTextChanged(fridaySickLeaveMinutes, "Friday", R.id.fridaySickLeaveMinutesText, R.id.fridaySickLeaveHoursText);
        setOnSickLeaveTextChanged(fridaySickLeaveHours, "Friday", R.id.fridaySickLeaveMinutesText, R.id.fridaySickLeaveHoursText);
        setOnSickLeaveTextChanged(saturdaySickLeaveMinutes, "Saturday", R.id.saturdaySickLeaveMinutesText, R.id.saturdaySickLeaveHoursText);
        setOnSickLeaveTextChanged(saturdaySickLeaveHours, "Saturday", R.id.saturdaySickLeaveMinutesText, R.id.saturdaySickLeaveHoursText);
        setOnSickLeaveTextChanged(sundaySickLeaveMinutes, "Sunday", R.id.sundaySickLeaveMinutesText, R.id.sundaySickLeaveHoursText);
        setOnSickLeaveTextChanged(sundaySickLeaveHours, "Sunday", R.id.sundaySickLeaveMinutesText, R.id.sundaySickLeaveHoursText);
    }

    private void ResetSickLeaveUI() {
        mondaySickLeaveMinutes.setText("");
        mondaySickLeaveHours.setText("");
        tuesdaySickLeaveMinutes.setText("");
        tuesdaySickLeaveHours.setText("");
        wednesdaySickLeaveMinutes.setText("");
        wednesdaySickLeaveHours.setText("");
        thursdaySickLeaveMinutes.setText("");
        thursdaySickLeaveHours.setText("");
        fridaySickLeaveMinutes.setText("");
        fridaySickLeaveHours.setText("");
        saturdaySickLeaveMinutes.setText("");
        saturdaySickLeaveHours.setText("");
        sundaySickLeaveMinutes.setText("");
        sundaySickLeaveHours.setText("");

        setOnSickLeaveTextChanged(mondaySickLeaveMinutes, "Monday", R.id.mondaySickLeaveMinutesText, R.id.mondaySickLeaveHoursText);
        setOnSickLeaveTextChanged(tuesdaySickLeaveMinutes, "Tuesday", R.id.tuesdaySickLeaveMinutesText, R.id.tuesdaySickLeaveHoursText);
        setOnSickLeaveTextChanged(wednesdaySickLeaveMinutes, "Wednesday", R.id.wednesdaySickLeaveMinutesText, R.id.wednesdaySickLeaveHoursText);
        setOnSickLeaveTextChanged(thursdaySickLeaveMinutes, "Thursday", R.id.thursdaySickLeaveMinutesText, R.id.thursdaySickLeaveHoursText);
        setOnSickLeaveTextChanged(fridaySickLeaveMinutes, "Friday", R.id.fridaySickLeaveMinutesText, R.id.fridaySickLeaveHoursText);
        setOnSickLeaveTextChanged(saturdaySickLeaveMinutes, "Saturday", R.id.saturdaySickLeaveMinutesText, R.id.saturdaySickLeaveHoursText);
        setOnSickLeaveTextChanged(sundaySickLeaveMinutes, "Sunday", R.id.sundaySickLeaveMinutesText, R.id.sundaySickLeaveHoursText);
    }

    private void setOnSickLeaveTextChanged(EditText editText, final String day, final int minutesId, final int hoursId) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                for(int i = 0; i < tsData.getSickLeaveHours().size(); i++) {
                    String item = tsData.getSickLeaveHours().get(i);
                    if(item.contains(day)) {
                        tsData.getSickLeaveHours().set(i,(day+"---"+GetHoursFromUI(minutesId,
                                hoursId, day)));
                        break;
                    }
                }
                UpdateHoursRemaining();
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
    }
    


    private void InitializeAnnualLeaveEventHandlers() {
        annualLeaveCheckBox = (CheckBox) view.findViewById(R.id.annualLeaveCheckbox);
        annualLeaveCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                onCheckboxUpdate(R.id.annualLeaveLayout, annualLeaveCheckBox, 3);
            }
        });

        mondayAnnualLeaveMinutes = (EditText) view.findViewById(R.id.mondayAnnualLeaveMinutesText);
        mondayAnnualLeaveHours = (EditText) view.findViewById(R.id.mondayAnnualLeaveHoursText);
        tuesdayAnnualLeaveMinutes = (EditText) view.findViewById(R.id.tuesdayAnnualLeaveMinutesText);
        tuesdayAnnualLeaveHours = (EditText) view.findViewById(R.id.tuesdayAnnualLeaveHoursText);
        wednesdayAnnualLeaveMinutes = (EditText) view.findViewById(R.id.wednesdayAnnualLeaveMinutesText);
        wednesdayAnnualLeaveHours = (EditText) view.findViewById(R.id.wednesdayAnnualLeaveHoursText);
        thursdayAnnualLeaveMinutes = (EditText) view.findViewById(R.id.thursdayAnnualLeaveMinutesText);
        thursdayAnnualLeaveHours = (EditText) view.findViewById(R.id.thursdayAnnualLeaveHoursText);
        fridayAnnualLeaveMinutes = (EditText) view.findViewById(R.id.fridayAnnualLeaveMinutesText);
        fridayAnnualLeaveHours = (EditText) view.findViewById(R.id.fridayAnnualLeaveHoursText);
        saturdayAnnualLeaveMinutes = (EditText) view.findViewById(R.id.saturdayAnnualLeaveMinutesText);
        saturdayAnnualLeaveHours = (EditText) view.findViewById(R.id.saturdayAnnualLeaveHoursText);
        sundayAnnualLeaveMinutes = (EditText) view.findViewById(R.id.sundayAnnualLeaveMinutesText);
        sundayAnnualLeaveHours = (EditText) view.findViewById(R.id.sundayAnnualLeaveHoursText);

        setOnAnnualLeaveTextChanged(mondayAnnualLeaveMinutes, "Monday", R.id.mondayAnnualLeaveMinutesText, R.id.mondayAnnualLeaveHoursText);
        setOnAnnualLeaveTextChanged(mondayAnnualLeaveHours, "Monday", R.id.mondayAnnualLeaveMinutesText, R.id.mondayAnnualLeaveHoursText);
        setOnAnnualLeaveTextChanged(tuesdayAnnualLeaveMinutes, "Tuesday", R.id.tuesdayAnnualLeaveMinutesText, R.id.tuesdayAnnualLeaveHoursText);
        setOnAnnualLeaveTextChanged(tuesdayAnnualLeaveHours, "Tuesday", R.id.tuesdayAnnualLeaveMinutesText, R.id.tuesdayAnnualLeaveHoursText);
        setOnAnnualLeaveTextChanged(wednesdayAnnualLeaveMinutes, "Wednesday", R.id.wednesdayAnnualLeaveMinutesText, R.id.wednesdayAnnualLeaveHoursText);
        setOnAnnualLeaveTextChanged(wednesdayAnnualLeaveHours, "Wednesday", R.id.wednesdayAnnualLeaveMinutesText, R.id.wednesdayAnnualLeaveHoursText);
        setOnAnnualLeaveTextChanged(thursdayAnnualLeaveMinutes, "Thursday", R.id.thursdayAnnualLeaveMinutesText, R.id.thursdayAnnualLeaveHoursText);
        setOnAnnualLeaveTextChanged(thursdayAnnualLeaveHours, "Thursday", R.id.thursdayAnnualLeaveMinutesText, R.id.thursdayAnnualLeaveHoursText);
        setOnAnnualLeaveTextChanged(fridayAnnualLeaveMinutes, "Friday", R.id.fridayAnnualLeaveMinutesText, R.id.fridayAnnualLeaveHoursText);
        setOnAnnualLeaveTextChanged(fridayAnnualLeaveHours, "Friday", R.id.fridayAnnualLeaveMinutesText, R.id.fridayAnnualLeaveHoursText);
        setOnAnnualLeaveTextChanged(saturdayAnnualLeaveMinutes, "Saturday", R.id.saturdayAnnualLeaveMinutesText, R.id.saturdayAnnualLeaveHoursText);
        setOnAnnualLeaveTextChanged(saturdayAnnualLeaveHours, "Saturday", R.id.saturdayAnnualLeaveMinutesText, R.id.saturdayAnnualLeaveHoursText);
        setOnAnnualLeaveTextChanged(sundayAnnualLeaveMinutes, "Sunday", R.id.sundayAnnualLeaveMinutesText, R.id.sundayAnnualLeaveHoursText);
        setOnAnnualLeaveTextChanged(sundayAnnualLeaveHours, "Sunday", R.id.sundayAnnualLeaveMinutesText, R.id.sundayAnnualLeaveHoursText);
        
    }

    private void ResetAnnualLeaveUI() {
        mondayAnnualLeaveMinutes.setText("");
        mondayAnnualLeaveHours.setText("");
        tuesdayAnnualLeaveMinutes.setText("");
        tuesdayAnnualLeaveHours.setText("");
        wednesdayAnnualLeaveMinutes.setText("");
        wednesdayAnnualLeaveHours.setText("");
        thursdayAnnualLeaveMinutes.setText("");
        thursdayAnnualLeaveHours.setText("");
        fridayAnnualLeaveMinutes.setText("");
        fridayAnnualLeaveHours.setText("");
        saturdayAnnualLeaveMinutes.setText("");
        saturdayAnnualLeaveHours.setText("");
        sundayAnnualLeaveMinutes.setText("");
        sundayAnnualLeaveHours.setText("");

        setOnAnnualLeaveTextChanged(mondayAnnualLeaveMinutes, "Monday", R.id.mondayAnnualLeaveMinutesText, R.id.mondayAnnualLeaveHoursText);
        setOnAnnualLeaveTextChanged(tuesdayAnnualLeaveMinutes, "Tuesday", R.id.tuesdayAnnualLeaveMinutesText, R.id.tuesdayAnnualLeaveHoursText);
        setOnAnnualLeaveTextChanged(wednesdayAnnualLeaveMinutes, "Wednesday", R.id.wednesdayAnnualLeaveMinutesText, R.id.wednesdayAnnualLeaveHoursText);
        setOnAnnualLeaveTextChanged(thursdayAnnualLeaveMinutes, "Thursday", R.id.thursdayAnnualLeaveMinutesText, R.id.thursdayAnnualLeaveHoursText);
        setOnAnnualLeaveTextChanged(fridayAnnualLeaveMinutes, "Friday", R.id.fridayAnnualLeaveMinutesText, R.id.fridayAnnualLeaveHoursText);
        setOnAnnualLeaveTextChanged(saturdayAnnualLeaveMinutes, "Saturday", R.id.saturdayAnnualLeaveMinutesText, R.id.saturdayAnnualLeaveHoursText);
        setOnAnnualLeaveTextChanged(sundayAnnualLeaveMinutes, "Sunday", R.id.sundayAnnualLeaveMinutesText, R.id.sundayAnnualLeaveHoursText);
    }

    private void setOnAnnualLeaveTextChanged(EditText editText, final String day, final int minutesId, final int hoursId) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                for(int i = 0; i < tsData.getAnnualLeaveHours().size(); i++) {
                    String item = tsData.getAnnualLeaveHours().get(i);
                    if(item.contains(day)) {
                        tsData.getAnnualLeaveHours().set(i,(day+"---"+GetHoursFromUI(minutesId,
                                hoursId, day)));
                        break;
                    }
                }
                UpdateHoursRemaining();
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void InitializeBereavementLeaveEventHandlers() {
        bereavementLeaveCheckBox = (CheckBox) view.findViewById(R.id.bereavementLeaveCheckbox);
        bereavementLeaveCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                onCheckboxUpdate(R.id.bereavementLeaveLayout, bereavementLeaveCheckBox, 4);
            }
        });

        mondayBereavementLeaveMinutes = (EditText) view.findViewById(R.id.mondayBereavementLeaveMinutesText);
        mondayBereavementLeaveHours = (EditText) view.findViewById(R.id.mondayBereavementLeaveHoursText);
        tuesdayBereavementLeaveMinutes = (EditText) view.findViewById(R.id.tuesdayBereavementLeaveMinutesText);
        tuesdayBereavementLeaveHours = (EditText) view.findViewById(R.id.tuesdayBereavementLeaveHoursText);
        wednesdayBereavementLeaveMinutes = (EditText) view.findViewById(R.id.wednesdayBereavementLeaveMinutesText);
        wednesdayBereavementLeaveHours = (EditText) view.findViewById(R.id.wednesdayBereavementLeaveHoursText);
        thursdayBereavementLeaveMinutes = (EditText) view.findViewById(R.id.thursdayBereavementLeaveMinutesText);
        thursdayBereavementLeaveHours = (EditText) view.findViewById(R.id.thursdayBereavementLeaveHoursText);
        fridayBereavementLeaveMinutes = (EditText) view.findViewById(R.id.fridayBereavementLeaveMinutesText);
        fridayBereavementLeaveHours = (EditText) view.findViewById(R.id.fridayBereavementLeaveHoursText);
        saturdayBereavementLeaveMinutes = (EditText) view.findViewById(R.id.saturdayBereavementLeaveMinutesText);
        saturdayBereavementLeaveHours = (EditText) view.findViewById(R.id.saturdayBereavementLeaveHoursText);
        sundayBereavementLeaveMinutes = (EditText) view.findViewById(R.id.sundayBereavementLeaveMinutesText);
        sundayBereavementLeaveHours = (EditText) view.findViewById(R.id.sundayBereavementLeaveHoursText);

        setOnBereavementLeaveTextChanged(mondayBereavementLeaveMinutes, "Monday", R.id.mondayBereavementLeaveMinutesText, R.id.mondayBereavementLeaveHoursText);
        setOnBereavementLeaveTextChanged(mondayBereavementLeaveHours, "Monday", R.id.mondayBereavementLeaveMinutesText, R.id.mondayBereavementLeaveHoursText);
        setOnBereavementLeaveTextChanged(tuesdayBereavementLeaveMinutes, "Tuesday", R.id.tuesdayBereavementLeaveMinutesText, R.id.tuesdayBereavementLeaveHoursText);
        setOnBereavementLeaveTextChanged(tuesdayBereavementLeaveHours, "Tuesday", R.id.tuesdayBereavementLeaveMinutesText, R.id.tuesdayBereavementLeaveHoursText);
        setOnBereavementLeaveTextChanged(wednesdayBereavementLeaveMinutes, "Wednesday", R.id.wednesdayBereavementLeaveMinutesText, R.id.wednesdayBereavementLeaveHoursText);
        setOnBereavementLeaveTextChanged(wednesdayBereavementLeaveHours, "Wednesday", R.id.wednesdayBereavementLeaveMinutesText, R.id.wednesdayBereavementLeaveHoursText);
        setOnBereavementLeaveTextChanged(thursdayBereavementLeaveMinutes, "Thursday", R.id.thursdayBereavementLeaveMinutesText, R.id.thursdayBereavementLeaveHoursText);
        setOnBereavementLeaveTextChanged(thursdayBereavementLeaveHours, "Thursday", R.id.thursdayBereavementLeaveMinutesText, R.id.thursdayBereavementLeaveHoursText);
        setOnBereavementLeaveTextChanged(fridayBereavementLeaveMinutes, "Friday", R.id.fridayBereavementLeaveMinutesText, R.id.fridayBereavementLeaveHoursText);
        setOnBereavementLeaveTextChanged(fridayBereavementLeaveHours, "Friday", R.id.fridayBereavementLeaveMinutesText, R.id.fridayBereavementLeaveHoursText);
        setOnBereavementLeaveTextChanged(saturdayBereavementLeaveMinutes, "Saturday", R.id.saturdayBereavementLeaveMinutesText, R.id.saturdayBereavementLeaveHoursText);
        setOnBereavementLeaveTextChanged(saturdayBereavementLeaveHours, "Saturday", R.id.saturdayBereavementLeaveMinutesText, R.id.saturdayBereavementLeaveHoursText);
        setOnBereavementLeaveTextChanged(sundayBereavementLeaveMinutes, "Sunday", R.id.sundayBereavementLeaveMinutesText, R.id.sundayBereavementLeaveHoursText);
        setOnBereavementLeaveTextChanged(sundayBereavementLeaveHours, "Sunday", R.id.sundayBereavementLeaveMinutesText, R.id.sundayBereavementLeaveHoursText);
    }

    private void ResetBereavementLeaveUI() {
        mondayBereavementLeaveMinutes.setText("");
        mondayBereavementLeaveHours.setText("");
        tuesdayBereavementLeaveMinutes.setText("");
        tuesdayBereavementLeaveHours.setText("");
        wednesdayBereavementLeaveMinutes.setText("");
        wednesdayBereavementLeaveHours.setText("");
        thursdayBereavementLeaveMinutes.setText("");
        thursdayBereavementLeaveHours.setText("");
        fridayBereavementLeaveMinutes.setText("");
        fridayBereavementLeaveHours.setText("");
        saturdayBereavementLeaveMinutes.setText("");
        saturdayBereavementLeaveHours.setText("");
        sundayBereavementLeaveMinutes.setText("");
        sundayBereavementLeaveHours.setText("");

        setOnBereavementLeaveTextChanged(mondayBereavementLeaveMinutes, "Monday", R.id.mondayBereavementLeaveMinutesText, R.id.mondayBereavementLeaveHoursText);
        setOnBereavementLeaveTextChanged(tuesdayBereavementLeaveMinutes, "Tuesday", R.id.tuesdayBereavementLeaveMinutesText, R.id.tuesdayBereavementLeaveHoursText);
        setOnBereavementLeaveTextChanged(wednesdayBereavementLeaveMinutes, "Wednesday", R.id.wednesdayBereavementLeaveMinutesText, R.id.wednesdayBereavementLeaveHoursText);
        setOnBereavementLeaveTextChanged(thursdayBereavementLeaveHours, "Thursday", R.id.thursdayBereavementLeaveMinutesText, R.id.thursdayBereavementLeaveHoursText);
        setOnBereavementLeaveTextChanged(fridayBereavementLeaveHours, "Friday", R.id.fridayBereavementLeaveMinutesText, R.id.fridayBereavementLeaveHoursText);
        setOnBereavementLeaveTextChanged(saturdayBereavementLeaveHours, "Saturday", R.id.saturdayBereavementLeaveMinutesText, R.id.saturdayBereavementLeaveHoursText);
        setOnBereavementLeaveTextChanged(sundayBereavementLeaveHours, "Sunday", R.id.sundayBereavementLeaveMinutesText, R.id.sundayBereavementLeaveHoursText);
    }


    private void setOnBereavementLeaveTextChanged(EditText editText, final String day, final int minutesId, final int hoursId) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                for(int i = 0; i < tsData.getBereavementLeaveHours().size(); i++) {
                    String item = tsData.getBereavementLeaveHours().get(i);
                    if(item.contains(day)) {
                        tsData.getBereavementLeaveHours().set(i,(day+"---"+GetHoursFromUI(minutesId,
                                hoursId, day)));
                        break;
                    }
                }
                UpdateHoursRemaining();
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void InitializeVACCTimeEventHandlers() {
        vaccTimeCheckBox = (CheckBox) view.findViewById(R.id.vaccTimeCheckbox);
        vaccTimeCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                onCheckboxUpdate(R.id.vaccTimeLayout, vaccTimeCheckBox, 5);
            }
        });

        mondayVACCTimeMinutes = (EditText) view.findViewById(R.id.mondayVACCTimeMinutesText);
        mondayVACCTimeHours = (EditText) view.findViewById(R.id.mondayVACCTimeHoursText);
        tuesdayVACCTimeMinutes = (EditText) view.findViewById(R.id.tuesdayVACCTimeMinutesText);
        tuesdayVACCTimeHours = (EditText) view.findViewById(R.id.tuesdayVACCTimeHoursText);
        wednesdayVACCTimeMinutes = (EditText) view.findViewById(R.id.wednesdayVACCTimeMinutesText);
        wednesdayVACCTimeHours = (EditText) view.findViewById(R.id.wednesdayVACCTimeHoursText);
        thursdayVACCTimeMinutes = (EditText) view.findViewById(R.id.thursdayVACCTimeMinutesText);
        thursdayVACCTimeHours = (EditText) view.findViewById(R.id.thursdayVACCTimeHoursText);
        fridayVACCTimeMinutes = (EditText) view.findViewById(R.id.fridayVACCTimeMinutesText);
        fridayVACCTimeHours = (EditText) view.findViewById(R.id.fridayVACCTimeHoursText);
        saturdayVACCTimeMinutes = (EditText) view.findViewById(R.id.saturdayVACCTimeMinutesText);
        saturdayVACCTimeHours = (EditText) view.findViewById(R.id.saturdayVACCTimeHoursText);
        sundayVACCTimeMinutes = (EditText) view.findViewById(R.id.sundayVACCTimeMinutesText);
        sundayVACCTimeHours = (EditText) view.findViewById(R.id.sundayVACCTimeHoursText);
        
        setOnVACCTimeTextChanged(mondayVACCTimeMinutes, "Monday", R.id.mondayVACCTimeMinutesText, R.id.mondayVACCTimeHoursText);
        setOnVACCTimeTextChanged(mondayVACCTimeHours, "Monday", R.id.mondayVACCTimeMinutesText, R.id.mondayVACCTimeHoursText);
        setOnVACCTimeTextChanged(tuesdayVACCTimeMinutes, "Tuesday", R.id.tuesdayVACCTimeMinutesText, R.id.tuesdayVACCTimeHoursText);
        setOnVACCTimeTextChanged(tuesdayVACCTimeHours, "Tuesday", R.id.tuesdayVACCTimeMinutesText, R.id.tuesdayVACCTimeHoursText);
        setOnVACCTimeTextChanged(wednesdayVACCTimeMinutes, "Wednesday", R.id.wednesdayVACCTimeMinutesText, R.id.wednesdayVACCTimeHoursText);
        setOnVACCTimeTextChanged(wednesdayVACCTimeHours, "Wednesday", R.id.wednesdayVACCTimeMinutesText, R.id.wednesdayVACCTimeHoursText);
        setOnVACCTimeTextChanged(thursdayVACCTimeMinutes, "Thursday", R.id.thursdayVACCTimeMinutesText, R.id.thursdayVACCTimeHoursText);
        setOnVACCTimeTextChanged(thursdayVACCTimeHours, "Thursday", R.id.thursdayVACCTimeMinutesText, R.id.thursdayVACCTimeHoursText);
        setOnVACCTimeTextChanged(fridayVACCTimeMinutes, "Friday", R.id.fridayVACCTimeMinutesText, R.id.fridayVACCTimeHoursText);
        setOnVACCTimeTextChanged(fridayVACCTimeHours, "Friday", R.id.fridayVACCTimeMinutesText, R.id.fridayVACCTimeHoursText);
        setOnVACCTimeTextChanged(saturdayVACCTimeMinutes, "Saturday", R.id.saturdayVACCTimeMinutesText, R.id.saturdayVACCTimeHoursText);
        setOnVACCTimeTextChanged(saturdayVACCTimeHours, "Saturday", R.id.saturdayVACCTimeMinutesText, R.id.saturdayVACCTimeHoursText);
        setOnVACCTimeTextChanged(sundayVACCTimeMinutes, "Sunday", R.id.sundayVACCTimeMinutesText, R.id.sundayVACCTimeHoursText);
        setOnVACCTimeTextChanged(sundayVACCTimeHours, "Sunday", R.id.sundayVACCTimeMinutesText, R.id.sundayVACCTimeHoursText);
    }

    private void ResetVACCTimeUI() {
        mondayVACCTimeMinutes.setText("");
        mondayVACCTimeHours.setText("");
        tuesdayVACCTimeMinutes.setText("");
        tuesdayVACCTimeHours.setText("");
        wednesdayVACCTimeMinutes.setText("");
        wednesdayVACCTimeHours.setText("");
        thursdayVACCTimeMinutes.setText("");
        thursdayVACCTimeHours.setText("");
        fridayVACCTimeMinutes.setText("");
        fridayVACCTimeHours.setText("");
        saturdayVACCTimeMinutes.setText("");
        saturdayVACCTimeHours.setText("");
        sundayVACCTimeMinutes.setText("");
        sundayVACCTimeHours.setText("");

        setOnVACCTimeTextChanged(mondayVACCTimeMinutes, "Monday", R.id.mondayVACCTimeMinutesText, R.id.mondayVACCTimeHoursText);
        setOnVACCTimeTextChanged(tuesdayVACCTimeMinutes, "Tuesday", R.id.tuesdayVACCTimeMinutesText, R.id.tuesdayVACCTimeHoursText);
        setOnVACCTimeTextChanged(wednesdayVACCTimeMinutes, "Wednesday", R.id.wednesdayVACCTimeMinutesText, R.id.wednesdayVACCTimeHoursText);
        setOnVACCTimeTextChanged(thursdayVACCTimeMinutes, "Thursday", R.id.thursdayVACCTimeMinutesText, R.id.thursdayVACCTimeHoursText);
        setOnVACCTimeTextChanged(fridayVACCTimeMinutes, "Friday", R.id.fridayVACCTimeMinutesText, R.id.fridayVACCTimeHoursText);
        setOnVACCTimeTextChanged(saturdayVACCTimeMinutes, "Saturday", R.id.saturdayVACCTimeMinutesText, R.id.saturdayVACCTimeHoursText);
        setOnVACCTimeTextChanged(sundayVACCTimeMinutes, "Sunday", R.id.sundayVACCTimeMinutesText, R.id.sundayVACCTimeHoursText);
    }

    private void setOnVACCTimeTextChanged(EditText editText, final String day, final int minutesId, final int hoursId) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                for(int i = 0; i < tsData.getVACCTimeHours().size(); i++) {
                    String item = tsData.getVACCTimeHours().get(i);
                    if(item.contains(day)) {
                        tsData.getVACCTimeHours().set(i,(day+"---"+GetHoursFromUI(minutesId,
                                hoursId, day)));
                        break;
                    }
                }
                UpdateHoursRemaining();
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    //Sets all the questions to 'gone' visibility state, effectively collapsing them all.
    public void CloseAllQuestions()
    {
        if (questionArray != null)
        {
            for (int i = 0; i < questionArray.length; i++)
            {
                if (questionArray [i] != null)
                {
                    questionArray [i].CloseQuestion ();
                }
            }
        }
    }

    //Hides the breadcrumbs from the index specified to the end.
    public void HideBreadcrumbsFrom(int indexOnwards)
    {
        for(int i = indexOnwards; i < questionArray.length; i++)
        {
            questionArray [i].HideBreadcrumb ();
        }
    }

    //Sets the breadcrumb and opens the question specified, by index (first question being 0)
    public void OpenQuestion(int index, boolean scroll)
    {
        ((MembersActivity) act).hideSoftKeyboard();

        //Hacktastic special conditions for special questions
        //If we're opening the summary screen (which isn't really a question) then save the timesheet and update
        //the summary.
        if(index == summaryIndex)
        {
            //Got to save everything before the summary can update.
            for(int i = 0; i < userInputs.size(); i++) {
                UserInputField userInput = userInputs.get(i);
                userInput.SaveData();
            }
            UpdateSummary ();
        }

        if(currentQuestion != null)
        {
            //Check for skip conditions.
            if(index > currentQuestion.ID) //We can only skip forward, so check that first
            {
                switch (currentQuestion.ID) {
                    case hoursTypeIndex:
                    {
                        if(workplaceHoursType.isChecked())
                        {
                            SkipToQuestion(overtimeIndex);
                            return;

                        } else if(TAFEHoursType.isChecked())
                        {
                            SkipToQuestion(notesIndex);
                            return;
                        }
                        break;
                    }

                    case overtimeIndex:
                    {
                        if(tsData.getWorkHours().compareTo(TimeSheetCore.HoursTarget) >= 0)
                        {
                            SkipToQuestion(notesIndex);
                            return;
                        }
                        break;
                    }
                }

                //Add more skips here if needed
            }

            //If we're opening a question back in the breadcrumb line, then grey out
            //the already opened questions (until the user changes a question and the
            //breadcrumbs need to update again)
            if(index < currentQuestion.ID)
            {
                for(int i = index+1; i < currentQuestion.ID+1; i++)
                {
                    questionArray[i].HideBreadcrumb();
                }
            }
        }
        else
        {
            HideBreadcrumbsFrom (index+1);
        }

        for(int i = 0; i < index; i++)
        {
            questionArray [i].Restore ();
        }

        questionArray[index].ShowBreadcrumb ();
        CloseAllQuestions ();
        questionArray [index].OpenQuestion ();
        questionArray [index].ActivateBreadcrumb (scroll);

    }

    public void SkipToQuestion(int index)
    {
        int currentIndex = currentQuestion.ID;

        //When skipping forward, hide the breadcrumbs of the questions we skipped
        for(int i = currentIndex + 1; i < index; i++)
        {
            questionArray[i].RemoveBreadcrumb();
        }

        questionArray[index].ShowBreadcrumb ();
        questionArray[index].OpenQuestion();
        questionArray[index].ActivateBreadcrumb (true);
    }

    private void SkipQuestion(int index)
    {
        questionArray[index].ShowBreadcrumb ();
        CloseAllQuestions ();
        questionArray[index].OpenQuestion ();
        questionArray[index].ActivateBreadcrumb (false);
    }

    public void VerifyTimesheet()
    {
        //Let's see if they've completed their profile. If not, we shall complain about it lacking details
        if(!profileData.isProfileComplete(profileData))
        {
            AlertDialog.Builder dialog = new AlertDialog.Builder(act);
            dialog.setTitle("Profile Incomplete");
            dialog.setMessage("Your profile is incomplete. Please switch to the profile tab and fill in the mandatory fields before sending your timesheet.");
            dialog.setPositiveButton("Close", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            dialog.show();

            //Early out here so we don't complain twice in a row. One step at a time.
            return;
        }

        //If we have enough hours we can build and send the timesheet email.
        //Otherwise we can complain via an alert that they need more hours.
        if((tsData.getTotalLoggedHours() == TimeSheetCore.HoursTarget) ||
                profileData.isSchoolBasedApprentice())	//school based apprentices are exempt from 38 hrs min
        {
            //Force save everything first.
            for(int i = 0; i < userInputs.size(); i++) {
                UserInputField userInput = userInputs.get(i);
                userInput.SaveData();
            }

            //Show the apprentice approval warning
            if(!employerApprovalMode)
            {
                AlertDialog.Builder dialog = new AlertDialog.Builder(act);
                dialog.setTitle("Timesheet Approval");
                dialog.setMessage("I declare that:\nThe information provided in this form is complete and correct.\n\nI understand that:\nThe VACC can make relevant enquiries to ensure that I receive the correct income.\n\nI also understand that providing false or misleading information is a serious offence and can if proved lead to disciplinary action including termination of employment and cancellation of my apprenticeship.");
                dialog.setPositiveButton("Agree", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        employerApprovalMode = true;
                        ((TextView) view.findViewById(R.id.employerWarning)).setVisibility(View.VISIBLE);

                        ((LinearLayout) view.findViewById(R.id.employerApprovalContainer)).setVisibility(View.VISIBLE);

                        //highlight the summary
                        ArrayList<TextView> textFields = new ArrayList ();

                        LinearLayout summaryParent = (LinearLayout) view.findViewById(R.id.summaryTableLayoutContainer);

                        FindChildrenRecursive(textFields, (ViewGroup) summaryParent, "TextView", null);

                        for(int i = 0; i < textFields.size(); i++) {
                            TextView text = textFields.get(i);
                            text.setTextColor(Color.argb(255, 255, 20, 20));
                        }

                        //jump to employer code for approval
                        ((EditText) view.findViewById(R.id.employerCodeText)).requestFocus();
                    }
                });
                dialog.setNegativeButton("Disagree", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                dialog.show();
            }
            else //We're in employer approval mode
            {
                if(TextUtils.isEmpty(((EditText) view.findViewById(R.id.employerCodeText)).getText().toString()))
                {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(act);
                    dialog.setTitle("Timesheet Approval");
                    dialog.setMessage("Employer approval code must be entered before approving the timesheet.");
                    dialog.setPositiveButton("Disagree", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    dialog.show();
                }
					else
                {

                    AlertDialog.Builder dialog = new AlertDialog.Builder(act);
                    dialog.setTitle("Timesheet Approval");
                    dialog.setMessage("I confirm the apprentice has undertaken the following hours and agree to have the amount invoiced where payment will be arranged within VACC credit terms.");
                    dialog.setPositiveButton("Agree", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                           tsData.setEmployerCode(((EditText) view.findViewById(R.id.employerCodeText)).getText().toString());
                            SaveTimeSheetDataAsCSVFile();
                            SubmitTimesheetToVacc();
                            //MailTimeSheet();
                        }
                    });

                    dialog.setNegativeButton("Disagree", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ((EditText) view.findViewById(R.id.employerCodeText)).setText("");
                        }
                    });
                    dialog.show();
                }
            }

        }
        else if((tsData.getTotalLoggedHours().compareTo(TimeSheetCore.HoursTarget) < 0) &&
                !profileData.isSchoolBasedApprentice())
        {
            AlertDialog.Builder dialog = new AlertDialog.Builder(act);
            dialog.setTitle("Timesheet Incomplete");
            dialog.setMessage("The required hours have not yet been met. Please navigate back to previous questions to complete the timesheet.");
            dialog.setPositiveButton("Close", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            dialog.show();
        }
    }

    public void MailTimeSheet()
    {
        String VACCEmailAddress = "payroll@vacc.com.au";
        Uri fileUri = FileProvider.getUriForFile(act, "au.com.vacc.timesheets.fileprovider", new File(act.getCacheDir() + "/csv", tsCore.GetCSVFilename()));
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL, new String[]{VACCEmailAddress, profileData.getExtraHostEmailAddress(), profileData.getEmployerEmailAddress()});
        i.putExtra(Intent.EXTRA_SUBJECT, "VACC Timesheet. Payroll No.: " + profileData.getApprenticeMembershipNumber());
        i.putExtra(Intent.EXTRA_TEXT, tsCore.GetTimesheetAsEmail());
        i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        i.putExtra(Intent.EXTRA_STREAM, fileUri);
        try {
            startActivityForResult(Intent.createChooser(i, "Send mail..."), 99);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(act, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }


        mailingTimeSheet = true;
    }

    private void SubmitTimesheetToVacc()
    {
        String postBody = tsCore.GetTimesheetDataAsPostBody();
        CustomJsonRequest jsonRequest = new CustomJsonRequest(act, "https://vacc.appswiz.com/api/reports/PostTimesheet", postBody, true, new CustomJsonCallBack() {
            @Override
            public void onError(String error) {
                showResponseDialog(0, error);
            }

            @Override
            public void getResult(String jsonNode) {
                showResponseDialog(1, "");
            }
        }, CustomJsonRequest.POST);
        jsonRequest.execute();
    }

    private void showResponseDialog(int status, String message)
    {
        AlertDialog.Builder dialog = new AlertDialog.Builder(act);
        if (status == 1)
        {
            ((MembersActivity)act).saveTimeStamp();
            dialog.setTitle("Timesheet successfully submitted");
            dialog.setMessage("The application will now close. See About Page for VACC contact information.");
            dialog.setPositiveButton("Close", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    act.finish();
                }
            });

        }
        else
        {
            dialog.setTitle("Timesheet");
            dialog.setMessage(message);
            dialog.setPositiveButton("Close", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
        }
        dialog.show();
    }




    /// <summary>
    /// Gets the time sheet data file URI.
    /// </summary>
    /// <returns>The time sheet data file URI.</returns>
    private Uri GetTimeSheetDataFileURI() {
     /*   File file = act.Application.GetFileStreamPath (tsCore.GetCSVFilename ());
        file.SetReadable (true, false);

        Uri contentUri = Uri.fromFile(file);
        return contentUri;*/
     return null;
    }



    //A helper function for searching UI objects recursively.
    //Pass in a list that will contain the children. Filters children by type specified.
    private void FindChildrenRecursive(ArrayList childrenFound, ViewGroup parent, String type, String matchingText)
    {
        if(parent.getChildCount() > 0)
        {
            for(int i = 0; i < parent.getChildCount(); i++)
            {
                View child = parent.getChildAt (i);

                //Check it is the child we want. I attempted to use tags for this, as that's what tags are intended for.
                //But it seems Xamarin returns a Java object from tags, not a C# object. Then I decided I couldn't be bothered figuring out
                //the finer details of java integration.

                boolean validChildFound = (false);

                //STUPID HACK. why does HACK bold itself. That's awesome.
                //Literally have to write individual conditions for the types passed in because of my tag failure.
                if(type.equals("Button"))
                {
                    //Check to see if this child is a button
                    if(child instanceof Button)
                    {
                        Button childAsButton = (Button) child;
                        if(childAsButton.getText().toString().equals(matchingText))
                        {
                            validChildFound = true;
                        }
                    }
                }
                else if(type.equals("ImageView"))
                {
                    if(child instanceof ImageView)
                    {
                        //Images have no text to check so pass in a null string and get every image.
                        validChildFound = true;
                    }
                }
                else if (type.equals("TextView"))
                {
                    if(child instanceof TextView)
                    {
                        validChildFound = true;
                    }
                }

                if(validChildFound) childrenFound.add(child);

                //Now the recursive part.
                if(child instanceof ViewGroup)
                {
                    FindChildrenRecursive(childrenFound, (ViewGroup) child, type, matchingText);
                }
            }
        }
    }

    //Find a button by text, ignoring visible
    Button FindVisibleButton(String text)
    {
        LinearLayout questionGroup = (LinearLayout) view.findViewById(R.id.questionContainer);

        ArrayList<Button> nextButtons = new ArrayList ();

        FindChildrenRecursive(nextButtons, questionGroup, "Button", text);

        for(int i = 0; i < nextButtons.size(); i++) {
            Button button = nextButtons.get(i);
            if(button.getVisibility() == View.VISIBLE) {
                return button;
            }
        }
        return null;
    }

    //Helper function to assign all the info button dialog strings at once in one spot.
    private String GetInfoTextFromImageID(int ID)
    {
        if(R.id.startingWeekInfo == ID)
        {
            return "Set the starting date of the time sheet you are submitting.";
        }
        else if(R.id.hoursTypeInfo == ID)
        {
            return "Select the option that best describes the hours you logged this week.";
        }
        else if (R.id.workHoursInfo == ID)
        {
            return "Enter the number of hours you worked this week, not including overtime.";
        }
        else if (R.id.overtimeInfo == ID)
        {
            return "Enter the hours and minutes of overtime worked per day for the week.";
        }
        else if (R.id.TAFEHoursInfo == ID)
        {
            return "Enter the number of hours you attended your RTO for this week.";
        }
        else if (R.id.sickLeaveInfo == ID)
        {
            return "Enter the hours and minutes of sick leave you took this week (if applicable).";
        }
        else if (R.id.annualLeaveInfo == ID)
        {
            return "Enter the hours and minutes of annual leave you took this week (if applicable).";
        }
        else if (R.id.bereavementLeaveInfo == ID)
        {
            return "Enter the hours and minutes of bereavement leave you took this week (if applicable).";
        }
        else if (R.id.vaccTimeInfo == ID)
        {
            return "Enter the hours and minutes of VACC duties taken per day of the week.";
        }
        else if (R.id.notesInfo == ID)
        {
            return "Add any additional notes for this week's timesheet. You may use multiple lines.";
        }
        else if (R.id.summaryInfo == ID)
        {
            return "Verify that all the details are correct before you send your timesheet. You will be unable to send your timesheet if you do not meet the " + TimeSheetCore.HoursTarget + " hour quota, or if you profile is incomplete.";
        }
        else
        {
            return "";
        }
    }

    private void UpdateHoursRemaining()
    {
        String hours = "0";
        if(tsData.getTotalLoggedHours().compareTo(BigDecimal.ZERO) != 0) {
            hours = tsData.getTotalLoggedHours().setScale(2, BigDecimal.ROUND_HALF_EVEN).toString();
        }
        hoursRemainingText.setText((hours + " of " + String.valueOf(TimeSheetCore.HoursTarget)));
        if(tsData.getTimeAndAHalfHours().compareTo(BigDecimal.ZERO) > 0) {
            String txt = hoursRemainingText.getText().toString() ;
            hoursRemainingText.setText(txt += "  (" + tsData.getTimeAndAHalfHours().setScale(2, BigDecimal.ROUND_HALF_EVEN) + " Time and a Half)");
        }
        if(tsData.getDoubleTimeHours().compareTo(BigDecimal.ZERO) > 0) {
            String txt = hoursRemainingText.getText().toString() ;
            hoursRemainingText.setText(txt += "  (" + tsData.getDoubleTimeHours().setScale(2, BigDecimal.ROUND_HALF_EVEN) + " Double Time)");

        }

        if(tsData.getTotalLoggedHours().compareTo(TimeSheetCore.HoursTarget) > 0)
        {
            hoursRemainingText.setTextColor(Color.argb(255, 255, 255, 50));
        }
        else
        {
            hoursRemainingText.setTextColor(Color.WHITE);
        }

        if (currentQuestion != null) {
            HideBreadcrumbsFrom (currentQuestion.ID + 1);
        }
    }

    /// <summary>
    /// Updates the summary page at the end.
    /// </summary>
    private void UpdateSummary()
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        ((TextView) view.findViewById (R.id.startDateSummaryText)).setText(dateFormat.format(tsData.getStartDate().getTime()));
        ((TextView) view.findViewById (R.id.endDateSummaryText)).setText(dateFormat.format(tsData.getEndDate().getTime()));

        if(tsData.getWorkHours().compareTo(BigDecimal.ZERO) == 0)
        {
            //Hide the work related fields from the summary
            ((TableRow) view.findViewById (R.id.workHoursTableRow)).setVisibility(View.GONE);
            ((TableRow) view.findViewById (R.id.doubleTimeHoursTableRow)).setVisibility(View.GONE);
            ((TableRow) view.findViewById (R.id.timeandaHalfHoursTableRow)).setVisibility(View.GONE);
        }
        else //else we have work hours logged, so lets display them and update the double time / time and a half hours (overtime)
        {
            ((TextView) view.findViewById (R.id.workHoursSummaryText)).setText(tsData.getWorkHours().setScale(2, BigDecimal.ROUND_HALF_EVEN).toString());
            ((TableRow) view.findViewById (R.id.workHoursTableRow)).setVisibility(View.VISIBLE);

            UpdateSummaryField ((TextView) view.findViewById (R.id.doubleTimeHoursSummaryText), (TableRow) view.findViewById (R.id.doubleTimeHoursTableRow), tsData.getDoubleTimeHours().setScale(2, BigDecimal.ROUND_HALF_EVEN).toString());
            UpdateSummaryField ((TextView) view.findViewById (R.id.timeandaHalfSummaryText), (TableRow) view.findViewById (R.id.timeandaHalfHoursTableRow), tsData.getTimeAndAHalfHours().setScale(2, BigDecimal.ROUND_HALF_EVEN).toString());
        }

        UpdateSummaryField ((TextView) view.findViewById (R.id.tafeHoursSummaryText), (TableRow) view.findViewById (R.id.tafeHoursTableRow), tsData.getTAFEHours().setScale(2, BigDecimal.ROUND_HALF_EVEN).toString());
        UpdateSummaryField ((TextView) view.findViewById (R.id.sickLeaveHoursSummaryText), (TableRow) view.findViewById (R.id.sickLeaveHoursTableRow), tsData.getTotalSickLeaveHours().setScale(2, BigDecimal.ROUND_HALF_EVEN).toString());
        UpdateSummaryField ((TextView) view.findViewById (R.id.annualLeaveHoursSummaryText), (TableRow) view.findViewById (R.id.annualLeaveHoursTableRow), tsData.getTotalAnnualLeaveHours().setScale(2, BigDecimal.ROUND_HALF_EVEN).toString());
        UpdateSummaryField ((TextView) view.findViewById (R.id.bereavementLeaveSummaryText), (TableRow) view.findViewById (R.id.bereavementLeaveHoursTableRow),tsData.getTotalBereavementLeaveHours().setScale(2, BigDecimal.ROUND_HALF_EVEN).toString());
        UpdateSummaryField ((TextView) view.findViewById (R.id.vaccTimeSummaryText), (TableRow) view.findViewById (R.id.vaccTimeHoursTableRow), tsData.getTotalVACCTimeHours().setScale(2, BigDecimal.ROUND_HALF_EVEN).toString());

        UpdateSummaryField (notesSummaryText, (TableRow) view.findViewById (R.id.notesTableRow), tsData.getNotes());

        if(TextUtils.isEmpty(tsData.getNotes()))
        {
            notesSummaryText.setVisibility(View.GONE);
            notesSummaryLabel.setVisibility(View.GONE);
        }
        else
        {
            notesSummaryText.setVisibility(View.VISIBLE);
            notesSummaryLabel.setVisibility(View.VISIBLE);
        }
    }

    private void UpdateSummaryField(TextView field, TableRow table, String summary)
    {
        if(TextUtils.isEmpty(summary) ||
                summary.equals("0") ||
                summary.equals("$0.00") || summary.equals("0.00"))
        {
            table.setVisibility(View.GONE);
        }
        else
        {
            field.setText(summary);
            table.setVisibility(View.VISIBLE);
        }
    }

    private void OnNextMonday()
    {
        mondayDate.add(Calendar.DAY_OF_YEAR, 7);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd, MMMM yyyy");
        mondayDateText.setText(dateFormat.format(mondayDate.getTime()));

        tsData.setStartDate(mondayDate);
    }

    private void OnPreviousMonday()
    {
        mondayDate.add(Calendar.DAY_OF_YEAR, -7);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd, MMMM yyyy");
        mondayDateText.setText(dateFormat.format(mondayDate.getTime()));

        tsData.setStartDate(mondayDate);
    }

    private void OnWorkPlaceHoursTypeCheckboxUpdated()
    {
        if(workplaceHoursType.isChecked())
        {
            TAFEHoursType.setChecked(false);
            mixedHoursType.setChecked(false);

            tsData.setWorkHours(TimeSheetCore.HoursTarget);
            tsData.setTAFEHours(new BigDecimal("0"));
        }
        else
        {
            tsData.setWorkHours(new BigDecimal("0"));
            ResetOvertimeUI();
        }
        tsData.calculateTimeHalfAndDoubleTime();

        UpdateHoursRemaining();

        OnHourTypeCheckboxUpdated();
    }

    private void OnTAFEHoursTypeCheckboxUpdated()
    {
        if(TAFEHoursType.isChecked())
        {

            workplaceHoursType.setChecked(false);
            mixedHoursType.setChecked(false);

            tsData.setTAFEHours(TimeSheetCore.HoursTarget);
            tsData.setWorkHours(new BigDecimal("0"));

        }
        else
        {
            tsData.setTAFEHours(new BigDecimal("0"));
            UpdateHoursRemaining();
        }

        tsData.calculateTimeHalfAndDoubleTime();

        UpdateHoursRemaining ();

        OnHourTypeCheckboxUpdated();
    }

    private void OnMixedHoursCheckboxUpdated()
    {
        if(mixedHoursType.isChecked())
        {
            workplaceHoursType.setChecked(false);
            TAFEHoursType.setChecked(false);
        }

        OnHourTypeCheckboxUpdated();

    }

    private void OnHourTypeCheckboxUpdated()
    {
        //UM what does this do??
        //[LD] - fix me!
      
        //CheckBox workPlaceHours = FindViewById<CheckBox>(R.id.workPlaceHoursType);
        //CheckBox TAFEHours = FindViewById<CheckBox>(R.id.TAFEHoursType);
        //CheckBox mixedHours = FindViewById<CheckBox>(R.id.mixedHoursType);
    }

    private void OnWorkHoursUpdated()
    {
        BigDecimal days, hours, minutes;

        try {
            hours = new BigDecimal(workHoursText.getText().toString());
        } catch (NumberFormatException e) {
            hours = new BigDecimal("0");
        }
        try {
            minutes = new BigDecimal(workMinutesText.getText().toString());
        } catch (NumberFormatException e) {
            minutes = new BigDecimal("0");
        }

        if(!TextUtils.isEmpty(workHoursText.getText().toString()) || !TextUtils.isEmpty(workMinutesText.getText().toString()))
        {
            days = DateTimeHelper.GetDaysFromHoursMinutes (hours, minutes);
            workDaysText.setHint(days.setScale(2, BigDecimal.ROUND_HALF_EVEN).toString().replace(".00", ""));
            workDaysText.setText(null);
        }
        else
        {
            days = new BigDecimal("0");
            workDaysText.setHint(days.toString());
        }
        //Calculate the hours from the days/hours/minutes
        tsData.setWorkHours(DateTimeHelper.CalculateHours(days));

        if(tsData.getWorkHours().compareTo(TimeSheetCore.HoursTarget) >= 0)
        {
            tsData.setTAFEHours(new BigDecimal("0"));
        }

        tsData.calculateTimeHalfAndDoubleTime();

        UpdateHoursRemaining ();
    }

    private void OnWorkDaysUpdated()
    {
        BigDecimal days, hours, minutes;
        //Parse the text fields, or fail to parse them (cuz they're empty or full of not-numbers) and call them zero.
        try {
            days = new BigDecimal(workDaysText.getText().toString());
        } catch (NumberFormatException e) {
            days = new BigDecimal("0");
        }


        if(!TextUtils.isEmpty(workDaysText.getText().toString()))
        {
            String[] hoursMinutes = DateTimeHelper.GetHoursMinutesFromDays(days).split("---");
            hours = new BigDecimal(hoursMinutes[0]).setScale(0, BigDecimal.ROUND_HALF_EVEN);
            minutes = new BigDecimal(hoursMinutes[1]).setScale(0, BigDecimal.ROUND_HALF_EVEN);
            Log.v("sak", hours + " " + new BigDecimal(hoursMinutes[0]).setScale(0, BigDecimal.ROUND_HALF_EVEN).toString());
            Log.v("sak", minutes + " " + new BigDecimal(hoursMinutes[1]).setScale(0, BigDecimal.ROUND_HALF_EVEN).toString());
            workHoursText.setHint(hours.toString());
            workMinutesText.setHint(minutes.toString());
            workHoursText.setText(null);
            workMinutesText.setText(null);
        }
        else
        {
            hours = new BigDecimal("0");
            minutes = new BigDecimal("0");
            workHoursText.setHint(hours.toString());
            workMinutesText.setHint(minutes.toString());
        }

        //Calculate the hours from the days/hours/minutes
        tsData.setWorkHours(DateTimeHelper.CalculateHours(days));

        if(tsData.getWorkHours().compareTo(TimeSheetCore.HoursTarget) >= 0)
        {
            tsData.setTAFEHours(new BigDecimal("0"));
        }


        tsData.calculateTimeHalfAndDoubleTime ();

        UpdateHoursRemaining ();
    }

    private void OnTAFEHoursUpdated()
    {

        BigDecimal days, hours, minutes;

        try {
            hours = new BigDecimal(tafeHoursText.getText().toString());
        } catch (NumberFormatException e) {
            hours = new BigDecimal("0");
        }
        try {
            minutes = new BigDecimal(tafeMinutesText.getText().toString());
        } catch (NumberFormatException e) {
            minutes = new BigDecimal("0");
        }

        if(!TextUtils.isEmpty(tafeHoursText.getText().toString()) || !TextUtils.isEmpty(tafeMinutesText.getText().toString()))
        {
            days = DateTimeHelper.GetDaysFromHoursMinutes (hours, minutes);
            tafeDaysText.setHint(days.setScale(2, BigDecimal.ROUND_HALF_EVEN).toString().replace(".00", ""));
            tafeDaysText.setText(null);
        }
        else
        {
            days = new BigDecimal("0");
            tafeDaysText.setHint(days.toString());
        }

        //Calculate the hours from the days/hours/minutes
        tsData.setTAFEHours(DateTimeHelper.CalculateHours(days));

        UpdateHoursRemaining ();

    }

    private void OnTAFEDaysUpdated()
    {

        BigDecimal days, hours, minutes;
        //Parse the text fields, or fail to parse them (cuz they're empty or full of not-numbers) and call them zero.
        try {
            days = new BigDecimal(tafeDaysText.getText().toString());
        } catch (NumberFormatException e) {
            days = new BigDecimal("0");
        }


        if(!TextUtils.isEmpty(tafeDaysText.getText().toString()))
        {
            String[] hoursMinutes = DateTimeHelper.GetHoursMinutesFromDays(days).split("---");
            hours = new BigDecimal(hoursMinutes[0]).setScale(0, BigDecimal.ROUND_HALF_EVEN);
            minutes = new BigDecimal(hoursMinutes[1]).setScale(0, BigDecimal.ROUND_HALF_EVEN);
            tafeHoursText.setHint(hours.toString());
            tafeMinutesText.setHint(minutes.toString());
            tafeHoursText.setText(null);
            tafeMinutesText.setText(null);
        }
        else
        {
            hours = new BigDecimal("0");
            minutes = new BigDecimal("0");
            tafeHoursText.setHint(hours.toString());
            tafeMinutesText.setHint(minutes.toString());
        }

        //Calculate the hours from the days/hours/minutes
        tsData.setTAFEHours(DateTimeHelper.CalculateHours(days));

        UpdateHoursRemaining ();
    }

    private void OnNotesUpdated()
    {
        EditText notesText = (EditText) view.findViewById (R.id.notesText);

        tsData.setNotes(notesText.getText().toString());
    }

    /// <summary>
    /// Gets the hours from UI elements.
    /// </summary>
    /// <returns>The hours from UI elements.</returns>
    /// <param name="minutesResourceId">Minutes resource identifier.</param>
    /// <param name="hoursResourceId">Hours resource identifier.</param>
    /// <param name="day">The day to get the values for. eg: Monday</param>
    private BigDecimal GetHoursFromUI(int minutesResourceId, int hoursResourceId, String day) {
        EditText minutesEditText = (EditText) view.findViewById (minutesResourceId);
        EditText hoursEditText = (EditText) view.findViewById (hoursResourceId);

        BigDecimal minutes, hours;

        try {
            minutes = new BigDecimal(minutesEditText.getText().toString());
        } catch (NumberFormatException e) {
            minutes = new BigDecimal("0");
        }
        try {
            hours = new BigDecimal(hoursEditText.getText().toString());
        } catch (NumberFormatException e) {
            hours = new BigDecimal("0");
        }


        return DateTimeHelper.GetHoursFromHoursMinutes(hours, minutes);
    }

    private void onCheckboxUpdate(int layoutId, CheckBox cb, int type) {
        LinearLayout layout = (LinearLayout) view.findViewById(layoutId);
        if(cb.isChecked())
        {
            layout.setVisibility(View.VISIBLE);
        }
        else
        {
            //type 1 = overtime  2 = sick leave  3 = annual leave  4 = bereavement leave  5 = vacc time
            switch (type) {
                case 1:
                    ResetOvertimeUI();
                    break;
                case 2:
                    ResetSickLeaveUI();
                    break;
                case 3:
                    ResetAnnualLeaveUI();
                    break;
                case 4:
                    ResetBereavementLeaveUI();
                    break;
                case 5:
                    ResetVACCTimeUI();
                    break;
            }
            layout.setVisibility(View.GONE);
        }
    }

    public void onPause()
    {
        super.onPause();
        for(int i = 0; i < userInputs.size(); i++) {
            UserInputField userInput = userInputs.get(i);
            userInput.SaveData();
        }
    }

    //And when it resumes we reload all our timesheet info from the core data container.
    public void onResume()
    {
        super.onResume ();
        for(int i = 0; i < userInputs.size(); i++) {
            UserInputField userInput = userInputs.get(i);
            userInput.LoadData();
        }
        ClearApprovalMode();

        if(mailingTimeSheet)
        {
           /* AlertDialog.Builder dialog = new AlertDialog.Builder(act);
            dialog.setTitle("Timesheet Complete");
            dialog.setMessage("Timesheet sent. The application will now close. See About Page for VACC contact information.");
            dialog.setPositiveButton("Close", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    act.finish();     
                }
            });
         
            dialog.show();*/

            mailingTimeSheet = (false);
        }
        else
        {
          CheckAppClear();
        }
    }

    public void AppClear()
    {
        if(TimeSheetCore.resumedApp)
        {
            TimeSheetCore.resumedApp = (false);
            ResetTimesheet();

        }
    }

    public void CheckAppClear()
    {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
               AppClear();
            }
        }, 5);
    }

    public void ClearApprovalMode()
    {
        employerApprovalMode = (false);

        ((TextView) view.findViewById(R.id.employerWarning)).setVisibility(View.GONE);

        ((LinearLayout) view.findViewById(R.id.employerApprovalContainer)).setVisibility(View.GONE);

        //highlight the summary
        ArrayList<TextView> textFields = new ArrayList ();

        LinearLayout summaryParent = (LinearLayout) view.findViewById(R.id.summaryTableLayoutContainer);

        FindChildrenRecursive(textFields, (ViewGroup) summaryParent, "TextView", null);

        for(int i = 0;i < textFields.size(); i++) {
            TextView txt = textFields.get(i);
            txt.setTextColor(Color.argb(255, 0, 0, 0));
        }

    }

    public void ResetTimesheet()
    {
        CloseAllQuestions ();
        OpenQuestion (0, true);
        workplaceHoursType.setChecked(false);

        TAFEHoursType.setChecked(false);

        mixedHoursType.setChecked(false);

        workDaysText.setText("");

        workHoursText.setText("");

        workMinutesText.setText("");

        OnWorkHoursUpdated();

        Log.v("sak","resetTimeSheet");
        overtimeCheckBox.setChecked(false);
        onCheckboxUpdate(R.id.overTimeLayout, overtimeCheckBox, 1);

        sickLeaveCheckBox.setChecked(false);
        onCheckboxUpdate(R.id.sickLeaveLayout, sickLeaveCheckBox, 2);

        annualLeaveCheckBox.setChecked(false);
        onCheckboxUpdate(R.id.annualLeaveLayout, annualLeaveCheckBox, 3);

        bereavementLeaveCheckBox.setChecked(false);
        onCheckboxUpdate(R.id.bereavementLeaveLayout, bereavementLeaveCheckBox, 4);

        vaccTimeCheckBox.setChecked(false);
        onCheckboxUpdate(R.id.vaccTimeLayout, vaccTimeCheckBox, 5);

        ResetOvertimeUI ();

        ResetVACCTimeUI ();

        ResetSickLeaveUI ();

        ResetAnnualLeaveUI ();

        ResetBereavementLeaveUI ();

        tafeDaysText.setText("");
        OnTAFEDaysUpdated();

        tafeHoursText.setText("");
        tafeMinutesText.setText("");

        ((EditText) view.findViewById(R.id.notesText)).setText("");
        ((EditText) view.findViewById(R.id.employerCodeText)).setText("");

        tsData.resetData();

        //now, set the start date accordingly in the data
        //just in case user does not change it manually
        mondayDate = DateTimeHelper.FindNearestMondayFromNow ();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd, MMMM yyyy");
        mondayDateText.setText(dateFormat.format(mondayDate.getTime()));
        tsData.setStartDate(mondayDate);

        OnNotesUpdated();

        UpdateSummary();

        ClearApprovalMode();
    }



    public void Reset()
    {
        AlertDialog.Builder dialog = new AlertDialog.Builder(act);
        dialog.setTitle("Timesheet");
        dialog.setMessage("Clear every field and restart the timesheet?");
        dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ResetTimesheet();
                dialog.cancel();
            }
        });
        dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        dialog.show();
    }
    

    /// <summary>
    /// Saves the time sheet data as CSV file.
    /// </summary>
    /// <remarks>Saves this locally for use by Email attachment.</remarks>
    private void SaveTimeSheetDataAsCSVFile() {
        File cachePath = new File(act.getCacheDir(), "csv");
        cachePath.mkdirs();
        FileOutputStream outputStream;

        try {
            outputStream = new FileOutputStream(cachePath + "/" + tsCore.GetCSVFilename());
            outputStream.write(tsCore.GetTimesheetAsCsv().getBytes());
            outputStream.flush();
            outputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

