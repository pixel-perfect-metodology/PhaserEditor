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
package phasereditor.canvas.core;

import org.json.JSONObject;

/**
 * @author arian
 *
 */
public class MissingPrefabModel extends BaseObjectModel {
	private JSONObject _srcData;

	public MissingPrefabModel(GroupModel parent, JSONObject obj) {
		super(parent, "<missing-asset>");
		_srcData = obj;
		readInfo(_srcData.getJSONObject("info"));
	}

	public JSONObject getSrcData() {
		return _srcData;
	}

	@Override
	public void build() {
		// nothing
	}

	@Override
	public void write(JSONObject obj, boolean saving) {
		for (String k : _srcData.keySet()) {
			obj.put(k, _srcData.get(k));
		}
	}

	@Override
	public boolean hasErrors() {
		return true;
	}
}
