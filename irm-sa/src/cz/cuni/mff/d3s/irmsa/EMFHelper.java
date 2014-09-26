/*******************************************************************************
 * Copyright 2014 Charles University in Prague
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package cz.cuni.mff.d3s.irmsa;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Collections;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.URIConverter.WriteableOutputStream;
import org.eclipse.emf.ecore.resource.impl.BinaryResourceImpl;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceImpl;

import cz.cuni.mff.d3s.deeco.logging.Log;

/**
 * Helper methods for EMF model reading and writing to XMI strings or files.
 * 
 * @author Ilias Gerostathopoulos <iliasg@d3s.mff.cuni.cz>
 * 
 */
public class EMFHelper {

	public static EObject loadModelFromXMI(String path) {
		Resource resource = new XMIResourceImpl(URI.createURI(path));
		try {
			resource.load(null);
		} catch (IOException e) {
			Log.w("Exception while loading the IRM design model instance.", e);
		}
		return (EObject) resource.getContents().get(0);
	}

	public static void saveModelInXMI(EObject model, String path) {
		Resource resource = new XMIResourceImpl(URI.createURI(path));
		resource.getContents().add(model);
		try {
			resource.save(Collections.EMPTY_MAP);
		} catch (IOException e) {
			Log.w("Exception while saving the trace model instance.", e);
		}
	}

	public static EObject loadModelFromBinary(String path) {
		
		Resource resource = new BinaryResourceImpl(URI.createURI(path));
		try {
			resource.load(null);
		} catch (IOException e) {
			Log.w("Exception while loading the IRM design model instance.", e);
		}
		return (EObject) resource.getContents().get(0);
	}
	
	public static void saveModelInBinary(EObject model, String path) {
		Resource resource = new BinaryResourceImpl(URI.createURI(path));
		resource.getContents().add(model);
		try {
			resource.save(Collections.EMPTY_MAP);
		} catch (IOException e) {
			Log.w("Exception while saving the trace model instance.", e);
		}
	}
		
	public static String getXMIStringFromModel(EObject model) {
		XMLResource resource = new XMIResourceImpl(URI.createURI(""));
		resource.getContents().add(model);
		StringWriter s = new StringWriter();
		WriteableOutputStream stream = new WriteableOutputStream(s, resource.getEncoding());
		try {
			resource.save(stream, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return stream.asWriter().toString();
	}

}
