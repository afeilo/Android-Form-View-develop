package com.xiefei.mylibrary;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.support.v4.view.ViewCompat;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.Scroller;

import java.util.List;

/**
 * Created by xiefei-pc on 2015/12/24.
 */
public class FormView extends View{
    private static String Tag = "FormView";
    private float mLeftMenuWidth = 100;
    private float mLeftMenuHeight = 200;//自定义左边导航条
    private int mLeftMenuTextColor = Color.BLACK;;//自定义左菜单文字的颜色
    private float mLeftMenuTextSize = 20;
    private int  mLeftMenuTextPadding = 0;//自定义左菜单padding
    private float mHeaderMenuHeight = 100;//自定义head的高度
    private float mHeaderMenuWidth;//这个作废 是算出来的
    private int mHeaderBackgroundColor = Color.rgb(227, 227, 227);//自定义head的颜色
    private int mHeaderMenuTextColor=Color.BLACK;//自定义head文字的颜色
    private float mHeaderMenuTextSize = 20;
    private int mHeaderMenuTextChooseColor = Color.rgb(76,164,235);
    private int mHeaderMenuTextPadding = 0;
    private int mAllRowCount = 7;//总列个数
    private int mVisibleRowCount = 7;//可见列个数
    private int mColumnCount = 6;//行个数
    private int mRowDivideColor = Color.WHITE;
    private float mRowWidth = 10;
    private float mColumnWidth = 2;//行列间隙
    private int mColumnDivideColor = Color.rgb(230,230,230);
    private int mContentBackgroundColor = Color.rgb(245, 245, 245);//主布局颜色
    private int mContentBackgroundChooseColor = Color.argb(50,76,164,235);
    private int mContentTextColor = Color.WHITE;
    private int mContentTextPadding = 2;
    private float mContentTextSize = 50;
    private EventClickListener eventClickListener = null;
    private EmptyClickListener emptyClickListener = null;
    private Paint mLeftBackgroundTextPaint;
    private Paint mHeaderBackgroundPaint;
    private Paint mMainPaint;
    private Paint mDividePaint;
    private int mDivideColor = Color.WHITE;
    private int mLeftBackgroundColor = Color.LTGRAY;
    private DateInterpreter mDateInterpreter;
    private int chooseItem = 2;//选择的item
    private GestureDetector mGestureDetector;
    private ScaleGestureDetector mScaleGestureDetector;
    private Scroller mScroller;//定义滑动器
    private Boolean isFirst = true;//是否是第一次测量
    private Boolean canScroll = true;//是否左右允许滑动
    private List<FormEvent> events = null;
    private float mContentTextLineSpace = 1.5f;
    private float mLeftMenuMinHeight = -1;
    private float mLeftMenuMaxHeight = -1;
    private float mZoomHeight = -1;
    private PointF mCurrentOrigin = new PointF(0f, 0f);
    private float mContentHeight = -1;//只是用来表示主Content的高度
    private float mContentWidth = -1;
    private float mContentMarginTop = 15;
    private TextPaint mEventPaint;//绘制文字
    private Direction mScrollDirection = Direction.NONE;
    private Direction mFlintDirection = Direction.NONE;
    private Boolean isZoom = false;
    public FormView(Context context) {
        this(context,null);
    }

    public FormView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
        init();
    }

    public FormView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.FormView,0,0);
        mLeftMenuWidth = typedArray.getDimension(R.styleable.FormView_leftMenuWidth,mLeftMenuWidth);
        mLeftMenuHeight = typedArray.getDimension(R.styleable.FormView_leftMenuHeight,mLeftMenuHeight);
        mLeftBackgroundColor = typedArray.getColor(R.styleable.FormView_leftMenuBackgroundColor,mLeftBackgroundColor);
        mLeftMenuTextSize = typedArray.getDimension(R.styleable.FormView_leftMenuTextSize,mLeftMenuTextSize);
        mLeftMenuTextColor = typedArray.getColor(R.styleable.FormView_leftMenuTextColor,mLeftMenuTextColor);
        mHeaderMenuWidth = typedArray.getDimension(R.styleable.FormView_headerMenuWidth,mHeaderMenuWidth);
        mHeaderMenuHeight = typedArray.getDimension(R.styleable.FormView_headerMenuHeight,mHeaderMenuHeight);
        mHeaderBackgroundColor = typedArray.getColor(R.styleable.FormView_headerMenuBachgroundColor,mHeaderBackgroundColor);
        mHeaderMenuTextSize = typedArray.getDimension(R.styleable.FormView_headerMenuTextSize,mHeaderMenuTextSize);
        mHeaderMenuTextColor = typedArray.getColor(R.styleable.FormView_headerMenuTextColor,mHeaderMenuTextColor);
        mHeaderMenuTextChooseColor = typedArray.getColor(R.styleable.FormView_headerMenuTextChooseColor,mHeaderMenuTextChooseColor);
        mAllRowCount = typedArray.getInteger(R.styleable.FormView_allRowCount,mAllRowCount);
        mVisibleRowCount = typedArray.getInteger(R.styleable.FormView_visibleRowCount,mVisibleRowCount);
        mRowWidth = typedArray.getDimension(R.styleable.FormView_rowDivide,mRowWidth);
        mColumnWidth = typedArray.getDimension(R.styleable.FormView_columnDivide,mColumnWidth);
        mRowDivideColor = typedArray.getColor(R.styleable.FormView_rowDivideColor,mRowDivideColor);
        mColumnDivideColor = typedArray.getColor(R.styleable.FormView_columnDivideColor,mColumnDivideColor);
        mContentBackgroundColor = typedArray.getColor(R.styleable.FormView_contentBackgroundColor,mContentBackgroundColor);
        mContentBackgroundChooseColor = typedArray.getColor(R.styleable.FormView_contentBackgroundChooseColor,mContentBackgroundChooseColor);
        mContentTextColor = typedArray.getColor(R.styleable.FormView_contentTextColor,mContentTextColor);
        mContentTextSize = typedArray.getDimension(R.styleable.FormView_contentTextSize,mContentTextSize);
        canScroll = typedArray.getBoolean(R.styleable.FormView_canScroll,canScroll);
        mContentMarginTop = typedArray.getDimension(R.styleable.FormView_contentMarginTop,mContentMarginTop);
        mContentTextLineSpace = typedArray.getDimension(R.styleable.FormView_contentTextLineSpace,mContentTextLineSpace);
        typedArray.recycle();
        init();
    }
    private void init(){
        mHeaderBackgroundPaint = new Paint();
        mHeaderBackgroundPaint.setColor(mHeaderBackgroundColor);
        mLeftBackgroundTextPaint = new Paint();
        mLeftBackgroundTextPaint.setColor(mLeftBackgroundColor);
        mMainPaint = new Paint();
        mMainPaint.setColor(mContentBackgroundColor);
        mDividePaint = new Paint();
        mDividePaint.setColor(mDivideColor);
        mScroller = new Scroller(getContext());
        mZoomHeight = mLeftMenuHeight*mColumnCount;
        mEventPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG|Paint.LINEAR_TEXT_FLAG);
        mEventPaint.setStyle(Paint.Style.FILL);
        mEventPaint.setTextSize(mContentTextSize);
        mScaleGestureDetector = new ScaleGestureDetector(getContext(), new ScaleGestureDetector.OnScaleGestureListener() {
            @Override
            public boolean onScale(ScaleGestureDetector detector) {
                mLeftMenuHeight = (float) Math.round(mLeftMenuHeight *detector.getScaleFactor());
                if(mLeftMenuHeight>mLeftMenuMinHeight&&mLeftMenuHeight<mLeftMenuMaxHeight){
                }else {
                    if(mLeftMenuHeight<=mLeftMenuMinHeight)
                        mLeftMenuHeight = mLeftMenuMinHeight;
                    else
                        mLeftMenuHeight = mLeftMenuMaxHeight;
                }
                invalidate();
                return true;
            }

            @Override
            public boolean onScaleBegin(ScaleGestureDetector detector) {
                isZoom = true;
                return true;
            }

            @Override
            public void onScaleEnd(ScaleGestureDetector detector) {
                isZoom = false;
                mZoomHeight = mLeftMenuHeight*mColumnCount;
            }
        });
        mGestureDetector = new GestureDetector(getContext(), new GestureDetector.OnGestureListener() {
            @Override
            public boolean onDown(MotionEvent e) {
                return false;
            }

            @Override
            public void onShowPress(MotionEvent e) {

            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                if(e.getX()>mLeftMenuWidth&&e.getY()>mHeaderMenuHeight+mContentMarginTop){
                    if(eventClickListener!=null||emptyClickListener!=null){
                        int x = (int) ((e.getX()-mCurrentOrigin.x-mLeftMenuWidth)/mHeaderMenuWidth);
                        int y = (int) ((e.getY()-mCurrentOrigin.y-mHeaderMenuHeight-mContentMarginTop)/mLeftMenuHeight);
                        for(FormEvent formEvent:events){
                            if(x==formEvent.getX()&&y==formEvent.getY()){
                                if(eventClickListener!=null)
                                    eventClickListener.onEventClick(formEvent);
                                return false;
                            }
                        }
                        if(emptyClickListener!=null){
                            emptyClickListener.onEmptyEventClick(x,y);
                        }
                    }
                }
                return false;
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                if(!isZoom){
                    mScroller.forceFinished(true);
                    if(canScroll&&Math.abs(distanceX)>Math.abs(distanceY)){
                        mCurrentOrigin.x -= distanceX;
                        mCurrentOrigin.x = mCurrentOrigin.x%mContentWidth;
                        mScrollDirection = Direction.HORIZONTAL;
                    }else {
                        mCurrentOrigin.y-=distanceY;
                        if(mCurrentOrigin.y>0){
                            mCurrentOrigin.y = 0;
                        }
                        if(mCurrentOrigin.y<mContentHeight-mZoomHeight){
                            mCurrentOrigin.y = mContentHeight-mZoomHeight;
//                            mScrollDirection = Direction.VERTICAL;
                        }
                    }
                    ViewCompat.postInvalidateOnAnimation(FormView.this);
                    invalidate();
                    return true;
                }
                return false;
            }

            @Override
            public void onLongPress(MotionEvent e) {

            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                if(!isZoom){
                    if(canScroll&&Math.abs(velocityX)>Math.abs(velocityY)){
                        mFlintDirection = Direction.HORIZONTAL;
                        mScroller.fling((int) mCurrentOrigin.x, (int) mCurrentOrigin.y,(int)velocityX,0,Integer.MIN_VALUE, Integer.MAX_VALUE,(int) (mContentHeight-mZoomHeight),0);
                    }else {
                        mFlintDirection = Direction.VERTICAL;
                        mScroller.fling((int) mCurrentOrigin.x, (int) mCurrentOrigin.y,0,(int)velocityY,Integer.MIN_VALUE, Integer.MAX_VALUE, (int) (mContentHeight-mZoomHeight),0);
                    }
                    ViewCompat.postInvalidateOnAnimation(FormView.this);
                    return true;
                }
                return  false;
            }
        });
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(mLeftMenuMinHeight==-1){
            mContentHeight = (float) ((getHeight()-mHeaderMenuHeight-2*mHeaderMenuTextPadding-mContentMarginTop)*1.0);
            mLeftMenuMinHeight = (float) (((getHeight()-mHeaderMenuHeight-2*mHeaderMenuTextPadding)*1.0)/mColumnCount);
            mLeftMenuHeight = Math.max(mLeftMenuHeight,mLeftMenuMinHeight);
        }
        if(mLeftMenuMaxHeight == -1){
            mLeftMenuMaxHeight = mLeftMenuMinHeight*4;
        }
        if(mContentWidth == -1){
            mHeaderMenuWidth = (getWidth()- (mLeftMenuWidth + mLeftMenuTextPadding * 2))/mVisibleRowCount;
            mContentWidth = (int) (mHeaderMenuWidth*mAllRowCount);
        }
        if(isFirst){
            mCurrentOrigin.x = 0;
            if(mAllRowCount == mVisibleRowCount)
                canScroll = false;
            if(canScroll){
                mCurrentOrigin.x = -mHeaderMenuWidth*chooseItem;
            }
            isFirst = false;
        }
        mCurrentOrigin.x = mCurrentOrigin.x%mContentWidth;
        while(mCurrentOrigin.x>0){
            mCurrentOrigin.x-=mContentWidth;
        }
        // 画中间部分
        if(mCurrentOrigin.y>0){
            mCurrentOrigin.y=0;
        }
        canvas.drawRect(mLeftMenuWidth + mLeftMenuTextPadding * 2, mHeaderMenuHeight+mContentMarginTop + mHeaderMenuTextPadding * 2,getWidth(),getHeight(), mMainPaint);
        canvas.drawRect(0,0,getWidth(), mHeaderMenuHeight + mHeaderMenuTextPadding * 2, mHeaderBackgroundPaint);
        canvas.save();
        canvas.clipRect(mLeftMenuWidth+mLeftMenuTextPadding*2,mHeaderMenuHeight+mHeaderMenuTextPadding*2+mContentMarginTop,getWidth(),getHeight());
        drawLeftColumnAndAxes(canvas);
        drawHeaderRowAndEvents(canvas);
        canvas.restore();
        drawHeadText(canvas);
        drawLeftText(canvas);
        mHeaderBackgroundPaint.setColor(mHeaderBackgroundColor);
    }
    private void drawHeaderRowAndEvents(Canvas canvas){
        // Draw the background color for the header column.
        float leftSize = 0;//左边对其
        mDividePaint.setColor(mRowDivideColor);
        mEventPaint.setColor(mHeaderMenuTextColor);
        mEventPaint.setTextSize(mHeaderMenuTextSize);
        //划竖线
        for (int i = 0; i < mAllRowCount; i++) {
            leftSize = mLeftMenuWidth+mLeftMenuTextPadding * 2+(mHeaderMenuWidth)*i+mCurrentOrigin.x;
            while (leftSize<mLeftMenuWidth+mLeftMenuTextPadding*2){
                leftSize+=mContentWidth;
            }
            while (leftSize>getWidth()){
                leftSize -= mContentWidth;
            }
            canvas.drawRect(leftSize,mHeaderMenuHeight+mHeaderMenuTextPadding*2,leftSize+mRowWidth,getHeight(),mDividePaint);
        }
    }
    private void drawLeftText(Canvas canvas){
        canvas.save();
        canvas.clipRect(0,mHeaderMenuHeight+mHeaderMenuTextPadding*2+mContentMarginTop,getWidth(),getHeight());
        mEventPaint.setColor(mLeftMenuTextColor);
        mEventPaint.setTextSize(mLeftMenuTextSize);
        mDividePaint.setColor(mColumnDivideColor);
        for (int i = 0; i < mColumnCount; i++) {
            canvas.save();
            String text = i+"";
            if(mDateInterpreter!=null){
                text = mDateInterpreter.interpretLeftText(i);
            }
            StaticLayout staticLayout = new StaticLayout(text,mEventPaint,(int)mLeftMenuWidth, Layout.Alignment.ALIGN_CENTER,1.0f,0,false);
            canvas.translate(0,mCurrentOrigin.y+mHeaderMenuHeight+mContentMarginTop+mLeftMenuHeight*i);
//            canvas.drawText(text,mLeftMenuWidth/2-mEventPaint.measureText(text)/2,mCurrentOrigin.y+mHeaderMenuHeight+mLeftMenuHeight*i+mLeftMenuHeight/2-(mEventPaint.descent()+mEventPaint.ascent())/2,mEventPaint);
            staticLayout.draw(canvas);
            canvas.restore();
        }
        canvas.restore();
    }
    private void drawLeftColumnAndAxes(Canvas canvas){
        float heightSize = 0;//划横线
        mDividePaint.setColor(mColumnDivideColor);
        mEventPaint.setColor(mContentTextColor);
        mEventPaint.setTextSize(mContentTextSize);
        for (int i = 0; i < mColumnCount; i++) {
            heightSize = (mLeftMenuHeight)*i+mHeaderMenuHeight+mHeaderMenuTextPadding*2+mCurrentOrigin.y;
            canvas.drawRect(mLeftMenuWidth+mLeftMenuTextPadding*2,heightSize,getWidth(),heightSize+mColumnWidth,mDividePaint);
        }
        drawEventText(canvas);
    }
    private void drawEventText(Canvas canvas){
        canvas.save();
        canvas.clipRect(mLeftMenuWidth+mLeftMenuTextPadding*2,mHeaderMenuHeight+mHeaderMenuTextPadding*2+mContentMarginTop,getWidth(),getHeight());
        if(events!=null){
            for (int i = 0; i < events.size(); i++) {
                drawEventText(canvas,events.get(i));
            }
        }
        canvas.restore();
    }
    private void drawHeadText(Canvas canvas){
        canvas.save();
        canvas.clipRect(mLeftMenuWidth+mLeftMenuTextPadding*2,0,getWidth(),getHeight());
        for (int i = 0; i < mAllRowCount; i++) {
            String text = i+"";
            if(mDateInterpreter!=null){
                text = mDateInterpreter.interpretHeaderText(i);
            }
            float  textStartX = (mCurrentOrigin.x+mHeaderMenuWidth*i);
            if(textStartX>mContentWidth-mHeaderMenuWidth)
                textStartX%=mContentWidth;
            while (textStartX<-mHeaderMenuWidth){
                textStartX+=mContentWidth;
            }if(i == chooseItem){
                mEventPaint.setColor(mHeaderMenuTextChooseColor);
                canvas.drawText(text,textStartX+mHeaderMenuWidth/2+mLeftMenuWidth-mEventPaint.measureText(text)/2,mHeaderMenuHeight/2-(mEventPaint.descent()+mEventPaint.ascent())/2,mEventPaint);
                mEventPaint.setColor(mHeaderMenuTextColor);
                mMainPaint.setColor(mContentBackgroundChooseColor);
                canvas.drawRect(textStartX+mLeftMenuWidth+mRowWidth,mHeaderMenuHeight+mContentMarginTop,textStartX+mHeaderMenuWidth+mLeftMenuWidth,getHeight(),mMainPaint);
                mMainPaint.setColor(mContentBackgroundColor);
            }
            else {
                canvas.drawText(text,textStartX+mHeaderMenuWidth/2+mLeftMenuWidth-mEventPaint.measureText(text)/2,mHeaderMenuHeight/2-(mEventPaint.descent()+mEventPaint.ascent())/2,mEventPaint);
            }
        }canvas.restore();

    }
    private void drawEventText(Canvas canvas,FormEvent event){
        canvas.save();
        if(event.getX()>mAllRowCount||event.getY()>mColumnCount){
            throw new IndexOutOfBoundsException("");
        }
        float textStartY = mCurrentOrigin.y+event.getY()*(mLeftMenuHeight+mLeftMenuTextPadding*2)+mHeaderMenuHeight;
        float  textStartX = (mCurrentOrigin.x+event.getX()*mHeaderMenuWidth);
        if(textStartX>mContentWidth-mHeaderMenuWidth)
            textStartX%=mContentWidth;
        while (textStartX<-mHeaderMenuWidth){
            textStartX+=mContentWidth;
        }
        canvas.clipRect(textStartX+mLeftMenuWidth+mLeftMenuTextPadding+mRowWidth, textStartY+mColumnWidth,textStartX+mLeftMenuWidth+mLeftMenuTextPadding+mHeaderMenuWidth,textStartY+mLeftMenuHeight-mColumnWidth);
        canvas.drawColor(event.getBackgroundColor());
        canvas.translate(textStartX+mLeftMenuWidth+mRowWidth+mLeftMenuTextPadding+mContentTextPadding,textStartY+mColumnWidth+mContentTextPadding);
        StaticLayout staticLayout = new StaticLayout(event.getText(),mEventPaint,
                (int) ((int) mHeaderMenuWidth-2*mContentTextPadding-2*mRowWidth), Layout.Alignment.ALIGN_CENTER,mContentTextLineSpace,0,false);
        staticLayout.draw(canvas);
        canvas.restore();
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mScaleGestureDetector.onTouchEvent(event);
        mGestureDetector.onTouchEvent(event);
        switch (event.getAction()){
            case MotionEvent.ACTION_UP:
                if(mScrollDirection == Direction.HORIZONTAL&&mFlintDirection == Direction.NONE)
                    goToNearestOrigin();
                break;
        }
        return true;
    }
    private void goToNearestOrigin(){
        int nearestPoint = Math.round(mCurrentOrigin.x/(mHeaderMenuWidth+mHeaderMenuTextPadding*2));
        float scrollX = mCurrentOrigin.x-nearestPoint*(mHeaderMenuWidth+mHeaderMenuTextPadding*2);
        mScroller.forceFinished(true);
        mScroller.startScroll((int)mCurrentOrigin.x, (int)mCurrentOrigin.y,(int)-scrollX,0,50);
        mScrollDirection = mFlintDirection = Direction.NONE;

    }
    @Override
    public void computeScroll() {
        if(mScroller.isFinished()){
            if(mFlintDirection == Direction.HORIZONTAL){
                goToNearestOrigin();
            }
        }
        if(mScroller.computeScrollOffset()){
                mCurrentOrigin.y =  mScroller.getCurrY();
                mCurrentOrigin.x = mScroller.getCurrX();
        }
        ViewCompat.postInvalidateOnAnimation(this);
    }
    private enum Direction {
        NONE, HORIZONTAL, VERTICAL
    }

    public void setCanScroll(Boolean canScroll) {
        this.canScroll = canScroll;
    }

    public void setChooseItem(int chooseItem) {
        this.chooseItem = chooseItem;
    }

    public void setmRowWidth(float mRowWidth) {
        this.mRowWidth = mRowWidth;
    }

    public void setmColumnWidth(float mColumnWidth) {
        this.mColumnWidth = mColumnWidth;
    }

    public void setmVisibleRowCount(int mVisibleRowCount) {
        this.mVisibleRowCount = mVisibleRowCount;
        mContentWidth=-1;
        canScroll = true;
        isFirst = true;
    }

    public void setmAllRowCount(int mAllRowCount) {
        this.mAllRowCount = mAllRowCount;
    }

    public void setmColumnCount(int mColumnCount) {
        this.mColumnCount = mColumnCount;
    }

    public void setmDateInterpreter(DateInterpreter mDateInterpreter) {
        this.mDateInterpreter = mDateInterpreter;
    }

    public void setEvents(List<FormEvent> events) {
        this.events = events;
    }

    public void setmContentTextColor(int mContentTextColor) {
        this.mContentTextColor = mContentTextColor;
    }

    public void setmContentTextSize(float mContentTextSize) {
        this.mContentTextSize = mContentTextSize;
    }

    public void setmContentTextLineSpace(float mContentTextLineSpace) {
        this.mContentTextLineSpace = mContentTextLineSpace;
    }

    public void setEventClickListener(EventClickListener eventClickListener) {
        this.eventClickListener = eventClickListener;
    }

    public void setEmptyClickListener(EmptyClickListener emptyClickListener) {
        this.emptyClickListener = emptyClickListener;
    }

    public interface EventClickListener{
        void onEventClick(FormEvent event);
    }
    public interface EmptyClickListener{
        void onEmptyEventClick(int x, int y);
    }
}
