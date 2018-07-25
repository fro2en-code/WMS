package com.plat.common.beans;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class BillBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8392939474874733716L;

	private AttachData attachData;
	
	private Map<String, Object> dataHead;

	private List<Map<String, Object>> dataDetail;

	public Map<String, Object> getDataHead() {
		return dataHead;
	}

	public void setDataHead(Map<String, Object> dataHead) {
		this.dataHead = dataHead;
	}

	public AttachData getAttachData() {
		return attachData;
	}

	public void setAttachData(AttachData attachData) {
		this.attachData = attachData;
	}

	public List<Map<String, Object>> getDataDetail() {
		return dataDetail;
	}

	public void setDataDetail(List<Map<String, Object>> dataDetail) {
		this.dataDetail = dataDetail;
	}

	public static class AttachData implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = 6770613215904361731L;
		private String taskId;
		private String storageCode;
		private String nextStorageCode;
		private String billId;

		public String getTaskId() {
			return taskId;
		}

		public void setTaskId(String taskId) {
			this.taskId = taskId;
		}

		public String getStorageCode() {
			return storageCode;
		}

		public void setStorageCode(String storageCode) {
			this.storageCode = storageCode;
		}
		
		public String getNextStorageCode() {
			return nextStorageCode;
		}

		public void setNextStorageCode(String nextStorageCode) {
			this.nextStorageCode = nextStorageCode;
		}

		public String getBillId() {
			return billId;
		}

		public void setBillId(String billId) {
			this.billId = billId;
		}

	}

}
