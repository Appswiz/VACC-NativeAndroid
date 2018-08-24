package au.com.vacc.timesheets.views;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;

import au.com.vacc.timesheets.R;

public class CustomProgressDialog extends ProgressDialog {

    private static CustomProgressDialog progressDialog = null;
    private Context mContext;
    private static int pendingTasks;

	public CustomProgressDialog(Context context, AttributeSet attrs) {
		super(context);
		this.mContext = context;
	}

	public CustomProgressDialog(Context context) {
		super(context);
		this.mContext = context;
	}

	public static void showProgressDialog(Context context) {
        pendingTasks++;

        if (context == null && progressDialog == null) return;

        if (progressDialog != null && progressDialog.isShowing()) return;

        if (context instanceof Activity && ((Activity) context).isFinishing()) return;

        if (context != null && progressDialog == null) {
            progressDialog = new CustomProgressDialog(context);
        }

        if (progressDialog == null) return;

        if (!progressDialog.isShowing()) {
            progressDialog.show();
        }
        progressDialog.setContent(R.layout.progress_dialog_layout);
        progressDialog.setCancelable(false);
    }

	public static void dismissProgressDialog() {
        
	    if (progressDialog == null) {
            return;
        }
	    pendingTasks--;

        if (pendingTasks == 0) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    public static void forceDismissProgressDialog() {

        if (progressDialog == null) {
            return;
        }

        pendingTasks = 0;
        progressDialog.dismiss();
        progressDialog = null;
    }

	public void setContent(int layoutId) {
		getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(layoutId);
	} 
}