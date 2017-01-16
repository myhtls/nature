package org.nature.platform.core.entity.platform;



import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.nature.platform.core.entity.SuperObject;

/**
 * 定制化－创建－应用程序
 * @author Javen
 */
@NamedQueries({
        @NamedQuery(name = "Application.findAll",query = "select app from Application app order by app.labelName desc"),
        @NamedQuery(name="Application.reApiName",query="select app from Application app where app.apiName=:apiName")
})
@Entity
@Table(name = "APPLICATION")
public class Application extends SuperObject implements java.io.Serializable{

	private static final long serialVersionUID = 1L;
	
	public final static String FINDALL = "Application.findAll";
    public final static String REAPINAME = "Application.reApiName";

    @Column( name = "LABELNAME" )
    private String labelName;
    
    @Column( name = "APINAME" )
    private String apiName;
    
    @Column( name = "DESCRIPTION" )
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


    @ManyToMany(mappedBy = "applications")
    private List<CustomeObject> customeObjects;

    @ManyToMany( mappedBy = "applications")
    private List<AppTab> tabs;

    public Application() {

    }

    public List<AppTab> getTabs() {
		return tabs;
	}

	public void setTabs(List<AppTab> tabs) {
		this.tabs = tabs;
	}

	public List<CustomeObject> getCustomeObjects() {
        return customeObjects;
    }

    public void setCustomeObjects(List<CustomeObject> customeObjects) {
        this.customeObjects = customeObjects;
    }

    public String getApiName() {
        return apiName;
    }

    public void setApiName(String apiName) {
        this.apiName = apiName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isStandard() {
        return isStandard;
    }

    public void setStandard(boolean standard) {
        isStandard = standard;
    }

    public boolean isDeployed() {
        return isDeployed;
    }

    public void setDeployed(boolean deployed) {
        isDeployed = deployed;
    }

    public String getLabelName() {
        return labelName;
    }

    public void setLabelName(String labelName) {
        this.labelName = labelName;
    }
}


