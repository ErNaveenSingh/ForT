package com.tcs.iwms.TrackApplication;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import com.tcs.iwms.connection.ConnectionURLConstants;
import com.tcs.iwms.dashboard.DashboardResponse;

import java.util.ArrayList;


/**
 * Created by 878562 on 6/14/2016.
 */
public class TrackMotherPagerAdapter extends FragmentPagerAdapter {

    //tabs data list
    ArrayList<TrackApplicationBean> trackAppEXOP;
    ArrayList<TrackApplicationBean> trackAppProposed;
    ArrayList<TrackApplicationBean> trackAppShortlist;

    EXOPFragment fragExopObj;
    ProposedFragment fragProposedObj;//,fragShorlistedObj;
    //new constructor
    public TrackMotherPagerAdapter(FragmentManager fragmentManager,
                                   ArrayList<TrackApplicationBean> childTrackAppEXOP,
                                   ArrayList<TrackApplicationBean> childTrackAppProposed,
                                   ArrayList<TrackApplicationBean> childTrackAppShortlist)
    {
        super(fragmentManager);

        this.trackAppEXOP = childTrackAppEXOP;
        this.trackAppProposed = childTrackAppProposed;
        this.trackAppShortlist = childTrackAppShortlist;
        Log.d("track","EXOP List Size in Adapter is"+trackAppEXOP.size());
    }



    /*public TrackMotherPagerAdapter(FragmentManager fm ,DashboardResponse trackModServerResponse) {
        super(fm);

        this.trackAppEXOP = trackModServerResponse.getResult().getTrackAppEXOP();
        this.trackAppProposed = trackModServerResponse.getResult().getTrackAppProposed();
        this.trackAppShortlist = trackModServerResponse.getResult().getTrackAppShortlist();

    }
*/
    @Override
    public CharSequence getPageTitle(int position) {
        //return super.getPageTitle(position);
        String tabTitle=null;
        switch (position)
        {
            case 0:
                tabTitle = ConnectionURLConstants.TRACKEXOP;
                break;
            case 1:
                tabTitle = ConnectionURLConstants.TRACK_PROPOSED;
                break;
            case 2:
                tabTitle = ConnectionURLConstants.TRACK_SHORTLISTED;
                break;
        }
        return tabTitle;
    }

    @Override
    public Fragment getItem(int position) {
        Log.d("track","req exop frag insdie getItem");
        Fragment fragmentObject = null;
        switch (position)
        {
            case 0:
                fragmentObject = new EXOPFragment();
                fragExopObj=(EXOPFragment)fragmentObject;
                Bundle bundle = new Bundle();
                Log.d("track", "track EXOPdata adapter EXOPdata" + trackAppEXOP.size());
                bundle.putParcelableArrayList("EXOP_DATA_LIST", trackAppEXOP);

                fragmentObject.setArguments(bundle);

                break;
            case 1:
                fragmentObject = new ProposedFragment();
                fragProposedObj= (ProposedFragment)fragmentObject;
                Bundle proposedbundle = new Bundle();
                proposedbundle.putParcelableArrayList("PROPOSED_DATA_LIST",trackAppProposed);

                fragmentObject.setArguments(proposedbundle);
                break;
            case 2:
                fragmentObject = new ShortListedFragment();
               // fragShorlistedObj=fragmentObject;
                Bundle listedbundle = new Bundle();
                listedbundle.putParcelableArrayList("SHORTLISTED_DATA_LIST",trackAppShortlist);

                fragmentObject.setArguments(listedbundle);
                break;
        }
        return fragmentObject;
    }

    @Override
    public int getCount() {
        return ConnectionURLConstants.TRACK_MODULE_TABS;
    }

    public void setFilteredData(ArrayList<TrackApplicationBean> trackAppEXOPfiltered,
                                ArrayList<TrackApplicationBean> trackAppProposedFiltered)
    {
        trackAppEXOP=trackAppEXOPfiltered;
        trackAppProposed = trackAppProposedFiltered;
        Log.d("track", "setFilteredData -Pager Adapter - EXOP " +trackAppEXOP.size());
        fragExopObj.setExopFilteredData(trackAppEXOP);
        fragProposedObj.setProposedFilteredData(trackAppProposedFiltered);
        notifyDataSetChanged();
    }


}
