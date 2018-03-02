package se.ltu.ssr.common.data;

public enum TypesOfData {
	TOURIST_DATA("Tourist Data"),
	HISTORICAL_DATA("Historical Data");
	private String typesOfData;

	
	private TypesOfData(String typesOfData) {
		this.typesOfData = typesOfData;
	}

	public String getTypesOfData() {
		return typesOfData;
	}

	
	
}
