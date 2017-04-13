# 自定义`ViewGroup`
CalendarView代码

	public class CalendarView extends ViewGroup {


    public CalendarView(Context context) {
        super(context);
    }

    public CalendarView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }
	}

布局

  	<com.example.administrator.customcalendar.widget.CalendarView
        android:layout_width="200dp"
        android:layout_height="200dp"
        android:background="#3c3c3c"
        >

        <TextView
            android:gravity="center"
            android:layout_width="100dp"
            android:layout_height="100dp"
            android:background="#fff"
            android:text="0"/>
    </com.example.administrator.customcalendar.widget.CalendarView>

## 结果
![](http://i.imgur.com/9Pa7fCp.png)
在CalendarView中只是简单的继承于ViewGroup，没有`onlayout``childView`所以在视图中是看不到`childView`的 

# `childView`的位置应该由父控件告诉

	public class CalendarView extends ViewGroup {


    private View mView;

    public CalendarView(Context context) {
        super(context);
    }

    public CalendarView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
	//        左上角
        mView.layout(0,0,mView.getMeasuredWidth(),mView.getMeasuredHeight());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
	//        获得引用
        mView = getChildAt(0);
    }
	}
布局代码没变

## 结果
![](http://i.imgur.com/gPyYM1Q.png)
运行效果与上面的一样，这是因为即使`ViewGroup`为`childView`摆放好位置，但是，这里的`mView.getMeasuredWidth()`和`mView.getMeasuredHeight()`都为0。

## 修改`onLayout`的值
将

  	mView.layout(0,0,mView.getMeasuredWidth	-(),mView.getMeasuredHeight());
改为
	
	mView.layout(0,0,200,200);

布局代码没变
### 结果
![](http://i.imgur.com/1zoHnEX.png)
布局代码中我们写的文字是居中的，但是这里很明显没有居中，另外如果`childView`调用
`getMeasuredHeight`和`getMeasuredWidth`会发现依然为0。`ViewGrop`没有把我们在`xml`中设置给`childView`的宽高测量出来，导致我们在`xml`中配置的宽度失效，

# `ViewGroup`提供建议的测量值给`ChildeView`

public class CalendarView extends ViewGroup {


    private View mView;

    public CalendarView(Context context) {
        super(context);
    }

    public CalendarView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
	//        左上角
        mView.layout(0,0,mView.getMeasuredWidth(),mView.getMeasuredHeight());


    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	//        提供宽高参考
        measureChildren(widthMeasureSpec,heightMeasureSpec);
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
	//        获得引用
        mView = getChildAt(0);
    }
	}	

上面代码中，我们把`onlayout`方法修改为之前的`mView.layout(0,0,mView.getMeasuredWidth(),mView.getMeasuredHeight());`
的方法，`childView`有多宽多高，我们就给多少位置它，在`onMeasure`f方法中`measureChildren(widthMeasureSpec,heightMeasureSpec);` 提供宽高参考。

布局代码没变

##	结果
![](http://i.imgur.com/mzkhyPe.png)
`ViewGroup`提供宽高测量参考给`childView`,`childView`测根据提供的值，进行测量。中间文字也居中了。这个时候能获取到`childeView`的`MeasuredHeight`和`MeasuredWidth`

下面我们修改布局代码

 	<TextView
            android:gravity="center"
            android:layout_width="150dp"
            android:layout_height="100dp"
            android:background="#fff"
            android:text="0"/>
把宽度改为`150dp`
![](http://i.imgur.com/trtT2mY.png)
可以看到，现在能自适应了

如果我们把代码改为

 	<TextView
            android:gravity="center"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:background="#fff"
            android:text="0"/>
![](http://i.imgur.com/InaQYFz.png)
可以看到这效果也是我们想要的

如果我们把代码改为

	<TextView
            android:gravity="center"
            android:layout_width="250dp"
            android:layout_height="wrap_content"
            android:background="#fff"
            android:text="0"/>

这里的宽度已经大于父控件`CalendarView`(200dp)
![](http://i.imgur.com/j8bHcAN.png)
为了显示区别我在外面套多了一层颜色，可以看到`ViewGroup`是装不下`childView`

在上面的基础上，我们把
`mView.layout(0,0,mView.getMeasuredWidth(),mView.getMeasuredHeight());`改为`  mView.layout(0,0,getMeasuredWidth(),mView.getMeasuredHeight());`会不会有什么变化呢？
![](http://i.imgur.com/f7xLrYZ.png)
`onlayout`只是用于控件放的位置，它并不会压缩`childView`

从上面的结果可以看出
`measureChildren(widthMeasureSpec,heightMeasureSpec);`只是提供一个参考的范围给`childView`真正实际的大小由自己决定

# 作为`ViewGroup`有权利决定`childView`的宽高

	public class CalendarView extends ViewGroup {


    private View mView;

    public CalendarView(Context context) {
        super(context);
    }

    public CalendarView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
	//        左上角
        mView.layout(0,0,mView.getMeasuredWidth(),mView.getMeasuredHeight());

        Log.d("CalendarView", "mView.getMeasuredHeight():" + mView.getMeasuredHeight());
        Log.d("CalendarView", "mView.getMeasuredWidth():" + mView.getMeasuredWidth());

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	//        提供宽高参考
	//        measureChildren(widthMeasureSpec,heightMeasureSpec);
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int modeWidth = MeasureSpec.getMode(widthMeasureSpec);

        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
        int modeHeight = MeasureSpec.getMode(heightMeasureSpec);

        int childWidth=sizeWidth/2;
        int childHeight=sizeHeight/2;

        int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(childWidth, MeasureSpec.EXACTLY);
        int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(childHeight, MeasureSpec.EXACTLY);
        mView.measure(childWidthMeasureSpec,childHeightMeasureSpec);
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
	//        获得引用
        mView = getChildAt(0);
    }
	}

我们把`childView`的宽高限定死是`ViewGroup`的一半，所以无论
	
	  <TextView
            android:gravity="center"
            android:layout_width="10dp"
            android:layout_height="10dp"
            android:background="#fff"
            android:text="0"/>
还是
		
	  <TextView
            android:gravity="center"
            android:layout_width="250dp"
            android:layout_height="250dp"
            android:background="#fff"
            android:text="0"/>
都只会显示一半

# 结果
![](http://i.imgur.com/t6jaCC7.png)
获取`childView`的宽高值一些固定不变。