// Generated by Phaser Editor v1.4.0 (Phaser v2.6.2)
var __extends = (this && this.__extends) || (function () {
    var extendStatics = Object.setPrototypeOf ||
        ({ __proto__: [] } instanceof Array && function (d, b) { d.__proto__ = b; }) ||
        function (d, b) { for (var p in b) if (b.hasOwnProperty(p)) d[p] = b[p]; };
    return function (d, b) {
        extendStatics(d, b);
        function __() { this.constructor = d; }
        d.prototype = b === null ? Object.create(b) : (__.prototype = b.prototype, new __());
    };
})();
/**
 * Coin.
 * @param aGame A reference to the currently running game.
 * @param aX The x coordinate (in world space) to position the Sprite at.
 * @param aY The y coordinate (in world space) to position the Sprite at.
 * @param aKey This is the image or texture used by the Sprite during rendering. It can be a string which is a reference to the Cache entry, or an instance of a RenderTexture or PIXI.Texture.
 * @param aFrame If this Sprite is using part of a sprite sheet or texture atlas you can specify the exact frame to use by giving a string or numeric index.
 */
var Coin = (function (_super) {
    __extends(Coin, _super);
    function Coin(aGame, aX, aY, aKey, aFrame) {
        var _this = _super.call(this, aGame, aX, aY, aKey === undefined ? 'coins' : aKey, aFrame === undefined ? 1 : aFrame) || this;
        _this.scale.setTo(0.5, 0.5);
        var _anim_rotate = _this.animations.add('rotate', [0, 1, 2, 3, 4, 5], 5, true);
        // public fields
        _this.fCoins = _this;
        _this.fAnim_rotate = _anim_rotate;
        // my code after objects creation
        _this.afterCreate();
        return _this;
    }
    /* sprite-methods-begin */
    Coin.prototype.afterCreate = function () {
        this.fAnim_rotate.play();
    };
    return Coin;
}(Phaser.Sprite));
/* --- end generated code --- */
// -- user code here --