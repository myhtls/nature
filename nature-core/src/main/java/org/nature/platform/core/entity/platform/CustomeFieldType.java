package org.nature.platform.core.entity.platform;



import org.nature.platform.core.entity.SuperObject;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 *
 * @author Javen
 */
@Entity
@Table(name = "CUSTOME_FIELD_TYPE")
public class CustomeFieldType extends SuperObject implements java.io.Serializable{

	private static final long serialVersionUID = 3707096342939360601L;

	@Column(name = "APINAME")
    private String apiName;
	
	@Column(name = "DESCRIPTION")
    private String Description;
	
    /**
     * 1：是  0:否
     */
	@Column(name = "ISDEPLOYED")
    private boolean isDeployed;

    @OneToOne( mappedBy= "customeFieldType" )
    private CustomField customField;

    public String getApiName() {
        return apiName;
    }

    public void setApiName(String apiName) {
        this.apiName = apiName;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public boolean getIsDeployed() {
        return isDeployed;
    }

    public void setIsDeployed(boolean isDeployed) {
        this.isDeployed = isDeployed;
    }

    public CustomField getCustomField() {
        return customField;
    }

    public void setCustomField(CustomField customField) {
        this.customField = customField;
    }
}
