package org.camunda.bpm.modeler.core.validation;

import java.util.List;

import org.camunda.bpm.modeler.Messages;
import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.CallActivity;
import org.eclipse.bpmn2.CatchEvent;
import org.eclipse.bpmn2.CompensateEventDefinition;
import org.eclipse.bpmn2.ConditionalEventDefinition;
import org.eclipse.bpmn2.DataObject;
import org.eclipse.bpmn2.Definitions;
import org.eclipse.bpmn2.EndEvent;
import org.eclipse.bpmn2.Error;
import org.eclipse.bpmn2.ErrorEventDefinition;
import org.eclipse.bpmn2.Escalation;
import org.eclipse.bpmn2.EscalationEventDefinition;
import org.eclipse.bpmn2.EventDefinition;
import org.eclipse.bpmn2.FlowElement;
import org.eclipse.bpmn2.FlowNode;
import org.eclipse.bpmn2.FormalExpression;
import org.eclipse.bpmn2.Gateway;
import org.eclipse.bpmn2.ItemDefinition;
import org.eclipse.bpmn2.Message;
import org.eclipse.bpmn2.MessageEventDefinition;
import org.eclipse.bpmn2.Process;
import org.eclipse.bpmn2.RootElement;
import org.eclipse.bpmn2.ScriptTask;
import org.eclipse.bpmn2.SendTask;
import org.eclipse.bpmn2.SequenceFlow;
import org.eclipse.bpmn2.Signal;
import org.eclipse.bpmn2.SignalEventDefinition;
import org.eclipse.bpmn2.StartEvent;
import org.eclipse.bpmn2.ThrowEvent;
import org.eclipse.bpmn2.TimerEventDefinition;
import org.eclipse.bpmn2.di.BPMNDiagram;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.AbstractModelConstraint;
import org.eclipse.emf.validation.EMFEventType;
import org.eclipse.emf.validation.IValidationContext;

public class Bpmn2BatchValidationConstraint extends AbstractModelConstraint {

	public final static String ERROR_ID = "org.camunda.bpm.modeler.validation.error"; //$NON-NLS-1$
	public final static String WARNING_ID = "org.camunda.bpm.modeler.validation.warning"; //$NON-NLS-1$
	
	private boolean warnings = false;
	
	public Bpmn2BatchValidationConstraint() {
	}

	@Override
	public IStatus validate(final IValidationContext ctx) {
		EObject eObj = ctx.getTarget();
		EMFEventType eType = ctx.getEventType();
		String id = ctx.getCurrentConstraintId();
		if (WARNING_ID.equals(id))
			warnings = true;
		else
			warnings = false;
		
		// In the case of batch mode.
		if (eType == EMFEventType.NULL) {
			if (eObj instanceof BPMNDiagram) {
				return validateDiagram(ctx, (BPMNDiagram) eObj);
			}
			if (eObj instanceof Definitions) {
				return validateDefinitions(ctx, (Definitions) eObj);
			}
			if (eObj instanceof BaseElement) {
				return validateBaseElement(ctx, (BaseElement) eObj);
			}
		} else { // In the case of live mode.
		}

		return ctx.createSuccessStatus();
	}

	private IStatus validateDiagram(final IValidationContext ctx, final BPMNDiagram obj) {
		Definitions defs = (Definitions) obj.eContainer();
		return validateDefinitions(ctx, defs);
	}

	private IStatus validateDefinitions(final IValidationContext ctx, final Definitions def) {
		List<RootElement> rootElements = def.getRootElements();
		for (RootElement root : rootElements) {
			if (root instanceof Process) {
				Process process = (Process) root;

				if (warnings) {
					// report warnings only
					boolean foundStartEvent = false;
					boolean foundEndEvent = false;
					List<FlowElement> flowElements = process.getFlowElements();
					for (FlowElement fe : flowElements) {
						if (fe instanceof StartEvent) {
							foundStartEvent = true;
						}
						if (fe instanceof EndEvent) {
							foundEndEvent = true;
						}
					}
					if (!foundStartEvent) {
						ctx.addResult(root);
						return ctx.createFailureStatus(Messages.Bpmn2BatchValidationConstraint_2);
					}
					if (!foundEndEvent) {
						ctx.addResult(root);
						return ctx.createFailureStatus(Messages.Bpmn2BatchValidationConstraint_3);
					}
				}
				else {
					// report errors only
					if (isEmpty(process.getName())) {
						ctx.addResult(root);
						return ctx.createFailureStatus(Messages.Bpmn2BatchValidationConstraint_4);
					}
				}
			}
			else if (root instanceof Error) {
				if (warnings) {
					if (((Error)root).getStructureRef()==null) {
						ctx.addResult(root);
						return ctx.createFailureStatus(Messages.Bpmn2BatchValidationConstraint_5);
					}
				}
			}
			else if (root instanceof Escalation) {
				if (warnings) {
					if (((Escalation)root).getStructureRef()==null) {
						ctx.addResult(root);
						return ctx.createFailureStatus(Messages.Bpmn2BatchValidationConstraint_6);
					}
				}
			}
			else if (root instanceof Message) {
				if (warnings) {
					if (((Message)root).getItemRef()==null) {
						ctx.addResult(root);
						return ctx.createFailureStatus(Messages.Bpmn2BatchValidationConstraint_7);
					}
				}
			}
			else if (root instanceof Signal) {
				if (warnings) {
					if (((Signal)root).getStructureRef()==null) {
						ctx.addResult(root);
						return ctx.createFailureStatus(Messages.Bpmn2BatchValidationConstraint_8);
					}
				}
			}
			else if (root instanceof ItemDefinition) {
				if (!warnings) {
					if (((ItemDefinition)root).getStructureRef()==null) {
						ctx.addResult(root);
						return ctx.createFailureStatus(Messages.Bpmn2BatchValidationConstraint_9);
					}
				}
			}
		}
		return ctx.createSuccessStatus();
	}

	private IStatus validateBaseElement(final IValidationContext ctx, final BaseElement fe) {

		if (fe instanceof StartEvent) {
			StartEvent se = (StartEvent) fe;
			
			if (!warnings) {
				if (se.getOutgoing() == null || se.getOutgoing().size() < 1) {
					return ctx.createFailureStatus(Messages.Bpmn2BatchValidationConstraint_10);
				}
			}
		}
		else if (fe instanceof EndEvent) {
			EndEvent ee = (EndEvent) fe;
			
			if (!warnings) {
				if (ee.getIncoming() == null || ee.getIncoming().size() < 1) {
					return ctx.createFailureStatus(Messages.Bpmn2BatchValidationConstraint_11);
				}
			}
		}
		else if (fe instanceof ScriptTask) {
			ScriptTask st = (ScriptTask) fe;
			
			if (warnings) {
				if (isEmpty(st.getScript())) {
					return ctx.createFailureStatus(Messages.Bpmn2BatchValidationConstraint_12);
				}
				if (isEmpty(st.getScriptFormat())) {
					return ctx.createFailureStatus(Messages.Bpmn2BatchValidationConstraint_13);
				}
			}
		}
		else if (fe instanceof SendTask) {
			SendTask st = (SendTask) fe;

			if (!warnings) {
				if (st.getOperationRef() == null) {
					return ctx.createFailureStatus(Messages.Bpmn2BatchValidationConstraint_14);
				}
				if (st.getMessageRef() == null) {
					return ctx.createFailureStatus(Messages.Bpmn2BatchValidationConstraint_15);
				}
			}
		}
		else if (fe instanceof CatchEvent) {
			CatchEvent event = (CatchEvent) fe;

			if (!warnings) {
				List<EventDefinition> eventdefs = event.getEventDefinitions();
				if (eventdefs.size()==0) {
					return ctx.createFailureStatus(Messages.Bpmn2BatchValidationConstraint_16);
				}
				
				for (EventDefinition ed : eventdefs) {
					if (ed instanceof TimerEventDefinition) {
						TimerEventDefinition ted = (TimerEventDefinition) ed;
						if (	ted.getTimeDate() == null
								&& ted.getTimeDuration() == null
								&& ted.getTimeCycle() == null
						) {
							return ctx.createFailureStatus(Messages.Bpmn2BatchValidationConstraint_17);
						}
					} else if (ed instanceof SignalEventDefinition) {
						if (((SignalEventDefinition) ed).getSignalRef() == null) {
							return ctx.createFailureStatus(Messages.Bpmn2BatchValidationConstraint_18);
						}
					} else if (ed instanceof ErrorEventDefinition) {
						if (((ErrorEventDefinition) ed).getErrorRef() == null
								|| ((ErrorEventDefinition) ed).getErrorRef().getErrorCode() == null) {
							return ctx.createFailureStatus(Messages.Bpmn2BatchValidationConstraint_19);
						}
					} else if (ed instanceof ConditionalEventDefinition) {
						FormalExpression conditionalExp = (FormalExpression) ((ConditionalEventDefinition) ed)
								.getCondition();
						if (conditionalExp.getBody() == null) {
							return ctx.createFailureStatus(Messages.Bpmn2BatchValidationConstraint_20);
						}
					} else if (ed instanceof EscalationEventDefinition) {
						if (((EscalationEventDefinition) ed).getEscalationRef() == null) {
							return ctx.createFailureStatus(Messages.Bpmn2BatchValidationConstraint_21);
						}
					} else if (ed instanceof MessageEventDefinition) {
						if (((MessageEventDefinition) ed).getMessageRef() == null) {
							return ctx.createFailureStatus(Messages.Bpmn2BatchValidationConstraint_22);
						}
					} else if (ed instanceof CompensateEventDefinition) {
						if (((CompensateEventDefinition) ed).getActivityRef() == null) {
							return ctx.createFailureStatus(Messages.Bpmn2BatchValidationConstraint_23);
						}
					}
				}
			}
		}
		else if (fe instanceof ThrowEvent) {
			ThrowEvent event = (ThrowEvent) fe;

			if (!warnings) {
				List<EventDefinition> eventdefs = event.getEventDefinitions();
				if (eventdefs.size()==0) {
					return ctx.createFailureStatus(Messages.Bpmn2BatchValidationConstraint_24);
				}

				for (EventDefinition ed : eventdefs) {
					if (ed instanceof TimerEventDefinition) {
						TimerEventDefinition ted = (TimerEventDefinition) ed;
						if (	ted.getTimeDate() == null
								&& ted.getTimeDuration() == null
								&& ted.getTimeCycle() == null
						) {
							return ctx.createFailureStatus(Messages.Bpmn2BatchValidationConstraint_25);
						}
					} else if (ed instanceof SignalEventDefinition) {
						if (((SignalEventDefinition) ed).getSignalRef() == null) {
							return ctx.createFailureStatus(Messages.Bpmn2BatchValidationConstraint_26);
						}
					} else if (ed instanceof ErrorEventDefinition) {
						if (((ErrorEventDefinition) ed).getErrorRef() == null
								|| ((ErrorEventDefinition) ed).getErrorRef().getErrorCode() == null) {
							return ctx.createFailureStatus(Messages.Bpmn2BatchValidationConstraint_27);
						}
					} else if (ed instanceof ConditionalEventDefinition) {
						FormalExpression conditionalExp = (FormalExpression) ((ConditionalEventDefinition) ed)
								.getCondition();
						if (conditionalExp.getBody() == null) {
							return ctx.createFailureStatus(Messages.Bpmn2BatchValidationConstraint_28);
						}
					} else if (ed instanceof EscalationEventDefinition) {
						if (((EscalationEventDefinition) ed).getEscalationRef() == null) {
							return ctx.createFailureStatus(Messages.Bpmn2BatchValidationConstraint_29);
						}
					} else if (ed instanceof MessageEventDefinition) {
						if (((MessageEventDefinition) ed).getMessageRef() == null) {
							return ctx.createFailureStatus(Messages.Bpmn2BatchValidationConstraint_30);
						}
					} else if (ed instanceof CompensateEventDefinition) {
						if (((CompensateEventDefinition) ed).getActivityRef() == null) {
							return ctx.createFailureStatus(Messages.Bpmn2BatchValidationConstraint_31);
						}
					}
				}
			}
		}
		else if (fe instanceof SequenceFlow) {
			SequenceFlow sf = (SequenceFlow) fe;

			if (!warnings) {
				if (sf.getSourceRef() == null) {
					return ctx.createFailureStatus(Messages.Bpmn2BatchValidationConstraint_32);
				}
				if (sf.getTargetRef() == null) {
					return ctx.createFailureStatus(Messages.Bpmn2BatchValidationConstraint_33);
				}
			}
		}
		else if (fe instanceof Gateway) {
			Gateway gw = (Gateway) fe;

			if (!warnings) {
//				if (gw.getGatewayDirection() == null
//						|| gw.getGatewayDirection().getValue() == GatewayDirection.UNSPECIFIED.getValue()) {
//					ctx.addResult(Bpmn2Package.eINSTANCE.getGateway_GatewayDirection());
//					return ctx.createFailureStatus("Gateway does not specify a valid direction");
//				}
//				if (gw instanceof ExclusiveGateway) {
//					if (gw.getGatewayDirection().getValue() != GatewayDirection.DIVERGING.getValue()
//							&& gw.getGatewayDirection().getValue() != GatewayDirection.CONVERGING.getValue()) {
//						return ctx.createFailureStatus(
//								"Invalid Gateway direction for Exclusing Gateway. It should be 'Converging' or 'Diverging'");
//					}
//				}
//				if (gw instanceof EventBasedGateway) {
//					if (gw.getGatewayDirection().getValue() != GatewayDirection.DIVERGING.getValue()) {
//						return ctx.createFailureStatus(
//								"Invalid Gateway direction for EventBased Gateway. It should be 'Diverging'");
//					}
//				}
//				if (gw instanceof ParallelGateway) {
//					if (gw.getGatewayDirection().getValue() != GatewayDirection.DIVERGING.getValue()
//							&& gw.getGatewayDirection().getValue() != GatewayDirection.CONVERGING.getValue()) {
//						return ctx.createFailureStatus(
//								"Invalid Gateway direction for Parallel Gateway. It should be 'Converging' or 'Diverging'");
//					}
//				}
//				if (gw instanceof InclusiveGateway) {
//					if (gw.getGatewayDirection().getValue() != GatewayDirection.DIVERGING.getValue()
//							&& gw.getGatewayDirection().getValue() != GatewayDirection.CONVERGING.getValue()) {
//						return ctx.createFailureStatus(
//								"Invalid Gateway direction for Inclusive Gateway. It should be 'Converging' or 'Diverging'");
//					}
//				}
//				if (gw instanceof ComplexGateway) {
//					if (gw.getGatewayDirection().getValue() != GatewayDirection.DIVERGING.getValue()
//							&& gw.getGatewayDirection().getValue() != GatewayDirection.CONVERGING.getValue()) {
//						return ctx.createFailureStatus(
//								"Invalid Gateway direction for Complex Gateway. It should be 'Converging' or 'Diverging'");
//					}
//				}
			}
		}
		else if (fe instanceof CallActivity) {
			CallActivity ca = (CallActivity) fe;

			if (!warnings) {
				if (ca.getCalledElementRef() == null) {
					return ctx.createFailureStatus(
							Messages.Bpmn2BatchValidationConstraint_34);
				}
			}
		}
		else if (fe instanceof DataObject) {
			DataObject dao = (DataObject) fe;

			if (!warnings) {
				if (dao.getName() == null || dao.getName().length() < 1) {
					return ctx.createFailureStatus(Messages.Bpmn2BatchValidationConstraint_35);
				}
			}
		}
		
		
		if (fe instanceof FlowNode) {
			return validateFlowNode(ctx, (FlowNode) fe);
		}

		return ctx.createSuccessStatus();
	}

	private IStatus validateFlowNode(final IValidationContext ctx, final FlowNode fn) {
		if (!warnings) {
			boolean needIncoming = true;
			boolean needOutgoing = true;
			if (fn instanceof ThrowEvent)
				needOutgoing = false;
			if (fn instanceof CatchEvent)
				needIncoming = false;
			
			if (needOutgoing) {
				if ((fn.getOutgoing() == null || fn.getOutgoing().size() < 1)) {
					return ctx.createFailureStatus(Messages.Bpmn2BatchValidationConstraint_36);
				}
			}
			if (needIncoming) {
				if ((fn.getIncoming() == null || fn.getIncoming().size() < 1)) {
					return ctx.createFailureStatus(Messages.Bpmn2BatchValidationConstraint_37);
				}
			}
		}

		return ctx.createSuccessStatus();
	}

	private static boolean isEmpty(final String str) {
		return str == null || str.isEmpty();
	}

	private boolean containsWhiteSpace(final String testString) {
		if (testString != null) {
			for (int i = 0; i < testString.length(); i++) {
				if (Character.isWhitespace(testString.charAt(i))) {
					return true;
				}
			}
		}
		return false;
	}
}
