package au.com.vacc.timesheets.utils;

import org.codehaus.jackson.JsonNode;


public interface CustomJsonCallBack {
	void onError(String error);
	void getResult(String jsonNode);
}
