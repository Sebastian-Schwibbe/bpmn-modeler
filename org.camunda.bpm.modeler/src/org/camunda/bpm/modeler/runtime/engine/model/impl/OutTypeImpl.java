/**
 */
package org.camunda.bpm.modeler.runtime.engine.model.impl;

import org.camunda.bpm.modeler.runtime.engine.model.ModelPackage;
import org.camunda.bpm.modeler.runtime.engine.model.OutType;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Out Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.camunda.bpm.modeler.runtime.engine.model.impl.OutTypeImpl#getSource <em>Source</em>}</li>
 *   <li>{@link org.camunda.bpm.modeler.runtime.engine.model.impl.OutTypeImpl#getSourceExpression <em>Source Expression</em>}</li>
 *   <li>{@link org.camunda.bpm.modeler.runtime.engine.model.impl.OutTypeImpl#getTarget <em>Target</em>}</li>
 *   <li>{@link org.camunda.bpm.modeler.runtime.engine.model.impl.OutTypeImpl#getVariables <em>Variables</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class OutTypeImpl extends EObjectImpl implements OutType {
  /**
	 * The default value of the '{@link #getSource() <em>Source</em>}' attribute.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @see #getSource()
	 * @generated
	 * @ordered
	 */
  protected static final String SOURCE_EDEFAULT = null;

  /**
	 * The cached value of the '{@link #getSource() <em>Source</em>}' attribute.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @see #getSource()
	 * @generated
	 * @ordered
	 */
  protected String source = SOURCE_EDEFAULT;

  /**
	 * The default value of the '{@link #getSourceExpression() <em>Source Expression</em>}' attribute.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @see #getSourceExpression()
	 * @generated
	 * @ordered
	 */
  protected static final String SOURCE_EXPRESSION_EDEFAULT = null;

  /**
	 * The cached value of the '{@link #getSourceExpression() <em>Source Expression</em>}' attribute.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @see #getSourceExpression()
	 * @generated
	 * @ordered
	 */
  protected String sourceExpression = SOURCE_EXPRESSION_EDEFAULT;

  /**
	 * The default value of the '{@link #getTarget() <em>Target</em>}' attribute.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @see #getTarget()
	 * @generated
	 * @ordered
	 */
  protected static final String TARGET_EDEFAULT = null;

  /**
	 * The cached value of the '{@link #getTarget() <em>Target</em>}' attribute.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @see #getTarget()
	 * @generated
	 * @ordered
	 */
  protected String target = TARGET_EDEFAULT;

  /**
	 * The default value of the '{@link #getVariables() <em>Variables</em>}' attribute.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @see #getVariables()
	 * @generated
	 * @ordered
	 */
  protected static final String VARIABLES_EDEFAULT = null;

  /**
	 * The cached value of the '{@link #getVariables() <em>Variables</em>}' attribute.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @see #getVariables()
	 * @generated
	 * @ordered
	 */
  protected String variables = VARIABLES_EDEFAULT;

  /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
  protected OutTypeImpl() {
		super();
	}

  /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
  @Override
  protected EClass eStaticClass() {
		return ModelPackage.Literals.OUT_TYPE;
	}

  /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
  public String getSource() {
		return source;
	}

  /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
  public void setSource(String newSource) {
		String oldSource = source;
		source = newSource;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.OUT_TYPE__SOURCE, oldSource, source));
	}

  /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
  public String getSourceExpression() {
		return sourceExpression;
	}

  /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
  public void setSourceExpression(String newSourceExpression) {
		String oldSourceExpression = sourceExpression;
		sourceExpression = newSourceExpression;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.OUT_TYPE__SOURCE_EXPRESSION, oldSourceExpression, sourceExpression));
	}

  /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
  public String getTarget() {
		return target;
	}

  /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
  public void setTarget(String newTarget) {
		String oldTarget = target;
		target = newTarget;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.OUT_TYPE__TARGET, oldTarget, target));
	}

  /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
  public String getVariables() {
		return variables;
	}

  /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
  public void setVariables(String newVariables) {
		String oldVariables = variables;
		variables = newVariables;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.OUT_TYPE__VARIABLES, oldVariables, variables));
	}

  /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
  @Override
  public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case ModelPackage.OUT_TYPE__SOURCE:
				return getSource();
			case ModelPackage.OUT_TYPE__SOURCE_EXPRESSION:
				return getSourceExpression();
			case ModelPackage.OUT_TYPE__TARGET:
				return getTarget();
			case ModelPackage.OUT_TYPE__VARIABLES:
				return getVariables();
		}
		return super.eGet(featureID, resolve, coreType);
	}

  /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
  @Override
  public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case ModelPackage.OUT_TYPE__SOURCE:
				setSource((String)newValue);
				return;
			case ModelPackage.OUT_TYPE__SOURCE_EXPRESSION:
				setSourceExpression((String)newValue);
				return;
			case ModelPackage.OUT_TYPE__TARGET:
				setTarget((String)newValue);
				return;
			case ModelPackage.OUT_TYPE__VARIABLES:
				setVariables((String)newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

  /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
  @Override
  public void eUnset(int featureID) {
		switch (featureID) {
			case ModelPackage.OUT_TYPE__SOURCE:
				setSource(SOURCE_EDEFAULT);
				return;
			case ModelPackage.OUT_TYPE__SOURCE_EXPRESSION:
				setSourceExpression(SOURCE_EXPRESSION_EDEFAULT);
				return;
			case ModelPackage.OUT_TYPE__TARGET:
				setTarget(TARGET_EDEFAULT);
				return;
			case ModelPackage.OUT_TYPE__VARIABLES:
				setVariables(VARIABLES_EDEFAULT);
				return;
		}
		super.eUnset(featureID);
	}

  /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
  @Override
  public boolean eIsSet(int featureID) {
		switch (featureID) {
			case ModelPackage.OUT_TYPE__SOURCE:
				return SOURCE_EDEFAULT == null ? source != null : !SOURCE_EDEFAULT.equals(source);
			case ModelPackage.OUT_TYPE__SOURCE_EXPRESSION:
				return SOURCE_EXPRESSION_EDEFAULT == null ? sourceExpression != null : !SOURCE_EXPRESSION_EDEFAULT.equals(sourceExpression);
			case ModelPackage.OUT_TYPE__TARGET:
				return TARGET_EDEFAULT == null ? target != null : !TARGET_EDEFAULT.equals(target);
			case ModelPackage.OUT_TYPE__VARIABLES:
				return VARIABLES_EDEFAULT == null ? variables != null : !VARIABLES_EDEFAULT.equals(variables);
		}
		return super.eIsSet(featureID);
	}

  /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
  @Override
  public String toString() {
		if (eIsProxy()) return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (source: ");
		result.append(source);
		result.append(", sourceExpression: ");
		result.append(sourceExpression);
		result.append(", target: ");
		result.append(target);
		result.append(", variables: ");
		result.append(variables);
		result.append(')');
		return result.toString();
	}

} //OutTypeImpl
