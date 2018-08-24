package au.com.vacc.timesheets.fragment;

import android.text.TextUtils;
import android.widget.EditText;

class UserTextField extends UserInputField{
    public EditText textField = null;
    public UserTextField(TimeSheetFragment timeSheetFragment, EditText viewById, String notes) {
        textField = viewById;
        linkedValue = notes;

        timesheet = timeSheetFragment;
    }

    @Override
    public void SaveData()
    {
        super.SaveData();

        linkedValue = textField.getText().toString();
    }

    @Override
    public void LoadData()
    {
        super.LoadData();

        if(TextUtils.isEmpty(linkedValue))
        {
            return;
        }

        textField.setText(linkedValue);

    }
}
