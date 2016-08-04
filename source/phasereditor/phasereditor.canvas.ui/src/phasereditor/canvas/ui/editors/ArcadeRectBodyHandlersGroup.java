// The MIT License (MIT)
//
// Copyright (c) 2015, 2016 Arian Fornaris
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

import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import phasereditor.canvas.core.RectArcadeBodyModel;
import phasereditor.canvas.ui.editors.ArcadeRectBodyResizeHandler.Corner;
import phasereditor.canvas.ui.editors.operations.ChangePropertyOperation;
import phasereditor.canvas.ui.editors.operations.CompositeOperation;
import phasereditor.canvas.ui.shapes.ISpriteNode;

/**
 * @author arian
 *
 */
public class ArcadeRectBodyHandlersGroup extends HandlersGroup {

	private Rectangle _resizeArcadeRectBody_area;
	private ArcadeBodyMoveHandler _moveArcadeRectBody_Center;

	public ArcadeRectBodyHandlersGroup(SelectionNode selnode) {
		super(selnode);

		_moveArcadeRectBody_Center = new ArcadeBodyMoveHandler(selnode);
		_resizeArcadeRectBody_area = new Rectangle();
		_resizeArcadeRectBody_area.setFill(Color.GREENYELLOW);
		_resizeArcadeRectBody_area.setMouseTransparent(true);
		_resizeArcadeRectBody_area.setOpacity(0.5);

		getChildren().setAll(_resizeArcadeRectBody_area, _moveArcadeRectBody_Center);

		for (Corner corner : Corner.values()) {
			getChildren().addAll(new ArcadeRectBodyResizeHandler(selnode, corner));
		}
	}

	@Override
	public void updateHandlers() {
		if (!(_selnode.getObjectNode() instanceof ISpriteNode)) {
			return;
		}

		ISpriteNode sprite = (ISpriteNode) _selnode.getObjectNode();

		if (!(sprite.getModel().getBody() instanceof RectArcadeBodyModel)) {
			return;
		}

		RectArcadeBodyModel body = (RectArcadeBodyModel) sprite.getModel().getBody();

		if (body == null) {
			return;
		}

		super.updateHandlers();

		double bodyX = body.getOffsetX();
		double bodyY = body.getOffsetY();
		double bodyW = body.getWidth();
		double bodyH = body.getHeight();

		if (bodyW == -1) {
			bodyW = sprite.getControl().getTextureWidth();
		}

		if (bodyH == -1) {
			bodyH = sprite.getControl().getTextureHeight();
		}

		{
			double scale = _selnode.getCanvas().getZoomBehavior().getScale();
			double scaleX = sprite.getModel().getScaleX();
			double scaleY = sprite.getModel().getScaleY();
			bodyX *= scale * scaleX;
			bodyY *= scale * scaleY;
			bodyW *= scale * scaleX;
			bodyH *= scale * scaleY;
		}

		double hs = SelectionNode.HANDLER_SIZE / 2;

		double left = bodyX;
		double top = bodyY;

		_moveArcadeRectBody_Center.relocate(bodyX + bodyW / 2 - hs, bodyY + bodyH / 2 - hs);
		_resizeArcadeRectBody_area.relocate(left, top);
		_resizeArcadeRectBody_area.setWidth(bodyW);
		_resizeArcadeRectBody_area.setHeight(bodyH);
	}

}

class ArcadeRectBodyResizeHandler extends DragHandlerNode {

	enum Corner {
		TOP_LEFT(1, 1, -1, -1),

		TOP_RIGHT(0, 1, 1, -1),

		BOTTOM_RIGHT(0, 0, 1, 1),

		BOTTOM_LEFT(1, 0, -1, 1),

		TOP(0, 1, 0, -1),

		RIGHT(0, 0, 1, 0),

		BOT(0, 0, 0, 1),

		LEFT(1, 0, -1, 0),

		;

		public double x;
		public double y;
		public double w;
		public double h;

		private Corner(double x, double y, double w, double h) {
			this.x = x;
			this.y = y;
			this.w = w;
			this.h = h;
		}
	}

	protected double _initX;
	protected double _initY;
	private double _initWidth;
	private double _initHeight;

	private Corner _corner;

	public ArcadeRectBodyResizeHandler(SelectionNode selnode, Corner corner) {
		super(selnode);
		_corner = corner;
	}

	@Override
	public void handleMousePressed(MouseEvent e) {
		super.handleMousePressed(e);
		ISpriteNode sprite = (ISpriteNode) getObjectNode();
		RectArcadeBodyModel body = (RectArcadeBodyModel) sprite.getModel().getBody();
		_initX = body.getOffsetX();
		_initY = body.getOffsetY();
		_initWidth = body.getWidth();
		_initHeight = body.getHeight();
	}

	@Override
	protected void handleDone() {
		CompositeOperation operations = new CompositeOperation();
		ISpriteNode sprite = (ISpriteNode) getObjectNode();
		RectArcadeBodyModel body = (RectArcadeBodyModel) sprite.getModel().getBody();
		double x = body.getOffsetX();
		double y = body.getOffsetY();
		double w = body.getWidth();
		double h = body.getHeight();
		body.setOffsetX(_initX);
		body.setOffsetX(_initY);
		body.setWidth(_initWidth);
		body.setHeight(_initHeight);

		String id = sprite.getModel().getId();

		operations.add(new ChangePropertyOperation<Number>(id, "body.offset.x", Double.valueOf(x)));
		operations.add(new ChangePropertyOperation<Number>(id, "body.offset.y", Double.valueOf(y)));
		operations.add(new ChangePropertyOperation<Number>(id, "body.width", Double.valueOf(w)));
		operations.add(new ChangePropertyOperation<Number>(id, "body.height", Double.valueOf(h)));

		getCanvas().getUpdateBehavior().executeOperations(operations);
	}

	@Override
	protected void handleDrag(double dx, double dy) {

		double dx2 = getModel().getScaleX() < 0 ? -dx : dx;
		double dy2 = getModel().getScaleY() < 0 ? -dy : dy;

		ISpriteNode sprite = (ISpriteNode) getObjectNode();
		RectArcadeBodyModel body = (RectArcadeBodyModel) sprite.getModel().getBody();

		SceneSettings settings = getCanvas().getSettingsModel();

		boolean stepping = settings.isEnableStepping();

		{
			double x = _initX;
			x += dx2 * _corner.x;
			int sw = settings.getStepWidth();

			if (stepping) {
				x = Math.round(x / sw) * sw;
			}

			body.setOffsetX(x);
		}

		{
			double y = _initY;
			y += dy2 * _corner.y;
			int sh = settings.getStepHeight();

			if (stepping) {
				y = Math.round(y / sh) * sh;
			}

			body.setOffsetY(y);
		}

		{
			double w;
			if (_initWidth == -1) {
				w = sprite.getControl().getTextureWidth();
			} else {
				w = _initWidth;
			}

			w += dx2 * _corner.w;
			int sw = settings.getStepWidth();

			if (stepping) {
				w = Math.round(w / sw) * sw;
			}

			body.setWidth(w);
		}

		double h;
		if (_initHeight == -1) {
			h = sprite.getControl().getTextureHeight();
		} else {
			h = _initHeight;
		}

		h += dy2 * _corner.h;
		int sh = settings.getStepHeight();

		if (stepping) {
			h = Math.round(h / sh) * sh;
		}

		body.setHeight(h);

		sprite.getControl().updateFromModel();
	}

	@Override
	protected void updateHandler() {
		ISpriteNode sprite = (ISpriteNode) _selnode.getObjectNode();

		RectArcadeBodyModel body = (RectArcadeBodyModel) sprite.getModel().getBody();

		if (body == null) {
			return;
		}

		double bodyX = body.getOffsetX();
		double bodyY = body.getOffsetY();
		double bodyW = body.getWidth();
		double bodyH = body.getHeight();

		if (bodyW == -1) {
			bodyW = sprite.getControl().getTextureWidth();
		}

		if (bodyH == -1) {
			bodyH = sprite.getControl().getTextureHeight();
		}

		{
			double scale = _selnode.getCanvas().getZoomBehavior().getScale();
			double scaleX = sprite.getModel().getScaleX();
			double scaleY = sprite.getModel().getScaleY();
			bodyX *= scale * scaleX;
			bodyY *= scale * scaleY;
			bodyW *= scale * scaleX;
			bodyH *= scale * scaleY;
		}

		double hs = SelectionNode.HANDLER_SIZE / 2;

		double left = bodyX;
		double right = bodyX + bodyW;
		double top = bodyY;
		double bot = bodyY + bodyH;
		double hor = bodyX + bodyW / 2;
		double ver = bodyY + bodyH / 2;

		switch (_corner) {
		case TOP_LEFT:
			relocate(left - hs, top - hs);
			setCursor(Cursor.NW_RESIZE);
			break;
		case TOP_RIGHT:
			relocate(right - hs, top - hs);
			setCursor(Cursor.NE_RESIZE);
			break;
		case BOTTOM_RIGHT:
			relocate(right - hs, bot - hs);
			setCursor(Cursor.SE_RESIZE);
			break;
		case BOTTOM_LEFT:
			relocate(left - hs, bot - hs);
			setCursor(Cursor.SW_RESIZE);
			break;
		case TOP:
			relocate(hor - hs, top - hs);
			setCursor(Cursor.N_RESIZE);
			break;
		case RIGHT:
			relocate(right - hs, ver - hs);
			setCursor(Cursor.E_RESIZE);
			break;
		case BOT:
			relocate(hor - hs, bot - hs);
			setCursor(Cursor.S_RESIZE);
			break;
		case LEFT:
			relocate(left - hs, ver - hs);
			setCursor(Cursor.W_RESIZE);
			break;
		default:
			break;
		}
	}
}