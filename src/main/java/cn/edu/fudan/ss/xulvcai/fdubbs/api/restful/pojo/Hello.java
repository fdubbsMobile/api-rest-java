package cn.edu.fudan.ss.xulvcai.fdubbs.api.restful.pojo;




public class Hello {

	private String message;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	public boolean equals(Hello hello) {
		return this.message.equals(hello.getMessage());
	}
	
}
