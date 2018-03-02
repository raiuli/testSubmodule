package se.ltu.ssr.common.data;

public class Record {
	private String name;
	private String value;
	private String unit;
	private String description;
	
	
	
	public Record(String name, String value, String unit, String description) {
		super();
		this.name = name;
		this.value = value;
		this.unit = unit;
		this.description = description;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Record [name=");
		builder.append(name);
		builder.append(", value=");
		builder.append(value);
		builder.append(", unit=");
		builder.append(unit);
		builder.append(", description=");
		builder.append(description);
		builder.append("]");
		return builder.toString();
	}
	
	

}
