package com.example.ehsan.homefood;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by ehsan on 17-08-2017.
 */

public class PagerAdapter extends FragmentPagerAdapter {

    List<Fragment> fragmentList=new LinkedList<>();
    List<String>text=new LinkedList<>();
    public PagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }
    @Override
    public CharSequence getPageTitle(int position)
    {
        return text.get(position);
    }
    public  void addFragment(Fragment fragment ,String st)
    {
        fragmentList.add(fragment);
        text.add(st);
    }
}
