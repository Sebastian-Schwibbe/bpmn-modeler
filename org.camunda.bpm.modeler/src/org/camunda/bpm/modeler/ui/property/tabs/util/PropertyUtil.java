package org.camunda.bpm.modeler.ui.property.tabs.util;

import org.camunda.bpm.modeler.ui.property.tabs.binding.BooleanButtonBinding;
import org.camunda.bpm.modeler.ui.property.tabs.binding.FormalExpressionTextBinding;
import org.camunda.bpm.modeler.ui.property.tabs.binding.IntegerTextBinding;
import org.camunda.bpm.modeler.ui.property.tabs.binding.ModelTextBinding;
import org.camunda.bpm.modeler.ui.property.tabs.binding.StringTextBinding;
import org.camunda.bpm.modeler.ui.property.tabs.radio.Radio.RadioGroup;
import org.camunda.bpm.modeler.ui.util.Browser;
import org.eclipse.bpmn2.Expression;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.graphiti.ui.platform.GFPropertySection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolTip;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertyConstants;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetWidgetFactory;

import de.cas.open.commons.util.GenericTupel;

/**
 * Using form data here for layouting.
 * 
 * To get to know how it works, refer to {@link http
 * ://www.eclipse.org/articles/article
 * .php?file=Article-Understanding-Layouts/index.html}.
 * 
 * @author nico.rehwaldt
 */
public class PropertyUtil {

	private static final int DESCRIPTION_FONT_SIZE = 8;

	public static Text createText(final GFPropertySection section, final Composite parent, final String label,
			final EStructuralFeature feature, final EObject bo) {
		Text text = createUnboundText(section, parent, label);

		addBinding(text, bo, feature);

		return text;
	}
	
	public static GenericTupel<Text, CLabel> createTextAndLabel(final GFPropertySection section, final Composite parent, final String label,
			final EStructuralFeature feature, final EObject bo) {
		GenericTupel<Text, CLabel> textAndLabel = createUnboundTextAndLabel(section, parent, label);

		return textAndLabel;
	}

	public static Text createUnboundText(final GFPropertySection section, final Composite parent, final String label) {
		Composite composite = createStandardComposite(section, parent);
		Text text = createSimpleText(section, composite, "");
		
		createLabel(section, composite, label, text);
		return text;
	}

	public static GenericTupel<Text, CLabel> createUnboundTextAndLabel(final GFPropertySection section, final Composite parent, final String string) {
		Composite composite = createStandardComposite(section, parent);
		
		Text text = createSimpleText(section, composite, "");
		
		CLabel label = createLabel(section, composite, string, text);
		
		GenericTupel<Text, CLabel> textAndLabel = new GenericTupel<Text, CLabel>(text, label);

		return textAndLabel;
	}
	
	public static CCombo createDropDown(final GFPropertySection section, final Composite parent, final String label) {
		Composite composite = createStandardComposite(section, parent);
		CCombo comboBox = createSimpleDropDown(section, composite);

		createLabel(section, composite, label, comboBox);
		return comboBox;
	}

	public static CCombo createDropDown(final GFPropertySection section, final Composite parent, final String label, final int style) {
		Composite composite = createStandardComposite(section, parent);
		CCombo comboBox = createSimpleDropDown(section, composite, style);

		createLabel(section, composite, label, comboBox);
		return comboBox;
	}

	public static Text createMultiText(final GFPropertySection section, final Composite parent, final String label,
			final EStructuralFeature feature, final EObject bo) {
		Composite composite = createStandardComposite(section, parent);

		Text text = createSimpleMultiText(section, composite, "");

		addBinding(text, bo, feature);

		createLabel(section, composite, label, text);

		return text;
	}

	protected static CCombo createSimpleDropDown(final GFPropertySection section, final Composite parent) {
		return createSimpleDropDown(section, parent, SWT.BORDER | SWT.READ_ONLY);
	}

	protected static CCombo createSimpleDropDown(final GFPropertySection section, final Composite parent, final int style) {
		TabbedPropertySheetWidgetFactory factory = section.getWidgetFactory();
		final CCombo dropDown = factory.createCCombo(parent, style);
		setStandardLayout(dropDown);

		return dropDown;
	}

	protected static Button createSimpleCheckbox(final GFPropertySection section, final Composite parent) {
		TabbedPropertySheetWidgetFactory factory = section.getWidgetFactory();
		final Button checkbox = factory.createButton(parent, "", SWT.CHECK);
		setStandardLayout(checkbox);

		return checkbox;
	}

	// public static
	public static Button createCheckbox(final GFPropertySection section, final Composite parent, final String label,
			final EStructuralFeature feature, final EObject bo) {
		Button checkbox = createUnboundCheckbox(section, parent, label);

		new BooleanButtonBinding(bo, feature, checkbox).establish();

		return checkbox;
	}

	public static Button createUnboundCheckbox(final GFPropertySection section, final Composite parent, final String label) {
		Composite composite = createStandardComposite(section, parent);

		Button checkbox = createSimpleCheckbox(section, composite);
		createLabel(section, composite, label, checkbox);

		return checkbox;
	}

	public static CLabel createLabel(final GFPropertySection section, final Composite parent, final String label, final Control control) {
		TabbedPropertySheetWidgetFactory factory = section.getWidgetFactory();
		CLabel cLabel = factory.createCLabel(parent, label + ":"); //$NON-NLS-1$
		FormData data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(control, -ITabbedPropertyConstants.HSPACE);
		data.top = new FormAttachment(control, 0, SWT.TOP);
		cLabel.setLayoutData(data);

		return cLabel;
	}

	public static ToolTip createToolTipFor(final Control element, final String message) {

		final ToolTip tip = new ToolTip(element.getShell(), SWT.NONE);
		tip.setMessage(message);

		element.addFocusListener(new FocusListener() {

			@Override
			public void focusLost(final FocusEvent e) {
				tip.setVisible(false);
			}

			@Override
			public void focusGained(final FocusEvent e) {
				Text actionWidget = (Text) e.widget;

				Rectangle bounds = actionWidget.getBounds();

				Point loc = actionWidget.toDisplay(actionWidget.getLocation());

				tip.setLocation(loc.x - bounds.x, loc.y - bounds.y + bounds.height + actionWidget.getBorderWidth());
				tip.setVisible(true);
			}
		});

		element.addDisposeListener(new DisposeListener() {
			@Override
			public void widgetDisposed(final DisposeEvent e) {
				tip.dispose();
			}
		});

		return tip;
	}

	public static Composite createStandardComposite(final GFPropertySection section, final Composite parent) {
		TabbedPropertySheetWidgetFactory factory = section.getWidgetFactory();
		Composite composite = factory.createFlatFormComposite(parent);
		
		composite.setLayoutData(new GridData(SWT.FILL, GridData.CENTER, true, false));
		return composite;
	}

	public static Composite createGridLayoutedComposite(final GFPropertySection section, final Composite parent) {
		TabbedPropertySheetWidgetFactory factory = section.getWidgetFactory();
		Composite composite = factory.createFlatFormComposite(parent);

		GridLayout gridLayout = new GridLayout();
		gridLayout.marginBottom = -5;
		gridLayout.marginRight = -5;
		gridLayout.marginLeft = -5;
		gridLayout.marginTop = -5;

		composite.setLayout(gridLayout);

		composite.setLayoutData(new GridData(SWT.FILL, GridData.CENTER, true, false));
		return composite;
	}

	public static Text createUnboundRadioText(final GFPropertySection section, final Composite parent, final String label,
			final EStructuralFeature feature, final RadioGroup<EStructuralFeature> radioGroup) {

		TabbedPropertySheetWidgetFactory factory = section.getWidgetFactory();

		Composite composite = createStandardComposite(section, parent);

		Composite radioComposite = createStandardComposite(section, composite);

		setStandardLayout(radioComposite);

		final Text text = factory.createText(radioComposite, "");
		final Button radioButton = factory.createButton(radioComposite, "", SWT.NO_FOCUS | SWT.RADIO);
		
		FormData textFormData = new FormData();
		textFormData.left = new FormAttachment(0, 15);
		textFormData.right = new FormAttachment(100, 0);

		FormData radioButtonData = new FormData();
		radioButtonData.right = new FormAttachment(text, 0);
		radioButtonData.top = new FormAttachment(text, 0, SWT.CENTER);

		text.setLayoutData(textFormData);
		radioButton.setLayoutData(radioButtonData);
		
		// register with radio group
		radioGroup.add(radioButton, feature);

		createLabel(section, composite, label, radioComposite);

		return text;
	}
	
	public static Text createRadioText(final GFPropertySection section, final Composite parent, final String label,
			final EStructuralFeature feature, final RadioGroup<EStructuralFeature> radioGroup, final EObject bo) {

		Text text = createUnboundRadioText(section, parent, label, feature, radioGroup);

		ModelTextBinding<?> binding = getBinding(text, bo, feature);

		if (binding != null) {
			binding.setDisableOnNull(true);
			binding.establish();
		}

		return text;
	}

	public static Text createSimpleText(final GFPropertySection section, final Composite parent, final String value) {
		TabbedPropertySheetWidgetFactory factory = section.getWidgetFactory();
		final Text text = factory.createText(parent, value); //$NON-NLS-1$
		setStandardLayout(text);
		return text;
	}

	protected static Text createSimpleMultiText(final GFPropertySection section, final Composite parent, final String value) {
		TabbedPropertySheetWidgetFactory factory = section.getWidgetFactory();
		final Text text = factory.createText(parent, value, SWT.MULTI | SWT.WRAP | SWT.V_SCROLL | SWT.H_SCROLL); //$NON-NLS-1$

		FormData data = getStandardLayout();

		data.height = 60;
		text.setLayoutData(data);
		return text;
	}

	public static void setStandardLayout(final Control control) {
		control.setLayoutData(getStandardLayout());
	}

	// binding ///////////////////////////////////////////

	private static void addBinding(final Text text, final EObject bo, final EStructuralFeature feature) {
		ModelTextBinding<?> binding = getBinding(text, bo, feature);

		if (binding != null) {
			binding.establish();
		}
	}

	private static ModelTextBinding<?> getBinding(final Text text, final EObject bo, final EStructuralFeature feature) {

		EClassifier featureType = feature.getEType();

		Class<?> instanceClass = featureType.getInstanceClass();
		if (instanceClass.equals(int.class) || instanceClass.equals(Integer.class)) {
			return new IntegerTextBinding(bo, feature, text);
		} else if (instanceClass.equals(Expression.class)) {
			return new FormalExpressionTextBinding(bo, feature, text);
		} else {
			return new StringTextBinding(bo, feature, text);
		}
	}

	public static FormData getStandardLayout() {
		FormData data = new FormData();
		data.left = new FormAttachment(0, 120);
		data.right = new FormAttachment(100, 0);
		data.top = new FormAttachment(0, ITabbedPropertyConstants.VSPACE);

		return data;
	}

	public static Text attachNote(final Control attachToControl, final String note) {
		Composite parent = attachToControl.getParent();
		
		final Text text = new Text(parent, SWT.READ_ONLY | SWT.MULTI | SWT.WRAP | SWT.NO_FOCUS); 
		
		// if we do this, we won*t be able to copy and paste from the 
		// text box anymore
		// text.setEnabled(false);
		
		text.setText(note);
		text.setEditable(false);
		
		makeDescription(text);

		FormData data = new FormData();
		data.left = new FormAttachment(attachToControl, 0, SWT.LEFT);
		data.right = new FormAttachment(attachToControl, 0, SWT.RIGHT);
		data.top = new FormAttachment(attachToControl, 0, SWT.BOTTOM);
		
		text.setLayoutData(data);
		
		return text;
	}
	
	public static void makeDescription(final Control control) {

		Display display = Display.getCurrent();

		control.setForeground(display.getSystemColor(SWT.COLOR_DARK_GRAY));
		control.setBackground(display.getSystemColor(SWT.COLOR_WHITE));
		
		FontData[] fontData = control.getFont().getFontData();
		for (int i = 0; i < fontData.length; ++i) {
			fontData[i].setHeight(DESCRIPTION_FONT_SIZE);
		}

		final Font newFont = new Font(display, fontData);
		control.setFont(newFont);

		// Since you created the font, you must dispose it
		control.addDisposeListener(new DisposeListener() {
			@Override
			public void widgetDisposed(final DisposeEvent e) {
				newFont.dispose();
			}
		});
	}
	
	public static void attachNoteWithLink(final GFPropertySection section, final Control attachToControl, final String note) {
		Composite parent = attachToControl.getParent();

		TabbedPropertySheetWidgetFactory factory = section.getWidgetFactory();

		Link link = new Link(parent, SWT.NO_BACKGROUND | SWT.NO_FOCUS);
		link.setText(note);

		factory.adapt(link, false, false);

		link.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				Browser.open(e.text);
			}
		});

		FormData helpTextFormData = new FormData();
		helpTextFormData.left = new FormAttachment(attachToControl, 0, SWT.LEFT);
		helpTextFormData.right = new FormAttachment(attachToControl, 0, SWT.RIGHT);
		helpTextFormData.top = new FormAttachment(attachToControl, 0, SWT.BOTTOM);

		link.setLayoutData(helpTextFormData);

	}

	/**
	 * create general note on properties panel tab
	 * which is not attached to a control
	 * 
	 * @param section
	 * @param parent
	 * @param note
	 */
	public static void createNoteWithLink(final GFPropertySection section, final Composite parent, final String note) {
		TabbedPropertySheetWidgetFactory factory = section.getWidgetFactory();
		Composite composite = createStandardComposite(section, parent);

		Link link = new Link(composite, SWT.NO_BACKGROUND | SWT.NO_FOCUS);
		link.setText(note);

		factory.adapt(link, false, false);

		link.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				Browser.open(e.text);
			}
		});
	}
}
