package com.wust.newsmartschool.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;

import com.wust.newsmartschool.R;


public class ClearEditText extends EditText implements
        View.OnFocusChangeListener, TextWatcher {
    /**
     * ���ゆ������寮���
     */
    private Drawable mClearDrawable;

    public ClearEditText(Context context) {
        this(context, null);
    }

    public ClearEditText(Context context, AttributeSet attrs) {
        //杩��������规�涔�寰���瑕�锛�涓���杩�涓�寰�澶�灞��т��藉��XML���㈠��涔�
        this(context, attrs, android.R.attr.editTextStyle);
    }

    public ClearEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }


    private void init() {
        //�峰��EditText��DrawableRight,��濡�娌℃��璁剧疆��浠�灏变娇�ㄩ�璁ょ���剧��
        mClearDrawable = getCompoundDrawables()[2];
        if (mClearDrawable == null) {
            mClearDrawable = getResources()
                    .getDrawable(R.drawable.emotionstore_progresscancelbtn);
        }
        mClearDrawable.setBounds(0, 0, mClearDrawable.getIntrinsicWidth(), mClearDrawable.getIntrinsicHeight());
        setClearIconVisible(false);
        setOnFocusChangeListener(this);
        addTextChangedListener(this);
    }


    /**
     * ��涓烘��浠�涓��界�存�ョ�EditText璁剧疆�瑰�讳�浠讹���浠ユ��浠��ㄨ�颁���浠���涓���浣�缃��ユā���瑰�讳�浠�
     * 褰���浠���涓���浣�缃� ��  EditText��瀹藉害 - �炬���版�т欢�宠竟���磋� - �炬����瀹藉害  ��
     * EditText��瀹藉害 - �炬���版�т欢�宠竟���磋�涔��存��浠�灏辩���瑰�讳��炬��锛�绔��存�瑰��娌℃������
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (getCompoundDrawables()[2] != null) {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                boolean touchable = event.getX() > (getWidth()
                        - getPaddingRight() - mClearDrawable.getIntrinsicWidth())
                        && (event.getX() < ((getWidth() - getPaddingRight())));
                if (touchable) {
                    this.setText("");
                }
            }
        }

        return super.onTouchEvent(event);
    }

    /**
     * 褰�ClearEditText���瑰�����������跺��锛��ゆ�����㈠��绗�涓查�垮害璁剧疆娓��ゅ�炬�����剧ず涓�����
     */
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            setClearIconVisible(getText().length() > 0);
        } else {
            setClearIconVisible(false);
        }
    }


    /**
     * 璁剧疆娓��ゅ�炬�����剧ず涓�����锛�璋���setCompoundDrawables涓�EditText缁��朵���
     * @param visible
     */
    protected void setClearIconVisible(boolean visible) {
        Drawable right = visible ? mClearDrawable : null;
        setCompoundDrawables(getCompoundDrawables()[0],
                getCompoundDrawables()[1], right, getCompoundDrawables()[3]);
    }


    /**
     * 褰�杈��ユ����㈠��瀹瑰�����������跺����璋����规�
     */
    @Override
    public void onTextChanged(CharSequence s, int start, int count,
                              int after) {
        setClearIconVisible(s.length() > 0);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count,
                                  int after) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }


    /**
     * 璁剧疆���ㄥ�ㄧ��
     */
    public void setShakeAnimation(){
        this.setAnimation(shakeAnimation(5));
    }


    /**
     * ���ㄥ�ㄧ��
     * @param counts 1绉������ㄥ�灏�涓�
     * @return
     */
    public static Animation shakeAnimation(int counts){
        Animation translateAnimation = new TranslateAnimation(0, 10, 0, 0);
        translateAnimation.setInterpolator(new CycleInterpolator(counts));
        translateAnimation.setDuration(1000);
        return translateAnimation;
    }


}

