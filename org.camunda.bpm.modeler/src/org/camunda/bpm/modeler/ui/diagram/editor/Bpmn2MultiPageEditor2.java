/*******************************************************************************
 * Copyright (c) 2011 Red Hat, Inc.
 *  All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Red Hat, Inc. - initial API and implementation
 *
 * @author Bob Brodt
 ******************************************************************************/

package org.camunda.bpm.modeler.ui.diagram.editor;

import java.util.ArrayList;
import java.util.List;

import org.camunda.bpm.modeler.core.utils.ModelUtil;
import org.eclipse.bpmn2.di.BPMNDiagram;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabFolder2Listener;
import org.eclipse.swt.custom.CTabFolderEvent;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.ide.IGotoMarker;
import org.eclipse.ui.part.MultiPageEditorPart;
import org.eclipse.ui.part.MultiPageEditorSite;

/**
 * This class implements a multi-page version of the BPMN2 Modeler (BPMN2Editor class).
 * To revert back to the original, single-page version simply change the editor extension
 * point in plugin.xml (see comments there).
 * 
 * This is still in the experimental phase and currently only supports a single diagram
 * per .bpmn file. An optional second page, which displays the XML source, can be created
 * from the context menu. The source view is not yet synchronized to the design view and
 * can only show the XML as of the last "Save" i.e. the current state of the file on disk,
 * not the in-memory model. Design/Source view synchronization will be implemented in a
 * future version, but direct editing of the XML will not be supported - it will remain
 * "view only".
 * 
 * Future versions will support multiple diagrams per .bpmn file with the ability to add
 * and remove pages containing different diagram types. It should be possible for the user
 * to create a single file that contains a mix of Process, Collaboration and Choreography
 * diagrams. Whether or not these types of files are actually deployable and/or executable
 * is another story ;)
 */
public class Bpmn2MultiPageEditor2 extends MultiPageEditorPart implements IGotoMarker {

	DesignEditor designEditor;
	private CTabFolder tabFolder;
	private int defaultTabHeight;
	private final List<BPMNDiagram> bpmnDiagrams = new ArrayList<BPMNDiagram>();
	
	public Bpmn2MultiPageEditor2() {
		super();
	}

	@Override
	protected IEditorSite createSite(final IEditorPart editor) {
		if (editor instanceof DesignEditor)
			return new DesignEditorSite(this, editor);
		return new MultiPageEditorSite(this, editor);
	}

	@Override
	public String getTitle() {
		if (designEditor!=null)
			return designEditor.getTitle();
		return super.getTitle();
	}

	@Override
	public String getPartName() {
		if (designEditor!=null)
			return designEditor.getPartName();
		return super.getPartName();
	}

    /**
     * Method declared on IEditorPart.
     * 
     * @param marker Marker to look for
     */
    @Override
    public void gotoMarker(final IMarker marker) {
        if (getActivePage() < 0) {
            setActivePage(0);
        }
        IDE.gotoMarker(getEditor(getActivePage()), marker);
    }
	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.MultiPageEditorPart#createPages()
	 */
	@Override
	protected void createPages() {
		tabFolder = (CTabFolder)getContainer();
		tabFolder.addCTabFolder2Listener( new CTabFolder2Listener() {

			@Override
			public void close(final CTabFolderEvent event) {
			}

			@Override
			public void minimize(final CTabFolderEvent event) {
			}

			@Override
			public void maximize(final CTabFolderEvent event) {
			}

			@Override
			public void restore(final CTabFolderEvent event) {
			}

			@Override
			public void showList(final CTabFolderEvent event) {
			}
			
		});
		tabFolder.addSelectionListener( new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				int pageIndex = tabFolder.getSelectionIndex();
				if (pageIndex>=0 && pageIndex<bpmnDiagrams.size() && designEditor!=null) {
					BPMNDiagram bpmnDiagram = bpmnDiagrams.get(pageIndex);
					designEditor.selectBpmnDiagram(bpmnDiagram);
				}
			}
		});
		
		// defer editor layout until all pages have been created
		tabFolder.setLayoutDeferred(true);
		
		createDesignEditor();
		
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				setActivePage(0);
				designEditor.selectBpmnDiagram(bpmnDiagrams.get(0));
				tabFolder.setLayoutDeferred(false);
				tabFolder.setTabPosition(SWT.TOP);
				updateTabs();
			}
		});
	}

	protected void createDesignEditor() {
		if (designEditor==null) {
			designEditor = new DesignEditor();
			
			try {
				int pageIndex = tabFolder.getItemCount();
				addPage(pageIndex, designEditor, Bpmn2MultiPageEditor2.this.getEditorInput());
				defaultTabHeight = tabFolder.getTabHeight();
				setPageText(pageIndex,ModelUtil.getDiagramTypeName( designEditor.getBpmnDiagram() ));

				defaultTabHeight = tabFolder.getTabHeight();

				updateTabs();
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public DesignEditor getDesignEditor() {
		return designEditor;
	}
	
	protected void addDesignPage(final BPMNDiagram bpmnDiagram) {
		createDesignEditor();
			Display.getDefault().asyncExec(new Runnable() {
				@Override
				public void run() {
					try {
			
						int pageIndex = tabFolder.getItemCount();
						Bpmn2DiagramEditorInput input = (Bpmn2DiagramEditorInput)designEditor.getEditorInput();
						input.setBpmnDiagram(bpmnDiagram);
						addPage(pageIndex, designEditor, input);
						CTabItem oldItem = tabFolder.getItem(pageIndex-1);
						CTabItem newItem = tabFolder.getItem(pageIndex);
						newItem.setControl( oldItem.getControl() );
						setPageText(pageIndex,bpmnDiagram.getName());
			
						setActivePage(pageIndex);
						updateTabs();
					}
					catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
	}
	
	public void showDesignPage(final BPMNDiagram bpmnDiagram) {
		final int pageIndex = bpmnDiagrams.indexOf(bpmnDiagram);
		if (pageIndex>=0) {
			if (getDesignEditor().getBpmnDiagram()!=bpmnDiagram) {
				setActivePage(pageIndex);
			}
		}
		else {
			designEditor.showDesignPage(bpmnDiagram);
		}
	}
	
	protected void removeDesignPage(final BPMNDiagram bpmnDiagram) {
		final int pageIndex = bpmnDiagrams.indexOf(bpmnDiagram);
		if (pageIndex>0) {
			// go back to "Design" page - the only page that can't be removed
			Display.getCurrent().asyncExec( new Runnable() {
				@Override
				public void run() {
					setActivePage(0);
					
					IEditorPart editor = getEditor(pageIndex);
					if (editor instanceof DesignEditor) {
						((DesignEditor)editor).deleteBpmnDiagram(bpmnDiagram);
					}
					
					// need to manage this ourselves so that the CTabFolder doesn't
					// dispose our editor site (a child of the CTabItem.control)
					tabFolder.getItem(pageIndex).setControl(null);
					
					removePage(pageIndex);
					
					tabFolder.getSelection().getControl().setVisible(true);
				}
			});
		}
	}

	public int getDesignPageCount() {
		int count = getPageCount();
		return count;
	}

	
	@Override
	public void addPage(final int pageIndex, final IEditorPart editor, final IEditorInput input)
			throws PartInitException {
		super.addPage(pageIndex,editor,input);
		if (editor instanceof DesignEditor) {
			bpmnDiagrams.add(pageIndex,((DesignEditor)editor).getBpmnDiagram());
		}
	}
	
	@Override
	public void removePage(final int pageIndex) {
		Object page = tabFolder.getItem(pageIndex).getData();
		super.removePage(pageIndex);
		updateTabs();
		if (page instanceof DesignEditor) {
			bpmnDiagrams.remove(pageIndex);
		}
	}

	@Override
	protected void pageChange(final int newPageIndex) {
		super.pageChange(newPageIndex);

		IEditorPart editor = getEditor(newPageIndex);
		if (editor instanceof DesignEditor) {
			BPMNDiagram bpmnDiagram = bpmnDiagrams.get(newPageIndex);
			((DesignEditor)editor).pageChange(bpmnDiagram);
//			Diagram diagram = DIUtils.findDiagram(designEditor, bpmnDiagram);
//			if (diagram != null)
//				designEditor.selectPictogramElements(new PictogramElement[] {(PictogramElement)diagram});
		}
	}

	@Override
	public int getPageCount() {
		return tabFolder.getItemCount();
	}
	
	public CTabItem getTabItem(final int pageIndex) {
		return tabFolder.getItem(pageIndex);
	}
	
	public BPMNDiagram getBpmnDiagram(final int i) {
		if (i>=0 && i<bpmnDiagrams.size()) {
			return bpmnDiagrams.get(i);
		}
		return null;
	}
	
	private void updateTabs() {
		if (!tabFolder.isLayoutDeferred()) {
			if (tabFolder.getItemCount()==1) {
				tabFolder.setTabHeight(0);
			}
			else
				tabFolder.setTabHeight(defaultTabHeight);
		}
		tabFolder.layout();
	}
	
	public CTabFolder getTabFolder() {
		return tabFolder;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.EditorPart#doSave(org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	public void doSave(final IProgressMonitor monitor) {
		designEditor.doSave(monitor);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.EditorPart#doSaveAs()
	 */
	@Override
	public void doSaveAs() {
		IEditorPart activeEditor = getActiveEditor();
		activeEditor.doSaveAs();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.EditorPart#isSaveAsAllowed()
	 */
	@Override
	public boolean isSaveAsAllowed() {
		
		/* 
		 * Depending upon the active page in multipage editor, call the saveAsAllowed. 
		 * It helps to see whether a particular editor allows 'save as' feature 
		 */
		IEditorPart activeEditor = getActiveEditor();
		return activeEditor.isSaveAsAllowed();
	}

	@Override
	public void dispose() {
		designEditor.dispose();
	}
}
