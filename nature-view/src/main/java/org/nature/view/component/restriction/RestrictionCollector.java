package org.nature.view.component.restriction;

import javax.el.ELContext;
import javax.el.ValueExpression;
import javax.faces.component.EditableValueHolder;
import javax.faces.component.StateHolder;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;

import org.nature.platform.persistence.query.jpql.Restriction;
import org.nature.platform.persistence.query.jpql.Restrictions;
import org.nature.platform.utils.BeansTool;
import org.nature.view.primefaces.model.LazyDataModelImpl;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 这个类是一个ActionListener，用于向List或LazyDataModel添加查询条件
 * 
 * @author hutianlong
 *
 */
public class RestrictionCollector implements ActionListener, StateHolder {

	private static final Logger logger = Logger.getLogger(RestrictionCollector.class.getName());
	public static final String IGNORE_RESTRICTIONS = RestrictionCollector.class.getName()
			+ "_ignoreCurrentRestrictions";
	public static final String RESTRICTIONS = RestrictionCollector.class.getName() + "_restrictions";

	private ValueExpression addTo;
	private ValueExpression debug;
	private boolean _transient;
	private List<Restriction> currentRestrictions;

	public RestrictionCollector() {
	}

	public RestrictionCollector(ValueExpression addTo) {
		this.addTo = addTo;
	}

	public RestrictionCollector(ValueExpression addTo, ValueExpression debug) {
		this.addTo = addTo;
		this.debug = debug;
	}

	/**
	 * 定义是否在当前请求中忽略限制
	 */
	public static void ignoreRestrictions() {
		Map<String, Object> requestMap = FacesContext.getCurrentInstance().getExternalContext().getRequestMap();
		requestMap.put(IGNORE_RESTRICTIONS, true);
	}

	/**
	 * 如果在当前请求中忽略限制，则返回true
	 *
	 * @return 如果在当前请求中忽略限制，则为true
	 */
	public static boolean isIgnoreRestrictions() {
		Map<String, Object> requestMap = FacesContext.getCurrentInstance().getExternalContext().getRequestMap();
		Object value = requestMap.get(IGNORE_RESTRICTIONS);
		return value != null && (Boolean) value == true;
	}

	/**
	 * 如果在当前请求中忽略限制，则返回true
	 *
	 * @param 当前的faces
	 *            context
	 * @return 如果在当前请求中忽略限制，则返回true
	 */
	public static boolean isIgnoreRestrictions(FacesContext context) {
		//获得上下文请求参数
		Map<String, Object> requestMap = context.getExternalContext().getRequestMap();
		//获得是否忽略请求限制
		Object value = requestMap.get(IGNORE_RESTRICTIONS);
		return value != null && (Boolean) value == true;
	}

	/**
	 * 处理请求
	 */
	@SuppressWarnings({  "unchecked" })
	@Override
	public void processAction(ActionEvent actionEvent) throws AbortProcessingException {
		FacesContext context = FacesContext.getCurrentInstance();
		//获得上下文，如果忽略请求请求将返回,不继续执行
		if (isIgnoreRestrictions(context)) {
			return;
		}
		//获得请求参数
		Map<String, Object> requestMap = context.getExternalContext().getRequestMap();
		//通过org.nature.view.component.restriction.RestrictionCollector_restrictions获得一个集合的RestrictionComponent标签组件
		List<RestrictionComponent> currentRequestRestrictions = (List<RestrictionComponent>) requestMap
				.get(RESTRICTIONS);
		
		if (currentRequestRestrictions != null) {
			int index = 0;
			currentRestrictions = null;
			for (RestrictionComponent restrictionComponent : currentRequestRestrictions) {
				//将查询标签组件加入此方法
				addRestriction(restrictionComponent.getComponent(), restrictionComponent, context, index);
				index++;
			}
			//如果当前查询条件不为空，将提行排序
			if (currentRestrictions != null) {
				Collections.sort(currentRestrictions);
			}
		}

	}
	
	 /**
     *返回请求映射中的当前查询列表。 如果没有找到查询条件，返回一个空列表。
     *
     * @return 请求映射中的限制列表
     */
    public static List<Restriction> getCurrentRestrictions() {
        return getCurrentRestrictions(FacesContext.getCurrentInstance());
    }
	
    /**
     *  返回请求映射中的当前查询列表。 如果没有找到查询条件，返回一个空列表。
     * @param context
     * @return
     */
	@SuppressWarnings({ "unchecked"})
	public static List<Restriction> getCurrentRestrictions(FacesContext context){
		//获得请求参数
		ELContext elContext = context.getELContext();
		Map<String,Object> requestMap = context.getExternalContext().getRequestMap();
		List<RestrictionComponent> currentRestrictions = (List<RestrictionComponent>)requestMap.get(RESTRICTIONS);
		Restrictions restrictions = new Restrictions();
		if(currentRestrictions != null){
			//遍历查询标签组件
			for(RestrictionComponent restrictionComponent : currentRestrictions){
				//将组件转换为一个查询条件类
				 Restriction restriction = restrictionComponent.toRestriction(elContext, restrictionComponent.getComponent());
				 //当标签允许呈现的时候将此条件加入
	                if (restrictionComponent.isRendered(elContext) && !BeansTool.isEmpty(restriction.getValue())) {
	                    restrictions.add(restriction);
	                }
			}
		}
		return restrictions;
		
	}
	
	/**
	 * 是否是执行组件
	 * @param context
	 * @param component
	 * @return
	 */
	public boolean isExecuteComponent(FacesContext context, UIComponent component) {
        boolean processComponent = false;
        if (context.getPartialViewContext().isAjaxRequest()) {
            Collection<String> executeIds = context.getPartialViewContext().getExecuteIds();
            if (executeIds != null) {
                for (String execute : executeIds) {
                    UIComponent root = context.getViewRoot().findComponent(execute);
                    if (root != null) {
                        if (isChild(root, component)) {
                            processComponent = true;
                        }
                    }
                }
            }

        } else {
            //非ajax只是验证是否在参数映射中
            processComponent = context.getExternalContext().getRequestParameterValuesMap()
            		.containsKey(component.getClientId());
        }
        return processComponent;
    }
	
	/**
	 * 是否是子组件
	 * @param base
	 * @param child
	 * @return
	 */
	 public boolean isChild(UIComponent base, UIComponent child) {

	        if (base.equals(child)) {
	            return true;
	        }

	        if (base.getFacetsAndChildren() != null) {
	            Iterator<UIComponent> children = base.getFacetsAndChildren();
	            while (children.hasNext()) {
	                UIComponent current = (UIComponent) children.next();
	                if (current.equals(child) || isChild(current, child)) {
	                    return true;
	                }
	            }
	        }

	        return false;
	    }

	 /**
	  * 添加查询条件
	  * @param component
	  * @param restrictionComponent
	  * @param context
	  * @param index
	  * @throws AbortProcessingException
	  */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void addRestriction(UIComponent component, RestrictionComponent restrictionComponent, FacesContext context,
			int index) throws AbortProcessingException {
		 //如果不是EditableValueHolder，请尝试添加子级
        if (!(component instanceof EditableValueHolder)) {
            //尝试在子组件中添加
            Iterator<UIComponent> children = component.getFacetsAndChildren();
            while (children.hasNext()) {
                UIComponent child = (UIComponent) children.next();
                // System.out.println(child);
                addRestriction(child, restrictionComponent, context, index);
            }
            return;
        }

        ELContext elContext = context.getELContext();

        Object debugValue = (debug != null) ? debug.getValue(elContext) : null;
        boolean checkDebug = (debugValue == null) ? false : (Boolean.valueOf(debugValue.toString()));

        if (!isExecuteComponent(context, component)) {
            //logger.log(Level.INFO, "Component: {0} is ignored from restrictions", new Object[]{component.getClientId()});
            return;
        }

        ValueExpression addTo = restrictionComponent.getAddTo();

        //use collector addTo if component is null
        if (addTo == null) {
            addTo = this.addTo;
        }

        if (addTo != null) {

            Object addToValue = addTo.getValue(elContext);

            List<Restriction> restrictions = null;
            if (addToValue instanceof LazyDataModelImpl) {
                LazyDataModelImpl dataModel = (LazyDataModelImpl) addToValue;
                dataModel.setDebug(checkDebug);
                //强行加载数据
                dataModel.setLoadData(true);
                restrictions = dataModel.getRestrictions();
                if (restrictions == null) {
                    restrictions = new Restrictions();
                    dataModel.setRestrictions(restrictions);
                }
            } else if (addToValue instanceof List) {
                restrictions = (List) addToValue;
            }

            Restriction restriction = restrictionComponent.toRestriction(elContext, component);
            restriction.setIndex(index);

            if (addToValue == null && checkDebug) {
                logger.log(Level.INFO, "Expression ''addTo'' is null. Restriction ''{0}'' of component ''{1}'' will not be added", new Object[]{restriction.getProperty(), component.getClientId()});
            }

            //尝试删除值
            removeValues(restriction, restrictions, component, checkDebug);
//            removeEmptyValues(restriction, restrictions, component, checkDebug);

            //如果新值不为空，则添加限制
            if (restrictionComponent.isRendered(elContext) == true && !BeansTool.isEmpty(restriction.getValue())) {
                if (restrictions != null) {
                    if (checkDebug) {
                        logger.log(Level.INFO, "Restriction added: {0}. Component: {1}", new Object[]{restriction, component.getClientId()});
                    }
                    //验证唯一
                    boolean found = false;
                    for (Restriction current : restrictions) {
                        if (isSameRestriction(restriction, current)) {
                            current.setValue(restriction.getValue());
                            current.setIndex(index);
                            found = true;
                            break;
                        }
                    }
                    //仅在未找到时添加
                    if (found == false) {
                        restrictions.add(restriction);
                    }

                    //本地设置
                    this.currentRestrictions = restrictions;

                }
            }

        }
	}
	
	/**
	 * 是否是相同的查询条件
	 * @param restriction
	 * @param other
	 * @return
	 */
	 public boolean isSameRestriction(Restriction restriction, Restriction other) {
	        //o novo metodo compara pelo id do componente, antes era verificado apenas o nome e o tipo da restricao
	        // return restriction.getProperty().equals(other.getProperty()) && restriction.getRestrictionType().equals(other.getRestrictionType());
	        return restriction.getComponentId() != null && restriction.getComponentId().equals(other.getComponentId());
	    }

	    public void removeValues(Restriction restriction, List<Restriction> restrictions, UIComponent component, boolean checkDebug) {
	        if (restrictions != null) {
	            Iterator<Restriction> itRestrictions = restrictions.iterator();
	            while (itRestrictions.hasNext()) {
	                Restriction current = itRestrictions.next();
	                if (isSameRestriction(restriction, current)) {
	                    if (checkDebug) {
	                        logger.log(Level.INFO, "Restriction removed: {0}. Component: {1}", new Object[]{current, component.getClientId()});
	                    }
	                    itRestrictions.remove();
	                    break;
	                }
	            }
	        }
	    }

	    public void removeEmptyValues(Restriction restriction, List<Restriction> restrictions, UIComponent component, boolean checkDebug) {
	        //如果新值为空，则删除限制
	        if (BeansTool.isEmpty(restriction.getValue()) && restrictions != null) {
	             Iterator<Restriction> itRestrictions = restrictions.iterator();
	            while (itRestrictions.hasNext()) {
	                Restriction current = itRestrictions.next();
	                if (current.getProperty().equals(restriction.getProperty()) && current.getRestrictionType().equals(restriction.getRestrictionType())) {
	                    if (checkDebug) {
	                        logger.log(Level.INFO, "Restriction removed: {0}. Component: {1}", new Object[]{current, component.getClientId()});
	                    }
	                    itRestrictions.remove();
	                    break;
	                }
	            }
	        }
	    }


	/**
	 * 这个方法取一个FacesContext 和 Object实例，这个Object实例表示包含组件状态的数组。restoreState方法将组件的属性设置为Ojbect数组中保存的值.
	 */
	@Override
	public void restoreState(FacesContext arg0, Object state) {
		 Object[] values = (Object[]) state;
	        addTo = (ValueExpression) values[0];
	        debug = (ValueExpression) values[1];

	}

	/**
	 * 要保存一组值,可以实现该方法.这个方法在呈现响应阶段调用,这个阶段需要为后续请求的处理保存响应的状态.
	 */
	@Override
	public Object saveState(FacesContext arg0) {
		Object[] state = new Object[2];
        state[0] = addTo;
        state[1] = debug;

        return state;
	}


    public boolean isTransient() {
        return _transient;
    }

    public void setTransient(boolean _transient) {
        this._transient = _transient;
    }

    public ValueExpression getAddTo() {
        return addTo;
    }

    public void setAddTo(ValueExpression addTo) {
        this.addTo = addTo;
    }

    public ValueExpression getDebug() {
        return debug;
    }

    public void setDebug(ValueExpression debug) {
        this.debug = debug;
    }


}
