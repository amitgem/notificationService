package net.event;

import java.util.Date;

public class EventLog {
	private String logEntry;
	private Date logTime;

	public String getLogEntry() {
		return logEntry;
	}

	public void setLogEntry(String logEntry) {
		this.logEntry = logEntry;
	}

	public Date getLogTime() {
		return logTime;
	}

	public void setLogTime(Date logTime) {
		this.logTime = logTime;
	}
}
