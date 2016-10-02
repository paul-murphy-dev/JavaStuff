package com.ibm.domain.pm;

import java.sql.Date;
import org.joda.time.DateTime;

public class TestItem {
	
	private int pkField;
	private Date dateField;
	private String stringField;
	private float floatField;
	private float decimalField;
	private DateTime dateTimeField;
	
	public int getPkField() {
		return pkField;
	}
	public void setPkField(int pkField) {
		this.pkField = pkField;
	}
	public Date getDateField() {
		return dateField;
	}
	public void setDateField(Date dateField) {
		this.dateField = dateField;
	}
	public String getStringField() {
		return stringField;
	}
	public void setStringField(String stringField) {
		this.stringField = stringField;
	}
	public float getFloatField() {
		return floatField;
	}
	public void setFloatField(float floatField) {
		this.floatField = floatField;
	}
	public float getDecimalField() {
		return decimalField;
	}
	public void setDecimalField(float decimalField) {
		this.decimalField = decimalField;
	}
	public DateTime getDateTimeField() {
		return dateTimeField;
	}
	public void setDateTimeField(DateTime dateTimeField) {
		this.dateTimeField = dateTimeField;
	}

}
