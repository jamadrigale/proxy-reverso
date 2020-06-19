package ar.com.mercadolibre.model;

import java.util.Date;

public class ProxyLog {

	private String ip;
	
	private String path;
	
	private Date logTime;

	public ProxyLog(String ip, String path) {
		super();
		this.ip = ip;
		this.path = path;
		this.logTime = new Date();
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Date getLogTime() {
		return logTime;
	}

	public void setLogTime(Date logTime) {
		this.logTime = logTime;
	}
	
}
