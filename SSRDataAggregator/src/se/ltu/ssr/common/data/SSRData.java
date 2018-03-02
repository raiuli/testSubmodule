package se.ltu.ssr.common.data;

import java.util.ArrayList;
import java.util.Date;

import se.ltu.ssr.adapter.interfaces.DataPacket;

public class SSRData implements DataPacket, Comparable{
	private String iDTags;
	private TypesOfData typesOfData;
	private String SourceID;
	private String SourceType;
	private SSRGeoLocation geolocation;
	private Date updateDateTime;
	private Date dataQueryDateTime;
	private String description;
	private ArrayList<Record> records = new ArrayList();
	
	
	public SSRData() {
		super();
	}
	
	
	
	
	public SSRData(String iDTags, TypesOfData typesOfData, String sourceID, String sourceType,
			SSRGeoLocation geolocation, Date updateDateTime, Date dataQueryDateTime, String description) {
		super();
		this.iDTags = iDTags;
		this.typesOfData = typesOfData;
		SourceID = sourceID;
		SourceType = sourceType;
		this.geolocation = geolocation;
		this.updateDateTime = updateDateTime;
		this.dataQueryDateTime = dataQueryDateTime;
		this.description = description;
	}




	public String getSourceID() {
		return SourceID;
	}

	public void setSourceID(String sourceID) {
		SourceID = sourceID;
	}

	public String getiDTags() {
		return iDTags;
	}
	public void setiDTags(String iDTags) {
		this.iDTags = iDTags;
	}
	public TypesOfData getTypesOfData() {
		return typesOfData;
	}
	public void setTypesOfData(TypesOfData typesOfData) {
		this.typesOfData = typesOfData;
	}
	public SSRGeoLocation getGeolocation() {
		return geolocation;
	}
	public void setGeolocation(SSRGeoLocation geolocation) {
		this.geolocation = geolocation;
	}
	public Date getUpdateDateTime() {
		return updateDateTime;
	}
	public void setUpdateDateTime(Date updateDateTime) {
		this.updateDateTime = updateDateTime;
	}
	public Date getDataQueryDateTime() {
		return dataQueryDateTime;
	}
	public void setDataQueryDateTime(Date dataQueryDateTime) {
		this.dataQueryDateTime = dataQueryDateTime;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public ArrayList<Record> getRecords() {
		return records;
	}
	public void setRecords(ArrayList<Record> records) {
		this.records = records;
	}




	public String getSourceType() {
		return SourceType;
	}




	public void setSourceType(String sourceType) {
		SourceType = sourceType;
	}




	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("SSRData [iDTags=");
		builder.append(iDTags);
		builder.append(", typesOfData=");
		builder.append(typesOfData);
		builder.append(", SourceID=");
		builder.append(SourceID);
		builder.append(", SourceType=");
		builder.append(SourceType);
		builder.append(", geolocation=");
		builder.append(geolocation);
		builder.append(", updateDateTime=");
		builder.append(updateDateTime);
		builder.append(", dataQueryDateTime=");
		builder.append(dataQueryDateTime);
		builder.append(", description=");
		builder.append(description);
		builder.append(", records=");
		builder.append(records);
		builder.append("]");
		return builder.toString();
	}




	@Override
	public int compareTo(Object o) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	
	
}
