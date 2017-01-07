// The MIT License (MIT)
//
// Copyright (c) 2015, 2017 Arian Fornaris
//
// Permission is hereby granted, free of charge, to any person obtaining a
// copy of this software and associated documentation files (the
// "Software"), to deal in the Software without restriction, including
// without limitation the rights to use, copy, modify, merge, publish,
// distribute, sublicense, and/or sell copies of the Software, and to permit
// persons to whom the Software is furnished to do so, subject to the
// following conditions: The above copyright notice and this permission
// notice shall be included in all copies or substantial portions of the
// Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS
// OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
// MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN
// NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
// DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
// OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE
// USE OR OTHER DEALINGS IN THE SOFTWARE.
package phasereditor.canvas.ui.editors;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

import phasereditor.canvas.core.SceneSettings;

/**
 * @author arian
 *
 */
public class CanvasSettingsComp extends Composite implements ISettingsPage {

	private GeneralEditorSettingsComp _generalEditorSettingsComp;
	private SceneSettings _model;

	/**
	 * Create the composite.
	 * 
	 * @param parent
	 * @param style
	 */
	public CanvasSettingsComp(Composite parent, int style) {
		super(parent, style);
		setLayout(new GridLayout(1, false));

		_generalEditorSettingsComp = new GeneralEditorSettingsComp(this, SWT.NONE);
		_generalEditorSettingsComp.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));

		afterCreateWidgets();
	}

	private void afterCreateWidgets() {
		//
	}

	@Override
	public void setModel(SceneSettings model) {
		_model = model;
		_generalEditorSettingsComp.setModel(model);
	}

	public SceneSettings getModel() {
		return _model;
	}

	@Override
	protected void checkSubclass() {
		//
	}
}