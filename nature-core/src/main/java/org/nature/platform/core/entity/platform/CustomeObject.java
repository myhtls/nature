package org.nature.platform.core.entity.platform;



import org.nature.platform.core.entity.SuperObject;

import javax.persistence.*;
import java.util.List;

/**
 * 定制化－创建－自定义对象
 * @author Javen
 */
@Entity
@Table(name = "CUSTOME_OBJECT")
public class CustomeObject extends SuperObject implements java.io.Serializable{

	private static final long serialVersionUID = -1497092450693659629L;

	@Column(name = "LABELNAME")
    private String labelName;
	
	@Column(name = "APINAME")
    private String apiName;
	
	@Column(name = "DESCRIPTION")
    private String description;
	
    /**
     * 1:是  0:否
     */
	@Column(name = "ISSTANDARD")
    private boolean isStandard;

    /**
     * 1：是  0:否
     */
	@Column(name = "ISDEPLOYED")
    private boolean isDeployed;

    @JoinTable(name = "APPLICATION_OBJECES",joinColumns = @JoinColumn(name = "APPLICATION_ID"),inverseJoinColumns = @JoinColumn(name = "CUSTOME_OBJECT_ID"))
    @ManyToMany
    private List<Application> applications;

    
    @OneToMany(mappedBy = "customeObject" )
    private List<CustomField> customFields;


    public String getApiName() {
        return apiName;
    }

    public void setApiName(String apiName) {
        this.apiName = apiName;
    }

    public List<Application> getApplications() {
        return applications;
    }

    public void setApplications(List<Application> applications) {
        this.applications = applications;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean getIsDeployed() {
        return isDeployed;
    }

    public void setIsDeployed(boolean isDeployed) {
        this.isDeployed = isDeployed;
    }

    public boolean getIsStandard() {
        return isStandard;
    }

    public void setIsStandard(boolean isStandard) {
        this.isStandard = isStandard;
    }

    public String getLabelName() {
        return labelName;
    }

    public void setLabelName(String labelName) {
        this.labelName = labelName;
    }

    public List<CustomField> getCustomFields() {
        return customFields;
    }

    public void setCustomFields(List<CustomField> customFields) {
        this.customFields = customFields;
    }


}
