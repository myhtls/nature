package org.nature.view.component.restriction;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.el.ELException;
import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.view.facelets.ComponentHandler;
import javax.faces.view.facelets.FaceletContext;
import javax.faces.view.facelets.FaceletException;
import javax.faces.view.facelets.TagAttribute;
import javax.faces.view.facelets.TagConfig;
import javax.faces.view.facelets.TagHandler;
import javax.persistence.TemporalType;

import org.nature.platform.persistence.query.jpql.LikeType;
import org.nature.platform.persistence.query.jpql.RestrictionType;

/**
 *  "t:restriction"
 * @author hutianlong
 *
 */
public class RestrictionTagHandler extends TagHandler {

	private final TagAttribute addTo;
	private final TagAttribute property;
	private final TagAttribute type;
	private final TagAttribute ilike;
	private final TagAttribute likeType;
	private final TagAttribute temporalType;
	private final TagAttribute rendered;
	private final TagAttribute castAs;

	public RestrictionTagHandler(TagConfig tagConfig) {
		super(tagConfig);
		this.addTo = getAttribute("addTo");
		this.property = getAttribute("property");
		this.type = getAttribute("type");
		this.ilike = getAttribute("ilike");
		this.likeType = getAttribute("likeType");
		this.temporalType = getAttribute("temporalType");
		this.rendered = getAttribute("rendered");
		this.castAs = getAttribute("castAs");
	}

	@SuppressWarnings("unchecked")
	@Override
	public void apply(FaceletContext faceletContext, UIComponent parent)
			throws IOException, FacesException, FaceletException, ELException {
		if (ComponentHandler.isNew(parent)) {
			
			ValueExpression addToVE = null;
			ValueExpression propertyVE = null;
			ValueExpression typeVE = null;
			ValueExpression ilikeVE = null;
			ValueExpression likeTypeVE = null;
			ValueExpression temporalTypeVE = null;
			ValueExpression renderedVE = null;
			ValueExpression castAsVE = null;

			// get value expressions
			if (addTo != null) {
				addToVE = addTo.getValueExpression(faceletContext, Object.class);
			}

			if (castAs != null) {
				castAsVE = castAs.getValueExpression(faceletContext, Object.class);
			}

			if (property != null) {
				propertyVE = property.getValueExpression(faceletContext, String.class);
			}

			if (type != null) {
				typeVE = type.getValueExpression(faceletContext, String.class);
				// validate
				String typeString = type.getValue(faceletContext);
				// if a type is informed, then validate the type
				if (typeString != null && !typeString.trim().isEmpty()) {
					RestrictionType restrictionType = RestrictionType.getByAcronym(typeString);
					if (restrictionType == null) {
						throw new FacesException("Restriction type \"" + typeString
								+ "\" not found. The supported types are: " + RestrictionType.getAcronymList());
					}

				}
			}

			if (ilike != null) {
				ilikeVE = ilike.getValueExpression(faceletContext, Boolean.class);
			}

			if (likeType != null) {
				likeTypeVE = likeType.getValueExpression(faceletContext, String.class);

				// 验证
				String typeString = likeType.getValue(faceletContext);
				//
				if (typeString != null) {
					boolean found = false;
					for (LikeType lt : LikeType.values()) {
						if (lt.name().toLowerCase().equals(typeString)) {
							found = true;
						}
					}
					if (found == false) {
						throw new FacesException("Like type \"" + typeString + "\" not found. The supported types are: "
								+ LikeType.getNameList());
					}

				}
			}

			if (temporalType != null) {
				temporalTypeVE = temporalType.getValueExpression(faceletContext, String.class);

				// 验证
				String typeString = temporalType.getValue(faceletContext);
				//
				if (typeString != null) {
					boolean found = false;
					for (TemporalType tt : TemporalType.values()) {
						if (tt.name().toLowerCase().equals(typeString)) {
							found = true;
						}
					}
					if (found == false) {
						throw new FacesException("Restriction type \"" + typeString
								+ "\" not found. The supported types are: time, timestamp, date");
					}

				}
			}

			if (rendered != null) {
				renderedVE = rendered.getValueExpression(faceletContext, Boolean.class);
			}

			// 创建一个组件
			RestrictionComponent restrictionComponent = new RestrictionComponent(parent, addToVE, propertyVE, typeVE,
					ilikeVE, likeTypeVE, temporalTypeVE, renderedVE, castAsVE);

			Map<String, Object> requestMap = faceletContext.getFacesContext().getExternalContext().getRequestMap();
			List<RestrictionComponent> currentRestrictions = (List<RestrictionComponent>) requestMap
					.get(RestrictionCollector.RESTRICTIONS);
			if (currentRestrictions == null) {
				currentRestrictions = new ArrayList<>();
			}
			currentRestrictions.add(restrictionComponent);
			// 添加一个 requestmap
			requestMap.put(RestrictionCollector.RESTRICTIONS, currentRestrictions);

		}
	}

}
