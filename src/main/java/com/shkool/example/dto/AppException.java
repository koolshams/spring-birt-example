package com.shkool.example.dto;

/**
 * Main application exception
 * 
 * @author shamskool
 *
 */
public class AppException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor with message
	 * 
	 * @param message
	 */
	public AppException(String message) {
		super(message);
	}

	/**
	 * Constructor with message and an parent exception
	 * 
	 * @param message
	 * @param t
	 */
	public AppException(String message, Throwable t) {
		super(message, t);
	}

	/**
	 * AppException DTO
	 * 
	 * @author shamseer
	 */
	public static class AppExceptionDto {
		String error;
		Integer code;

		/**
		 * Constructor
		 * 
		 * @param error
		 * @param code
		 */
		public AppExceptionDto(String error, Integer code) {
			super();
			this.error = error;
			this.code = code;
		}

		public String getError() {
			return error;
		}

		public void setError(String error) {
			this.error = error;
		}

		public Integer getCode() {
			return code;
		}

		public void setCode(Integer code) {
			this.code = code;
		}

	}
}
