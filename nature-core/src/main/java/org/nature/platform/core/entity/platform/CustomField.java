package org.nature.platform.core.entity.platform;



import org.nature.platform.core.entity.SuperObject;

import javax.persistence.*;

/**
 * 自定义字段
 * @author Javen
 */
@Entity
@Table(name = "CUSTOM_FIELD")
public class CustomField extends SuperObject implements java.io.Serializable{

	private static final long serialVersionUID = 2239578978744111085L;

	@Column(name = "LABELNAME")
    private String labelName;
	
	@Column(name = "APINAME")
    private String apiName;
	
	@Column(name = "DESCRIPTION")
    private String Description;
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

    @OneToOne
    @JoinColumn(name = "CUSTOME_FIELDTYPE_ID")
    private CustomeFieldType customeFieldType;

    @ManyToOne
    @JoinColumn(name = "CUSTOME_OBJECT_ID")
    private CustomeObject customeObject;


    public String getApiName() {
        return apiName;
    }

    public void setApiName(String apiName) {
        this.apiName = apiName;
    }

    public CustomeFieldType getCustomeFieldType() {
        return customeFieldType;
    }

    public void setCustomeFieldType(CustomeFieldType customeFieldType) {
        this.customeFieldType = customeFieldType;
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

    public CustomeObject getCustomeObject() {
        return customeObject;
    }

    public void setCustomeObject(CustomeObject customeObject) {
        this.customeObject = customeObject;
    }


}
