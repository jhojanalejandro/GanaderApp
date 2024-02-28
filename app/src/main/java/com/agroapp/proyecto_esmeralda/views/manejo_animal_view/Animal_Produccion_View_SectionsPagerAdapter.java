package com.agroapp.proyecto_esmeralda.views.manejo_animal_view;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.agroapp.proyecto_esmeralda.R;


/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class Animal_Produccion_View_SectionsPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.tab_lote_01,R.string.tab_lote_02,R.string.tab_lote_03,R.string.tab_lote_04,R.string.tab_horras,R.string.tab_lote_c1,R.string.tab_lote_c2,R.string.tab_lote_c3,R.string.tab_lote_n1,R.string.tab_lote_n2,R.string.tab_lote_n3};
    private final Context mContext;

    public Animal_Produccion_View_SectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        return Animales_Production_View_Fragment.newInstance(position + 1);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        // Show 2 total pages.
        return 11;
    }
}