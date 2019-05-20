package com.example.xueyetong.Fragments;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.xueyetong.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NewsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NewsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    Resources resources;
    private ViewPager mPager;
    private ArrayList<Fragment> fragmentsList;
    private ImageView ivBottomLine;
    private TextView tvTabNews, tvTabList;
    private int currentIndex = 0;
    private int bottomLineWidth;
    private int offset = 0;
    private int position_one;
    public final static int num = 2;
    Fragment news1;
    Fragment news2;

    private OnFragmentInteractionListener mListener;

    public NewsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NewsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NewsFragment newInstance(String param1, String param2) {
        NewsFragment fragment = new NewsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news,null);
        resources = getResources();
        InitWidth(view);
        InitTextView(view);
        InitViewPager(view);
        TranslateAnimation animation = new TranslateAnimation(position_one,offset,0,0);
        tvTabList.setTextColor(resources.getColor(R.color.m_black));
        animation.setFillAfter(true);
        animation.setDuration(300);
        ivBottomLine.startAnimation(animation);
        return view;
    }

    private void InitTextView(View parentView){
        tvTabNews = parentView.findViewById(R.id.news_tab_1);
        tvTabList = parentView.findViewById(R.id.news_tab_2);

        tvTabNews.setOnClickListener(new MyOnClickListener(0));
        tvTabList.setOnClickListener(new MyOnClickListener(1));
    }

    private void InitViewPager(View parentView){
        mPager = parentView.findViewById(R.id.vPager);
        fragmentsList = new ArrayList<Fragment>();
        news1 = new NewsFragment_1();
        news2 = new NewsFragment_2();
        fragmentsList.add(news1);
        fragmentsList.add(news2);
        mPager.setAdapter(new MyFragmentPagerAdapter(getChildFragmentManager(),fragmentsList));
        mPager.setOnPageChangeListener(new MyOnPageChangeListener());
        mPager.setCurrentItem(0);
    }

    private void InitWidth(View parentView){
        ivBottomLine = parentView.findViewById(R.id.iv_bottom_line);
        bottomLineWidth = ivBottomLine.getLayoutParams().width;
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenW = dm.widthPixels;
        offset = (int)((screenW / num - bottomLineWidth)/2);
        int avg = (int)(screenW / num);
        position_one = avg + offset;
    }

    public class MyOnClickListener implements View.OnClickListener{
        private int index = 0;
        public MyOnClickListener(int i){
            index = i;
        }

        @Override
        public void onClick(View v) {
            mPager.setCurrentItem(index);
        }
    }

    public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener{
        @Override
        public void onPageSelected(int i) {
            Animation animation = null;
            switch (i){
                case 0:
                    if(currentIndex == 1){
                        animation = new TranslateAnimation(position_one,offset,0,0);
                        tvTabList.setTextColor(resources.getColor(R.color.m_black));
                    }
                    tvTabNews.setTextColor(resources.getColor(R.color.colorPrimary));
                    break;
                case 1:
                    if (currentIndex == 0){
                        animation = new TranslateAnimation(offset,position_one,0,0);
                        tvTabNews.setTextColor(resources.getColor(R.color.m_black));
                    }
                    tvTabList.setTextColor(resources.getColor(R.color.colorPrimary));
                    break;
            }
            currentIndex = i;
            animation.setFillAfter(true);
            animation.setDuration(300);
            ivBottomLine.startAnimation(animation);
        }

        @Override
        public void onPageScrolled(int i, float v, int i1) {

        }

        @Override
        public void onPageScrollStateChanged(int i) {

        }
    }





    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
