package gov.nih.nci.cadsr.domain;

import java.util.Date;

public class ValueMeanings {

//  private String id;
    private String shortMeaning;
    private String description;
    private String comments;
    private Date beginDate;
    private Date endDate;
    private Date dateCreated;
    private String createdBy;
    private Date dateModified;
    private String modifiedBy;
    private String cdrId;
    private String preferredName;
    private String preferredDefinition;
    private String longName;
    private String contextId;
    private String workflowStatus;
    private String version;
    private String publicId;
    private boolean latestVersion;
    private boolean deleted;
    private String origin;
    private String changeNote;
    private String definitionSource;
	public String getShortMeaning() {
		return shortMeaning;
	}
	public void setShortMeaning(String shortMeaning) {
		this.shortMeaning = shortMeaning;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
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
	public String getCdrId() {
		return cdrId;
	}
	public void setCdrId(String cdrId) {
		this.cdrId = cdrId;
	}
	public String getPreferredName() {
		return preferredName;
	}
	public void setPreferredName(String preferredName) {
		this.preferredName = preferredName;
	}
	public String getPreferredDefinition() {
		return preferredDefinition;
	}
	public void setPreferredDefinition(String preferredDefinition) {
		this.preferredDefinition = preferredDefinition;
	}
	public String getLongName() {
		return longName;
	}
	public void setLongName(String longName) {
		this.longName = longName;
	}
	public String getContextId() {
		return contextId;
	}
	public void setContextId(String contextId) {
		this.contextId = contextId;
	}
	public String getWorkflowStatus() {
		return workflowStatus;
	}
	public void setWorkflowStatus(String workflowStatus) {
		this.workflowStatus = workflowStatus;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getPublicId() {
		return publicId;
	}
	public void setPublicId(String publicId) {
		this.publicId = publicId;
	}
	public boolean isLatestVersion() {
		return latestVersion;
	}
	public void setLatestVersion(boolean latestVersion) {
		this.latestVersion = latestVersion;
	}
	public boolean isDeleted() {
		return deleted;
	}
	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}
	public String getOrigin() {
		return origin;
	}
	public void setOrigin(String origin) {
		this.origin = origin;
	}
	public String getChangeNote() {
		return changeNote;
	}
	public void setChangeNote(String changeNote) {
		this.changeNote = changeNote;
	}
	public String getDefinitionSource() {
		return definitionSource;
	}
	public void setDefinitionSource(String definitionSource) {
		this.definitionSource = definitionSource;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((beginDate == null) ? 0 : beginDate.hashCode());
		result = prime * result + ((cdrId == null) ? 0 : cdrId.hashCode());
		result = prime * result + ((changeNote == null) ? 0 : changeNote.hashCode());
		result = prime * result + ((comments == null) ? 0 : comments.hashCode());
		result = prime * result + ((contextId == null) ? 0 : contextId.hashCode());
		result = prime * result + ((createdBy == null) ? 0 : createdBy.hashCode());
		result = prime * result + ((dateCreated == null) ? 0 : dateCreated.hashCode());
		result = prime * result + ((dateModified == null) ? 0 : dateModified.hashCode());
		result = prime * result + ((definitionSource == null) ? 0 : definitionSource.hashCode());
		result = prime * result + (deleted ? 1231 : 1237);
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((endDate == null) ? 0 : endDate.hashCode());
		result = prime * result + (latestVersion ? 1231 : 1237);
		result = prime * result + ((longName == null) ? 0 : longName.hashCode());
		result = prime * result + ((modifiedBy == null) ? 0 : modifiedBy.hashCode());
		result = prime * result + ((origin == null) ? 0 : origin.hashCode());
		result = prime * result + ((preferredDefinition == null) ? 0 : preferredDefinition.hashCode());
		result = prime * result + ((preferredName == null) ? 0 : preferredName.hashCode());
		result = prime * result + ((publicId == null) ? 0 : publicId.hashCode());
		result = prime * result + ((shortMeaning == null) ? 0 : shortMeaning.hashCode());
		result = prime * result + ((version == null) ? 0 : version.hashCode());
		result = prime * result + ((workflowStatus == null) ? 0 : workflowStatus.hashCode());
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
		ValueMeanings other = (ValueMeanings) obj;
		if (beginDate == null) {
			if (other.beginDate != null)
				return false;
		} else if (!beginDate.equals(other.beginDate))
			return false;
		if (cdrId == null) {
			if (other.cdrId != null)
				return false;
		} else if (!cdrId.equals(other.cdrId))
			return false;
		if (changeNote == null) {
			if (other.changeNote != null)
				return false;
		} else if (!changeNote.equals(other.changeNote))
			return false;
		if (comments == null) {
			if (other.comments != null)
				return false;
		} else if (!comments.equals(other.comments))
			return false;
		if (contextId == null) {
			if (other.contextId != null)
				return false;
		} else if (!contextId.equals(other.contextId))
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
		if (definitionSource == null) {
			if (other.definitionSource != null)
				return false;
		} else if (!definitionSource.equals(other.definitionSource))
			return false;
		if (deleted != other.deleted)
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (endDate == null) {
			if (other.endDate != null)
				return false;
		} else if (!endDate.equals(other.endDate))
			return false;
		if (latestVersion != other.latestVersion)
			return false;
		if (longName == null) {
			if (other.longName != null)
				return false;
		} else if (!longName.equals(other.longName))
			return false;
		if (modifiedBy == null) {
			if (other.modifiedBy != null)
				return false;
		} else if (!modifiedBy.equals(other.modifiedBy))
			return false;
		if (origin == null) {
			if (other.origin != null)
				return false;
		} else if (!origin.equals(other.origin))
			return false;
		if (preferredDefinition == null) {
			if (other.preferredDefinition != null)
				return false;
		} else if (!preferredDefinition.equals(other.preferredDefinition))
			return false;
		if (preferredName == null) {
			if (other.preferredName != null)
				return false;
		} else if (!preferredName.equals(other.preferredName))
			return false;
		if (publicId == null) {
			if (other.publicId != null)
				return false;
		} else if (!publicId.equals(other.publicId))
			return false;
		if (shortMeaning == null) {
			if (other.shortMeaning != null)
				return false;
		} else if (!shortMeaning.equals(other.shortMeaning))
			return false;
		if (version == null) {
			if (other.version != null)
				return false;
		} else if (!version.equals(other.version))
			return false;
		if (workflowStatus == null) {
			if (other.workflowStatus != null)
				return false;
		} else if (!workflowStatus.equals(other.workflowStatus))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "ValueMeanings [shortMeaning=" + shortMeaning + ", description=" + description + ", comments=" + comments + ", beginDate=" + beginDate + ", endDate=" + endDate + ", dateCreated=" + dateCreated + ", createdBy=" + createdBy + ", dateModified=" + dateModified + ", modifiedBy=" + modifiedBy + ", cdrId=" + cdrId + ", preferredName=" + preferredName + ", preferredDefinition=" + preferredDefinition + ", longName=" + longName + ", contextId=" + contextId + ", workflowStatus=" + workflowStatus + ", version=" + version + ", publicId=" + publicId + ", latestVersion=" + latestVersion + ", deleted=" + deleted + ", origin=" + origin + ", changeNote=" + changeNote + ", definitionSource=" + definitionSource + "]";
	}

    
}
