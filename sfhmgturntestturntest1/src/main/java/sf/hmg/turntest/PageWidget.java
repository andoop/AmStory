package sf.hmg.turntest;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.Region;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Scroller;

public class PageWidget extends View {

	// private static final String TAG = "hmg";
	private int mWidth = 320;
	private int mHeight = 480;
	private int mCornerX = 0; // 锟斤拷拽锟斤拷锟接︼拷锟揭筹拷锟�
	private int mCornerY = 0;
	private Path mPath0;// 路锟斤拷 锟斤拷前页
	private Path mPath1;// 路锟斤拷 锟斤拷一页
	private Bitmap mCurPageBitmap = null; // 锟斤拷前页
	private Bitmap mNextPageBitmap = null;

	private PointF mTouch = new PointF(); // 锟斤拷拽锟斤拷
	private PointF mBezierStart1 = new PointF(); // 锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟绞硷拷锟�
	private PointF mBezierControl1 = new PointF(); // 锟斤拷锟斤拷锟斤拷锟斤拷呖锟斤拷频锟�
	private PointF mBeziervertex1 = new PointF(); // 锟斤拷锟斤拷锟斤拷锟斤拷叨锟斤拷锟�
	private PointF mBezierEnd1 = new PointF(); // 锟斤拷锟斤拷锟斤拷锟斤拷呓锟斤拷锟斤拷

	private PointF mBezierStart2 = new PointF(); // 锟斤拷一锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟�
	private PointF mBezierControl2 = new PointF();
	private PointF mBeziervertex2 = new PointF();
	private PointF mBezierEnd2 = new PointF();

	float mMiddleX;
	float mMiddleY;
	float mDegrees;
	float mTouchToCornerDis;
	private ColorMatrixColorFilter mColorMatrixFilter;
	private Matrix mMatrix;
	float[] mMatrixArray = { 0, 0, 0, 0, 0, 0, 0, 0, 1.0f };

	boolean mIsRTandLB; // 锟角凤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷
	boolean nextPage;
	boolean sMove = false;
	private boolean isMove = false;
	private boolean midMove = false;
	float mMaxLength = (float) Math.hypot(mWidth, mHeight);
	int[] mBackShadowColors;
	int[] mFrontShadowColors;
	private GradientDrawable mBackShadowDrawableLR;
	private GradientDrawable mBackShadowDrawableRL;
	private GradientDrawable mFolderShadowDrawableLR;
	private GradientDrawable mFolderShadowDrawableRL;

	private GradientDrawable mFrontShadowDrawableHBT;
	private GradientDrawable mFrontShadowDrawableHTB;
	private GradientDrawable mFrontShadowDrawableVLR;
	private GradientDrawable mFrontShadowDrawableVRL;

	private Paint mPaint;

	private Scroller mScroller;

	public PageWidget(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		mPath0 = new Path();
		mPath1 = new Path();
		createDrawable();

		mPaint = new Paint();
		mPaint.setStyle(Paint.Style.FILL);

		ColorMatrix cm = new ColorMatrix();
		float array[] = { 0.55f, 0, 0, 0, 80.0f, 0, 0.55f, 0, 0, 80.0f, 0, 0,
				0.55f, 0, 80.0f, 0, 0, 0, 0.2f, 0 };
		cm.set(array);
		mColorMatrixFilter = new ColorMatrixColorFilter(cm);
		mMatrix = new Matrix();
		mScroller = new Scroller(getContext());

		mTouch.x = 0.01f; // 锟斤拷锟斤拷x,y为0,锟斤拷锟斤拷锟节碉拷锟斤拷锟绞憋拷锟斤拷锟斤拷锟斤拷锟�
		mTouch.y = 0.01f;
	}

	/**
	 * Author : hmg25 Version: 1.0 Description : 锟斤拷锟斤拷锟斤拷拽锟斤拷锟接︼拷锟斤拷锟阶э拷锟�
	 */

	public void calcCornerXY(float x, float y) {
		mCornerX = mWidth;
		if (x < mWidth / 2.0)
			nextPage = false;
		else
			nextPage = true;
		if (y <= mHeight / 2.0)
			mCornerY = 0;
		else
			mCornerY = mHeight;
		if ((mCornerX == 0 && mCornerY == mHeight)
				|| (mCornerX == mWidth && mCornerY == 0))
			mIsRTandLB = true;// 锟斤拷锟皆凤拷页
		else
			mIsRTandLB = false;// 锟斤拷锟斤拷页
		if (y > mHeight / 3.0 && y < mHeight * 2.0 / 3) {
			midMove = true;
		} else
			midMove = false;
	}

	public boolean doTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		if (event.getAction() == MotionEvent.ACTION_MOVE) {
			if (event.getPointerCount() == 1) {
				isMove = true;
				if (nextPage) {
					mTouch.x = event.getX();
					if (!midMove)
						mTouch.y = event.getY();
					else
						mTouch.y = Math.abs(mCornerY - 1f);
				} else {
					mTouch.x = 2 * event.getX() - mCornerX;
					if (!midMove)
						mTouch.y = 2 * event.getY() - mCornerY;
					else
						mTouch.y = Math.abs(mCornerY - 1f);
				}
			}
			this.postInvalidate();// 锟斤拷图
		}
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			sMove = false;
			if (nextPage) {
				mTouch.x = event.getX();
				if (!midMove)
					mTouch.y = event.getY();
				else
					mTouch.y = Math.abs(mCornerY - 1f);
			} else {
				mTouch.x = 2 * event.getX() - mCornerX;
				if (!midMove)
					mTouch.y = 2 * event.getY() - mCornerY;
				else
					mTouch.y = Math.abs(mCornerY - 1f);
			}
			// calcCornerXY(mTouch.x, mTouch.y);
			// this.postInvalidate();
		}
		if (event.getAction() == MotionEvent.ACTION_UP) {
			sMove = true;
			if (canDragOver(event.getX(), event.getY()) || isMove) {
				isMove = false;
				startAnimation(1200);
			} else {
				if (nextPage) {
					mTouch.x = -mCornerX + 0.09f;
					mTouch.y = Math.abs(mCornerY - 0.09f);
				} else {
					mTouch.x = mCornerX - 0.09f;
					mTouch.y = Math.abs(mCornerY - 0.09f);
				}
			}
			this.postInvalidate();
		}
		return true;
	}

	/**
	 * Author : hmg25 Version: 1.0 Description :
	 * 锟斤拷锟街憋拷锟絇1P2锟斤拷直锟斤拷P3P4锟侥斤拷锟斤拷锟斤拷锟�
	 */
	// 锟斤拷锟斤拷直锟竭的斤拷锟斤拷
	public PointF getCross(PointF P1, PointF P2, PointF P3, PointF P4) {
		PointF CrossP = new PointF();
		// 锟斤拷元锟斤拷锟斤拷通式锟斤拷 y=ax+b
		float a1 = (P2.y - P1.y) / (P2.x - P1.x);
		float b1 = ((P1.x * P2.y) - (P2.x * P1.y)) / (P1.x - P2.x);

		float a2 = (P4.y - P3.y) / (P4.x - P3.x);
		float b2 = ((P3.x * P4.y) - (P4.x * P3.y)) / (P3.x - P4.x);
		CrossP.x = (b2 - b1) / (a1 - a2);
		CrossP.y = a1 * CrossP.x + b1;
		return CrossP;
	}

	private void getControlAndStartPoint() {
		mMiddleX = (mTouch.x + mCornerX) / 2;
		mMiddleY = (mTouch.y + mCornerY) / 2;
		mBezierControl1.x = mMiddleX - (mCornerY - mMiddleY)
				* (mCornerY - mMiddleY) / (mCornerX - mMiddleX);
		mBezierControl1.y = mCornerY;
		mBezierControl2.x = mCornerX;
		mBezierControl2.y = mMiddleY - (mCornerX - mMiddleX)
				* (mCornerX - mMiddleX) / (mCornerY - mMiddleY);
		mBezierStart1.x = mBezierControl1.x - (mCornerX - mBezierControl1.x)
				/ 2;
		mBezierStart1.y = mCornerY;
	}

	// private void getControlAndStartPoint()
	// {
	// mMiddleX = (mTouch.x + mCornerX) / 2;
	// mMiddleY = (mTouch.y + mCornerY) / 2;
	// mTouchToCornerDis = (float) Math.hypot((mTouch.x - mCornerX),
	// (mTouch.y - mCornerY));
	// double degress = Math.atan2(Math.abs(mCornerY - mTouch.y), mCornerX
	// - mTouch.x);
	// mBezierControl1.x = mCornerX
	// - (float) (mTouchToCornerDis / 2 / Math.cos(degress));
	// mBezierControl1.y = mCornerY;
	// mBezierControl2.x = mCornerX;
	// mBezierControl2.y = Math.abs(mCornerY
	// - (float) (mTouchToCornerDis / 2 / Math.sin(degress)));
	// mBezierStart1.x = mBezierControl1.x - (mCornerX - mBezierControl1.x)
	// / 2;
	// mBezierStart1.y = mCornerY;
	// }

	// 锟矫碉拷锟斤拷锟斤拷锟斤拷锟斤拷叩亩锟斤拷悖拷锟斤拷频悖拷锟绞硷拷悖拷盏锟斤拷锟斤拷页锟斤拷锟斤拷
	private void calcPoints() {
		getControlAndStartPoint();
		// /////////
		// mMiddleX = (mTouch.x + mCornerX) / 2;
		// mMiddleY = (mTouch.y + mCornerY) / 2;
		// mBezierControl1.x = mMiddleX - (mCornerY - mMiddleY)
		// * (mCornerY - mMiddleY) / (mCornerX - mMiddleX);
		// mBezierControl1.y = mCornerY;
		// mBezierControl2.x = mCornerX;
		// mBezierControl2.y = mMiddleY - (mCornerX - mMiddleX)
		// * (mCornerX - mMiddleX) / (mCornerY - mMiddleY);
		// mBezierStart1.x = mBezierControl1.x - (mCornerX - mBezierControl1.x)
		// / 2;
		// mBezierStart1.y = mCornerY;

		// 锟斤拷mBezierStart1.x < 0锟斤拷锟斤拷mBezierStart1.x > 480时
		// 锟斤拷锟斤拷锟斤拷页锟斤拷锟斤拷锟斤拷锟紹UG锟斤拷锟节达拷锟斤拷锟斤拷
		if (!sMove) {
			if (mBezierStart1.x < 0 || mBezierStart1.x > mWidth) {
				if (mBezierStart1.x < 0)
					mBezierStart1.x = mWidth - mBezierStart1.x;

				float f1 = mCornerX - mTouch.x;
				float f2 = mWidth * f1 / mBezierStart1.x;
				mTouch.x = mCornerX - f2;

				float f3 = Math.abs(mCornerX - mTouch.x)
						* Math.abs(mCornerY - mTouch.y) / f1;
				mTouch.y = Math.abs(mCornerY - f3);

				// mMiddleX = (mTouch.x + mCornerX) / 2;
				// mMiddleY = (mTouch.y + mCornerY) / 2;
				//
				// mBezierControl1.x = mMiddleX - (mCornerY - mMiddleY)
				// * (mCornerY - mMiddleY) / (mCornerX - mMiddleX);
				// mBezierControl1.y = mCornerY;
				//
				// mBezierControl2.x = mCornerX;
				// mBezierControl2.y = mMiddleY - (mCornerX - mMiddleX)
				// * (mCornerX - mMiddleX) / (mCornerY - mMiddleY);
				// mBezierStart1.x = mBezierControl1.x
				// - (mCornerX - mBezierControl1.x) / 2;
				getControlAndStartPoint();
			}
		}
		mBezierStart2.x = mCornerX;
		mBezierStart2.y = mBezierControl2.y - (mCornerY - mBezierControl2.y)
				/ 2;

		mTouchToCornerDis = (float) Math.hypot((mTouch.x - mCornerX),
				(mTouch.y - mCornerY));

		mBezierEnd1 = getCross(mTouch, mBezierControl1, mBezierStart1,
				mBezierStart2);
		mBezierEnd2 = getCross(mTouch, mBezierControl2, mBezierStart1,
				mBezierStart2);
		mBeziervertex1.x = (mBezierStart1.x + 2 * mBezierControl1.x + mBezierEnd1.x) / 4;
		mBeziervertex1.y = (2 * mBezierControl1.y + mBezierStart1.y + mBezierEnd1.y) / 4;
		mBeziervertex2.x = (mBezierStart2.x + 2 * mBezierControl2.x + mBezierEnd2.x) / 4;
		mBeziervertex2.y = (2 * mBezierControl2.y + mBezierStart2.y + mBezierEnd2.y) / 4;
	}

	// 锟筋当前页锟斤拷示锟斤拷锟斤拷锟斤拷锟斤拷
	private void drawCurrentPageArea(Canvas canvas, Bitmap bitmap, Path path) {
		mPath0.reset();
		mPath0.moveTo(mBezierStart1.x, mBezierStart1.y);
		mPath0.quadTo(mBezierControl1.x, mBezierControl1.y, mBezierEnd1.x,
				mBezierEnd1.y);
		mPath0.lineTo(mTouch.x, mTouch.y);
		mPath0.lineTo(mBezierEnd2.x, mBezierEnd2.y);
		mPath0.quadTo(mBezierControl2.x, mBezierControl2.y, mBezierStart2.x,
				mBezierStart2.y);
		mPath0.lineTo(mCornerX, mCornerY);
		mPath0.close();

		canvas.save();
		canvas.clipPath(path, Region.Op.XOR);
		canvas.drawBitmap(bitmap, 0, 0, null);
		canvas.restore();
	}

	// 锟斤拷一页锟斤拷示锟斤拷锟街碉拷锟斤拷锟斤拷锟斤拷锟接�
	private void drawNextPageAreaAndShadow(Canvas canvas, Bitmap bitmap) {
		mPath1.reset();
		mPath1.moveTo(mBezierStart1.x, mBezierStart1.y);
		mPath1.lineTo(mBeziervertex1.x, mBeziervertex1.y);
		mPath1.lineTo(mBeziervertex2.x, mBeziervertex2.y);
		mPath1.lineTo(mBezierStart2.x, mBezierStart2.y);
		mPath1.lineTo(mCornerX, mCornerY);
		mPath1.close();

		mDegrees = (float) Math.toDegrees(Math.atan2(mBezierControl1.x
				- mCornerX, mBezierControl2.y - mCornerY));
		int leftx;
		int rightx;
		GradientDrawable mBackShadowDrawable;
		if (mIsRTandLB) {
			leftx = (int) (mBezierStart1.x);
			rightx = (int) (mBezierStart1.x + mTouchToCornerDis / 4);
			mBackShadowDrawable = mBackShadowDrawableLR;
		} else {
			leftx = (int) (mBezierStart1.x - mTouchToCornerDis / 4);
			rightx = (int) mBezierStart1.x;
			mBackShadowDrawable = mBackShadowDrawableRL;
		}
		canvas.save();
		canvas.clipPath(mPath0);
		canvas.clipPath(mPath1, Region.Op.INTERSECT);
		canvas.drawBitmap(bitmap, 0, 0, null);
		canvas.rotate(mDegrees, mBezierStart1.x, mBezierStart1.y);
		mBackShadowDrawable.setBounds(leftx, (int) mBezierStart1.y, rightx,
				(int) (mMaxLength + mBezierStart1.y));
		mBackShadowDrawable.draw(canvas);
		canvas.restore();
	}

	public void setBitmaps(Bitmap bm1, Bitmap bm2) {
		mCurPageBitmap = bm1;
		mNextPageBitmap = bm2;
	}

	public void setScreen(int w, int h) {
		mWidth = w;
		mHeight = h;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawColor(0xFFAAAAAA);
		calcPoints();
		drawCurrentPageArea(canvas, mCurPageBitmap, mPath0);
		drawNextPageAreaAndShadow(canvas, mNextPageBitmap);
		drawCurrentPageShadow(canvas);
		drawCurrentBackArea(canvas, mCurPageBitmap);
	}

	/**
	 * Author : hmg25 Version: 1.0 Description : 锟斤拷锟斤拷锟斤拷影锟斤拷GradientDrawable
	 */
	private void createDrawable() {
		int[] color = { 0x333333, 0xb0333333 };
		mFolderShadowDrawableRL = new GradientDrawable(
				GradientDrawable.Orientation.RIGHT_LEFT, color);
		mFolderShadowDrawableRL
				.setGradientType(GradientDrawable.LINEAR_GRADIENT);

		mFolderShadowDrawableLR = new GradientDrawable(
				GradientDrawable.Orientation.LEFT_RIGHT, color);
		mFolderShadowDrawableLR
				.setGradientType(GradientDrawable.LINEAR_GRADIENT);

		mBackShadowColors = new int[] { 0xff111111, 0x111111 };
		mBackShadowDrawableRL = new GradientDrawable(
				GradientDrawable.Orientation.RIGHT_LEFT, mBackShadowColors);
		mBackShadowDrawableRL.setGradientType(GradientDrawable.LINEAR_GRADIENT);

		mBackShadowDrawableLR = new GradientDrawable(
				GradientDrawable.Orientation.LEFT_RIGHT, mBackShadowColors);
		mBackShadowDrawableLR.setGradientType(GradientDrawable.LINEAR_GRADIENT);

		mFrontShadowColors = new int[] { 0x80111111, 0x111111 };
		mFrontShadowDrawableVLR = new GradientDrawable(
				GradientDrawable.Orientation.LEFT_RIGHT, mFrontShadowColors);
		mFrontShadowDrawableVLR
				.setGradientType(GradientDrawable.LINEAR_GRADIENT);
		mFrontShadowDrawableVRL = new GradientDrawable(
				GradientDrawable.Orientation.RIGHT_LEFT, mFrontShadowColors);
		mFrontShadowDrawableVRL
				.setGradientType(GradientDrawable.LINEAR_GRADIENT);

		mFrontShadowDrawableHTB = new GradientDrawable(
				GradientDrawable.Orientation.TOP_BOTTOM, mFrontShadowColors);
		mFrontShadowDrawableHTB
				.setGradientType(GradientDrawable.LINEAR_GRADIENT);

		mFrontShadowDrawableHBT = new GradientDrawable(
				GradientDrawable.Orientation.BOTTOM_TOP, mFrontShadowColors);
		mFrontShadowDrawableHBT
				.setGradientType(GradientDrawable.LINEAR_GRADIENT);
	}

	/**
	 * Author : hmg25 Version: 1.0 Description : 锟斤拷锟狡凤拷锟斤拷页锟斤拷锟斤拷影
	 */
	public void drawCurrentPageShadow(Canvas canvas) {
		double degree;
		if (mIsRTandLB) {
			degree = Math.PI
					/ 4
					- Math.atan2(mBezierControl1.y - mTouch.y, mTouch.x
							- mBezierControl1.x);
		} else {
			degree = Math.PI
					/ 4
					- Math.atan2(mTouch.y - mBezierControl1.y, mTouch.x
							- mBezierControl1.x);
		}
		// 锟斤拷锟斤拷页锟斤拷影锟斤拷锟斤拷锟斤拷touch锟斤拷木锟斤拷锟�
		double d1 = (float) 25 * 1.414 * Math.cos(degree);
		double d2 = (float) 25 * 1.414 * Math.sin(degree);
		float x = (float) (mTouch.x + d1);
		float y;
		if (mIsRTandLB) {
			y = (float) (mTouch.y + d2);
		} else {
			y = (float) (mTouch.y - d2);
		}
		mPath1.reset();
		mPath1.moveTo(x, y);
		mPath1.lineTo(mTouch.x, mTouch.y);
		mPath1.lineTo(mBezierControl1.x, mBezierControl1.y);
		mPath1.lineTo(mBezierStart1.x, mBezierStart1.y);
		mPath1.close();
		float rotateDegrees;
		canvas.save();

		canvas.clipPath(mPath0, Region.Op.XOR);
		canvas.clipPath(mPath1, Region.Op.INTERSECT);
		int leftx;
		int rightx;
		GradientDrawable mCurrentPageShadow;
		if (mIsRTandLB) {
			leftx = (int) (mBezierControl1.x);
			rightx = (int) mBezierControl1.x + 25;
			mCurrentPageShadow = mFrontShadowDrawableVLR;
		} else {
			leftx = (int) (mBezierControl1.x - 25);
			rightx = (int) mBezierControl1.x + 1;
			mCurrentPageShadow = mFrontShadowDrawableVRL;
		}

		rotateDegrees = (float) Math.toDegrees(Math.atan2(mTouch.x
				- mBezierControl1.x, mBezierControl1.y - mTouch.y));
		canvas.rotate(rotateDegrees, mBezierControl1.x, mBezierControl1.y);
		mCurrentPageShadow.setBounds(leftx,
				(int) (mBezierControl1.y - mMaxLength), rightx,
				(int) (mBezierControl1.y));
		mCurrentPageShadow.draw(canvas);
		canvas.restore();
		mPath1.reset();
		mPath1.moveTo(x, y);
		mPath1.lineTo(mTouch.x, mTouch.y);
		mPath1.lineTo(mBezierControl2.x, mBezierControl2.y);
		mPath1.lineTo(mBezierStart2.x, mBezierStart2.y);
		mPath1.close();
		canvas.save();
		canvas.clipPath(mPath0, Region.Op.XOR);
		canvas.clipPath(mPath1, Region.Op.INTERSECT);
		if (mIsRTandLB) {
			leftx = (int) (mBezierControl2.y);
			rightx = (int) (mBezierControl2.y + 25);
			mCurrentPageShadow = mFrontShadowDrawableHTB;
		} else {
			leftx = (int) (mBezierControl2.y - 25);
			rightx = (int) (mBezierControl2.y + 1);
			mCurrentPageShadow = mFrontShadowDrawableHBT;
		}
		rotateDegrees = (float) Math.toDegrees(Math.atan2(mBezierControl2.y
				- mTouch.y, mBezierControl2.x - mTouch.x));
		canvas.rotate(rotateDegrees, mBezierControl2.x, mBezierControl2.y);
		float temp;
		if (mBezierControl2.y < 0)
			temp = mBezierControl2.y - mHeight;
		else
			temp = mBezierControl2.y;

		int hmg = (int) Math.hypot(mBezierControl2.x, temp);
		if (hmg > mMaxLength)
			mCurrentPageShadow
					.setBounds((int) (mBezierControl2.x - 25) - hmg, leftx,
							(int) (mBezierControl2.x + mMaxLength) - hmg,
							rightx);
		else
			mCurrentPageShadow.setBounds(
					(int) (mBezierControl2.x - mMaxLength), leftx,
					(int) (mBezierControl2.x), rightx);
		mCurrentPageShadow.draw(canvas);
		canvas.restore();
		// if(!sMove)
		// {
		// Log.i("hmg", "mTouchX --> " + mTouch.x + "  mTouchY-->  "
		// + mTouch.y);
		// Log.i("hmg", "mBezierControl1.x--  " + mBezierControl1.x
		// + "  mBezierControl1.y -- " + mBezierControl1.y);
		// Log.i("hmg", "mBezierControl2.x -- " + mBezierControl2.x
		// + "  mBezierControl2.y -- " + mBezierControl2.y);
		// }
	}

	/**
	 * Author : hmg25 Version: 1.0 Description : 锟斤拷锟狡凤拷锟斤拷页锟斤拷锟斤拷
	 */
	private void drawCurrentBackArea(Canvas canvas, Bitmap bitmap) {
		int i = (int) (mBezierStart1.x + mBezierControl1.x) / 2;
		float f1 = Math.abs(i - mBezierControl1.x);
		int i1 = (int) (mBezierStart2.y + mBezierControl2.y) / 2;
		float f2 = Math.abs(i1 - mBezierControl2.y);
		float f3 = Math.min(f1, f2);
		mPath1.reset();
		mPath1.moveTo(mBeziervertex2.x, mBeziervertex2.y);
		mPath1.lineTo(mBeziervertex1.x, mBeziervertex1.y);
		mPath1.lineTo(mBezierEnd1.x, mBezierEnd1.y);
		mPath1.lineTo(mTouch.x, mTouch.y);
		mPath1.lineTo(mBezierEnd2.x, mBezierEnd2.y);
		mPath1.close();
		GradientDrawable mFolderShadowDrawable;
		int left;
		int right;
		if (mIsRTandLB) {
			left = (int) (mBezierStart1.x - 1);
			right = (int) (mBezierStart1.x + f3 + 1);
			mFolderShadowDrawable = mFolderShadowDrawableLR;
		} else {
			left = (int) (mBezierStart1.x - f3 - 1);
			right = (int) (mBezierStart1.x + 1);
			mFolderShadowDrawable = mFolderShadowDrawableRL;
		}
		canvas.save();
		canvas.clipPath(mPath0);
		canvas.clipPath(mPath1, Region.Op.INTERSECT);

		mPaint.setColorFilter(mColorMatrixFilter);

		float dis = (float) Math.hypot(mCornerX - mBezierControl1.x,
				mBezierControl2.y - mCornerY);
		float f8 = (mCornerX - mBezierControl1.x) / dis;
		float f9 = (mBezierControl2.y - mCornerY) / dis;
		mMatrixArray[0] = 1 - 2 * f9 * f9;
		mMatrixArray[1] = 2 * f8 * f9;
		mMatrixArray[3] = mMatrixArray[1];
		mMatrixArray[4] = 1 - 2 * f8 * f8;
		mMatrix.reset();
		mMatrix.setValues(mMatrixArray);
		mMatrix.preTranslate(-mBezierControl1.x, -mBezierControl1.y);
		mMatrix.postTranslate(mBezierControl1.x, mBezierControl1.y);
		canvas.drawBitmap(bitmap, mMatrix, mPaint);
		// canvas.drawBitmap(bitmap, mMatrix, null);
		mPaint.setColorFilter(null);
		canvas.rotate(mDegrees, mBezierStart1.x, mBezierStart1.y);
		mFolderShadowDrawable.setBounds(left, (int) mBezierStart1.y, right,
				(int) (mBezierStart1.y + mMaxLength));
		mFolderShadowDrawable.draw(canvas);
		canvas.restore();
	}

	public void computeScroll() {
		super.computeScroll();
		if (mScroller.computeScrollOffset()) {
			float x = mScroller.getCurrX();
			float y = mScroller.getCurrY();
			mTouch.x = x;
			mTouch.y = y + 0.9f;
			postInvalidate();
		}
	}

	private void startAnimation(int delayMillis) {
		int dx, dy;
		// dx 水平锟斤拷锟津滑讹拷锟侥撅拷锟诫，锟斤拷值锟斤拷使锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟�
		// dy 锟斤拷直锟斤拷锟津滑讹拷锟侥撅拷锟诫，锟斤拷值锟斤拷使锟斤拷锟斤拷锟斤拷锟较癸拷锟斤拷
		if (nextPage) {
			dx = -(int) (mWidth + mTouch.x - 1);
		} else {
			dx = (int) (mWidth - mTouch.x - 1);
		}

		if (mCornerY > 0) {
			dy = (int) (mHeight - mTouch.y - 1);
		} else {
			dy = (int) (1 - mTouch.y); // 锟斤拷止mTouch.y锟斤拷锟秸憋拷为0
		}
		mScroller.startScroll((int) mTouch.x, (int) mTouch.y, dx, dy,
				delayMillis);
	}

	public void abortAnimation() {
		if (!mScroller.isFinished()) {
			mScroller.abortAnimation();
		}
	}

	public boolean canDragOver(float x, float y) {
		RectF LT = new RectF(0f, 0f, 30f, 30f);// 锟斤拷锟斤拷
		RectF RT = new RectF(mWidth - 30f, 0f, mWidth, 30f);// 锟斤拷锟斤拷
		RectF LB = new RectF(0f, mHeight - 30f, 30f, mHeight);// 锟斤拷锟斤拷
		RectF RB = new RectF(mWidth - 30f, mHeight - 30f, mWidth, mHeight);// 锟斤拷锟斤拷
		if (LT.contains(x, y) || RT.contains(x, y) || LB.contains(x, y)
				|| RB.contains(x, y))
			return false;
		return true;
	}

	// 锟斤拷锟揭凤拷
	public boolean DragToRight() {
		if (nextPage)
			return false;
		return true;
	}

}
