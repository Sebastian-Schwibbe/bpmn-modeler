/******************************************************************************* 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * camunda services GmbH - initial API and implementation 
 *
 ******************************************************************************/

package org.camunda.bpm.modeler.core.importer.handlers;

import org.camunda.bpm.modeler.core.importer.ModelImport;
import org.eclipse.bpmn2.DataStoreReference;

/**
 * 
 * @author Nico Rehwaldt
 * @author Daniel Meyer
 */
public class DatastoreReferenceShapeHandler extends AbstractShapeHandler<DataStoreReference> {

	public DatastoreReferenceShapeHandler(ModelImport bpmn2ModelImport) {
		super(bpmn2ModelImport);
	}

}
