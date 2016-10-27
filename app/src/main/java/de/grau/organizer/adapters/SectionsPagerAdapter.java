package de.grau.organizer.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import de.grau.organizer.fragments.ListFragment;
import de.grau.organizer.fragments.MonthFragment;
import de.grau.organizer.fragments.WeekFragment;

/**
 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {
    private boolean isSwipeEnabled = true;

    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        switch(position){
            case 0:
                return MonthFragment.newInstance("String1", "String2");
            case 1:
                return WeekFragment.newInstance("String1", "String2");
            case 2:
                 return ListFragment.newInstance("String1","String2");
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        // Show 3 total pages.
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "MONAT";
            case 1:
                return "WOCHE";
            case 2:
                return "LISTE";
        }
        return null;
    }



    private void disableSwipe(){
        this.isSwipeEnabled = false;
    }

    private void enableSwipe(){
        this.isSwipeEnabled = true;
    }
}
