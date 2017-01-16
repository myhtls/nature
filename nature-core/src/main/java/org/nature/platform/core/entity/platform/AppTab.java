package org.nature.platform.core.entity.platform;

import java.util.Collection;
import javax.persistence.*;
import org.nature.platform.core.entity.SuperObject;
import org.hibernate.annotations.BatchSize;

/**
 *定制化－创建－应用tab项
 * @author Javen
 */
@NamedQueries({
        @NamedQuery(name = "AppTab.findAll",query = "select tab from AppTab tab order by tab.labelName desc")
})
@Entity
@Table(name = "APP_TAB")
public class AppTab  extends SuperObject implements java.io.Serializable{

	private static final long serialVersionUID = 1L;

	public final static String FINDALL = "AppTab.findAll";

    @Column(name = "LABELNAME")
    private String labelName;
    
    @Column(name = "APINAME")
    private String apiName;
    
    @Column(name = "TAB_TYPE")
    private String tabType;

    @JoinColumn(name = "OBJECT_ID")
    @OneToOne(fetch = FetchType.LAZY)
    private CustomeObject customeObject;

    @JoinTable(name = "APPLICATION_TABS", joinColumns = @JoinColumn(name = "APPLICATION_ID"),inverseJoinColumns = @JoinColumn(name = "TAB_ID"))
    @ManyToMany
    @BatchSize( size=200 )
    private Collection<Application> applications;

    public String getApiName() {
        return apiName;
    }

    public void setApiName(String apiName) {
        this.apiName = apiName;
    }


    public CustomeObject getCustomeObject() {
        return customeObject;
    }

    public void setCustomeObject(CustomeObject customeObject) {
        this.customeObject = customeObject;
    }

    public String getLabelName() {
        return labelName;
    }

    public void setLabelName(String labelName) {
        this.labelName = labelName;
    }

    public String getTabType() {
        return tabType;
    }

    public void setTabType(String tabType) {
        this.tabType = tabType;
    }


	public Collection<Application> getApplications() {
		return applications;
	}

	public void setApplications(Collection<Application> applications) {
		this.applications = applications;
	}

	@Override
    public boolean equals(Object other) {
        return (other instanceof AppTab) && (id != null)
                ? id.equals(((AppTab) other).id)
                : (other == this);
    }

    @Override
    public int hashCode() {
        return (id != null)
                ? (this.getClass().hashCode() + id.hashCode())
                : super.hashCode();
    }

    @Override
    public String toString() {
        return String.valueOf(id);
    }
    
    
}
