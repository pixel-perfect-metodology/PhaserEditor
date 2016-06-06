// The MIT License (MIT)
//
// Copyright (c) 2016 Arian Fornaris
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

import phasereditor.assetpack.core.AssetModel;
import phasereditor.assetpack.core.AssetPackCore;
import phasereditor.assetpack.core.AssetType;
import phasereditor.assetpack.core.IAssetElementModel;
import phasereditor.assetpack.core.IAssetKey;
import phasereditor.assetpack.core.ImageAssetModel;

/**
 * @author arian
 *
 */
public class AssetSpriteModel<T extends IAssetKey> extends BaseSpriteModel {
	private T _assetKey;

	public AssetSpriteModel(GroupModel parent, String typeName, JSONObject obj) {
		super(parent, typeName, obj);
	}

	public AssetSpriteModel(GroupModel parent, T assetKey, String typeName) {
		super(parent, typeName);
		_assetKey = assetKey;
		setEditorName(assetKey.getAsset().getKey());
	}

	public T getAssetKey() {
		return _assetKey;
	}

	public void setAssetKey(T assetKey) {
		_assetKey = assetKey;
	}

	public AssetType getAssetType() {
		return _assetKey.getAsset().getType();
	}

	@Override
	protected void writeMetadata(JSONObject obj) {
		super.writeMetadata(obj);
		// TODO: change it to use the UUID of asset packs!
		IAssetKey key = _assetKey;

		// if the key is an image frame, then we save only the refs to the image
		// asset.
		if (key instanceof ImageAssetModel.Frame) {
			key = key.getAsset();
		}

		obj.put("asset-ref", AssetPackCore.getAssetJSONReference(key));
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void readMetadata(JSONObject obj) {
		super.readMetadata(obj);

		IAssetKey asset = findAsset(obj);

		// what we really need is to get the frame of the image.
		if (asset instanceof ImageAssetModel) {
			_assetKey = (T) ((ImageAssetModel) asset).getFrame();
		} else {
			_assetKey = (T) asset;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see phasereditor.canvas.core.BaseObjectModel#rebuild()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void rebuild() {
		if (_assetKey == null) {
			// nothing to do
			return;
		}

		AssetModel asset = _assetKey.getAsset();
		if (!asset.isOnWorkspace()) {
			// the asset was deleted! set the key to null.
			_assetKey = null;
			return;
		}

		// if the asset key is a sub-element then reconnect with it
		if (_assetKey instanceof IAssetElementModel) {
			for (IAssetElementModel elem : asset.getSubElements()) {
				if (elem.getKey().equals(_assetKey.getKey())) {
					_assetKey = (T) elem;
					return;
				}
			}
			// elem not found, set it to null
			_assetKey = null;
		}
	}
}
