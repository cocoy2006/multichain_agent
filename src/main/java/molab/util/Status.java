package molab.util;

public class Status {
	
	public static enum Common {
		FAILURE(0), SUCCESS(1), CLOSE(0), OPEN(1);
		private int value;

		private Common(int value) {
			this.value = value;
		}

		public int getInt() {
			return value;
		}
	}
	
	public static enum Err {
		SYSTEM_ERROR(-1, "system_error"), 
		SUCCESS(0, "success"),
		ILLEGAL_APPSECRET(900, "illegal_appsecret"),
		ILLEGAL_TOKEN(901, "illegal_token"),
		ILLEGAL_PARAMETER(902, "illegal_parameter"),
		ILLEGAL_ADDRESS(903, "illegal_address"),
		ILLEGAL_DATE(904, "illegal_date");
		
		private int code;
		private String msg;

		private Err(int code, String msg) {
			this.code = code;
			this.msg = msg;
		}

		public int getCode() {
			return code;
		}
		
		public String getMsg() {
			return msg;
		}
	}
	
}
