package sing.widget;

import android.animation.LayoutTransition;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.transition.TransitionManager;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.ContextCompat;

import java.util.Timer;
import java.util.TimerTask;

import sing.searchlayuot.R;

/**
 * @author: LiangYX
 * @ClassName: SearchLayout
 * @date: 2017/7/10 下午2:29
 * @Description: 自定义搜索框
 */
public class SearchLayout extends LinearLayout {

    private int searchTextColor;// 搜索框文字的颜色
    private int searchTextSize;// 搜索框文字的大小
    private int searchTextColorHint;// 搜索框提示文字的颜色
    private int searchButtonColor;// 按钮文字的颜色
    private int searchButtonTextSize;// 按钮文字的大小
    private int searchBackgroundColor;//最外层父控件的背景颜色
    private Drawable searchTextBackground;//搜索框的背景
    private Drawable searchDrawableLeftIcon;// 左边的图标
    private int searchDrawableLeftIconSize = 0;// 左边的图标大小,默认为0，此时大小为 searchHeight/3*2
    private int searchDrawablePadding;// 左边的图标的距离
    private String searchHint = "搜索";// 提示文字
    private boolean searchSingleLine;// 搜索框是否单行显示
    private String searchButtonEmptyTxt = "取消";// 搜索按钮为空字符
    private String searchButtonTxt = "搜索";// 搜索按钮为空字符
    private int searchPadding;// 搜索框距离最外层的距离,若有值，以下4个属性无效
    private int searchPaddingLeft;
    private int searchPaddingTop;
    private int searchPaddingRight;
    private int searchPaddingBottom;
    private int searchContentPadding;// 搜索框距离内层的距离,若有值，以下4个属性无效
    private int searchContentPaddingLeft;
    private int searchContentPaddingTop;
    private int searchContentPaddingRight;
    private int searchContentPaddingBottom;
    private int searchHeight;// 搜索框高度
    private int searchImeOption;// 键盘的选项
    private int searchButtonWidth;// 按钮宽度

    public SearchLayout(Context context) {
        this(context, null);
    }

    public SearchLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SearchLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttr(context, attrs, defStyleAttr);
    }

    private ConstraintLayout constraintLayout;
    private EditText et;// 输入框
    private Button bt;// 搜索按钮
    private ConstraintSet constraintSet;
    private LinearLayout parent;// 最外层父控件
    private ImageView ivClear;// 清空

    private void initAttr(Context context, AttributeSet attrs, int defStyleAttr) {
        LinearLayout.inflate(context, R.layout.search_layout, this);

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.SearchLayout, defStyleAttr, 0);

        searchTextBackground = array.getDrawable(R.styleable.SearchLayout_search_text_background);
        searchDrawableLeftIcon = array.getDrawable(R.styleable.SearchLayout_search_drawable_left_icon);
        searchDrawableLeftIconSize = array.getDimensionPixelSize(R.styleable.SearchLayout_search_drawable_left_icon_size,0);
        searchBackgroundColor = array.getColor(R.styleable.SearchLayout_search_background_color, Color.parseColor("#f0eff5"));
        searchPadding = array.getDimensionPixelSize(R.styleable.SearchLayout_search_padding, -1);
        searchPaddingLeft = array.getDimensionPixelSize(R.styleable.SearchLayout_search_padding_left, 16);
        searchPaddingTop = array.getDimensionPixelSize(R.styleable.SearchLayout_search_padding_top, 16);
        searchPaddingRight = array.getDimensionPixelSize(R.styleable.SearchLayout_search_padding_right, 16);
        searchPaddingBottom = array.getDimensionPixelSize(R.styleable.SearchLayout_search_padding_bottom, 16);
        searchContentPadding = array.getDimensionPixelSize(R.styleable.SearchLayout_search_content_padding, -1);
        searchContentPaddingLeft = array.getDimensionPixelSize(R.styleable.SearchLayout_search_content_padding_left, 10);
        searchContentPaddingTop = array.getDimensionPixelSize(R.styleable.SearchLayout_search_content_padding_top, 10);
        searchContentPaddingRight = array.getDimensionPixelSize(R.styleable.SearchLayout_search_content_padding_right, 10);
        searchContentPaddingBottom = array.getDimensionPixelSize(R.styleable.SearchLayout_search_content_padding_bottom, 10);
        searchTextColor = array.getColor(R.styleable.SearchLayout_search_text_color, Color.parseColor("#313131"));
        searchTextSize = array.getDimensionPixelSize(R.styleable.SearchLayout_search_text_size, 28);
        searchTextColorHint = array.getColor(R.styleable.SearchLayout_search_text_color_hint, Color.parseColor("#9a9a9c"));
        searchSingleLine = array.getBoolean(R.styleable.SearchLayout_search_single_line, true);
        searchHint = array.getString(R.styleable.SearchLayout_search_hint);
        searchDrawablePadding = array.getDimensionPixelSize(R.styleable.SearchLayout_search_drawable_padding, 10);
        searchHeight = array.getDimensionPixelSize(R.styleable.SearchLayout_search_height, 70);
        searchButtonColor = array.getColor(R.styleable.SearchLayout_search_button_color, Color.parseColor("#313131"));
        searchButtonTextSize = array.getDimensionPixelSize(R.styleable.SearchLayout_search_button_text_size, 28);
        searchButtonEmptyTxt = array.getString(R.styleable.SearchLayout_search_button_empty_txt);
        searchButtonTxt = array.getString(R.styleable.SearchLayout_search_button_txt);
        searchImeOption = array.getInt(R.styleable.SearchLayout_search_imeOption, -1);
        searchButtonWidth = array.getDimensionPixelSize(R.styleable.SearchLayout_search_button_width, 90);

        if (searchTextBackground == null)
            searchTextBackground = ContextCompat.getDrawable(context, R.drawable.bg_search);
        if (searchDrawableLeftIcon == null)
            searchDrawableLeftIcon = ContextCompat.getDrawable(context, R.drawable.ic_search);
        if (TextUtils.isEmpty(searchHint))
            searchHint = "搜索";
        if (TextUtils.isEmpty(searchButtonEmptyTxt))
            searchButtonEmptyTxt = "取消";
        if (TextUtils.isEmpty(searchButtonTxt))
            searchButtonTxt = "搜索";

        array.recycle();

        initView();
    }

    private void initView() {
        parent = findViewById(R.id.parent);
        et = findViewById(R.id.et);

        constraintLayout = findViewById(R.id.constraint_layout);
        bt = findViewById(R.id.bt);
        ivClear = findViewById(R.id.iv_clear);

        setValue();
    }

    private void setValue() {
        et.setTextColor(searchTextColor);
        et.setTextSize(TypedValue.COMPLEX_UNIT_PX,searchTextSize);
        et.setHintTextColor(searchTextColorHint);
        et.setSingleLine(searchSingleLine);
        et.setHint(searchHint);
        if (searchImeOption != -1){
            et.setImeOptions(searchImeOption);
        }
        if (searchDrawableLeftIconSize == 0){
            searchDrawableLeftIcon.setBounds(0, 0, searchHeight/3*2, searchHeight/3*2);
        }else{
            searchDrawableLeftIcon.setBounds(0, 0, searchDrawableLeftIconSize, searchDrawableLeftIconSize);
        }
        et.setCompoundDrawablePadding(searchDrawablePadding);
        et.setCompoundDrawables(searchDrawableLeftIcon, null, null, null);

        constraintLayout.setBackground(searchTextBackground);
        constraintLayout.getLayoutParams().height = searchHeight;

        bt.setTextColor(searchButtonColor);
        bt.setTextSize(TypedValue.COMPLEX_UNIT_PX,searchButtonTextSize);
        bt.getLayoutParams().width = searchButtonWidth;

        parent.setBackgroundColor(searchBackgroundColor);
        if (searchPadding != -1){
            parent.setPadding(searchPadding, searchPadding, searchPadding, searchPadding);
        }else{
            parent.setPadding(searchPaddingLeft, searchPaddingTop, searchPaddingRight, searchPaddingBottom);
        }

        if (searchContentPadding != -1){
            et.setPadding(searchContentPadding, searchContentPadding, searchContentPadding, searchContentPadding);
        }else{
            et.setPadding(searchContentPaddingLeft, searchContentPaddingTop, searchContentPaddingRight, searchContentPaddingBottom);
        }

        ivClear.setVisibility(GONE);

        constraintSet = new ConstraintSet();
        constraintSet.clone(constraintLayout);

        initListener();
    }

    private void initListener() {
        et.setOnClickListener(v -> {
            if (bt.getVisibility() == View.GONE){ // 有个BUG，如果不加判断的话，在输入内容之后再次点击会隐藏清空按钮
                setState(et.getText().toString());
                changeLayout(true);
            }
        });
        constraintLayout.setOnClickListener(v -> {
            bt.setText(searchButtonEmptyTxt);
            changeLayout(true);
        });

        bt.setOnClickListener(v -> {
            if (onSearchListener != null) {
                String str = bt.getText().toString().trim();
                String searchContent = et.getText().toString().trim();
                onSearchListener.onClick(searchContent, str.equals(searchButtonTxt));
            }
            if (bt.getText().toString().equals(searchButtonEmptyTxt)) {
                changeLayout(false);
                et.setText("");
            } else {
                setSoftInput(false);
            }
        });

        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                setState(s.toString());
            }
        });

        addEditorActionListener();

        ivClear.setOnClickListener(v -> et.setText(""));

        setEditTextState(false);
    }

    private void setState(String content){
        if (TextUtils.isEmpty(content)) {
            bt.setText(searchButtonEmptyTxt);
            ivClear.setVisibility(View.GONE);
        } else {
            bt.setText(searchButtonTxt);
            ivClear.setVisibility(View.VISIBLE);
        }
    }

    // 添加键盘回车键监听器
    private void addEditorActionListener() {
        if (searchImeOption != -1){
            et.setOnEditorActionListener((v, actionId, event) -> {
                if (onEditorActionListener != null){
                    onEditorActionListener.onEditorAction(v,actionId,event);
                }
                return false;
            });
        }
    }

    // 设置输入框状态，canInput为true是输入框移至左侧，自动弹出键盘，否则反之
    public void changeLayout(boolean canInput) {
        TransitionManager.beginDelayedTransition(constraintLayout);
        parent.setLayoutTransition(canInput ? new LayoutTransition() : null);
        constraintSet.constrainWidth(R.id.et, canInput ? constraintLayout.getLayoutParams().width : LayoutParams.WRAP_CONTENT);
        constraintSet.applyTo(constraintLayout);

        setEditTextState(canInput);
    }

    private void setEditTextState(final boolean isFocusable){
        bt.setVisibility(isFocusable?View.VISIBLE:View.GONE);
        et.setFocusable(isFocusable);//可以通过键盘得到焦点
        et.setFocusableInTouchMode(isFocusable);//可以通过触摸得到焦点
        if (isFocusable){
            et.requestFocus();//获取焦点 光标出现
        }else{
            et.clearFocus();
        }

        setSoftInput(isFocusable);
    }

    // 设置键盘弹出、收起
    public void setSoftInput(final boolean isFocusable) {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                InputMethodManager m = (InputMethodManager)et.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (isFocusable) {
                    m.showSoftInput(et, InputMethodManager.SHOW_FORCED);
                } else {
                    m.hideSoftInputFromWindow(et.getWindowToken(), 0);
                }
            }
        }, 300);
    }

    public EditText getEditText(){
        return et;
    }

    // 设置键盘回车键事件
    public void setImeOptions(int imeOptions){
        et.setImeOptions(imeOptions);
        addEditorActionListener();
    }

    private OnSearchListener onSearchListener;// 右边按钮的点击监听
    public void setOnSearchListener(OnSearchListener onSearchListener) {
        this.onSearchListener = onSearchListener;
    }

    private OnEditorActionListener onEditorActionListener;// 键盘回车键
    public void setOnEditorActionListener(OnEditorActionListener onEditorActionListener){
        this.onEditorActionListener = onEditorActionListener;
    }

    public interface OnSearchListener {
        void onClick(String searchStr, boolean isSearch);
    }
    public interface OnEditorActionListener{
        boolean onEditorAction(TextView v, int actionId, KeyEvent event);
    }
}