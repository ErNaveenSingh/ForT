package com.tcs.iwms.TrackApplication;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.tcs.iwms.R;
import com.tcs.iwms.connection.ConnectionManager;
import com.tcs.iwms.connection.ConnectionURLConstants;
import com.tcs.iwms.connection.ResponseInterface;
import com.tcs.iwms.dashboard.DashboardResponse;
import com.tcs.iwms.worklist.WorklistPagerAdapter;
import com.tcs.iwms.worklist.requestorModule.RequestorIdBasedSearchFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by 878562 on 6/14/2016.
 */
public class TrackApplicationMother extends Fragment implements ResponseInterface
        {

    TabLayout tabLayout;
    ConnectionManager connectionManager;
    ProgressDialog progressdialog;
    TrackMotherPagerAdapter pagerAdapter;
    ViewPager pager;

    //tabs data list
    ArrayList<TrackApplicationBean> trackAppEXOP;
    ArrayList<TrackApplicationBean> trackAppProposed;
    ArrayList<TrackApplicationBean> trackAppShortlist;
    DashboardResponse trackResponseObject;
    //selected ID from filter screen
    int selectedFilterId =0;
    View parentView = null;

            int FRAGMENT_CODE =0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        Log.d("track","Hola! I am in onCreate of Track Mother Fragment ");
        //before adding actionbar set optionmenu true
        setHasOptionsMenu(true);


        parentView = inflater.inflate(R.layout.fragment_worklist_mother,container,false);

        pager= (ViewPager) parentView.findViewById(R.id.view_pager);

        tabLayout= (TabLayout) parentView.findViewById(R.id.tab_layout);

        connectionManager = new ConnectionManager(getActivity(),TrackApplicationMother.this);
        progressdialog = new ProgressDialog(getActivity());

        Bundle bundle = getArguments();
        String login = bundle.getString("LOGINID");
        Log.d("track","trackmother login inside mother frag,"+login);
        //create json request n sending for request
        JSONObject jsonObject =createTrackJSONRequest(login);
        fetchTrackApplicationResponse(ConnectionURLConstants.TRACK_APPLICATION_TABS,jsonObject);

        return parentView;

    }

    private JSONObject createTrackJSONRequest(String loginId)
    {
        JSONObject jsonObject =null;
        try {
            jsonObject = new JSONObject();
            jsonObject.put("loginid",loginId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("track", "request json" + jsonObject.toString());
        return jsonObject;
    }

    private void fetchTrackApplicationResponse(String requestUrl ,JSONObject requestJSON)
    {
        Log.d("track","request for server response"+requestUrl);
        progressdialog.setMessage("Creating tabs :)");
        progressdialog.show();
        connectionManager.getResponseThroughVolley(requestUrl, requestJSON);
    }


    @Override
    public void onResponseReceivedfromServer(String response) {
        if (progressdialog!=null)
            progressdialog.dismiss();

        Gson trackGson = new Gson();
        trackResponseObject = trackGson.fromJson(response,DashboardResponse.class);

        if (trackResponseObject!=null)
        {
            //response not null
            String loginId = trackResponseObject.getResult().getLoginid();
            Log.d("track","request loginId serverResponse"+loginId);

            trackAppEXOP = trackResponseObject.getResult()
                    .getTrackAppEXOP();

            trackAppProposed = trackResponseObject.getResult()
                    .getTrackAppProposed();

            trackAppShortlist = trackResponseObject.getResult()
                    .getTrackAppShortlist();

            Log.d("track","req tabs data size"+trackAppEXOP.size()+"2nd"+trackAppProposed.size()
                    +"3rd"+trackAppShortlist.size());


            //sending data Adapter
            pagerAdapter=new TrackMotherPagerAdapter(getChildFragmentManager(),
                    trackAppEXOP,
                    trackAppProposed,
                    trackAppShortlist);

            pager.setAdapter(pagerAdapter);
            tabLayout.setupWithViewPager(pager);
            tabLayout.setTabsFromPagerAdapter(pagerAdapter);

        }else
        {
            //server response is null
            //show alert n back to main dashboard
            Log.d("track","req server response null");
        }

    }

    @Override
    public void onErrorReceivedFromServer(VolleyError error) {
        if (progressdialog!=null)
            progressdialog.dismiss();

        Log.d("track", "request onErrorReceivedFromServer");

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.trackmotherfragment_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_filter:
                // Handle activity menu item
                TrackFilterFragment reqIdBasedFrag = new TrackFilterFragment();
                reqIdBasedFrag.setTargetFragment(this, FRAGMENT_CODE);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .add(R.id.home_fragment_container, reqIdBasedFrag, "TRACK_FILTER")
                        .addToBackStack(null)
                        .commit();

                return true;
            default:
                // Handle fragment menu items
                return super.onOptionsItemSelected(item);
        }
    }




    @Override
    public void onResume() {
        super.onResume();
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        ActionBar actionBar = activity.getSupportActionBar();
        actionBar.setTitle(ConnectionURLConstants.TRACK_APPLICATION_TITLE);
        actionBar.setDisplayHomeAsUpEnabled(false);
    }


    //filtering data set
    private ArrayList<TrackApplicationBean> filterData(int filterid
            ,ArrayList<TrackApplicationBean> parentArrayData)
    {

        ArrayList<TrackApplicationBean> modifiedData = new ArrayList<TrackApplicationBean>();

        for (TrackApplicationBean trackApplicationBean : parentArrayData)
        {
            if(trackApplicationBean.getStatusId()==filterid) {
                modifiedData.add(trackApplicationBean);
            }
        }

        Log.d("track ","filter modified" + modifiedData.size());
        return modifiedData;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d("track","trackmother destroyview");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("tracj", "onActivityResult" + "requestCode"+requestCode+"resultCode");

        if(requestCode == FRAGMENT_CODE && resultCode == Activity.RESULT_OK) {
            if(data != null) {
                int value = data.getIntExtra("KEY_SELECTED", 0);
                if(value != 0) {
                    Log.v("track", "Data passed from Child fragment = " + value);
                    if (trackAppEXOP!=null)
                    {
                        trackAppEXOP = filterData(value, trackAppEXOP);
                        Log.d("track ","filter new trackAppEXOP class"+trackAppEXOP.size());
                    }else
                    {
                        Log.d("track ","filter aync trackAppEXOP null");
                    }

                    if (trackAppProposed!=null)
                    {
                        this.trackAppProposed = filterData(value, trackAppProposed);
                        Log.d("track ","filter new trackAppProposed"+trackAppProposed.size());
                    }else
                    {
                        Log.d("track ","filter aync trackAppProposed null");
                    }

                    if (trackAppShortlist!=null)
                    {

                        this.trackAppShortlist = filterData(value, trackAppShortlist);
                        Log.d("track ","filter new trackAppShortlist"+trackAppShortlist.size());
                    }else
                    {
                        Log.d("track ","filter aync trackAppShortlist null");
                    }

                }

                //got all new array list
                Log.d("track", "Data After Filter - EXOP " + trackAppEXOP.size() + " 2nd" + trackAppProposed.size()
                        + " 3rd" + trackAppShortlist.size());

                /*pagerAdapter=new TrackMotherPagerAdapter(getChildFragmentManager(),
                        trackAppEXOPFiltered,
                        trackAppProposed,
                        trackAppShortlist);*/

                pagerAdapter.setFilteredData(trackAppEXOP,trackAppProposed);
                pagerAdapter.notifyDataSetChanged();

              /*  pager.setAdapter(pagerAdapter);
                tabLayout.setupWithViewPager(pager);
                tabLayout.setTabsFromPagerAdapter(pagerAdapter);*/

            }
        }

    }



}
