package molab.util;

import com.google.gson.Gson;

import molab.component.Data;
import molab.component.Resp;

public class RespUtil {

	private static String resp(Data data, Integer errcode, String errmsg) {
		Resp resp = new Resp();
		if (data != null) {
			resp.setData(data);
		}
		resp.setErrcode(errcode);
		resp.setErrmsg(errmsg);
		return new Gson().toJson(resp);
	}

	public static String successResp(Data data) {
		return resp(data, Status.Err.SUCCESS.getCode(), Status.Err.SUCCESS.getMsg());
	}

	public static String errorResp(Integer errcode, String errmsg) {
		Data data = new Data();
		return resp(data, errcode, errmsg);
	}

}
