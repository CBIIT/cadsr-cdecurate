package gov.nih.nci.cadsr.domain;

import java.sql.Date;

public class PermissibleValues {

//    private String id;
    private String value;
    private String shortMeaning;		//legacy; not used anymore
    private String meaningDescription;	//legacy; not used anymore
    private Date beginDate;
    private Date endDate;
    private Long highValueNumber;			//legacy; not used anymore
    private Long lowValueNumber;			//legacy; not used anymore
    private Date dateCreated;
    private String createdBy;
    private Date dateModified;
    private String modifiedBy;
    private ValueMeanings valueMeaning;
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getShortMeaning() {
		return shortMeaning;
	}
	public void setShortMeaning(String shortMeaning) {
		this.shortMeaning = shortMeaning;
	}
	public String getMeaningDescription() {
		return meaningDescription;
	}
	public void setMeaningDescription(String meaningDescription) {
		this.meaningDescription = meaningDescription;
	}
	public Date getBeginDate() {
		return beginDate;
	}
	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public Long getHighValueNumber() {
		return highValueNumber;
	}
	public void setHighValueNumber(Long highValueNumber) {
		this.highValueNumber = highValueNumber;
	}
	public Long getLowValueNumber() {
		return lowValueNumber;
	}
	public void setLowValueNumber(Long lowValueNumber) {
		this.lowValueNumber = lowValueNumber;
	}
	public Date getDateCreated() {
		return dateCreated;
	}
	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public Date getDateModified() {
		return dateModified;
	}
	public void setDateModified(Date dateModified) {
		this.dateModified = dateModified;
	}
	public String getModifiedBy() {
		return modifiedBy;
	}
	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
	public ValueMeanings getValueMeaning() {
		return valueMeaning;
	}
	public void setValueMeaning(ValueMeanings valueMeaning) {
		this.valueMeaning = valueMeaning;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((beginDate == null) ? 0 : beginDate.hashCode());
		result = prime * result + ((createdBy == null) ? 0 : createdBy.hashCode());
		result = prime * result + ((dateCreated == null) ? 0 : dateCreated.hashCode());
		result = prime * result + ((dateModified == null) ? 0 : dateModified.hashCode());
		result = prime * result + ((endDate == null) ? 0 : endDate.hashCode());
		result = prime * result + ((highValueNumber == null) ? 0 : highValueNumber.hashCode());
		result = prime * result + ((lowValueNumber == null) ? 0 : lowValueNumber.hashCode());
		result = prime * result + ((meaningDescription == null) ? 0 : meaningDescription.hashCode());
		result = prime * result + ((modifiedBy == null) ? 0 : modifiedBy.hashCode());
		result = prime * result + ((shortMeaning == null) ? 0 : shortMeaning.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		result = prime * result + ((valueMeaning == null) ? 0 : valueMeaning.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PermissibleValues other = (PermissibleValues) obj;
		if (beginDate == null) {
			if (other.beginDate != null)
				return false;
		} else if (!beginDate.equals(other.beginDate))
			return false;
		if (createdBy == null) {
			if (other.createdBy != null)
				return false;
		} else if (!createdBy.equals(other.createdBy))
			return false;
		if (dateCreated == null) {
			if (other.dateCreated != null)
				return false;
		} else if (!dateCreated.equals(other.dateCreated))
			return false;
		if (dateModified == null) {
			if (other.dateModified != null)
				return false;
		} else if (!dateModified.equals(other.dateModified))
			return false;
		if (endDate == null) {
			if (other.endDate != null)
				return false;
		} else if (!endDate.equals(other.endDate))
			return false;
		if (highValueNumber == null) {
			if (other.highValueNumber != null)
				return false;
		} else if (!highValueNumber.equals(other.highValueNumber))
			return false;
		if (lowValueNumber == null) {
			if (other.lowValueNumber != null)
				return false;
		} else if (!lowValueNumber.equals(other.lowValueNumber))
			return false;
		if (meaningDescription == null) {
			if (other.meaningDescription != null)
				return false;
		} else if (!meaningDescription.equals(other.meaningDescription))
			return false;
		if (modifiedBy == null) {
			if (other.modifiedBy != null)
				return false;
		} else if (!modifiedBy.equals(other.modifiedBy))
			return false;
		if (shortMeaning == null) {
			if (other.shortMeaning != null)
				return false;
		} else if (!shortMeaning.equals(other.shortMeaning))
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		if (valueMeaning == null) {
			if (other.valueMeaning != null)
				return false;
		} else if (!valueMeaning.equals(other.valueMeaning))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "PermissibleValues [value=" + value + ", shortMeaning=" + shortMeaning + ", meaningDescription=" + meaningDescription + ", beginDate=" + beginDate + ", endDate=" + endDate + ", highValueNumber=" + highValueNumber + ", lowValueNumber=" + lowValueNumber + ", dateCreated=" + dateCreated + ", createdBy=" + createdBy + ", dateModified=" + dateModified + ", modifiedBy=" + modifiedBy + ", valueMeaning=" + valueMeaning + "]";
	}
    
    

}
