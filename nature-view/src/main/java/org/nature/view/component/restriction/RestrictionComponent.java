package org.nature.view.component.restriction;

import java.util.Arrays;

import javax.el.ELContext;
import javax.el.ValueExpression;
import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIComponent;
import javax.persistence.TemporalType;

import org.nature.platform.persistence.query.jpql.LikeType;
import org.nature.platform.persistence.query.jpql.Restriction;
import org.nature.platform.persistence.query.jpql.RestrictionType;

/**
 * 这个类是一个java bean，有一些值用于限制创建。
 * @author hutianlong
 *
 */
public class RestrictionComponent {
	
	private UIComponent component;
    private ValueExpression addTo;
    private ValueExpression property;
    private ValueExpression type;
    private ValueExpression ilike;
    private ValueExpression likeType;
    private ValueExpression temporalType;
    private ValueExpression rendered;
    private ValueExpression castAs;
    
    public RestrictionComponent() {
    }

    public RestrictionComponent(UIComponent component, ValueExpression addTo, ValueExpression property, ValueExpression type) {
        this.component = component;
        this.addTo = addTo;
        this.property = property;
        this.type = type;
    }

    public RestrictionComponent(UIComponent component, ValueExpression addTo, ValueExpression property, ValueExpression type, ValueExpression ilike,
            ValueExpression likeType, ValueExpression temporalType, ValueExpression rendered, ValueExpression castAs) {
        this.component = component;
        this.addTo = addTo;
        this.property = property;
        this.type = type;
        this.ilike = ilike;
        this.likeType = likeType;
        this.temporalType = temporalType;
        this.rendered = rendered;
        this.castAs = castAs;
    }
    
    /**
     * 是否呈现
     * @param elContext
     * @return
     */
    public boolean isRendered(ELContext elContext) {
        Object renderedValue = (getRendered() != null) ? getRendered().getValue(elContext) : null;
        return (renderedValue == null) ? true : (Boolean.valueOf(renderedValue.toString()));
    }
    
    /**
     * 将RestrictionComponent的实例转换为查询条件
     *
     * @param elContext
     * @param component
     * @return
     */
    public Restriction toRestriction(ELContext elContext, UIComponent component) {

        Restriction restriction = new Restriction();
        RestrictionType restrictionType = null;
        //如果没有通知值，则使用 "eq"
        if (type != null) {
            String restrictionTypeString = (String) type.getValue(elContext);
            //如果通知类型，则验证类型
            if (restrictionTypeString != null) {
                restrictionType = RestrictionType.getByAcronym(restrictionTypeString);
            }
        } else {
            restrictionType = RestrictionType.EQUALS;
        }

        boolean ilikeValue = true;
        //如果没有通知值，则使用 "eq"
        if (ilike != null) {
            ilikeValue = Boolean.valueOf(ilike.toString());
        }

        LikeType likeTypeValue = null;
        //如果没有通知值，则使用 "both"
        if (likeType != null) {
            //  从enum获得
            likeTypeValue = LikeType.valueOf(likeType.getValue(elContext).toString().toUpperCase());
        } else {
            //默认值
            likeTypeValue = LikeType.BOTH;
        }

        TemporalType temporalTypeValue = null;
        //如果没有通知值，则使用 "null"
        if (temporalType != null) {
            //从enum获得
            temporalTypeValue = TemporalType.valueOf(temporalType.getValue(elContext).toString().toUpperCase());
        }

        String castAsValue = null;
        if (castAs != null) {
            castAsValue = (String) castAs.getValue(elContext);
        }

        Object value = ((EditableValueHolder) component).getValue();
        //Hibernate 不接受 Object[] (arrays), 因此将其转换为List
        if (RestrictionType.IN.equals(restrictionType) && value != null && value instanceof Object[]) {
            value = Arrays.asList((Object[]) value);
        }
        restriction.setValue(value);

        restriction.setRestrictionType(restrictionType);
        restriction.setProperty((String) property.getValue(elContext));
        restriction.setIlike(ilikeValue);
        restriction.setLikeType(likeTypeValue);
        restriction.setTemporalType(temporalTypeValue);
        restriction.setComponentId(component.getId());
        restriction.setCastAs(castAsValue);

        return restriction;
    }

	public UIComponent getComponent() {
		return component;
	}

	public void setComponent(UIComponent component) {
		this.component = component;
	}

	public ValueExpression getAddTo() {
		return addTo;
	}

	public void setAddTo(ValueExpression addTo) {
		this.addTo = addTo;
	}

	public ValueExpression getProperty() {
		return property;
	}

	public void setProperty(ValueExpression property) {
		this.property = property;
	}

	public ValueExpression getType() {
		return type;
	}

	public void setType(ValueExpression type) {
		this.type = type;
	}

	public ValueExpression getIlike() {
		return ilike;
	}

	public void setIlike(ValueExpression ilike) {
		this.ilike = ilike;
	}

	public ValueExpression getLikeType() {
		return likeType;
	}

	public void setLikeType(ValueExpression likeType) {
		this.likeType = likeType;
	}

	public ValueExpression getTemporalType() {
		return temporalType;
	}

	public void setTemporalType(ValueExpression temporalType) {
		this.temporalType = temporalType;
	}

	public ValueExpression getRendered() {
		return rendered;
	}

	public void setRendered(ValueExpression rendered) {
		this.rendered = rendered;
	}

	public ValueExpression getCastAs() {
		return castAs;
	}

	public void setCastAs(ValueExpression castAs) {
		this.castAs = castAs;
	}
    
    
    
    

}
